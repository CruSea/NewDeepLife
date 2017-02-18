package deeplife.gcme.com.deeplife.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Models.Country;
import deeplife.gcme.com.deeplife.Models.User;
import deeplife.gcme.com.deeplife.R;

/**
 * Created by briggsm on 12/20/16.
 */

public class ProfileShowActivity extends AppCompatActivity {
    private EditText FullName, Email, Country, Phone, Gender;
    //private TextView DisplayName;
    private User myUser;
    Button editBtn;

    Country userCountry;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        Init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateTextViews();
    }

    public void Init() {
        //editBtn = (Button) findViewById(R.id.editBtn);

        //DisplayName = (TextView) findViewById(R.id.txt_disciple_profile_displayname);
        FullName = (EditText) findViewById(R.id.txt_disciple_profile_fullname);
        Email = (EditText) findViewById(R.id.txt_disciple_profile_email);
        Country = (EditText) findViewById(R.id.txt_disciple_profile_country);
        Phone = (EditText) findViewById(R.id.txt_disciple_profile_phone);
        Gender = (EditText) findViewById(R.id.txt_disciple_profile_gender);

//        DisplayName.setText(myUser.getFullName());
//        DisplayName.setEnabled(false);
    }

    private void populateTextViews() {
        myUser = DeepLife.myDATABASE.getMainUser();

        FullName.setText(myUser.getFullName());
        Email.setText(myUser.getEmail());
        userCountry = DeepLife.myDATABASE.getCountryByID(Integer.valueOf(myUser.getCountry()));
        Country.setText(userCountry.getName());
        Phone.setText("+" + myUser.getPhone());
        Gender.setText(myUser.getGender());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.disciple_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.disciple_edit) {
            Intent intent = new Intent(this, ProfileEditActivity.class);
            Bundle b = new Bundle();
            b.putString("FullName", FullName.getText().toString());
            b.putString("Email", Email.getText().toString());
            b.putString("CountrySerID", String.valueOf(userCountry.getSerID()));
            b.putString("Phone", Phone.getText().toString());
            b.putString("Gender", Gender.getText().toString());
            intent.putExtras(b);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}

