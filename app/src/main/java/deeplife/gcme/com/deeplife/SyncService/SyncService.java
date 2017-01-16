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
import deeplife.gcme.com.deeplife.Testimony.Testimony;
import kotlin.Pair;
import me.tatarka.support.job.JobService;

/**
 * Created by bengeos on 12/16/16.
 */


public class SyncService extends JobService {
    public static final String TAG = "SyncService";

    public enum ApiRequest {
        USER_NAME("User_Name"),
        USER_PASS("User_Pass"),
        COUNTRY("Country"),
        SERVICE("Service"),
        PARAM("Param");

        private final String name;
        ApiRequest(String s) { this.name = s; }
        public boolean equalsName(String otherName) { return (otherName == null) ? false : name.equals(otherName); }
        @Override public String toString() { return this.name; }
    }

    // === ApiService ===
    public enum ApiService {
        // From apiController.php in Web App:
        GETALL_DISCIPLES("GetAll_Disciples"),
        GETNEW_DISCIPLES("GetNew_Disciples"),
        ADDNEW_DISCIPLES("AddNew_Disciples"),
        ADDNEW_DISCIPLES_LOG("AddNew_Disciples_Log"),
        DELETE_ALL_DISCIPLE_LOG("Delete_All_Disciple_Log"),
        GETALL_SCHEDULES("GetAll_Schedules"),
        GETNEW_SCHEDULES("GetNew_Schedules"),
        ADDNEW_SCHEDULES("AddNew_Schedules"),
        ADDNEW_SCHEDULE_LOG("AddNew_Schedule_Log"),
        DELETE_ALL_SCHEDULE_LOG("Delete_All_Schedule_Log"),
        ISVALID_USER("IsValid_User"),
        CREATEUSER("CreateUser"),
        GETALL_QUESTIONS("GetAll_Questions"),
        GETALL_ANSWERS("GetAll_Answers"),
        ADDNEW_ANSWERS("AddNew_Answers"),
        SEND_LOG("Send_Log"),
        LOG_IN("Log_In"),
        SIGN_UP("Sign_Up"),
        UPDATE_DISCIPLES("Update_Disciples"),
        UPDATE("Update"),
        META_DATA("Meta_Data"),
        SEND_REPORT("Send_Report"),
        GETNEW_NEWSFEED("GetNew_NewsFeed"),
        SEND_TESTIMONY("Send_Testimony"),
        UPLOAD_USER_PIC("Upload_User_Pic"),
        UPLOAD_DISCIPLE_PIC("Upload_Disciple_pic"),
        UPDATE_SCHEDULES("Update_Schedules"),
        GETALL_TESTIMONIES("GetAll_Testimonies"),
        GETNEW_TESTIMONIES("GetNew_Testimonies"),
        ADDNEW_TESTIMONY("AddNew_Testimony"),
        DELETE_TESTIMONY("Delete_Testimony"),
        ADDNEW_TESTIMONY_LOG("AddNew_Testimony_Log"),
        DELETE_ALL_TESTIMONYLOGS("Delete_All_TestimonyLogs"),
        GETALL_NEWSFEEDS("GetAll_NewsFeeds"),
        GETNEW_NEWSFEEDS("GetNew_NewsFeeds"),
        ADDNEW_NEWSFEED_LOG("AddNew_NewsFeed_Log"),
        DELETE_ALL_NEWSFEED_LOGS("Delete_All_NewsFeed_Logs"),
        GETALL_CATEGORY("GetAll_Category"),
        GETALL_LEARNINGTOOLS("GetAll_LearningTools"),
        GETNEW_LEARNINGTOOLS("GetNew_LearningTools"),
        DISCIPLETREE("DiscipleTree");


        private final String name;
        ApiService(String s) { this.name = s; }
        public boolean equalsName(String otherName) { return (otherName == null) ? false : name.equals(otherName); }
        @Override public String toString() { return this.name; }
    }

    //public static final String[] Sync_Tasks = {"Send_Log", "Send_Disciples","Remove_Disciple","Update_Disciple","Send_Schedule","Send_Report","Send_Testimony","Update_Schedules","AddNew_Disciples","Update_Disciples","Send_Testimony","Send_Answers","AddNew_Answers"};
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
                    Send_Param.add(new kotlin.Pair<String, String>(ApiRequest.USER_NAME.toString(), user.getUser_Email()));
                }else {
                    Send_Param.add(new kotlin.Pair<String, String>(ApiRequest.USER_NAME.toString(), user.getUser_Phone()));
                }
                Send_Param.add(new kotlin.Pair<String, String>(ApiRequest.USER_PASS.toString(), user.getUser_Pass()));
                Send_Param.add(new kotlin.Pair<String, String>(ApiRequest.COUNTRY.toString(), user.getUser_Country()));
                Send_Param.add(new kotlin.Pair<String, String>(ApiRequest.SERVICE.toString(), myLogs.getService().toString()));
                Send_Param.add(new kotlin.Pair<String, String>(ApiRequest.PARAM.toString(), myParser.toJson(myLogs.getParam())));
            }else{
                Send_Param.add(new kotlin.Pair<String, String>(ApiRequest.USER_NAME.toString(), " "));
                Send_Param.add(new kotlin.Pair<String, String>(ApiRequest.USER_PASS.toString(), " "));
                Send_Param.add(new kotlin.Pair<String, String>(ApiRequest.COUNTRY.toString(), " "));
                Send_Param.add(new kotlin.Pair<String, String>(ApiRequest.SERVICE.toString(), myLogs.getService().toString()));
                Send_Param.add(new kotlin.Pair<String, String>(ApiRequest.PARAM.toString(), myParser.toJson(myLogs.getParam())));
            }
            // Temp
//            Log.i(TAG, "Enum test1: " + Task.ADD_NEW_ANSWERS);
//            Log.i(TAG, "Enum test2: " + Task.ADD_NEW_ANSWERS.name());
//            Log.i(TAG, "Enum test3: " + Task.valueOf("ADD_NEW_ANSWERS"));
//            Log.i(TAG, "Enum test4: " + Task.SEND_ANSWERS);

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
            myLogs.setService(ApiService.SEND_LOG);
        }else if(DeepLife.myDATABASE.getSendDisciples().size()>0){
            Log.i(TAG,"GET DISCIPLES TO SEND -> \n");
            ArrayList<Disciple> foundData = DeepLife.myDATABASE.getSendDisciples();
            for(int i=0;i<foundData.size();i++){
                myLogs.getParam().add(foundData.get(i));
            }
            //myLogs.setService(ApiService.SEND_DISCIPLES);  // briggsm: Are we sure? Biniam had AddNew_Disciples here. Is that right? I'm changing to Send_Disciples.
            myLogs.setService(ApiService.ADDNEW_DISCIPLES);
        }else if(DeepLife.myDATABASE.getTopImageSync() != null){
            Log.i(TAG,"GET Images TO SEND -> \n");
            ImageSync tosync = DeepLife.myDATABASE.getTopImageSync();
            ImageSync img = new ImageSync();
            img.setImage(encodeImage(tosync.getFilePath()));
            img.setParam(tosync.getParam());
            img.setId(tosync.getId());
            myLogs.getParam().add(img);
            //myLogs.setService(tosync.getParam());
            myLogs.setService(ApiService.valueOf(tosync.getParam())); // briggsm: not 100% sure this will work... TODO: debug.
        }else if(DeepLife.myDATABASE.getUpdateDisciples().size()>0){
            Log.i(TAG,"GET DISCIPLES TO UPDATE -> \n");
            
            ArrayList<Disciple> foundData = DeepLife.myDATABASE.getUpdateDisciples();
            for(int i=0;i<foundData.size();i++){
                myLogs.getParam().add(foundData.get(i));
            }
            myLogs.setService(ApiService.UPDATE_DISCIPLES);
        }else if(DeepLife.myDATABASE.getSendAnswers().size()>0){
            Log.i(TAG,"GET Answers TO Send -> \n");
            ArrayList<Answer> foundData = DeepLife.myDATABASE.getSendAnswers();
            for(int i=0;i<foundData.size();i++){
                myLogs.getParam().add(foundData.get(i));
            }
            //myLogs.setService(Sync_Tasks[12]); // briggsm: Are we sure? Biniam had AddNewAnswers here. Is that right? I'm changing to Send_Answers
            //myLogs.setService(ApiService.SEND_ANSWERS);
            myLogs.setService(ApiService.ADDNEW_ANSWERS);
        }else if(DeepLife.myDATABASE.getSendSchedules().size()>0){
            Log.i(TAG,"GET Schedules TO Send -> \n");
            ArrayList<Schedule> foundData = DeepLife.myDATABASE.getSendSchedules();
            for(int i=0;i<foundData.size();i++){
                myLogs.getParam().add(foundData.get(i));
            }
            //myLogs.setService(ApiService.SEND_SCHEDULE);
            myLogs.setService(ApiService.ADDNEW_SCHEDULES);
        }else if(DeepLife.myDATABASE.getUpdateSchedules().size()>0){
            Log.i(TAG,"GET Schedules TO UPDATE -> \n");
            ArrayList<Schedule> foundData = DeepLife.myDATABASE.getUpdateSchedules();
            for(int i=0;i<foundData.size();i++){
                myLogs.getParam().add(foundData.get(i));
            }
            myLogs.setService(ApiService.UPDATE_SCHEDULES);
        }else if(DeepLife.myDATABASE.getSendReports().size()>0){
            Log.i(TAG,"GET Reports TO Send -> \n");
            ArrayList<ReportItem> foundData = DeepLife.myDATABASE.getSendReports();
            for(int i=0;i<foundData.size();i++){
                myLogs.getParam().add(foundData.get(i));
            }
            myLogs.setService(ApiService.SEND_REPORT);
        }else if(DeepLife.myDATABASE.getSendTestimony().size()>0){
            Log.i(TAG,"GET Testimony TO Send -> \n");
            ArrayList<Testimony> foundData = DeepLife.myDATABASE.getSendTestimony();
            for(int i=0;i<foundData.size();i++){
                myLogs.getParam().add(foundData.get(i));
            }
            myLogs.setService(ApiService.SEND_TESTIMONY);
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
