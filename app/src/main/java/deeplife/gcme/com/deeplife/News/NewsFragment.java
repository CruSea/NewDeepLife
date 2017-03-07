package deeplife.gcme.com.deeplife.News;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.FileManager.FileDownloader;
import deeplife.gcme.com.deeplife.FileManager.FileManager;
import deeplife.gcme.com.deeplife.R;

/**
 * Created by bengeos on 12/6/16.
 */

public class NewsFragment extends Fragment {
    private static RecyclerView myRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static Context myContext;
    private static FileDownloader myFileDownloader;
    private static FileManager myFileManager;
    private static final String TAG = "NewsFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<News> myNews = DeepLife.myDATABASE.getAllNews();
        Log.i(TAG, "There are " + myNews.size() + " News");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment_page, container, false);
        myRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        myRecyclerView.setLayoutManager(mLayoutManager);
        myContext = getActivity();
        ArrayList<News> items = DeepLife.myDATABASE.getAllNews();
        mAdapter = new NewsListAdapter(items, getContext());
        myRecyclerView.setAdapter(mAdapter);
        return view;
    }
}
