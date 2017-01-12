
package deeplife.gcme.com.deeplife.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import deeplife.gcme.com.deeplife.Disciples.Disciple;
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
import deeplife.gcme.com.deeplife.WinBuildSend.WbsQuestion;

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
    public static final String Table_LEARNING = "LEARNING_TOOLS";
    public static final String Table_DISCIPLE_TREE = "DISCIPLE_TREE_COUNT";

    public static final String[] DISCIPLES_FIELDS = {"SerID", "FullName", "DisplayName", "Email", "Phone", "Country", "MentorID", "Stage", "ImageURL", "ImagePath", "Role", "Gender", "Created"};
    public static final String[] LOGS_FIELDS = {"Type", "Task", "Value"};
    public static final String[] NewsFeed_FIELDS = {"News_ID", "Title", "Content", "Category", "ImageURL", "ImagePath", "PubDate"};

    public static final String[] COUNTRY_FIELDS = {"serid", "iso3", "name", "code"};
    public static final String[] SCHEDULES_FIELDS = {"Disciple_Phone", "Title", "Alarm_Time", "Alarm_Repeat", "Description"};
    public static final String[] USER_FIELDS = {"SerID", "Full_Name", "Email", "Phone", "Password", "Country", "Picture", "Favorite_Scripture"};
    public static final String[] QUESTION_LIST_FIELDS = {"SerID", "Category", "Question", "Description", "Mandatory", "Type", "Country", "Created"};
    public static final String[] REPORT_FORM_FIELDS = {"Report_ID", "Category", "Questions"};
    public static final String[] REPORT_FIELDS = {"Report_ID", "Value", "Date"};
    public static final String[] QUESTION_ANSWER_FIELDS = {"SerID", "DisciplePhone", "Question_ID", "Answer", "BuildStage"};
    public static final String[] TESTIMONY_FIELDS = {"SerID", "UserID", "Description", "Status", "PubDate", "UserName"};
    public static final String[] IMAGE_SYNC_FIELDS = {"FileName", "Param"};
    public static final String[] CATEGORY_FIELDS = {"SerID", "Name", "Parent", "Status", "Created"};
    public static final String[] LEARNING_FIELDS = {"SerID", "Title", "Description", "VideoURL", "Country", "IsDefault", "Created"};
    public static final String[] DISCIPLE_TREE_FIELDS = {"SerID", "UserID", "Count"};

//    public static final String[] DISCIPLES_COLUMN = {"id", "SerID", "FullName", "DisplayName", "Email", "Phone", "Country", "MentorID", "Stage", "ImageURL", "ImagePath", "Role", "Gender", "Created"};
//    public static final String[] SCHEDULES_COLUMN = {"id", "Disciple_Phone", "Title", "Alarm_Time", "Alarm_Repeat", "Description"};
//    public static final String[] REPORT_FORM_COLUMN = {"id", "Report_ID", "Category", "Questions"};
//    public static final String[] NewsFeed_COLUMN = {"id", "News_ID", "Title", "Content", "Category", "ImageURL", "ImagePath", "PubDate"};
//    public static final String[] REPORT_COLUMN = {"id", "Report_ID", "Value", "Date"};
//    public static final String[] COUNTRY_COLUMN = {"id", "serid", "iso3", "name", "code"};
//    public static final String[] LOGS_COLUMN = {"id", "Type", "Task", "Value"};
//    public static final String[] USER_COLUMN = {"id", "SerID", "Full_Name", "Email", "Phone", "Password", "Country", "Picture", "Favorite_Scripture"};
//    public static final String[] QUESTION_LIST_COLUMN = {"id", "SerID", "Category", "Question", "Description", "Mandatory", "Type", "Country", "Created"};
//    public static final String[] QUESTION_ANSWER_COLUMN = {"id", "SerID", "DisciplePhone", "Question_ID", "Answer", "BuildStage"};
//    public static final String[] TESTIMONY_COLUMN = {"id", "SerID", "UserID", "Description", "Status", "PubDate", "UserName"};
//    public static final String[] IMAGE_SYNC_COLUMN = {"id", "FileName", "Param"};
//    public static final String[] CATEGORY_COLUMN = {"id", "SerID", "Name", "Parent", "Status", "Created"};
//    public static final String[] LEARNING_COLUMN = {"id", "SerID", "Title", "Description", "VideoURL", "Country", "IsDefault", "Created"};
//    public static final String[] DISCIPLE_TREE_COLUMN = {"id", "SerID", "UserID", "Count"};

    public enum DisciplesColumn {
        ID("id"),
        SERID("SerID"),
        FULLNAME("FullName"),
        DISPLAYNAME("DisplayName"),
        EMAIL("Email"),
        PHONE("Phone"),
        COUNTRY("Country"),
        MENTORID("MentorID"),
        STAGE("Stage"),
        IMAGEURL("ImageURL"),
        IMAGEPATH("ImagePath"),
        ROLE("Role"),
        GENDER("Gender"),
        CREATED("Created");

        private final String name;
        private DisciplesColumn(String s) { this.name = s; }
        public boolean equalsName(String otherName) { return (otherName == null) ? false : name.equals(otherName); }
        @Override public String toString() { return this.name; }
    }

    public enum SchedulesColumn {
        ID("id"),
        DISCIPLE_PHONE("Disciple_Phone"),
        TITLE("Title"),
        ALARM_TIME("Alarm_Time"),
        ALARM_REPEAT("Alarm_Repeat"),
        DESCRIPTION("Description");

        private final String name;
        private SchedulesColumn(String s) { this.name = s; }
        public boolean equalsName(String otherName) { return (otherName == null) ? false : name.equals(otherName); }
        @Override public String toString() { return this.name; }
    }

    public enum ReportFormColumn {
        ID("id"),
        REPORT_ID("Report_ID"),
        CATEGORY("Category"),
        QUESTIONS("Questions");

        private final String name;
        private ReportFormColumn(String s) { this.name = s; }
        public boolean equalsName(String otherName) { return (otherName == null) ? false : name.equals(otherName); }
        @Override public String toString() { return this.name; }
    }

    public enum NewsfeedColumn {
        ID("id"),
        NEWS_ID("News_ID"),
        TITLE("Title"),
        CONTENT("Content"),
        CATEGORY("Category"),
        IMAGEURL("ImageURL"),
        IMAGEPATH("ImagePath"),
        PUBDATE("PubDate");

        private final String name;
        private NewsfeedColumn(String s) { this.name = s; }
        public boolean equalsName(String otherName) { return (otherName == null) ? false : name.equals(otherName); }
        @Override public String toString() { return this.name; }
    }

    public enum ReportColumn {
        ID("id"),
        REPORT_ID("Report_ID"),
        VALUE("Value"),
        DATE("Date");

        private final String name;
        private ReportColumn(String s) { this.name = s; }
        public boolean equalsName(String otherName) { return (otherName == null) ? false : name.equals(otherName); }
        @Override public String toString() { return this.name; }
    }

    public enum CountryColumn {
        ID("id"),
        SERID("serid"),
        ISO3("iso3"),
        NAME("name"),
        CODE("code");

        private final String name;
        private CountryColumn(String s) { this.name = s; }
        public boolean equalsName(String otherName) { return (otherName == null) ? false : name.equals(otherName); }
        @Override public String toString() { return this.name; }
    }

    public enum LogsColumn {
        ID("id"),
        TYPE("Type"),
        TASK("Task"),
        VALUE("Value");

        private final String name;
        private LogsColumn(String s) { this.name = s; }
        public boolean equalsName(String otherName) { return (otherName == null) ? false : name.equals(otherName); }
        @Override public String toString() { return this.name; }
    }

    public enum UserColumn {
        ID("id"),
        SERID("SerID"),
        FULL_NAME("Full_Name"),
        EMAIL("Email"),
        PHONE("Phone"),
        PASSWORD("Password"),
        COUNTRY("Country"),
        PICTURE("Picture"),
        FAVORITE_SCRIPTURE("Favorite_Scripture");

        private final String name;
        private UserColumn(String s) { this.name = s; }
        public boolean equalsName(String otherName) { return (otherName == null) ? false : name.equals(otherName); }
        @Override public String toString() { return this.name; }
    }

    public enum QuestionListColumn {
        ID("id"),
        SERID("SerID"),
        CATEGORY("Category"),
        QUESTION("Question"),
        DESCRIPTION("Description"),
        MANDATORY("Mandatory"),
        TYPE("Type"),
        COUNTRY("Country"),
        CREATED("Created");

        private final String name;
        private QuestionListColumn(String s) { this.name = s; }
        public boolean equalsName(String otherName) { return (otherName == null) ? false : name.equals(otherName); }
        @Override public String toString() { return this.name; }
    }

    public enum QuestionAnswerColumn {
        ID("id"),
        SERID("SerID"),
        DISCIPLEPHONE("DisciplePhone"),
        QUESTION_ID("Question_ID"),
        ANSWER("Answer"),
        BUILDSTAGE("BuildStage");

        private final String name;
        private QuestionAnswerColumn(String s) { this.name = s; }
        public boolean equalsName(String otherName) { return (otherName == null) ? false : name.equals(otherName); }
        @Override public String toString() { return this.name; }
    }

    public enum TestimonyColumn {
        ID("id"),
        SERID("SerID"),
        USERID("UserID"),
        DESCRIPTION("Description"),
        STATUS("Status"),
        PUBDATE("PubDate"),
        USERNAME("UserName");

        private final String name;
        private TestimonyColumn(String s) { this.name = s; }
        public boolean equalsName(String otherName) { return (otherName == null) ? false : name.equals(otherName); }
        @Override public String toString() { return this.name; }
    }

    public enum ImageSyncColumn {
        ID("id"),
        FILENAME("FileName"),
        PARAM("Param");

        private final String name;
        private ImageSyncColumn(String s) { this.name = s; }
        public boolean equalsName(String otherName) { return (otherName == null) ? false : name.equals(otherName); }
        @Override public String toString() { return this.name; }
    }

    public enum CategoryColumn {
        ID("id"),
        SERID("SerID"),
        NAME("Name"),
        PARENT("Parent"),
        STATUS("Status"),
        CREATED("Created");

        private final String name;
        private CategoryColumn(String s) { this.name = s; }
        public boolean equalsName(String otherName) { return (otherName == null) ? false : name.equals(otherName); }
        @Override public String toString() { return this.name; }
    }

    public enum LearningColumn {
        ID("id"),
        SERID("SerID"),
        TITLE("Title"),
        DESCRIPTION("Description"),
        VIDEOURL("VideoURL"),
        COUNTRY("Country"),
        ISDEFAULT("IsDefault"),
        CREATED("Created");

        private final String name;
        private LearningColumn(String s) { this.name = s; }
        public boolean equalsName(String otherName) { return (otherName == null) ? false : name.equals(otherName); }
        @Override public String toString() { return this.name; }
    }

    public enum DiscipleTreeColumn {
        ID("id"),
        SERID("SerID"),
        USERID("UserID"),
        COUNT("Count");

        private final String name;
        private DiscipleTreeColumn(String s) { this.name = s; }
        public boolean equalsName(String otherName) { return (otherName == null) ? false : name.equals(otherName); }
        @Override public String toString() { return this.name; }
    }

    private SQLiteDatabase myDatabase;
    private SQL_Helper mySQL;
    private Context myContext;
    public static final String TAG = "Database";

    public Database(Context context) {
        myContext = context;
        mySQL = new SQL_Helper(myContext);
        myDatabase = mySQL.getWritableDatabase();
        mySQL.createTables(Table_DISCIPLES, DISCIPLES_FIELDS);
        mySQL.createTables(Table_LOGS, LOGS_FIELDS);
        mySQL.createTables(Table_USER, USER_FIELDS);
        mySQL.createTables(Table_SCHEDULES, SCHEDULES_FIELDS);
        mySQL.createTables(Table_QUESTION_LIST, QUESTION_LIST_FIELDS);
        mySQL.createTables(Table_QUESTION_ANSWER, QUESTION_ANSWER_FIELDS);
        mySQL.createTables(Table_Reports, REPORT_FIELDS);
        mySQL.createTables(Table_Report_Forms, REPORT_FORM_FIELDS);
        mySQL.createTables(Table_COUNTRY, COUNTRY_FIELDS);
        mySQL.createTables(Table_NEWSFEED, NewsFeed_FIELDS);
        mySQL.createTables(Table_TESTIMONY, TESTIMONY_FIELDS);
        mySQL.createTables(Table_IMAGE_SYNC, IMAGE_SYNC_FIELDS);
        mySQL.createTables(Table_CATEGORIES, CATEGORY_FIELDS);
        mySQL.createTables(Table_LEARNING, LEARNING_FIELDS);
        mySQL.createTables(Table_DISCIPLE_TREE, DISCIPLE_TREE_FIELDS);
    }

    public long insert(String DB_Table, ContentValues cv) {
        long state = myDatabase.insert(DB_Table, null, cv);
        Log.i(TAG, "Inserting->: " + cv.toString());
        return state;
    }

    public long Delete_All(String DB_Table) {
        long state = myDatabase.delete(DB_Table, null, null);
        return state;
    }

    public long remove(String DB_Table, int id) {
        String[] args = {"" + id};
        long val = myDatabase.delete(DB_Table, "id = ?", args);
        return val;
    }

    public long update(String DB_Table, ContentValues cv, int id) {
        Log.i(TAG, "Updating Table: " + DB_Table);
        String[] args = {"" + id};
        long state = myDatabase.update(DB_Table, cv, "id = ?", args);
        Log.i(TAG, "Updating Data: " + cv.toString());
        Log.i(TAG, "Updating Completed: " + state + "\n");
        return state;
    }

    public int count(String DB_Table) {
        Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
        if (c != null) {
            return c.getCount();
        } else {
            return 0;
        }
    }

    public Cursor getAll(String DB_Table) {
        Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
        return c;
    }

    public String get_Value_At_Top(String DB_Table, String column) {
        String str = "";
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            c.moveToFirst();
            str = c.getString(c.getColumnIndex(column));
        } catch (Exception e) {

        }

        return str;
    }

    public String get_Value_At_Bottom(String DB_Table, String column) {
        String str = "";
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            c.moveToLast();
            str = c.getString(c.getColumnIndex(column));
        } catch (Exception e) {

        }
        return str;
    }

    public Cursor get_value_by_ID(String DB_Table, String id) {
        Cursor cur = myDatabase.rawQuery("select * from " + DB_Table + " where id=" + id, null);
        return cur;
    }

    public String get_Name_by_phone(String phone) {
        String name = "";
        try {
            String DB_Table = Table_DISCIPLES;
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), DISCIPLES_FIELDS[2] + " = '" + phone + "'", null, null, null, null);
            c.moveToLast();
            name = c.getString(c.getColumnIndex(DISCIPLES_FIELDS[0]));
        } catch (Exception e) {

        }
        return name;
    }

    public long Delete_By_ID(String DB_Table, int pos) {
        String[] args = {"" + pos};
        long val = myDatabase.delete(DB_Table, "id = ?", args);
        return val;
    }

    public long Delete_By_Column(String DB_Table, String column, String val) {
        String[] args = {val};
        long v = myDatabase.delete(DB_Table, column + " = ?", args);
        return v;
    }

    public int get_Top_ID(String DB_Table) {
        int pos = -1;
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            c.moveToFirst();
            pos = Integer.valueOf(c.getString(c.getColumnIndex("id")));
        } catch (Exception e) {

        }
        return pos;
    }

    public User get_User() {
        String DB_Table = Table_USER;
        User found = new User();
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                c.moveToPosition(i);
                User dis = new User();
                dis.setID(Integer.valueOf(c.getString(c.getColumnIndex(UserColumn.ID.toString()))));
                dis.setSerID(Integer.valueOf(c.getString(c.getColumnIndex(UserColumn.SERID.toString()))));
                dis.setUser_Name(c.getString(c.getColumnIndex(UserColumn.FULL_NAME.toString())));
                dis.setUser_Email(c.getString(c.getColumnIndex(UserColumn.EMAIL.toString())));
                dis.setUser_Phone(c.getString(c.getColumnIndex(UserColumn.PHONE.toString())));
                dis.setUser_Pass(c.getString(c.getColumnIndex(UserColumn.PASSWORD.toString())));
                dis.setUser_Country(c.getString(c.getColumnIndex(UserColumn.COUNTRY.toString())));
                dis.setUser_Picture(c.getString(c.getColumnIndex(UserColumn.PICTURE.toString())));
                dis.setUser_Favorite_Scripture(c.getString(c.getColumnIndex(UserColumn.FAVORITE_SCRIPTURE.toString())));
                return dis;
            }
        } catch (Exception e) {

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
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(NewsfeedColumn.ID.toString())));
                    if (cur_id == id) {
                        News news = new News();
                        news.setId(Integer.valueOf(c.getString(c.getColumnIndex(NewsfeedColumn.ID.toString()))));
                        news.setSerID(Integer.valueOf(c.getString(c.getColumnIndex(NewsfeedColumn.NEWS_ID.toString()))));
                        news.setTitle(c.getString(c.getColumnIndex(NewsfeedColumn.TITLE.toString())));
                        news.setContent(c.getString(c.getColumnIndex(NewsfeedColumn.CONTENT.toString())));
                        news.setCategory(c.getString(c.getColumnIndex(NewsfeedColumn.CATEGORY.toString())));
                        news.setImageURL(c.getString(c.getColumnIndex(NewsfeedColumn.IMAGEURL.toString())));
                        news.setImagePath(c.getString(c.getColumnIndex(NewsfeedColumn.IMAGEPATH.toString())));
                        news.setPubDate(c.getString(c.getColumnIndex(NewsfeedColumn.PUBDATE.toString())));
                        return news;
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed Get News By ID: " + e.toString());
            return null;
        }
        return null;

    }

    public News getNewsBySerID(int id) {
        Log.i(TAG, "Get News By Server ID: " + id);
        String DB_Table = Table_NEWSFEED;
        News found = new News();
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    int ser_id = Integer.valueOf(c.getString(c.getColumnIndex(NewsfeedColumn.NEWS_ID.toString())));
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(NewsfeedColumn.ID.toString())));
                    if (ser_id == id) {
                        News news = getNewsByID(cur_id);
                        return news;
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;

    }

    public ArrayList<News> getAllNews() {
        Log.i(TAG, "Get All News: ");
        String DB_Table = Table_NEWSFEED;
        ArrayList<News> found = new ArrayList<News>();
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                c.moveToPosition(i);
                int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(NewsfeedColumn.ID.toString())));
                News new_news = getNewsByID(cur_id);
                if (new_news != null) {
                    found.add(new_news);
                    Log.i(TAG, "Get All News Populating: " + new_news.getId());
                }
            }
        } catch (Exception e) {
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
        Log.i(TAG, "Get Testimony By ID: " + id);
        String DB_Table = Table_TESTIMONY;
        News found = new News();
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(TestimonyColumn.ID.toString())));
                    if (cur_id == id) {
                        Testimony testimony = new Testimony();
                        testimony.setID(Integer.valueOf(c.getString(c.getColumnIndex(TestimonyColumn.ID.toString()))));
                        testimony.setSerID(Integer.valueOf(c.getString(c.getColumnIndex(TestimonyColumn.SERID.toString()))));
                        testimony.setUser_ID(Integer.valueOf(c.getString(c.getColumnIndex(TestimonyColumn.USERID.toString()))));
                        testimony.setContent(c.getString(c.getColumnIndex(TestimonyColumn.DESCRIPTION.toString())));
                        testimony.setStatus(Integer.valueOf(c.getString(c.getColumnIndex(TestimonyColumn.STATUS.toString()))));
                        testimony.setPubDate(c.getString(c.getColumnIndex(TestimonyColumn.PUBDATE.toString())));
                        testimony.setUserName(c.getString(c.getColumnIndex(TestimonyColumn.USERNAME.toString())));
                        return testimony;
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed Get Testimony By ID: " + e.toString());
            return null;
        }
        return null;

    }

    public Testimony getTestimonyBySerID(int id) {
        Log.i(TAG, "Get Testimony By Server ID: " + id);
        String DB_Table = Table_TESTIMONY;
        Testimony found = new Testimony();
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    int ser_id = Integer.valueOf(c.getString(c.getColumnIndex(TestimonyColumn.SERID.toString())));
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(TestimonyColumn.ID.toString())));
                    if (ser_id == id) {
                        Testimony testimony = getTestimonyByID(cur_id);
                        return testimony;
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed Get Testimony By Server ID: " + e.toString());
            return null;
        }
        return null;
    }

    public ArrayList<Testimony> getAllTestimonies() {
        Log.i(TAG, "Get All Testimonies: ");
        String DB_Table = Table_TESTIMONY;
        ArrayList<Testimony> found = new ArrayList<Testimony>();
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                c.moveToPosition(i);
                int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(TestimonyColumn.ID.toString())));
                Testimony newTestimony = getTestimonyByID(cur_id);
                if (newTestimony != null) {
                    found.add(newTestimony);
                    Log.i(TAG, "Get All Testimonies Populating: " + newTestimony.getID());
                }
            }
        } catch (Exception e) {
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
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(DisciplesColumn.ID.toString())));
                    if (cur_id == id) {
                        Disciple disciple = new Disciple();
                        disciple.setID(Integer.valueOf(c.getString(c.getColumnIndex(DisciplesColumn.ID.toString()))));
                        disciple.setSerID(Integer.valueOf(c.getString(c.getColumnIndex(DisciplesColumn.SERID.toString()))));
                        disciple.setFullName(c.getString(c.getColumnIndex(DisciplesColumn.FULLNAME.toString())));
                        disciple.setDisplayName(c.getString(c.getColumnIndex(DisciplesColumn.DISPLAYNAME.toString())));
                        disciple.setEmail(c.getString(c.getColumnIndex(DisciplesColumn.EMAIL.toString())));
                        disciple.setPhone(c.getString(c.getColumnIndex(DisciplesColumn.PHONE.toString())));
                        disciple.setCountry(c.getString(c.getColumnIndex(DisciplesColumn.COUNTRY.toString())));
                        disciple.setMentorID(Integer.valueOf(c.getString(c.getColumnIndex(DisciplesColumn.MENTORID.toString()))));
                        disciple.setStage(c.getString(c.getColumnIndex(DisciplesColumn.STAGE.toString())));
                        disciple.setImageURL(c.getString(c.getColumnIndex(DisciplesColumn.IMAGEURL.toString())));
                        disciple.setImagePath(c.getString(c.getColumnIndex(DisciplesColumn.IMAGEPATH.toString())));
                        disciple.setRole(c.getString(c.getColumnIndex(DisciplesColumn.ROLE.toString())));
                        disciple.setGender(c.getString(c.getColumnIndex(DisciplesColumn.GENDER.toString())));
                        disciple.setCreated(c.getString(c.getColumnIndex(DisciplesColumn.CREATED.toString())));
                        return disciple;
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed Get Disciple by ID: " + e.toString());
            return null;
        }
        return null;

    }


    public Disciple getDiscipleBySerID(int id) {
        Log.i(TAG, "Get Disciple by Server ID: ");
        String DB_Table = Table_DISCIPLES;
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    int ser_id = Integer.valueOf(c.getString(c.getColumnIndex(DisciplesColumn.SERID.toString())));
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(DisciplesColumn.ID.toString())));
                    if (ser_id == id) {
                        Disciple disciple = getDiscipleByID(cur_id);
                        return disciple;
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed Get Disciple by Server ID: " + e.toString());
            return null;
        }
        return null;
    }

    public Disciple getDiscipleByPhone(String phone) {
        Log.i(TAG, "Get Disciple by Server ID: ");
        String DB_Table = Table_DISCIPLES;
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    String phone_num = c.getString(c.getColumnIndex(DisciplesColumn.PHONE.toString()));
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(DisciplesColumn.ID.toString())));
                    if (phone_num.equals(phone)) {
                        Disciple disciple = getDiscipleByID(cur_id);
                        return disciple;
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed Get Disciple by Server ID: " + e.toString());
            return null;
        }
        return null;
    }

    public ArrayList<Disciple> getAllDisciples() {
        Log.i(TAG, "Get All Disciples: ");
        String DB_Table = Table_DISCIPLES;
        ArrayList<Disciple> found = new ArrayList<Disciple>();
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                c.moveToPosition(i);
                int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(DisciplesColumn.ID.toString())));
                Disciple disciple = getDiscipleByID(cur_id);
                if (disciple != null) {
                    found.add(disciple);
                    Log.i(TAG, "Get All Disciples: " + disciple.getID());
                }
            }
        } catch (Exception e) {
            return null;
        }
        return found;
    }

    public long updateDisciple(Disciple disciple) {
        Log.i(TAG, "UPDATE updateDisciple: ");
        String DB_Table = Table_DISCIPLES;
        try {
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
            if (disciple.getID() > 0) {
//                long x = DeepLife.myDATABASE.update(DB_Table, cv, disciple.getID()); // briggsm: pretty sure we don't need "DeepLife.myDATABASE." here.
                long x = update(DB_Table, cv, disciple.getID());
                return x;
            } else {
//                int id = DeepLife.myDATABASE.getDiscipleByPhone(disciple.getPhone()).getID(); // briggsm: pretty sure we don't need "DeepLife.myDATABASE." here.
//                long x = DeepLife.myDATABASE.update(DB_Table, cv, id);
                int id = getDiscipleByPhone(disciple.getPhone()).getID();
                long x = update(DB_Table, cv, id);
                return x;

            }
        } catch (Exception e) {
            Log.i(TAG, "Failed UPDATE updateDisciple: " + e.toString());
            return 0;
        }
    }
    ////////////////////////////////
    ////////////////////////////////
    ///////  CATEGORY    ///////////
    ////////////////////////////////
    ////////////////////////////////


    public Category getCategoryByID(int id) {
        Log.i(TAG, "Get Category by ID: " + id);
        String DB_Table = Table_CATEGORIES;
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(CategoryColumn.ID.toString())));
                    Log.i(TAG, "--> Get Category by SerID: " + c.getColumnIndex(CategoryColumn.SERID.toString()) + ", " + c.getColumnIndex(CategoryColumn.NAME.toString()));
                    if (cur_id == id) {
                        Category category = new Category();
                        category.setID(Integer.valueOf(c.getString(c.getColumnIndex(CategoryColumn.ID.toString()))));
                        category.setSerID(Integer.valueOf(c.getString(c.getColumnIndex(CategoryColumn.SERID.toString()))));
                        category.setName(c.getString(c.getColumnIndex(CategoryColumn.NAME.toString())));
                        category.setParent(Integer.valueOf(c.getString(c.getColumnIndex(CategoryColumn.PARENT.toString()))));
                        category.setStatus(Integer.valueOf(c.getString(c.getColumnIndex(CategoryColumn.STATUS.toString()))));
                        return category;
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed Get Category by ID: " + e.toString());
            return null;
        }
        return null;

    }

    public Category getCategoryBySerID(int id) {
        Log.i(TAG, "Get Category by Server ID: ");
        String DB_Table = Table_CATEGORIES;
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    int ser_id = Integer.valueOf(c.getString(c.getColumnIndex(CategoryColumn.SERID.toString())));
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(CategoryColumn.ID.toString())));
                    if (ser_id == id) {
                        Category category = getCategoryByID(cur_id);
                        return category;
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed Get Category by Server ID: " + e.toString());
            return null;
        }
        return null;
    }

    public Category getCategoryByParentID(int id) {
        Log.i(TAG, "Get Category by Server ID: ");
        String DB_Table = Table_CATEGORIES;
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    int par_id = Integer.valueOf(c.getString(c.getColumnIndex(CategoryColumn.PARENT.toString())));
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(CategoryColumn.ID.toString())));
                    if (par_id == id) {
                        Category category = getCategoryByID(cur_id);
                        return category;
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed Get Category by Server ID: " + e.toString());
            return null;
        }
        return null;
    }

    public List<Category> getCategoriesByParentID(int id) {
        Log.i(TAG, "Get Category by Server ID: ");
        String DB_Table = Table_CATEGORIES;
        List<Category> found = new ArrayList<Category>();
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    int par_id = Integer.valueOf(c.getString(c.getColumnIndex(CategoryColumn.PARENT.toString())));
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(CategoryColumn.ID.toString())));
                    if (par_id == id) {
                        Category category = getCategoryByID(cur_id);
                        if (category != null) {
                            found.add(category);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed Get Category by Server ID: " + e.toString());
            return found;
        }
        return found;
    }

    public ArrayList<Category> getParentCategory() {
        Log.i(TAG, "Get All Parent Category: ");
        String DB_Table = Table_CATEGORIES;
        ArrayList<Category> found = new ArrayList<Category>();
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                c.moveToPosition(i);
                int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(CategoryColumn.ID.toString())));
                Category category = getCategoryByID(cur_id);
                if (category != null && category.getID() == 0) {
                    found.add(category);
                    Log.i(TAG, "Get All Parent Category: " + category.getID());
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed Get All Parent Category: " + e.toString());
            return null;
        }
        return found;
    }


    ////////////////////////////////
    ////////////////////////////////
    //  WIN BUILD SEND QUESTION  //
    ////////////////////////////////
    ////////////////////////////////


    public WbsQuestion getWinBuildSendQuestionByID(int id) {
        Log.i(TAG, "Get WinBuildQuestion by ID: ");
        String DB_Table = Table_QUESTION_LIST;
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(QuestionListColumn.ID.toString())));
                    if (cur_id == id) {
                        WbsQuestion wbsQuestion = new WbsQuestion();
                        wbsQuestion.setID(Integer.valueOf(c.getString(c.getColumnIndex(QuestionListColumn.ID.toString()))));
                        wbsQuestion.setSerID(Integer.valueOf(c.getString(c.getColumnIndex(QuestionListColumn.SERID.toString()))));
                        wbsQuestion.setCategory(Integer.valueOf(c.getString(c.getColumnIndex(QuestionListColumn.CATEGORY.toString()))));
                        wbsQuestion.setQuestion(c.getString(c.getColumnIndex(QuestionListColumn.QUESTION.toString())));
                        wbsQuestion.setDescription(c.getString(c.getColumnIndex(QuestionListColumn.DESCRIPTION.toString())));
                        wbsQuestion.setMandatory(Integer.valueOf(c.getString(c.getColumnIndex(QuestionListColumn.MANDATORY.toString()))));
                        wbsQuestion.setType(c.getString(c.getColumnIndex(QuestionListColumn.TYPE.toString())).equalsIgnoreCase("YESNO") ? WbsQuestion.Type.YESNO : WbsQuestion.Type.NUMBER);
                        wbsQuestion.setCountry(Integer.valueOf(c.getString(c.getColumnIndex(QuestionListColumn.COUNTRY.toString()))));
                        wbsQuestion.setCreated(c.getString(c.getColumnIndex(QuestionListColumn.CREATED.toString())));
                        return wbsQuestion;
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed Get WinBuildQuestion by ID: " + e.toString());
            return null;
        }
        return null;

    }

    public WbsQuestion getWinBuildSendQuestionBySerID(int id) {
        Log.i(TAG, "Get WinBuildQuestion by Server ID: ");
        String DB_Table = Table_QUESTION_LIST;
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    int ser_id = Integer.valueOf(c.getString(c.getColumnIndex(QuestionListColumn.SERID.toString())));
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(QuestionListColumn.ID.toString())));
                    if (ser_id == id) {
                        WbsQuestion wbsQuestion = getWinBuildSendQuestionByID(cur_id);
                        return wbsQuestion;
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed Get WinBuildQuestion by Server ID: " + e.toString());
            return null;
        }
        return null;
    }

    public ArrayList<WbsQuestion> getWinBuildSendQuestionsByCategory(int categoryID) {
        Log.i(TAG, "Get All WinBuildQuestion by Category id: " + categoryID);
        String DB_Table = Table_QUESTION_LIST;
        ArrayList<WbsQuestion> found = new ArrayList<WbsQuestion>();
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                c.moveToPosition(i);
                int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(QuestionListColumn.ID.toString())));
                int cat_id = Integer.valueOf(c.getString(c.getColumnIndex(QuestionListColumn.CATEGORY.toString())));
                if (cat_id == categoryID) {
                    WbsQuestion wbsQuestion = getWinBuildSendQuestionByID(cur_id);
                    if (wbsQuestion != null) {
                        found.add(wbsQuestion);
                        Log.i(TAG, "Get All WinBuildQuestion: " + wbsQuestion.getID());
                    }

                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed Get getWinBuildSendQuestionsByCategory: " + e.toString());
            return null;
        }
        return found;
    }

    public ArrayList<WbsQuestion> getWinBuildSendQuestionsByCategorySerID(int SerID) {
        Log.i(TAG, "Get All WinBuildQuestion by Category Parent ID: " + SerID);
        ArrayList<WbsQuestion> found = new ArrayList<WbsQuestion>();
        if (SerID > 0) {
            found = getWinBuildSendQuestionsByCategory(SerID);
            Log.i(TAG, "Get All WinBuildQuestion: " + SerID);
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
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(QuestionAnswerColumn.ID.toString())));
                    if (cur_id == id) {
                        Answer answer = new Answer();
                        answer.setID(Integer.valueOf(c.getString(c.getColumnIndex(QuestionAnswerColumn.ID.toString()))));
                        answer.setSerID(Integer.valueOf(c.getString(c.getColumnIndex(QuestionAnswerColumn.SERID.toString()))));
                        answer.setDisciplePhone(c.getString(c.getColumnIndex(QuestionAnswerColumn.DISCIPLEPHONE.toString())));
                        answer.setQuestionID(Integer.valueOf(c.getString(c.getColumnIndex(QuestionAnswerColumn.QUESTION_ID.toString()))));
                        answer.setAnswer(c.getString(c.getColumnIndex(QuestionAnswerColumn.ANSWER.toString())));
                        answer.setBuildStage(c.getString(c.getColumnIndex(QuestionAnswerColumn.BUILDSTAGE.toString())));
                        return answer;
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed Get WinBuildQuestion by ID: " + e.toString());
            return null;
        }
        return null;
    }

    public Answer getAnswerBySerID(int id) {
        // briggsm:  This function seems to be implemented wrong - it's not even looking at "SerID".
        Log.i(TAG, "Get getAnswerBySerID by ServerID: ");
        String DB_Table = Table_QUESTION_ANSWER;
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    int qst_id = Integer.valueOf(c.getString(c.getColumnIndex(QuestionAnswerColumn.QUESTION_ID.toString())));
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(QuestionAnswerColumn.ID.toString())));
                    if (qst_id == id) {
                        Answer answer = getAnswerByID(cur_id);
                        return answer;
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed Get getAnswerByQuestionID by QuestionID: " + e.toString());
            return null;
        }
        return null;
    }

    public Answer getAnswerByQuestionID(int id) {
        Log.i(TAG, "Get getAnswerByQuestionID by QuestionID: ");
        String DB_Table = Table_QUESTION_ANSWER;
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    int qst_id = Integer.valueOf(c.getString(c.getColumnIndex(QuestionAnswerColumn.QUESTION_ID.toString())));
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(QuestionAnswerColumn.ID.toString())));
                    if (qst_id == id) {
                        Answer answer = getAnswerByID(cur_id);
                        return answer;
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed Get getAnswerByQuestionID by QuestionID: " + e.toString());
            return null;
        }
        return null;
    }

    public Answer getAnswerByQuestionIDandDisciplePhone(int questionID, String disciplePhone) {
        Log.i(TAG, "Get getAnswerByQuestionIDandDisciplePhone by QuestionPhone and DisciplePhone: ");
        String DB_Table = Table_QUESTION_ANSWER;
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    int qst_id = Integer.valueOf(c.getString(c.getColumnIndex(QuestionAnswerColumn.QUESTION_ID.toString())));
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(QuestionAnswerColumn.ID.toString())));
                    String dis_ph = c.getString(c.getColumnIndex(QuestionAnswerColumn.DISCIPLEPHONE.toString()));
                    if (qst_id == questionID && disciplePhone.equals(dis_ph)) {
                        Answer answer = getAnswerByID(cur_id);
                        return answer;
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed Get getAnswerByQuestionIDandDisciplePhone by QuestionPhone and DisciplePhone: " + e.toString());
            return null;
        }
        return null;
    }

    public long updateAnswer(Answer answer) {
        Log.i(TAG, "UPDATE updateAnswer: ");
        String DB_Table = Table_QUESTION_ANSWER;
        try {
            ContentValues cv = new ContentValues();
            cv.put(Database.QUESTION_ANSWER_FIELDS[0], answer.getSerID());
            cv.put(Database.QUESTION_ANSWER_FIELDS[1], answer.getDisciplePhone());
            cv.put(Database.QUESTION_ANSWER_FIELDS[2], answer.getQuestionID());
            cv.put(Database.QUESTION_ANSWER_FIELDS[3], answer.getAnswer());
            cv.put(Database.QUESTION_ANSWER_FIELDS[4], answer.getBuildStage());
//            int id = DeepLife.myDATABASE.getAnswerByQuestionIDandDisciplePhone(answer.getQuestionID(), answer.getDisciplePhone()).getID(); // briggsm: pretty sure we don't need "DeepLife.myDATABASE." here.
//            long x = DeepLife.myDATABASE.update(DB_Table, cv, id);
            int id = getAnswerByQuestionIDandDisciplePhone(answer.getQuestionID(), answer.getDisciplePhone()).getID();
            long x = update(DB_Table, cv, id);

            return x;

        } catch (Exception e) {
            Log.i(TAG, "Failed UPDATE updateAnswer: " + e.toString());
            return 0;
        }
    }

    public long add_updateAnswer(Answer answer) {
        Log.i(TAG, "add_updateAnswer: ");
        Answer oldAnswer1 = getAnswerByQuestionIDandDisciplePhone(answer.getQuestionID(), answer.getDisciplePhone());
        if (oldAnswer1 == null) {
            long res = addAnswer(answer);
            return res;
        } else {
            long res = updateAnswer(answer);
            return res;
        }
    }

    public long addAnswer(Answer answer) {
        Log.i(TAG, "Add addAnswer: ");
        String DB_Table = Table_QUESTION_ANSWER;
        try {
            ContentValues cv = new ContentValues();
            cv.put(Database.QUESTION_ANSWER_FIELDS[0], answer.getSerID());
            cv.put(Database.QUESTION_ANSWER_FIELDS[1], answer.getDisciplePhone());
            cv.put(Database.QUESTION_ANSWER_FIELDS[2], answer.getQuestionID());
            cv.put(Database.QUESTION_ANSWER_FIELDS[3], answer.getAnswer());
            cv.put(Database.QUESTION_ANSWER_FIELDS[4], answer.getBuildStage());
            Answer oldAnswer1 = getAnswerBySerID(answer.getSerID());
            if (oldAnswer1 == null) {
//                long x = DeepLife.myDATABASE.insert(DB_Table, cv);  // briggsm: pretty sure we don't need "DeepLife.myDATABASE." here.
                long x = insert(DB_Table, cv);
                return x;
            }
            return 0;
        } catch (Exception e) {
            Log.i(TAG, "Failed Add addAnswer: " + e.toString());
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
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(CountryColumn.ID.toString())));
                    if (cur_id == id) {
                        Country country = new Country();
                        country.setID(Integer.valueOf(c.getString(c.getColumnIndex(CountryColumn.ID.toString()))));
                        country.setSerID(Integer.valueOf(c.getString(c.getColumnIndex(CountryColumn.SERID.toString()))));
                        country.setISO3(c.getString(c.getColumnIndex(CountryColumn.ISO3.toString())));
                        country.setName(c.getString(c.getColumnIndex(CountryColumn.NAME.toString())));
                        country.setCode(Integer.valueOf(c.getString(c.getColumnIndex(CountryColumn.CODE.toString()))));
                        return country;
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed Get Country By ID: " + e.toString());
            return null;
        }
        return null;

    }

    public Country getCountryBySerID(int id) {
        Log.i(TAG, "Get getCountryBySerID by ServerID: ");
        String DB_Table = Table_COUNTRY;
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    int ser_id = Integer.valueOf(c.getString(c.getColumnIndex(CountryColumn.SERID.toString())));
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(CountryColumn.ID.toString())));
                    if (ser_id == id) {
                        Country country = getCountryByID(cur_id);
                        return country;
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed Get getCountryBySerID by ServerID: " + e.toString());
            return null;
        }
        return null;
    }

    public ArrayList<Country> getAllCountries() {
        Log.i(TAG, "Get All getAllCountries: ");
        String DB_Table = Table_COUNTRY;
        ArrayList<Country> found = new ArrayList<Country>();
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                c.moveToPosition(i);
                int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(CountryColumn.ID.toString())));
                Country country = getCountryByID(cur_id);
                if (country != null) {
                    found.add(country);
                    Log.i(TAG, "Get All Countries: " + country.getID());
                }
            }
        } catch (Exception e) {
            return null;
        }
        return found;
    }

    public ArrayList<Logs> getSendLogs() {
        Log.i(TAG, "SendLogs:\n");
        String DB_Table = Table_LOGS;
        ArrayList<Logs> Found = new ArrayList<>();

        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    String Log_Task = c.getString(c.getColumnIndex(LogsColumn.TASK.toString()));
                    Log.i(TAG, "Comparing-> \n" + Logs.Task.SEND_LOG + " | " + Log_Task);
                    if (Logs.Task.SEND_LOG.equalsName(Log_Task)) {
                        Log.i(TAG, "SendLogs Count:-> " + c.getCount());  // briggsm: think this is wrong. 'c' might have 10 rows, but only 2 of them SendLogs. This would return 10. Right?
                        Logs newLogs = new Logs();
                        newLogs.setID(Integer.valueOf(c.getString(c.getColumnIndex(LogsColumn.ID.toString()))));
//                        newLogs.setType(c.getString(c.getColumnIndex(LogsColumn.TYPE.toString())));
//                        newLogs.setTask(c.getString(c.getColumnIndex(LogsColumn.TASK.toString())));
                        newLogs.setType(Logs.Type.fromString(c.getString(c.getColumnIndex(LogsColumn.TYPE.toString()))));
                        newLogs.setTask(Logs.Task.fromString(c.getString(c.getColumnIndex(LogsColumn.TASK.toString()))));

                        newLogs.setValue(c.getString(c.getColumnIndex(LogsColumn.VALUE.toString())));
                        Log.i(TAG, "Found for SendLogs:-> \n" + newLogs.toString());
                        Found.add(newLogs);
                    }
                }
                return Found;
            }
        } catch (Exception e) {
            Log.i(TAG, "Faild getSendLogs:\n" + e.toString());
            return null;
        }
        return Found;


        /*
        // briggsm: !!! Add a loop here, right? !!! Probably, shouldn't do it this way actually. Need to return ALL logs at once.
        Cursor c = getLogsCursorAtCorrectPosition(SyncService.Task.SEND_LOG);
        if (c != null) {
            Log.i(TAG, "getSendLogs: Found at least 1 SEND_LOG");
            Logs newLogs = new Logs();
            newLogs.setID(Integer.valueOf(c.getString(c.getColumnIndex(LogsColumn.ID.toString()))));
            newLogs.setType(c.getString(c.getColumnIndex(LogsColumn.TYPE.toString())));
            newLogs.setTask(c.getString(c.getColumnIndex(LogsColumn.TASK.toString())));
            newLogs.setValue(c.getString(c.getColumnIndex(LogsColumn.VALUE.toString())));
            Log.i(TAG, "Found for SendLogs:-> \n" + newLogs.toString());
            Found.add(newLogs);
        }
        return Found;
        */
    }

    // private ok???
    //public

    /*
    // private ok???
    public Cursor getLogsCursorAtCorrectPosition(SyncService.Task logTask) {
        // Return null if Log doesn't exist,
        // Otherwise return a cursor object which already points at correct row.
        // Note: If there more than one matching row, then only the first one is returned
        //      so, this function should probably be called by a loop.
        Log.i(TAG, "getLogsCursorAtCorrectPosition: ");
        String DB_Table = Table_LOGS;

        //ArrayList<Logs> Found = new ArrayList<Logs>();
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    String Log_Task = c.getString(c.getColumnIndex(LogsColumn.TASK.toString()));
                    Log.i(TAG, "Comparing-> \n" + logTask + " | " + Log_Task);
                    if (logTask.equalsName(Log_Task)) {
                        return c;
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed getLogsCursorAtCorrecPosition: " + e.toString());
            return null;
        }
        return null;
    }
    */

    public ImageSync getTopImageSync() {
        String DB_Table = Table_IMAGE_SYNC;
        ImageSync found = new ImageSync();
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToPosition(c.getCount() - 1);
                ImageSync dis = new ImageSync();
                dis.setId(c.getString(c.getColumnIndex(ImageSyncColumn.ID.toString())));
                dis.setFilePath(c.getString(c.getColumnIndex(ImageSyncColumn.FILENAME.toString())));
                dis.setParam(c.getString(c.getColumnIndex(ImageSyncColumn.PARAM.toString())));
                return dis;
            } else {
                return null;
            }
        } catch (Exception e) {

        }
        return null;
    }

    public ArrayList<Disciple> getSendDisciples() {
        Log.i(TAG, "getSendDisciples:\n");
        String DB_Table = Table_LOGS;
        ArrayList<Disciple> Found = new ArrayList<Disciple>();
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    String Log_Task = c.getString(c.getColumnIndex(LogsColumn.TASK.toString()));
                    String Log_Value = c.getString(c.getColumnIndex(LogsColumn.VALUE.toString()));
                    int Log_ID = Integer.valueOf(c.getString(c.getColumnIndex(LogsColumn.ID.toString())));
                    Log.i(TAG, "Comparing-> \n" + Logs.Task.SEND_DISCIPLES + " | " + Log_Task);
                    if (Logs.Task.SEND_DISCIPLES.equalsName(Log_Task)) {
                        Log.i(TAG, "SendDisciples Count:-> " + c.getCount());
                        Disciple sendDisciple = getDiscipleByPhone(Log_Value);
                        if (sendDisciple != null) {
                            sendDisciple.setID(Log_ID);
                            Log.i(TAG, "Found for Send:-> \n" + sendDisciple.toString());
                            Found.add(sendDisciple);
                        }
                    }
                }
                return Found;
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed getSendDisciples:\n" + e.toString());
        }
        return Found;
    }

    public ArrayList<Disciple> getUpdateDisciples() {
        Log.i(TAG, "getUpdateDisciples:\n");
        String DB_Table = Table_LOGS;
        ArrayList<Disciple> Found = new ArrayList<Disciple>();
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    int Log_ID = Integer.valueOf(c.getString(c.getColumnIndex(LogsColumn.ID.toString())));
                    String Log_Task = c.getString(c.getColumnIndex(LogsColumn.TASK.toString()));
                    String Task_Value = c.getString(c.getColumnIndex(LogsColumn.VALUE.toString()));
                    Log.i(TAG, "Comparing-> \n" + Logs.Task.UPDATE_DISCIPLE + " | " + Log_Task);
                    if (Logs.Task.UPDATE_DISCIPLE.equalsName(Log_Task)) {
                        Log.i(TAG, "UpdateDisciples Count:-> " + c.getCount());
                        Disciple updateDisciple = getDiscipleByPhone(Task_Value);
                        if (updateDisciple != null) {
                            updateDisciple.setID(Log_ID);
                            Found.add(updateDisciple);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed getUpdateDisciples:\n" + e.toString());
        }
        return Found;
    }

    public ArrayList<Testimony> getSendTestimony() {
        Log.i(TAG, "getSendTestimony:\n");
        String DB_Table = Table_LOGS;
        ArrayList<Testimony> Found = new ArrayList<Testimony>();
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    int Log_ID = Integer.valueOf(c.getString(c.getColumnIndex(LogsColumn.ID.toString())));
                    String Log_Task = c.getString(c.getColumnIndex(LogsColumn.TASK.toString()));
                    int Task_Value = Integer.valueOf(c.getString(c.getColumnIndex(LogsColumn.VALUE.toString())));

                    Log.i(TAG, "Comparing-> \n" + Logs.Task.SEND_TESTIMONY + " | " + Log_Task);
                    if (Logs.Task.SEND_TESTIMONY.equals(Log_Task)) {
                        Log.i(TAG, "SendTestimony Count:-> " + c.getCount());
                        Testimony newTestimony = getTestimonyByID(Task_Value);
                        if (newTestimony != null) {
                            newTestimony.setID(Log_ID);
                            Log.i(TAG, "Found for Testimony Send:-> \n" + newTestimony.toString());
                            Found.add(newTestimony);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed getSendTestimony:\n" + e.toString());
        }
        return Found;
    }

    public ArrayList<Answer> getSendAnswers() {
        Log.i(TAG, "getSendAnswers:\n");
        String DB_Table = Table_LOGS;
        ArrayList<Answer> Found = new ArrayList<Answer>();
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    String Log_Task = c.getString(c.getColumnIndex(LogsColumn.TASK.toString()));
                    int Task_Value = Integer.valueOf(c.getString(c.getColumnIndex(LogsColumn.VALUE.toString())));
                    int Log_ID = Integer.valueOf(c.getString(c.getColumnIndex(LogsColumn.ID.toString())));
                    Log.i(TAG, "Comparing-> \n" + Logs.Task.SEND_TESTIMONY + " | " + Log_Task);
                    if (Logs.Task.SEND_TESTIMONY.equalsName(Log_Task)) {
                        Log.i(TAG, "SendAnswer Count:-> " + c.getCount());
                        Answer sendAnswer = getAnswerByID(Task_Value);
                        if (sendAnswer != null) {
                            sendAnswer.setID(Log_ID);
                            Log.i(TAG, "Found for SendAnswer Send:-> \n" + sendAnswer.toString());
                            Found.add(sendAnswer);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed getSendAnswers:\n" + e.toString());
        }
        return Found;
    }


    public ArrayList<Schedule> getSendSchedules() {
        Log.i(TAG, "SendSchedules:\n");
        String DB_Table = Table_LOGS;
        ArrayList<Schedule> Found = new ArrayList<Schedule>();
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    String str = c.getString(c.getColumnIndex(LogsColumn.TASK.toString()));
                    String id = c.getString(c.getColumnIndex(LogsColumn.VALUE.toString()));
                    String ID = c.getString(c.getColumnIndex(LogsColumn.TYPE.toString()));
                    Log.i(TAG, "SendSchedule Comparing-> \n" + Logs.Task.SEND_SCHEDULE + " | " + str);
                    if (Logs.Task.SEND_SCHEDULE.equalsName(str)) {
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
        } catch (Exception e) {

        }
        return Found;
    }

    public ArrayList<Schedule> getUpdateSchedules() {
        Log.i(TAG, "UpdateDisciples:\n");
        String DB_Table = Table_LOGS;
        ArrayList<Schedule> Found = new ArrayList<Schedule>();
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    String ID = c.getString(c.getColumnIndex(LogsColumn.ID.toString()));
                    String str = c.getString(c.getColumnIndex(LogsColumn.TASK.toString()));
                    String id = c.getString(c.getColumnIndex(LogsColumn.VALUE.toString()));
                    Log.i(TAG, "Comparing-> \n" + Logs.Task.UPDATE_SCHEDULES + " | " + str);
                    if (Logs.Task.UPDATE_SCHEDULES.equalsName(str)) {
//                        Log.i(TAG, "Update Schedule Count:-> " + c.getCount());
//                        Schedule newschedule = getScheduleWithId(id);
//                        if(newschedule !=null){
//                            newschedule.setID(ID);
//                            Found.add(newschedule);
//                        }
                    }
                }
            }
        } catch (Exception e) {

        }
        return Found;
    }

    public ArrayList<ReportItem> getSendReports() {
        Log.i(TAG, "SendReports:\n");
        String DB_Table = Table_LOGS;
        ArrayList<ReportItem> Found = new ArrayList<ReportItem>();
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    String str = c.getString(c.getColumnIndex(LogsColumn.TASK.toString()));
                    String id = c.getString(c.getColumnIndex(LogsColumn.VALUE.toString()));
                    String ID = c.getString(c.getColumnIndex(LogsColumn.ID.toString()));
                    Log.i(TAG, "Comparing-> \n" + Logs.Task.SEND_REPORT + " | " + str);
                    if (Logs.Task.SEND_REPORT.equalsName(str)) {
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
        } catch (Exception e) {

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
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    User user = new User();
                    user.setID(Integer.valueOf(c.getString(c.getColumnIndex(UserColumn.ID.toString()))));
                    user.setSerID(Integer.valueOf(c.getString(c.getColumnIndex(UserColumn.SERID.toString()))));
                    user.setFull_Name(c.getString(c.getColumnIndex(UserColumn.FULL_NAME.toString())));
                    user.setUser_Email(c.getString(c.getColumnIndex(UserColumn.EMAIL.toString())));
                    user.setUser_Phone(c.getString(c.getColumnIndex(UserColumn.PHONE.toString())));
                    user.setUser_Pass(c.getString(c.getColumnIndex(UserColumn.PASSWORD.toString())));
                    user.setUser_Country(c.getString(c.getColumnIndex(UserColumn.COUNTRY.toString())));
                    user.setUser_Picture(c.getString(c.getColumnIndex(UserColumn.PICTURE.toString())));
                    user.setUser_Favorite_Scripture(c.getString(c.getColumnIndex(UserColumn.FAVORITE_SCRIPTURE.toString())));
                    return user;
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed Get MainUser By ID: " + e.toString());
            return null;
        }
        return null;

    }

    public long updateMainUser(User mainUser) {
        Log.i(TAG, "UPDATE updateDisciple: ");
        String DB_Table = Table_USER;
        try {
            ContentValues cv = new ContentValues();
            cv.put(Database.USER_FIELDS[0], mainUser.getSerID());
            cv.put(Database.USER_FIELDS[1], mainUser.getFull_Name());
            cv.put(Database.USER_FIELDS[2], mainUser.getUser_Email());
            cv.put(Database.USER_FIELDS[3], mainUser.getUser_Phone());
            cv.put(Database.USER_FIELDS[4], mainUser.getUser_Pass());
            cv.put(Database.USER_FIELDS[5], mainUser.getUser_Country());
            cv.put(Database.USER_FIELDS[6], mainUser.getUser_Picture());
            cv.put(Database.USER_FIELDS[7], mainUser.getUser_Favorite_Scripture());
//            DeepLife.myDATABASE.Delete_All(DB_Table); // briggsm: pretty sure we don't need "DeepLife.myDATABASE." here.
//            long x = DeepLife.myDATABASE.insert(DB_Table, cv);
            Delete_All(DB_Table);
            long x = insert(DB_Table, cv);
            return x;
        } catch (Exception e) {
            Log.i(TAG, "Failed UPDATE updateMainUser: " + e.toString());
            return 0;
        }
    }


    /////////////////////////////////
    /////////////////////////////////
    ////////  Learning Tools   /////////
    /////////////////////////////////
    /////////////////////////////////

    public LearningTool getLearningToolByID(int id) {
        Log.i(TAG, "Get getLearningToolByID: ");
        String DB_Table = Table_LEARNING;
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    // briggsm: think we're missing a check for id!
                    c.moveToPosition(i);
                    LearningTool learningTool = new LearningTool();
                    learningTool.setID(Integer.valueOf(c.getString(c.getColumnIndex(LearningColumn.ID.toString()))));
                    learningTool.setSerID(Integer.valueOf(c.getString(c.getColumnIndex(LearningColumn.SERID.toString()))));
                    learningTool.setTitle(c.getString(c.getColumnIndex(LearningColumn.TITLE.toString())));
                    learningTool.setContent(c.getString(c.getColumnIndex(LearningColumn.DESCRIPTION.toString())));
                    learningTool.setVideoURL(c.getString(c.getColumnIndex(LearningColumn.VIDEOURL.toString())));
                    learningTool.setCountry(Integer.valueOf(c.getString(c.getColumnIndex(LearningColumn.COUNTRY.toString()))));
                    learningTool.setDefaultLearn(c.getString(c.getColumnIndex(LearningColumn.ISDEFAULT.toString())));
                    learningTool.setCreated(c.getString(c.getColumnIndex(LearningColumn.CREATED.toString())));
                    return learningTool;
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed Get getLearningToolByID By ID: " + e.toString());
            return null;
        }
        return null;

    }

    public LearningTool getLearningToolsBySerID(int id) {
        Log.i(TAG, "Get getLearningToolsBySerID by ServerID: ");
        String DB_Table = Table_LEARNING;
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    int ser_id = Integer.valueOf(c.getString(c.getColumnIndex(LearningColumn.SERID.toString())));
                    int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(LearningColumn.ID.toString())));

                    if (ser_id == id) {
                        LearningTool learningTool = getLearningToolByID(cur_id);
                        return learningTool;
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed Get getCountryBySerID by ServerID: " + e.toString());
            return null;
        }
        return null;
    }

    public ArrayList<LearningTool> getAllLearningTools() {
        Log.i(TAG, "Get All getAllLearningTools: ");
        String DB_Table = Table_COUNTRY;
        ArrayList<LearningTool> found = new ArrayList<LearningTool>();
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                c.moveToPosition(i);
                int cur_id = Integer.valueOf(c.getString(c.getColumnIndex(CountryColumn.ID.toString())));
                LearningTool learningTool = getLearningToolByID(cur_id);
                if (learningTool != null) {
                    found.add(learningTool);
                    Log.i(TAG, "Get All Countries: " + learningTool.getID());
                }
            }
        } catch (Exception e) {
            return null;
        }
        return found;
    }


    public DiscipleTreeCount getDiscipleTreeCount() {
        Log.i(TAG, "Get getDiscipleTreeCount: ");
        String DB_Table = Table_DISCIPLE_TREE;
        try {
            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    DiscipleTreeCount discipleTreeCount = new DiscipleTreeCount();
                    discipleTreeCount.setID(Integer.valueOf(c.getString(c.getColumnIndex(DiscipleTreeColumn.ID.toString()))));
                    discipleTreeCount.setSerID(Integer.valueOf(c.getString(c.getColumnIndex(DiscipleTreeColumn.SERID.toString()))));
                    discipleTreeCount.setUserID(Integer.valueOf(c.getString(c.getColumnIndex(DiscipleTreeColumn.USERID.toString()))));
                    discipleTreeCount.setCount(Integer.valueOf(c.getString(c.getColumnIndex(DiscipleTreeColumn.COUNT.toString()))));
                    return discipleTreeCount;
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed Get getDiscipleTreeCount: " + e.toString());
            return null;
        }
        return null;

    }


    private String[] getColumns(String DB_Table) {
        String[] strs = null;
        if (DB_Table == Table_DISCIPLES) {
            strs = getStrArrFromEnum(DisciplesColumn.class);
        } else if (DB_Table == Table_LOGS) {
            strs = getStrArrFromEnum(LogsColumn.class);
        } else if (DB_Table == Table_USER) {
            strs = getStrArrFromEnum(UserColumn.class);
        } else if (DB_Table == Table_SCHEDULES) {
            strs = getStrArrFromEnum(SchedulesColumn.class);
        } else if (DB_Table == Table_QUESTION_LIST) {
            strs = getStrArrFromEnum(QuestionListColumn.class);
        } else if (DB_Table == Table_QUESTION_ANSWER) {
            strs = getStrArrFromEnum(QuestionAnswerColumn.class);
        } else if (DB_Table == Table_Reports) {
            strs = getStrArrFromEnum(ReportColumn.class);
        } else if (DB_Table == Table_Report_Forms) {
            strs = getStrArrFromEnum(ReportFormColumn.class);
        } else if (DB_Table == Table_COUNTRY) {
            strs = getStrArrFromEnum(CountryColumn.class);
        } else if (DB_Table == Table_NEWSFEED) {
            strs = getStrArrFromEnum(NewsfeedColumn.class);
        } else if (DB_Table == Table_TESTIMONY) {
            strs = getStrArrFromEnum(TestimonyColumn.class);
        } else if (DB_Table == Table_CATEGORIES) {
            strs = getStrArrFromEnum(CategoryColumn.class);
        } else if (DB_Table == Table_IMAGE_SYNC) {
            strs = getStrArrFromEnum(ImageSyncColumn.class);
        } else if (DB_Table == Table_LEARNING) {
            strs = getStrArrFromEnum(LearningColumn.class);
        } else if (DB_Table == Table_DISCIPLE_TREE) {
            strs = getStrArrFromEnum(DiscipleTreeColumn.class);
        }
        return strs;
    }

    public static <E extends Enum<E>> String[] getStrArrFromEnum(Class<E> e) {
        Enum<E>[] enumConstants = e.getEnumConstants();
        int numConstants = enumConstants.length;
        String[] strArr = new String[numConstants];
        for (int i = 0; i < numConstants; i++) {
            strArr[i] = enumConstants[i].toString();
        }
        return  strArr;
    }
}
