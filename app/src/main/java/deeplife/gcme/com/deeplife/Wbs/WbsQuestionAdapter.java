package deeplife.gcme.com.deeplife.Wbs;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;

import java.util.List;

import deeplife.gcme.com.deeplife.Disciples.Disciple;
import deeplife.gcme.com.deeplife.R;

/**
 * Created by briggsm on 1/19/17.
 */

public class WbsQuestionAdapter extends ExpandableRecyclerAdapter<WbsQuestion, WbsQuestion, WbsQuestionParentViewHolder, WbsQuestionChildViewHolder> {
    // Remember: Questions can be 1 of 3 types: YESNO, NUMERIC, FOLDER. "Folder" must be treated specially.

    private Context mContext;
    private LayoutInflater mInflater;
    private List<WbsQuestion> mWbsParentQuestions;
    private Disciple mDisciple;

    public WbsQuestionAdapter(Context context, @NonNull List<WbsQuestion> wbsParentQuestions, Disciple disciple) {
        super(wbsParentQuestions);
        mContext = context;
        mWbsParentQuestions = wbsParentQuestions;
        mInflater = LayoutInflater.from(context);
        mDisciple = disciple;
    }

    @UiThread
    @NonNull
    @Override
    public WbsQuestionParentViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View wbsQuestionParentView;
        wbsQuestionParentView = mInflater.inflate(R.layout.wbs_fragment_item1, parentViewGroup, false);
        return new WbsQuestionParentViewHolder(mContext, wbsQuestionParentView, mDisciple);
    }

    @UiThread
    @NonNull
    @Override
    public WbsQuestionChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View wbsQuestionChildView;

        wbsQuestionChildView = mInflater.inflate(R.layout.wbs_fragment_item1, childViewGroup, false);
        wbsQuestionChildView.setPadding(20,0,0,0);  // This is in pixels.
        CardView cardView = (CardView) wbsQuestionChildView.findViewById(R.id.card_view);
        cardView.setCardBackgroundColor(Color.LTGRAY);

        return new WbsQuestionChildViewHolder(mContext, wbsQuestionChildView, mDisciple);
    }

    @UiThread
    @Override
    public void onBindParentViewHolder(@NonNull WbsQuestionParentViewHolder wbsQuestionParentViewHolder, int parentPosition, @NonNull WbsQuestion wbsQuestion) {
        wbsQuestionParentViewHolder.bind(wbsQuestion);
    }

    @UiThread
    @Override
    public void onBindChildViewHolder(@NonNull WbsQuestionChildViewHolder wbsQuestionChildViewHolder, int parentPosition, int childPosition, @NonNull WbsQuestion wbsQuestion) {
        wbsQuestionChildViewHolder.bind(wbsQuestion);
    }
}
