package deeplife.gcme.com.deeplife.Getstarted;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by bengeos on 12/18/16.
 */

public class ViewPagerAdapter extends ViewPager {
    private boolean isSwipeable;
    public ViewPagerAdapter(Context context) {
        super(context);
        this.isSwipeable = true;
    }

    public ViewPagerAdapter(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isSwipeable = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.isSwipeable) {
            return super.onTouchEvent(ev);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.isSwipeable) {
            return super.onInterceptTouchEvent(ev);
        }
        return false;
    }

    public boolean isSwipeable() {
        return isSwipeable;
    }

    public void setSwipeable(boolean swipeable) {
        isSwipeable = swipeable;
    }
}
