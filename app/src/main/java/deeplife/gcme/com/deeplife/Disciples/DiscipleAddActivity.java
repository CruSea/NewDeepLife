package deeplife.gcme.com.deeplife.Disciples;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import deeplife.gcme.com.deeplife.Adapters.CountryListAdapter;
import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Models.Country;
import deeplife.gcme.com.deeplife.Models.Logs;
import deeplife.gcme.com.deeplife.R;
import deeplife.gcme.com.deeplife.SyncService.SyncDatabase;
import deeplife.gcme.com.deeplife.SyncService.SyncService;

/**
 * Created by bengeos on 12/17/16.
 */

public class DiscipleAddActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText FullName,Email,Phone,PhoneCode;
    private Button AddDisciple;
    private Spinner myCountry,myGender;
    private List<Country> myCountries;
    private Disciple myDisciple;
    private int CountryPos = 0;
    private SyncDatabase mySyncDatabase;
    private  AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disciples_fragment_add_new);

        toolbar = (Toolbar) findViewById(R.id.add_disciple_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add your Disciple");

        mySyncDatabase = new SyncDatabase();

        myCountry = (Spinner) findViewById(R.id.spn_discipleadd_country);
        myGender = (Spinner) findViewById(R.id.spn_discipleadd_country);
        FullName = (EditText) findViewById(R.id.txt_discipleadd_fullname);
        Email = (EditText) findViewById(R.id.txt_discipleadd_email);
        Phone = (EditText) findViewById(R.id.txt_discipleadd_phone);
        PhoneCode = (EditText) findViewById(R.id.txt_discipleadd_phonecode);
        AddDisciple = (Button) findViewById(R.id.btn_discipleadd_add);

        myCountries = DeepLife.myDATABASE.getAllCountries();
        if(myCountries != null){
            myCountry.setAdapter(new CountryListAdapter(this,R.layout.login_countries_item,myCountries));
        }
        myCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryPos = 0;
                PhoneCode.setText("+"+myCountries.get(position).getCode());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        AddDisciple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDisciple = getDisciple();
                if(myDisciple != null){
                    long x = mySyncDatabase.AddDisciple(myDisciple);
                    if(x>0){
                        ShowDialog("Your Disciple is Successfully Added!");
                        Logs logs = new Logs();
                        logs.setType("Disciple");
                        logs.setTask(SyncService.Sync_Tasks[1]);
                        logs.setValue(myDisciple.getPhone());
                        mySyncDatabase.AddLog(logs);
                        DisciplesFragment.UpdateList();

                    }else {
                        ShowDialog("Opps! This Disciple Could not be Added!");
                    }
                }else {
                    ShowDialog("Opps! This Disciple Could not be Added! make sure you have entered correct information");
                }

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
                        finish();
                        break;
                }
            }
        };
        builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name)
                .setMessage(message)
                .setPositiveButton("Ok", dialogClickListener).show();
    }
    public Disciple getDisciple(){
        if(FullName.getText().toString().length()>2 && Email.getText().toString().length()>3 && Phone.getText().toString().length()>8){
            Disciple myDisciple = new Disciple();
            myDisciple.setFullName(FullName.getText().toString());
            myDisciple.setEmail(Email.getText().toString());
            myDisciple.setPhone(myCountries.get(CountryPos).getCode()+Phone.getText().toString());
            myDisciple.setCountry(""+myCountries.get(CountryPos).getSerID());
            myDisciple.setRole("0");
            myDisciple.setDisplayName(FullName.getText().toString());
            myDisciple.setGender(myGender.getSelectedItem().toString());
            myDisciple.setImagePath("0");
            myDisciple.setImageURL("0");
            myDisciple.setStage("WIN");
            myDisciple.setMentorID(0);
            myDisciple.setCreated("Today");
            myDisciple.setSerID(0);
            return myDisciple;
        }
        return null;
    }
}
