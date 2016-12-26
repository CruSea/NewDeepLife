package deeplife.gcme.com.deeplife.WinBuildSend;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Disciples.Disciple;
import deeplife.gcme.com.deeplife.Disciples.DiscipleListAdapter;
import deeplife.gcme.com.deeplife.Models.Answer;
import deeplife.gcme.com.deeplife.Models.Category;
import deeplife.gcme.com.deeplife.R;
import deeplife.gcme.com.deeplife.Tabs.SlidingTabLayout;

/**
 * Created by bengeos on 12/19/16.
 */

public class WinBuildSendActivity extends AppCompatActivity {
    private static RecyclerView myRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static Context myContext;
    public static Button Btn_Win,Btn_Build,Btn_Send;
    public static ImageView DiscipleImage;
    public static TextView DiscipleName;
    public static String DisciplePhone;
    public static int Stage;
    public static String BuildStage;
    public static Disciple myDisciple;
    public static List<Category> categories;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_build_fragment_page);
        myRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(mLayoutManager);
        myContext = this;
        Stage = 1;
        DisciplePhone = getIntent().getExtras().getString("DisciplePhone");
        myDisciple = DeepLife.myDATABASE.getDiscipleByPhone(DisciplePhone);
        categories = new ArrayList<Category>();

        DiscipleImage = (ImageView) findViewById(R.id.img_winbuildsend_image);
        DiscipleName = (TextView) findViewById(R.id.txt_winbuildsend_fullname);
        Btn_Win = (Button) findViewById(R.id.btn_winbuildsend_win);
        Btn_Build = (Button) findViewById(R.id.btn_winbuildsend_build);
        Btn_Send = (Button) findViewById(R.id.btn_winbuildsend_send);

        int sum = DeepLife.myDATABASE.count(Database.Table_QUESTION_LIST);
        Toast.makeText(this,"There are: "+sum,Toast.LENGTH_LONG).show();
        checkStage();
        UpdateQuestionList(Stage);

        Btn_Win.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateQuestionList(1);
            }
        });

        Btn_Build.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateQuestionList(2);
            }
        });
        Btn_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateQuestionList(3);
            }
        });
        DiscipleName.setText(myDisciple.getDisplayName());
        if(myDisciple.getImagePath() != null){
            File file = new File(myDisciple.getImagePath());
            if(file.isFile()){
                DiscipleImage.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            }
        }

    }
    public void UpdateQuestionList(int stage){
        ArrayList<WinBuildSendQuestion> SendQuestions = DeepLife.myDATABASE.getWinBuildSendQuestionsByCategorySerID(stage);
        if(SendQuestions != null){
            List<Category> items = DeepLife.myDATABASE.getCategoriesByParentID(stage);
            if(items.size()>0){
                for(Category category :items){
                    WinBuildSendQuestion question = new WinBuildSendQuestion();
                    question.setQuestion(category.getName());
                    question.setType("Folder");
                    ArrayList<WinBuildSendQuestion> FoundQuestion = DeepLife.myDATABASE.getWinBuildSendQuestionsByCategorySerID(category.getSerID());
                    if(FoundQuestion.size()>0){
                        SendQuestions.add(question);
                    }
                }
            }
            if(stage == 1){
                BuildStage = "WIN";
            }else if(stage == 2){
                BuildStage = "BUILD";
            }else {
                BuildStage = "SEND";
            }
            UpdateGUIAdapter(SendQuestions);
            Disciple disciple = DeepLife.myDATABASE.getDiscipleByPhone(DisciplePhone);
            if(disciple != null){
                disciple.setStage(BuildStage);
                DeepLife.myDATABASE.updateDisciple(disciple);
            }
        }
    }
    public static void UpdateGUIAdapter(List<WinBuildSendQuestion> datas){
        mAdapter = new WinBuildSendItemsAdapter(datas,myContext,DisciplePhone,BuildStage);
        myRecyclerView.setAdapter(mAdapter);
    }
    public static void checkStage() {
        Toast.makeText(myContext,"Checking for Stage change",Toast.LENGTH_LONG).show();
        int WinBuilSend = 1;
        ArrayList<WinBuildSendQuestion> Questions = DeepLife.myDATABASE.getWinBuildSendQuestionsByCategorySerID(1);
        for(WinBuildSendQuestion WinQuestioons : Questions){
            if(WinQuestioons.getMandatory() != 0){
                Answer answer = DeepLife.myDATABASE.getAnswerByQuestionIDandDisciplePhone(WinQuestioons.getSerID(),DisciplePhone);
                if(answer != null){
                    if(answer.getAnswer().equals("NO") || answer.getAnswer().equals("0")){
                        WinBuilSend =0;
                        break;
                    }
                }else {
                    WinBuilSend = 0;
                    break;
                }
            }
        }
        if(WinBuilSend == 0){
            Stage = 1;
            BuildStage = "WIN";
        }else {
            Stage = 2;
            BuildStage = "BUILD";
            WinBuilSend = 1;
            Questions = DeepLife.myDATABASE.getWinBuildSendQuestionsByCategorySerID(2);
            for(WinBuildSendQuestion WinQuestioons : Questions){
                if(WinQuestioons.getMandatory() != 0){
                    Answer answer = DeepLife.myDATABASE.getAnswerByQuestionIDandDisciplePhone(WinQuestioons.getSerID(),DisciplePhone);
                    if(answer != null){
                        if(answer.getAnswer().equals("NO") || answer.getAnswer().equals("0")){
                            WinBuilSend =0;
                            break;
                        }
                    }else {
                        WinBuilSend =0;
                        break;
                    }
                }
            }
            if(WinBuilSend == 0 && Stage > 1){
                Stage = 2;
                BuildStage = "BUILD";
            }else {
                Stage = 3;
                BuildStage = "SEND";
                WinBuilSend = 1;
                Questions = DeepLife.myDATABASE.getWinBuildSendQuestionsByCategorySerID(3);
                for(WinBuildSendQuestion WinQuestioons : Questions){
                    if(WinQuestioons.getMandatory() != 0){
                        Answer answer = DeepLife.myDATABASE.getAnswerByQuestionIDandDisciplePhone(WinQuestioons.getSerID(),DisciplePhone);
                        if(answer != null){
                            if(answer.getAnswer().equals("NO") || answer.getAnswer().equals("0")){
                                WinBuilSend =0;
                                break;
                            }
                        }else {
                            WinBuilSend =0;
                            break;
                        }
                    }
                }
                if(WinBuilSend == 0 && Stage > 2){
                    Stage = 3;
                    BuildStage = "SEND";
                }else {
                    Stage = 3;
                    BuildStage = "SEND";
                }
            }
        }

        if(Stage == 1){
            Btn_Win.setEnabled(true);
            Btn_Build.setEnabled(false);
            Btn_Send.setEnabled(false);
        }else if(Stage == 2){
            Btn_Win.setEnabled(true);
            Btn_Build.setEnabled(true);
            Btn_Send.setEnabled(false);
        }else {
            Btn_Win.setEnabled(true);
            Btn_Build.setEnabled(true);
            Btn_Send.setEnabled(true);
        }
    }
}
