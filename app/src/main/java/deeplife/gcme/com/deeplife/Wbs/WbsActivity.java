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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Disciples.Disciple;
import deeplife.gcme.com.deeplife.Disciples.DisciplesFragment;
import deeplife.gcme.com.deeplife.Models.Answer;
import deeplife.gcme.com.deeplife.Models.Category;
import deeplife.gcme.com.deeplife.R;

/**
 * Created by bengeos on 12/19/16.
 */

public class WbsActivity extends AppCompatActivity {
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
        Toast.makeText(this, "There are: " + sum, Toast.LENGTH_LONG).show();
        checkStage();
        UpdateQuestionList(buildStage);

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

    public void UpdateQuestionList(Disciple.STAGE stage) {
        ArrayList<WbsQuestion> SendQuestions = DeepLife.myDATABASE.getWbsQuestionsByCategorySerID(stage.toServerId());
        if (SendQuestions != null) {
            List<Category> items = DeepLife.myDATABASE.getCategoriesByParentID(stage.toServerId());
            if (items.size() > 0) {
                for (Category category : items) {
                    WbsQuestion question = new WbsQuestion();
                    question.setQuestion(category.getName());
                    question.setType(WbsQuestion.Type.FOLDER);
                    ArrayList<WbsQuestion> FoundQuestion = DeepLife.myDATABASE.getWbsQuestionsByCategorySerID(category.getSerID());
                    if (FoundQuestion.size() > 0) {
                        SendQuestions.add(question);
                    }
                }
            }
            if (buildStage == Disciple.STAGE.WIN) {
                L_Win.setBackgroundColor(Color.rgb(254, 191, 10));
                L_Build.setBackgroundColor(Color.rgb(00, 188, 216));
                L_Send.setBackgroundColor(Color.rgb(00, 188, 216));
            } else if (buildStage == Disciple.STAGE.BUILD) {
                L_Win.setBackgroundColor(Color.rgb(00, 188, 216));
                L_Build.setBackgroundColor(Color.rgb(254, 191, 10));
                L_Send.setBackgroundColor(Color.rgb(00, 188, 216));
            } else {
                // SEND
                L_Win.setBackgroundColor(Color.rgb(00, 188, 216));
                L_Build.setBackgroundColor(Color.rgb(00, 188, 216));
                L_Send.setBackgroundColor(Color.rgb(254, 191, 10));
            }

            UpdateGUIAdapter(SendQuestions);
            Disciple disciple = DeepLife.myDATABASE.getDiscipleByPhone(DisciplePhone);
            if (disciple != null) {
                disciple.setStage(buildStage);
                DeepLife.myDATABASE.updateDisciple(disciple);

            }
        }
    }

    public static void UpdateGUIAdapter(List<WbsQuestion> datas) {
        mAdapter = new WbsItemsAdapter(datas, myContext, DisciplePhone, buildStage);
        myRecyclerView.setAdapter(mAdapter);
        try {
            DisciplesFragment.UpdateList();
        } catch (Exception e) {

        }
    }

    public static void checkStage() {
        Toast.makeText(myContext, "Checking for Stage change", Toast.LENGTH_LONG).show();
        int wbs = 1;
        ArrayList<WbsQuestion> Questions = DeepLife.myDATABASE.getWbsQuestionsByCategorySerID(1);
        for (WbsQuestion WinQuestioons : Questions) {
            if (WinQuestioons.getMandatory() != 0) {
                Answer answer = DeepLife.myDATABASE.getAnswerByQuestionIDandDisciplePhone(WinQuestioons.getSerID(), DisciplePhone);
                if (answer != null) {
                    if (answer.getAnswer().equals("NO") || answer.getAnswer().equals("0")) {
                        wbs = 0;
                        break;
                    }
                } else {
                    wbs = 0;
                    break;
                }
            }
        }
        if (wbs == 0) {
            buildStage = Disciple.STAGE.WIN;
        } else {
            buildStage = Disciple.STAGE.BUILD;
            wbs = 1;
            Questions = DeepLife.myDATABASE.getWbsQuestionsByCategorySerID(2);
            for (WbsQuestion WinQuestioons : Questions) {
                if (WinQuestioons.getMandatory() != 0) {
                    Answer answer = DeepLife.myDATABASE.getAnswerByQuestionIDandDisciplePhone(WinQuestioons.getSerID(), DisciplePhone);
                    if (answer != null) {
                        if (answer.getAnswer().equals("NO") || answer.getAnswer().equals("0")) {
                            wbs = 0;
                            break;
                        }
                    } else {
                        wbs = 0;
                        break;
                    }
                }
            }
            //if(wbs == 0 && Stage > 1){  // briggsm: took out check for Steve > 1, cuz it looks useless to me.
            if (wbs == 0) {
                buildStage = Disciple.STAGE.BUILD;
            } else {
                buildStage = Disciple.STAGE.SEND;
                wbs = 1;
                Questions = DeepLife.myDATABASE.getWbsQuestionsByCategorySerID(3);
                for (WbsQuestion WinQuestioons : Questions) {
                    if (WinQuestioons.getMandatory() != 0) {
                        Answer answer = DeepLife.myDATABASE.getAnswerByQuestionIDandDisciplePhone(WinQuestioons.getSerID(), DisciplePhone);
                        if (answer != null) {
                            if (answer.getAnswer().equals("NO") || answer.getAnswer().equals("0")) {
                                wbs = 0;
                                break;
                            }
                        } else {
                            wbs = 0;
                            break;
                        }
                    }
                }
                //if(wbs == 0 && Stage > 2){ // briggsm: took out check for Steve > 1, cuz it looks useless to me.
                if (wbs == 0) {
                    buildStage = Disciple.STAGE.SEND;
                } else {
                    buildStage = Disciple.STAGE.SEND;
                }
            }
        }

        if (buildStage == Disciple.STAGE.WIN) {
            Btn_Win.setEnabled(true);
            Btn_Win.setTypeface(Btn_Win.getTypeface(), Typeface.BOLD);
            Btn_Win.setText("->" + DeepLife.getContext().getString(R.string.text_win) + "<-");


            Btn_Build.setEnabled(false);
            Btn_Build.setTypeface(Btn_Build.getTypeface(), Typeface.ITALIC);
            Btn_Build.setText(R.string.text_build);


            Btn_Send.setEnabled(false);
            Btn_Send.setTypeface(Btn_Send.getTypeface(), Typeface.ITALIC);
            Btn_Send.setText(R.string.text_send);

            myDisciple.setStage(Disciple.STAGE.WIN);
        } else if (buildStage == Disciple.STAGE.BUILD) {
            Btn_Win.setEnabled(true);
            Btn_Win.setTypeface(Btn_Win.getTypeface(), Typeface.ITALIC);
            Btn_Win.setText(R.string.text_win);
            Btn_Win.setTextColor(Color.GRAY);
            Btn_Win.setActivated(false);

            Btn_Build.setEnabled(true);
            Btn_Build.setTypeface(Btn_Build.getTypeface(), Typeface.BOLD);
            Btn_Build.setText("->" + DeepLife.getContext().getString(R.string.text_build) + "<-");

            Btn_Send.setEnabled(false);
            Btn_Send.setTypeface(Btn_Send.getTypeface(), Typeface.ITALIC);
            Btn_Send.setText(DeepLife.getContext().getString(R.string.text_send));

            myDisciple.setStage(Disciple.STAGE.BUILD);
        } else {
            // SEND
            Btn_Win.setEnabled(true);
            Btn_Win.setTypeface(Btn_Win.getTypeface(), Typeface.ITALIC);
            Btn_Win.setTextColor(Color.GRAY);
            Btn_Win.setText(DeepLife.getContext().getString(R.string.text_win));

            Btn_Build.setEnabled(true);
            Btn_Build.setTypeface(Btn_Build.getTypeface(), Typeface.ITALIC);
            Btn_Build.setTextColor(Color.GRAY);
            Btn_Build.setText(DeepLife.getContext().getString(R.string.text_build));

            Btn_Send.setEnabled(true);
            Btn_Send.setTypeface(Btn_Send.getTypeface(), Typeface.BOLD);
            Btn_Send.setText("->" + DeepLife.getContext().getString(R.string.text_send) + "<-");

            myDisciple.setStage(Disciple.STAGE.SEND);
        }
        DeepLife.myDATABASE.updateDisciple(myDisciple);
    }
}
