package deeplife.gcme.com.deeplife.LearningTools;

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

public class LearningToolsListAdpapter extends RecyclerView.Adapter<LearningToolsListAdpapter.DataObjectHolder> {
    private List<LearningTool> LearningTools;
    private Context myContext;

    public LearningToolsListAdpapter(List<LearningTool> learningTools, Context myContext) {
        LearningTools = learningTools;
        this.myContext = myContext;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.learning_fragment_item, parent, false);
        LearningToolsListAdpapter.DataObjectHolder dataObjectHolder = new LearningToolsListAdpapter.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return LearningTools.size();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView Title,Content;
        ImageView LearningImage;
        public DataObjectHolder(View itemView) {
            super(itemView);
            Title = (TextView) itemView.findViewById(R.id.txt_learning_title);
            Content = (TextView) itemView.findViewById(R.id.txt_learning_content);
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
