package deeplife.gcme.com.deeplife;

import android.content.Intent;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.Models.User;

/**
 * Created by bengeos on 12/18/16.
 */

public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Thread splash = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                } catch(InterruptedException e){
                } finally {
                    getNextActivity();
                }
                //super.run();
            }
        };
        splash.start();
    }
    public synchronized void getNextActivity() {

        int Count = DeepLife.myDATABASE.count(Database.Table_USER);
        User bb = DeepLife.myDATABASE.getMainUser();
        if(Count == 1){
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        }

    }
}
