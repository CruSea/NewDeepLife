package deeplife.gcme.com.deeplife.SyncService;

import android.content.ContentValues;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Disciples.Disciple;
import deeplife.gcme.com.deeplife.News.News;
import deeplife.gcme.com.deeplife.Testimony.Testimony;
import deeplife.gcme.com.deeplife.Testimony.TestimonyFragment;

/**
 * Created by bengeos on 12/16/16.
 */

public class SyncDatabase {
    private static String TAG = "SyncDatabase";

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
    public static void Add_Disciples(JSONArray json_disciples){
        try{
            if(json_disciples.length()>0){
                Log.i(TAG,"Adding New Disciples -> \n"+json_disciples.toString());
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
                    cv.put(Database.DISCIPLES_FIELDS[11], obj.getString("created"));
                    Disciple disciple = DeepLife.myDATABASE.getDiscipleBySerID(Integer.valueOf(obj.getString("id")));
                    if(disciple == null){
                        long x = DeepLife.myDATABASE.insert(Database.Table_DISCIPLES,cv);
                        if(x>0){
                            Log.i(TAG,"Successfully Added: Disciples Added -> \n"+cv.toString());
                        }else {
                            Log.i(TAG,"Error During Adding: Disciples -> \n"+cv.toString());
                        }
                    }else {
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
        }catch (Exception e){
            Log.i(TAG,e.toString());
        }
    }
}
