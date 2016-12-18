package deeplife.gcme.com.deeplife;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by bengeos on 12/18/16.
 */

public class Login extends AppCompatActivity {
    private Button Login,SignUp,Forgotten;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        SignUp = (Button) findViewById(R.id.btn_link_sign_up);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(deeplife.gcme.com.deeplife.Login.this, deeplife.gcme.com.deeplife.SignUp.class);
                startActivity(intent);
            }
        });
        Login = (Button) findViewById(R.id.btn_login_login);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(deeplife.gcme.com.deeplife.Login.this, deeplife.gcme.com.deeplife.MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
