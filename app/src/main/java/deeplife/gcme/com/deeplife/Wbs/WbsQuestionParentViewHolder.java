package deeplife.gcme.com.deeplife.Wbs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;

import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Disciples.Disciple;
import deeplife.gcme.com.deeplife.Models.Answer;
import deeplife.gcme.com.deeplife.Models.Logs;
import deeplife.gcme.com.deeplife.R;
import deeplife.gcme.com.deeplife.SyncService.SyncDatabase;
import deeplife.gcme.com.deeplife.SyncService.SyncService;


/**
 * Created by briggsm on 1/19/17.
 */

public class WbsQuestionParentViewHolder extends ParentViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "WbsQuestionParentViewHo";

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
        itemView.setOnLongClickListener(this); // ??? briggsm: Why?

        mContext = context;
        mySyncDatabase = new SyncDatabase();
        mDisciple = disciple;

    }

    public void bind(@NonNull WbsQuestion wbsQuestion) {
        Answer answer;
        switch (wbsQuestion.getType()) {
            case YESNO:
                frameLayoutYesNo.setVisibility(View.VISIBLE);
                frameLayoutNumeric.setVisibility(View.GONE);
                frameLayoutFolder.setVisibility(View.GONE);
                QuestionYesNo.setText(wbsQuestion.getQuestion());
                answer = DeepLife.myDATABASE.getAnswerByQuestionID(wbsQuestion.getSerID());
                btnToggleYesNo.setChecked(answer == null ? false : answer.getAnswer().equalsIgnoreCase("yes"));
                break;
            case NUMBER:
                frameLayoutYesNo.setVisibility(View.GONE);
                frameLayoutNumeric.setVisibility(View.VISIBLE);
                frameLayoutFolder.setVisibility(View.GONE);
                QuestionNumeric.setText(wbsQuestion.getQuestion());
                answer = DeepLife.myDATABASE.getAnswerByQuestionID(wbsQuestion.getSerID());
                QuestionNumericValue.setText(answer == null ? "0" : answer.getAnswer());
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
        Answer answer = new Answer();
        answer.setDisciplePhone(mDisciple.getPhone());
        answer.setQuestionID(mWbsQuestion.getSerID());
        answer.setSerID(0);
        answer.setBuildStage(Disciple.STAGE.WIN); // briggsm: !!!!!!!!!!!!!!!!!! Just doing this for now, so it's not null (until we take it out of DB all together). !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if (v.getId() == R.id.btn_wbs_numeric_inc) {
            int value = Integer.valueOf(QuestionNumericValue.getText().toString());
            value = value + 1;
            QuestionNumericValue.setText("" + value);
            answer.setAnswer("" + value);
            long modifiedRowId = DeepLife.myDATABASE.add_updateAnswer(answer);
            if (modifiedRowId > 0) {
                Logs logs = new Logs();
                logs.setTask(Logs.TASK.SEND_ANSWERS);
                logs.setType(Logs.TYPE.ADD_NEW_ANSWERS);
                logs.setService(SyncService.API_SERVICE.ADDNEW_ANSWERS);
                logs.setValue("" + modifiedRowId);
                mySyncDatabase.AddLog(logs);
            }
        } else if (v.getId() == R.id.btn_wbs_numeric_dec) {
            int value = Integer.valueOf(QuestionNumericValue.getText().toString());
            if (value > 0) {
                value = value - 1;
                QuestionNumericValue.setText("" + value);
                answer.setAnswer("" + value);
                long modifiedRowId = DeepLife.myDATABASE.add_updateAnswer(answer);
                if (modifiedRowId > 0) {
                    Logs logs = new Logs();
                    logs.setTask(Logs.TASK.SEND_ANSWERS);
                    logs.setType(Logs.TYPE.ADD_NEW_ANSWERS);
                    logs.setService(SyncService.API_SERVICE.ADDNEW_ANSWERS);
                    logs.setValue("" + modifiedRowId);
                    mySyncDatabase.AddLog(logs);
                }
            }

        } else if (v.getId() == R.id.tgl_wbs_yesno_state) {
            if (btnToggleYesNo.isChecked()) {
                Log.d(TAG, "onClick: Checked");
                answer.setAnswer("YES");
                long modifiedRowId = DeepLife.myDATABASE.add_updateAnswer(answer);
                if (modifiedRowId > 0) {
                    Logs logs = new Logs();
                    logs.setTask(Logs.TASK.SEND_ANSWERS);
                    logs.setType(Logs.TYPE.ADD_NEW_ANSWERS);
                    logs.setService(SyncService.API_SERVICE.ADDNEW_ANSWERS);
                    logs.setValue("" + modifiedRowId);
                    mySyncDatabase.AddLog(logs);
                }
            } else {
                Log.d(TAG, "onClick: Un-checked");
                answer.setAnswer("NO");
                long modifiedRowId = DeepLife.myDATABASE.add_updateAnswer(answer);
                if (modifiedRowId > 0) {
                    Logs logs = new Logs();
                    logs.setTask(Logs.TASK.SEND_ANSWERS);
                    logs.setType(Logs.TYPE.ADD_NEW_ANSWERS);
                    logs.setService(SyncService.API_SERVICE.ADDNEW_ANSWERS);
                    logs.setValue("" + modifiedRowId);
                    mySyncDatabase.AddLog(logs);
                }

            }
        } else if (v.getId() == R.id.txt_wbs_yesno_readnote || v.getId() == R.id.txt_wbs_numeric_readnote) {
            showDialog(mWbsQuestion.getDescription());
        } else {
            // Must be a folder - so pass the click up to the Super class.
            super.onClick(v);
        }

//        WbsActivity.checkStage();
        WbsActivity.updateCurrentStageButton();

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
