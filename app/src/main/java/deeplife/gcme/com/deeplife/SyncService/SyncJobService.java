package deeplife.gcme.com.deeplife.SyncService;

import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

/**
 * Created by bengeos on 12/24/16.
 */

public class SyncJobService extends Job {
    public static final String TAG = "job_demo_tag";
    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        Log.i(TAG,"Sync Servie Started");
        return Result.SUCCESS;
    }
    public static void scheduleJob() {
        new JobRequest.Builder(SyncJobService.TAG)
                .setPeriodic(2000)
                .setPersisted(true)
                .build()
                .schedule();
    }
}
