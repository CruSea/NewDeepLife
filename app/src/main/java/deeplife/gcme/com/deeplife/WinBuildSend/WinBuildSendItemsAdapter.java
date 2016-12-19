package deeplife.gcme.com.deeplife.WinBuildSend;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import deeplife.gcme.com.deeplife.Disciples.Disciple;
import deeplife.gcme.com.deeplife.Disciples.DiscipleListAdapter;
import deeplife.gcme.com.deeplife.R;

/**
 * Created by bengeos on 12/19/16.
 */

public class WinBuildSendItemsAdapter extends RecyclerView.Adapter<WinBuildSendItemsAdapter.DataObjectHolder> {
    private List<WinBuildSend> winBuildSends;
    public static Context myContext;

    public WinBuildSendItemsAdapter(List<WinBuildSend> winBuildSends,Context context) {
        this.winBuildSends = winBuildSends;
        myContext = context;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.win_build_fragment_item1, parent, false);
        WinBuildSendItemsAdapter.DataObjectHolder dataObjectHolder = new WinBuildSendItemsAdapter.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        if(position%2 == 0){
            holder.frameLayout1.setVisibility(View.GONE);
            holder.frameLayout2.setVisibility(View.VISIBLE);
        }else {
            holder.frameLayout1.setVisibility(View.VISIBLE);
            holder.frameLayout2.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return winBuildSends.size();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView FullName,Email,Phone;
        Button btn_WinBuild;
        ImageView NewsImage;
        FrameLayout frameLayout1,frameLayout2;
        public DataObjectHolder(View itemView) {
            super(itemView);
            frameLayout1 = (FrameLayout) itemView.findViewById(R.id.frame1);
            frameLayout2 = (FrameLayout) itemView.findViewById(R.id.frame2);
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
