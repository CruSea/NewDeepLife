
package deeplife.gcme.com.deeplife.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Disciples.Disciple;
import deeplife.gcme.com.deeplife.Models.Answer;
import deeplife.gcme.com.deeplife.Models.Category;
import deeplife.gcme.com.deeplife.Models.Country;
import deeplife.gcme.com.deeplife.Models.ImageSync;
import deeplife.gcme.com.deeplife.Models.Logs;
import deeplife.gcme.com.deeplife.Models.ReportItem;
import deeplife.gcme.com.deeplife.Models.Schedule;
import deeplife.gcme.com.deeplife.SyncService.SyncService;
import deeplife.gcme.com.deeplife.WinBuildSend.WinBuildSendQuestion;
import deeplife.gcme.com.deeplife.News.News;
import deeplife.gcme.com.deeplife.Testimony.Testimony;
import deeplife.gcme.com.deeplife.Models.User;

public class Database {
    public static final String Table_DISCIPLES = "DISCIPLES";
    public static final String Table_SCHEDULES = "SCHEDULES";
    public static final String Table_LOGS = "LOGS";
    public static final String Table_USER = "USER";
    public static final String Table_NEWSFEED = "NewsFeed";
    public static final String Table_COUNTRY = "countries";
    public static final String Table_Reports = "Reports";
    public static final String Table_Report_Forms = "Report_Form";
    public static final String Table_QUESTION_LIST = "QUESTION_LIST";
    public static final String Table_QUESTION_ANSWER = "QUESTION_ANSWER";
    public static final String Table_TESTIMONY = "TESTIMONY";
    public static final String Table_IMAGE_SYNC = "ImageToSync";
    public static final String Table_CATEGORIES = "Categories";

    public static final String[] DISCIPLES_FIELDS = {"SerID","FullName","DisplayName", "Email", "Phone", "Country","MentorID","Stage","ImageURL","ImagePath","Role","Gender","Created" };
    public static final String[] LOGS_FIELDS = { "Type", "Task","Value" };
    public static final String[] NewsFeed_FIELDS = { "News_ID", "Title","Content","Category","ImageURL","ImagePath","PubDate" };

    public static final String[] COUNTRY_FIELDS = { "serid", "iso3","name","code" };
    public static final String[] SCHEDULES_FIELDS = { "Disciple_Phone","Title","Alarm_Time","Alarm_Repeat","Description"};
    public static final String[] USER_FIELDS = { "SerID","Full_Name", "Email","Phone","Password","Country","Picture","Favorite_Scripture" };
    public static final String[] QUESTION_LIST_FIELDS = {"SerID","Category", "Question","Description","Mandatory","Type","Country","Created"};
    public static final String[] REPORT_FORM_FIELDS = {"Report_ID","Category","Questions"};
    public static final String[] REPORT_FIELDS = {"Report_ID","Value","Date"};
    public static final String[] QUESTION_ANSWER_FIELDS = {"SerID","DisciplePhone","Question_ID", "Answer","BuildStage"};
    public static final String[] TESTIMONY_FIELDS = {"SerID","UserID","Description","Status","PubDate","UserName"};
    public static final String[] IMAGE_SYNC_FIELDS = {"FileName", "Param"};
    public static final String[] CATEGORY_FIELDS = {"SerID", "Name", "Parent", "Status", "Created"};

    public static final String[] DISCIPLES_COLUMN = { "id", "SerID","FullName","DisplayName", "Email", "Phone", "Country","MentorID","Stage","ImageURL","ImagePath","Role","Gender","Created"  };
    public static final String[] SCHEDULES_COLUMN = { "id","Disciple_Phone","Title","Alarm_Time","Alarm_Repeat","Description" };
    public static final String[] REPORT_FORM_COLUMN = {"id","Report_ID","Category","Questions"};
    public static final String[] NewsFeed_COLUMN = { "id","News_ID", "Title","Content","Category","ImageURL","ImagePath","PubDate" };
    public static final String[] REPORT_COLUMN = {"id","Report_ID","Value","Date"};
    public static final String[] COUNTRY_COLUMN = {"id", "serid", "iso3","name","code"};
    public static final String[] LOGS_COLUMN = { "id", "Type", "Task","Value" };
    public static final String[] USER_COLUMN = { "id", "SerID","Full_Name", "Email","Phone","Password","Country","Picture","Favorite_Scripture"  };
    public static final String[] QUESTION_LIST_COLUMN = {"id","SerID","Category", "Question","Description","Mandatory","Type","Country","Created"};
    public static final String[] QUESTION_ANSWER_COLUMN = {"id","SerID","DisciplePhone","Question_ID", "Answer","BuildStage"};
    public static final String[] TESTIMONY_COLUMN = {"id","SerID","UserID","Description","Status","PubDate","UserName"};
    public static final String[] IMAGE_SYNC_COLUMN = {"id","FileName", "Param"};
    public static final String[] CATEGORY_COLUMN = {"id","SerID", "Name", "Parent", "Status", "Created"};


	private SQLiteDatabase myDatabase;
	private SQL_Helper mySQL;
	private Context myContext;
    public static final String TAG = "Database";

    public Database(Context context){
        myContext = context;
        mySQL = new SQL_Helper(myContext);
        myDatabase = mySQL.getWritableDatabase();
        mySQL.createTables(Table_DISCIPLES, DISCIPLES_FIELDS);
        mySQL.createTables(Table_LOGS, LOGS_FIELDS);
        mySQL.createTables(Table_USER, USER_FIELDS);
        mySQL.createTables(Table_SCHEDULES, SCHEDULES_FIELDS);
        mySQL.createTables(Table_QUESTION_LIST,QUESTION_LIST_FIELDS);
        mySQL.createTables(Table_QUESTION_ANSWER, QUESTION_ANSWER_FIELDS);
        mySQL.createTables(Table_Reports, REPORT_FIELDS);
        mySQL.createTables(Table_Report_Forms, REPORT_FORM_FIELDS);
        mySQL.createTables(Table_COUNTRY, COUNTRY_FIELDS);
        mySQL.createTables(Table_NEWSFEED, NewsFeed_FIELDS);
        mySQL.createTables(Table_TESTIMONY, TESTIMONY_FIELDS);
        mySQL.createTables(Table_IMAGE_SYNC,IMAGE_SYNC_FIELDS);
        mySQL.createTables(Table_CATEGORIES,CATEGORY_FIELDS);
    }

    public long insert(String DB_Table, ContentValues cv){
        long state = myDatabase.insert(DB_Table, null, cv);
        Log.i(TAG, "Inserting->: " + cv.toString());
        return state;
    }
    public long Delete_All(String DB_Table){
        long state = myDatabase.delete(DB_Table, null, null);
        return state;
    }
    public long remove(String DB_Table, int id){
        String[] args = {""+id};
        long val = myDatabase.delete(DB_Table, "id = ?", args);
        return val;
    }

    public long update(String DB_Table, ContentValues cv, int id){
        Log.i(TAG, "Updating Table: "+DB_Table);
        String[] args = {""+id};
        long state = myDatabase.update(DB_Table, cv, "id = ?", args);
        Log.i(TAG, "Updating Data: "+cv.toString());
        Log.i(TAG, "Updating Completed: "+state+"\n");
        return state;
    }

    public int count(String DB_Table){
        Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
        if(c != null){
            return c.getCount();
        }else{
            return 0;
        }
    }
    public Cursor getAll(String DB_Table){
        Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
        return c;
    }

    public String get_Value_At_Top(String DB_Table, String column){
        String str = "";
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            c.moveToFirst();
            str = c.getString(c.getColumnIndex(column));
        }catch (Exception e){

        }

        return str;
    }

    public String get_Value_At_Bottom(String DB_Table, String column){
        String str = "";
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            c.moveToLast();
            str = c.getString(c.getColumnIndex(column));
        }catch (Exception e){

        }
        return str;
    }

    public Cursor get_value_by_ID (String DB_Table, String id){
        Cursor cur = myDatabase.rawQuery("select * from " + DB_Table + " where id=" + id, null);
        return cur;
    }

    public String get_Name_by_phone(String phone){
        String name = "";
        try{
            String DB_Table = Table_DISCIPLES;
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), DISCIPLES_FIELDS[2] + " = '" + phone + "'", null, null, null, null);
            c.moveToLast();
            name = c.getString(c.getColumnIndex(DISCIPLES_FIELDS[0]));
        }catch (Exception e){

        }
        return name;
    }
    public long Delete_By_ID(String DB_Table, int pos){
        String[] args = {""+pos};
        long val = myDatabase.delete(DB_Table, "id = ?", args);
        return val;
    }
    public long Delete_By_Column(String DB_Table, String column, String val){
        String[] args = {val};
        long v = myDatabase.delete(DB_Table, column + " = ?", args);
        return v;
    }
    public int get_Top_ID(String DB_Table){
        int pos = -1;
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            c.moveToFirst();
            pos = Integer.valueOf(c.getString(c.getColumnIndex("id")));
        }catch (Exception e){

        }
        return pos;
    }

    public User get_User(){
        String DB_Table = Table_USER;
        User found = new User();
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            c.moveToFirst();
            for(int i=0;i<c.getCount();i++){
                c.moveToPosition(i);
                User dis = new User();
                dis.setID(Integer.valueOf(c.getString(c.getColumnIndex(USER_COLUMN[0]))));
                dis.setUser_Name(c.getString(c.getColumnIndex(USER_COLUMN[1])));
                dis.setUser_Email(c.getString(c.getColumnIndex(USER_COLUMN[2])));
                dis.setUser_Phone(c.getString(c.getColumnIndex(USER_COLUMN[3])));
                dis.setUser_Pass(c.getString(c.getColumnIndex(USER_COLUMN[4])));
                dis.setUser_Country(c.getString(c.getColumnIndex(USER_COLUMN[5])));
                dis.setUser_Picture(c.getString(c.getColumnIndex(USER_COLUMN[6])));
                dis.setUser_Favorite_Scripture(c.getString(c.getColumnIndex(USER_COLUMN[7])));
                return dis;
            }
        }catch (Exception e){

        }
        return found;
    }

    ////////////////////////////////
    ////////////////////////////////
    ///////  News FEEDS    /////////
    ////////////////////////////////
    ////////////////////////////////
    public News getNewsByID(int id) {
        String DB_Table = Table_NEWSFEED;
        News found = new News();
        Log.i(TAG, "Get News By ID: " + id);
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if(c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(NewsFeed_COLUMN[0])));
                    if(cur_id == id){
                        News news = new News();
                        news.setId(Integer.valueOf(c.getString(c.getColumnIndex(NewsFeed_COLUMN[0]))));
                        news.setSerID(Integer.valueOf(c.getString(c.getColumnIndex(NewsFeed_COLUMN[1]))));
                        news.setTitle(c.getString(c.getColumnIndex(NewsFeed_COLUMN[2])));
                        news.setContent(c.getString(c.getColumnIndex(NewsFeed_COLUMN[3])));
                        news.setCategory(c.getString(c.getColumnIndex(NewsFeed_COLUMN[4])));
                        news.setImageURL(c.getString(c.getColumnIndex(NewsFeed_COLUMN[5])));
                        news.setImagePath(c.getString(c.getColumnIndex(NewsFeed_COLUMN[6])));
                        news.setPubDate(c.getString(c.getColumnIndex(NewsFeed_COLUMN[7])));
                        return news;
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG, "Failed Get News By ID: "+e.toString());
            return null;
        }
        return null;

    }
    public News getNewsBySerID(int id) {
        Log.i(TAG, "Get News By Server ID: "+id);
        String DB_Table = Table_NEWSFEED;
        News found = new News();
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if(c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    int ser_id = Integer.valueOf(c.getString(c.getColumnIndex(NewsFeed_COLUMN[1])));
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(NewsFeed_COLUMN[0])));
                    if(ser_id == id){
                        News news = getNewsByID(cur_id);
                        return news;
                    }
                }
            }
        }catch (Exception e){
            return null;
        }
        return null;

    }
    public ArrayList<News> getAllNews(){
        Log.i(TAG, "Get All News: ");
        String DB_Table = Table_NEWSFEED;
        ArrayList<News> found = new ArrayList<News>();
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            c.moveToFirst();
            for(int i=0;i<c.getCount();i++){
                c.moveToPosition(i);
                int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(NewsFeed_COLUMN[0])));
                News new_news = getNewsByID(cur_id);
                if(new_news != null){
                    found.add(new_news);
                    Log.i(TAG, "Get All News Populating: "+new_news.getId());
                }
            }
        }catch (Exception e){
            return null;
        }
        return found;
    }

    /////////////////////////////////
    /////////////////////////////////
    ////////  Testimonies   /////////
    /////////////////////////////////
    /////////////////////////////////


    public Testimony getTestimonyByID(int id) {
        Log.i(TAG, "Get Testimony By ID: "+id);
        String DB_Table = Table_TESTIMONY;
        News found = new News();
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if(c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(TESTIMONY_COLUMN[0])));
                    if(cur_id == id){
                        Testimony testimony = new Testimony();
                        testimony.setID(Integer.valueOf(c.getString(c.getColumnIndex(TESTIMONY_COLUMN[0]))));
                        testimony.setSerID(Integer.valueOf(c.getString(c.getColumnIndex(TESTIMONY_COLUMN[1]))));
                        testimony.setUser_ID(Integer.valueOf(c.getString(c.getColumnIndex(TESTIMONY_COLUMN[2]))));
                        testimony.setContent(c.getString(c.getColumnIndex(TESTIMONY_COLUMN[3])));
                        testimony.setStatus(Integer.valueOf(c.getString(c.getColumnIndex(TESTIMONY_COLUMN[4]))));
                        testimony.setPubDate(c.getString(c.getColumnIndex(TESTIMONY_COLUMN[5])));
                        testimony.setUserName(c.getString(c.getColumnIndex(TESTIMONY_COLUMN[6])));
                        return testimony;
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG, "Failed Get Testimony By ID: "+e.toString());
            return null;
        }
        return null;

    }
    public Testimony getTestimonyBySerID(int id) {
        Log.i(TAG, "Get Testimony By Server ID: "+id);
        String DB_Table = Table_TESTIMONY;
        Testimony found = new Testimony();
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if(c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    int ser_id = Integer.valueOf(c.getString(c.getColumnIndex(TESTIMONY_COLUMN[1])));
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(TESTIMONY_COLUMN[0])));
                    if(ser_id == id){
                        Testimony testimony = getTestimonyByID(cur_id);
                        return testimony;
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG, "Failed Get Testimony By Server ID: "+e.toString());
            return null;
        }
        return null;
    }
    public ArrayList<Testimony> getAllTestimonies(){
        Log.i(TAG, "Get All Testimonies: ");
        String DB_Table = Table_TESTIMONY;
        ArrayList<Testimony> found = new ArrayList<Testimony>();
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            c.moveToFirst();
            for(int i=0;i<c.getCount();i++){
                c.moveToPosition(i);
                int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(TESTIMONY_COLUMN[0])));
                Testimony newTestimony = getTestimonyByID(cur_id);
                if(newTestimony != null){
                    found.add(newTestimony);
                    Log.i(TAG, "Get All Testimonies Populating: "+newTestimony.getID());
                }
            }
        }catch (Exception e){
            return null;
        }
        return found;
    }

    ////////////////////////////////
    ////////////////////////////////
    ///////  Disciples   ///////////
    ////////////////////////////////
    ////////////////////////////////


    public Disciple getDiscipleByID(int id) {
        Log.i(TAG, "Get Disciple by ID: ");
        String DB_Table = Table_DISCIPLES;
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if(c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[0])));
                    if(cur_id == id){
                        Disciple disciple = new Disciple();
                        disciple.setID(Integer.valueOf(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[0]))));
                        disciple.setSerID(Integer.valueOf(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[1]))));
                        disciple.setFullName(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[2])));
                        disciple.setDisplayName(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[3])));
                        disciple.setEmail(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[4])));
                        disciple.setPhone(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[5])));
                        disciple.setCountry(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[6])));
                        disciple.setMentorID(Integer.valueOf(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[7]))));
                        disciple.setStage(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[8])));
                        disciple.setImageURL(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[9])));
                        disciple.setImagePath(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[10])));
                        disciple.setRole(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[11])));
                        disciple.setGender(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[12])));
                        disciple.setCreated(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[13])));
                        return disciple;
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG, "Failed Get Disciple by ID: "+e.toString());
            return null;
        }
        return null;

    }


    public Disciple getDiscipleBySerID(int id) {
        Log.i(TAG, "Get Disciple by Server ID: ");
        String DB_Table = Table_DISCIPLES;
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if(c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    int ser_id = Integer.valueOf(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[1])));
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[0])));
                    if(ser_id == id){
                        Disciple disciple = getDiscipleByID(cur_id);
                        return disciple;
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG, "Failed Get Disciple by Server ID: "+e.toString());
            return null;
        }
        return null;
    }
    public Disciple getDiscipleByPhone(String phone) {
        Log.i(TAG, "Get Disciple by Server ID: ");
        String DB_Table = Table_DISCIPLES;
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if(c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    String phone_num = c.getString(c.getColumnIndex(DISCIPLES_COLUMN[5]));
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[0])));
                    if(phone_num.equals(phone)){
                        Disciple disciple = getDiscipleByID(cur_id);
                        return disciple;
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG, "Failed Get Disciple by Server ID: "+e.toString());
            return null;
        }
        return null;
    }
    public ArrayList<Disciple> getAllDisciples(){
        Log.i(TAG, "Get All Disciples: ");
        String DB_Table = Table_DISCIPLES;
        ArrayList<Disciple> found = new ArrayList<Disciple>();
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            c.moveToFirst();
            for(int i=0;i<c.getCount();i++){
                c.moveToPosition(i);
                int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[0])));
                Disciple disciple = getDiscipleByID(cur_id);
                if(disciple != null){
                    found.add(disciple);
                    Log.i(TAG, "Get All Disciples: "+disciple.getID());
                }
            }
        }catch (Exception e){
            return null;
        }
        return found;
    }
    public long updateDisciple(Disciple disciple) {
        Log.i(TAG, "UPDATE updateDisciple: ");
        String DB_Table = Table_DISCIPLES;
        try{
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
            int id = DeepLife.myDATABASE.getDiscipleByPhone(disciple.getPhone()).getID();
            long  x = DeepLife.myDATABASE.update(DB_Table,cv,id);
            return x;
        }catch (Exception e){
            Log.i(TAG, "Failed UPDATE updateDisciple: "+e.toString());
            return 0;
        }
    }
    ////////////////////////////////
    ////////////////////////////////
    ///////  CATEGORY    ///////////
    ////////////////////////////////
    ////////////////////////////////


    public Category getCategoryByID(int id) {
        Log.i(TAG, "Get Category by ID: "+id);
        String DB_Table = Table_CATEGORIES;
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if(c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(CATEGORY_COLUMN[0])));
                    Log.i(TAG, "--> Get Category by SerID: "+c.getColumnIndex(CATEGORY_COLUMN[1])+", "+c.getColumnIndex(CATEGORY_COLUMN[2]));
                    if(cur_id == id){
                        Category category = new Category();
                        category.setID(Integer.valueOf(c.getString(c.getColumnIndex(CATEGORY_COLUMN[0]))));
                        category.setSerID(Integer.valueOf(c.getString(c.getColumnIndex(CATEGORY_COLUMN[1]))));
                        category.setName(c.getString(c.getColumnIndex(CATEGORY_COLUMN[2])));
                        category.setParent(Integer.valueOf(c.getString(c.getColumnIndex(CATEGORY_COLUMN[3]))));
                        category.setStatus(Integer.valueOf(c.getString(c.getColumnIndex(CATEGORY_COLUMN[4]))));
                        return category;
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG, "Failed Get Category by ID: "+e.toString());
            return null;
        }
        return null;

    }

    public Category getCategoryBySerID(int id) {
        Log.i(TAG, "Get Category by Server ID: ");
        String DB_Table = Table_CATEGORIES;
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if(c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    int ser_id = Integer.valueOf(c.getString(c.getColumnIndex(CATEGORY_COLUMN[1])));
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(CATEGORY_COLUMN[0])));
                    if(ser_id == id){
                        Category category = getCategoryByID(cur_id);
                        return category;
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG, "Failed Get Category by Server ID: "+e.toString());
            return null;
        }
        return null;
    }
    public Category getCategoryByParentID(int id) {
        Log.i(TAG, "Get Category by Server ID: ");
        String DB_Table = Table_CATEGORIES;
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if(c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    int par_id = Integer.valueOf(c.getString(c.getColumnIndex(CATEGORY_COLUMN[3])));
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(CATEGORY_COLUMN[0])));
                    if(par_id == id){
                        Category category = getCategoryByID(cur_id);
                        return category;
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG, "Failed Get Category by Server ID: "+e.toString());
            return null;
        }
        return null;
    }
    public List<Category> getCategoriesByParentID(int id) {
        Log.i(TAG, "Get Category by Server ID: ");
        String DB_Table = Table_CATEGORIES;
        List<Category> found = new ArrayList<Category>();
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if(c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    int par_id = Integer.valueOf(c.getString(c.getColumnIndex(CATEGORY_COLUMN[3])));
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(CATEGORY_COLUMN[0])));
                    if(par_id == id){
                        Category category = getCategoryByID(cur_id);
                        if(category != null){
                            found.add(category);
                        }
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG, "Failed Get Category by Server ID: "+e.toString());
            return found;
        }
        return found;
    }
    public ArrayList<Category> getParentCategory(){
        Log.i(TAG, "Get All Parent Category: ");
        String DB_Table = Table_CATEGORIES;
        ArrayList<Category> found = new ArrayList<Category>();
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            c.moveToFirst();
            for(int i=0;i<c.getCount();i++){
                c.moveToPosition(i);
                int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(CATEGORY_COLUMN[0])));
                Category category = getCategoryByID(cur_id);
                if(category!= null && category.getID() == 0){
                    found.add(category);
                    Log.i(TAG, "Get All Parent Category: "+category.getID());
                }
            }
        }catch (Exception e){
            Log.i(TAG, "Failed Get All Parent Category: "+e.toString());
            return null;
        }
        return found;
    }


    ////////////////////////////////
    ////////////////////////////////
    //  WIN BUILD SEND QUESTION  //
    ////////////////////////////////
    ////////////////////////////////


    public WinBuildSendQuestion getWinBuildSendQuestionByID(int id) {
        Log.i(TAG, "Get WinBuildQuestion by ID: ");
        String DB_Table = Table_QUESTION_LIST;
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if(c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(QUESTION_LIST_COLUMN[0])));
                    if(cur_id == id){
                        WinBuildSendQuestion winBuildSendQuestion = new WinBuildSendQuestion();
                        winBuildSendQuestion.setID(Integer.valueOf(c.getString(c.getColumnIndex(QUESTION_LIST_COLUMN[0]))));
                        winBuildSendQuestion.setSerID(Integer.valueOf(c.getString(c.getColumnIndex(QUESTION_LIST_COLUMN[1]))));
                        winBuildSendQuestion.setCategory(Integer.valueOf(c.getString(c.getColumnIndex(QUESTION_LIST_COLUMN[2]))));
                        winBuildSendQuestion.setQuestion(c.getString(c.getColumnIndex(QUESTION_LIST_COLUMN[3])));
                        winBuildSendQuestion.setDescription(c.getString(c.getColumnIndex(QUESTION_LIST_COLUMN[4])));
                        winBuildSendQuestion.setMandatory(Integer.valueOf(c.getString(c.getColumnIndex(QUESTION_LIST_COLUMN[5]))));
                        winBuildSendQuestion.setType(c.getString(c.getColumnIndex(QUESTION_LIST_COLUMN[6])));
                        winBuildSendQuestion.setCauntry(Integer.valueOf(c.getString(c.getColumnIndex(QUESTION_LIST_COLUMN[7]))));
                        winBuildSendQuestion.setCreated(c.getString(c.getColumnIndex(QUESTION_LIST_COLUMN[8])));
                        return winBuildSendQuestion;
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG, "Failed Get WinBuildQuestion by ID: "+e.toString());
            return null;
        }
        return null;

    }

    public WinBuildSendQuestion getWinBuildSendQuestionBySerID(int id) {
        Log.i(TAG, "Get WinBuildQuestion by Server ID: ");
        String DB_Table = Table_QUESTION_LIST;
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if(c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    int ser_id = Integer.valueOf(c.getString(c.getColumnIndex(QUESTION_LIST_COLUMN[1])));
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(QUESTION_LIST_COLUMN[0])));
                    if(ser_id == id){
                        WinBuildSendQuestion winBuildSendQuestion = getWinBuildSendQuestionByID(cur_id);
                        return winBuildSendQuestion;
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG, "Failed Get WinBuildQuestion by Server ID: "+e.toString());
            return null;
        }
        return null;
    }

    public ArrayList<WinBuildSendQuestion> getWinBuildSendQuestionsByCategory(int categoryID){
        Log.i(TAG, "Get All WinBuildQuestion by Category id: "+categoryID);
        String DB_Table = Table_QUESTION_LIST;
        ArrayList<WinBuildSendQuestion> found = new ArrayList<WinBuildSendQuestion>();
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            c.moveToFirst();
            for(int i=0;i<c.getCount();i++){
                c.moveToPosition(i);
                int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(QUESTION_LIST_COLUMN[0])));
                int cat_id = Integer.valueOf(c.getString(c.getColumnIndex(QUESTION_LIST_COLUMN[2])));
                if(cat_id == categoryID){
                    WinBuildSendQuestion winBuildSendQuestion = getWinBuildSendQuestionByID(cur_id);
                    if(winBuildSendQuestion != null){
                        found.add(winBuildSendQuestion);
                        Log.i(TAG, "Get All WinBuildQuestion: "+winBuildSendQuestion.getID());
                    }

                }
            }
        }catch (Exception e){
            Log.i(TAG, "Failed Get getWinBuildSendQuestionsByCategory: "+e.toString());
            return null;
        }
        return found;
    }
    public ArrayList<WinBuildSendQuestion> getWinBuildSendQuestionsByCategorySerID(int SerID){
        Log.i(TAG, "Get All WinBuildQuestion by Category Parent ID: "+SerID);
        ArrayList<WinBuildSendQuestion> found = new ArrayList<WinBuildSendQuestion>();
        if(SerID > 0){
            found = getWinBuildSendQuestionsByCategory(SerID);
            Log.i(TAG, "Get All WinBuildQuestion: "+SerID);
            return found;
        }
        return found;
    }

    ////////////////////////////////
    ////////////////////////////////
    //  Answers Table  //
    ////////////////////////////////
    ////////////////////////////////


    public Answer getAnswerByID(int id) {
        Log.i(TAG, "Get Answer by ID: ");
        String DB_Table = Table_QUESTION_ANSWER;
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if(c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(QUESTION_ANSWER_COLUMN[0])));
                    if(cur_id == id){
                        Answer answer = new Answer();
                        answer.setID(Integer.valueOf(c.getString(c.getColumnIndex(QUESTION_ANSWER_COLUMN[0]))));
                        answer.setSerID(Integer.valueOf(c.getString(c.getColumnIndex(QUESTION_ANSWER_COLUMN[1]))));
                        answer.setDisciplePhone(c.getString(c.getColumnIndex(QUESTION_ANSWER_COLUMN[2])));
                        answer.setQuestionID(Integer.valueOf(c.getString(c.getColumnIndex(QUESTION_ANSWER_COLUMN[3]))));
                        answer.setAnswer(c.getString(c.getColumnIndex(QUESTION_ANSWER_COLUMN[4])));
                        answer.setBuildStage(c.getString(c.getColumnIndex(QUESTION_ANSWER_COLUMN[5])));
                        return answer;
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG, "Failed Get WinBuildQuestion by ID: "+e.toString());
            return null;
        }
        return null;
    }
    public Answer getAnswerBySerID(int id) {
        Log.i(TAG, "Get getAnswerBySerID by ServerID: ");
        String DB_Table = Table_QUESTION_ANSWER;
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if(c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    int qst_id = Integer.valueOf(c.getString(c.getColumnIndex(QUESTION_ANSWER_COLUMN[2])));
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(QUESTION_ANSWER_COLUMN[0])));
                    if(qst_id == id){
                        Answer answer = getAnswerByID(cur_id);
                        return answer;
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG, "Failed Get getAnswerByQuestionID by QuestionID: "+e.toString());
            return null;
        }
        return null;
    }
    public Answer getAnswerByQuestionID(int id) {
        Log.i(TAG, "Get getAnswerByQuestionID by QuestionID: ");
        String DB_Table = Table_QUESTION_ANSWER;
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if(c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    int qst_id = Integer.valueOf(c.getString(c.getColumnIndex(QUESTION_ANSWER_COLUMN[3])));
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(QUESTION_ANSWER_COLUMN[0])));
                    if(qst_id == id){
                        Answer answer = getAnswerByID(cur_id);
                        return answer;
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG, "Failed Get getAnswerByQuestionID by QuestionID: "+e.toString());
            return null;
        }
        return null;
    }
    public Answer getAnswerByQuestionIDandDisciplePhone(int questionID, String disciplePhone) {
        Log.i(TAG, "Get getAnswerByQuestionIDandDisciplePhone by QuestionPhone and DisciplePhone: ");
        String DB_Table = Table_QUESTION_ANSWER;
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if(c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    int qst_id = Integer.valueOf(c.getString(c.getColumnIndex(QUESTION_ANSWER_COLUMN[3])));
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(QUESTION_ANSWER_COLUMN[0])));
                    String dis_ph = c.getString(c.getColumnIndex(QUESTION_ANSWER_COLUMN[2]));
                    if(qst_id == questionID && disciplePhone.equals(dis_ph)){
                        Answer answer = getAnswerByID(cur_id);
                        return answer;
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG, "Failed Get getAnswerByQuestionIDandDisciplePhone by QuestionPhone and DisciplePhone: "+e.toString());
            return null;
        }
        return null;
    }
    public long updateAnswer(Answer answer) {
        Log.i(TAG, "UPDATE updateAnswer: ");
        String DB_Table = Table_QUESTION_ANSWER;
        try{
            ContentValues cv = new ContentValues();
            cv.put(Database.QUESTION_ANSWER_FIELDS[0], answer.getSerID());
            cv.put(Database.QUESTION_ANSWER_FIELDS[1], answer.getDisciplePhone());
            cv.put(Database.QUESTION_ANSWER_FIELDS[2], answer.getQuestionID());
            cv.put(Database.QUESTION_ANSWER_FIELDS[3], answer.getAnswer());
            cv.put(Database.QUESTION_ANSWER_FIELDS[4], answer.getBuildStage());
            int id = DeepLife.myDATABASE.getAnswerByQuestionIDandDisciplePhone(answer.getQuestionID(),answer.getDisciplePhone()).getID();
            long  x = DeepLife.myDATABASE.update(DB_Table,cv,id);
            return x;
        }catch (Exception e){
            Log.i(TAG, "Failed UPDATE updateAnswer: "+e.toString());
            return 0;
        }
    }
    public long add_updateAnswer(Answer answer) {
        Log.i(TAG, "add_updateAnswer: ");
        Answer oldAnswer1 = getAnswerByQuestionIDandDisciplePhone(answer.getQuestionID(),answer.getDisciplePhone());
        if(oldAnswer1 == null){
            long res = addAnswer(answer);
            return res;
        }else {
            long res = updateAnswer(answer);
            return res;
        }
    }
    public long addAnswer(Answer answer) {
        Log.i(TAG, "Add addAnswer: ");
        String DB_Table = Table_QUESTION_ANSWER;
        try{
            ContentValues cv = new ContentValues();
            cv.put(Database.QUESTION_ANSWER_FIELDS[0], answer.getSerID());
            cv.put(Database.QUESTION_ANSWER_FIELDS[1], answer.getDisciplePhone());
            cv.put(Database.QUESTION_ANSWER_FIELDS[2], answer.getQuestionID());
            cv.put(Database.QUESTION_ANSWER_FIELDS[3], answer.getAnswer());
            cv.put(Database.QUESTION_ANSWER_FIELDS[4], answer.getBuildStage());
            Answer oldAnswer1 = getAnswerBySerID(answer.getSerID());
            if(oldAnswer1 == null){
                long  x = DeepLife.myDATABASE.insert(DB_Table,cv);
                return x;
            }
            return 0;
        }catch (Exception e){
            Log.i(TAG, "Failed Add addAnswer: "+e.toString());
            return 0;
        }
    }


    ////////////////////////////////
    ////////////////////////////////
    ///////  Country Table    /////////
    ////////////////////////////////
    ////////////////////////////////
    public Country getCountryByID(int id) {
        String DB_Table = Table_COUNTRY;
        News found = new News();
        Log.i(TAG, "Get Country By ID: " + id);
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if(c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(COUNTRY_COLUMN[0])));
                    if(cur_id == id){
                        Country country = new Country();
                        country.setID(Integer.valueOf(c.getString(c.getColumnIndex(COUNTRY_COLUMN[0]))));
                        country.setSerID(Integer.valueOf(c.getString(c.getColumnIndex(COUNTRY_COLUMN[1]))));
                        country.setISO3(c.getString(c.getColumnIndex(COUNTRY_COLUMN[2])));
                        country.setName(c.getString(c.getColumnIndex(COUNTRY_COLUMN[3])));
                        country.setCode(Integer.valueOf(c.getString(c.getColumnIndex(COUNTRY_COLUMN[4]))));
                        return country;
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG, "Failed Get Country By ID: "+e.toString());
            return null;
        }
        return null;

    }
    public Country getCountryBySerID(int id) {
        Log.i(TAG, "Get getCountryBySerID by ServerID: ");
        String DB_Table = Table_COUNTRY;
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if(c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    int ser_id = Integer.valueOf(c.getString(c.getColumnIndex(COUNTRY_COLUMN[1])));
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(COUNTRY_COLUMN[0])));
                    if(ser_id == id){
                        Country country = getCountryByID(cur_id);
                        return country;
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG, "Failed Get getCountryBySerID by ServerID: "+e.toString());
            return null;
        }
        return null;
    }
    public ArrayList<Country> getAllCountries(){
        Log.i(TAG, "Get All getAllCountries: ");
        String DB_Table = Table_COUNTRY;
        ArrayList<Country> found = new ArrayList<Country>();
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            c.moveToFirst();
            for(int i=0;i<c.getCount();i++){
                c.moveToPosition(i);
                int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(COUNTRY_COLUMN[0])));
                Country country = getCountryByID(cur_id);
                if(country != null){
                    found.add(country);
                    Log.i(TAG, "Get All Countries: "+country.getID());
                }
            }
        }catch (Exception e){
            return null;
        }
        return found;
    }

    public ArrayList<Logs> getSendLogs(){
        Log.i(TAG, "SendLogs:\n");
        ArrayList<Logs> Found = new ArrayList<Logs>();
        try{
            Cursor c = myDatabase.query(Table_LOGS, LOGS_COLUMN, null, null, null, null, null);
            if(c != null && c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    String str = c.getString(c.getColumnIndex(LOGS_COLUMN[2]));
                    Log.i(TAG, "Comparing-> \n" + SyncService.Sync_Tasks[0] + " | "+str);
                    if (SyncService.Sync_Tasks[0].equals(str)){
                        Log.i(TAG, "SendLogs Count:-> " + c.getCount());
                        Logs newLogs = new Logs();
                        newLogs.setID(Integer.valueOf(c.getString(c.getColumnIndex(LOGS_COLUMN[0]))));
                        newLogs.setType(c.getString(c.getColumnIndex(LOGS_COLUMN[1])));
                        newLogs.setTask(c.getString(c.getColumnIndex(LOGS_COLUMN[2])));
                        newLogs.setValue(c.getString(c.getColumnIndex(LOGS_COLUMN[3])));
                        Log.i(TAG, "Found for SendLogs:-> \n" + newLogs.toString());
                        Found.add(newLogs);
                    }
                }
                return Found;
            }
        }catch (Exception e){
            Log.i(TAG, "Faild getSendLogs:\n"+e.toString());
            return null;
        }
        return Found;
    }
    public ImageSync getTopImageSync(){
        String DB_Table = Table_IMAGE_SYNC;
        ImageSync found = new ImageSync();
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if(c.getCount()>0){
                c.moveToPosition(c.getCount()-1);
                ImageSync dis = new ImageSync();
                dis.setId(c.getString(c.getColumnIndex(IMAGE_SYNC_COLUMN[0])));
                dis.setFilePath(c.getString(c.getColumnIndex(IMAGE_SYNC_COLUMN[1])));
                dis.setParam(c.getString(c.getColumnIndex(IMAGE_SYNC_COLUMN[2])));
                return dis;
            }else{
                return null;
            }
        }catch (Exception e){

        }
        return null;
    }

    public ArrayList<Disciple> getSendDisciples(){
        Log.i(TAG, "getSendDisciples:\n");
        ArrayList<Disciple> Found = new ArrayList<Disciple>();
        try{
            Cursor c = myDatabase.query(Table_LOGS, LOGS_COLUMN, null, null, null, null, null);
            if(c != null && c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    String Log_Task = c.getString(c.getColumnIndex(LOGS_COLUMN[2]));
                    String Log_Value = c.getString(c.getColumnIndex(LOGS_COLUMN[3]));
                    int Log_ID = Integer.valueOf(c.getString(c.getColumnIndex(LOGS_COLUMN[0])));
                    Log.i(TAG, "Comparing-> \n" + SyncService.Sync_Tasks[1] + " | "+Log_Task);
                    if(SyncService.Sync_Tasks[1].equals(Log_Task)){
                        Log.i(TAG, "SendDisciples Count:-> " + c.getCount());
                        Disciple sendDisciple = getDiscipleByPhone(Log_Value);
                        if(sendDisciple !=null){
                            sendDisciple.setID(Log_ID);
                            Log.i(TAG, "Found for Send:-> \n" + sendDisciple.toString());
                            Found.add(sendDisciple);
                        }
                    }
                }
                return Found;
            }
        }catch (Exception e){
            Log.i(TAG, "Failed getSendDisciples:\n"+e.toString());
        }
        return Found;
    }

    public ArrayList<Disciple> getUpdateDisciples(){
        Log.i(TAG, "getUpdateDisciples:\n");
        ArrayList<Disciple> Found = new ArrayList<Disciple>();
        try{
            Cursor c = myDatabase.query(Table_LOGS, LOGS_COLUMN, null, null, null, null, null);
            if(c != null && c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    int ID = Integer.valueOf(c.getString(c.getColumnIndex(LOGS_COLUMN[0])));
                    String Log_Task = c.getString(c.getColumnIndex(LOGS_COLUMN[2]));
                    String Task_Value = c.getString(c.getColumnIndex(LOGS_COLUMN[3]));
                    Log.i(TAG, "Comparing-> \n" + SyncService.Sync_Tasks[3] + " | "+Log_Task);
                    if(SyncService.Sync_Tasks[3].equals(Log_Task)){
                        Log.i(TAG, "UpdateDisciples Count:-> " + c.getCount());
                        Disciple updateDisciple = getDiscipleByPhone(Task_Value);
                        if(updateDisciple !=null){
                            updateDisciple.setID(ID);
                            Found.add(updateDisciple);
                        }
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG, "Failed getUpdateDisciples:\n"+e.toString());
        }
        return Found;
    }
    public ArrayList<Testimony> getSendTestimony(){
        Log.i(TAG, "getSendTestimony:\n");
        ArrayList<Testimony> Found = new ArrayList<Testimony>();
        try{
            Cursor c = myDatabase.query(Table_LOGS, LOGS_COLUMN, null, null, null, null, null);
            if(c != null && c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    String str = c.getString(c.getColumnIndex(LOGS_COLUMN[2]));
                    int id = Integer.valueOf(c.getString(c.getColumnIndex(LOGS_COLUMN[3])));
                    int ID = Integer.valueOf(c.getString(c.getColumnIndex(LOGS_COLUMN[0])));
                    Log.i(TAG, "Comparing-> \n" + SyncService.Sync_Tasks[6] + " | "+str);
                    if(SyncService.Sync_Tasks[6].equals(str)){
                        Log.i(TAG, "SendTestimony Count:-> " + c.getCount());
                        Testimony newTestimony = getTestimonyByID(id);
                        if(newTestimony !=null){
                            newTestimony.setID(ID);
                            Log.i(TAG, "Found for Testimony Send:-> \n" + newTestimony.toString());
                            Found.add(newTestimony);
                        }
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG, "Failed getSendTestimony:\n"+e.toString());
        }
        return Found;
    }

    public ArrayList<Answer> getSendAnswers(){
        Log.i(TAG, "getSendAnswers:\n");
        ArrayList<Answer> Found = new ArrayList<Answer>();
        try{
            Cursor c = myDatabase.query(Table_LOGS, LOGS_COLUMN, null, null, null, null, null);
            if(c != null && c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    String Task_Name = c.getString(c.getColumnIndex(LOGS_COLUMN[2]));
                    int AnswerID = Integer.valueOf(c.getString(c.getColumnIndex(LOGS_COLUMN[3])));
                    int LogID = Integer.valueOf(c.getString(c.getColumnIndex(LOGS_COLUMN[0])));
                    Log.i(TAG, "Comparing-> \n" + SyncService.Sync_Tasks[6] + " | "+Task_Name);
                    if(SyncService.Sync_Tasks[11].equals(Task_Name)){
                        Log.i(TAG, "SendAnswer Count:-> " + c.getCount());
                        Answer sendAnswer = getAnswerByID(AnswerID);
                        if(sendAnswer !=null){
                            sendAnswer.setID(LogID);
                            Log.i(TAG, "Found for SendAnswer Send:-> \n" + sendAnswer.toString());
                            Found.add(sendAnswer);
                        }
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG, "Failed getSendAnswers:\n"+e.toString());
        }
        return Found;
    }





    public ArrayList<Schedule> getSendSchedules(){
        Log.i(TAG, "SendSchedules:\n");
        ArrayList<Schedule> Found = new ArrayList<Schedule>();
        try{
            Cursor c = myDatabase.query(Table_LOGS, LOGS_COLUMN, null, null, null, null, null);
            if(c != null && c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    String str = c.getString(c.getColumnIndex(LOGS_COLUMN[2]));
                    String id = c.getString(c.getColumnIndex(LOGS_COLUMN[3]));
                    String ID = c.getString(c.getColumnIndex(LOGS_COLUMN[0]));
                    Log.i(TAG, "SendSchedule Comparing-> \n" + SyncService.Sync_Tasks[4] + " | "+str);
                    if(SyncService.Sync_Tasks[4].equals(str)){
//                        Log.i(TAG, "SendSchedule Count:-> " + c.getCount());
//                        Schedule newSchedule = getScheduleWithId(id);
//                        if(newSchedule !=null){
//                            newSchedule.setID(ID);
//                            Log.i(TAG, "Found for SendSchedules:-> \n" + newSchedule.toString());
//                            Found.add(newSchedule);
//                        }
                    }
                }
            }
        }catch (Exception e){

        }
        return Found;
    }

    public ArrayList<Schedule> getUpdateSchedules(){
        Log.i(TAG, "UpdateDisciples:\n");
        ArrayList<Schedule> Found = new ArrayList<Schedule>();
        try{
            Cursor c = myDatabase.query(Table_LOGS, LOGS_COLUMN, null, null, null, null, null);
            if(c != null && c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    String ID = c.getString(c.getColumnIndex(LOGS_COLUMN[0]));
                    String str = c.getString(c.getColumnIndex(LOGS_COLUMN[2]));
                    String id = c.getString(c.getColumnIndex(LOGS_COLUMN[3]));
                    Log.i(TAG, "Comparing-> \n" + SyncService.Sync_Tasks[3] + " | "+str);
                    if(SyncService.Sync_Tasks[7].equals(str)){
//                        Log.i(TAG, "Update Schedule Count:-> " + c.getCount());
//                        Schedule newschedule = getScheduleWithId(id);
//                        if(newschedule !=null){
//                            newschedule.setID(ID);
//                            Found.add(newschedule);
//                        }
                    }
                }
            }
        }catch (Exception e){

        }
        return Found;
    }

    public ArrayList<ReportItem> getSendReports(){
        Log.i(TAG, "SendReports:\n");
        ArrayList<ReportItem> Found = new ArrayList<ReportItem>();
        try{
            Cursor c = myDatabase.query(Table_LOGS, LOGS_COLUMN, null, null, null, null, null);
            if(c != null && c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    String str = c.getString(c.getColumnIndex(LOGS_COLUMN[2]));
                    String id = c.getString(c.getColumnIndex(LOGS_COLUMN[3]));
                    String ID = c.getString(c.getColumnIndex(LOGS_COLUMN[0]));
                    Log.i(TAG, "Comparing-> \n" + SyncService.Sync_Tasks[5] + " | "+str);
                    if(SyncService.Sync_Tasks[5].equals(str)){
                        Log.i(TAG, "SendDisciples Count:-> " + c.getCount());
//                        ReportItem newReport = get_Report_by_id(id);
//                        if(newReport !=null){
//                            newReport.setId(ID);
//                            Log.i(TAG, "Found for Send:-> \n" + newReport.toString());
//                            Found.add(newReport);
//                        }
                    }
                }
            }
        }catch (Exception e){

        }
        return Found;
    }


    /////////////////////////////////
    /////////////////////////////////
    ////////  Main User   /////////
    /////////////////////////////////
    /////////////////////////////////


    public User getMainUser() {
        Log.i(TAG, "Get getMainUser: ");
        String DB_Table = Table_USER;
        try{
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if(c.getCount()>0){
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
                    c.moveToPosition(i);
                    User user = new User();
                    user.setID(Integer.valueOf(c.getString(c.getColumnIndex(USER_COLUMN[0]))));
                    user.setSerID(Integer.valueOf(c.getString(c.getColumnIndex(USER_COLUMN[1]))));
                    user.setFull_Name(c.getString(c.getColumnIndex(USER_COLUMN[2])));
                    user.setUser_Email(c.getString(c.getColumnIndex(USER_COLUMN[3])));
                    user.setUser_Phone(c.getString(c.getColumnIndex(USER_COLUMN[4])));
                    user.setUser_Pass(c.getString(c.getColumnIndex(USER_COLUMN[5])));
                    user.setUser_Country(c.getString(c.getColumnIndex(USER_COLUMN[6])));
                    user.setUser_Picture(c.getString(c.getColumnIndex(USER_COLUMN[7])));
                    user.setUser_Favorite_Scripture(c.getString(c.getColumnIndex(USER_COLUMN[8])));
                    return user;
                }
            }
        }catch (Exception e){
            Log.i(TAG, "Failed Get MainUser By ID: "+e.toString());
            return null;
        }
        return null;

    }

    public long updateMainUser(User mainUser) {
        Log.i(TAG, "UPDATE updateDisciple: ");
        String DB_Table = Table_USER;
        try{
            ContentValues cv = new ContentValues();
            cv.put(Database.USER_FIELDS[0], mainUser.getSerID());
            cv.put(Database.USER_FIELDS[1], mainUser.getFull_Name());
            cv.put(Database.USER_FIELDS[2], mainUser.getUser_Email());
            cv.put(Database.USER_FIELDS[3], mainUser.getUser_Phone());
            cv.put(Database.USER_FIELDS[4], mainUser.getUser_Pass());
            cv.put(Database.USER_FIELDS[5], mainUser.getUser_Country());
            cv.put(Database.USER_FIELDS[6], mainUser.getUser_Picture());
            cv.put(Database.USER_FIELDS[7], mainUser.getUser_Favorite_Scripture());
            long x = DeepLife.myDATABASE.update(DB_Table,cv,mainUser.getID());
            return x;
        }catch (Exception e){
            Log.i(TAG, "Failed UPDATE updateMainUser: "+e.toString());
            return 0;
        }
    }

//    public ArrayList<ImageSync> Get_All_ImageSync(){
//        String DB_Table = Table_IMAGE_SYNC;
//        ArrayList<ImageSync> found = new ArrayList<ImageSync>();
//        try{
//            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
//            c.moveToFirst();
//            for(int i=0;i<c.getCount();i++){
//                c.moveToPosition(i);
//                ImageSync dis = new ImageSync();
//                dis.setId(c.getString(c.getColumnIndex(IMAGE_SYNC_COLUMN[0])));
//                dis.setFilePath(c.getString(c.getColumnIndex(IMAGE_SYNC_COLUMN[1])));
//                dis.setParam(c.getString(c.getColumnIndex(IMAGE_SYNC_COLUMN[2])));
//                found.add(dis);
//            }
//        }catch (Exception e){
//
//        }
//        return found;
//    }
//
//    public ArrayList<Testimony> getSendTestimony(){
//        Log.i(TAG, "SendTestimony:\n");
//        ArrayList<Testimony> Found = new ArrayList<Testimony>();
//        try{
//            Cursor c = myDatabase.query(Table_LOGS, LOGS_COLUMN, null, null, null, null, null);
//            if(c != null && c.getCount()>0){
//                c.moveToFirst();
//                for(int i=0;i<c.getCount();i++){
//                    c.moveToPosition(i);
//                    String str = c.getString(c.getColumnIndex(LOGS_COLUMN[2]));
//                    String id = c.getString(c.getColumnIndex(LOGS_COLUMN[3]));
//                    String ID = c.getString(c.getColumnIndex(LOGS_COLUMN[0]));
//                    Log.i(TAG, "Comparing-> \n" + SyncService.Sync_Tasks[6] + " | "+str);
//                    if(SyncService.Sync_Tasks[6].equals(str)){
//                        Log.i(TAG, "SendTestimony Count:-> " + c.getCount());
//                        Testimony newTestimony = get_Testimony_by_id(id);
//                        if(newTestimony !=null){
//                            newTestimony.setId(ID);
//                            Log.i(TAG, "Found for Testimony Send:-> \n" + newTestimony.toString());
//                            Found.add(newTestimony);
//                        }
//                    }
//                }
//            }
//        }catch (Exception e){
//
//        }
//        return Found;
//    }
//    public ImageSync Get_Top_ImageSync(){
//        String DB_Table = Table_IMAGE_SYNC;
//        ImageSync found = new ImageSync();
//        try{
//            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
//            if(c.getCount()>0){
//                c.moveToPosition(c.getCount()-1);
//                ImageSync dis = new ImageSync();
//                dis.setId(c.getString(c.getColumnIndex(IMAGE_SYNC_COLUMN[0])));
//                dis.setFilePath(c.getString(c.getColumnIndex(IMAGE_SYNC_COLUMN[1])));
//                dis.setParam(c.getString(c.getColumnIndex(IMAGE_SYNC_COLUMN[2])));
//                return dis;
//            }else{
//                return null;
//            }
//        }catch (Exception e){
//
//        }
//        return null;
//    }
//    public ArrayList<Question> get_All_Questions(String Category){
//        String DB_Table = Table_QUESTION_LIST;
//        ArrayList<Question> found = new ArrayList<Question>();
//        try{
//            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), QUESTION_LIST_FIELDS[0] + " = '" + Category + "'", null, null, null, null);
//            c.moveToFirst();
//            for(int i=0;i<c.getCount();i++){
//                c.moveToPosition(i);
//                Question dis = new Question();
//                dis.setId(c.getString(c.getColumnIndex(QUESTION_LIST_COLUMN[0])));
//                dis.setCategory(c.getString(c.getColumnIndex(QUESTION_LIST_COLUMN[1])));
//                dis.setDescription(c.getString(c.getColumnIndex(QUESTION_LIST_COLUMN[2])));
//                dis.setNote(c.getString(c.getColumnIndex(QUESTION_LIST_COLUMN[3])));
//                dis.setMandatory(c.getString(c.getColumnIndex(QUESTION_LIST_COLUMN[4])));
//                found.add(dis);
//            }
//        }catch (Exception e){
//
//        }
//        return found;
//    }
//    public ArrayList<Question> get_All_Questions(){
//        String DB_Table = Table_QUESTION_LIST;
//        ArrayList<Question> found = new ArrayList<Question>();
//        try{
//            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
//            c.moveToFirst();
//            for(int i=0;i<c.getCount();i++){
//                c.moveToPosition(i);
//                Question dis = new Question();
//                dis.setId(c.getString(c.getColumnIndex(QUESTION_LIST_COLUMN[0])));
//                dis.setCategory(c.getString(c.getColumnIndex(QUESTION_LIST_COLUMN[1])));
//                dis.setDescription(c.getString(c.getColumnIndex(QUESTION_LIST_COLUMN[2])));
//                dis.setNote(c.getString(c.getColumnIndex(QUESTION_LIST_COLUMN[3])));
//                dis.setMandatory(c.getString(c.getColumnIndex(QUESTION_LIST_COLUMN[4])));
//                found.add(dis);
//            }
//        }catch (Exception e){
//
//        }
//        return found;
//    }
//    public ArrayList<ReportItem> get_All_Report(){
//        String DB_Table = Table_Report_Forms;
//        ArrayList<ReportItem> found = new ArrayList<ReportItem>();
//        try{
//            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
//            c.moveToFirst();
//            for(int i=0;i<c.getCount();i++){
//                c.moveToPosition(i);
//                ReportItem dis = new ReportItem();
//                dis.setId(c.getString(c.getColumnIndex(REPORT_FORM_COLUMN[0])));
//                dis.setReport_ID(c.getString(c.getColumnIndex(REPORT_FORM_COLUMN[1])));
//                dis.setCategory(c.getString(c.getColumnIndex(REPORT_FORM_COLUMN[2])));
//                dis.setQuestion(c.getString(c.getColumnIndex(REPORT_FORM_COLUMN[3])));
//                found.add(dis);
//            }
//        }catch (Exception e){
//        }
//        return found;
//    }
//    public ReportItem get_Report(String Report_ID){
//        try{
//            String DB_Table = Table_Reports;
//            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
//            c.moveToFirst();
//            for(int i=0;i<c.getCount();i++){
//                c.moveToPosition(i);
//                String rep_id  = c.getString(c.getColumnIndex(REPORT_COLUMN[1]));
//                if(rep_id.equals(Report_ID)){
//                    ReportItem dis = new ReportItem();
//                    dis.setId(c.getString(c.getColumnIndex(REPORT_COLUMN[0])));
//                    dis.setReport_ID(c.getString(c.getColumnIndex(REPORT_COLUMN[1])));
//                    dis.setValue(c.getString(c.getColumnIndex(REPORT_COLUMN[2])));
//                    dis.setQuestion(c.getString(c.getColumnIndex(REPORT_COLUMN[1])));
//                    dis.setCategory(c.getString(c.getColumnIndex(REPORT_COLUMN[1])));
//                    return dis;
//                }
//            }
//        }catch (Exception e){
//
//        }
//        return null;
//    }
//    public ReportItem get_Report_by_id(String ID){
//        try{
//            String DB_Table = Table_Reports;
//            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
//            c.moveToFirst();
//            for(int i=0;i<c.getCount();i++){
//                c.moveToPosition(i);
//                String rep_id  = c.getString(c.getColumnIndex(REPORT_COLUMN[0]));
//                if(rep_id.equals(ID)){
//                    ReportItem dis = new ReportItem();
//                    dis.setId(c.getString(c.getColumnIndex(REPORT_COLUMN[0])));
//                    dis.setReport_ID(c.getString(c.getColumnIndex(REPORT_COLUMN[1])));
//                    dis.setValue(c.getString(c.getColumnIndex(REPORT_COLUMN[2])));
//                    dis.setQuestion(c.getString(c.getColumnIndex(REPORT_COLUMN[1])));
//                    dis.setCategory(c.getString(c.getColumnIndex(REPORT_COLUMN[1])));
//                    return dis;
//                }
//            }
//        }catch (Exception e){
//
//        }
//        return null;
//    }
//    public NewsFeed get_NewsFeed_by_id(String ID){
//        try{
//            String DB_Table = Table_NEWSFEED;
//            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
//            c.moveToFirst();
//            for(int i=0;i<c.getCount();i++){
//                c.moveToPosition(i);
//                String news_id  = c.getString(c.getColumnIndex(NewsFeed_COLUMN[0]));
//                if(news_id.equals(ID)){
//                    NewsFeed news = new NewsFeed();
//                    news.setId(c.getString(c.getColumnIndex(NewsFeed_COLUMN[0])));
//                    news.setNews_ID(c.getString(c.getColumnIndex(NewsFeed_COLUMN[1])));
//                    news.setTitle(c.getString(c.getColumnIndex(NewsFeed_COLUMN[2])));
//                    news.setContent(c.getString(c.getColumnIndex(NewsFeed_COLUMN[3])));
//                    news.setImageURL(c.getString(c.getColumnIndex(NewsFeed_COLUMN[4])));
//                    news.setImagePath(c.getString(c.getColumnIndex(NewsFeed_COLUMN[5])));
//                    news.setPubDate(c.getString(c.getColumnIndex(NewsFeed_COLUMN[6])));
//                    news.setCategory(c.getString(c.getColumnIndex(NewsFeed_COLUMN[7])));
//                    return news;
//                }
//            }
//        }catch (Exception e){
//
//        }
//        return null;
//    }
//    public ArrayList<Logs> getSendLogs(){
//        Log.i(TAG, "SendLogs:\n");
//        ArrayList<Logs> Found = new ArrayList<Logs>();
//        try{
//            Cursor c = myDatabase.query(Table_LOGS, LOGS_COLUMN, null, null, null, null, null);
//            if(c != null && c.getCount()>0){
//                c.moveToFirst();
//                for(int i=0;i<c.getCount();i++){
//                    c.moveToPosition(i);
//                    String str = c.getString(c.getColumnIndex(LOGS_COLUMN[2]));
//                    Log.i(TAG, "Comparing-> \n" + SyncService.Sync_Tasks[0] + " | "+str);
//                    if (SyncService.Sync_Tasks[0].equals(str)){
//                        Log.i(TAG, "SendLogs Count:-> " + c.getCount());
//                        Logs newLogs = new Logs();
//                        newLogs.setId(c.getString(c.getColumnIndex(LOGS_COLUMN[0])));
//                        newLogs.setType(c.getString(c.getColumnIndex(LOGS_COLUMN[1])));
//                        newLogs.setTask(c.getString(c.getColumnIndex(LOGS_COLUMN[2])));
//                        newLogs.setValue(c.getString(c.getColumnIndex(LOGS_COLUMN[3])));
//                        Log.i(TAG, "Found for SendLogs:-> \n" + newLogs.toString());
//                        Found.add(newLogs);
//                    }
//                }
//            }
//        }catch (Exception e){
//
//        }
//        return Found;
//    }
//    public ArrayList<Disciples> getSendDisciples(){
//        Log.i(TAG, "SendDisciples:\n");
//        ArrayList<Disciples> Found = new ArrayList<Disciples>();
//        try{
//            Cursor c = myDatabase.query(Table_LOGS, LOGS_COLUMN, null, null, null, null, null);
//            if(c != null && c.getCount()>0){
//                c.moveToFirst();
//                for(int i=0;i<c.getCount();i++){
//                    c.moveToPosition(i);
//                    String str = c.getString(c.getColumnIndex(LOGS_COLUMN[2]));
//                    String id = c.getString(c.getColumnIndex(LOGS_COLUMN[3]));
//                    String ID = c.getString(c.getColumnIndex(LOGS_COLUMN[0]));
//                    Log.i(TAG, "Comparing-> \n" + SyncService.Sync_Tasks[1] + " | "+str);
//                    if(SyncService.Sync_Tasks[1].equals(str)){
//                        Log.i(TAG, "SendDisciples Count:-> " + c.getCount());
//                        Disciples newDisciples = getDiscipleProfile(id);
//                        if(newDisciples !=null){
//                            newDisciples.setId(ID);
//                            Log.i(TAG, "Found for Send:-> \n" + newDisciples.toString());
//                            Found.add(newDisciples);
//                        }
//                    }
//                }
//            }
//        }catch (Exception e){
//
//        }
//        return Found;
//    }
//    public ArrayList<Schedule> getSendSchedules(){
//        Log.i(TAG, "SendSchedules:\n");
//        ArrayList<Schedule> Found = new ArrayList<Schedule>();
//        try{
//            Cursor c = myDatabase.query(Table_LOGS, LOGS_COLUMN, null, null, null, null, null);
//            if(c != null && c.getCount()>0){
//                c.moveToFirst();
//                for(int i=0;i<c.getCount();i++){
//                    c.moveToPosition(i);
//                    String str = c.getString(c.getColumnIndex(LOGS_COLUMN[2]));
//                    String id = c.getString(c.getColumnIndex(LOGS_COLUMN[3]));
//                    String ID = c.getString(c.getColumnIndex(LOGS_COLUMN[0]));
//                    Log.i(TAG, "SendSchedule Comparing-> \n" + SyncService.Sync_Tasks[4] + " | "+str);
//                    if(SyncService.Sync_Tasks[4].equals(str)){
//                        Log.i(TAG, "SendSchedule Count:-> " + c.getCount());
//                        Schedule newSchedule = getScheduleWithId(id);
//                        if(newSchedule !=null){
//                            newSchedule.setID(ID);
//                            Log.i(TAG, "Found for SendSchedules:-> \n" + newSchedule.toString());
//                            Found.add(newSchedule);
//                        }
//                    }
//                }
//            }
//        }catch (Exception e){
//
//        }
//        return Found;
//    }
//    public ArrayList<Schedule> getUpdateSchedules(){
//        Log.i(TAG, "UpdateDisciples:\n");
//        ArrayList<Schedule> Found = new ArrayList<Schedule>();
//        try{
//            Cursor c = myDatabase.query(Table_LOGS, LOGS_COLUMN, null, null, null, null, null);
//            if(c != null && c.getCount()>0){
//                c.moveToFirst();
//                for(int i=0;i<c.getCount();i++){
//                    c.moveToPosition(i);
//                    String ID = c.getString(c.getColumnIndex(LOGS_COLUMN[0]));
//                    String str = c.getString(c.getColumnIndex(LOGS_COLUMN[2]));
//                    String id = c.getString(c.getColumnIndex(LOGS_COLUMN[3]));
//                    Log.i(TAG, "Comparing-> \n" + SyncService.Sync_Tasks[3] + " | "+str);
//                    if(SyncService.Sync_Tasks[7].equals(str)){
//                        Log.i(TAG, "Update Schedule Count:-> " + c.getCount());
//                        Schedule newschedule = getScheduleWithId(id);
//                        if(newschedule !=null){
//                            newschedule.setID(ID);
//                            Found.add(newschedule);
//                        }
//                    }
//                }
//            }
//        }catch (Exception e){
//
//        }
//        return Found;
//    }
//    public ArrayList<ReportItem> getSendReports(){
//        Log.i(TAG, "SendReports:\n");
//        ArrayList<ReportItem> Found = new ArrayList<ReportItem>();
//        try{
//            Cursor c = myDatabase.query(Table_LOGS, LOGS_COLUMN, null, null, null, null, null);
//            if(c != null && c.getCount()>0){
//                c.moveToFirst();
//                for(int i=0;i<c.getCount();i++){
//                    c.moveToPosition(i);
//                    String str = c.getString(c.getColumnIndex(LOGS_COLUMN[2]));
//                    String id = c.getString(c.getColumnIndex(LOGS_COLUMN[3]));
//                    String ID = c.getString(c.getColumnIndex(LOGS_COLUMN[0]));
//                    Log.i(TAG, "Comparing-> \n" + SyncService.Sync_Tasks[5] + " | "+str);
//                    if(SyncService.Sync_Tasks[5].equals(str)){
//                        Log.i(TAG, "SendDisciples Count:-> " + c.getCount());
//                        ReportItem newReport = get_Report_by_id(id);
//                        if(newReport !=null){
//                            newReport.setId(ID);
//                            Log.i(TAG, "Found for Send:-> \n" + newReport.toString());
//                            Found.add(newReport);
//                        }
//                    }
//                }
//            }
//        }catch (Exception e){
//
//        }
//        return Found;
//    }
//    public ArrayList<Disciples> getUpdateDisciples(){
//        Log.i(TAG, "UpdateDisciples:\n");
//        ArrayList<Disciples> Found = new ArrayList<Disciples>();
//        try{
//            Cursor c = myDatabase.query(Table_LOGS, LOGS_COLUMN, null, null, null, null, null);
//            if(c != null && c.getCount()>0){
//                c.moveToFirst();
//                for(int i=0;i<c.getCount();i++){
//                    c.moveToPosition(i);
//                    String ID = c.getString(c.getColumnIndex(LOGS_COLUMN[0]));
//                    String str = c.getString(c.getColumnIndex(LOGS_COLUMN[2]));
//                    String id = c.getString(c.getColumnIndex(LOGS_COLUMN[3]));
//                    Log.i(TAG, "Comparing-> \n" + SyncService.Sync_Tasks[3] + " | "+str);
//                    if(SyncService.Sync_Tasks[3].equals(str)){
//                        Log.i(TAG, "UpdateDisciples Count:-> " + c.getCount());
//                        Disciples newDisciples = getDiscipleProfile(id);
//                        if(newDisciples !=null){
//                            newDisciples.setId(ID);
//                            Found.add(newDisciples);
//                        }
//                    }
//                }
//            }
//        }catch (Exception e){
//
//        }
//        return Found;
//    }
//    public ArrayList<NewsFeed> getAllNewsFeeds(){
//        ArrayList<NewsFeed> found = new ArrayList<NewsFeed>();
//        String DB_Table = Table_NEWSFEED;
//        try{
//            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
//            if(c != null && c.getCount()>0){
//                c.moveToFirst();
//                for(int i = 0;i < c.getCount();i++){
//                    c.moveToPosition(i);
//                    NewsFeed news = new NewsFeed();
//                    news.setId(c.getString(c.getColumnIndex(NewsFeed_COLUMN[0])));
//                    news.setNews_ID(c.getString(c.getColumnIndex(NewsFeed_COLUMN[1])));
//                    news.setTitle(c.getString(c.getColumnIndex(NewsFeed_COLUMN[2])));
//                    news.setContent(c.getString(c.getColumnIndex(NewsFeed_COLUMN[3])));
//                    news.setImageURL(c.getString(c.getColumnIndex(NewsFeed_COLUMN[4])));
////                    news.setImagePath(c.getString(c.getColumnIndex(NewsFeed_COLUMN[5])));
//                    news.setImagePath(c.getString(c.getColumnIndex(NewsFeed_COLUMN[5])));
//                    news.setPubDate(c.getString(c.getColumnIndex(NewsFeed_COLUMN[6])));
//                    news.setCategory(c.getString(c.getColumnIndex(NewsFeed_COLUMN[7])));
//                    found.add(news);
//                }
//            }
//        }catch (Exception e){
//            return found;
//        }
//        return found;
//    }
//    public Disciples Get_Disciple_By_Phone(String Phone_Num){
//        Disciples found = null;
//        Log.i(TAG, "GetDisciple by Phone: \n");
//        String DB_Table = Table_DISCIPLES;
//        try{
//            Log.i(TAG, "GetDisciple by Phone -> Searching for  \n");
//            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
//            Log.i(TAG, "GetDisciple by Phone -> Searching for "+c.getCount());
//            if (c.getCount() > 0) {
//                c.moveToFirst();
//
//                for(int i=0;i<c.getCount(); i++) {
//                    c.moveToPosition(i);
//                    Log.i(TAG, "GetDisciple by Phone -> Searching for "+c.getPosition());
//                    if(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[3])).equals(Phone_Num)){
//                        Log.i(TAG, "Comparing: \n" + c.getString(c.getColumnIndex(DISCIPLES_COLUMN[3])) +" | "+ Phone_Num);
//                        Disciples dis = new Disciples();
//                        dis.setId(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[0])));
//                        dis.setFull_Name(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[1])));
//                        dis.setEmail(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[2])));
//                        dis.setPhone(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[3])));
//                        dis.setCountry(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[4])));
//                        dis.setBuild_Phase(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[5])));
//                        dis.setGender(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[6])));
//                        dis.setPicture(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[7])));
//                        Log.i(TAG, "Found Disciples:-> "+dis.toString());
//                        return dis;
//                    }
//                }
//            }
//            Log.i(TAG, "GetDisciple by Phone -> Found:  "+found.toString());
//        }catch (Exception e){
//
//        }
//        return found;
//    }
//    public ArrayList<Disciples> getDisciples(){
//        Log.i(TAG, "GetDisciples: \n");
//        String DB_Table = Table_DISCIPLES;
//        ArrayList<Disciples> found = new ArrayList<Disciples>();
//        try{
//            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, DISCIPLES_COLUMN[0] + " DESC");
//            if (c.getCount() > 0) {
//                c.moveToFirst();
//                for(int i=0;i<c.getCount(); i++) {
//
//                    c.moveToPosition(i);
//                    Disciples dis = new Disciples();
//                    dis.setId(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[0])));
//                    dis.setFull_Name(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[1])));
//                    dis.setEmail(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[2])));
//                    dis.setPhone(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[3])));
//                    dis.setCountry(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[4])));
//                    dis.setBuild_Phase(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[5])));
//                    dis.setGender(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[6])));
//                    dis.setPicture(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[7])));
//                    Log.i(TAG, "Found Disciples:-> "+dis.toString());
//                    found.add(dis);
//                }
//            }
//        }catch (Exception e){
//
//        }
//        return found;
//    }
//    public ArrayList<Schedule> get_All_Schedule(){
//        Log.i(TAG, "GetAll Schedule:\n");
//        String DB_Table = Table_SCHEDULES;
//        ArrayList<Schedule> found = new ArrayList<Schedule>();
//        try{
//            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, SCHEDULES_COLUMN[0] + " DESC");
//            if (c.getCount() > 0) {
//                c.moveToFirst();
//                for(int i=0;i<c.getCount();i++){
//                    c.moveToPosition(i);
//                    Schedule dis = new Schedule();
//                    dis.setID(c.getString(c.getColumnIndex(SCHEDULES_COLUMN[0])));
//                    dis.setDisciple_Phone(c.getString(c.getColumnIndex(SCHEDULES_COLUMN[1])));
//                    dis.setTitle(c.getString(c.getColumnIndex(SCHEDULES_COLUMN[2])));
//                    dis.setAlarm_Time(c.getString(c.getColumnIndex(SCHEDULES_COLUMN[3])));
//                    dis.setAlarm_Repeat(c.getString(c.getColumnIndex(SCHEDULES_COLUMN[4])));
//                    dis.setDescription(c.getString(c.getColumnIndex(SCHEDULES_COLUMN[5])));
//                    Log.i(TAG, "Found Schedules:-> "+dis.toString());
//                    found.add(dis);
//                }
//            }
//        }catch (Exception e){
//
//        }
//        return found;
//    }
//    public ArrayList<Schedule> get_Schedule_With_User(String Dis_Phone){
//        String DB_Table = Table_SCHEDULES;
//        ArrayList<Schedule> found = new ArrayList<Schedule>();
//        try{
//            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
//            c.moveToFirst();
//            for (int i = 0; i < c.getCount(); i++){
//                c.moveToPosition(i);
//                Schedule dis = new Schedule();
//                String phone = c.getString(c.getColumnIndex(SCHEDULES_COLUMN[1]));
//                if(Dis_Phone.equals(phone)){
//                    dis.setID(c.getString(c.getColumnIndex(SCHEDULES_COLUMN[0])));
//                    dis.setDisciple_Phone(c.getString(c.getColumnIndex(SCHEDULES_COLUMN[1])));
//                    dis.setTitle(c.getString(c.getColumnIndex(SCHEDULES_COLUMN[2])));
//                    dis.setAlarm_Time(c.getString(c.getColumnIndex(SCHEDULES_COLUMN[3])));
//                    dis.setAlarm_Repeat(c.getString(c.getColumnIndex(SCHEDULES_COLUMN[4])));
//                    dis.setDescription(c.getString(c.getColumnIndex(SCHEDULES_COLUMN[5])));
//                    Log.i(TAG, "Found Schedules:->:" + dis.toString());
//                    found.add(dis);
//                }
//            }
//        }catch (Exception e){
//            return found;
//        }
//        return found;
//    }
//    public Schedule getScheduleWithId(String id){
//        try{
//            Schedule dis = new Schedule();
//            String DB_Table = Table_SCHEDULES;
//            Cursor c = myDatabase.query(DB_Table,getColumns(DB_Table),null,null,null,null,null);
//            for(int i=0;i<c.getCount();i++){
//                c.moveToPosition(i);
//                dis.setID(c.getString(c.getColumnIndex(SCHEDULES_COLUMN[0])));
//                dis.setDisciple_Phone(c.getString(c.getColumnIndex(SCHEDULES_COLUMN[1])));
//                dis.setTitle(c.getString(c.getColumnIndex(SCHEDULES_COLUMN[2])));
//                dis.setAlarm_Time(c.getString(c.getColumnIndex(SCHEDULES_COLUMN[3])));
//                dis.setAlarm_Repeat(c.getString(c.getColumnIndex(SCHEDULES_COLUMN[4])));
//                dis.setDescription(c.getString(c.getColumnIndex(SCHEDULES_COLUMN[5])));
//                if(dis.getID().equals(id)) {
//                    return dis;
//                }
//            }
//            if(c.getCount()>0){
//
//
//            }
//        }catch (Exception e){
//
//        }
//        return null;
//    }
//    public Schedule get_Schedule_by_time(String time){
//        try{
//            String DB_Table = Table_SCHEDULES;
//            Cursor c = myDatabase.query(DB_Table,getColumns(DB_Table),null,null,null,null,null);
//            c.moveToFirst();
//            if(c.getCount()>0){
//                for(int i=0;i<c.getCount();i++){
//                    c.moveToPosition(i);
//                    Schedule dis = new Schedule();
//                    dis.setID(c.getString(c.getColumnIndex(SCHEDULES_COLUMN[0])));
//                    dis.setDisciple_Phone(c.getString(c.getColumnIndex(SCHEDULES_COLUMN[1])));
//                    dis.setTitle(c.getString(c.getColumnIndex(SCHEDULES_COLUMN[2])));
//                    dis.setAlarm_Time(c.getString(c.getColumnIndex(SCHEDULES_COLUMN[3])));
//                    dis.setAlarm_Repeat(c.getString(c.getColumnIndex(SCHEDULES_COLUMN[4])));
//                    dis.setDescription(c.getString(c.getColumnIndex(SCHEDULES_COLUMN[5])));
//                    Log.i(TAG, "Schedule Time Comparing: "+dis.getAlarm_Time()+" | "+time);
//                    if(dis.getAlarm_Time().equals(time)){
//                        return dis;
//                    }
//                }
//
//            }
//        }catch (Exception e){
//
//        }
//        return null;
//    }
//    public ArrayList<QuestionAnswer> get_Answer(String Dis_ID, String phase){
//        String DB_Table = Table_QUESTION_ANSWER;
//        ArrayList<QuestionAnswer> found = new ArrayList<QuestionAnswer>();
//        try{
//            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), QUESTION_ANSWER_FIELDS[0] + " = '" + Dis_ID + "' and " + QUESTION_ANSWER_FIELDS[3] + "= '" + phase + "'", null, null, null, null);
//            Log.i("Deep Life", "Answer from db count: " + c.getCount() + " data: " + c.toString());
//            c.moveToFirst();
//            for(int i=0;i<c.getCount();i++){
//                c.moveToPosition(i);
//                QuestionAnswer dis = new QuestionAnswer();
//                dis.setId(c.getString(c.getColumnIndex(QUESTION_ANSWER_COLUMN[0])));
//                dis.setDisciple_id(c.getString(c.getColumnIndex(QUESTION_ANSWER_COLUMN[1])));
//                dis.setQuestion_id(c.getString(c.getColumnIndex(QUESTION_ANSWER_COLUMN[2])));
//                dis.setAnswer(c.getString(c.getColumnIndex(QUESTION_ANSWER_COLUMN[3])));
//                dis.setBuild_Phase(c.getString(c.getColumnIndex(QUESTION_ANSWER_COLUMN[4])));
//
//                found.add(dis);
//            }
//        }catch (Exception e){
//
//        }
//        return found;
//    }
//    public Disciples getDiscipleProfile(String Dis_ID){
//        try{
//            Log.i(TAG, "GetDiscipleProfile");
//            Disciples dis = new Disciples();
//            String DB_Table = Table_DISCIPLES;
//            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, SCHEDULES_COLUMN[0] + " DESC");
//            if(c != null){
//                c.moveToFirst();
//                for(int i=0; i<c.getCount();i++){
//                    c.moveToPosition(i);
//                    String ID = c.getString(c.getColumnIndex(DISCIPLES_COLUMN[0]));
//                    Log.i(TAG, "GetDiscipleProfile Comparing: "+ID+" | "+Dis_ID);
//                    if(ID.equals(Dis_ID)){
//                        dis.setId(Dis_ID);
//                        dis.setFull_Name(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[1])));
//                        dis.setEmail(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[2])));
//                        dis.setPhone(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[3])));
//                        dis.setCountry(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[4])));
//                        dis.setBuild_Phase(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[5])));
//                        dis.setGender(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[6])));
//                        dis.setPicture(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[7])));
//                        Log.i(TAG, "Found DiscipleProfile:-> " + dis.toString());
//                        return dis;
//                    }
//                }
//            }
//        }catch (Exception e){
//
//        }
//        return null;
//    }
//    public Disciples getDiscipleProfileFromPhone(String Dis_Phone){
//        try{
//            Log.i(TAG, "GetDiscipleProfile");
//            Disciples dis = new Disciples();
//            String DB_Table = Table_DISCIPLES;
//            Cursor c = myDatabase.rawQuery("select * from " + DB_Table + " where " + DISCIPLES_COLUMN[3] + "= '" + Dis_Phone + "'", null);
//            if(c.getCount()>0){
//                c.moveToFirst();
//                dis.setId(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[0])));
//                dis.setFull_Name(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[1])));
//                dis.setEmail(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[2])));
//                dis.setPhone(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[3])));
//                dis.setCountry(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[4])));
//                dis.setBuild_Phase(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[5])));
//                dis.setGender(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[6])));
//                dis.setPicture(c.getString(c.getColumnIndex(DISCIPLES_COLUMN[7])));
//                Log.i(TAG, "Found DiscipleProfile:-> " + dis.toString());
//                return dis;
//            }
//
//        }catch (Exception e){
//
//        }
//        return null;
//    }
//
//
//
//
//    public ArrayList<String> get_all_in_column(String DB_Table, String atColumn){
//        ArrayList<String> found = new ArrayList<String>();
//        try{
//            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
//            c.moveToFirst();
//            for(int i = 0;i < c.getCount();i++){
//                c.moveToPosition(i);
//                String DB_Val = c.getString(c.getColumnIndex(atColumn));
//                if(!found.contains(DB_Val)){
//                    found.add(DB_Val);
//                }
//            }
//        }catch (Exception e){
//
//        }
//        return found;
//    }
//
////    public Testimony get_Testimony_by_id(String ID){
////        try{
////            String DB_Table = Table_TESTIMONY;
////            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
////            c.moveToFirst();
////            for(int i=0;i<c.getCount();i++){
////                c.moveToPosition(i);
////                String rep_id  = c.getString(c.getColumnIndex(TESTIMONY_COLUMN[0]));
////                if(rep_id.equals(ID)){
////                    Testimony dis = new Testimony();
////                    dis.setID(c.getString(c.getColumnIndex(TESTIMONY_COLUMN[0])));
////                    dis.setTitle(c.getString(c.getColumnIndex(TESTIMONY_COLUMN[1])));
//////                    dis.setDetail(c.getString(c.getColumnIndex(TESTIMONY_COLUMN[2])));
////                    return dis;
////                }
////            }
////        }catch (Exception e){
////
////        }
////        return null;
////    }
//    public String get_DiscipleName(String phone){
//        String Name = null;
//        try{
//            String DB_Table = Table_DISCIPLES;
//            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
//            c.moveToFirst();
//            for(int i=0;i<c.getCount();i++){
//                c.moveToPosition(i);
//                String str = c.getString(c.getColumnIndex(DISCIPLES_COLUMN[3]));
//                if(str.equals(phone)){
//                    Name = c.getString(c.getColumnIndex(DISCIPLES_COLUMN[1]));
//                }
//            }
//        }catch (Exception e){
//
//        }
//        return Name;
//    }
//    public int get_LogID(String Task, String Value){
//        Log.i(TAG, "Get LogID:->");
//		int id = 0;
//		try{
//            String DB_Table = Table_LOGS;
//            Cursor c = myDatabase.query(DB_Table, LOGS_COLUMN, null, null, null, null, null);
//            if(c != null){
//                c.moveToFirst();
//                for(int i=0;i<c.getCount();i++){
//                    c.moveToPosition(i);
//                    String task = c.getString(c.getColumnIndex(LOGS_COLUMN[2]));
//                    String value = c.getString(c.getColumnIndex(LOGS_COLUMN[3]));
//                    Log.i(TAG, "Get LogID: " + task + " --- " + value);
//                    if(Task.equals(task) && Value.equals(value)){
//                        String _id = c.getString(c.getColumnIndex(LOGS_COLUMN[0]));
//                        id = Integer.valueOf(_id);
//                        return  id;
//                    }
//                }
//            }
//        }catch (Exception e){
//
//        }
//		return id;
//	}
//
//
//    public boolean isUniqueDisciple(String Country_Code, String Phone){
//        Log.i(TAG, "Checking for duplication: \n");
//        String DB_Table = Table_DISCIPLES;
//        try{
//            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
//            if (c.getCount() > 0) {
//                c.moveToFirst();
//                for(int i=0;i<c.getCount(); i++) {
//                    c.moveToPosition(i);
//                    String country = c.getString(c.getColumnIndex(DISCIPLES_COLUMN[4]));
//                    String phone = c.getString(c.getColumnIndex(DISCIPLES_COLUMN[3]));
//                    if(country.equals(Country_Code) & phone.equals(Phone)) {
//                        return true;
//                    }
//                }
//                return false;
//            }else{
//                return false;
//            }
//        }catch (Exception e){
//            return true;
//        }
//    }
//
//    public int Get_Country_Posistion_By_id(String id){
//        Log.i(TAG, "GetAll Countries:\n");
//        String DB_Table = Table_COUNTRY;
//        try{
//            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
//            if (c.getCount() > 0) {
//                c.moveToFirst();
//                for(int i=0;i<c.getCount();i++){
//                    c.moveToPosition(i);
//                    if(c.getString(c.getColumnIndex(COUNTRY_COLUMN[0])).equals(id)){
//                        return c.getPosition();
//                    }
//                }
//            }
//        }catch (Exception e){
//
//        }
//        return 0;
//    }
//    public ArrayList<Country> get_All_Country(){
//        Log.i(TAG, "GetAll Countries:\n");
//        String DB_Table = Table_COUNTRY;
//        ArrayList<Country> found = new ArrayList<Country>();
//        try{
//            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
//            if (c.getCount() > 0) {
//                c.moveToFirst();
//                for(int i=0;i<c.getCount();i++){
//                    c.moveToPosition(i);
//                    Country dis = new Country();
//                    dis.setId(c.getString(c.getColumnIndex(COUNTRY_COLUMN[0])));
//                    dis.setCountry_id(c.getString(c.getColumnIndex(COUNTRY_COLUMN[1])));
//                    dis.setIso3(c.getString(c.getColumnIndex(COUNTRY_COLUMN[2])));
//                    dis.setName(c.getString(c.getColumnIndex(COUNTRY_COLUMN[3])));
//                    dis.setCode(c.getString(c.getColumnIndex(COUNTRY_COLUMN[4])));
//                    Log.i(TAG, "Found Countries:-> "+dis.toString());
//                    found.add(dis);
//                }
//            }
//        }catch (Exception e){
//
//        }
//        return found;
//    }
//    public Country get_Country_by_CountryID(String CountryID){
//        try{
//            Log.i(TAG, "GetAll Country by CountryID:\n");
//            String DB_Table = Table_COUNTRY;
//            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
//            if (c.getCount() > 0) {
//                c.moveToFirst();
//                for(int i=0;i<c.getCount();i++){
//                    c.moveToPosition(i);
//                    String id = c.getString(c.getColumnIndex(COUNTRY_COLUMN[1]));
//                    if(id.equals(CountryID)){
//                        Country dis = new Country();
//                        dis.setId(c.getString(c.getColumnIndex(COUNTRY_COLUMN[0])));
//                        dis.setCountry_id(c.getString(c.getColumnIndex(COUNTRY_COLUMN[1])));
//                        dis.setIso3(c.getString(c.getColumnIndex(COUNTRY_COLUMN[2])));
//                        dis.setName(c.getString(c.getColumnIndex(COUNTRY_COLUMN[3])));
//                        dis.setCode(c.getString(c.getColumnIndex(COUNTRY_COLUMN[4])));
//                        Log.i(TAG, "Found Country:-> "+dis.toString());
//                        return dis;
//                    }
//                }
//            }
//        }catch (Exception e){
//
//        }
//        return null;
//    }
//    public Country get_Country_by_Country_Code(String CountryCode){
//        try{
//            Log.i(TAG, "GetAll Country by CountryID:\n");
//            String DB_Table = Table_COUNTRY;
//            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
//            if (c.getCount() > 0) {
//                c.moveToFirst();
//                for(int i=0;i<c.getCount();i++){
//                    c.moveToPosition(i);
//                    String id = c.getString(c.getColumnIndex(COUNTRY_COLUMN[4]));
//                    if(id.equals(CountryCode)){
//                        Country dis = new Country();
//                        dis.setId(c.getString(c.getColumnIndex(COUNTRY_COLUMN[0])));
//                        dis.setCountry_id(c.getString(c.getColumnIndex(COUNTRY_COLUMN[1])));
//                        dis.setIso3(c.getString(c.getColumnIndex(COUNTRY_COLUMN[2])));
//                        dis.setName(c.getString(c.getColumnIndex(COUNTRY_COLUMN[3])));
//                        dis.setCode(c.getString(c.getColumnIndex(COUNTRY_COLUMN[4])));
//                        Log.i(TAG, "Found Country:-> "+dis.toString());
//                        return dis;
//                    }
//                }
//            }
//        }catch (Exception e){
//
//        }
//        return null;
//    }
//
//
//    public User getUserProfile(){
//        User dis = new User();
//        try{
//            String DB_Table = Table_USER;
//            Cursor c = myDatabase.rawQuery("select * from " + DB_Table, null);
//            c.moveToFirst();
//            if(c.getCount()>0){
//                dis.setId(c.getString(c.getColumnIndex(USER_COLUMN[0])));
//                dis.setUser_Name(c.getString(c.getColumnIndex(USER_COLUMN[1])));
//                dis.setUser_Email(c.getString(c.getColumnIndex(USER_COLUMN[2])));
//                dis.setUser_Phone(c.getString(c.getColumnIndex(USER_COLUMN[3])));
//                dis.setUser_Pass(c.getString(c.getColumnIndex(USER_COLUMN[4])));
//                dis.setUser_Country(c.getString(c.getColumnIndex(USER_COLUMN[5])));
//                dis.setUser_Picture(c.getString(c.getColumnIndex(USER_COLUMN[6])));
//                dis.setUser_Favorite_Scripture(c.getString(c.getColumnIndex(USER_COLUMN[7])));
//                return dis;
//            }
//        }catch (Exception e){
//
//        }
//        return null;
//    }
//
//
//    public long checkExistence(String Db_Table, String column, String id, String build){
//
//        Cursor cursor = myDatabase.query(Db_Table, getColumns(Db_Table),column+" = '"+id+"' and "+QUESTION_ANSWER_FIELDS[3]+" = '"+ build+"'",null,null,null,null);
//        return cursor.getCount();
//
//    }
//    public int count_Questions(String DB_Table, String Category){
//        Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), QUESTION_LIST_FIELDS[0]+" = '"+Category+"'", null, null, null, null);
//        return c.getCount();
//    }
//
//
//    public User getUser(){
//        User newUser = new User();
//        try{
//            Cursor c = myDatabase.query(Table_USER, USER_COLUMN, null, null, null, null, null);
//            if(c != null && c.getCount() == 1){
//                c.moveToFirst();
//                newUser.setId(c.getString(c.getColumnIndex(USER_COLUMN[0])));
//                newUser.setUser_Name(c.getString(c.getColumnIndex(USER_COLUMN[3])));
//                newUser.setUser_Pass(c.getString(c.getColumnIndex(USER_COLUMN[4])));
//                newUser.setUser_Country(c.getString(c.getColumnIndex(USER_COLUMN[5])));
//            }else{
//                newUser = null;
//            }
//        }catch (Exception e){
//
//        }
//        return newUser;
//    }




    private String[] getColumns(String DB_Table){
        String[] strs = null;
        if(DB_Table == Table_DISCIPLES){
            strs = DISCIPLES_COLUMN;
        }else if(DB_Table == Table_LOGS){
            strs = LOGS_COLUMN;
        }else if(DB_Table == Table_USER){
            strs = USER_COLUMN;
        }else if(DB_Table == Table_SCHEDULES){
            strs = SCHEDULES_COLUMN;
        } else if(DB_Table == Table_QUESTION_LIST){
            strs = QUESTION_LIST_COLUMN;
        } else if(DB_Table == Table_QUESTION_ANSWER){
            strs = QUESTION_ANSWER_COLUMN;
        } else if(DB_Table == Table_Reports){
            strs = REPORT_COLUMN;
        }else if(DB_Table == Table_Report_Forms){
            strs = REPORT_FORM_COLUMN;
        }else if(DB_Table == Table_COUNTRY){
            strs = COUNTRY_COLUMN;
        }else if(DB_Table == Table_NEWSFEED){
            strs = NewsFeed_COLUMN;
        }else if(DB_Table == Table_TESTIMONY){
            strs = TESTIMONY_COLUMN;
        }else if(DB_Table == Table_CATEGORIES){
            strs = CATEGORY_COLUMN;
        }else if(DB_Table == Table_IMAGE_SYNC){
            strs = IMAGE_SYNC_COLUMN;
        }
        return strs;
    }
}
