package deeplife.gcme.com.deeplife;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import deeplife.gcme.com.deeplife.Adapters.CountryListAdapter;
import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.Models.Country;
import deeplife.gcme.com.deeplife.Models.SignUpUser;
import deeplife.gcme.com.deeplife.SyncService.SyncDatabase;
import deeplife.gcme.com.deeplife.SyncService.SyncService;
import kotlin.Pair;

/**
 * Created by bengeos on 12/18/16.
 */

public class SignUp extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "SignUp";
    private static final int MY_PERMISSION_ACCESS_COURSE_LOCATION = 1;

    private GoogleApiClient mGoogleApiClient;

    public static EditText UserFull, UserEmail, UserPhone, UserPass1, UserPass2, TextCode;
    public static Spinner UserGender, UserCountry;
    public static Context myContext;
    public static Button SignUp;
    public static SignUpUser NewUser;
    public static SyncDatabase mySyncDatabase;
    public static ProgressDialog progressDialog;
    private Gson myParser;
    public static int CountryPos;
    public static List<Country> myCountries;
    private AlertDialog.Builder builder;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword1, inputLayoutPassword2, inputLayoutPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);
        mySyncDatabase = new SyncDatabase();
        myContext = this;
        CountryPos = 0;
        myParser = new Gson();
        progressDialog = new ProgressDialog(myContext);
        myCountries = DeepLife.myDATABASE.getAllCountries();
        Init();

    }

    public void Init() {
        UserFull = (EditText) findViewById(R.id.txt_signup_fullname);
        UserEmail = (EditText) findViewById(R.id.txt_signup_email);
        UserPhone = (EditText) findViewById(R.id.txt_signup_phone);
        TextCode = (EditText) findViewById(R.id.txt_signup_code);
        UserPass1 = (EditText) findViewById(R.id.txt_signup_password1);
        UserPass2 = (EditText) findViewById(R.id.txt_signup_password2);
        UserGender = (Spinner) findViewById(R.id.spn_signup_gender);
        UserCountry = (Spinner) findViewById(R.id.spn_signup_country);
        UserCountry.setOnItemSelectedListener(this);
        SignUp = (Button) findViewById(R.id.btn_signup_signup);
        SignUp.setOnClickListener(this);

        inputLayoutName = (TextInputLayout) findViewById(R.id.signup_fullname);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.signup_email);
        inputLayoutPassword1 = (TextInputLayout) findViewById(R.id.signup_password1);
        inputLayoutPassword2 = (TextInputLayout) findViewById(R.id.signup_password2);
        inputLayoutPhone = (TextInputLayout) findViewById(R.id.signup_phone);

        UserFull.addTextChangedListener(new MyTextWatcher(UserFull));
        UserEmail.addTextChangedListener(new MyTextWatcher(UserEmail));
        UserPass1.addTextChangedListener(new MyTextWatcher(UserPass1));
        UserPass2.addTextChangedListener(new MyTextWatcher(UserPass2));

        if (myCountries != null && myCountries.size()>0) {
            UserCountry.setAdapter(new CountryListAdapter(this, R.layout.login_countries_item, myCountries));

            /*
            Locale locale;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = getResources().getConfiguration().getLocales().get(0);
            } else {
                locale = getResources().getConfiguration().locale;
            }
            String iso3Country = locale.getISO3Country();
            Log.d(TAG, "Signup Init: iso3Country: " + iso3Country);

            int pos = DeepLife.myDATABASE.getCountryByISO3(iso3Country).getID();
            UserCountry.setSelection(pos - 1);
            */
        }

        // Ask for Permission if we don't have it already
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_ACCESS_COURSE_LOCATION);
        }

        // Make sure we have permission before setting up the Google API Client
        //if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Create an instance of GoogleAPIClient.
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }
        }
    }

    protected void onStart() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        super.onStart();
    }

    protected void onStop() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Country country = Country.getCountryFromGeoLocation(this, mGoogleApiClient);
        if (country != null) {
            int pos = country.getID();
            UserCountry.setSelection(pos - 1);
        } else {
            UserCountry.setSelection(0);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_ACCESS_COURSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted!

                    // Create an instance of GoogleAPIClient.
                    if (mGoogleApiClient == null) {
                        mGoogleApiClient = new GoogleApiClient.Builder(this)
                                .addConnectionCallbacks(this)
                                .addOnConnectionFailedListener(this)
                                .addApi(LocationServices.API)
                                .build();
                    }

                    // Now we can connect to Google API Client
                    if (mGoogleApiClient != null) {
                        mGoogleApiClient.connect();
                    }

                    Country country = Country.getCountryFromGeoLocation(this, mGoogleApiClient);
                    if (country != null) {
                        int pos = country.getID();
                        UserCountry.setSelection(pos - 1);
                    } else {
                        UserCountry.setSelection(0);
                    }

                } else {
                    // Permission denied :-(
                }
                return;
            }
        }
    }

    public void ShowDialog(String message) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        break;
                }
            }
        };
        builder = new AlertDialog.Builder(myContext);
        builder.setTitle(R.string.app_name)
                .setMessage(message)
                .setPositiveButton(R.string.dlg_btn_ok, dialogClickListener).show();
    }

    private void requestSignUp(final SignUpUser signupUser){
        progressDialog.setTitle(R.string.app_name);
        progressDialog.setMessage(getString(R.string.msg_authenticating_account));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ArrayList<SignUpUser> user = new ArrayList<SignUpUser>();
        user.add(signupUser);
        List<Pair<String, String>> Send_Param;
        Send_Param = new ArrayList<Pair<String, String>>();
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.USER_NAME.toString(), signupUser.getUser_Email()));
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.USER_PASS.toString(), signupUser.getUser_Pass()));
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.COUNTRY.toString(), signupUser.getUser_Country()));
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.SERVICE.toString(), SyncService.API_SERVICE.SIGN_UP.toString()));
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.PARAM.toString(), myParser.toJson(user)));
        Fuel.post(DeepLife.API_URL, Send_Param).responseString(new Handler<String>() {
            @Override
            public void success(Request request, Response response, String s) {
                progressDialog.cancel();
                try {
                    Log.i(TAG, "Server Request -> \n" + request.toString());
                    Log.i(TAG, "Server Response -> \n" + s);
                    JSONObject myObject = (JSONObject) new JSONTokener(s).nextValue();

                    if (!myObject.isNull(SyncDatabase.ApiResponseKey.RESPONSE.toString())) {
                        mySyncDatabase.ProcessResponse(s);
                        DeepLife.myDATABASE.Delete_All(Database.Table_DISCIPLES);
                        DeepLife.myDATABASE.Delete_All(Database.Table_SCHEDULES);
                        DeepLife.myDATABASE.Delete_All(Database.Table_LOGS);
                        DeepLife.myDATABASE.Delete_All(Database.Table_USER);


                        ContentValues cv = new ContentValues();
                        cv.put(Database.UserColumn.FULL_NAME.toString(), signupUser.getUser_Name());
                        cv.put(Database.UserColumn.EMAIL.toString(), signupUser.getUser_Email());
                        cv.put(Database.UserColumn.PHONE.toString(), signupUser.getUser_Phone());
                        cv.put(Database.UserColumn.PASSWORD.toString(), signupUser.getUser_Pass());
                        cv.put(Database.UserColumn.COUNTRY.toString(), signupUser.getUser_Country());
                        cv.put(Database.UserColumn.GENDER.toString(), signupUser.getUser_Gender());
                        cv.put(Database.UserColumn.PICTURE.toString(), "");
                        cv.put(Database.UserColumn.FAVORITE_SCRIPTURE.toString(), "");

                        long x = DeepLife.myDATABASE.insert(Database.Table_USER, cv);
                        Log.i(TAG, "Main User Adding-> " + x);

                        Intent register = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(register);
                        finish();
                    } else {
                        ShowDialog(getString(R.string.dlg_msg_invalid_user_acct));
                    }

                } catch (Exception e) {
                    ShowDialog(getString(R.string.dlg_msg_something_wrong_error, e.toString()));
                    Log.i(TAG, "Error Occurred-> \n" + e.toString());
                }

            }

            @Override
            public void failure(Request request, Response response, FuelError fuelError) {
                Log.i(TAG, "Server Response -> \n" + response.toString());
                progressDialog.cancel();
                ShowDialog(getString(R.string.dlg_msg_authentication_failed));
            }
        });
    }
    private void submitForm() {
        if (!validateName()) {
            return;
        }
        if (!validateEmail()) {
            return;
        }
        if (!validatePhone()) {
            return;
        }
        if (!validatePassword1()) {
            return;
        }
        if (!validatePassword2()) {
            return;
        }

        NewUser = new SignUpUser();
        NewUser.setUser_Name(UserFull.getText().toString());
        NewUser.setUser_Pass(UserPass1.getText().toString());
        NewUser.setUser_Country("" + myCountries.get(CountryPos).getSerID());
        NewUser.setUser_Phone("" + myCountries.get(CountryPos).getCode() + UserPhone.getText().toString());
        NewUser.setUser_Email(UserEmail.getText().toString());
        NewUser.setUser_Gender(UserGender.getSelectedItem().toString());
        requestSignUp(NewUser);
    }

    private boolean validateName() {
        if (UserFull.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(UserFull);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = UserEmail.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(UserEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePhone() {
        String phone = UserPhone.getText().toString().trim();
        if (phone.isEmpty() || phone.length() < 8) {
            inputLayoutPhone.setError(getString(R.string.err_msg_phone));
            requestFocus(UserPhone);
            return false;
        } else {
            inputLayoutPhone.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword1() {
        if (UserPass1.getText().toString().trim().isEmpty() || UserPass1.getText().toString().length() < 7) {
            inputLayoutPassword1.setError(getString(R.string.err_msg_password1));
            inputLayoutPassword2.setError(getString(R.string.err_msg_password2));
            requestFocus(UserPass1);
            return false;
        } else {
            inputLayoutPassword1.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword2() {
        if (UserPass2.getText().toString().trim().isEmpty() || UserPass2.getText().toString().length() < 7 || !UserPass1.getText().toString().equals(UserPass2.getText().toString())) {
            inputLayoutPassword1.setError(getString(R.string.err_msg_password1));
            inputLayoutPassword2.setError(getString(R.string.err_msg_password2));
            requestFocus(UserPass2);
            return false;
        } else {
            inputLayoutPassword2.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == SignUp.getId()){
            submitForm();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(myCountries != null && myCountries.size()>0){
            String str = "+" + myCountries.get(position).getCode();
            TextCode.setText(str);
            CountryPos = position;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if (myCountries != null && myCountries.size() > 0) {
            String str = "+" + myCountries.get(0).getCode();
            TextCode.setText(str);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.txt_signup_fullname:
                    validateName();
                    break;
                case R.id.txt_signup_email:
                    validateEmail();
                    break;
                case R.id.txt_signup_phone:
                    validatePhone();
                    break;
                case R.id.txt_signup_password1:
                    validatePassword1();
                    break;
                case R.id.txt_signup_password2:
                    validatePassword2();
                    break;
            }
        }
    }
}
