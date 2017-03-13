package deeplife.gcme.com.deeplife.SyncService;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Disciples.Disciple;
import deeplife.gcme.com.deeplife.FileManager.FileManager;
import deeplife.gcme.com.deeplife.LearningTools.LearningTool;
import deeplife.gcme.com.deeplife.Models.Answer;
import deeplife.gcme.com.deeplife.Models.Category;
import deeplife.gcme.com.deeplife.Models.Country;
import deeplife.gcme.com.deeplife.Models.DiscipleTreeCount;
import deeplife.gcme.com.deeplife.Models.ImageSync;
import deeplife.gcme.com.deeplife.Models.Logs;
import deeplife.gcme.com.deeplife.Models.ReportItem;
import deeplife.gcme.com.deeplife.Models.Schedule;
import deeplife.gcme.com.deeplife.Models.User;
import deeplife.gcme.com.deeplife.News.News;
import deeplife.gcme.com.deeplife.Testimony.Testimony;
import deeplife.gcme.com.deeplife.Testimony.TestimonyFragment;
import deeplife.gcme.com.deeplife.Wbs.WbsQuestion;
import kotlin.Pair;

/**
 * Created by bengeos on 2/24/17.
 */

public class NewSyncService extends Service {
    private static final String TAG = "NewSyncService";

    // === API_REQUEST ===
    private enum API_REQUEST {
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
    private enum API_SERVICE {
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
    private static List<Object> Param;
    private static Gson myParser;
    private static List<kotlin.Pair<String, String>> Send_Param;
    private static User user;
    private static MultipartUtility myMultipartUtility;
    private static boolean isUploading;
    private static SyncDatabase mySyncDatabase;
    private static Logs myLogs;
    private static boolean isInprogress = false;
    private static Context myContext;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myLogs = new Logs();
        myParser = new Gson();
        mySyncDatabase = new SyncDatabase();
        myContext = this;
        DeepLife.isSyncLoaded = true;
    }

    public static void StartSync(){
        if(!isInprogress){
            SyncJob();
//            Toast.makeText(myContext,"NewSync ReStarted",Toast.LENGTH_LONG).show();
        }else {
//            Toast.makeText(myContext,"NewSync is in progress",Toast.LENGTH_LONG).show();
        }
    }

    private static void SyncJob(){
        isInprogress = true;
        Log.i(TAG, "---------------------------------------------");
        Log.i(TAG, "The Job scheduler started");
        try {
            user = DeepLife.myDATABASE.getMainUser();
            Send_Param = new ArrayList<Pair<String, String>>();
            getService();
            if (user != null) {
                if (user.getEmail() != null) {
                    Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.USER_NAME.toString(), user.getEmail()));
                } else {
                    Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.USER_NAME.toString(), user.getPhone()));
                }
                Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.USER_PASS.toString(), user.getPass()));
                Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.COUNTRY.toString(), user.getCountry()));
                Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.SERVICE.toString(), myLogs.getService().toString()));
                Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.PARAM.toString(), myParser.toJson(myLogs.getParam())));
            } else {
                Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.USER_NAME.toString(), " "));
                Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.USER_PASS.toString(), " "));
                Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.COUNTRY.toString(), " "));
                Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.SERVICE.toString(), myLogs.getService().toString()));
                Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.PARAM.toString(), myParser.toJson(myLogs.getParam())));
            }
            Log.i(TAG, "API Request: " + Send_Param.toString());
            Fuel.post(DeepLife.API_URL, Send_Param).responseString(new Handler<String>() {
                @Override
                public void success(@NotNull Request request, @NotNull Response response, String s) {
                    Log.d(TAG, "Request: " + request);
                    Log.i(TAG, "Response: " + s);
                    ProcessResponse(s);
                    isInprogress = false;
                }

                @Override
                public void failure(@NotNull Request request, @NotNull Response response, @NotNull FuelError fuelError) {
                    isInprogress = false;
                    Log.e(TAG, "Fuel failure: \n" + fuelError.toString());
                }
            });
            Log.i(TAG, "API Request SENT to: " + DeepLife.API_URL);
        } catch (Exception e) {
            Log.e(TAG, "The Job scheduler Failed ...." + e.toString());
            isInprogress = false;
        }
    }

    private static void getService() {
        Logs found = getParam();
        if (found != null) {
            //myLogs = getParam();  // briggsm: Don't need to call getParam() twice.
            myLogs = found;
        } else {
            myLogs = new Logs();
        }

    }

    private static Logs getParam() {
        // Parameter Populator
        Logs myLogs = new Logs();
        if (DeepLife.myDATABASE.getSendLogs().size() > 0) {
            Log.i(TAG, "GET LOG TO SEND...");
            ArrayList<Logs> foundData = DeepLife.myDATABASE.getSendLogs();
            for (int i = 0; i < foundData.size(); i++) {
                myLogs.getParam().add(foundData.get(i));
            }
            myLogs.setService(SyncService.API_SERVICE.SEND_LOG);
        } else if (DeepLife.myDATABASE.getSendDisciples().size() > 0) {
            Log.i(TAG, "GET DISCIPLES TO SEND...");
            ArrayList<Disciple> foundData = DeepLife.myDATABASE.getSendDisciples();
            for (int i = 0; i < foundData.size(); i++) {
                myLogs.getParam().add(foundData.get(i));
            }
            //myLogs.setService(API_SERVICE.SEND_DISCIPLES);  // briggsm: Are we sure? Biniam had AddNew_Disciples here. Is that right? I'm changing to Send_Disciples.
            myLogs.setService(SyncService.API_SERVICE.ADDNEW_DISCIPLES);
        } else if (DeepLife.myDATABASE.getTopImageSync() != null) {
            Log.i(TAG, "GET Images TO SEND...");
            ImageSync tosync = DeepLife.myDATABASE.getTopImageSync();
            ImageSync img = new ImageSync();
            img.setImage(encodeImage(tosync.getFilePath()));
            img.setParam(tosync.getParam());
            img.setId(tosync.getId());
            myLogs.getParam().add(img);
            //myLogs.setService(tosync.getParam());
            myLogs.setService(SyncService.API_SERVICE.valueOf(tosync.getParam())); // briggsm: not 100% sure this will work... TODO: debug.
        } else if (DeepLife.myDATABASE.getUpdateDisciples().size() > 0) {
            Log.i(TAG, "GET DISCIPLES TO UPDATE...");

            ArrayList<Disciple> foundData = DeepLife.myDATABASE.getUpdateDisciples();
            for (int i = 0; i < foundData.size(); i++) {
                myLogs.getParam().add(foundData.get(i));
            }
            myLogs.setService(SyncService.API_SERVICE.UPDATE_DISCIPLES);
        } else if (DeepLife.myDATABASE.getSendAnswers().size() > 0) {
            Log.i(TAG, "GET Answers TO Send...");
            ArrayList<Answer> foundData = DeepLife.myDATABASE.getSendAnswers();
            for (int i = 0; i < foundData.size(); i++) {
                myLogs.getParam().add(foundData.get(i));
            }
            //myLogs.setService(Sync_Tasks[12]); // briggsm: Are we sure? Biniam had AddNewAnswers here. Is that right? I'm changing to Send_Answers
            //myLogs.setService(API_SERVICE.SEND_ANSWERS);
            myLogs.setService(SyncService.API_SERVICE.ADDNEW_ANSWERS);
        } else if (DeepLife.myDATABASE.getSendSchedules().size() > 0) {
            Log.i(TAG, "GET Schedules TO Send...");
            ArrayList<Schedule> foundData = DeepLife.myDATABASE.getSendSchedules();
            for (int i = 0; i < foundData.size(); i++) {
                myLogs.getParam().add(foundData.get(i));
            }
            //myLogs.setService(API_SERVICE.SEND_SCHEDULE);
            myLogs.setService(SyncService.API_SERVICE.ADDNEW_SCHEDULES);
        } else if (DeepLife.myDATABASE.getUpdateSchedules().size() > 0) {
            Log.i(TAG, "GET Schedules TO UPDATE...");
            ArrayList<Schedule> foundData = DeepLife.myDATABASE.getUpdateSchedules();
            for (int i = 0; i < foundData.size(); i++) {
                myLogs.getParam().add(foundData.get(i));
            }
            myLogs.setService(SyncService.API_SERVICE.UPDATE_SCHEDULES);
        } else if (DeepLife.myDATABASE.getSendReports().size() > 0) {
            Log.i(TAG, "GET Reports TO Send...");
            ArrayList<ReportItem> foundData = DeepLife.myDATABASE.getSendReports();
            for (int i = 0; i < foundData.size(); i++) {
                myLogs.getParam().add(foundData.get(i));
            }
            myLogs.setService(SyncService.API_SERVICE.SEND_REPORT);
        } else if (DeepLife.myDATABASE.getSendTestimony().size() > 0) {
            Log.i(TAG, "GET Testimony TO Send...");
            ArrayList<Testimony> foundData = DeepLife.myDATABASE.getSendTestimony();
            for (int i = 0; i < foundData.size(); i++) {
                myLogs.getParam().add(foundData.get(i));
            }
            myLogs.setService(SyncService.API_SERVICE.SEND_TESTIMONY);
        } else if (DeepLife.myDATABASE.getUpdateUserProfile().size() > 0) {
            Log.i(TAG, "GET USER PROFILE TO UPDATE...");

            ArrayList<User> foundData = DeepLife.myDATABASE.getUpdateUserProfile();
            for (int i = 0; i < foundData.size(); i++) {
                myLogs.getParam().add(foundData.get(i));
            }
            myLogs.setService(SyncService.API_SERVICE.UPDATE_PROFILE);
        }
        return myLogs;
    }

    private static String encodeImage(String filePath) {
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






    public enum ApiResponseKey {
        RESPONSE("Response"),
        NEWSFEEDS("NewsFeeds"),
        TESTIMONIES("Testimonies"),
        DISCIPLES("Disciples"),
        QUESTIONS("Questions"),
        CATEGORIES("Categories"),
        ANSWERS("Answers"),
        COUNTRY_UC("Country"),
        LOG_RESPONSE("Log_Response"),
        PROFILE("Profile"),
        LEARNINGTOOLS("LearningTools"),
        ID("id"),
        CONTENT("content"),
        IMAGE_URL("image_url"),
        PUBLISH_DATE("publish_date"),
        USER_ID("user_id"),
        CREATED("created"),
        USER_NAME("user_name"),
        DISPLAYNAME("displayName"),
        EMAIL("email"),
        PHONE_NO("phone_no"),
        MENTOR_ID("mentor_id"),
        ROLE_ID("role_id"),
        GENDER("gender"),
        CATEGORY("category"),
        QUESTION("question"),
        MANDATORY("mandatory"),
        TYPE("type"),
        COUNTRY_LC("country"),
        PARENT("parent"),
        STATUS("status"),
        DISCIPLE_PHONE("disciple_phone"),
        QUESTION_ID("question_id"),
        ANSWER("answer"),
        STAGE("stage"),
        PASS("pass"),
        PICTURE("picture"),
        FIRSTNAME("firstName"),
        TITLE("title"),
        DESCRIPTION("description"),
        IFRAMCODE("iframcode"),
        DEFAULT_LEARN("default_learn"),
        ISO3("iso3"),
        NAME("name"),
        CODE("code"),
        LOG_ID("Log_ID");


        private final String name;

        ApiResponseKey(String s) {
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

    private static void ProcessResponse(String jsonArray) {
        Gson myGson = new Gson();
        try {
            JSONObject myObject = (JSONObject) new JSONTokener(jsonArray).nextValue();
            Log.d(TAG, "Server Response (ProcessResponse): " + myObject.toString());
            if (!myObject.isNull(SyncDatabase.ApiResponseKey.RESPONSE.toString())) {
                JSONObject json_response = myObject.getJSONObject(SyncDatabase.ApiResponseKey.RESPONSE.toString());
                if (!json_response.isNull(SyncDatabase.ApiResponseKey.NEWSFEEDS.toString())) {
                    JSONArray json_newsfeeds = json_response.getJSONArray(SyncDatabase.ApiResponseKey.NEWSFEEDS.toString());
                    Log.d(TAG, "News Feeds: \n" + json_newsfeeds.toString());
                    Add_News(json_newsfeeds);
                }
                if (!json_response.isNull(SyncDatabase.ApiResponseKey.TESTIMONIES.toString())) {
                    JSONArray json_testimonies = json_response.getJSONArray(SyncDatabase.ApiResponseKey.TESTIMONIES.toString());
                    Log.d(TAG, "Testimonies: \n" + json_testimonies.toString());
                    Add_Testimony(json_testimonies);
                }
                if (!json_response.isNull(SyncDatabase.ApiResponseKey.DISCIPLES.toString())) {
                    JSONArray json_disciples = json_response.getJSONArray(SyncDatabase.ApiResponseKey.DISCIPLES.toString());
                    Log.d(TAG, "Disciples: \n" + json_disciples.toString());
                    Add_Disciples(json_disciples);
                }
                if (!json_response.isNull(SyncDatabase.ApiResponseKey.QUESTIONS.toString())) {
                    JSONArray json_questions = json_response.getJSONArray(SyncDatabase.ApiResponseKey.QUESTIONS.toString());
                    Log.d(TAG, "Questions: \n" + json_questions.toString());
                    Add_Questions(json_questions);
                }
                if (!json_response.isNull(SyncDatabase.ApiResponseKey.CATEGORIES.toString())) {
                    JSONArray json_categories = json_response.getJSONArray(SyncDatabase.ApiResponseKey.CATEGORIES.toString());
                    Log.d(TAG, "Categories: \n" + json_categories.toString());
                    Add_Category(json_categories);
                }
                if (!json_response.isNull(SyncDatabase.ApiResponseKey.ANSWERS.toString())) {
                    JSONArray json_answers = json_response.getJSONArray(SyncDatabase.ApiResponseKey.ANSWERS.toString());
                    Log.d(TAG, "Answers: \n" + json_answers.toString());
                    Add_Answers(json_answers);
                }
                if (!json_response.isNull(SyncDatabase.ApiResponseKey.COUNTRY_UC.toString())) {
                    JSONArray json_answers = json_response.getJSONArray(SyncDatabase.ApiResponseKey.COUNTRY_UC.toString());
                    Log.d(TAG, "Country: \n" + json_answers.toString());
                    Add_Countries(json_answers);
                }
                if (!json_response.isNull(SyncDatabase.ApiResponseKey.LOG_RESPONSE.toString())) {
                    JSONArray json_logs = json_response.getJSONArray(SyncDatabase.ApiResponseKey.LOG_RESPONSE.toString());
                    Log.d(TAG, "Deleting Logs: \n" + json_logs.toString());
                    Delete_Logs(json_logs);
                }
                if (!json_response.isNull(SyncDatabase.ApiResponseKey.PROFILE.toString())) {
                    JSONObject json_user_profile = json_response.getJSONObject(SyncDatabase.ApiResponseKey.PROFILE.toString());
                    Log.d(TAG, "Updating Main User: \n" + json_user_profile.toString());
                    Update_MainUser(json_user_profile);
                }
                if (!json_response.isNull(SyncDatabase.ApiResponseKey.LEARNINGTOOLS.toString())) {
                    JSONArray json_learning_tools = json_response.getJSONArray(SyncDatabase.ApiResponseKey.LEARNINGTOOLS.toString());
                    Log.d(TAG, "Add Learning tools: \n" + json_learning_tools.toString());
                    Add_LearningTools(json_learning_tools);
                }
                if (!json_response.isNull(SyncDatabase.ApiResponseKey.DISCIPLE_TREE.toString())) {
                    JSONObject json_disciple_tree = json_response.getJSONObject(SyncDatabase.ApiResponseKey.DISCIPLE_TREE.toString());
                    Log.d(TAG, "Add disciple tree : \n" + json_disciple_tree.toString());
                    Update_DiscipleTree(json_disciple_tree);
                }
            } else {
                Log.w(TAG, "ProcessResponse: No 'Response' JSON object!");
            }
        } catch (JSONException e) {
            Log.e(TAG, "ProcessResponse: JSON Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void Add_News(JSONArray json_newses) {
        try {
            if (json_newses.length() > 0) {
                Log.d(TAG, "Adding New News -> \n" + json_newses.toString());
                for (int i = 0; i < json_newses.length(); i++) {
                    JSONObject obj = json_newses.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put(Database.NewsfeedColumn.NEWS_ID.toString(), obj.getString(SyncDatabase.ApiResponseKey.ID.toString()));
                    cv.put(Database.NewsfeedColumn.TITLE.toString(), obj.getString(SyncDatabase.ApiResponseKey.TITLE.toString()));
                    cv.put(Database.NewsfeedColumn.CONTENT.toString(), obj.getString(SyncDatabase.ApiResponseKey.CONTENT.toString()));
                    cv.put(Database.NewsfeedColumn.CATEGORY.toString(), obj.getString(SyncDatabase.ApiResponseKey.CATEGORY.toString()));
                    cv.put(Database.NewsfeedColumn.IMAGEURL.toString(), obj.getString(SyncDatabase.ApiResponseKey.IMAGE_URL.toString()));
                    cv.put(Database.NewsfeedColumn.IMAGEPATH.toString(), "");
                    cv.put(Database.NewsfeedColumn.PUBDATE.toString(), obj.getString(SyncDatabase.ApiResponseKey.PUBLISH_DATE.toString()));
                    News news = DeepLife.myDATABASE.getNewsBySerID(Integer.valueOf(obj.getString(SyncDatabase.ApiResponseKey.ID.toString())));
                    if (news == null) {
                        long x = DeepLife.myDATABASE.insert(Database.Table_NEWSFEED, cv);
                        if (x > 0) {
                            Log.d(TAG, "Successfully Added: News Added -> \n" + cv.toString());
                        } else {
                            Log.e(TAG, "Error During Adding: News -> \n" + cv.toString());
                        }

                    } else {
                        long x = DeepLife.myDATABASE.update(Database.Table_NEWSFEED, cv, news.getId());
                        Log.d(TAG, "Updated: News Updated -> \n" + cv.toString());
                        if (x > 0) {
                            Log.d(TAG, "Successfully Updated: News Updated -> \n" + cv.toString());
                        } else {
                            Log.e(TAG, "Error During Updating: News -> \n" + cv.toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Add_News: Exception: " + e.getMessage());
        }
    }

    public static void Add_Testimony(JSONArray json_testimony) {
        try {
            if (json_testimony.length() > 0) {
                Log.d(TAG, "Adding New Testimony -> \n" + json_testimony.toString());
                for (int i = 0; i < json_testimony.length(); i++) {
                    JSONObject obj = json_testimony.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put(Database.TestimonyColumn.SERID.toString(), obj.getString(SyncDatabase.ApiResponseKey.ID.toString()));
                    cv.put(Database.TestimonyColumn.USERID.toString(), obj.getString(SyncDatabase.ApiResponseKey.USER_ID.toString()));
                    cv.put(Database.TestimonyColumn.DESCRIPTION.toString(), obj.getString(SyncDatabase.ApiResponseKey.DESCRIPTION.toString()));
                    cv.put(Database.TestimonyColumn.STATUS.toString(), obj.getString(SyncDatabase.ApiResponseKey.STATUS.toString()));
                    cv.put(Database.TestimonyColumn.PUBDATE.toString(), obj.getString(SyncDatabase.ApiResponseKey.CREATED.toString()));
                    cv.put(Database.TestimonyColumn.USERNAME.toString(), obj.getString(SyncDatabase.ApiResponseKey.USER_NAME.toString()));
                    Testimony testimony = DeepLife.myDATABASE.getTestimonyBySerID(Integer.valueOf(obj.getString(SyncDatabase.ApiResponseKey.ID.toString())));
                    if (testimony == null) {
                        long x = DeepLife.myDATABASE.insert(Database.Table_TESTIMONY, cv);
                        if (x > 0) {
                            Log.d(TAG, "Successfully Added: Testimony Added -> \n" + cv.toString());
                        } else {
                            Log.e(TAG, "Error During Adding: Testimony -> \n" + cv.toString());
                        }
                    } else {
                        long x = DeepLife.myDATABASE.update(Database.Table_TESTIMONY, cv, testimony.getID());
                        Log.d(TAG, "Updated: Testimony Updated -> \n" + cv.toString());
                        if (x > 0) {
                            Log.d(TAG, "Successfully Updated: Testimony Updated -> \n" + cv.toString());
                        } else {
                            Log.e(TAG, "Error During Updating: Testimony -> \n" + cv.toString());
                        }
                    }
                }
                TestimonyFragment.UpdateList();
            }
        } catch (Exception e) {
            Log.e(TAG, "Add_Testimony: Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public long AddDisciple(Disciple disciple) {
        ContentValues cv = new ContentValues();
        cv.put(Database.DisciplesColumn.SERID.toString(), disciple.getSerID());
        cv.put(Database.DisciplesColumn.FULLNAME.toString(), disciple.getFullName());
        cv.put(Database.DisciplesColumn.DISPLAYNAME.toString(), disciple.getDisplayName());
        cv.put(Database.DisciplesColumn.EMAIL.toString(), disciple.getEmail());
        cv.put(Database.DisciplesColumn.PHONE.toString(), disciple.getPhone());
        cv.put(Database.DisciplesColumn.COUNTRY.toString(), disciple.getCountry());
        cv.put(Database.DisciplesColumn.MENTORID.toString(), disciple.getMentorID());
        cv.put(Database.DisciplesColumn.STAGE.toString(), disciple.getStage().toString());
        cv.put(Database.DisciplesColumn.IMAGEURL.toString(), disciple.getImageURL());
        cv.put(Database.DisciplesColumn.IMAGEPATH.toString(), disciple.getImagePath());
        cv.put(Database.DisciplesColumn.ROLE.toString(), disciple.getRole().toString());
        cv.put(Database.DisciplesColumn.GENDER.toString(), disciple.getGender().toString());
        cv.put(Database.DisciplesColumn.CREATED.toString(), disciple.getCreated());
        Disciple old_disciple = DeepLife.myDATABASE.getDiscipleByPhone(disciple.getPhone());
        if (old_disciple == null) {
            long x = DeepLife.myDATABASE.insert(Database.Table_DISCIPLES, cv);
            if (x > 0) {
                Log.d(TAG, "Successfully Added: Disciples Added -> \n" + cv.toString());
            } else {
                Log.e(TAG, "Error During Adding: Disciples -> \n" + cv.toString());
            }
            return x;
        }
        return 0;
    }

    public long AddLog(Logs logs) {
        ContentValues cv = new ContentValues();
        cv.put(Database.LogsColumn.TYPE.toString(), logs.getType().toString());
        cv.put(Database.LogsColumn.TASK.toString(), logs.getTask().toString());
        cv.put(Database.LogsColumn.VALUE.toString(), logs.getValue());
        long x = DeepLife.myDATABASE.insert(Database.Table_LOGS, cv);
        if (x > 0) {
            Log.d(TAG, "Successfully Added: new Logs Added -> \n" + cv.toString());
        } else {
            Log.e(TAG, "Error During Adding: Logs -> \n" + cv.toString());
        }
        return x;
    }

    public static void Add_Disciples(JSONArray json_disciples) {
        try {
            if (json_disciples.length() > 0) {
                Log.d(TAG, "Adding New Disciples -> \n" + json_disciples.toString());
                if (json_disciples.length() > 0) {
                    DeepLife.myDATABASE.Delete_All(Database.Table_DISCIPLES);
                    for (int i = 0; i < json_disciples.length(); i++) {
                        JSONObject obj = json_disciples.getJSONObject(i);
                        ContentValues cv = new ContentValues();
                        cv.put(Database.DisciplesColumn.SERID.toString(), obj.getString(SyncDatabase.ApiResponseKey.ID.toString()));
                        cv.put(Database.DisciplesColumn.FULLNAME.toString(), obj.getString(SyncDatabase.ApiResponseKey.FIRSTNAME.toString()));
                        cv.put(Database.DisciplesColumn.DISPLAYNAME.toString(), obj.getString(SyncDatabase.ApiResponseKey.DISPLAYNAME.toString()));
                        cv.put(Database.DisciplesColumn.EMAIL.toString(), obj.getString(SyncDatabase.ApiResponseKey.EMAIL.toString()));
                        cv.put(Database.DisciplesColumn.PHONE.toString(), obj.getString(SyncDatabase.ApiResponseKey.PHONE_NO.toString()));
                        cv.put(Database.DisciplesColumn.COUNTRY.toString(), obj.getString(SyncDatabase.ApiResponseKey.COUNTRY_LC.toString()));
                        cv.put(Database.DisciplesColumn.MENTORID.toString(), obj.getString(SyncDatabase.ApiResponseKey.MENTOR_ID.toString()));
                        cv.put(Database.DisciplesColumn.STAGE.toString(), obj.getString(SyncDatabase.ApiResponseKey.STAGE.toString()));
                        cv.put(Database.DisciplesColumn.IMAGEURL.toString(), obj.getString(SyncDatabase.ApiResponseKey.PICTURE.toString()));
                        cv.put(Database.DisciplesColumn.IMAGEPATH.toString(), "");
                        cv.put(Database.DisciplesColumn.ROLE.toString(), obj.getString(SyncDatabase.ApiResponseKey.ROLE_ID.toString()));
                        cv.put(Database.DisciplesColumn.GENDER.toString(), obj.getString(SyncDatabase.ApiResponseKey.GENDER.toString()));
                        cv.put(Database.DisciplesColumn.CREATED.toString(), obj.getString(SyncDatabase.ApiResponseKey.CREATED.toString()));
                        Disciple disciple = DeepLife.myDATABASE.getDiscipleByPhone(obj.getString(SyncDatabase.ApiResponseKey.PHONE_NO.toString()));
                        if (disciple == null) {
                            long x = DeepLife.myDATABASE.insert(Database.Table_DISCIPLES, cv);
                            if (x > 0) {
                                Log.d(TAG, "Successfully Added: Disciples Added -> \n" + cv.toString());
                            } else {
                                Log.e(TAG, "Error During Adding: Disciples -> \n" + cv.toString());
                            }
                        } else {
                            cv.put(Database.DisciplesColumn.IMAGEPATH.toString(), disciple.getImagePath());
                            long x = DeepLife.myDATABASE.update(Database.Table_DISCIPLES, cv, disciple.getID());
                            Log.d(TAG, "Updated: Disciple Updated -> \n" + cv.toString());
                            if (x > 0) {
                                Log.d(TAG, "Successfully Updated: Disciples Updated -> \n" + cv.toString());
                            } else {
                                Log.e(TAG, "Error During Updating: Disciples -> \n" + cv.toString());
                            }
                        }
                    }
                }

            }
        } catch (Exception e) {
            Log.e(TAG, "Add_Disciples: Exception: " + e.getMessage());
        }
    }

    public static void Add_Questions(JSONArray json_questions) {
        try {
            if (json_questions.length() > 0) {
                Log.d(TAG, "Adding New WBS Questions -> \n" + json_questions.toString());
                for (int i = 0; i < json_questions.length(); i++) {
                    JSONObject obj = json_questions.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put(Database.QuestionListColumn.SERID.toString(), obj.getString(SyncDatabase.ApiResponseKey.ID.toString()));
                    cv.put(Database.QuestionListColumn.CATEGORY.toString(), obj.getString(SyncDatabase.ApiResponseKey.CATEGORY.toString()));
                    cv.put(Database.QuestionListColumn.QUESTION.toString(), obj.getString(SyncDatabase.ApiResponseKey.QUESTION.toString()));
                    cv.put(Database.QuestionListColumn.DESCRIPTION.toString(), obj.getString(SyncDatabase.ApiResponseKey.DESCRIPTION.toString()));
                    cv.put(Database.QuestionListColumn.MANDATORY.toString(), obj.getString(SyncDatabase.ApiResponseKey.MANDATORY.toString()));
                    cv.put(Database.QuestionListColumn.TYPE.toString(), obj.getString(SyncDatabase.ApiResponseKey.TYPE.toString()));
                    cv.put(Database.QuestionListColumn.COUNTRY.toString(), obj.getString(SyncDatabase.ApiResponseKey.COUNTRY_LC.toString()));
                    cv.put(Database.QuestionListColumn.CREATED.toString(), obj.getString(SyncDatabase.ApiResponseKey.CREATED.toString()));
                    WbsQuestion wbsQuestion = DeepLife.myDATABASE.getWbsQuestionBySerID(Integer.valueOf(obj.getString(SyncDatabase.ApiResponseKey.ID.toString())));
                    if (wbsQuestion == null) {
                        long x = DeepLife.myDATABASE.insert(Database.Table_QUESTION_LIST, cv);
                        if (x > 0) {
                            Log.d(TAG, "Successfully Added: WBS Questions Added -> \n" + cv.toString());
                        } else {
                            Log.e(TAG, "Error During Adding: WBS Questions -> \n" + cv.toString());
                        }
                    } else {
                        long x = DeepLife.myDATABASE.update(Database.Table_QUESTION_LIST, cv, wbsQuestion.getID());
                        Log.d(TAG, "Updated: WBS Questions Updated -> \n" + cv.toString());
                        if (x > 0) {
                            Log.d(TAG, "Successfully Updated: WBS Questions Updated -> \n" + cv.toString());
                        } else {
                            Log.e(TAG, "Error During Updating: WBS Questions -> \n" + cv.toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Add_Questions: Exception: " + e.getMessage());
        }
    }

    public static void Add_Category(JSONArray json_questions) {
        try {
            if (json_questions.length() > 0) {
                Log.d(TAG, "Adding/Updating New Categories  -> \n" + json_questions.toString());
                for (int i = 0; i < json_questions.length(); i++) {
                    JSONObject obj = json_questions.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put(Database.CategoryColumn.SERID.toString(), obj.getString(SyncDatabase.ApiResponseKey.ID.toString()));
                    cv.put(Database.CategoryColumn.NAME.toString(), obj.getString(SyncDatabase.ApiResponseKey.NAME.toString()));
                    cv.put(Database.CategoryColumn.PARENT.toString(), obj.getString(SyncDatabase.ApiResponseKey.PARENT.toString()));
                    cv.put(Database.CategoryColumn.STATUS.toString(), obj.getString(SyncDatabase.ApiResponseKey.STATUS.toString()));
                    cv.put(Database.CategoryColumn.CREATED.toString(), obj.getString(SyncDatabase.ApiResponseKey.CREATED.toString()));
                    ;
                    Category category = DeepLife.myDATABASE.getCategoryByID(Integer.valueOf(obj.getString(SyncDatabase.ApiResponseKey.ID.toString())));
                    if (category == null) {
                        long x = DeepLife.myDATABASE.insert(Database.Table_CATEGORIES, cv);
                        if (x > 0) {
                            Log.d(TAG, "Successfully Added: Category -> \n" + cv.toString());
                        } else {
                            Log.e(TAG, "Error During Adding: Category -> \n" + cv.toString());
                        }
                    } else {
                        long x = DeepLife.myDATABASE.update(Database.Table_CATEGORIES, cv, category.getID());
                        Log.d(TAG, "Updated: Category -> \n" + cv.toString());
                        if (x > 0) {
                            Log.d(TAG, "Successfully Updated: Category Updated -> \n" + cv.toString());
                        } else {
                            Log.e(TAG, "Error During Updating: Category -> \n" + cv.toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Add_Category: Exception: " + e.getMessage());
        }
    }

    public static void Add_Answers(JSONArray json_answers) {
        try {
            if (json_answers.length() > 0) {
                Log.d(TAG, "Adding New Answers  -> \n" + json_answers.toString());
                for (int i = 0; i < json_answers.length(); i++) {
                    JSONObject obj = json_answers.getJSONObject(i);
                    String disciplePhone = obj.getString(SyncDatabase.ApiResponseKey.DISCIPLE_PHONE.toString());
                    ContentValues cv = new ContentValues();
                    cv.put(Database.QuestionAnswerColumn.SERID.toString(), obj.getString(SyncDatabase.ApiResponseKey.ID.toString()));
                    cv.put(Database.QuestionAnswerColumn.DISCIPLEPHONE.toString(), disciplePhone);
                    cv.put(Database.QuestionAnswerColumn.QUESTION_ID.toString(), obj.getString(SyncDatabase.ApiResponseKey.QUESTION_ID.toString()));
                    cv.put(Database.QuestionAnswerColumn.ANSWER.toString(), obj.getString(SyncDatabase.ApiResponseKey.ANSWER.toString()));
                    cv.put(Database.QuestionAnswerColumn.BUILDSTAGE.toString(), obj.getString(SyncDatabase.ApiResponseKey.STAGE.toString()));
                    //Answer answer = DeepLife.myDATABASE.getAnswerBySerID(Integer.valueOf(obj.getString(ApiResponseKey.ID.toString())));
                    //Answer answer = DeepLife.myDATABASE.getAnswerByQuestionID(Integer.valueOf(obj.getString(ApiResponseKey.QUESTION_ID.toString())));
                    Answer answer = DeepLife.myDATABASE.getAnswerByQuestionIDandDisciplePhone(Integer.valueOf(obj.getString(SyncDatabase.ApiResponseKey.QUESTION_ID.toString())), disciplePhone);
                    if (answer == null) {
                        long x = DeepLife.myDATABASE.insert(Database.Table_QUESTION_ANSWER, cv);
                        if (x > 0) {
                            Log.d(TAG, "Successfully Added: Answers -> \n" + cv.toString());
                        } else {
                            Log.e(TAG, "Error During Adding: Answers -> \n" + cv.toString());
                        }
                    } else {
                        if(DeepLife.myDATABASE.getSendAnswers().size()<1){
                            long x = DeepLife.myDATABASE.update(Database.Table_QUESTION_ANSWER, cv, answer.getID());
                            Log.d(TAG, "Updated: Answers -> \n" + cv.toString());
                            if (x > 0) {
                                Log.d(TAG, "Successfully Updated: Answers Updated -> \n" + cv.toString());
                            } else {
                                Log.e(TAG, "Error During Updating: Answers -> \n" + cv.toString());
                            }
                        }else {
                            Log.e(TAG, "Discard updates from server till the local sent -> \n");
                        }

                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Add_Answers: Exception: " + e.getMessage());
        }
    }

    public static void Add_MainUser(JSONObject json_mainuser) {
        try {
            if (json_mainuser.length() > 0) {
                Log.d(TAG, "Adding MainUser  -> \n" + json_mainuser.toString());
                JSONObject obj = json_mainuser;
                ContentValues cv = new ContentValues();
                cv.put(Database.UserColumn.SERID.toString(), obj.getString(SyncDatabase.ApiResponseKey.ID.toString()));
                cv.put(Database.UserColumn.FULL_NAME.toString(), obj.getString(SyncDatabase.ApiResponseKey.FIRSTNAME.toString()));
                cv.put(Database.UserColumn.EMAIL.toString(), obj.getString(SyncDatabase.ApiResponseKey.EMAIL.toString()));
                cv.put(Database.UserColumn.PHONE.toString(), obj.getString(SyncDatabase.ApiResponseKey.PHONE_NO.toString()));
                cv.put(Database.UserColumn.PASSWORD.toString(), SyncDatabase.ApiResponseKey.PASS.toString());
                cv.put(Database.UserColumn.COUNTRY.toString(), obj.getString(SyncDatabase.ApiResponseKey.COUNTRY_LC.toString()));
                cv.put(Database.UserColumn.GENDER.toString(), obj.getString(SyncDatabase.ApiResponseKey.GENDER.toString()));
                cv.put(Database.UserColumn.PICTURE.toString(), obj.getString(SyncDatabase.ApiResponseKey.PICTURE.toString()));
                cv.put(Database.UserColumn.FAVORITE_SCRIPTURE.toString(), "");
                DeepLife.myDATABASE.Delete_All(Database.Table_USER);
                long x = DeepLife.myDATABASE.insert(Database.Table_USER, cv);
                if (x > 0) {
                    Log.d(TAG, "Successfully Added: Main User -> \n" + cv.toString());
                } else {
                    Log.e(TAG, "Error During Adding: Main User -> \n" + cv.toString());
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Add_MainUser: Exception: " + e.getMessage());
        }
    }

    public static void Add_LearningTools(JSONArray json_learnings) {
        try {
            if (json_learnings.length() > 0) {
                Log.d(TAG, "Adding LearningTools  -> \n" + json_learnings.toString());
                for (int i = 0; i < json_learnings.length(); i++) {
                    JSONObject obj = json_learnings.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put(Database.LearningColumn.SERID.toString(), obj.getString(SyncDatabase.ApiResponseKey.ID.toString()));
                    cv.put(Database.LearningColumn.TITLE.toString(), obj.getString(SyncDatabase.ApiResponseKey.TITLE.toString()));
                    cv.put(Database.LearningColumn.DESCRIPTION.toString(), obj.getString(SyncDatabase.ApiResponseKey.DESCRIPTION.toString()));
                    cv.put(Database.LearningColumn.VIDEOURL.toString(), obj.getString(SyncDatabase.ApiResponseKey.IFRAMCODE.toString()));
                    cv.put(Database.LearningColumn.COUNTRY.toString(), obj.getString(SyncDatabase.ApiResponseKey.COUNTRY_LC.toString()));
                    cv.put(Database.LearningColumn.ISDEFAULT.toString(), obj.getString(SyncDatabase.ApiResponseKey.DEFAULT_LEARN.toString()));
                    cv.put(Database.LearningColumn.CREATED.toString(), obj.getString(SyncDatabase.ApiResponseKey.CREATED.toString()));
                    LearningTool learningTool = DeepLife.myDATABASE.getLearningToolsBySerID(Integer.valueOf(obj.getString(SyncDatabase.ApiResponseKey.ID.toString())));
                    if (learningTool == null) {
                        long x = DeepLife.myDATABASE.insert(Database.Table_LEARNING, cv);
                        if (x > 0) {
                            Log.d(TAG, "Successfully Added: Learning Tools -> \n" + cv.toString());
                        } else {
                            Log.e(TAG, "Error During Adding: Learning Tools -> \n" + cv.toString());
                        }
                    } else {
                        long x = DeepLife.myDATABASE.update(Database.Table_LEARNING, cv, learningTool.getID());
                        if (x > 0) {
                            Log.d(TAG, "Successfully Updated: Learning Tools -> \n" + cv.toString());
                        } else {
                            Log.e(TAG, "Error During Updated: Learning Tools -> \n" + cv.toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Add_LearningTools: Exception: " + e.getMessage());
        }
    }
    public static void Update_DiscipleTree(JSONObject json_discipletree) {
        try {
            if (json_discipletree.length() > 0) {
                Log.d(TAG, "Adding Updated_DiscipleTree  -> \n" + json_discipletree.toString());
                JSONObject obj = json_discipletree;
                ContentValues cv = new ContentValues();
                cv.put(Database.DiscipleTreeColumn.SERID.toString(), obj.getString(SyncDatabase.ApiResponseKey.ID.toString()));
                cv.put(Database.DiscipleTreeColumn.USERID.toString(), obj.getString(SyncDatabase.ApiResponseKey.USER_ID.toString()));
                cv.put(Database.DiscipleTreeColumn.COUNT.toString(), obj.getString(SyncDatabase.ApiResponseKey.DISCIPLE_COUNT.toString()));
                DiscipleTreeCount discipleTreeCount = DeepLife.myDATABASE.getDiscipleTreeCountBySerID(Integer.valueOf(obj.getString(SyncDatabase.ApiResponseKey.ID.toString())));
                if (discipleTreeCount == null) {
                    long x = DeepLife.myDATABASE.insert(Database.Table_DISCIPLE_TREE, cv);
                    if (x > 0) {
                        Log.d(TAG, "Successfully Added: DiscipleTreeCount-> \n" + cv.toString());
                    } else {
                        Log.e(TAG, "Error During Adding: DiscipleTreeCount -> \n" + cv.toString());
                    }
                } else {
                    long x = DeepLife.myDATABASE.update(Database.Table_DISCIPLE_TREE, cv, discipleTreeCount.getID());
                    if (x > 0) {
                        Log.d(TAG, "Successfully Updated: discipleTreeCount -> \n" + cv.toString());
                    } else {
                        Log.e(TAG, "Error During Updated: discipleTreeCount -> \n" + cv.toString());
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Update_DiscipleTree: Exception: " + e.getMessage());
        }
    }

    public static void Update_MainUser(JSONObject json_mainuser) {
        try {
            User user = DeepLife.myDATABASE.getMainUser();
            if (user != null) {
                if (json_mainuser.length() > 0) {
                    Log.d(TAG, "Updating MainUser  -> \n" + json_mainuser.toString());
                    JSONObject obj = json_mainuser;
                    ContentValues cv = new ContentValues();
                    cv.put(Database.UserColumn.SERID.toString(), obj.getString(SyncDatabase.ApiResponseKey.ID.toString()));
                    cv.put(Database.UserColumn.FULL_NAME.toString(), obj.getString(SyncDatabase.ApiResponseKey.FIRSTNAME.toString()));
                    cv.put(Database.UserColumn.EMAIL.toString(), obj.getString(SyncDatabase.ApiResponseKey.EMAIL.toString()));
                    cv.put(Database.UserColumn.PHONE.toString(), obj.getString(SyncDatabase.ApiResponseKey.PHONE_NO.toString()));
                    cv.put(Database.UserColumn.COUNTRY.toString(), obj.getString(SyncDatabase.ApiResponseKey.COUNTRY_LC.toString()));
                    cv.put(Database.UserColumn.GENDER.toString(), obj.getString(SyncDatabase.ApiResponseKey.GENDER.toString()));
                    cv.put(Database.UserColumn.PICTURE.toString(), obj.getString(SyncDatabase.ApiResponseKey.PICTURE.toString()));
                    cv.put(Database.UserColumn.FAVORITE_SCRIPTURE.toString(), "");
                    long x = DeepLife.myDATABASE.update(Database.Table_USER, cv, user.getID());
                    if (x > 0) {
                        Log.d(TAG, "Successfully Updated: Main User -> \n" + cv.toString());
                    } else {
                        Log.e(TAG, "Error During Updated: Main User -> \n" + cv.toString());
                    }
                }
            } else {
                if (json_mainuser.length() > 0) {
                    Log.d(TAG, "Updating MainUser  -> \n" + json_mainuser.toString());
                    JSONObject obj = json_mainuser;
                    ContentValues cv = new ContentValues();
                    cv.put(Database.UserColumn.SERID.toString(), obj.getString(SyncDatabase.ApiResponseKey.ID.toString()));
                    cv.put(Database.UserColumn.FULL_NAME.toString(), obj.getString(SyncDatabase.ApiResponseKey.FIRSTNAME.toString()));
                    cv.put(Database.UserColumn.EMAIL.toString(), obj.getString(SyncDatabase.ApiResponseKey.EMAIL.toString()));
                    cv.put(Database.UserColumn.PHONE.toString(), obj.getString(SyncDatabase.ApiResponseKey.PHONE_NO.toString()));
                    cv.put(Database.UserColumn.PASSWORD.toString(), SyncDatabase.ApiResponseKey.PASS.toString());
                    cv.put(Database.UserColumn.COUNTRY.toString(), obj.getString(SyncDatabase.ApiResponseKey.COUNTRY_LC.toString()));
                    cv.put(Database.UserColumn.GENDER.toString(), obj.getString(SyncDatabase.ApiResponseKey.GENDER.toString()));
                    cv.put(Database.UserColumn.PICTURE.toString(), obj.getString(SyncDatabase.ApiResponseKey.PICTURE.toString()));
                    cv.put(Database.UserColumn.FAVORITE_SCRIPTURE.toString(), "");
                    long x = DeepLife.myDATABASE.insert(Database.Table_USER, cv);
                    if (x > 0) {
                        Log.d(TAG, "Successfully Updated: Main User -> \n" + cv.toString());
                    } else {
                        Log.e(TAG, "Error During Updated: Main User -> \n" + cv.toString());
                    }
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "Update_MainUser: Exception: " + e.getMessage());
        }
    }

    public static void Add_Countries(JSONArray json_countries) {
        try {
            if (json_countries.length() > 0) {
                Log.d(TAG, "Adding New Countries  -> \n" + json_countries.toString());
                for (int i = 0; i < json_countries.length(); i++) {
                    JSONObject obj = json_countries.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put(Database.CountryColumn.SERID.toString(), obj.getString(SyncDatabase.ApiResponseKey.ID.toString()));
                    cv.put(Database.CountryColumn.ISO3.toString(), obj.getString(SyncDatabase.ApiResponseKey.ISO3.toString()));
                    cv.put(Database.CountryColumn.NAME.toString(), obj.getString(SyncDatabase.ApiResponseKey.NAME.toString()));
                    cv.put(Database.CountryColumn.CODE.toString(), obj.getString(SyncDatabase.ApiResponseKey.CODE.toString()));
                    Country country = DeepLife.myDATABASE.getCountryByID(Integer.valueOf(obj.getString(SyncDatabase.ApiResponseKey.ID.toString())));
                    if (country == null) {
                        long x = DeepLife.myDATABASE.insert(Database.Table_COUNTRY, cv);
                        if (x > 0) {
                            Log.d(TAG, "Successfully Added: Country -> \n" + cv.toString());
                        } else {
                            Log.e(TAG, "Error During Adding: Country -> \n" + cv.toString());
                        }
                    } else {
                        long x = DeepLife.myDATABASE.update(Database.Table_COUNTRY, cv, country.getID());
                        Log.d(TAG, "Updated: Countries -> \n" + cv.toString());
                        if (x > 0) {
                            Log.d(TAG, "Successfully Updated: Country Updated -> \n" + cv.toString());
                        } else {
                            Log.e(TAG, "Error During Updating: Country -> \n" + cv.toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Add_Countries: Exception: " + e.getMessage());
        }
    }

    private static void Delete_Logs(JSONArray json_logs) {
        try {
            Log.i(TAG, "Deleting Confirmed Logs -> \n");
            Log.i(TAG, "Found  -> " + json_logs.length() + "   ->" + json_logs.toString());
            if (json_logs.length() > 0) {
                for (int i = 0; i < json_logs.length(); i++) {
                    JSONObject obj = json_logs.getJSONObject(i);
                    Log.i(TAG, "Deleting  -> Logs: " + obj.toString());
                    int id = Integer.valueOf(obj.getString(SyncDatabase.ApiResponseKey.LOG_ID.toString()));
                    Log.i(TAG, "Deleting -> LogID: " + id);
                    if (id > 0) {
                        long val = DeepLife.myDATABASE.remove(Database.Table_LOGS, id);
                        Log.i(TAG, "Deleting -> LogID: " + id + " :-> " + val);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Delete_Logs: Exception: " + e.getMessage());
        }
    }
}


