package deeplife.gcme.com.deeplife.SyncService;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Disciples.Disciple;
import deeplife.gcme.com.deeplife.FileManager.FileManager;
import deeplife.gcme.com.deeplife.Models.Answer;
import deeplife.gcme.com.deeplife.Models.ImageSync;
import deeplife.gcme.com.deeplife.Models.Logs;
import deeplife.gcme.com.deeplife.Models.ReportItem;
import deeplife.gcme.com.deeplife.Models.Schedule;
import deeplife.gcme.com.deeplife.Models.User;
import deeplife.gcme.com.deeplife.SendParam;
import deeplife.gcme.com.deeplife.Testimony.Testimony;
import kotlin.Pair;
import me.tatarka.support.job.JobService;

/**
 * Created by bengeos on 12/16/16.
 */


public class SyncService extends JobService {
    public static final String TAG = "SyncService";

    public static final String[] Sync_Tasks = {"Send_Log", "Send_Disciples","Remove_Disciple","Update_Disciple","Send_Schedule","Send_Report","Send_Testimony","Update_Schedules","AddNew_Disciples","Update_Disciples","Send_Testimony","Send_Answers","AddNew_Answers"};
    private List<Object> Param;
    private Gson myParser;
    private List<kotlin.Pair<String,String>> Send_Param;
    private User user;
    private FileManager myFileManager;
    private MultipartUtility myMultipartUtility;
    private boolean isUploading;
    private SyncDatabase mySyncDatabase;
    private Logs myLogs;

    public SyncService() {
        myLogs = new Logs();
        myParser = new Gson();
        mySyncDatabase = new SyncDatabase();
    }

    @Override
    public boolean onStartJob(me.tatarka.support.job.JobParameters params) {
        Log.i(TAG, "The Job scheduler started");
        try{
            user = DeepLife.myDATABASE.getMainUser();
            Send_Param = new ArrayList<Pair<String,String>>();
            getService();
            if(user != null ){
                if(user.getUser_Email() != null){
                    Send_Param.add(new kotlin.Pair<String, String>(SendParam.USER_NAME, user.getUser_Email()));
                }else {
                    Send_Param.add(new kotlin.Pair<String, String>(SendParam.USER_NAME, user.getUser_Phone()));
                }
                Send_Param.add(new kotlin.Pair<String, String>(SendParam.USER_PASS, user.getUser_Pass()));
                Send_Param.add(new kotlin.Pair<String, String>(SendParam.COUNTRY, user.getUser_Country()));
                Send_Param.add(new kotlin.Pair<String, String>(SendParam.SERVICE, myLogs.getService()));
                Send_Param.add(new kotlin.Pair<String, String>(SendParam.PARAM, myParser.toJson(myLogs.getParam())));
            }else{
                Send_Param.add(new kotlin.Pair<String, String>(SendParam.USER_NAME, " "));
                Send_Param.add(new kotlin.Pair<String, String>(SendParam.USER_PASS, " "));
                Send_Param.add(new kotlin.Pair<String, String>(SendParam.COUNTRY, " "));
                Send_Param.add(new kotlin.Pair<String, String>(SendParam.SERVICE, myLogs.getService()));
                Send_Param.add(new kotlin.Pair<String, String>(SendParam.PARAM, myParser.toJson(myLogs.getParam())));
            }
            Log.i(TAG, "Prepared Request: \n" + Send_Param.toString());
            Log.i(TAG,"Service Started for \n"+DeepLife.API_URL);
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
        }catch (Exception e){
            Log.i(TAG, "The Job scheduler Failed ...."+e.toString());
        }
        jobFinished(params, false);
        return true;
    }

    @Override
    public boolean onStopJob(me.tatarka.support.job.JobParameters params) {
        Log.i(TAG,"Service Stopped");
        return false;
    }
    public void getService(){
        Logs found = getParam();
        if(found != null){
            myLogs = getParam();
        }else {
            myLogs = new Logs();
        }

    }

    public Logs getParam(){
        Logs myLogs = new Logs();
        if(DeepLife.myDATABASE.getSendLogs().size()>0){
            Log.i(TAG,"GET LOG TO SEND -> \n");
            ArrayList<Logs> foundData = DeepLife.myDATABASE.getSendLogs();
            for(int i=0;i<foundData.size();i++){
                myLogs.getParam().add(foundData.get(i));
            }
            myLogs.setService(Sync_Tasks[0]);
        }else if(DeepLife.myDATABASE.getSendDisciples().size()>0){
            Log.i(TAG,"GET DISCIPLES TO SEND -> \n");
            ArrayList<Disciple> foundData = DeepLife.myDATABASE.getSendDisciples();
            for(int i=0;i<foundData.size();i++){
                myLogs.getParam().add(foundData.get(i));
            }
            myLogs.setService(Sync_Tasks[8]);
        }else if(DeepLife.myDATABASE.getTopImageSync() != null){
            Log.i(TAG,"GET Images TO SEND -> \n");
            ImageSync tosync = DeepLife.myDATABASE.getTopImageSync();
            ImageSync img = new ImageSync();
            img.setImage(encodeImage(tosync.getFilePath()));
            img.setParam(tosync.getParam());
            img.setId(tosync.getId());
            myLogs.getParam().add(img);
            myLogs.setService(tosync.getParam());
        }else if(DeepLife.myDATABASE.getUpdateDisciples().size()>0){
            Log.i(TAG,"GET DISCIPLES TO UPDATE -> \n");
            ArrayList<Disciple> foundData = DeepLife.myDATABASE.getUpdateDisciples();
            for(int i=0;i<foundData.size();i++){
                myLogs.getParam().add(foundData.get(i));
            }
            myLogs.setService(Sync_Tasks[9]);
        }else if(DeepLife.myDATABASE.getSendAnswers().size()>0){
            Log.i(TAG,"GET Answers TO Send -> \n");
            ArrayList<Answer> foundData = DeepLife.myDATABASE.getSendAnswers();
            for(int i=0;i<foundData.size();i++){
                myLogs.getParam().add(foundData.get(i));
            }
            myLogs.setService(Sync_Tasks[12]);
        }else if(DeepLife.myDATABASE.getSendSchedules().size()>0){
            Log.i(TAG,"GET Schedules TO Send -> \n");
            ArrayList<Schedule> foundData = DeepLife.myDATABASE.getSendSchedules();
            for(int i=0;i<foundData.size();i++){
                myLogs.getParam().add(foundData.get(i));
            }
            myLogs.setService(Sync_Tasks[4]);
        }else if(DeepLife.myDATABASE.getUpdateSchedules().size()>0){
            Log.i(TAG,"GET Schedules TO UPDATE -> \n");
            ArrayList<Schedule> foundData = DeepLife.myDATABASE.getUpdateSchedules();
            for(int i=0;i<foundData.size();i++){
                myLogs.getParam().add(foundData.get(i));
            }
            myLogs.setService(Sync_Tasks[7]);
        }else if(DeepLife.myDATABASE.getSendReports().size()>0){
            Log.i(TAG,"GET Reports TO Send -> \n");
            ArrayList<ReportItem> foundData = DeepLife.myDATABASE.getSendReports();
            for(int i=0;i<foundData.size();i++){
                myLogs.getParam().add(foundData.get(i));
            }
            myLogs.setService(Sync_Tasks[5]);
        }else if(DeepLife.myDATABASE.getSendTestimony().size()>0){
            Log.i(TAG,"GET Testimony TO Send -> \n");
            ArrayList<Testimony> foundData = DeepLife.myDATABASE.getSendTestimony();
            for(int i=0;i<foundData.size();i++){
                myLogs.getParam().add(foundData.get(i));
            }
            myLogs.setService(Sync_Tasks[6]);
        }
        return myLogs;
    }

    public static String encodeImage(String filePath) {
        File myFile = new File(filePath);
        if(myFile.isFile()){
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
            byte[] ba = bao.toByteArray();
            return Base64.encodeToString(ba, 0);
        }
        return filePath;
    }
}
