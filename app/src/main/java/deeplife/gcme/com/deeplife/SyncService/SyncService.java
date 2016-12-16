package deeplife.gcme.com.deeplife.SyncService;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import deeplife.gcme.com.deeplife.FileManager.FileManager;
import deeplife.gcme.com.deeplife.Models.User;

/**
 * Created by bengeos on 12/16/16.
 */

public class SyncService extends JobService {
    public static final String TAG = "SyncService";
    public static final String[] Sync_Tasks = {"Send_Log", "Send_Disciples","Remove_Disciple","Update_Disciple","Send_Schedule","Send_Report","Send_Testimony","Update_Schedules"};
    private List<Object> Param;
    private Gson myParser;
    private List<kotlin.Pair<String,String>> Send_Param;
    private User user;
    private FileManager myFileManager;
    private MultipartUtility myMultipartUtility;
    private boolean isUploading;

    public SyncService() {
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG,"Service Started");
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG,"Service Stopped");
        return true;
    }
}
