package deeplife.gcme.com.deeplife.LearningTools;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.List;

import deeplife.gcme.com.deeplife.R;

/**
 * Created by bengeos on 12/7/16.
 */

public class LearningToolsListAdapter extends RecyclerView.Adapter<LearningToolsListAdapter.DataObjectHolder> {
    private List<LearningTool> LearningTools;
    private Context myContext;

    public LearningToolsListAdapter(List<LearningTool> learningTools, Context myContext) {
        LearningTools = learningTools;
        this.myContext = myContext;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.learning_fragment_item, parent, false);
        LearningToolsListAdapter.DataObjectHolder dataObjectHolder = new LearningToolsListAdapter.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.Title.setText(LearningTools.get(position).getTitle());
        holder.Content.setText(LearningTools.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return LearningTools.size();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView Title, Content;
        ImageView LearningImage;
        VideoView Video;

        public DataObjectHolder(View itemView) {
            super(itemView);
            Title = (TextView) itemView.findViewById(R.id.txt_learning_title);
            Content = (TextView) itemView.findViewById(R.id.txt_learning_content);
            Video = (VideoView) itemView.findViewById(R.id.vdo_learning_video);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.vdo_learning_video) {
                Video.start();
            }
        }

        @Override
        public boolean onLongClick(View v) {
            return true;
        }
    }
}
