package deeplife.gcme.com.deeplife;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
    public static Button Login,SignUp,Forgotten;
    public static Context myContext;
    private LoginAccess loginAccess;
    public static List<Country> myCountries;
    public static ProgressDialog PROGRESS_DIALOG;
    public static Spinner mySpinner;
    public static EditText TextCode,UserName,UserPass;
    private static String LoginChoice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        myContext = this;
        PROGRESS_DIALOG = new ProgressDialog(this);
        PROGRESS_DIALOG.setTitle(R.string.app_name);
        myCountries = DeepLife.myDATABASE.getAllCountries();
        if(myCountries.size()>0){

        }else {
            PROGRESS_DIALOG.setMessage("Downloading Necessary Files ....");
            PROGRESS_DIALOG.show();
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
                if(position == 0){
                    TextCode.setText("Email");
                }else {
                    TextCode.setText("+"+myCountries.get(position).getCode());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Login = (Button) findViewById(R.id.btn_login_login);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setUserPhone(UserName.getText().toString());
                user.setUserPass(UserPass.getText().toString());
                user.setUserCountry(""+myCountries.get(mySpinner.getSelectedItemPosition()).getSerID());
                loginAccess = new LoginAccess(user);
                PROGRESS_DIALOG.setMessage("Authenticating the user ....");
                PROGRESS_DIALOG.show();
                loginAccess.LogInAuthnticate();
            }
        });

        UpdateView();
    }
    public static void UpdateView(){
        myCountries = DeepLife.myDATABASE.getAllCountries();
        mySpinner.setAdapter(new CountryListAdapter(myContext,R.layout.login_countries_item,myCountries));
    }
    public static void GetNextActivity(){
        Toast.makeText(myContext,"Login Successful",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(myContext, deeplife.gcme.com.deeplife.MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        myContext.startActivity(intent);
    }
    public static void DialogState(int state){
        if(state == 0){
            PROGRESS_DIALOG.cancel();
            UpdateView();
        }else{
            PROGRESS_DIALOG.show();
        }
    }
    public static void showDialog(final String message) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        break;
                }
            }
        };
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
        builder.setMessage(message)
                .setPositiveButton("ok ", dialogClickListener)
                .show();
    }

}
