package deeplife.gcme.com.deeplife.LearningTools;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

import deeplife.gcme.com.deeplife.R;

/**
 * Created by bengeos on 1/25/17.
 */

public class LearningItemAdapter extends RecyclerView.Adapter<LearningItemAdapter.DataObjectHolder> {
    private static List<LearningTool> myItems;
    private static Context myContext;

    public LearningItemAdapter(List<LearningTool> myItems, Context myContext) {
        this.myItems = myItems;
        if(myItems != null){
            Collections.reverse(myItems);
        }
        this.myContext = myContext;
    }
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.learning_fragment_list_item, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.Title.setText(myItems.get(position).getTitle());
        Glide.with(myContext)
                .load(myItems.get(position).getVideoURL())
                .into(holder.AlbumImage);
    }

    @Override
    public int getItemCount() {
        return myItems.size();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView Title, Content, PubDate;
        private ImageView AlbumImage;


        public DataObjectHolder(View itemView) {
            super(itemView);
            Title = (TextView) itemView.findViewById(R.id.title);
            AlbumImage = (ImageView) itemView.findViewById(R.id.thumbnail);
            Title.setOnClickListener(this);
            AlbumImage.setOnClickListener(this);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.thumbnail){
//                Intent intent = new Intent(myContext, YouTubeActivity.class);
//                Bundle b = new Bundle();
//                b.putString("VideoURL",String.valueOf(myItems.get(getAdapterPosition()).getVideoURL()));
//                intent.putExtras(b);
//                myContext.startActivity(intent);
            }
        }

        @Override
        public boolean onLongClick(View v) {

            return true;
        }
    }

}
