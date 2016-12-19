package deeplife.gcme.com.deeplife.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bengeos on 12/18/16.
 */

public class GetStartViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> myFragmentList;

    public GetStartViewPagerAdapter(FragmentManager fm) {
        super(fm);
        myFragmentList = new ArrayList<Fragment>();
    }

    public void AddFragent(Fragment fragment){
        myFragmentList.add(fragment);
    }
    @Override
    public Fragment getItem(int position) {
        return myFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return myFragmentList.size();
    }
}
