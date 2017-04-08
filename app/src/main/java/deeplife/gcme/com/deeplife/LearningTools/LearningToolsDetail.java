package deeplife.gcme.com.deeplife.LearningTools;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.R;

/**
 * Created by bengeos on 2/20/17.
 */

public class LearningToolsDetail extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{

    private YouTubePlayerView youTubeView;
    private String VIDEO_URL;
    private YouTubePlayer player;
    private int RECOVERY_DIALOG_REQUEST = 11;
    private final static String reg = "(?:youtube(?:-nocookie)?\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:v|e(?:mbed)?)\\/|\\S*?[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})";
    private Context myContext;
    private RecyclerView recyclerView;
    private LearningItemAdapter myAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.learning_detail_fragment);
        VIDEO_URL = getIntent().getExtras().getString("VideoURL");
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(DeepLife.YOUTUBE_DEVELOPER_API_KEY, this);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(myContext);

        myContext = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                ChannelsDialog();
            }
        });
        List<LearningTool> learningitems = DeepLife.myDATABASE.getAllLearningTools();
        myAdapter = new LearningItemAdapter(learningitems,myContext);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(myContext, getWidthDP());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            this.player = youTubePlayer;
            String str = getVideoId(VIDEO_URL);
            if(str != null){
                youTubePlayer.cueVideo(str);
            }
            // Hiding player controls
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        }
    }
    public static String getVideoId(String videoUrl) {
        if (videoUrl == null || videoUrl.trim().length() <= 0)
            return null;
        Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(videoUrl);
        if (matcher.find())
            return matcher.group(1);
        return null;
    }
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
//            String errorMessage = String.format(getString(R.string.error_player), youTubeInitializationResult.toString());
            Toast.makeText(this, youTubeInitializationResult.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(DeepLife.YOUTUBE_DEVELOPER_API_KEY, this);
        }
    }
    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    public void ChannelsDialog(){
        final AlertDialog myAlertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
        builder.setIcon(R.drawable.logoicon_xhdpi);
        LayoutInflater layoutInflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.learning_fragment_category_fragment,null);
        ListView myListView = (ListView) view.findViewById(R.id.lst_channels);
//        final String[] channels = App.myDatabase.getAllChannels();
        final String[] channels = {"Sample Channel 1","Sample Channel 2","Sample Channel 3"};
        if(channels != null){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(myContext,android.R.layout.simple_list_item_1,channels);
            myListView.setAdapter(adapter);
            builder.setTitle(R.string.learning_category);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setView(view);
            myAlertDialog = builder.show();
            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    myAlertDialog.dismiss();
                }
            });
        }

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
