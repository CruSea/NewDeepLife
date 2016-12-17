package deeplife.gcme.com.deeplife.Testimony;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import deeplife.gcme.com.deeplife.R;

/**
 * Created by bengeos on 12/6/16.
 */

public class TestimonyListAdapter extends RecyclerView.Adapter<TestimonyListAdapter.DataObjectHolder> {

    public static List<Testimony> Testimonies;
    public static Context myContext;

    public TestimonyListAdapter(Context context,List<Testimony> testimonies) {
        Testimonies = testimonies;
        myContext = context;
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
    }

    @Override
    public int getItemCount() {
        return Testimonies.size();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView Title,Content;
        ImageView NewsImage;
        public DataObjectHolder(View itemView) {
            super(itemView);
            Content = (TextView) itemView.findViewById(R.id.txt_testimony_content);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }

        @Override
        public boolean onLongClick(View v) {
            Toast.makeText(myContext,"Long Clicked"+Testimonies.get(getAdapterPosition()).getID(),Toast.LENGTH_LONG).show();
            return true;
        }
    }
}
