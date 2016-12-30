package deeplife.gcme.com.deeplife.Disciples;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.FileManager.FileManager;
import deeplife.gcme.com.deeplife.Models.Country;
import deeplife.gcme.com.deeplife.Models.Logs;
import deeplife.gcme.com.deeplife.Processing.ImageProcessing;
import deeplife.gcme.com.deeplife.R;
import deeplife.gcme.com.deeplife.SyncService.SyncDatabase;
import deeplife.gcme.com.deeplife.SyncService.SyncService;
import deeplife.gcme.com.deeplife.WinBuildSend.WinBuildSendActivity;

/**
 * Created by bengeos on 12/25/16.
 */

public class DiscipleProfileActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Toolbar myToolbar;
    private TabLayout myTabLayout;
    private EditText FullName,Email,Countr,Phone,Note;
    private ImageButton DiscipleImageBtn;
    private ImageView DiscipleImage;
    private String DisciplePhone;
    private Disciple myDisciple;
    private Activity myActivity;
    private Bitmap imageFromCrop = null;
    private String newImage = "";
    private FileManager myFileManager;
    private SyncDatabase mySyncDatabase;
    private Button DisCall,DisMessage,DisComplete;
    private Context myContext;
    private TextView DisplayName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disciple_profile_page1);
        DisciplePhone = getIntent().getExtras().getString("DisciplePhone");
        myDisciple = DeepLife.myDATABASE.getDiscipleByPhone(DisciplePhone);
        myActivity = this;
        myContext = this;
        myFileManager = new FileManager(this);
        mySyncDatabase = new SyncDatabase();
        Init();
        EditMode(false);
        FillForm();

    }

    private void FillForm() {
        if(myDisciple != null){
            FullName.setText(myDisciple.getDisplayName());
            DisplayName.setText(myDisciple.getDisplayName());
            Email.setText(myDisciple.getEmail());
            Phone.setText("+"+myDisciple.getPhone());
            Country country = DeepLife.myDATABASE.getCountryByID(Integer.valueOf(myDisciple.getCountry()));
            Countr.setText(country.getName());
            Countr.setEnabled(false);
            if(myDisciple.getImagePath() != null){
                File file = new File(myDisciple.getImagePath());
                if(file.isFile()){
                    DiscipleImage.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                }
            }
        }
    }

    private void Init() {
        FullName = (EditText) findViewById(R.id.txt_disciple_profile_fullname);
        DisplayName = (TextView) findViewById(R.id.txt_disciple_profile_displayname);
        Email = (EditText) findViewById(R.id.txt_disciple_profile_email);
        Email.setEnabled(false);
        Phone = (EditText) findViewById(R.id.txt_disciple_profile_phone);
        Phone.setEnabled(false);
        Note = (EditText) findViewById(R.id.txt_disciple_profile_note);
        Countr = (EditText) findViewById(R.id.txt_disciple_profile_country);
        DiscipleImage = (ImageView) findViewById(R.id.img_discipleprofile_image);
        DiscipleImageBtn = (ImageButton) findViewById(R.id.btn_discipleprofile_image);
        DisComplete = (Button) findViewById(R.id.btn_disciple_profile_complete);
        DisCall = (Button) findViewById(R.id.btn_disciple_profile_call);
        DisMessage = (Button) findViewById(R.id.btn_disciple_profile_message);
        DiscipleImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(myContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(myActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);
                }else {
                    Crop.pickImage(myActivity);
                }

            }
        });
        DisComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myContext, WinBuildSendActivity.class);
                Bundle b = new Bundle();
                b.putString("DisciplePhone",myDisciple.getPhone().toString());
                intent.putExtras(b);
                myContext.startActivity(intent);
            }
        });
        DisCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = "+"+myDisciple.getPhone();
                String number = "tel:" + phone;
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                if (ContextCompat.checkSelfPermission(myContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(myActivity, new String[]{Manifest.permission.READ_PHONE_STATE}, 11);
                }else {
                    myContext.startActivity(callIntent);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
    }

    private void EditMode(boolean state){
        FullName.setEnabled(state);
        Note.setEnabled(state);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.disciple_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.disciple_edit){
            EditMode(true);
        }else if(item.getItemId() == R.id.disciple_edit){
            myDisciple.setDisplayName(FullName.getText().toString());
            DeepLife.myDATABASE.updateDisciple(myDisciple);
            Logs logs = new Logs();
            logs.setType("Disciple");
            logs.setTask(SyncService.Sync_Tasks[1]);
            logs.setValue(myDisciple.getPhone());
            mySyncDatabase.AddLog(logs);
            EditMode(false);
        }
        return super.onOptionsItemSelected(item);
    }

    private void beginCrop(Uri source) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        Uri destination = Uri.fromFile(new File(getExternalCacheDir(), timeStamp));
        Crop.of(source, destination).withAspect(720,720).start(this);
    }

    private void handleCrop(int resultCode, final Intent result) {
        if (resultCode == RESULT_OK) {
            ImageProcessing imageProcessing = new ImageProcessing(getApplicationContext());
            final File file = imageProcessing.createImage("myprofile");
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    Looper.prepare();
                    try {
                        imageFromCrop = Glide.with(getApplicationContext()).load(Crop.getOutput(result)).asBitmap().into(720, 720).get();
                        imageFromCrop.compress(Bitmap.CompressFormat.JPEG, 70, new FileOutputStream(file.getAbsolutePath()));
                    } catch (ExecutionException e) {
                        Log.e(Database.TAG, e.getMessage());
                    } catch (InterruptedException e) {
                        Log.e(Database.TAG, e.getMessage());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
                @Override
                protected void onPostExecute(Void dummy) {
                    if (imageFromCrop != null) {
                        DiscipleImage.setImageBitmap(imageFromCrop);
                        newImage = file.getName();
                        File des = myFileManager.getFileAt("Disciples",myDisciple.getPhone()+file.getName());
                        try {
                            myFileManager.CopyFile(file,des);
                            myDisciple.setImagePath(des.getAbsolutePath());
                            DeepLife.myDATABASE.updateDisciple(myDisciple);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }.execute();

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
