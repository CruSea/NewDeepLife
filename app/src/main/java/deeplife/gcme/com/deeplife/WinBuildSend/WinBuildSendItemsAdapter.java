package deeplife.gcme.com.deeplife.WinBuildSend;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Disciples.Disciple;
import deeplife.gcme.com.deeplife.Disciples.DiscipleListAdapter;
import deeplife.gcme.com.deeplife.Models.Answer;
import deeplife.gcme.com.deeplife.R;

/**
 * Created by bengeos on 12/19/16.
 */

public class WinBuildSendItemsAdapter extends RecyclerView.Adapter<WinBuildSendItemsAdapter.DataObjectHolder> {
    public static List<WinBuildSendQuestion> winBuildSends;
    public static Context myContext;
    public static String DisciplePhone;

    public WinBuildSendItemsAdapter(List<WinBuildSendQuestion> winBuildSends,Context context, String disciplePhone) {
        this.winBuildSends = winBuildSends;
        myContext = context;
        DisciplePhone = disciplePhone;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.win_build_fragment_item1, parent, false);
        WinBuildSendItemsAdapter.DataObjectHolder dataObjectHolder = new WinBuildSendItemsAdapter.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        Answer answer = DeepLife.myDATABASE.getAnswerByQuestionIDandDisciplePhone(winBuildSends.get(position).getSerID(),DisciplePhone);
        if(position%2 == 0){
            holder.frameLayout1.setVisibility(View.GONE);
            holder.frameLayout2.setVisibility(View.VISIBLE);
            holder.Question2.setText(winBuildSends.get(position).getQuestion());
            if(answer != null){
                holder.QuestionValue.setText(""+answer.getAnswer());
            }

        }else {
            holder.frameLayout1.setVisibility(View.VISIBLE);
            holder.frameLayout2.setVisibility(View.GONE);
            holder.Question1.setText(winBuildSends.get(position).getQuestion());

        }
    }

    @Override
    public int getItemCount() {
        return winBuildSends.size();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView Question1,Question2,Question3,Email,Phone;
        Button btnInc,btnDec;
        ToggleButton btnToggle;
        ImageView NewsImage;
        TextView QuestionValue;
        FrameLayout frameLayout1,frameLayout2;
        public DataObjectHolder(View itemView) {
            super(itemView);
            frameLayout1 = (FrameLayout) itemView.findViewById(R.id.frame1);
            frameLayout2 = (FrameLayout) itemView.findViewById(R.id.frame2);

            Question1 = (TextView) itemView.findViewById(R.id.txt_winbuildsend_question1);
            Question2 = (TextView) itemView.findViewById(R.id.txt_winbuildsend_question2);

            btnInc = (Button) itemView.findViewById(R.id.btn_winbuildsend_inc);
            btnInc.setOnClickListener(this);
            btnDec = (Button) itemView.findViewById(R.id.btn_winbuildsend_dec);
            btnDec.setOnClickListener(this);

            QuestionValue = (TextView) itemView.findViewById(R.id.txt_winbuildsend_value);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Answer answer = new Answer();
            if(v.getId() == R.id.btn_winbuildsend_inc){
                int value = Integer.valueOf(QuestionValue.getText().toString());
                value = value + 1;
                QuestionValue.setText(""+value);
                answer.setDisciplePhone(DisciplePhone);
                answer.setQuestionID(winBuildSends.get(getAdapterPosition()).getSerID());
                answer.setAnswer(""+value);
                answer.setSerID(0);
                answer.setBuildStage("WIN");
                DeepLife.myDATABASE.add_updateAnswer(answer);
            }else if(v.getId() == R.id.btn_winbuildsend_dec){
                int value = Integer.valueOf(QuestionValue.getText().toString());
                if(value > 0){
                    value = value - 1;
                    QuestionValue.setText(""+value);
                    answer.setDisciplePhone(DisciplePhone);
                    answer.setQuestionID(winBuildSends.get(getAdapterPosition()).getSerID());
                    answer.setAnswer(""+value);
                    answer.setSerID(0);
                    answer.setBuildStage("WIN");
                    DeepLife.myDATABASE.add_updateAnswer(answer);
                }

            }



        }

        @Override
        public boolean onLongClick(View v) {
            return true;
        }
    }
}
