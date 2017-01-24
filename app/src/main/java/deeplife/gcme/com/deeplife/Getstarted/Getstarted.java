package deeplife.gcme.com.deeplife.Getstarted;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import deeplife.gcme.com.deeplife.Adapters.GetStartFragmentStateAdapter;
import deeplife.gcme.com.deeplife.Adapters.GetStartViewPagerAdapter;
import deeplife.gcme.com.deeplife.R;

/**
 * Created by bengeos on 12/18/16.
 */

public class Getstarted extends FragmentActivity {
    public ViewPagerAdapter viewPagerAdapter;
    public GetStartViewPagerAdapter viewPagerAdapter1;
    private ViewPager myViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getstarted_page);
        viewPagerAdapter = (ViewPagerAdapter) findViewById(R.id.help_viewpage);
        viewPagerAdapter.setSwipeable(true);
        GetStartFragmentStateAdapter stateAdapter = new GetStartFragmentStateAdapter(this, getSupportFragmentManager());
        stateAdapter.AddFragment(new GetstartedFragment1());
        stateAdapter.AddFragment(new GetstartedFragment2());
        viewPagerAdapter.setAdapter(stateAdapter);
//        myViewPager = (ViewPager) findViewById(R.id.viewpager);
//        viewPagerAdapter1 = new GetStartViewPagerAdapter(getSupportFragmentManager());
//        viewPagerAdapter1.AddFragent(new GetstartedFragment1());
//        viewPagerAdapter1.AddFragent(new GetstartedFragment1());
//        myViewPager.setAdapter(viewPagerAdapter1);


    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }
}
