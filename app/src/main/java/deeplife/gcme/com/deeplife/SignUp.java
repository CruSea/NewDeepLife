package deeplife.gcme.com.deeplife;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by bengeos on 12/18/16.
 */

public class SignUp extends AppCompatActivity {
    public static EditText UserFull,UserEmail,UserPhone,UserPass1,UserPass2;
    public static Spinner UserGender,UserCountry;
    public static Button SignUp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);
        UserFull = (EditText) findViewById(R.id.txt_signup_fullname);
        UserEmail = (EditText) findViewById(R.id.txt_signup_email);
        UserPhone = (EditText) findViewById(R.id.txt_signup_phone);
        UserPass1 = (EditText) findViewById(R.id.txt_signup_password1);
        UserPass2 = (EditText) findViewById(R.id.txt_signup_password1);
        UserGender = (Spinner) findViewById(R.id.spn_signup_gender);
        UserCountry = (Spinner) findViewById(R.id.spn_signup_country);
        SignUp = (Button) findViewById(R.id.btn_signup_signup);


    }

    public static void init(){

    }
}
