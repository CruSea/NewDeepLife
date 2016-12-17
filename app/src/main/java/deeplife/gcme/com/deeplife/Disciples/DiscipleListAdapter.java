package deeplife.gcme.com.deeplife.Disciples;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import deeplife.gcme.com.deeplife.News.NewsListAdapter;
import deeplife.gcme.com.deeplife.R;

/**
 * Created by bengeos on 12/7/16.
 */

public class DiscipleListAdapter extends RecyclerView.Adapter<DiscipleListAdapter.DataObjectHolder> {
    private List<Disciple> Disciples;
    private Context myContext;

    public DiscipleListAdapter(List<Disciple> disciples, Context myContext) {
        Disciples = disciples;
        this.myContext = myContext;
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
        holder.Phone.setText(Disciples.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return Disciples.size();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView FullName,Email,Phone;
        ImageView NewsImage;
        public DataObjectHolder(View itemView) {
            super(itemView);
            FullName = (TextView) itemView.findViewById(R.id.txt_disciple_name);
            Email = (TextView) itemView.findViewById(R.id.txt_disciple_email);
            Phone = (TextView) itemView.findViewById(R.id.txt_disciple_phone);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }

        @Override
        public boolean onLongClick(View v) {
            return true;
        }
    }
}
