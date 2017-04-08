package deeplife.gcme.com.deeplife.Profile;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import deeplife.gcme.com.deeplife.Adapters.CountryListAdapter;
import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Disciples.DiscipleEditActivity;
import deeplife.gcme.com.deeplife.Models.Country;
import deeplife.gcme.com.deeplife.Models.Logs;
import deeplife.gcme.com.deeplife.Models.User;
import deeplife.gcme.com.deeplife.R;
import deeplife.gcme.com.deeplife.SyncService.SyncDatabase;
import deeplife.gcme.com.deeplife.SyncService.SyncService;
import kotlin.Pair;

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
    TextView phoneCode,countryLabel;
    EditText phoneNumber;
    EditText email;

    List<Country> myCountries;
    private boolean isEditing = false;
    private boolean isEditingPhone = false;
    private User mainUser;
    private static ProgressDialog myProgressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit_activity2);

        myProgressDialog = new ProgressDialog(this);
        myProgressDialog.setTitle("Updating Profile ...");

        saveBtn = (Button) findViewById(R.id.profileEditSaveBtn);
        saveBtn.setText("Edit");
        cancelBtn = (Button) findViewById(R.id.profileEditCancelBtn);
        cancelBtn.setVisibility(View.INVISIBLE);

        fullName = (EditText) findViewById(R.id.profileEditFullNameText);
        genderSpinner = (Spinner) findViewById(R.id.profileEditGenderSpinner);
        countrySpinner = (Spinner) findViewById(R.id.profileEditCountrySpinner);
        countryLabel = (TextView) findViewById(R.id.textView24);
        phoneCode = (TextView) findViewById(R.id.profileEditPhoneCodeText);
        phoneNumber = (EditText) findViewById(R.id.profileEditPhoneNumberText);
        email = (EditText) findViewById(R.id.profileEditEmailText);
        phoneCode.setVisibility(View.GONE);
        countryLabel.setVisibility(View.GONE);


        mainUser = DeepLife.myDATABASE.getMainUser();


        fullName.setEnabled(false);
        countrySpinner.setVisibility(View.GONE);
        phoneNumber.setEnabled(false);
        phoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    isEditingPhone = true;
                    phoneNumber.setText("");
                    myCountries = myDATABASE.getAllCountries();
                    if (myCountries != null) {
                        phoneCode.setVisibility(View.VISIBLE);
                        countryLabel.setVisibility(View.VISIBLE);
                        countrySpinner.setVisibility(View.VISIBLE);
                        CountryListAdapter countryListAdapter = new CountryListAdapter(ProfileEditActivity.this, R.layout.login_countries_item, myCountries);
                        countrySpinner.setAdapter(countryListAdapter);
                        if(mainUser != null && mainUser.getCountry() != null){
                            int countrySerID = Integer.valueOf(mainUser.getCountry());
                            Country country = myDATABASE.getCountryBySerID(countrySerID);
                            if(country != null){
                                int pos = country.getSerID() - 1;
                                countrySpinner.setSelection(pos);
                                phoneCode.setText("+"+country.getCode());
                            }
                        }

                        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                phoneCode.setText("+"+myCountries.get(position).getCode());
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }
                }
            }
        });
        email.setEnabled(false);
        if(mainUser !=null){
            if(mainUser.getFullName() !=null){
                fullName.setText(mainUser.getFullName());
            }
            if(mainUser.getPhone() != null){
                phoneNumber.setText("+"+mainUser.getPhone());
            }
            if(mainUser.getEmail() != null){
                email.setText(mainUser.getEmail());
            }
        }
        // Retrieve the Intent's Extra's
//        final String extrasFullName = getIntent().getExtras().getString("FullName");
//        String extrasGender = getIntent().getExtras().getString("Gender");
//        String extrasCountrySerId = getIntent().getExtras().getString("CountrySerID");
//        final String extrasPhoneNumber = getIntent().getExtras().getString("Phone");
//        final String extrasEmail = getIntent().getExtras().getString("Email");

        // Fill in text views with info from Intent
//        fullName.setText(extrasFullName);
//
//        if (extrasGender.equalsIgnoreCase("Female")) {
//            genderSpinner.setSelection(1);
//        } else {
//            genderSpinner.setSelection(0);
//        }
//
//        phoneNumber.setText(extrasPhoneNumber);
//        email.setText(extrasEmail);
//
//        myCountries = myDATABASE.getAllCountries();
//        if (myCountries != null) {
//            CountryListAdapter countryListAdapter = new CountryListAdapter(this, R.layout.login_countries_item, myCountries);
//            countrySpinner.setAdapter(countryListAdapter);
//
//            int countrySerID = Integer.valueOf(extrasCountrySerId);
//            int pos = myDATABASE.getCountryBySerID(countrySerID).getID() - 1;
//            countrySpinner.setSelection(pos);
//        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isEditing){
                    isEditing = true;
                    saveBtn.setText("Save");
                    cancelBtn.setVisibility(View.VISIBLE);
                    fullName.setEnabled(true);
                    phoneNumber.setEnabled(true);
                    email.setEnabled(true);
                }else {
                    if(mainUser != null){
                        Profile newProfile = new Profile();
                        if(isEditingPhone){
                            String str = phoneCode.getText().toString();
                            int pos = countrySpinner.getSelectedItemPosition()+1;
                            newProfile.setPhone(str.substring(1)+phoneNumber.getText().toString());
                            newProfile.setCountry(pos+"");
                        }else {
                            newProfile.setPhone(phoneNumber.getText().toString().substring(1));
                            newProfile.setCountry(mainUser.getCountry()+"");
                        }
                        newProfile.setId("1");
                        newProfile.setFullname(fullName.getText().toString());
                        newProfile.setEmail(email.getText().toString());
                        newProfile.setGender(genderSpinner.getSelectedItem().toString());
                        UpdateProfile(newProfile);
                    }
                    isEditing = false;
                    isEditingPhone = false;
                    saveBtn.setText("Edit");
                    cancelBtn.setVisibility(View.INVISIBLE);
                    fullName.setEnabled(false);
                    phoneNumber.setEnabled(false);
                    email.setEnabled(false);
                }
//                // Get the strings we'll be working with
//                String newFullName = fullName.getText().toString();
//                String newEmail = email.getText().toString();
//                String newPhoneNumber = phoneNumber.getText().toString();
//
//                // briggsm: Is it best to do these kinds of checks here, or on the back end?
//                // Validate locally (name not null)
//                if (newFullName.length() == 0) {
//                    new AlertDialog.Builder(ProfileEditActivity.this)
//                            .setTitle("Invalid Field")
//                            .setMessage("'Full Name' must be specified")
//                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    // do nothing
//                                }
//                            })
//                            .setIcon(android.R.drawable.ic_dialog_alert)
//                            .show();
//                    // and dont' do: onBackPressed(), just return from function now.
//                    return;
//                }
//
//                /*
//                // NOTE: THIS WILL BE DONE ON BACK END!
//                // Validate email & phone do not already exist on server
//                if (!newEmail.equals(extrasEmail)) {
//                    // Validate that it's a valid email address
//                    boolean isValidEmail = !TextUtils.isEmpty(newEmail) && android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches();
//                    if (!isValidEmail) {
//                        new AlertDialog.Builder(ProfileEditActivity.this)
//                                .setTitle("Invalid Field")
//                                .setMessage("'Email' must be a valid email address")
//                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        // do nothing
//                                    }
//                                })
//                                .setIcon(android.R.drawable.ic_dialog_alert)
//                                .show();
//                        // and dont' do: onBackPressed(), just return from function now.
//                        return;
//                    }
//
//                    // Validate that it's not already in the DB
//
//
//                }
//                if (!newPhoneNumber.equals(extrasPhoneNumber)) {
//                    // Validate that it's a valid email address
//                    if (newPhoneNumber.isEmpty() || newPhoneNumber.length() < 8) {
//                        new AlertDialog.Builder(ProfileEditActivity.this)
//                                .setTitle("Invalid Field")
//                                .setMessage("'Phone Number' must be valid")
//                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        // do nothing
//                                    }
//                                })
//                                .setIcon(android.R.drawable.ic_dialog_alert)
//                                .show();
//                        // and dont' do: onBackPressed(), just return from function now.
//                        return;
//                    }
//
//                    // Validate that it's not already in the DB
//
//
//                }
//                */
//
//
//                // Save to DB
//                Log.i(TAG, "onClick saveBtn: Saving to DB.");
//                User currMainUser = DeepLife.myDATABASE.getMainUser();
//
//                // Update the fields
//                currMainUser.setFullName(newFullName);
//                currMainUser.setEmail(newEmail);
//                currMainUser.setPhone(newPhoneNumber.substring(1)); // to take off the "+" sign.
//                currMainUser.setGender(genderSpinner.getSelectedItemPosition() == 1 ? "Female" : "Male");
//                currMainUser.setCountry("" + ((Country)countrySpinner.getSelectedItem()).getSerID() );
//
//                // Update the DB itself (here in Android).
//                long modifiedRowId = DeepLife.myDATABASE.updateMainUser(currMainUser);
//
//                // Create a Log Entry - so the Back End DB can be updated as well.
//                if (modifiedRowId > 0) {
//                    Logs logs = new Logs();
//                    logs.setType(Logs.TYPE.USER);  // briggsm: looks like we don't even need to specify log TYPE.
//                    logs.setTask(Logs.TASK.UPDATE_USER_PROFILE);  // briggsm: Biniam says Send_Disciples.  Why not AddNew_Disciples? What's difference?
//                    logs.setValue("" + modifiedRowId);  // briggsm: looks like task VALUE never used/needed either, cuz there will only ever be 1 mainUser. (just set it so other checks don't crash...)
//                    new SyncDatabase().AddLog(logs);
//                } else {
//                    // Main User not updated for some reason.
//                    Log.e(TAG, "onClick: saveBtn: Main User not updated for some reason.");
//                }
//
//                // If we get here, all the validity tests must have passed, and data saved to DB. So "press" the back button.
//                onBackPressed();
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
    private static List<kotlin.Pair<String, String>> Send_Param;
    private static User user;
    private static Gson myParser;
    private static SyncDatabase mySyncDatabase;

    private static void UpdateProfile(Profile profile){
        try {
            myProgressDialog.setCancelable(false);
            myProgressDialog.show();
            myParser = new Gson();
            Logs logs = new Logs();
            mySyncDatabase = new SyncDatabase();

            List<Object> myProfiles = new ArrayList<>();
            myProfiles.add(profile);
            logs.setParam(myProfiles);
            user = DeepLife.myDATABASE.getMainUser();
            if(user != null){
                Send_Param = new ArrayList<Pair<String, String>>();
                if (user != null) {
                    if (user.getEmail() != null) {
                        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.USER_NAME.toString(), user.getEmail()));
                    } else {
                        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.USER_NAME.toString(), user.getPhone()));
                    }
                    Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.USER_PASS.toString(), user.getPass()));
                    Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.COUNTRY.toString(), user.getCountry()));
                    Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.SERVICE.toString(), SyncService.API_SERVICE.UPDATE_PROFILE.toString()));
                    Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.PARAM.toString(), myParser.toJson(logs.getParam())));
                }
                Log.i(TAG, "Update Profile: " + Send_Param.toString());
                Fuel.post(DeepLife.API_URL, Send_Param).responseString(new Handler<String>() {
                    @Override
                    public void success(@NotNull Request request, @NotNull Response response, String s) {
                        Log.d(TAG, "Update Profile Request: " + request);
                        Log.i(TAG, "Update Profile Response: " + s);
                        try{
                            JSONObject myObject = (JSONObject) new JSONTokener(s).nextValue();
                            if (!myObject.isNull(SyncDatabase.ApiResponseKey.RESPONSE.toString())) {
                                JSONObject json_response = myObject.getJSONObject(SyncDatabase.ApiResponseKey.RESPONSE.toString());
                                if (!json_response.isNull(SyncDatabase.ApiResponseKey.PROFILE_UPDATE.toString())) {
                                    JSONObject json_user_profile = json_response.getJSONObject(SyncDatabase.ApiResponseKey.PROFILE_UPDATE.toString());
                                    mySyncDatabase.Update_MainUser(json_user_profile);
                                }
                            }
                        }catch (Exception e){
                            Log.e(TAG, "Update Profile failure: \n" + e.toString());
                            myProgressDialog.cancel();
                        }
                        myProgressDialog.cancel();
                    }
                        @Override
                    public void failure(@NotNull Request request, @NotNull Response response, @NotNull FuelError fuelError) {
                        Log.e(TAG, "Update Profile Fuel failure: \n" + fuelError.toString());
                        myProgressDialog.cancel();
                    }
                });
                Log.i(TAG, "API Request SENT to: " + DeepLife.API_URL);
            }else {
                myProgressDialog.cancel();
            }
        } catch (Exception e) {
            Log.e(TAG, "The Job scheduler Failed ...." + e.toString());
        }
    }
}
