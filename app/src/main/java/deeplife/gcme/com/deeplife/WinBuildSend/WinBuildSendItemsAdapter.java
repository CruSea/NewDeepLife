package deeplife.gcme.com.deeplife.WinBuildSend;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;

import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Models.Answer;
import deeplife.gcme.com.deeplife.Models.Logs;
import deeplife.gcme.com.deeplife.R;
import deeplife.gcme.com.deeplife.SyncService.SyncDatabase;
import deeplife.gcme.com.deeplife.SyncService.SyncService;

/**
 * Created by bengeos on 12/19/16.
 */

public class WinBuildSendItemsAdapter extends RecyclerView.Adapter<WinBuildSendItemsAdapter.DataObjectHolder> {
    public static List<WinBuildSendQuestion> winBuildSends;
    public static Context myContext;
    public static String DisciplePhone;
    public static SyncDatabase mySyncDatabase;
    public static String BuildStage;
    private int count;

    public WinBuildSendItemsAdapter(List<WinBuildSendQuestion> winBuildSends, Context context, String disciplePhone, String buildStage) {
        this.winBuildSends = winBuildSends;
        this.count = winBuildSends.size();
        mySyncDatabase = new SyncDatabase();
        BuildStage = buildStage;
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
        if(winBuildSends.get(position).getType().equals("YESNO")){
            holder.frameLayout1.setVisibility(View.VISIBLE);
            holder.frameLayout2.setVisibility(View.GONE);
            holder.frameLayout3.setVisibility(View.GONE);

            if(answer != null){
                if(answer.getAnswer().equals("YES")){
                    holder.btnToggle.setChecked(true);
                }else {
                    holder.btnToggle.setChecked(false);
                }

            }

        }else if(winBuildSends.get(position).getType().equals("NUMBER")){
            holder.frameLayout1.setVisibility(View.GONE);
            holder.frameLayout2.setVisibility(View.VISIBLE);
            holder.frameLayout3.setVisibility(View.GONE);

            if(answer != null){
                int value = Integer.valueOf(answer.getAnswer());
                holder.QuestionValue.setText(""+value);
            }
        }else {
            holder.frameLayout1.setVisibility(View.GONE);
            holder.frameLayout2.setVisibility(View.GONE);
            holder.frameLayout3.setVisibility(View.VISIBLE);
        }

        holder.Question1.setText(winBuildSends.get(position).getQuestion());
        holder.Question2.setText(winBuildSends.get(position).getQuestion());
        holder.Question3.setText(winBuildSends.get(position).getQuestion());
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
        TextView QuestionValue,ReadNote1,ReadNote2;
        FrameLayout frameLayout1,frameLayout2,frameLayout3;
        public DataObjectHolder(View itemView) {
            super(itemView);
            frameLayout1 = (FrameLayout) itemView.findViewById(R.id.frame1);
            frameLayout2 = (FrameLayout) itemView.findViewById(R.id.frame2);
            frameLayout3 = (FrameLayout) itemView.findViewById(R.id.frame3);

            ReadNote1 = (TextView) itemView.findViewById(R.id.txt_readnote1);
            ReadNote1.setOnClickListener(this);
            ReadNote2 = (TextView) itemView.findViewById(R.id.txt_readnote2);
            ReadNote2.setOnClickListener(this);

            btnToggle = (ToggleButton) itemView.findViewById(R.id.tgl_winbuildsend_state);
            btnToggle.setOnClickListener(this);

            Question1 = (TextView) itemView.findViewById(R.id.txt_winbuildsend_question1);
            Question2 = (TextView) itemView.findViewById(R.id.txt_winbuildsend_question2);
            Question3 = (TextView) itemView.findViewById(R.id.txt_winbuildsend_question3);

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
            answer.setDisciplePhone(DisciplePhone);
            answer.setQuestionID(winBuildSends.get(getAdapterPosition()).getSerID());
            answer.setSerID(0);
            answer.setBuildStage(BuildStage);
            if(v.getId() == R.id.btn_winbuildsend_inc){
                int value = Integer.valueOf(QuestionValue.getText().toString());
                value = value + 1;
                QuestionValue.setText(""+value);
                answer.setAnswer(""+value);
                long x = DeepLife.myDATABASE.add_updateAnswer(answer);
                if(x>0){
                    Logs logs = new Logs();
                    logs.setTask(SyncService.Sync_Tasks[11]);
                    logs.setService(SyncService.Sync_Tasks[12]);
                    logs.setValue(""+x);
                    mySyncDatabase.AddLog(logs);
                }
            }else if(v.getId() == R.id.btn_winbuildsend_dec){
                int value = Integer.valueOf(QuestionValue.getText().toString());
                if(value > 0){
                    value = value - 1;
                    QuestionValue.setText(""+value);
                    answer.setAnswer(""+value);
                    long x = DeepLife.myDATABASE.add_updateAnswer(answer);
                    if(x>0){
                        Logs logs = new Logs();
                        logs.setTask(SyncService.Sync_Tasks[11]);
                        logs.setService(SyncService.Sync_Tasks[12]);
                        logs.setValue(""+x);
                        mySyncDatabase.AddLog(logs);
                    }
                }

            }else if(v.getId() == R.id.tgl_winbuildsend_state){
                if(btnToggle.isChecked()){
                    Toast.makeText(myContext,"Checked",Toast.LENGTH_LONG).show();
                    answer.setAnswer("YES");
                    long x = DeepLife.myDATABASE.add_updateAnswer(answer);
                    if(x>0){
                        Logs logs = new Logs();
                        logs.setTask(SyncService.Sync_Tasks[11]);
                        logs.setService(SyncService.Sync_Tasks[12]);
                        logs.setValue(""+x);
                        mySyncDatabase.AddLog(logs);
                    }
                }else {
                    Toast.makeText(myContext,"Not Checked",Toast.LENGTH_LONG).show();
                    answer.setAnswer("NO");
                    long x = DeepLife.myDATABASE.add_updateAnswer(answer);
                    if(x>0){
                        Logs logs = new Logs();
                        logs.setTask(SyncService.Sync_Tasks[11]);
                        logs.setService(SyncService.Sync_Tasks[12]);
                        logs.setValue(""+x);
                        mySyncDatabase.AddLog(logs);
                    }

                }
            }else if(v.getId() == R.id.txt_readnote1 || v.getId() == R.id.txt_readnote2){
                showDialog(winBuildSends.get(getAdapterPosition()).getDescription());
                Toast.makeText(myContext,"Showing Dialog for Description",Toast.LENGTH_LONG).show();
            }

            WinBuildSendActivity.checkStage();

        }

        @Override
        public boolean onLongClick(View v) {
            return true;
        }
    }
    public static void showDialog(final String message) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
        builder.setMessage(message)
                .setPositiveButton(R.string.dlg_btn_ok, dialogClickListener)
                .show();
    }
}
