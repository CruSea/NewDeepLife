package deeplife.gcme.com.deeplife;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import deeplife.gcme.com.deeplife.Adapters.CountryListAdapter;
import deeplife.gcme.com.deeplife.Models.Country;
import deeplife.gcme.com.deeplife.Models.User;
import deeplife.gcme.com.deeplife.SyncService.SyncDatabase;
import deeplife.gcme.com.deeplife.SyncService.SyncService;
import kotlin.Pair;

/**
 * Created by bengeos on 12/18/16.
 */

public class Login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static String TAG = "Login";
    private static final int MY_PERMISSION_ACCESS_COURSE_LOCATION = 1;

    private GoogleApiClient mGoogleApiClient;

    public static Button Login, SignUp, Forgotten;
    public static Context myContext;
    private SyncDatabase mySyncDatabase;
    public static List<Country> myCountries;
    public static ProgressDialog progressDialog;
    public static Spinner UserCountry;
    public static EditText TextCode, userEmail, userPass, userPhone;
    private TextView loginEmail,loginPhone;
    private LinearLayout userPhoneLayout,userEmailLayout;
    private TextInputLayout inputLayoutEmail, inputLayoutPhone, inputLayoutPhoneCode, inputLayoutPassword;
    private static boolean isEmailLogin = true;
    //private static int CountryChoicePos = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        myContext = this;
        mySyncDatabase = new SyncDatabase();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(R.string.app_name);

        inputLayoutEmail = (TextInputLayout) findViewById(R.id.login_input_email);
        inputLayoutPhone = (TextInputLayout) findViewById(R.id.login_input_phone);
        inputLayoutPhoneCode = (TextInputLayout) findViewById(R.id.login_input_phonecode);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.login_input_password);
        loginPhone = (TextView) findViewById(R.id.txt_login_phone);
        loginPhone.setOnClickListener(this);
        loginEmail = (TextView) findViewById(R.id.txt_login_email);
        loginEmail.setOnClickListener(this);
        userPhoneLayout = (LinearLayout) findViewById(R.id.lyt_user_phone);
        userPhoneLayout.setVisibility(View.GONE);
        userEmailLayout = (LinearLayout) findViewById(R.id.lyt_user_email);
        TextCode = (EditText) findViewById(R.id.txt_login_code);
        userEmail = (EditText) findViewById(R.id.txt_login_useremail);
        userPhone = (EditText) findViewById(R.id.txt_login_userphone);
        userPass = (EditText) findViewById(R.id.txt_login_userpass);

        SignUp = (Button) findViewById(R.id.btn_link_sign_up);
        SignUp.setOnClickListener(this);
        Login = (Button) findViewById(R.id.btn_login_login);
        Login.setOnClickListener(this);
        Forgotten = (Button) findViewById(R.id.btn_link_forgot_password);
        Forgotten.setOnClickListener(this);
        UserCountry = (Spinner) findViewById(R.id.login_countries_spinner);
        UserCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextCode.setText("+" + myCountries.get(position).getCode());
                //CountryChoicePos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (myCountries.size() > 0) {
                    TextCode.setText("+" + myCountries.get(0).getCode());
                }
            }
        });
        myCountries = DeepLife.myDATABASE.getAllCountries();
        if (myCountries != null && myCountries.size() > 0) {
            UserCountry.setAdapter(new CountryListAdapter(myContext, R.layout.login_countries_item, myCountries));
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

    public static void GetNextActivity() {
        Toast.makeText(myContext, R.string.toast_msg_login_successful, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(myContext, deeplife.gcme.com.deeplife.MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        myContext.startActivity(intent);
    }
    public static void showDialog(final String message) {
        try {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
//                            UpdateView();
                            break;
                    }
                }
            };
            android.app.AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
            builder.setMessage(message)
                    .setPositiveButton(R.string.dlg_btn_ok, dialogClickListener)
                    .show();
        } catch (Exception e) {

        }

    }
    public void getPhoneLoginPage(){
        if (myCountries != null && myCountries.size() > 0) {
            isEmailLogin = false;
            userPhoneLayout.setVisibility(View.VISIBLE);
            userEmailLayout.setVisibility(View.GONE);
            //UserCountry.setAdapter(new CountryListAdapter(myContext, R.layout.login_countries_item, myCountries));
            //UserCountry.setSelection(CountryChoicePos);
        }else {
            myCountries = DeepLife.myDATABASE.getAllCountries();
            if(myCountries != null && myCountries.size()>0){
                isEmailLogin = false;
                userPhoneLayout.setVisibility(View.VISIBLE);
                userEmailLayout.setVisibility(View.GONE);
                //UserCountry.setAdapter(new CountryListAdapter(myContext, R.layout.login_countries_item, myCountries));
                //UserCountry.setSelection(CountryChoicePos);
            }else {
                metaDataRequest();
            }
        }
        userEmail.setText("");
        userPhone.setText("");
        userPass.setText("");
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == SignUp.getId()){
            if(DeepLife.myDATABASE.getAllCountries()!=null) {
                if (DeepLife.myDATABASE.getAllCountries().size() > 0) {
                    Intent intent = new Intent(deeplife.gcme.com.deeplife.Login.this, deeplife.gcme.com.deeplife.SignUp.class);
                    startActivity(intent);
                }
            }
             else {
                metaDataRequest();
            }

        }else if(v.getId() == loginEmail.getId()){
            isEmailLogin = true;
            userPhoneLayout.setVisibility(View.GONE);
            userEmailLayout.setVisibility(View.VISIBLE);
        }else if(v.getId() == loginPhone.getId()){
            getPhoneLoginPage();
        }else if(v.getId() == Login.getId()){
            if(isEmailLogin){
                submitEmailLoginForm();
            }else {
                submitPhoneLoginForm();
            }
        }else if(v.getId() == Forgotten.getId()){
            Uri uri = Uri.parse(DeepLife.FORGOTEN_URL);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
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
                case R.id.txt_login_useremail:
                    validateUserEmail();
                    break;
                case R.id.txt_login_userpass:
                    validateUserPassword();
                    break;
                case R.id.txt_login_userphone:
                    validateUserPhone();
                    break;
            }
        }
    }

    private boolean validateUserPhone() {
        String phone = userPhone.getText().toString().trim();
        if (phone.isEmpty() || phone.length() < 8) {
            inputLayoutPhone.setError(getString(R.string.err_msg_phone));
            requestFocus(userPhone);
            return false;
        } else {
            inputLayoutPhone.setErrorEnabled(false);
            inputLayoutPhoneCode.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateUserPassword() {
        String password = userPass.getText().toString().trim();
        if (password.isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password1));
            requestFocus(userPass);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateUserEmail() {
        String email = userEmail.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(userEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
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
    private void submitEmailLoginForm() {
        if (!validateUserEmail()) {
            return;
        }
        if (!validateUserPassword()) {
            return;
        }
        User user = new User();
        user.setEmail(userEmail.getText().toString());
        user.setCountry("000");
        user.setPass(userPass.getText().toString());
        loginRequest(user);

    }
    private void submitPhoneLoginForm() {
        if (!validateUserPhone()) {
            return;
        }
        if (!validateUserPassword()) {
            return;
        }
        User user = new User();
        String str1 = myCountries.get(UserCountry.getSelectedItemPosition()).getCode()+""+userPhone.getText().toString();
        String str2 = ""+myCountries.get(UserCountry.getSelectedItemPosition()).getSerID();
        user.setPhone(str1);
        user.setCountry(str2);
        user.setPass(userPass.getText().toString());
        loginRequest(user);

    }
    public void metaDataRequest(){
        List<Pair<String, String>> Send_Param;
        Send_Param = new ArrayList<Pair<String, String>>();
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.USER_NAME.toString(), ""));
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.USER_PASS.toString(), ""));
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.COUNTRY.toString(), ""));
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.SERVICE.toString(), SyncService.API_SERVICE.META_DATA.toString()));
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.PARAM.toString(), "[]"));
        Log.i(TAG, "Request Sent to Server (Meta Data): " + Send_Param.toString());
        progressDialog.setCancelable(false);
        progressDialog.setTitle(getString(R.string.dlg_msg_downloading_files));
        progressDialog.show();
        try {
            Fuel.post(DeepLife.API_URL, Send_Param).responseString(new Handler<String>() {
                @Override
                public void success(@NotNull Request request, @NotNull Response response, String s) {
                    progressDialog.cancel();
                    Log.i(TAG, "Server Request (Meta Data): " + request.toString());
                    Log.i(TAG, "Server Response (Meta Data): " + s);
                    JSONObject myObject = null;
                    try {
                        myObject = (JSONObject) new JSONTokener(s).nextValue();
                        if (!myObject.isNull(SyncDatabase.ApiResponseKey.RESPONSE.toString())) {
                            mySyncDatabase.ProcessResponse(s);
                            getPhoneLoginPage();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void failure(@NotNull Request request, @NotNull Response response, @NotNull FuelError fuelError) {
                    Log.i(TAG, "Server Response Error Meta data-> \n" + fuelError);
                    progressDialog.cancel();
                }
            });
        } catch (Exception e) {
            Log.i(TAG, "Fuel try catch error -> \n" + e.toString());
            progressDialog.cancel();
        }
    }
    public void loginRequest(final User loginUser){
        List<Pair<String, String>> Send_Param;
        Send_Param = new ArrayList<Pair<String, String>>();
        if (loginUser.getEmail() != null) {
            Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.USER_NAME.toString(), loginUser.getEmail()));
        } else {
            Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.USER_NAME.toString(), loginUser.getPhone()));
        }
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.USER_PASS.toString(), loginUser.getPass()));
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.COUNTRY.toString(), loginUser.getCountry()));
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.SERVICE.toString(), SyncService.API_SERVICE.LOG_IN.toString()));
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.PARAM.toString(), "[]"));

        progressDialog.setCancelable(false);
        progressDialog.setTitle(getString(R.string.dlg_msg_authenticating_user));
        progressDialog.show();
        try {
            Log.i(TAG, "Request URL:->" + DeepLife.API_URL);
            Log.i(TAG, "Request:->" + Send_Param);
            Fuel.post(DeepLife.API_URL, Send_Param).responseString(new Handler<String>() {
                @Override
                public void success(@NotNull Request request, @NotNull Response response, String s) {
                    progressDialog.cancel();
                    Log.i(TAG, "Server Request  -> \n" + request.toString());
                    Log.i(TAG, "Server Response -> \n" + s);
                    JSONObject myObject = null;
                    try {
                        myObject = (JSONObject) new JSONTokener(s).nextValue();
                        if (!myObject.isNull(SyncDatabase.ApiResponseKey.RESPONSE.toString())) {
                            mySyncDatabase.ProcessResponse(s);
                            User user = DeepLife.myDATABASE.getMainUser();
                            if (user != null) {
                                user.setPass(loginUser.getPass());
                                long state = DeepLife.myDATABASE.updateMainUser(user);
                                User myUser1 = DeepLife.myDATABASE.getMainUser();
                                if (myUser1 != null && state >0) {
                                    GetNextActivity();
                                } else {
                                    Log.w(TAG, "LogInAuthenticate onSuccess(): unable to update Main User");
                                    showDialog(DeepLife.getContext().getString(R.string.dlg_msg_login_failure));
                                }
                            } else {
                                Log.w(TAG, "LogInAuthenticate onSuccess(): failed to get main user");
                                showDialog(DeepLife.getContext().getString(R.string.dlg_msg_login_failure));
                            }
                        } else {
                            Log.w(TAG, "LogInAuthenticate onSuccess(): JSONObject 'Response' is null");
                            showDialog(DeepLife.getContext().getString(R.string.dlg_msg_login_failure));
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "LogInAuthenticate onSuccess(): JSONException");
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(@NotNull Request request, @NotNull Response response, @NotNull FuelError fuelError) {
                    Log.i(TAG, "Server Response Error-> \n" + fuelError);
                    progressDialog.cancel();
                    showDialog(DeepLife.getContext().getString(R.string.dlg_msg_login_failure));
                }
            });
        } catch (Exception e) {
            Log.i(TAG, "Fuel try catch error -> \n" + e.toString());
            progressDialog.cancel();
            showDialog(DeepLife.getContext().getString(R.string.dlg_msg_login_failure));
        }
    }
}
