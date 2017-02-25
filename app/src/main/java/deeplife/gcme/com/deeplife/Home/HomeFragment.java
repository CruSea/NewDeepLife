package deeplife.gcme.com.deeplife.Home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Disciples.Disciple;
import deeplife.gcme.com.deeplife.Models.DiscipleTreeCount;
import deeplife.gcme.com.deeplife.R;
import deeplife.gcme.com.deeplife.SyncService.NewSyncService;

/**
 * Created by bengeos on 12/6/16.
 */

public class HomeFragment extends Fragment {
    private List<Disciple> myDisciples;
    private TextView DiscipleTree, Win, Build, Send;
    private int DisCount, WinCount, BuildCount, SendCount;
    private Context myContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_page, container, false);
        DiscipleTree = (TextView) view.findViewById(R.id.txt_home_disciple);
        Win = (TextView) view.findViewById(R.id.txt_home_win);
        Build = (TextView) view.findViewById(R.id.txt_home_build);
        Send = (TextView) view.findViewById(R.id.txt_home_send);
        WinCount = 0;
        BuildCount = 0;
        SendCount = 0;
        DisCount = 0;
        myContext = getContext();
        if(DeepLife.isNetworkAvailable(myContext)){
            if(DeepLife.isSyncLoaded){
                NewSyncService.StartSync();
            }
        }

        DiscipleTreeCount discipleTreeCount = DeepLife.myDATABASE.getDiscipleTreeCount();
        if (discipleTreeCount != null) {
            DisCount = discipleTreeCount.getCount();
        }
        myDisciples = DeepLife.myDATABASE.getAllDisciples();
        if (myDisciples != null) {

            for (Disciple dis : myDisciples) {
                if (dis.getStage() == Disciple.STAGE.WIN) {
                    WinCount += 1;
                } else if (dis.getStage() == Disciple.STAGE.BUILD) {
                    BuildCount += 1;
                } else if (dis.getStage() == Disciple.STAGE.SEND) {
                    SendCount += 1;
                }
            }
        }
        Win.setText("" + WinCount);
        Build.setText("" + BuildCount);
        Send.setText("" + SendCount);
        DiscipleTree.setText("" + DisCount);
        return view;
    }
}
