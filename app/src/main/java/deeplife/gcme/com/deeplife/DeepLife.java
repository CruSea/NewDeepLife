package deeplife.gcme.com.deeplife;

import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.util.List;

import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.SyncService.SyncService;

/**
 * Created by bengeos on 12/16/16.
 */

public class DeepLife extends Application {
//    public static final String DEEP_URL  = "http://staging.deeplife.cc";
    public static final String DEEP_URL  = "http://192.168.100.19/DeepLife_Web/public";
    public static final String API_URL  = DEEP_URL+"/deep_api";
    public static final String PROFILE_PIC_URL  = DEEP_URL+"/img/profile/";


    private static final String TAG = "DeepLifeApplication";
    private static final int JOB_ID = 10001;
    public JobScheduler myJobScheduler;

    public static Database myDATABASE;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"Application Started");
        myDATABASE = new Database(this);
    }


}
