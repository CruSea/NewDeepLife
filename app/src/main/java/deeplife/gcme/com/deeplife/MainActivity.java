package deeplife.gcme.com.deeplife;

import android.app.job.JobInfo;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import deeplife.gcme.com.deeplife.Adapters.ViewPageAdapter;
import deeplife.gcme.com.deeplife.Disciples.DisciplesFragment;
import deeplife.gcme.com.deeplife.Home.HomeFragment;
import deeplife.gcme.com.deeplife.LearningTools.LearningFragment;
import deeplife.gcme.com.deeplife.News.NewsFragment;
import deeplife.gcme.com.deeplife.Tabs.SlidingTabLayout;
import deeplife.gcme.com.deeplife.Testimony.TestimonyFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private Toolbar myToolbar;
    private TabLayout myTabLayout;

    private ViewPager myViewPager;

    private CollapsingToolbarLayout myCollapsingToolbarLayout;
    private LinearLayout myLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(myToolbar);
        Log.i(TAG,"Main Activity Started");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        myViewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPageAdapter.addFragment(new HomeFragment(),"Home");
        viewPageAdapter.addFragment(new TestimonyFragment(),"Testimony");
        viewPageAdapter.addFragment(new NewsFragment(),"News");
        viewPageAdapter.addFragment(new DisciplesFragment(),"Disciples");
        viewPageAdapter.addFragment(new LearningFragment(),"Learning");
        myViewPager.setAdapter(viewPageAdapter);

        myTabLayout = (TabLayout) findViewById(R.id.tabs);
        myTabLayout.setupWithViewPager(myViewPager);
        myTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        myTabLayout.setTabMode(TabLayout.MODE_FIXED);
        myTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));

        myTabLayout.getTabAt(0).setIcon(R.drawable.nav_home_icon_grey);
        myTabLayout.getTabAt(1).setIcon(R.drawable.nav_testimonials_icon_grey);
        myTabLayout.getTabAt(2).setIcon(R.drawable.nav_news_icon_grey);
        myTabLayout.getTabAt(3).setIcon(R.drawable.nav_disciple_icon_grey);
        myTabLayout.getTabAt(4).setIcon(R.drawable.nav_learning_icon_grey);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            myViewPager.setCurrentItem(0,true);
        } else if (id == R.id.nav_testimony) {
            myViewPager.setCurrentItem(1,true);
        } else if (id == R.id.nav_news) {
            myViewPager.setCurrentItem(2,true);
        } else if (id == R.id.nav_disciples) {
            myViewPager.setCurrentItem(3,true);
        } else if (id == R.id.nav_learning) {
            myViewPager.setCurrentItem(3,true);
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
