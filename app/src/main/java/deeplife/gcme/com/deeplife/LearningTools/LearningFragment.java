package deeplife.gcme.com.deeplife.LearningTools;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.R;

/**
 * Created by bengeos on 12/6/16.
 */

public class LearningFragment extends Fragment {
    private static RecyclerView myRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static Context myContext;
    private LearningItemAdapter myAdapter;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.disciples_fragment_page, container, false);
        myRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        myRecyclerView.setLayoutManager(mLayoutManager);
        myContext = getActivity();
        List<LearningTool> learningitems = DeepLife.myDATABASE.getAllLearningTools();
//        Toast.makeText(getContext(),"There are "+items.size(),Toast.LENGTH_LONG).show();
        mAdapter = new LearningToolsListAdapter(learningitems, getContext());
        myRecyclerView.setAdapter(mAdapter);

        myAdapter = new LearningItemAdapter(learningitems,myContext);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(myContext, getWidthDP());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);


        return view;
    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    private int getWidthDP (){
        Resources r = getResources();
        int widthDP = Math.round(r.getDisplayMetrics().widthPixels/(r.getDisplayMetrics().xdpi / r.getDisplayMetrics().DENSITY_DEFAULT));
        return widthDP/170;
    }
}
