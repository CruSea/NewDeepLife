package deeplife.gcme.com.deeplife.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Models.Country;
import deeplife.gcme.com.deeplife.Models.User;
import deeplife.gcme.com.deeplife.R;

/**
 * Created by briggsm on 12/20/16.
 */

public class ProfileShowActivity extends AppCompatActivity {
    private EditText FullName,Email,Country,Phone,Gender;
    private TextView DisplayName;
    private User myUser;
    Button editBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        myUser = DeepLife.myDATABASE.getMainUser();
        Init();
    }

    public void Init(){
        DisplayName = (TextView) findViewById(R.id.txt_disciple_profile_displayname);
        FullName = (EditText) findViewById(R.id.txt_disciple_profile_fullname);
        Email = (EditText) findViewById(R.id.txt_disciple_profile_email);
        Country = (EditText) findViewById(R.id.txt_disciple_profile_country);
        Phone = (EditText) findViewById(R.id.txt_disciple_profile_phone);
        Gender = (EditText) findViewById(R.id.txt_disciple_profile_gender);

        DisplayName.setText(myUser.getFull_Name());
        DisplayName.setEnabled(false);

        FullName.setText(myUser.getFull_Name());
        FullName.setEnabled(false);

        Email.setText(myUser.getUser_Email());
        Email.setEnabled(false);

        deeplife.gcme.com.deeplife.Models.Country country = DeepLife.myDATABASE.getCountryByID(Integer.valueOf(myUser.getUser_Country()));
        Country.setText(country.getName());
        Country.setEnabled(false);

        Phone.setText("+"+myUser.getUser_Phone());
        Phone.setEnabled(false);

        Gender.setText(myUser.getUser_Gender());
        Gender.setEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.disciple_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}

