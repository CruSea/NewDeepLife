package deeplife.gcme.com.deeplife.Disciples;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Models.Logs;
import deeplife.gcme.com.deeplife.R;
import deeplife.gcme.com.deeplife.SyncService.SyncDatabase;
import deeplife.gcme.com.deeplife.Wbs.WbsActivity;

/**
 * Created by bengeos on 12/7/16.
 */

public class DiscipleListAdapter extends RecyclerView.Adapter<DiscipleListAdapter.DataObjectHolder> {
    public static List<Disciple> Disciples;
    public static Context myContext;
    public static AlertDialog.Builder builder;
    public static SyncDatabase mySyncDatabase;

    public DiscipleListAdapter(List<Disciple> disciples, Context myContext) {
        Disciples = disciples;
        this.myContext = myContext;
        mySyncDatabase = new SyncDatabase();
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.disciples_fragment_item, parent, false);
        DiscipleListAdapter.DataObjectHolder dataObjectHolder = new DiscipleListAdapter.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.FullName.setText(Disciples.get(position).getFullName());
        holder.Email.setText(Disciples.get(position).getEmail());
        holder.Phone.setText("+" + Disciples.get(position).getPhone());
        holder.btnWbs.setText(Disciples.get(position).getStage() == Disciple.STAGE.SEND ? R.string.text_send : Disciples.get(position).getStage() == Disciple.STAGE.BUILD ? R.string.text_build : R.string.text_win);
        if (Disciples.get(position).getImagePath() != null) {
            File file = new File(Disciples.get(position).getImagePath());
            if (file.isFile()) {
                holder.DiscipleImage.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            }
        }
    }

    public void ShowDialog(String message) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        break;
                }
            }
        };
        builder = new AlertDialog.Builder(myContext);
        builder.setTitle(R.string.app_name)
                .setMessage(message)
                .setPositiveButton(R.string.dlg_btn_ok, dialogClickListener).show();
    }

    public static void DeleteDiscipleDialog(final int discipleid, final String disciplePhone) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        DeepLife.myDATABASE.Delete_By_ID(Database.Table_DISCIPLES, discipleid);
                        Logs logs = new Logs();
                        logs.setType(Logs.TYPE.REMOVE_DISCIPLE);
                        logs.setTask(Logs.TASK.SEND_LOG);
                        logs.setValue(disciplePhone);
                        mySyncDatabase.AddLog(logs);
                        DisciplesFragment.UpdateList();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        builder = new AlertDialog.Builder(myContext);
        builder.setTitle(R.string.app_name)
                .setMessage(DeepLife.getContext().getString(R.string.dlg_msg_are_you_sure_delete_x, "Disciple"))
                .setNegativeButton(R.string.dlg_btn_no_dont_delete, dialogClickListener)
                .setPositiveButton(R.string.dlg_btn_yes, dialogClickListener).show();
    }

    @Override
    public int getItemCount() {
        return Disciples.size();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView FullName, Email, Phone;
        Button btnWbs;
        ImageView DiscipleImage;

        public DataObjectHolder(View itemView) {
            super(itemView);
            FullName = (TextView) itemView.findViewById(R.id.txt_disciple_name);
            Email = (TextView) itemView.findViewById(R.id.txt_disciple_email);
            Phone = (TextView) itemView.findViewById(R.id.txt_disciple_phone);
            DiscipleImage = (ImageView) itemView.findViewById(R.id.img_disciple_image);
            btnWbs = (Button) itemView.findViewById(R.id.btn_wbs);
            btnWbs.setOnClickListener(this);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_wbs) {
                Intent intent = new Intent(myContext, WbsActivity.class);
                Bundle b = new Bundle();
                b.putString("DisciplePhone", Disciples.get(getAdapterPosition()).getPhone().toString());
                intent.putExtras(b);
                myContext.startActivity(intent);
            } else {
                Intent intent = new Intent(myContext, DiscipleProfileActivity.class);
                Bundle b = new Bundle();
                b.putString("DisciplePhone", Disciples.get(getAdapterPosition()).getPhone().toString());
                intent.putExtras(b);
                myContext.startActivity(intent);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            int discipleid = Disciples.get(getAdapterPosition()).getID();
            String disciplePhone = Disciples.get(getAdapterPosition()).getPhone();
            DeleteDiscipleDialog(discipleid, disciplePhone);
            return true;
        }
    }
}
