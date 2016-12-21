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

import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Disciples.Disciple;
import deeplife.gcme.com.deeplife.Disciples.DiscipleListAdapter;
import deeplife.gcme.com.deeplife.R;

/**
 * Created by bengeos on 12/19/16.
 */

public class WinBuildSendActivity extends AppCompatActivity {
    private static RecyclerView myRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static Context myContext;
    private Button Btn_Win,Btn_Build,Btn_Send;
    private String DisciplePhone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_build_fragment_page);
        myRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(mLayoutManager);
        myContext = this;

        Btn_Win = (Button) findViewById(R.id.btn_winbuildsend_win);
        Btn_Build = (Button) findViewById(R.id.btn_winbuildsend_build);
        Btn_Send = (Button) findViewById(R.id.btn_winbuildsend_send);

        Btn_Build.setEnabled(false);
        Btn_Send.setEnabled(false);
        int sum = DeepLife.myDATABASE.count(Database.Table_QUESTION_LIST);
        Toast.makeText(this,"There are: "+sum,Toast.LENGTH_LONG).show();
        ArrayList<WinBuildSendQuestion> WinQuestioons = DeepLife.myDATABASE.getWinBuildSendQuestionsByCategorySerID(1);

        DisciplePhone = getIntent().getExtras().getString("DisciplePhone");

        if(WinQuestioons != null){
            mAdapter = new WinBuildSendItemsAdapter(WinQuestioons,this,DisciplePhone);
            myRecyclerView.setAdapter(mAdapter);
        }

        Btn_Win.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<WinBuildSendQuestion> WinQuestioons = DeepLife.myDATABASE.getWinBuildSendQuestionsByCategorySerID(1);
                if(WinQuestioons != null){
                    mAdapter = new WinBuildSendItemsAdapter(WinQuestioons,myContext,DisciplePhone);
                    myRecyclerView.setAdapter(mAdapter);
                }
            }
        });

        Btn_Build.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<WinBuildSendQuestion> BuildQuestions = DeepLife.myDATABASE.getWinBuildSendQuestionsByCategorySerID(2);
                if(BuildQuestions != null){
                    mAdapter = new WinBuildSendItemsAdapter(BuildQuestions,myContext,DisciplePhone);
                    myRecyclerView.setAdapter(mAdapter);
                }
            }
        });
        Btn_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<WinBuildSendQuestion> SendQuestions = DeepLife.myDATABASE.getWinBuildSendQuestionsByCategorySerID(3);
                if(SendQuestions != null){
                    mAdapter = new WinBuildSendItemsAdapter(SendQuestions,myContext,DisciplePhone);
                    myRecyclerView.setAdapter(mAdapter);
                }
            }
        });

    }
}
