package deeplife.gcme.com.deeplife;

import android.content.Intent;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by bengeos on 12/18/16.
 */

public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent intent = new Intent(Splash.this,Login.class);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Intent intent = new Intent(Splash.this,Login.class);
        startActivity(intent);
    }
}
