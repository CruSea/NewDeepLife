package deeplife.gcme.com.deeplife.Home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Disciples.Disciple;
import deeplife.gcme.com.deeplife.R;

/**
 * Created by bengeos on 12/6/16.
 */

public class HomeFragment extends Fragment {
    private List<Disciple> myDisciples;
    private TextView DiscipleTree,Win,Build,Send;
    private int DisCount,WinCount,BuildCount,SendCount;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_page,container,false);
        DiscipleTree = (TextView) view.findViewById(R.id.txt_home_disciple);
        Win = (TextView) view.findViewById(R.id.txt_home_win);
        Build = (TextView) view.findViewById(R.id.txt_home_build);
        Send = (TextView) view.findViewById(R.id.txt_home_send);
        myDisciples = DeepLife.myDATABASE.getAllDisciples();
        if(myDisciples != null){
            WinCount = 0;
            BuildCount = 0;
            SendCount = 0;
            for(Disciple dis : myDisciples){
                if(dis.getStage().toLowerCase().equals("win")){
                    WinCount +=1;
                }else if(dis.getStage().toLowerCase().equals("build")){
                    BuildCount +=1;
                }else if(dis.getStage().toLowerCase().equals("send")){
                    SendCount +=1;
                }
            }
            Win.setText(""+WinCount);
            Build.setText(""+BuildCount);
            Send.setText(""+SendCount);
        }
        return view;
    }
}
