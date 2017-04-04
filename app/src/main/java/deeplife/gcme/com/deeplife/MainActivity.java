package deeplife.gcme.com.deeplife;

import android.Manifest;
import android.app.job.JobScheduler;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import deeplife.gcme.com.deeplife.Adapters.ViewPageAdapter;
import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.Disciples.DisciplesFragment;
import deeplife.gcme.com.deeplife.Home.HomeFragment;
import deeplife.gcme.com.deeplife.LearningTools.LearningFragment;
import deeplife.gcme.com.deeplife.Models.User;
import deeplife.gcme.com.deeplife.News.NewsFragment;
import deeplife.gcme.com.deeplife.Profile.ProfileEditActivity;
import deeplife.gcme.com.deeplife.Profile.ProfileShowActivity;
import deeplife.gcme.com.deeplife.SyncService.NewSyncService;
import deeplife.gcme.com.deeplife.Testimony.TestimonyFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private Toolbar myToolbar;
    private TabLayout myTabLayout;
    private ViewPager myViewPager;
    private TextView UserName;

    private CollapsingToolbarLayout myCollapsingToolbarLayout;
    private LinearLayout myLinearLayout;

    private JobScheduler myJobScheduler;
    private User myUser;


    private static int PERMISSIONS_MULTIPLE_REQUEST = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this,NewSyncService.class);
        startService(intent);
//        myJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        myToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(myToolbar);
        Log.i(TAG, "Main Activity Started");
//
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
        toggle.syncState();
//
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        myViewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPageAdapter.addFragment(new HomeFragment(), getString(R.string.home_fragment_title));
        viewPageAdapter.addFragment(new DisciplesFragment(), getString(R.string.disciples_fragment_title));
        viewPageAdapter.addFragment(new TestimonyFragment(), getString(R.string.testimony_fragment_title));
        viewPageAdapter.addFragment(new NewsFragment(), getString(R.string.news_fragment_title));
        viewPageAdapter.addFragment(new LearningFragment(), getString(R.string.learning_fragment_title));
        myViewPager.setAdapter(viewPageAdapter);

        myTabLayout = (TabLayout) findViewById(R.id.tabs);
        myTabLayout.setupWithViewPager(myViewPager);
        myTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        myTabLayout.setTabMode(TabLayout.MODE_FIXED);
        myTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));

        myTabLayout.getTabAt(0).setIcon(R.drawable.nav_home_icon_grey);
        myTabLayout.getTabAt(1).setIcon(R.drawable.nav_disciple_icon_grey);
        myTabLayout.getTabAt(2).setIcon(R.drawable.nav_testimonials_icon_grey);
        myTabLayout.getTabAt(3).setIcon(R.drawable.nav_news_icon_grey);
        myTabLayout.getTabAt(4).setIcon(R.drawable.nav_learning_icon_grey);
//        UserName = (TextView) findViewById(R.id.txt_main_user_name);

        checkPermissions();
//        myUser = DeepLife.myDATABASE.getMainUser();
//        if(myUser != null && myUser.getFullName() != null){
//            UserName.setText(myUser.getFullName());
//        }

    }


    public void checkPermissions() {
        if((ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)){

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                    }, PERMISSIONS_MULTIPLE_REQUEST);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            myViewPager.setCurrentItem(0, true);
        } else if (id == R.id.nav_testimony) {
            myViewPager.setCurrentItem(2, true);
        } else if (id == R.id.nav_news) {
            myViewPager.setCurrentItem(3, true);
        } else if (id == R.id.nav_disciples) {
            myViewPager.setCurrentItem(1, true);
        } else if (id == R.id.nav_learning) {
            myViewPager.setCurrentItem(4, true);
        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout) {
            DeepLife.myDATABASE.Delete_All(Database.Table_USER);
            DeepLife.myDATABASE.Delete_All(Database.Table_QUESTION_ANSWER);
            DeepLife.myDATABASE.Delete_All(Database.Table_DISCIPLES);
            DeepLife.myDATABASE.Delete_All(Database.Table_NEWSFEED);
            DeepLife.myDATABASE.Delete_All(Database.Table_TESTIMONY);
            DeepLife.myDATABASE.Delete_All(Database.Table_LEARNING);
            Intent intent = new Intent(this, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            User main_user = DeepLife.myDATABASE.getMainUser();
            if(main_user != null){
                Intent intent = new Intent(this, ProfileEditActivity.class);
                Bundle b = new Bundle();
                if(main_user.getFullName()!= null){
                    b.putString("FullName", main_user.getFullName());
                }else {
                    b.putString("FullName", "Unknown");
                }

                if(main_user.getEmail() != null){
                    b.putString("Email", main_user.getEmail());
                }else {
                    b.putString("Email", "Unknown");
                }

                if(main_user.getCountry() != null){
                    b.putString("CountrySerID", main_user.getCountry());
                }else {
                    b.putString("CountrySerID", "Unknown");
                }

                if(main_user.getPhone() !=null){
                    b.putString("Phone", main_user.getPhone());
                }else {
                    b.putString("Phone", "Unknown");
                }

                if(main_user.getGender() != null){
                    b.putString("Gender", main_user.getGender());
                }else {
                    b.putString("Gender", "Unknown");
                }
                intent.putExtras(b);
                startActivity(intent);
            }
            //startActivity(new Intent(this, ProfileEditActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
