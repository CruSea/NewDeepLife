package deeplife.gcme.com.deeplife.Testimony;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Models.Logs;
import deeplife.gcme.com.deeplife.R;
import deeplife.gcme.com.deeplife.SyncService.SyncDatabase;

/**
 * Created by bengeos on 12/18/16.
 */

public class TestimonyAddActivity extends AppCompatActivity {
    private static final String TAG =  "Testimony";
    private Button AddTestimony;
    private EditText TestimonyTXT;
    private Context myContext;
    private SyncDatabase mySyncDatabase;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testimony_fragment_add);
        myContext = getApplicationContext();
        mySyncDatabase = new SyncDatabase();
        TestimonyTXT = (EditText) findViewById(R.id.txt_testimony_content);
        AddTestimony = (Button) findViewById(R.id.btn_add_testimony);
        AddTestimony.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TestimonyTXT.getText().toString().length()>5){
                    Testimony testimony = new Testimony();
                    testimony.setSerID(12);
                    testimony.setUser_ID(12);
                    testimony.setContent(TestimonyTXT.getText().toString());
                    Testimony found = DeepLife.myDATABASE.addNewTestimony(testimony);
                    if(found != null){
                        TestimonyTXT.setText("");
                        Toast.makeText(myContext,"Testimony Saved!", Toast.LENGTH_SHORT).show();
                        Logs logs = new Logs();
                        logs.setTask(Logs.TASK.SEND_TESTIMONY);
                        logs.setValue(""+found.getID());
                        mySyncDatabase.AddLog(logs);
                    }else {
                        Toast.makeText(myContext,"Failed to save the testimony!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(myContext,"Your Testimony is short. please write it with more description", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }
}
