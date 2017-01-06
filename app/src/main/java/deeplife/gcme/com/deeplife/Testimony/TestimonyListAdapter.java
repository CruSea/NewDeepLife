package deeplife.gcme.com.deeplife.Testimony;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.R;
import deeplife.gcme.com.deeplife.SyncService.SyncDatabase;

/**
 * Created by bengeos on 12/6/16.
 */

public class TestimonyListAdapter extends RecyclerView.Adapter<TestimonyListAdapter.DataObjectHolder> {

    public static List<Testimony> Testimonies;
    public static Context myContext;
    private static SyncDatabase mySyncDatabase;
    public static AlertDialog.Builder builder;

    public TestimonyListAdapter(Context context,List<Testimony> testimonies) {
        Testimonies = testimonies;
        myContext = context;
        mySyncDatabase = new SyncDatabase();
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.testimony_fragment_item, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.Content.setText(Testimonies.get(position).getContent());
        holder.Person.setText(Testimonies.get(position).getUserName());
        holder.PubDate.setText(Testimonies.get(position).getPubDate());
    }

    @Override
    public int getItemCount() {
        return Testimonies.size();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView Person,Content,PubDate;
        ImageView TestimonyImage;
        public DataObjectHolder(View itemView) {
            super(itemView);
            Content = (TextView) itemView.findViewById(R.id.txt_testimony_content);
            Person = (TextView) itemView.findViewById(R.id.txt_testimony_person);
            PubDate = (TextView) itemView.findViewById(R.id.txt_testimony_time);
            TestimonyImage = (ImageView) itemView.findViewById(R.id.img_testimony_image);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }

        @Override
        public boolean onLongClick(View v) {
            Toast.makeText(myContext,"Long Clicked"+Testimonies.get(getAdapterPosition()).getID(),Toast.LENGTH_LONG).show();
            DeleteTestimonyDialog(Testimonies.get(getAdapterPosition()).getID());
            return true;
        }
    }

    public static void DeleteTestimonyDialog(final int testimony_id) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        DeepLife.myDATABASE.Delete_By_ID(Database.Table_TESTIMONY,testimony_id);
                        TestimonyFragment.UpdateList();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        builder = new AlertDialog.Builder(myContext);
        builder.setTitle(R.string.app_name)
                .setMessage(DeepLife.getContext().getString(R.string.dlg_msg_are_you_sure_delete_x, "Testimony"))
                .setNegativeButton(R.string.dlg_btn_no_dont_delete, dialogClickListener)
                .setPositiveButton(R.string.dlg_btn_ok, dialogClickListener).show();
    }
}
