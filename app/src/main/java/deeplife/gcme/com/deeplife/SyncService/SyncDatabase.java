package deeplife.gcme.com.deeplife.SyncService;

import android.content.ContentValues;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Disciples.Disciple;
import deeplife.gcme.com.deeplife.Models.Answer;
import deeplife.gcme.com.deeplife.Models.Category;
import deeplife.gcme.com.deeplife.Models.Country;
import deeplife.gcme.com.deeplife.Models.Logs;
import deeplife.gcme.com.deeplife.WinBuildSend.WinBuildSendQuestion;
import deeplife.gcme.com.deeplife.News.News;
import deeplife.gcme.com.deeplife.Testimony.Testimony;
import deeplife.gcme.com.deeplife.Testimony.TestimonyFragment;

/**
 * Created by bengeos on 12/16/16.
 */

public class SyncDatabase {
    private static String TAG = "SyncDatabase";
    public static void ProcessResponse(String jsonArray){
        Gson myGson = new Gson();
        try {
            JSONObject myObject = (JSONObject) new JSONTokener(jsonArray).nextValue();
            Log.i(TAG, "Server Response -> \n" + myObject.toString());
            if (!myObject.isNull("Response")) {
                JSONObject json_response = myObject.getJSONObject("Response");
                if (!json_response.isNull("NewsFeeds")) {
                    JSONArray json_newsfeeds = json_response.getJSONArray("NewsFeeds");
                    Log.i(TAG, "News Feeds: \n" + json_newsfeeds.toString());
                    Add_News(json_newsfeeds);
                }
                if (!json_response.isNull("Testimonies")) {
                    JSONArray json_testimonies = json_response.getJSONArray("Testimonies");
                    Log.i(TAG, "Testimonies: \n" + json_testimonies.toString());
                    Add_Testimony(json_testimonies);
                }
                if (!json_response.isNull("Disciples")) {
                    JSONArray json_disciples = json_response.getJSONArray("Disciples");
                    Log.i(TAG, "Disciples: \n" + json_disciples.toString());
                    Add_Disciples(json_disciples);
                }
                if (!json_response.isNull("Questions")) {
                    JSONArray json_questions = json_response.getJSONArray("Questions");
                    Log.i(TAG, "Questions: \n" + json_questions.toString());
                    Add_Questions(json_questions);
                }
                if (!json_response.isNull("Categories")) {
                    JSONArray json_categories = json_response.getJSONArray("Categories");
                    Log.i(TAG, "Categories: \n" + json_categories.toString());
                    Add_Category(json_categories);
                }
                if (!json_response.isNull("Answers")) {
                    JSONArray json_answers = json_response.getJSONArray("Answers");
                    Log.i(TAG, "Answers: \n" + json_answers.toString());
                    Add_Answers(json_answers);
                }
                if (!json_response.isNull("Country")) {
                    JSONArray json_answers = json_response.getJSONArray("Country");
                    Log.i(TAG, "Country: \n" + json_answers.toString());
                    Add_Countries(json_answers);
                }
                if (!json_response.isNull("Log_Response")) {
                    JSONArray json_logs = json_response.getJSONArray("Log_Response");
                    Delete_Logs(json_logs);
                }
                if (!json_response.isNull("Profile")) {
                    JSONObject json_user_profile = json_response.getJSONObject("Profile");
                    Add_MainUser(json_user_profile);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void Add_News(JSONArray json_newses){
        try{
            if(json_newses.length()>0){
                Log.i(TAG,"Adding New News -> \n"+json_newses.toString());
                for(int i=0;i<json_newses.length();i++){
                    JSONObject obj = json_newses.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put(Database.NewsFeed_FIELDS[0], obj.getString("id"));
                    cv.put(Database.NewsFeed_FIELDS[1], obj.getString("title"));
                    cv.put(Database.NewsFeed_FIELDS[2], obj.getString("content"));
                    cv.put(Database.NewsFeed_FIELDS[3], obj.getString("category"));
                    cv.put(Database.NewsFeed_FIELDS[4], obj.getString("image_url"));
                    cv.put(Database.NewsFeed_FIELDS[5], "");
                    cv.put(Database.NewsFeed_FIELDS[6], obj.getString("publish_date"));
                    News news = DeepLife.myDATABASE.getNewsBySerID(Integer.valueOf(obj.getString("id")));
                    if(news == null){
                        long x = DeepLife.myDATABASE.insert(Database.Table_NEWSFEED,cv);
                        if(x>0){
                            Log.i(TAG,"Successfully Added: News Added -> \n"+cv.toString());
                        }else {
                            Log.i(TAG,"Error During Adding: News -> \n"+cv.toString());
                        }

                    }else {
                        long x = DeepLife.myDATABASE.update(Database.Table_NEWSFEED,cv,news.getId());
                        Log.i(TAG,"Updated: News Updated -> \n"+cv.toString());
                        if(x>0){
                            Log.i(TAG,"Successfully Updated: News Updated -> \n"+cv.toString());
                        }else {
                            Log.i(TAG,"Error During Updating: News -> \n"+cv.toString());
                        }
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG,e.toString());
        }
    }
    public static void Add_Testimony(JSONArray json_testimony){
        try{
            if(json_testimony.length()>0){
                Log.i(TAG,"Adding New Testimony -> \n"+json_testimony.toString());
                for(int i=0;i<json_testimony.length();i++){
                    JSONObject obj = json_testimony.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put(Database.TESTIMONY_FIELDS[0], obj.getString("id"));
                    cv.put(Database.TESTIMONY_FIELDS[1], obj.getString("user_id"));
                    cv.put(Database.TESTIMONY_FIELDS[2], obj.getString("description"));
                    cv.put(Database.TESTIMONY_FIELDS[3], obj.getString("status"));
                    cv.put(Database.TESTIMONY_FIELDS[4], obj.getString("created"));
                    cv.put(Database.TESTIMONY_FIELDS[5], obj.getString("user_name"));
                    Testimony testimony = DeepLife.myDATABASE.getTestimonyBySerID(Integer.valueOf(obj.getString("id")));
                    if(testimony == null){
                        long x = DeepLife.myDATABASE.insert(Database.Table_TESTIMONY,cv);
                        if(x>0){
                            Log.i(TAG,"Successfully Added: Testimony Added -> \n"+cv.toString());
                        }else {
                            Log.i(TAG,"Error During Adding: Testimony -> \n"+cv.toString());
                        }
                    }else {
                        long x = DeepLife.myDATABASE.update(Database.Table_TESTIMONY,cv,testimony.getID());
                        Log.i(TAG,"Updated: Testimony Updated -> \n"+cv.toString());
                        if(x>0){
                            Log.i(TAG,"Successfully Updated: Testimony Updated -> \n"+cv.toString());
                        }else {
                            Log.i(TAG,"Error During Updating: Testimony -> \n"+cv.toString());
                        }
                    }
                }
                TestimonyFragment.UpdateList();
            }
        }catch (Exception e){
            Log.i(TAG,e.toString());
        }
    }
    public long AddDisciple(Disciple disciple){
        ContentValues cv = new ContentValues();
        cv.put(Database.DISCIPLES_FIELDS[0], disciple.getSerID());
        cv.put(Database.DISCIPLES_FIELDS[1], disciple.getFullName());
        cv.put(Database.DISCIPLES_FIELDS[2], disciple.getDisplayName());
        cv.put(Database.DISCIPLES_FIELDS[3], disciple.getEmail());
        cv.put(Database.DISCIPLES_FIELDS[4], disciple.getPhone());
        cv.put(Database.DISCIPLES_FIELDS[5], disciple.getCountry());
        cv.put(Database.DISCIPLES_FIELDS[6], disciple.getMentorID());
        cv.put(Database.DISCIPLES_FIELDS[7], disciple.getStage());
        cv.put(Database.DISCIPLES_FIELDS[8], disciple.getImageURL());
        cv.put(Database.DISCIPLES_FIELDS[9], disciple.getImagePath());
        cv.put(Database.DISCIPLES_FIELDS[10], disciple.getRole());
        cv.put(Database.DISCIPLES_FIELDS[11], disciple.getGender());
        cv.put(Database.DISCIPLES_FIELDS[12], disciple.getCreated());
        Disciple old_disciple = DeepLife.myDATABASE.getDiscipleByPhone(disciple.getPhone());
        if(old_disciple == null){
            long x = DeepLife.myDATABASE.insert(Database.Table_DISCIPLES,cv);
            if(x>0){
                Log.i(TAG,"Successfully Added: Disciples Added -> \n"+cv.toString());
            }else {
                Log.i(TAG,"Error During Adding: Disciples -> \n"+cv.toString());
            }
            return x;
        }
        return 0;
    }
    public long AddLog(Logs logs){
        ContentValues log = new ContentValues();
        log.put(Database.LOGS_FIELDS[0], logs.getType());
        log.put(Database.LOGS_FIELDS[1], logs.getTask());
        log.put(Database.LOGS_FIELDS[2], logs.getValue());
        long x = DeepLife.myDATABASE.insert(Database.Table_LOGS,log);
        if(x>0){
            Log.i(TAG,"Successfully Added: new Logs Added -> \n"+log.toString());
        }else {
            Log.i(TAG,"Error During Adding: Logs -> \n"+log.toString());
        }
        return x;
    }
    public static void Add_Disciples(JSONArray json_disciples){
        try{
            if(json_disciples.length()>0){
                Log.i(TAG,"Adding New Disciples -> \n"+json_disciples.toString());
                if(json_disciples.length()>0){
                    DeepLife.myDATABASE.Delete_All(Database.Table_DISCIPLES);
                    for(int i=0;i<json_disciples.length();i++){
                        JSONObject obj = json_disciples.getJSONObject(i);
                        ContentValues cv = new ContentValues();
                        cv.put(Database.DISCIPLES_FIELDS[0], obj.getString("id"));
                        cv.put(Database.DISCIPLES_FIELDS[1], obj.getString("firstName"));
                        cv.put(Database.DISCIPLES_FIELDS[2], obj.getString("displayName"));
                        cv.put(Database.DISCIPLES_FIELDS[3], obj.getString("email"));
                        cv.put(Database.DISCIPLES_FIELDS[4], obj.getString("phone_no"));
                        cv.put(Database.DISCIPLES_FIELDS[5], obj.getString("country"));
                        cv.put(Database.DISCIPLES_FIELDS[6], obj.getString("mentor_id"));
                        cv.put(Database.DISCIPLES_FIELDS[7], obj.getString("stage"));
                        cv.put(Database.DISCIPLES_FIELDS[8], obj.getString("picture"));
                        cv.put(Database.DISCIPLES_FIELDS[9], "");
                        cv.put(Database.DISCIPLES_FIELDS[10], obj.getString("role_id"));
                        cv.put(Database.DISCIPLES_FIELDS[11], obj.getString("gender"));
                        cv.put(Database.DISCIPLES_FIELDS[12], obj.getString("created"));
                        Disciple disciple = DeepLife.myDATABASE.getDiscipleByPhone(obj.getString("phone_no"));
                        if(disciple == null){
                            long x = DeepLife.myDATABASE.insert(Database.Table_DISCIPLES,cv);
                            if(x>0){
                                Log.i(TAG,"Successfully Added: Disciples Added -> \n"+cv.toString());
                            }else {
                                Log.i(TAG,"Error During Adding: Disciples -> \n"+cv.toString());
                            }
                        }else {
                            cv.put(Database.DISCIPLES_FIELDS[9], disciple.getImagePath());
                            long x = DeepLife.myDATABASE.update(Database.Table_DISCIPLES,cv,disciple.getID());
                            Log.i(TAG,"Updated: Testimony Updated -> \n"+cv.toString());
                            if(x>0){
                                Log.i(TAG,"Successfully Updated: Disciples Updated -> \n"+cv.toString());
                            }else {
                                Log.i(TAG,"Error During Updating: Disciples -> \n"+cv.toString());
                            }
                        }
                    }
                }

            }
        }catch (Exception e){
            Log.i(TAG,e.toString());
        }
    }
    public static void Add_Questions(JSONArray json_questions){
        try{
            if(json_questions.length()>0){
                Log.i(TAG,"Adding New WinBuildSend Questions -> \n"+json_questions.toString());
                for(int i=0;i<json_questions.length();i++){
                    JSONObject obj = json_questions.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put(Database.QUESTION_LIST_FIELDS[0], obj.getString("id"));
                    cv.put(Database.QUESTION_LIST_FIELDS[1], obj.getString("category"));
                    cv.put(Database.QUESTION_LIST_FIELDS[2], obj.getString("question"));
                    cv.put(Database.QUESTION_LIST_FIELDS[3], obj.getString("description"));
                    cv.put(Database.QUESTION_LIST_FIELDS[4], obj.getString("mandatory"));
                    cv.put(Database.QUESTION_LIST_FIELDS[5], obj.getString("type"));
                    cv.put(Database.QUESTION_LIST_FIELDS[6], obj.getString("country"));
                    cv.put(Database.QUESTION_LIST_FIELDS[7], obj.getString("created"));
                    WinBuildSendQuestion winBuildSendQuestion = DeepLife.myDATABASE.getWinBuildSendQuestionBySerID(Integer.valueOf(obj.getString("id")));
                    if(winBuildSendQuestion == null){
                        long x = DeepLife.myDATABASE.insert(Database.Table_QUESTION_LIST,cv);
                        if(x>0){
                            Log.i(TAG,"Successfully Added: WinBuildSend Questions Added -> \n"+cv.toString());
                        }else {
                            Log.i(TAG,"Error During Adding: WinBuildSend Questions -> \n"+cv.toString());
                        }
                    }else {
                        long x = DeepLife.myDATABASE.update(Database.Table_QUESTION_LIST,cv,winBuildSendQuestion.getID());
                        Log.i(TAG,"Updated: WinBuildSend Questions Updated -> \n"+cv.toString());
                        if(x>0){
                            Log.i(TAG,"Successfully Updated: WinBuildSend Questions Updated -> \n"+cv.toString());
                        }else {
                            Log.i(TAG,"Error During Updating: WinBuildSend Questions -> \n"+cv.toString());
                        }
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG,e.toString());
        }
    }
    public static void Add_Category(JSONArray json_questions){
        try{
            if(json_questions.length()>0){
                Log.i(TAG,"Adding New Category  -> \n"+json_questions.toString());
                for(int i=0;i<json_questions.length();i++){
                    JSONObject obj = json_questions.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put(Database.CATEGORY_FIELDS[0], obj.getString("id"));
                    cv.put(Database.CATEGORY_FIELDS[1], obj.getString("name"));
                    cv.put(Database.CATEGORY_FIELDS[2], obj.getString("parent"));
                    cv.put(Database.CATEGORY_FIELDS[3], obj.getString("status"));
                    cv.put(Database.CATEGORY_FIELDS[4], obj.getString("created"));;
                    Category category = DeepLife.myDATABASE.getCategoryByID(Integer.valueOf(obj.getString("id")));
                    if(category == null){
                        long x = DeepLife.myDATABASE.insert(Database.Table_CATEGORIES,cv);
                        if(x>0){
                            Log.i(TAG,"Successfully Added: Category -> \n"+cv.toString());
                        }else {
                            Log.i(TAG,"Error During Adding: Category -> \n"+cv.toString());
                        }
                    }else {
                        long x = DeepLife.myDATABASE.update(Database.Table_CATEGORIES,cv,category.getID());
                        Log.i(TAG,"Updated: Category -> \n"+cv.toString());
                        if(x>0){
                            Log.i(TAG,"Successfully Updated: Category Updated -> \n"+cv.toString());
                        }else {
                            Log.i(TAG,"Error During Updating: Category -> \n"+cv.toString());
                        }
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG,e.toString());
        }
    }
    public static void Add_Answers(JSONArray json_answers){
        try{
            if(json_answers.length()>0){
                Log.i(TAG,"Adding New Answers  -> \n"+json_answers.toString());
                for(int i=0;i<json_answers.length();i++){
                    JSONObject obj = json_answers.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put(Database.QUESTION_ANSWER_FIELDS[0], obj.getString("id"));
                    cv.put(Database.QUESTION_ANSWER_FIELDS[1], obj.getString("disciple_phone"));
                    cv.put(Database.QUESTION_ANSWER_FIELDS[2], obj.getString("question_id"));
                    cv.put(Database.QUESTION_ANSWER_FIELDS[3], obj.getString("answer"));
                    cv.put(Database.QUESTION_ANSWER_FIELDS[4], obj.getString("stage"));;
                    Answer answer = DeepLife.myDATABASE.getAnswerBySerID(Integer.valueOf(obj.getString("id")));
                    if(answer == null){
                        long x = DeepLife.myDATABASE.insert(Database.Table_QUESTION_ANSWER,cv);
                        if(x>0){
                            Log.i(TAG,"Successfully Added: Answers -> \n"+cv.toString());
                        }else {
                            Log.i(TAG,"Error During Adding: Answers -> \n"+cv.toString());
                        }
                    }else {
                        long x = DeepLife.myDATABASE.update(Database.Table_QUESTION_ANSWER,cv,answer.getID());
                        Log.i(TAG,"Updated: Answers -> \n"+cv.toString());
                        if(x>0){
                            Log.i(TAG,"Successfully Updated: Answers Updated -> \n"+cv.toString());
                        }else {
                            Log.i(TAG,"Error During Updating: Answers -> \n"+cv.toString());
                        }
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG,e.toString());
        }
    }
    public static void Add_MainUser(JSONObject json_mainuser){
        try{
            if(json_mainuser.length()>0){
                Log.i(TAG,"Adding MainUser  -> \n"+json_mainuser.toString());
                JSONObject obj = json_mainuser;
                ContentValues cv = new ContentValues();
                cv.put(Database.USER_FIELDS[0], obj.getString("id"));
                cv.put(Database.USER_FIELDS[1], obj.getString("firstName"));
                cv.put(Database.USER_FIELDS[2], obj.getString("email"));
                cv.put(Database.USER_FIELDS[3], obj.getString("phone_no"));
                cv.put(Database.USER_FIELDS[4], "pass");
                cv.put(Database.USER_FIELDS[5], obj.getString("country"));
                cv.put(Database.USER_FIELDS[6], obj.getString("picture"));
                cv.put(Database.USER_FIELDS[7], obj.getString("picture"));
                DeepLife.myDATABASE.Delete_All(Database.Table_USER);
                long x = DeepLife.myDATABASE.insert(Database.Table_USER,cv);
                if(x>0){
                    Log.i(TAG,"Successfully Added: Main User -> \n"+cv.toString());
                }else {
                    Log.i(TAG,"Error During Adding: Main User -> \n"+cv.toString());
                }
            }
        }catch (Exception e){
            Log.i(TAG,e.toString());
        }
    }
    public static void Add_Countries(JSONArray json_countries){
        try{
            if(json_countries.length()>0){
                Log.i(TAG,"Adding New Countries  -> \n"+json_countries.toString());
                for(int i=0;i<json_countries.length();i++){
                    JSONObject obj = json_countries.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put(Database.COUNTRY_FIELDS[0], obj.getString("id"));
                    cv.put(Database.COUNTRY_FIELDS[1], obj.getString("iso3"));
                    cv.put(Database.COUNTRY_FIELDS[2], obj.getString("name"));
                    cv.put(Database.COUNTRY_FIELDS[3], obj.getString("code"));
                    Country country = DeepLife.myDATABASE.getCountryByID(Integer.valueOf(obj.getString("id")));
                    if(country == null){
                        long x = DeepLife.myDATABASE.insert(Database.Table_COUNTRY,cv);
                        if(x>0){
                            Log.i(TAG,"Successfully Added: Country -> \n"+cv.toString());
                        }else {
                            Log.i(TAG,"Error During Adding: Country -> \n"+cv.toString());
                        }
                    }else {
                        long x = DeepLife.myDATABASE.update(Database.Table_COUNTRY,cv,country.getID());
                        Log.i(TAG,"Updated: Answers -> \n"+cv.toString());
                        if(x>0){
                            Log.i(TAG,"Successfully Updated: Country Updated -> \n"+cv.toString());
                        }else {
                            Log.i(TAG,"Error During Updating: Country -> \n"+cv.toString());
                        }
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG,e.toString());
        }
    }
    private static void Delete_Logs(JSONArray json_logs) {
        try{
            Log.i(TAG,"Deleting Confirmed Logs -> \n");
            Log.i(TAG,"Found  -> "+json_logs.length()+"   ->"+json_logs.toString());
            if(json_logs.length()>0){
                for(int i=0;i<json_logs.length();i++){
                    JSONObject obj = json_logs.getJSONObject(i);
                    Log.i(TAG, "Deleting  -> Logs: " + obj.toString());
                    int id = Integer.valueOf(obj.getString("Log_ID"));
                    Log.i(TAG, "Deleting -> LogID: " + id);
                    if(id>0){
                        long val = DeepLife.myDATABASE.remove(Database.Table_LOGS, id);
                        Log.i(TAG, "Deleting -> LogID: " + id+" :-> "+val);
                    }
                }
            }
        }catch (Exception e){

        }
    }
}
