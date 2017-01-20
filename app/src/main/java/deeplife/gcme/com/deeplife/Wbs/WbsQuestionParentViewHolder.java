package deeplife.gcme.com.deeplife.Wbs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;

import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Disciples.Disciple;
import deeplife.gcme.com.deeplife.Models.Answer;
import deeplife.gcme.com.deeplife.Models.Logs;
import deeplife.gcme.com.deeplife.R;
import deeplife.gcme.com.deeplife.SyncService.SyncDatabase;
import deeplife.gcme.com.deeplife.SyncService.SyncService;

import static deeplife.gcme.com.deeplife.Wbs.WbsItemsAdapter.wbsQuestions;


/**
 * Created by briggsm on 1/19/17.
 */

public class WbsQuestionParentViewHolder extends ParentViewHolder implements View.OnClickListener, View.OnLongClickListener {

    TextView QuestionYesNo, QuestionNumeric, FolderName, Email, Phone;
    Button btnInc, btnDec;
    ToggleButton btnToggleYesNo;
    ImageView NewsImage;
    TextView QuestionNumericValue, ReadNoteYesNo, ReadNoteNumeric;
    FrameLayout frameLayoutYesNo, frameLayoutNumeric, frameLayoutFolder;

    Context mContext;
    SyncDatabase mySyncDatabase;
    Disciple mDisciple;
    WbsQuestion mWbsQuestion; // Gets set on 'bind'


    public WbsQuestionParentViewHolder(Context context, @NonNull View itemView, Disciple disciple) {
        super(itemView);

        frameLayoutYesNo = (FrameLayout) itemView.findViewById(R.id.wbs_frame_yesno_question);
        frameLayoutNumeric = (FrameLayout) itemView.findViewById(R.id.wbs_frame_numeric_question);
        frameLayoutFolder = (FrameLayout) itemView.findViewById(R.id.wbs_frame_folder);

        ReadNoteYesNo = (TextView) itemView.findViewById(R.id.txt_wbs_yesno_readnote);
        ReadNoteNumeric = (TextView) itemView.findViewById(R.id.txt_wbs_numeric_readnote);

        btnToggleYesNo = (ToggleButton) itemView.findViewById(R.id.tgl_wbs_yesno_state);

        QuestionYesNo = (TextView) itemView.findViewById(R.id.txt_wbs_yesno_question);
        QuestionNumeric = (TextView) itemView.findViewById(R.id.txt_wbs_numeric_question);
        FolderName = (TextView) itemView.findViewById(R.id.txt_wbs_folder_name);

        btnInc = (Button) itemView.findViewById(R.id.btn_wbs_numeric_inc);
        btnDec = (Button) itemView.findViewById(R.id.btn_wbs_numeric_dec);

        QuestionNumericValue = (TextView) itemView.findViewById(R.id.txt_wbs_numeric_value);

        mContext = context;
        mySyncDatabase = new SyncDatabase();
        mDisciple = disciple;

    }

    public void bind(@NonNull WbsQuestion wbsQuestion) {
        switch (wbsQuestion.getType()) {
            case YESNO:
                frameLayoutYesNo.setVisibility(View.VISIBLE);
                frameLayoutNumeric.setVisibility(View.GONE);
                frameLayoutFolder.setVisibility(View.GONE);
                QuestionYesNo.setText(wbsQuestion.getQuestion());
                break;
            case NUMBER:
                frameLayoutYesNo.setVisibility(View.GONE);
                frameLayoutNumeric.setVisibility(View.VISIBLE);
                frameLayoutFolder.setVisibility(View.GONE);
                QuestionNumeric.setText(wbsQuestion.getQuestion());
                break;
            default:  // Folder
                frameLayoutYesNo.setVisibility(View.GONE);
                frameLayoutNumeric.setVisibility(View.GONE);
                frameLayoutFolder.setVisibility(View.VISIBLE);
                FolderName.setText(wbsQuestion.getQuestion());
                break;
        }
        mWbsQuestion = wbsQuestion;
    }

    @Override
    public void onClick(View v) {
        //v.findViewById(R.id.wbs_frame_folder)

        Answer answer = new Answer();
        //answer.setDisciplePhone(disciplePhone);
        answer.setDisciplePhone(mDisciple.getPhone());
        //answer.setQuestionID(wbsQuestions.get(getAdapterPosition()).getSerID());
        answer.setQuestionID(mWbsQuestion.getSerID());
        answer.setSerID(0);
        //answer.setBuildStage(buildStage); // briggsm: Why do we need "stage" for an answer??? If "really" needed, can't we just determine it from the "Question's ServerID" field in the Answer???
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
                Toast.makeText(mContext, "Checked", Toast.LENGTH_LONG).show();
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
                Toast.makeText(mContext, "Not Checked", Toast.LENGTH_LONG).show();
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
            Toast.makeText(mContext, "Showing Dialog for Description", Toast.LENGTH_LONG).show();
        } else {
            // ??? folder ???
            super.onClick(v);
        }

        WbsActivity.checkStage();

    }

    @Override
    public boolean onLongClick(View v) {
        return true;
    }

    private void showDialog(final String message) {
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
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(message)
                .setPositiveButton(R.string.dlg_btn_ok, dialogClickListener)
                .show();
    }
}
