package deeplife.gcme.com.deeplife.Profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import deeplife.gcme.com.deeplife.R;

/**
 * Created by briggsm on 12/21/16.
 */

public class ProfileEditActivity extends AppCompatActivity {

    Button saveBtn;
    Button cancelBtn;
    Spinner countrySpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit_activity);

        saveBtn = (Button)findViewById(R.id.saveBtn);
        cancelBtn = (Button)findViewById(R.id.cancelBtn);
        countrySpinner = (Spinner)findViewById(R.id.countrySpinner);

        // Spinner Temp Init
        String[] countryItems = new String[]{"Ethiopia", "Hungary", "Turkey", "Uganda", "USA", "Zimbabwe"};
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countryItems);
        countrySpinner.setAdapter(countryAdapter);

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
