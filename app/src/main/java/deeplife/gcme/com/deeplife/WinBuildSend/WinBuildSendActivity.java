package deeplife.gcme.com.deeplife.WinBuildSend;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Disciples.Disciple;
import deeplife.gcme.com.deeplife.Disciples.DiscipleListAdapter;
import deeplife.gcme.com.deeplife.R;

/**
 * Created by bengeos on 12/19/16.
 */

public class WinBuildSendActivity extends AppCompatActivity {
    private static RecyclerView myRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static Context myContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_build_fragment_page);
        myRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(mLayoutManager);
        myContext = this;
        ArrayList<WinBuildSend> items = new ArrayList<WinBuildSend>();
        items.add(new WinBuildSend());
        items.add(new WinBuildSend());
        items.add(new WinBuildSend());
        mAdapter = new WinBuildSendItemsAdapter(items,this);
        myRecyclerView.setAdapter(mAdapter);
    }
}
