package deeplife.gcme.com.deeplife.WinBuildSend;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
    public static String DisciplePhone;
    public static int Stage;
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
        categories = new ArrayList<Category>();

        Btn_Win = (Button) findViewById(R.id.btn_winbuildsend_win);
        Btn_Build = (Button) findViewById(R.id.btn_winbuildsend_build);
        Btn_Send = (Button) findViewById(R.id.btn_winbuildsend_send);

        int sum = DeepLife.myDATABASE.count(Database.Table_QUESTION_LIST);
        Toast.makeText(this,"There are: "+sum,Toast.LENGTH_LONG).show();
        checkStage();
        ArrayList<WinBuildSendQuestion> WinQuestioons = DeepLife.myDATABASE.getWinBuildSendQuestionsByCategorySerID(Stage);
        if(WinQuestioons != null){
            List<Category> items = DeepLife.myDATABASE.getCategoriesByParentID(Stage);
            if(items.size()>0){
                for(Category category :items){
                    WinBuildSendQuestion question = new WinBuildSendQuestion();
                    question.setQuestion(category.getName());
                    question.setType("Folder");
                    WinQuestioons.add(question);
                }
            }
            UpdateGUIAdapter(WinQuestioons);
        }

        Btn_Win.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<WinBuildSendQuestion> WinQuestioons = DeepLife.myDATABASE.getWinBuildSendQuestionsByCategorySerID(1);
                if(WinQuestioons != null){
                    List<Category> items = DeepLife.myDATABASE.getCategoriesByParentID(1);
                    if(items.size()>0){
                        for(Category category :items){
                            WinBuildSendQuestion question = new WinBuildSendQuestion();
                            question.setQuestion(category.getName());
                            question.setType("Folder");
                            WinQuestioons.add(question);
                        }
                    }
                    UpdateGUIAdapter(WinQuestioons);
                }
            }
        });

        Btn_Build.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<WinBuildSendQuestion> BuildQuestions = DeepLife.myDATABASE.getWinBuildSendQuestionsByCategorySerID(2);
                if(BuildQuestions != null){
                    List<Category> items = DeepLife.myDATABASE.getCategoriesByParentID(2);
                    if(items.size()>0){
                        for(Category category :items){
                            WinBuildSendQuestion question = new WinBuildSendQuestion();
                            question.setQuestion(category.getName());
                            question.setType("Folder");
                            BuildQuestions.add(question);
                        }
                    }
                    UpdateGUIAdapter(BuildQuestions);
                }
            }
        });
        Btn_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<WinBuildSendQuestion> SendQuestions = DeepLife.myDATABASE.getWinBuildSendQuestionsByCategorySerID(3);
                if(SendQuestions != null){
                    List<Category> items = DeepLife.myDATABASE.getCategoriesByParentID(3);
                    if(items.size()>0){
                        for(Category category :items){
                            WinBuildSendQuestion question = new WinBuildSendQuestion();
                            question.setQuestion(category.getName());
                            question.setType("Folder");
                            SendQuestions.add(question);
                        }
                    }
                    UpdateGUIAdapter(SendQuestions);
                }
            }
        });

    }
    public static void UpdateGUIAdapter(List<WinBuildSendQuestion> datas){
        mAdapter = new WinBuildSendItemsAdapter(datas,myContext,DisciplePhone);
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
        }else {
            Stage = 2;
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
            }else {
                Stage = 3;
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
                }else {
                    Stage = 3;
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
