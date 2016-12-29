package deeplife.gcme.com.deeplife.News;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import deeplife.gcme.com.deeplife.FileManager.FileManager;
import deeplife.gcme.com.deeplife.R;
import deeplife.gcme.com.deeplife.Testimony.TestimonyListAdapter;

/**
 * Created by bengeos on 12/6/16.
 */

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.DataObjectHolder> {
    private List<News> Newses;
    private Context myContext;
    private FileManager myFileManager;


    public NewsListAdapter(List<News> newses, Context myContext) {
        Newses = newses;
        this.myContext = myContext;
        myFileManager = new FileManager(myContext);
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_fragment_item, parent, false);
        NewsListAdapter.DataObjectHolder dataObjectHolder = new NewsListAdapter.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.Title.setText(Newses.get(position).getTitle());
        holder.Content.setText(Newses.get(position).getContent());
        holder.PubDate.setText(Newses.get(position).getPubDate());
        String filename = "news"+Newses.get(position).getSerID()+".png";
        File myFile = myFileManager.getFileAt("News",filename);
        if(myFile.isFile()){
           holder.NewsImage.setImageBitmap(BitmapFactory.decodeFile(myFile.getAbsolutePath()));
        }
    }

    @Override
    public int getItemCount() {
        return Newses.size();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView Title,Content,PubDate;
        ImageView NewsImage;
        public DataObjectHolder(View itemView) {
            super(itemView);
            Title = (TextView) itemView.findViewById(R.id.txt_newsfeed_title);
            Content = (TextView) itemView.findViewById(R.id.txt_newsfeed_content);
            PubDate = (TextView) itemView.findViewById(R.id.txt_newsfeed_pubdate);
            NewsImage = (ImageView) itemView.findViewById(R.id.img_news_image);
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
