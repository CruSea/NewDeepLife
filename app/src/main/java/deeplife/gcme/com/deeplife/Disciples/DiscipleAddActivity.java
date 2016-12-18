package deeplife.gcme.com.deeplife.Disciples;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import deeplife.gcme.com.deeplife.R;

/**
 * Created by bengeos on 12/17/16.
 */

public class DiscipleAddActivity extends AppCompatActivity {
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disciples_fragment_add_new);

        toolbar = (Toolbar) findViewById(R.id.add_disciple_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add your Disciple");
    }
}
