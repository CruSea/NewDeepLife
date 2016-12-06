package deeplife.gcme.com.deeplife.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bengeos on 12/6/16.
 */

public class ViewPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> myFragmentList;
    private List<String> Titles;

    public ViewPageAdapter(FragmentManager fm) {
        super(fm);
        myFragmentList = new ArrayList<Fragment>();
        Titles = new ArrayList<String>();
    }
    public void addFragment(Fragment fragment,String title){
        myFragmentList.add(fragment);
        Titles.add(title);
    }
    @Override
    public Fragment getItem(int position) {
        return myFragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles.get(position);
    }

    @Override
    public int getCount() {
        return myFragmentList.size();
    }
}
