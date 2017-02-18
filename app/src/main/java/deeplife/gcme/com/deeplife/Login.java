package deeplife.gcme.com.deeplife;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import deeplife.gcme.com.deeplife.Adapters.CountryListAdapter;
import deeplife.gcme.com.deeplife.Adapters.LoginAccess;
import deeplife.gcme.com.deeplife.Models.Country;
import deeplife.gcme.com.deeplife.Models.User;

/**
 * Created by bengeos on 12/18/16.
 */

public class Login extends AppCompatActivity {
    public static Button Login, SignUp, Forgotten;
    public static Context myContext;
    private LoginAccess loginAccess;
    public static List<Country> myCountries;
    public static ProgressDialog progressDialog;
    public static Spinner mySpinner;
    public static EditText TextCode, UserName, UserPass;
    private static String LoginChoice;
    private static int CountryChoicePos = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        myContext = this;
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(R.string.app_name);
        myCountries = DeepLife.myDATABASE.getAllCountries();
        if (myCountries != null && myCountries.size() > 0) {

        } else {
            progressDialog.setMessage(getString(R.string.dlg_msg_downloading_files));
            progressDialog.show();
            loginAccess = new LoginAccess(new User());
            loginAccess.GetMetaData();
        }

        TextCode = (EditText) findViewById(R.id.txt_login_code);
        UserName = (EditText) findViewById(R.id.txt_login_username);
        UserPass = (EditText) findViewById(R.id.txt_login_userpass);
        SignUp = (Button) findViewById(R.id.btn_link_sign_up);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(deeplife.gcme.com.deeplife.Login.this, deeplife.gcme.com.deeplife.SignUp.class);
                startActivity(intent);
            }
        });

        mySpinner = (Spinner) findViewById(R.id.login_countries_spinner);
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextCode.setText("+" + myCountries.get(position).getCode());
                CountryChoicePos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (myCountries.size() > 0) {
                    TextCode.setText("+" + myCountries.get(0).getCode());
                }
            }
        });

        Login = (Button) findViewById(R.id.btn_login_login);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (myCountries == null || myCountries.size() == 0) {
                        myCountries = DeepLife.myDATABASE.getAllCountries();
                        UpdateView();
                    } else {
                        User user = new User();
                        if (UserName.getText().toString().contains("@")) {
                            user.setEmail(UserName.getText().toString());
                        } else {
                            user.setPhone("" + myCountries.get(mySpinner.getSelectedItemPosition()).getCode() + "" + UserName.getText().toString());
                        }
                        user.setPass(UserPass.getText().toString());
                        user.setCountry("" + myCountries.get(mySpinner.getSelectedItemPosition()).getSerID());
                        loginAccess = new LoginAccess(user);

                        progressDialog.setMessage(getString(R.string.dlg_msg_authenticating_user));
                        progressDialog.show();
                        loginAccess.LogInAuthnticate();
                    }
                } catch (Exception e) {

                }

            }
        });

        Forgotten = (Button) findViewById(R.id.btn_link_forgot_password);
        Forgotten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(DeepLife.FORGOTEN_URL);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        UpdateView();
    }

    public static void UpdateView() {
        myCountries = DeepLife.myDATABASE.getAllCountries();
        if (myCountries != null) {
            mySpinner.setAdapter(new CountryListAdapter(myContext, R.layout.login_countries_item, myCountries));
            if (CountryChoicePos > 0) {
                mySpinner.setSelection(CountryChoicePos);
            }
        }
    }

    public static void GetNextActivity() {
        Toast.makeText(myContext, R.string.toast_msg_login_successful, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(myContext, deeplife.gcme.com.deeplife.MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        myContext.startActivity(intent);
    }

    public static void DialogState(int state) {
        try {
            if (state == 0) {
                progressDialog.cancel();
                UpdateView();
            } else {
                progressDialog.show();
            }
        } catch (Exception e) {

        }

    }

    public static void showDialog(final String message) {
        try {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            UpdateView();
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

}
