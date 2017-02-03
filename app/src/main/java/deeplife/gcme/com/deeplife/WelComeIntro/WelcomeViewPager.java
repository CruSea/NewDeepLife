package deeplife.gcme.com.deeplife.WelComeIntro;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by bengeos on 2/3/17.
 */

public class WelcomeViewPager extends PagerAdapter {
    private LayoutInflater layoutInflater;
    private Context myContext;
    private int[] myLayouts;

    public WelcomeViewPager(Context myContext,int[] layouts) {
        this.myContext = myContext;
        this.myLayouts = layouts;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(myLayouts[position], container, false);
        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return myLayouts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
