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

    // === API_REQUEST ===
    public enum API_REQUEST {
        USER_NAME("User_Name"),
        USER_PASS("User_Pass"),
        COUNTRY("Country"),
        SERVICE("Service"),
        PARAM("Param");

        private final String name;

        API_REQUEST(String s) {
            this.name = s;
        }

        public boolean equalsName(String otherName) {
            return (otherName == null) ? false : name.equals(otherName);
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    // === API_SERVICE ===
    public enum API_SERVICE {
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
        SEND_TESTIMONY("addnew_testimony"),
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
        DISCIPLETREE("DiscipleTree"),
        UPDATE_PROFILE("Update_Profile");


        private final String name;

        API_SERVICE(String s) {
            this.name = s;
        }

        public boolean equalsName(String otherName) {
            return (otherName == null) ? false : name.equals(otherName);
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    //public static final String[] Sync_Tasks = {"Send_Log", "Send_Disciples","Remove_Disciple","Update_Disciple","Send_Schedule","Send_Report","Send_Testimony","Update_Schedules","AddNew_Disciples","Update_Disciples","Send_Testimony","Send_Answers","AddNew_Answers"};
    private List<Object> Param;
    private Gson myParser;
    private List<kotlin.Pair<String, String>> Send_Param;
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
        Log.i(TAG, "---------------------------------------------");
        Log.i(TAG, "The Job scheduler started");
        try {
            user = DeepLife.myDATABASE.getMainUser();
            Send_Param = new ArrayList<Pair<String, String>>();
            getService();
            if (user != null) {
                if (user.getEmail() != null) {
                    Send_Param.add(new kotlin.Pair<String, String>(API_REQUEST.USER_NAME.toString(), user.getEmail()));
                } else {
                    Send_Param.add(new kotlin.Pair<String, String>(API_REQUEST.USER_NAME.toString(), user.getPhone()));
                }
                Send_Param.add(new kotlin.Pair<String, String>(API_REQUEST.USER_PASS.toString(), user.getPass()));
                Send_Param.add(new kotlin.Pair<String, String>(API_REQUEST.COUNTRY.toString(), user.getCountry()));
                Send_Param.add(new kotlin.Pair<String, String>(API_REQUEST.SERVICE.toString(), myLogs.getService().toString()));
                Send_Param.add(new kotlin.Pair<String, String>(API_REQUEST.PARAM.toString(), myParser.toJson(myLogs.getParam())));
            } else {
                Send_Param.add(new kotlin.Pair<String, String>(API_REQUEST.USER_NAME.toString(), " "));
                Send_Param.add(new kotlin.Pair<String, String>(API_REQUEST.USER_PASS.toString(), " "));
                Send_Param.add(new kotlin.Pair<String, String>(API_REQUEST.COUNTRY.toString(), " "));
                Send_Param.add(new kotlin.Pair<String, String>(API_REQUEST.SERVICE.toString(), myLogs.getService().toString()));
                Send_Param.add(new kotlin.Pair<String, String>(API_REQUEST.PARAM.toString(), myParser.toJson(myLogs.getParam())));
            }
            Log.i(TAG, "API Request: " + Send_Param.toString());
            Fuel.post(DeepLife.API_URL, Send_Param).responseString(new Handler<String>() {
                @Override
                public void success(@NotNull Request request, @NotNull Response response, String s) {
                    Log.d(TAG, "Request: " + request);
                    Log.i(TAG, "Response: " + s);
                    mySyncDatabase.ProcessResponse(s);
                }

                @Override
                public void failure(@NotNull Request request, @NotNull Response response, @NotNull FuelError fuelError) {
                    Log.e(TAG, "Fuel failure: \n" + fuelError.toString());
                }
            });
            Log.i(TAG, "API Request SENT to: " + DeepLife.API_URL);
        } catch (Exception e) {
            Log.e(TAG, "The Job scheduler Failed ...." + e.toString());
        }
        jobFinished(params, false);
        return true;
    }

    @Override
    public boolean onStopJob(me.tatarka.support.job.JobParameters params) {
        Log.i(TAG, "Service Stopped");
        return false;
    }

    public void getService() {
        Logs found = getParam();
        if (found != null) {
            //myLogs = getParam();  // briggsm: Don't need to call getParam() twice.
            myLogs = found;
        } else {
            myLogs = new Logs();
        }

    }

    public Logs getParam() {
        // Parameter Populator
        Logs myLogs = new Logs();
        if (DeepLife.myDATABASE.getSendLogs().size() > 0) {
            Log.i(TAG, "GET LOG TO SEND...");
            ArrayList<Logs> foundData = DeepLife.myDATABASE.getSendLogs();
            for (int i = 0; i < foundData.size(); i++) {
                myLogs.getParam().add(foundData.get(i));
            }
            myLogs.setService(API_SERVICE.SEND_LOG);
        } else if (DeepLife.myDATABASE.getSendDisciples().size() > 0) {
            Log.i(TAG, "GET DISCIPLES TO SEND...");
            ArrayList<Disciple> foundData = DeepLife.myDATABASE.getSendDisciples();
            for (int i = 0; i < foundData.size(); i++) {
                myLogs.getParam().add(foundData.get(i));
            }
            //myLogs.setService(API_SERVICE.SEND_DISCIPLES);  // briggsm: Are we sure? Biniam had AddNew_Disciples here. Is that right? I'm changing to Send_Disciples.
            myLogs.setService(API_SERVICE.ADDNEW_DISCIPLES);
        } else if (DeepLife.myDATABASE.getTopImageSync() != null) {
            Log.i(TAG, "GET Images TO SEND...");
            ImageSync tosync = DeepLife.myDATABASE.getTopImageSync();
            ImageSync img = new ImageSync();
            img.setImage(encodeImage(tosync.getFilePath()));
            img.setParam(tosync.getParam());
            img.setId(tosync.getId());
            myLogs.getParam().add(img);
            //myLogs.setService(tosync.getParam());
            myLogs.setService(API_SERVICE.valueOf(tosync.getParam())); // briggsm: not 100% sure this will work... TODO: debug.
        } else if (DeepLife.myDATABASE.getUpdateDisciples().size() > 0) {
            Log.i(TAG, "GET DISCIPLES TO UPDATE...");

            ArrayList<Disciple> foundData = DeepLife.myDATABASE.getUpdateDisciples();
            for (int i = 0; i < foundData.size(); i++) {
                myLogs.getParam().add(foundData.get(i));
            }
            myLogs.setService(API_SERVICE.UPDATE_DISCIPLES);
        } else if (DeepLife.myDATABASE.getSendAnswers().size() > 0) {
            Log.i(TAG, "GET Answers TO Send...");
            ArrayList<Answer> foundData = DeepLife.myDATABASE.getSendAnswers();
            for (int i = 0; i < foundData.size(); i++) {
                myLogs.getParam().add(foundData.get(i));
            }
            //myLogs.setService(Sync_Tasks[12]); // briggsm: Are we sure? Biniam had AddNewAnswers here. Is that right? I'm changing to Send_Answers
            //myLogs.setService(API_SERVICE.SEND_ANSWERS);
            myLogs.setService(API_SERVICE.ADDNEW_ANSWERS);
        } else if (DeepLife.myDATABASE.getSendSchedules().size() > 0) {
            Log.i(TAG, "GET Schedules TO Send...");
            ArrayList<Schedule> foundData = DeepLife.myDATABASE.getSendSchedules();
            for (int i = 0; i < foundData.size(); i++) {
                myLogs.getParam().add(foundData.get(i));
            }
            //myLogs.setService(API_SERVICE.SEND_SCHEDULE);
            myLogs.setService(API_SERVICE.ADDNEW_SCHEDULES);
        } else if (DeepLife.myDATABASE.getUpdateSchedules().size() > 0) {
            Log.i(TAG, "GET Schedules TO UPDATE...");
            ArrayList<Schedule> foundData = DeepLife.myDATABASE.getUpdateSchedules();
            for (int i = 0; i < foundData.size(); i++) {
                myLogs.getParam().add(foundData.get(i));
            }
            myLogs.setService(API_SERVICE.UPDATE_SCHEDULES);
        } else if (DeepLife.myDATABASE.getSendReports().size() > 0) {
            Log.i(TAG, "GET Reports TO Send...");
            ArrayList<ReportItem> foundData = DeepLife.myDATABASE.getSendReports();
            for (int i = 0; i < foundData.size(); i++) {
                myLogs.getParam().add(foundData.get(i));
            }
            myLogs.setService(API_SERVICE.SEND_REPORT);
        } else if (DeepLife.myDATABASE.getSendTestimony().size() > 0) {
            Log.i(TAG, "GET Testimony TO Send...");
            ArrayList<Testimony> foundData = DeepLife.myDATABASE.getSendTestimony();
            for (int i = 0; i < foundData.size(); i++) {
                myLogs.getParam().add(foundData.get(i));
            }
            myLogs.setService(API_SERVICE.SEND_TESTIMONY);
        } else if (DeepLife.myDATABASE.getUpdateUserProfile().size() > 0) {
            Log.i(TAG, "GET USER PROFILE TO UPDATE...");

            ArrayList<User> foundData = DeepLife.myDATABASE.getUpdateUserProfile();
            for (int i = 0; i < foundData.size(); i++) {
                myLogs.getParam().add(foundData.get(i));
            }
            myLogs.setService(API_SERVICE.UPDATE_PROFILE);
        }
        return myLogs;
    }

    public static String encodeImage(String filePath) {
        File myFile = new File(filePath);
        if (myFile.isFile()) {
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
            byte[] ba = bao.toByteArray();
            return Base64.encodeToString(ba, 0);
        }
        return filePath;
    }
}
