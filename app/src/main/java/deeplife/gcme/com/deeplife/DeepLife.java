package deeplife.gcme.com.deeplife;

import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.util.List;

import deeplife.gcme.com.deeplife.SyncService.SyncService;

/**
 * Created by bengeos on 12/16/16.
 */

public class DeepLife extends Application {
    private static final String TAG = "DeepLifeApplication";
    private static final int JOB_ID = 10001;
    private JobScheduler myJobScheduler;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"Application Started");
        myJobScheduler  = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobConstr();
    }

    public void JobConstr(){
        JobInfo.Builder jobInfo;
        jobInfo = new JobInfo.Builder(JOB_ID,  new ComponentName(this,SyncService.class))
                .setMinimumLatency(1000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setRequiresCharging(false)
                .setPersisted(true)
                .setRequiresCharging(false);
        myJobScheduler.cancelAll();
        int x = myJobScheduler.schedule(jobInfo.build());
//        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(this,SyncService.class));
//        builder.setPeriodic(5000);
//        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
//        builder.setRequiresCharging(false);
//        myJobScheduler.cancelAll();
//        int x = myJobScheduler.schedule(builder.build());
        if(x == android.app.job.JobScheduler.RESULT_SUCCESS){
//            List<JobInfo> xx = myJobScheduler.getAllPendingJobs();
            Log.i(TAG,"The Job scheduler Constructed\n");
        }else{
            Log.i(TAG, "The Job scheduler Not Constructed");
        }

    }
}
