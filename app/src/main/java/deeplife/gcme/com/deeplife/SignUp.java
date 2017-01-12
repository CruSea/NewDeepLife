package deeplife.gcme.com.deeplife;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
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
import android.widget.Toast;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import deeplife.gcme.com.deeplife.Adapters.CountryListAdapter;
import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.Models.Country;
import deeplife.gcme.com.deeplife.Models.User;
import deeplife.gcme.com.deeplife.SyncService.SyncDatabase;
import deeplife.gcme.com.deeplife.SyncService.SyncService;
import kotlin.Pair;

/**
 * Created by bengeos on 12/18/16.
 */

public class SignUp extends AppCompatActivity {
    private static final String TAG = "SignUp";
    public static EditText UserFull,UserEmail,UserPhone,UserPass1,UserPass2,TextCode;
    public static Spinner UserGender,UserCountry;
    public static Context myContext;
    public static Button SignUp;
    public static User NewUser;
    public static SyncDatabase mySyncDatabase;
    private Gson myParser;
    public static int CountryPos;
    public static List<Country> myCountries;
    private  AlertDialog.Builder builder;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword1,inputLayoutPassword2,inputLayoutPhone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);
        myContext = this;
        CountryPos = 0;
        myParser = new Gson();

        Init();

    }

    public void Init() {
        mySyncDatabase = new SyncDatabase();

        UserFull = (EditText) findViewById(R.id.txt_signup_fullname);
        UserEmail = (EditText) findViewById(R.id.txt_signup_email);
        UserPhone = (EditText) findViewById(R.id.txt_signup_phone);
        TextCode = (EditText) findViewById(R.id.txt_signup_code);
        UserPass1 = (EditText) findViewById(R.id.txt_signup_password1);
        UserPass2 = (EditText) findViewById(R.id.txt_signup_password1);
        UserGender = (Spinner) findViewById(R.id.spn_signup_gender);
        UserCountry = (Spinner) findViewById(R.id.spn_signup_country);
        SignUp = (Button) findViewById(R.id.btn_signup_signup);

        inputLayoutName = (TextInputLayout) findViewById(R.id.signup_fullname);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.signup_email);
        inputLayoutPassword1 = (TextInputLayout) findViewById(R.id.signup_password1);
        inputLayoutPassword2 = (TextInputLayout) findViewById(R.id.signup_password2);
        inputLayoutPhone = (TextInputLayout) findViewById(R.id.signup_phone);

        UserFull.addTextChangedListener(new MyTextWatcher(UserFull));
        UserEmail.addTextChangedListener(new MyTextWatcher(UserEmail));
        UserPass1.addTextChangedListener(new MyTextWatcher(UserPass1));
        UserPass2.addTextChangedListener(new MyTextWatcher(UserPass2));

        myCountries = DeepLife.myDATABASE.getAllCountries();
        if(myCountries != null){
            UserCountry.setAdapter(new CountryListAdapter(this,R.layout.login_countries_item,myCountries));
        }
        UserCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextCode.setText("+"+myCountries.get(position).getCode());
                CountryPos = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if(myCountries.size()>0){
                    TextCode.setText("+"+myCountries.get(0).getCode());
                }
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

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

        NewUser = new User();
        NewUser.setUser_Name(UserFull.getText().toString());
        NewUser.setUser_Pass(UserPass1.getText().toString());
        NewUser.setUser_Country(""+myCountries.get(CountryPos).getSerID());
        NewUser.setUser_Phone(""+myCountries.get(CountryPos).getCode()+UserPhone.getText().toString());
        NewUser.setUser_Email(UserEmail.getText().toString());
        NewUser.setUser_Gender(UserGender.getSelectedItem().toString());


        final ProgressDialog myDialog = new ProgressDialog(this);
        myDialog.setTitle(R.string.app_name);
        myDialog.setMessage(getString(R.string.msg_authenticating_account));
        myDialog.show();
        ArrayList<User> user = new ArrayList<User>();
        user.add(NewUser);
        List<Pair<String, String>> Send_Param;
        Send_Param = new ArrayList<Pair<String, String>>();
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.ApiRequest.USER_NAME.toString(), NewUser.getUser_Phone()));
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.ApiRequest.USER_PASS.toString(), NewUser.getUser_Pass()));
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.ApiRequest.COUNTRY.toString(), NewUser.getUser_Country()));
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.ApiRequest.SERVICE.toString(), SyncService.ApiService.SIGN_UP.toString()));
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.ApiRequest.PARAM.toString(), myParser.toJson(user)));
        Fuel.post(DeepLife.API_URL, Send_Param).responseString(new Handler<String>() {
            @Override
            public void success(Request request, Response response, String s) {
                myDialog.cancel();
                try {
                    Log.i(TAG, "Server Request -> \n" + request.toString());
                    Log.i(TAG, "Server Response -> \n" + s);
                    JSONObject myObject = (JSONObject) new JSONTokener(s).nextValue();

                    if (!myObject.isNull("Response")) {
                        mySyncDatabase.ProcessResponse(s);

                        DeepLife.myDATABASE.Delete_All(Database.Table_DISCIPLES);
                        DeepLife.myDATABASE.Delete_All(Database.Table_SCHEDULES);
                        DeepLife.myDATABASE.Delete_All(Database.Table_LOGS);
                        DeepLife.myDATABASE.Delete_All(Database.Table_USER);

                        ContentValues cv = new ContentValues();
                        /*
                        // briggsm: Below indexes seems wrong
                        cv.put(Database.USER_FIELDS[0],NewUser.getUser_Name());
                        cv.put(Database.USER_FIELDS[1],NewUser.getUser_Email());
                        cv.put(Database.USER_FIELDS[2],NewUser.getUser_Phone());
                        cv.put(Database.USER_FIELDS[3],NewUser.getUser_Pass());
                        cv.put(Database.USER_FIELDS[4], NewUser.getUser_Country());
                        cv.put(Database.USER_FIELDS[5],"");
                        cv.put(Database.USER_FIELDS[6],"");
                        */
                        cv.put(Database.UserColumn.FULL_NAME.toString(),NewUser.getUser_Name());
                        cv.put(Database.UserColumn.EMAIL.toString(),NewUser.getUser_Email());
                        cv.put(Database.UserColumn.PHONE.toString(),NewUser.getUser_Phone());
                        cv.put(Database.UserColumn.PASSWORD.toString(),NewUser.getUser_Pass());
                        cv.put(Database.UserColumn.COUNTRY.toString(), NewUser.getUser_Country());
                        cv.put(Database.UserColumn.PICTURE.toString(),"");
                        cv.put(Database.UserColumn.FAVORITE_SCRIPTURE.toString(),"");

                        long x = DeepLife.myDATABASE.insert(Database.Table_USER, cv);
                        Log.i(TAG, "Main User Adding-> " + x);

                        Intent register = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(register);
                        finish();

                    } else {
                        ShowDialog(getString(R.string.dlg_msg_invalid_user_acct));
                    }
                } catch (Exception e) {
                    //ShowDialog("Something went wrong! Please try again!\nError: "+e.toString());
                    ShowDialog(getString(R.string.dlg_msg_something_wrong_error, e.toString()));
                    Log.i(TAG, "Error Occurred-> \n" + e.toString());
                }

            }
            @Override
            public void failure(Request request, Response response, FuelError fuelError) {
                Log.i(TAG, "Server Response -> \n" + response.toString());
                myDialog.cancel();
                ShowDialog(getString(R.string.dlg_msg_authentication_failed));
            }
        });

        Toast.makeText(getApplicationContext(), R.string.thank_you, Toast.LENGTH_SHORT).show();
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
        if (UserPass1.getText().toString().trim().isEmpty() || UserPass1.getText().toString().length()<7) {
            inputLayoutPassword1.setError(getString(R.string.err_msg_password1));
            requestFocus(UserPass1);
            return false;
        } else {
            inputLayoutPassword1.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validatePassword2() {
        if (UserPass2.getText().toString().trim().isEmpty() || UserPass2.getText().toString().length()<7 || !UserPass1.getText().toString().equals(UserPass2.getText().toString())) {
            inputLayoutPassword1.setError(getString(R.string.err_msg_password2));
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
