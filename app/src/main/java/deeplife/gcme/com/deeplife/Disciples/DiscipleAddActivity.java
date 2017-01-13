package deeplife.gcme.com.deeplife.Disciples;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import deeplife.gcme.com.deeplife.Adapters.CountryListAdapter;
import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.FileManager.FileManager;
import deeplife.gcme.com.deeplife.Models.Country;
import deeplife.gcme.com.deeplife.Models.Logs;
import deeplife.gcme.com.deeplife.Models.User;
import deeplife.gcme.com.deeplife.Processing.ImageProcessing;
import deeplife.gcme.com.deeplife.R;
import deeplife.gcme.com.deeplife.SyncService.SyncDatabase;

/**
 * Created by bengeos on 12/17/16.
 */

public class DiscipleAddActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText FullName,Email,Phone,PhoneCode;
    private ImageButton DiscipleImageAdd;
    private ImageView DiscipleImage;
    private Button AddDisciple;
    private Spinner myCountry,myGender;
    private List<Country> myCountries;
    private Disciple myDisciple;
    private int CountryPos = 0;
    private SyncDatabase mySyncDatabase;
    private  AlertDialog.Builder builder;
    private Activity myActivity;
    private Bitmap imageFromCrop = null;
    private String newImage = "";
    private FileManager myFileManager;
    private File DiscipleImageFile;
    private Context myContext;
    private User myUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disciples_fragment_add_new);

        toolbar = (Toolbar) findViewById(R.id.add_disciple_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.toolbar_title_disciple_add);
        myFileManager = new FileManager(this);
        DiscipleImageFile = new File("");

        mySyncDatabase = new SyncDatabase();
        myActivity = this;
        myContext = this;

        myUser = DeepLife.myDATABASE.getMainUser();

        myCountry = (Spinner) findViewById(R.id.spn_discipleadd_country);
        myGender = (Spinner) findViewById(R.id.spn_discipleadd_country);
        FullName = (EditText) findViewById(R.id.txt_discipleadd_fullname);
        Email = (EditText) findViewById(R.id.txt_discipleadd_email);
        Phone = (EditText) findViewById(R.id.txt_discipleadd_phone);
        PhoneCode = (EditText) findViewById(R.id.txt_discipleadd_phonecode);
        AddDisciple = (Button) findViewById(R.id.btn_discipleadd_add);
        DiscipleImage = (ImageView) findViewById(R.id.img_discipleadd_image);
        DiscipleImageAdd = (ImageButton) findViewById(R.id.btn_discipleadd_image);

        myCountries = DeepLife.myDATABASE.getAllCountries();
        if(myCountries != null){
            myCountry.setAdapter(new CountryListAdapter(this,R.layout.login_countries_item,myCountries));
            int xx = Integer.valueOf(myUser.getUser_Country());
            Country country = DeepLife.myDATABASE.getCountryByID(xx);
        }
        myCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryPos = 0;
                PhoneCode.setText("+"+myCountries.get(position).getCode());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                int xx = Integer.valueOf(myUser.getUser_Country());
                int pos = DeepLife.myDATABASE.getCountryBySerID(xx).getID();
                parent.setSelection(pos);
            }
        });
        AddDisciple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDisciple = getDisciple();
                if(myDisciple != null){
                    long x = mySyncDatabase.AddDisciple(myDisciple);
                    if(x>0){
                        Logs logs = new Logs();
                        logs.setType(Logs.Type.DISCIPLE);
                        logs.setTask(Logs.Task.SEND_DISCIPLES);  // briggsm: Biniam says Send_Disciples.  Why not AddNew_Disciples? What's difference?
                        logs.setValue(myDisciple.getPhone());
                        mySyncDatabase.AddLog(logs);
                        DisciplesFragment.UpdateList();
                        ShowDialog(getString(R.string.dlg_msg_disciple_add_success),true);

                    }else {
                        ShowDialog(getString(R.string.dlg_msg_disciple_add_failure),false);
                    }
                }else {
                    ShowDialog(getString(R.string.dlg_msg_disciple_add_failure_check), false);
                }
            }
        });

        DiscipleImageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(myContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(myActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);
                }else {
                    Crop.pickImage(myActivity);
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

    public void ShowDialog(String message, final boolean state) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        if(state){
                            finish();
                        }
                        break;
                }
            }
        };
        builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name)
                .setMessage(message)
                .setPositiveButton(R.string.dlg_btn_ok, dialogClickListener).show();
    }
    public Disciple getDisciple(){
        if(FullName.getText().toString().length()>2){
            Disciple myDisciple = new Disciple();
            myDisciple.setFullName(FullName.getText().toString());
            myDisciple.setEmail(Email.getText().toString());
            myDisciple.setPhone(myCountries.get(CountryPos).getCode()+Phone.getText().toString());
            myDisciple.setCountry(""+myCountries.get(CountryPos).getSerID());
            myDisciple.setRole("0");
            myDisciple.setDisplayName(FullName.getText().toString());
            myDisciple.setGender(myGender.getSelectedItem().toString());
            if(DiscipleImageFile.isFile()){
                myDisciple.setImagePath(myFileManager.getFileAt("Disciples",myDisciple.getPhone()+DiscipleImageFile.getName()).getAbsolutePath());
            }else {
                myDisciple.setImagePath("0");
            }
            myDisciple.setImageURL("0");
            myDisciple.setStage("WIN");
            myDisciple.setMentorID(0);
            myDisciple.setCreated("Today");
            myDisciple.setSerID(0);
            return myDisciple;
        }
        return null;
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
                        DiscipleImageFile = file;
//                        DiscipleImageFile = myFileManager.getFileAt("Disciples",myDisciple.getPhone()+file.getName());
                    }
                }
            }.execute();

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
