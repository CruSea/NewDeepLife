package deeplife.gcme.com.deeplife.Profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import deeplife.gcme.com.deeplife.Adapters.CountryListAdapter;
import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Models.Country;
import deeplife.gcme.com.deeplife.R;

/**
 * Created by briggsm on 12/21/16.
 */

public class ProfileEditActivity extends AppCompatActivity {

    Button saveBtn;
    Button cancelBtn;
    EditText fullName;
    Spinner genderSpinner;
    Spinner countrySpinner;
    EditText phone;
    EditText email;

    List<Country> myCountries;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit_activity);

        saveBtn = (Button)findViewById(R.id.profileEditSaveBtn);
        cancelBtn = (Button)findViewById(R.id.profileEditCancelBtn);

        fullName = (EditText) findViewById(R.id.profileEditFullNameText);
        genderSpinner = (Spinner) findViewById(R.id.profileEditGenderSpinner);
        countrySpinner = (Spinner) findViewById(R.id.profileEditCountrySpinner);
        phone = (EditText) findViewById(R.id.profileEditPhoneText);
        email = (EditText) findViewById(R.id.profileEditEmailText);

//        // Spinner Temp Init
//        String[] countryItems = new String[]{"Ethiopia", "Hungary", "Turkey", "Uganda", "USA", "Zimbabwe"};
//        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countryItems);
//        countrySpinner.setAdapter(countryAdapter);

        myCountries = DeepLife.myDATABASE.getAllCountries();
        if (myCountries != null) {
            CountryListAdapter countryListAdapter = new CountryListAdapter(this, R.layout.login_countries_item, myCountries);
            countrySpinner.setAdapter(countryListAdapter);

            int countrySerID = Integer.valueOf(getIntent().getExtras().getString("CountrySerID"));
            int pos = DeepLife.myDATABASE.getCountryBySerID(countrySerID).getID() - 1;
            countrySpinner.setSelection(pos);
            //countrySpinner.setSelection(countryListAdapter.getPosition());
        }




        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Just for now, "press" back
                // TODO: save to DB
                onBackPressed();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Don't save anything, just go back.
                onBackPressed();
            }
        });

        // Fill in text views with info from Intent
        fullName.setText(getIntent().getExtras().getString("FullName"));
        String passedGender = getIntent().getExtras().getString("Gender");
        if (passedGender.equalsIgnoreCase("Female")) {
            genderSpinner.setSelection(1);
        } else {
            genderSpinner.setSelection(0);
        }
        //getIntent().getExtras().getString("Country");
        phone.setText(getIntent().getExtras().getString("Phone"));
        email.setText(getIntent().getExtras().getString("Email"));

    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.disciple_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    */
}
