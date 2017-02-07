package deeplife.gcme.com.deeplife.SyncService;

import android.content.ContentValues;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Disciples.Disciple;
import deeplife.gcme.com.deeplife.LearningTools.LearningTool;
import deeplife.gcme.com.deeplife.Models.Answer;
import deeplife.gcme.com.deeplife.Models.Category;
import deeplife.gcme.com.deeplife.Models.Country;
import deeplife.gcme.com.deeplife.Models.Logs;
import deeplife.gcme.com.deeplife.Models.User;
import deeplife.gcme.com.deeplife.News.News;
import deeplife.gcme.com.deeplife.Testimony.Testimony;
import deeplife.gcme.com.deeplife.Testimony.TestimonyFragment;
import deeplife.gcme.com.deeplife.Wbs.WbsQuestion;

/**
 * Created by bengeos on 12/16/16.
 */

public class SyncDatabase {
    private static final String TAG = "SyncDatabase";

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

    public static void ProcessResponse(String jsonArray) {
        Gson myGson = new Gson();
        try {
            JSONObject myObject = (JSONObject) new JSONTokener(jsonArray).nextValue();
            Log.d(TAG, "Server Response (ProcessResponse): " + myObject.toString());
            if (!myObject.isNull(ApiResponseKey.RESPONSE.toString())) {
                JSONObject json_response = myObject.getJSONObject(ApiResponseKey.RESPONSE.toString());
                if (!json_response.isNull(ApiResponseKey.NEWSFEEDS.toString())) {
                    JSONArray json_newsfeeds = json_response.getJSONArray(ApiResponseKey.NEWSFEEDS.toString());
                    Log.d(TAG, "News Feeds: \n" + json_newsfeeds.toString());
                    Add_News(json_newsfeeds);
                }
                if (!json_response.isNull(ApiResponseKey.TESTIMONIES.toString())) {
                    JSONArray json_testimonies = json_response.getJSONArray(ApiResponseKey.TESTIMONIES.toString());
                    Log.d(TAG, "Testimonies: \n" + json_testimonies.toString());
                    Add_Testimony(json_testimonies);
                }
                if (!json_response.isNull(ApiResponseKey.DISCIPLES.toString())) {
                    JSONArray json_disciples = json_response.getJSONArray(ApiResponseKey.DISCIPLES.toString());
                    Log.d(TAG, "Disciples: \n" + json_disciples.toString());
                    Add_Disciples(json_disciples);
                }
                if (!json_response.isNull(ApiResponseKey.QUESTIONS.toString())) {
                    JSONArray json_questions = json_response.getJSONArray(ApiResponseKey.QUESTIONS.toString());
                    Log.d(TAG, "Questions: \n" + json_questions.toString());
                    Add_Questions(json_questions);
                }
                if (!json_response.isNull(ApiResponseKey.CATEGORIES.toString())) {
                    JSONArray json_categories = json_response.getJSONArray(ApiResponseKey.CATEGORIES.toString());
                    Log.d(TAG, "Categories: \n" + json_categories.toString());
                    Add_Category(json_categories);
                }
                if (!json_response.isNull(ApiResponseKey.ANSWERS.toString())) {
                    JSONArray json_answers = json_response.getJSONArray(ApiResponseKey.ANSWERS.toString());
                    Log.d(TAG, "Answers: \n" + json_answers.toString());
                    Add_Answers(json_answers);
                }
                if (!json_response.isNull(ApiResponseKey.COUNTRY_UC.toString())) {
                    JSONArray json_answers = json_response.getJSONArray(ApiResponseKey.COUNTRY_UC.toString());
                    Log.d(TAG, "Country: \n" + json_answers.toString());
                    Add_Countries(json_answers);
                }
                if (!json_response.isNull(ApiResponseKey.LOG_RESPONSE.toString())) {
                    JSONArray json_logs = json_response.getJSONArray(ApiResponseKey.LOG_RESPONSE.toString());
                    Log.d(TAG, "Deleting Logs: \n" + json_logs.toString());
                    Delete_Logs(json_logs);
                }
                if (!json_response.isNull(ApiResponseKey.PROFILE.toString())) {
                    JSONObject json_user_profile = json_response.getJSONObject(ApiResponseKey.PROFILE.toString());
                    Log.d(TAG, "Updating Main User: \n" + json_user_profile.toString());
                    Update_MainUser(json_user_profile);
                }
                if (!json_response.isNull(ApiResponseKey.LEARNINGTOOLS.toString())) {
                    JSONArray json_learning_tools = json_response.getJSONArray(ApiResponseKey.LEARNINGTOOLS.toString());
                    Log.d(TAG, "Add Learning tools: \n" + json_learning_tools.toString());
                    Add_LearningTools(json_learning_tools);
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
                    cv.put(Database.NewsfeedColumn.NEWS_ID.toString(), obj.getString(ApiResponseKey.ID.toString()));
                    cv.put(Database.NewsfeedColumn.TITLE.toString(), obj.getString(ApiResponseKey.TITLE.toString()));
                    cv.put(Database.NewsfeedColumn.CONTENT.toString(), obj.getString(ApiResponseKey.CONTENT.toString()));
                    cv.put(Database.NewsfeedColumn.CATEGORY.toString(), obj.getString(ApiResponseKey.CATEGORY.toString()));
                    cv.put(Database.NewsfeedColumn.IMAGEURL.toString(), obj.getString(ApiResponseKey.IMAGE_URL.toString()));
                    cv.put(Database.NewsfeedColumn.IMAGEPATH.toString(), "");
                    cv.put(Database.NewsfeedColumn.PUBDATE.toString(), obj.getString(ApiResponseKey.PUBLISH_DATE.toString()));
                    News news = DeepLife.myDATABASE.getNewsBySerID(Integer.valueOf(obj.getString(ApiResponseKey.ID.toString())));
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
                    cv.put(Database.TestimonyColumn.SERID.toString(), obj.getString(ApiResponseKey.ID.toString()));
                    cv.put(Database.TestimonyColumn.USERID.toString(), obj.getString(ApiResponseKey.USER_ID.toString()));
                    cv.put(Database.TestimonyColumn.DESCRIPTION.toString(), obj.getString(ApiResponseKey.DESCRIPTION.toString()));
                    cv.put(Database.TestimonyColumn.STATUS.toString(), obj.getString(ApiResponseKey.STATUS.toString()));
                    cv.put(Database.TestimonyColumn.PUBDATE.toString(), obj.getString(ApiResponseKey.CREATED.toString()));
                    cv.put(Database.TestimonyColumn.USERNAME.toString(), obj.getString(ApiResponseKey.USER_NAME.toString()));
                    Testimony testimony = DeepLife.myDATABASE.getTestimonyBySerID(Integer.valueOf(obj.getString(ApiResponseKey.ID.toString())));
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
                        cv.put(Database.DisciplesColumn.SERID.toString(), obj.getString(ApiResponseKey.ID.toString()));
                        cv.put(Database.DisciplesColumn.FULLNAME.toString(), obj.getString(ApiResponseKey.FIRSTNAME.toString()));
                        cv.put(Database.DisciplesColumn.DISPLAYNAME.toString(), obj.getString(ApiResponseKey.DISPLAYNAME.toString()));
                        cv.put(Database.DisciplesColumn.EMAIL.toString(), obj.getString(ApiResponseKey.EMAIL.toString()));
                        cv.put(Database.DisciplesColumn.PHONE.toString(), obj.getString(ApiResponseKey.PHONE_NO.toString()));
                        cv.put(Database.DisciplesColumn.COUNTRY.toString(), obj.getString(ApiResponseKey.COUNTRY_LC.toString()));
                        cv.put(Database.DisciplesColumn.MENTORID.toString(), obj.getString(ApiResponseKey.MENTOR_ID.toString()));
                        cv.put(Database.DisciplesColumn.STAGE.toString(), obj.getString(ApiResponseKey.STAGE.toString()));
                        cv.put(Database.DisciplesColumn.IMAGEURL.toString(), obj.getString(ApiResponseKey.PICTURE.toString()));
                        cv.put(Database.DisciplesColumn.IMAGEPATH.toString(), "");
                        cv.put(Database.DisciplesColumn.ROLE.toString(), obj.getString(ApiResponseKey.ROLE_ID.toString()));
                        cv.put(Database.DisciplesColumn.GENDER.toString(), obj.getString(ApiResponseKey.GENDER.toString()));
                        cv.put(Database.DisciplesColumn.CREATED.toString(), obj.getString(ApiResponseKey.CREATED.toString()));
                        Disciple disciple = DeepLife.myDATABASE.getDiscipleByPhone(obj.getString(ApiResponseKey.PHONE_NO.toString()));
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
                    cv.put(Database.QuestionListColumn.SERID.toString(), obj.getString(ApiResponseKey.ID.toString()));
                    cv.put(Database.QuestionListColumn.CATEGORY.toString(), obj.getString(ApiResponseKey.CATEGORY.toString()));
                    cv.put(Database.QuestionListColumn.QUESTION.toString(), obj.getString(ApiResponseKey.QUESTION.toString()));
                    cv.put(Database.QuestionListColumn.DESCRIPTION.toString(), obj.getString(ApiResponseKey.DESCRIPTION.toString()));
                    cv.put(Database.QuestionListColumn.MANDATORY.toString(), obj.getString(ApiResponseKey.MANDATORY.toString()));
                    cv.put(Database.QuestionListColumn.TYPE.toString(), obj.getString(ApiResponseKey.TYPE.toString()));
                    cv.put(Database.QuestionListColumn.COUNTRY.toString(), obj.getString(ApiResponseKey.COUNTRY_LC.toString()));
                    cv.put(Database.QuestionListColumn.CREATED.toString(), obj.getString(ApiResponseKey.CREATED.toString()));
                    WbsQuestion wbsQuestion = DeepLife.myDATABASE.getWbsQuestionBySerID(Integer.valueOf(obj.getString(ApiResponseKey.ID.toString())));
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
                    cv.put(Database.CategoryColumn.SERID.toString(), obj.getString(ApiResponseKey.ID.toString()));
                    cv.put(Database.CategoryColumn.NAME.toString(), obj.getString(ApiResponseKey.NAME.toString()));
                    cv.put(Database.CategoryColumn.PARENT.toString(), obj.getString(ApiResponseKey.PARENT.toString()));
                    cv.put(Database.CategoryColumn.STATUS.toString(), obj.getString(ApiResponseKey.STATUS.toString()));
                    cv.put(Database.CategoryColumn.CREATED.toString(), obj.getString(ApiResponseKey.CREATED.toString()));
                    ;
                    Category category = DeepLife.myDATABASE.getCategoryByID(Integer.valueOf(obj.getString(ApiResponseKey.ID.toString())));
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
                    ContentValues cv = new ContentValues();
                    cv.put(Database.QuestionAnswerColumn.SERID.toString(), obj.getString(ApiResponseKey.ID.toString()));
                    cv.put(Database.QuestionAnswerColumn.DISCIPLEPHONE.toString(), obj.getString(ApiResponseKey.DISCIPLE_PHONE.toString()));
                    cv.put(Database.QuestionAnswerColumn.QUESTION_ID.toString(), obj.getString(ApiResponseKey.QUESTION_ID.toString()));
                    cv.put(Database.QuestionAnswerColumn.ANSWER.toString(), obj.getString(ApiResponseKey.ANSWER.toString()));
                    cv.put(Database.QuestionAnswerColumn.BUILDSTAGE.toString(), obj.getString(ApiResponseKey.STAGE.toString()));

                    //Answer answer = DeepLife.myDATABASE.getAnswerBySerID(Integer.valueOf(obj.getString(ApiResponseKey.ID.toString())));
                    Answer answer = DeepLife.myDATABASE.getAnswerByQuestionID(Integer.valueOf(obj.getString(ApiResponseKey.QUESTION_ID.toString())));
                    if (answer == null) {
                        long x = DeepLife.myDATABASE.insert(Database.Table_QUESTION_ANSWER, cv);
                        if (x > 0) {
                            Log.d(TAG, "Successfully Added: Answers -> \n" + cv.toString());
                        } else {
                            Log.e(TAG, "Error During Adding: Answers -> \n" + cv.toString());
                        }
                    } else {
                        long x = DeepLife.myDATABASE.update(Database.Table_QUESTION_ANSWER, cv, answer.getID());
                        Log.d(TAG, "Updated: Answers -> \n" + cv.toString());
                        if (x > 0) {
                            Log.d(TAG, "Successfully Updated: Answers Updated -> \n" + cv.toString());
                        } else {
                            Log.e(TAG, "Error During Updating: Answers -> \n" + cv.toString());
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
                cv.put(Database.UserColumn.SERID.toString(), obj.getString(ApiResponseKey.ID.toString()));
                cv.put(Database.UserColumn.FULL_NAME.toString(), obj.getString(ApiResponseKey.FIRSTNAME.toString()));
                cv.put(Database.UserColumn.EMAIL.toString(), obj.getString(ApiResponseKey.EMAIL.toString()));
                cv.put(Database.UserColumn.PHONE.toString(), obj.getString(ApiResponseKey.PHONE_NO.toString()));
                cv.put(Database.UserColumn.PASSWORD.toString(), ApiResponseKey.PASS.toString());
                cv.put(Database.UserColumn.COUNTRY.toString(), obj.getString(ApiResponseKey.COUNTRY_LC.toString()));
                cv.put(Database.UserColumn.GENDER.toString(), obj.getString(ApiResponseKey.GENDER.toString()));
                cv.put(Database.UserColumn.PICTURE.toString(), obj.getString(ApiResponseKey.PICTURE.toString()));
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
                    cv.put(Database.LearningColumn.SERID.toString(), obj.getString(ApiResponseKey.ID.toString()));
                    cv.put(Database.LearningColumn.TITLE.toString(), obj.getString(ApiResponseKey.TITLE.toString()));
                    cv.put(Database.LearningColumn.DESCRIPTION.toString(), obj.getString(ApiResponseKey.DESCRIPTION.toString()));
                    cv.put(Database.LearningColumn.VIDEOURL.toString(), obj.getString(ApiResponseKey.IFRAMCODE.toString()));
                    cv.put(Database.LearningColumn.COUNTRY.toString(), obj.getString(ApiResponseKey.COUNTRY_LC.toString()));
                    cv.put(Database.LearningColumn.ISDEFAULT.toString(), obj.getString(ApiResponseKey.DEFAULT_LEARN.toString()));
                    cv.put(Database.LearningColumn.CREATED.toString(), obj.getString(ApiResponseKey.CREATED.toString()));
                    LearningTool learningTool = DeepLife.myDATABASE.getLearningToolsBySerID(Integer.valueOf(obj.getString(ApiResponseKey.ID.toString())));
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

    public static void Update_MainUser(JSONObject json_mainuser) {
        try {
            User user = DeepLife.myDATABASE.getMainUser();
            if (user != null) {
                if (json_mainuser.length() > 0) {
                    Log.d(TAG, "Updating MainUser  -> \n" + json_mainuser.toString());
                    JSONObject obj = json_mainuser;
                    ContentValues cv = new ContentValues();
                    cv.put(Database.UserColumn.SERID.toString(), obj.getString(ApiResponseKey.ID.toString()));
                    cv.put(Database.UserColumn.FULL_NAME.toString(), obj.getString(ApiResponseKey.FIRSTNAME.toString()));
                    cv.put(Database.UserColumn.EMAIL.toString(), obj.getString(ApiResponseKey.EMAIL.toString()));
                    cv.put(Database.UserColumn.PHONE.toString(), obj.getString(ApiResponseKey.PHONE_NO.toString()));
                    cv.put(Database.UserColumn.COUNTRY.toString(), obj.getString(ApiResponseKey.COUNTRY_LC.toString()));
                    cv.put(Database.UserColumn.GENDER.toString(), obj.getString(ApiResponseKey.GENDER.toString()));
                    cv.put(Database.UserColumn.PICTURE.toString(), obj.getString(ApiResponseKey.PICTURE.toString()));
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
                    cv.put(Database.UserColumn.SERID.toString(), obj.getString(ApiResponseKey.ID.toString()));
                    cv.put(Database.UserColumn.FULL_NAME.toString(), obj.getString(ApiResponseKey.FIRSTNAME.toString()));
                    cv.put(Database.UserColumn.EMAIL.toString(), obj.getString(ApiResponseKey.EMAIL.toString()));
                    cv.put(Database.UserColumn.PHONE.toString(), obj.getString(ApiResponseKey.PHONE_NO.toString()));
                    cv.put(Database.UserColumn.PASSWORD.toString(), ApiResponseKey.PASS.toString());
                    cv.put(Database.UserColumn.COUNTRY.toString(), obj.getString(ApiResponseKey.COUNTRY_LC.toString()));
                    cv.put(Database.UserColumn.GENDER.toString(), obj.getString(ApiResponseKey.GENDER.toString()));
                    cv.put(Database.UserColumn.PICTURE.toString(), obj.getString(ApiResponseKey.PICTURE.toString()));
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
                    cv.put(Database.CountryColumn.SERID.toString(), obj.getString(ApiResponseKey.ID.toString()));
                    cv.put(Database.CountryColumn.ISO3.toString(), obj.getString(ApiResponseKey.ISO3.toString()));
                    cv.put(Database.CountryColumn.NAME.toString(), obj.getString(ApiResponseKey.NAME.toString()));
                    cv.put(Database.CountryColumn.CODE.toString(), obj.getString(ApiResponseKey.CODE.toString()));
                    Country country = DeepLife.myDATABASE.getCountryByID(Integer.valueOf(obj.getString(ApiResponseKey.ID.toString())));
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
                    int id = Integer.valueOf(obj.getString(ApiResponseKey.LOG_ID.toString()));
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
