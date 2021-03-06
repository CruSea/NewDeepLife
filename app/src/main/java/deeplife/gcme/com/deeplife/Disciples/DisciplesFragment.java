package deeplife.gcme.com.deeplife.Disciples;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.R;
import deeplife.gcme.com.deeplife.SyncService.NewSyncService;

/**
 * Created by bengeos on 12/6/16.
 */

public class DisciplesFragment extends Fragment {

    private static RecyclerView myRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static Context myContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        myContext = getActivity();
        /// ReSync Database here
        if(DeepLife.isNetworkAvailable(myContext)){
            if(DeepLife.isSyncLoaded){
                NewSyncService.StartSync();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.disciples_fragment_page, container, false);
        myRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        myRecyclerView.setLayoutManager(mLayoutManager);
        ArrayList<Disciple> items = DeepLife.myDATABASE.getAllDisciples();
        mAdapter = new DiscipleListAdapter(items, getContext());
        myRecyclerView.setAdapter(mAdapter);
        if(DeepLife.isNetworkAvailable(myContext)){
            if(DeepLife.isSyncLoaded){
                NewSyncService.StartSync();
            }
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.disciple_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.disciple_add) {
//            Toast.makeText(getContext(), "Add New Disciple", Toast.LENGTH_LONG).show();  // briggsm: This toast message is just for debug, right? If not, we need to extract string for translation, but I think this toast message is unnecessary in final app.
            Intent intent = new Intent(getContext(), DiscipleAddActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public static void UpdateList() {
        ArrayList<Disciple> items = DeepLife.myDATABASE.getAllDisciples();
        mAdapter = new DiscipleListAdapter(items, myContext);
        myRecyclerView.setAdapter(mAdapter);
    }
}
