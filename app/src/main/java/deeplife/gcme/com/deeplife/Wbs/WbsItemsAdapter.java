package deeplife.gcme.com.deeplife.Wbs;

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
import deeplife.gcme.com.deeplife.Disciples.Disciple;
import deeplife.gcme.com.deeplife.Models.Answer;
import deeplife.gcme.com.deeplife.Models.Logs;
import deeplife.gcme.com.deeplife.R;
import deeplife.gcme.com.deeplife.SyncService.SyncDatabase;
import deeplife.gcme.com.deeplife.SyncService.SyncService;

/**
 * Created by bengeos on 12/19/16.
 */

public class WbsItemsAdapter extends RecyclerView.Adapter<WbsItemsAdapter.DataObjectHolder> {
    public static List<WbsQuestion> wbsQuestions;
    public static Context myContext;
    public static String DisciplePhone;
    public static SyncDatabase mySyncDatabase;
    public static Disciple.STAGE BuildStage;
    private int count;

    public WbsItemsAdapter(List<WbsQuestion> wbsQuestions, Context context, String disciplePhone, Disciple.STAGE buildStage) {
        this.wbsQuestions = wbsQuestions;
        this.count = wbsQuestions.size();
        mySyncDatabase = new SyncDatabase();
        BuildStage = buildStage;
        myContext = context;
        DisciplePhone = disciplePhone;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wbs_fragment_item1, parent, false);
        WbsItemsAdapter.DataObjectHolder dataObjectHolder = new WbsItemsAdapter.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        Answer answer = DeepLife.myDATABASE.getAnswerByQuestionIDandDisciplePhone(wbsQuestions.get(position).getSerID(), DisciplePhone);
        if (wbsQuestions.get(position).getType() == WbsQuestion.Type.YESNO) {
            holder.frameLayoutYesNo.setVisibility(View.VISIBLE);
            holder.frameLayoutNumeric.setVisibility(View.GONE);
            holder.frameLayoutFolder.setVisibility(View.GONE);

            if (answer != null) {
                if (answer.getAnswer().equals("YES")) {
                    holder.btnToggleYesNo.setChecked(true);
                } else {
                    holder.btnToggleYesNo.setChecked(false);
                }

            }

        } else if (wbsQuestions.get(position).getType() == WbsQuestion.Type.NUMBER) {
            holder.frameLayoutYesNo.setVisibility(View.GONE);
            holder.frameLayoutNumeric.setVisibility(View.VISIBLE);
            holder.frameLayoutFolder.setVisibility(View.GONE);

            if (answer != null) {
                int value = Integer.valueOf(answer.getAnswer());
                holder.QuestionNumericValue.setText("" + value);
            }
        } else {
            // Folder
            holder.frameLayoutYesNo.setVisibility(View.GONE);
            holder.frameLayoutNumeric.setVisibility(View.GONE);
            holder.frameLayoutFolder.setVisibility(View.VISIBLE);
        }

        holder.QuestionYesNo.setText(wbsQuestions.get(position).getQuestion());
        holder.QuestionNumeric.setText(wbsQuestions.get(position).getQuestion());
        holder.FolderName.setText(wbsQuestions.get(position).getQuestion());
    }

    @Override
    public int getItemCount() {
        return wbsQuestions.size();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView QuestionYesNo, QuestionNumeric, FolderName, Email, Phone;
        Button btnInc, btnDec;
        ToggleButton btnToggleYesNo;
        ImageView NewsImage;
        TextView QuestionNumericValue, ReadNoteYesNo, ReadNoteNumeric;
        FrameLayout frameLayoutYesNo, frameLayoutNumeric, frameLayoutFolder;

        public DataObjectHolder(View itemView) {
            super(itemView);
            frameLayoutYesNo = (FrameLayout) itemView.findViewById(R.id.wbs_frame_yesno_question);
            frameLayoutNumeric = (FrameLayout) itemView.findViewById(R.id.wbs_frame_numeric_question);
            frameLayoutFolder = (FrameLayout) itemView.findViewById(R.id.wbs_frame_folder);

            ReadNoteYesNo = (TextView) itemView.findViewById(R.id.txt_wbs_yesno_readnote);
            ReadNoteYesNo.setOnClickListener(this);
            ReadNoteNumeric = (TextView) itemView.findViewById(R.id.txt_wbs_numeric_readnote);
            ReadNoteNumeric.setOnClickListener(this);

            btnToggleYesNo = (ToggleButton) itemView.findViewById(R.id.tgl_wbs_yesno_state);
            btnToggleYesNo.setOnClickListener(this);

            QuestionYesNo = (TextView) itemView.findViewById(R.id.txt_wbs_yesno_question);
            QuestionNumeric = (TextView) itemView.findViewById(R.id.txt_wbs_numeric_question);
            FolderName = (TextView) itemView.findViewById(R.id.txt_wbs_folder_name);

            btnInc = (Button) itemView.findViewById(R.id.btn_wbs_numeric_inc);
            btnInc.setOnClickListener(this);
            btnDec = (Button) itemView.findViewById(R.id.btn_wbs_numeric_dec);
            btnDec.setOnClickListener(this);

            QuestionNumericValue = (TextView) itemView.findViewById(R.id.txt_wbs_numeric_value);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Answer answer = new Answer();
            answer.setDisciplePhone(DisciplePhone);
            answer.setQuestionID(wbsQuestions.get(getAdapterPosition()).getSerID());
            answer.setSerID(0);
            answer.setBuildStage(BuildStage);
            if (v.getId() == R.id.btn_wbs_numeric_inc) {
                int value = Integer.valueOf(QuestionNumericValue.getText().toString());
                value = value + 1;
                QuestionNumericValue.setText("" + value);
                answer.setAnswer("" + value);
                long x = DeepLife.myDATABASE.add_updateAnswer(answer);
                if (x > 0) {
                    Logs logs = new Logs();
                    logs.setTask(Logs.TASK.SEND_ANSWERS);
                    logs.setType(Logs.TYPE.ADD_NEW_ANSWERS);
                    logs.setService(SyncService.API_SERVICE.ADDNEW_ANSWERS);
                    logs.setValue("" + x);
                    mySyncDatabase.AddLog(logs);
                }
            } else if (v.getId() == R.id.btn_wbs_numeric_dec) {
                int value = Integer.valueOf(QuestionNumericValue.getText().toString());
                if (value > 0) {
                    value = value - 1;
                    QuestionNumericValue.setText("" + value);
                    answer.setAnswer("" + value);
                    long x = DeepLife.myDATABASE.add_updateAnswer(answer);
                    if (x > 0) {
                        Logs logs = new Logs();
                        logs.setTask(Logs.TASK.SEND_ANSWERS);
                        logs.setType(Logs.TYPE.ADD_NEW_ANSWERS);
                        logs.setService(SyncService.API_SERVICE.ADDNEW_ANSWERS);
                        logs.setValue("" + x);
                        mySyncDatabase.AddLog(logs);
                    }
                }

            } else if (v.getId() == R.id.tgl_wbs_yesno_state) {
                if (btnToggleYesNo.isChecked()) {
                    Toast.makeText(myContext, "Checked", Toast.LENGTH_LONG).show();
                    answer.setAnswer("YES");
                    long x = DeepLife.myDATABASE.add_updateAnswer(answer);
                    if (x > 0) {
                        Logs logs = new Logs();
                        logs.setTask(Logs.TASK.SEND_ANSWERS);
                        logs.setType(Logs.TYPE.ADD_NEW_ANSWERS);
                        logs.setService(SyncService.API_SERVICE.ADDNEW_ANSWERS);
                        logs.setValue("" + x);
                        mySyncDatabase.AddLog(logs);
                    }
                } else {
                    Toast.makeText(myContext, "Not Checked", Toast.LENGTH_LONG).show();
                    answer.setAnswer("NO");
                    long x = DeepLife.myDATABASE.add_updateAnswer(answer);
                    if (x > 0) {
                        Logs logs = new Logs();
                        logs.setTask(Logs.TASK.SEND_ANSWERS);
                        logs.setType(Logs.TYPE.ADD_NEW_ANSWERS);
                        logs.setService(SyncService.API_SERVICE.ADDNEW_ANSWERS);
                        logs.setValue("" + x);
                        mySyncDatabase.AddLog(logs);
                    }

                }
            } else if (v.getId() == R.id.txt_wbs_yesno_readnote || v.getId() == R.id.txt_wbs_numeric_readnote) {
                showDialog(wbsQuestions.get(getAdapterPosition()).getDescription());
                Toast.makeText(myContext, "Showing Dialog for Description", Toast.LENGTH_LONG).show();
            }

            WbsActivity.checkStage();

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
                switch (which) {
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
