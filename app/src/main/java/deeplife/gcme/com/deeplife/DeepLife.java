package deeplife.gcme.com.deeplife;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import java.util.List;

import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.SyncService.SyncService;
import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;

/**
 * Created by bengeos on 12/16/16.
 */

public class DeepLife extends Application {
//    public static final String DEEP_URL  = "http://deeplifestaging.briggs-inc.com";
    public static final String DEEP_URL  = "http://staging.deeplife.cc";
//    public static final String DEEP_URL  = "http://192.168.0.44/DeepLife_Web/public";


    public static final String FORGOTEN_URL = DEEP_URL + "/forgot";
    public static final String API_URL = DEEP_URL + "/deep_api";
    public static final String PROFILE_PIC_URL = DEEP_URL + "/img/profile/";


    public static int ImageDownloadCount = 0;
    private static final int REQUEST_PHONE_CALL = 11;

    private static final String TAG = "DeepLifeApplication";
    public JobScheduler myJobScheduler;

    public static Database myDATABASE;
    private static int JOB_ID = 1000;

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Application Started");
        mContext = this;
        myDATABASE = new Database(this);
//        JobManager.create(this).addJobCreator(new SyncJob());
        myJobScheduler = JobScheduler.getInstance(this);
//        JobConstr();
    }

    public void JobConstr() {
        JobInfo.Builder jobInfo;
        jobInfo = new JobInfo.Builder(JOB_ID++, new ComponentName(this, SyncService.class))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPeriodic(30000)
                .setRequiresDeviceIdle(false)
                .setRequiresCharging(false);
        myJobScheduler.cancelAll();
        int x = myJobScheduler.schedule(jobInfo.build());
        if (x == android.app.job.JobScheduler.RESULT_SUCCESS) {
            List<JobInfo> xx = myJobScheduler.getAllPendingJobs();
            Log.i(TAG, "The Job scheduler Constructed\n");
        } else {
            Log.i(TAG, "The Job scheduler Not Constructed");
        }
    }

    public static Context getContext() {
        return mContext;
    }

    public static <E extends Enum<E>> String[] getStrArrFromEnum(Class<E> e) {
        Enum<E>[] enumConstants = e.getEnumConstants();
        int numConstants = enumConstants.length;
        String[] strArr = new String[numConstants];
        for (int i = 0; i < numConstants; i++) {
            strArr[i] = enumConstants[i].toString();
        }
        return strArr;
    }
}
