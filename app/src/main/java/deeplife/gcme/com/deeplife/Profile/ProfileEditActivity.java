package deeplife.gcme.com.deeplife.Profile;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import deeplife.gcme.com.deeplife.Adapters.CountryListAdapter;
import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Models.Country;
import deeplife.gcme.com.deeplife.Models.Logs;
import deeplife.gcme.com.deeplife.Models.User;
import deeplife.gcme.com.deeplife.R;
import deeplife.gcme.com.deeplife.SyncService.SyncDatabase;

import static deeplife.gcme.com.deeplife.DeepLife.myDATABASE;

/**
 * Created by briggsm on 12/21/16.
 */

public class ProfileEditActivity extends AppCompatActivity {
    private static final String TAG = "ProfileEditActivity";

    Button saveBtn;
    Button cancelBtn;
    EditText fullName;
    Spinner genderSpinner;
    Spinner countrySpinner;
    TextView phoneCode;
    EditText phoneNumber;
    EditText email;

    List<Country> myCountries;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit_activity2);

        saveBtn = (Button) findViewById(R.id.profileEditSaveBtn);
        cancelBtn = (Button) findViewById(R.id.profileEditCancelBtn);

        fullName = (EditText) findViewById(R.id.profileEditFullNameText);
        genderSpinner = (Spinner) findViewById(R.id.profileEditGenderSpinner);
        countrySpinner = (Spinner) findViewById(R.id.profileEditCountrySpinner);
        phoneCode = (TextView) findViewById(R.id.profileEditPhoneCodeText);
        phoneNumber = (EditText) findViewById(R.id.profileEditPhoneNumberText);
        email = (EditText) findViewById(R.id.profileEditEmailText);

        // Retrieve the Intent's Extra's
        final String extrasFullName = getIntent().getExtras().getString("FullName");
        String extrasGender = getIntent().getExtras().getString("Gender");
        String extrasCountrySerId = getIntent().getExtras().getString("CountrySerID");
        final String extrasPhoneNumber = getIntent().getExtras().getString("Phone");
        final String extrasEmail = getIntent().getExtras().getString("Email");

        // Fill in text views with info from Intent
        fullName.setText(extrasFullName);

        if (extrasGender.equalsIgnoreCase("Female")) {
            genderSpinner.setSelection(1);
        } else {
            genderSpinner.setSelection(0);
        }

        phoneNumber.setText(extrasPhoneNumber);
        email.setText(extrasEmail);

        myCountries = myDATABASE.getAllCountries();
        if (myCountries != null) {
            CountryListAdapter countryListAdapter = new CountryListAdapter(this, R.layout.login_countries_item, myCountries);
            countrySpinner.setAdapter(countryListAdapter);

            int countrySerID = Integer.valueOf(extrasCountrySerId);
            int pos = myDATABASE.getCountryBySerID(countrySerID).getID() - 1;
            countrySpinner.setSelection(pos);
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the strings we'll be working with
                String newFullName = fullName.getText().toString();
                String newEmail = email.getText().toString();
                String newPhoneNumber = phoneNumber.getText().toString();

                // briggsm: Is it best to do these kinds of checks here, or on the back end?
                // Validate locally (name not null)
                if (newFullName.length() == 0) {
                    new AlertDialog.Builder(ProfileEditActivity.this)
                            .setTitle("Invalid Field")
                            .setMessage("'Full Name' must be specified")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    // and dont' do: onBackPressed(), just return from function now.
                    return;
                }

                /*
                // NOTE: THIS WILL BE DONE ON BACK END!
                // Validate email & phone do not already exist on server
                if (!newEmail.equals(extrasEmail)) {
                    // Validate that it's a valid email address
                    boolean isValidEmail = !TextUtils.isEmpty(newEmail) && android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches();
                    if (!isValidEmail) {
                        new AlertDialog.Builder(ProfileEditActivity.this)
                                .setTitle("Invalid Field")
                                .setMessage("'Email' must be a valid email address")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        // and dont' do: onBackPressed(), just return from function now.
                        return;
                    }

                    // Validate that it's not already in the DB


                }
                if (!newPhoneNumber.equals(extrasPhoneNumber)) {
                    // Validate that it's a valid email address
                    if (newPhoneNumber.isEmpty() || newPhoneNumber.length() < 8) {
                        new AlertDialog.Builder(ProfileEditActivity.this)
                                .setTitle("Invalid Field")
                                .setMessage("'Phone Number' must be valid")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        // and dont' do: onBackPressed(), just return from function now.
                        return;
                    }

                    // Validate that it's not already in the DB


                }
                */


                // Save to DB
                Log.i(TAG, "onClick saveBtn: Saving to DB.");
                User currMainUser = DeepLife.myDATABASE.getMainUser();

                // Update the fields
                currMainUser.setFullName(newFullName);
                currMainUser.setEmail(newEmail);
                currMainUser.setPhone(newPhoneNumber.substring(1)); // to take off the "+" sign.
                currMainUser.setGender(genderSpinner.getSelectedItemPosition() == 1 ? "Female" : "Male");
                currMainUser.setCountry("" + ((Country)countrySpinner.getSelectedItem()).getSerID() );

                // Update the DB itself (here in Android).
                long modifiedRowId = DeepLife.myDATABASE.updateMainUser(currMainUser);

                // Create a Log Entry - so the Back End DB can be updated as well.
                if (modifiedRowId > 0) {
                    Logs logs = new Logs();
                    logs.setType(Logs.TYPE.USER);  // briggsm: looks like we don't even need to specify log TYPE.
                    logs.setTask(Logs.TASK.UPDATE_USER_PROFILE);  // briggsm: Biniam says Send_Disciples.  Why not AddNew_Disciples? What's difference?
                    logs.setValue("" + modifiedRowId);  // briggsm: looks like task VALUE never used/needed either, cuz there will only ever be 1 mainUser. (just set it so other checks don't crash...)
                    new SyncDatabase().AddLog(logs);
                } else {
                    // Main User not updated for some reason.
                    Log.e(TAG, "onClick: saveBtn: Main User not updated for some reason.");
                }

                // If we get here, all the validity tests must have passed, and data saved to DB. So "press" the back button.
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
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.disciple_profile_menu, menu);
        return true;
    }
    */
}
