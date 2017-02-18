package deeplife.gcme.com.deeplife.Testimony;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.R;

/**
 * Created by bengeos on 12/6/16.
 */

public class TestimonyFragment extends Fragment {
    private static final String TAG = "TestimonyFragment";

    public static RecyclerView myRecyclerView;
    public static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static Context myContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        myContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.testimony_fragment, container, false);
        myRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        myRecyclerView.setLayoutManager(mLayoutManager);
        myContext = getActivity();
        UpdateList();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.testimony_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.testimony_add) {
            Intent intent = new Intent(getContext(), TestimonyAddActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public static void UpdateList() {
        if (myRecyclerView != null) {
            ArrayList<Testimony> items = DeepLife.myDATABASE.getAllTestimonies();
            Log.d(TAG, "UpdateList: There are '" + items.size() + "' Testimonies");
            mAdapter = new TestimonyListAdapter(myContext, items);
            myRecyclerView.setAdapter(mAdapter);
        }
    }
}
