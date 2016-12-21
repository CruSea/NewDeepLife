package deeplife.gcme.com.deeplife.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import deeplife.gcme.com.deeplife.R;

/**
 * Created by briggsm on 12/20/16.
 */

public class ProfileShowActivity extends AppCompatActivity {

    Button editBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_show_activity);

        editBtn = (Button) findViewById(R.id.editBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfileEditActivity.class));
            }
        });
    }
}
