package deeplife.gcme.com.deeplife.SyncService;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.FileManager.FileManager;
import deeplife.gcme.com.deeplife.Models.User;
import kotlin.Pair;

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
    private SyncDatabase mySyncDatabase;

    public SyncService() {
        mySyncDatabase = new SyncDatabase();
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "The Job scheduler started");
        user = new User();
        user.setUserName("916417951");
        user.setUserPass("passben");
        user.setUserCountry("68");


        Send_Param = new ArrayList<Pair<String,String>>();
        if(user != null ){
            Send_Param.add(new kotlin.Pair<String, String>("User_Name",user.getUserName()));
            Send_Param.add(new kotlin.Pair<String, String>("User_Pass",user.getUserPass()));
            Send_Param.add(new kotlin.Pair<String, String>("Country", user.getUserCountry()));
            Send_Param.add(new kotlin.Pair<String, String>("Service",getService()));
            Send_Param.add(new kotlin.Pair<String, String>("Param","[]"));
        }else{
            Send_Param.add(new kotlin.Pair<String, String>("User_Name",""));
            Send_Param.add(new kotlin.Pair<String, String>("User_Pass",""));
            Send_Param.add(new kotlin.Pair<String, String>("Country", ""));
            Send_Param.add(new kotlin.Pair<String, String>("Service",getService()));
            Send_Param.add(new kotlin.Pair<String, String>("Param",myParser.toJson(getParam())));
        }
        Log.i(TAG, "Prepared Request: \n" + Send_Param.toString());
        Log.i(TAG,"Service Started");
        Fuel.post(DeepLife.API_URL, Send_Param).responseString(new Handler<String>() {
            @Override
            public void success(@NotNull Request request, @NotNull Response response, String s) {
                Log.i(TAG, "Request: \n" + request);
                Log.i(TAG, "Response: \n" + s);
                mySyncDatabase.ProcessResponse(s);
            }

            @Override
            public void failure(@NotNull Request request, @NotNull Response response, @NotNull FuelError fuelError) {
                Log.i(TAG, "Fuel failure: \n" + fuelError.toString());
            }
        });
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG,"Service Stopped");
        return true;
    }

    public String getService(){
        return "Update";
    }

    public String getParam() {
        return "[]";
    }
}
