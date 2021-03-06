package deeplife.gcme.com.deeplife.Wbs;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Disciples.Disciple;
import deeplife.gcme.com.deeplife.Models.Answer;
import deeplife.gcme.com.deeplife.Models.Category;
import deeplife.gcme.com.deeplife.Models.Logs;
import deeplife.gcme.com.deeplife.R;
import deeplife.gcme.com.deeplife.SyncService.SyncDatabase;

/**
 * Created by bengeos on 12/19/16.
 */

public class WbsActivity extends AppCompatActivity {
    private static final String TAG = "WbsActivity";

    private static RecyclerView myRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static LinearLayout L_Win, L_Build, L_Send;
    private static Context myContext;
    public static Button Btn_Win, Btn_Build, Btn_Send;
    public static ImageView DiscipleImage;
    public static TextView DiscipleName;
    public static String DisciplePhone;
    //public static int Stage;  // briggsm: don't think we need 2 different "stage" variables, so I got rid of this one.
    public static Disciple.STAGE buildStage;
    public static Disciple myDisciple;
    public static List<Category> categories;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wbs_fragment_page);
        toolbar = (Toolbar) findViewById(R.id.add_wbs_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.toolbar_title_disciple_wbs);

        wbs_Win_Questions = DeepLife.myDATABASE.getAllQuestionByStage(Disciple.STAGE.WIN);
        wbs_Build_Questions = DeepLife.myDATABASE.getAllQuestionByStage(Disciple.STAGE.BUILD);
        wbs_Send_Questions = DeepLife.myDATABASE.getAllQuestionByStage(Disciple.STAGE.SEND);


        myRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(mLayoutManager);
        myContext = this;
        DisciplePhone = getIntent().getExtras().getString("DisciplePhone");
        myDisciple = DeepLife.myDATABASE.getDiscipleByPhone(DisciplePhone);
        categories = new ArrayList<Category>();

        DiscipleImage = (ImageView) findViewById(R.id.img_wbs_image);
        DiscipleName = (TextView) findViewById(R.id.txt_wbs_fullname);
        Btn_Win = (Button) findViewById(R.id.btn_wbs_win);
        Btn_Build = (Button) findViewById(R.id.btn_wbs_build);
        Btn_Send = (Button) findViewById(R.id.btn_wbs_send);

        L_Win = (LinearLayout) findViewById(R.id.lyt_win);
        L_Build = (LinearLayout) findViewById(R.id.lyt_build);
        L_Send = (LinearLayout) findViewById(R.id.lyt_send);

        int sum = DeepLife.myDATABASE.count(Database.Table_QUESTION_LIST);
        //Toast.makeText(this, "There are: " + sum, Toast.LENGTH_LONG).show();
        Log.i(TAG, "onCreate: There are: '" + sum + "' questions in DB.");
        loadCurrentStage();
        Btn_Win.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateQuestionList(Disciple.STAGE.WIN);
            }
        });

        Btn_Build.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateQuestionList(Disciple.STAGE.BUILD);
            }
        });
        Btn_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateQuestionList(Disciple.STAGE.SEND);
            }
        });
        DiscipleName.setText(myDisciple.getDisplayName());
        if (myDisciple.getImagePath() != null) {
            File file = new File(myDisciple.getImagePath());
            if (file.isFile()) {
                DiscipleImage.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            }
        }

    }

    public static void UpdateQuestionList(Disciple.STAGE stage) {
        ArrayList<WbsQuestion> wbsQuestions = new ArrayList<WbsQuestion>();
        if(stage == Disciple.STAGE.WIN){
            wbsQuestions = wbs_Win_Questions;
        } else if(stage == Disciple.STAGE.BUILD){
            wbsQuestions = wbs_Build_Questions;
        }else {
            wbsQuestions = wbs_Send_Questions;
        }
        if (wbsQuestions != null) {
            updateStagePointerBar(stage);
            updateStageButton(getQuestionStage());
            UpdateGUIAdapter(wbsQuestions);
            Disciple disciple = DeepLife.myDATABASE.getDiscipleByPhone(DisciplePhone);
            if (disciple != null) {
                disciple.setStage(stage);
                long id = DeepLife.myDATABASE.updateDisciple(disciple);
                Logs logs = new Logs();
                logs.setType(Logs.TYPE.DISCIPLE);
                logs.setTask(Logs.TASK.UPDATE_DISCIPLES);
                logs.setValue("" + DisciplePhone);
                new SyncDatabase().AddLog(logs);
            }
        }
    }
    public static void updateStagePointerBar(Disciple.STAGE stage){
        if (stage == Disciple.STAGE.WIN) {
            // Win Stage
            L_Win.setBackgroundColor(Color.rgb(254, 191, 10));
            L_Build.setBackgroundColor(Color.rgb(00, 188, 216));
            L_Send.setBackgroundColor(Color.rgb(00, 188, 216));

        } else if (stage == Disciple.STAGE.BUILD) {
            // Build Stage
            L_Win.setBackgroundColor(Color.rgb(00, 188, 216));
            L_Build.setBackgroundColor(Color.rgb(254, 191, 10));
            L_Send.setBackgroundColor(Color.rgb(00, 188, 216));

        } else {
            // Send Stage
            L_Win.setBackgroundColor(Color.rgb(00, 188, 216));
            L_Build.setBackgroundColor(Color.rgb(00, 188, 216));
            L_Send.setBackgroundColor(Color.rgb(254, 191, 10));
        }
    }
    public static void updateStageButton(Disciple.STAGE stage){
        Btn_Build.setEnabled(false);
        Btn_Build.setTypeface(Btn_Build.getTypeface(), Typeface.ITALIC);
        Btn_Build.setText(R.string.text_build);

        Btn_Send.setEnabled(false);
        Btn_Send.setTypeface(Btn_Send.getTypeface(), Typeface.ITALIC);
        Btn_Send.setText(R.string.text_send);
        if (stage == Disciple.STAGE.WIN) {
            Btn_Win.setEnabled(true);
            Btn_Win.setTypeface(Btn_Win.getTypeface(), Typeface.BOLD);
            Btn_Win.setText("->" + DeepLife.getContext().getString(R.string.text_win) + "<-");

        } else if (stage == Disciple.STAGE.BUILD) {
            Btn_Win.setEnabled(true);
            Btn_Win.setTypeface(Btn_Win.getTypeface(), Typeface.BOLD);


            Btn_Build.setEnabled(true);
            Btn_Build.setTypeface(Btn_Build.getTypeface(), Typeface.BOLD);
            Btn_Build.setText("->" + DeepLife.getContext().getString(R.string.text_build) + "<-");
        } else {
            Btn_Win.setEnabled(true);
            Btn_Win.setTypeface(Btn_Win.getTypeface(), Typeface.BOLD);
            Btn_Win.setText(R.string.text_win);

            Btn_Build.setEnabled(true);
            Btn_Build.setTypeface(Btn_Build.getTypeface(), Typeface.BOLD);
            Btn_Build.setText(R.string.text_build);

            Btn_Send.setEnabled(true);
            Btn_Send.setTypeface(Btn_Send.getTypeface(), Typeface.BOLD);
            Btn_Send.setText("->" + DeepLife.getContext().getString(R.string.text_send) + "<-");
        }

    }
    public static void updateCurrentStageButton(){
        updateStageButton(getDiscipleStage());
    }
    public static void updateCurrentStagePointer(){
        updateStagePointerBar(getQuestionStage());
    }
    public static void UpdateGUIAdapter(List<WbsQuestion> wbsQuestions) {
        mAdapter = new WbsQuestionAdapter(myContext, wbsQuestions, myDisciple);
        myRecyclerView.setAdapter(mAdapter);
    }
    public static void loadCurrentStage(){
        UpdateQuestionList(getQuestionStage());
    }
    private static ArrayList<WbsQuestion> wbs_Win_Questions,wbs_Build_Questions,wbs_Send_Questions;
    public static Disciple.STAGE getQuestionStage(){
        Log.d(TAG, "checkStage: Checking for Stage");
        for (WbsQuestion wbsQuestion : wbs_Win_Questions) {
            if(wbsQuestion.getType() != WbsQuestion.Type.FOLDER){
                if (wbsQuestion.getMandatory() != 0) {
                    Answer answer = DeepLife.myDATABASE.getAnswerByQuestionIDandDisciplePhone(wbsQuestion.getSerID(), DisciplePhone);
                    if (answer != null) {
                        if (answer.getAnswer().equals("NO") || answer.getAnswer().equals("0")) {
                            return Disciple.STAGE.WIN;
                        }
                    }
                }
            }else {
                for(WbsQuestion wbsQuestion1: wbsQuestion.getChildList()){
                    if (wbsQuestion1.getMandatory() != 0) {
                        Answer answer = DeepLife.myDATABASE.getAnswerByQuestionIDandDisciplePhone(wbsQuestion1.getSerID(), DisciplePhone);
                        if (answer != null) {
                            if (answer.getAnswer().equals("NO") || answer.getAnswer().equals("0")) {
                                return Disciple.STAGE.WIN;
                            }
                        }
                    }
                }
            }

        }
        for (WbsQuestion wbsQuestion : wbs_Build_Questions) {
            if(wbsQuestion.getType() != WbsQuestion.Type.FOLDER){
                if (wbsQuestion.getMandatory() != 0) {
                    Answer answer = DeepLife.myDATABASE.getAnswerByQuestionIDandDisciplePhone(wbsQuestion.getSerID(), DisciplePhone);
                    if (answer != null) {
                        if (answer.getAnswer().equals("NO") || answer.getAnswer().equals("0")) {
                            return Disciple.STAGE.BUILD;
                        }
                    }
                }
            }else {
                for(WbsQuestion wbsQuestion1: wbsQuestion.getChildList()){
                    if (wbsQuestion1.getMandatory() != 0) {
                        Answer answer = DeepLife.myDATABASE.getAnswerByQuestionIDandDisciplePhone(wbsQuestion1.getSerID(), DisciplePhone);
                        if (answer != null) {
                            if (answer.getAnswer().equals("NO") || answer.getAnswer().equals("0")) {
                                return Disciple.STAGE.BUILD;
                            }
                        }
                    }
                }
            }
        }
        return Disciple.STAGE.SEND;
    }
    public static Disciple.STAGE getDiscipleStage(){
        Log.d(TAG, "checkStage: Checking for Stage");
        return getQuestionStage();
    }
}
