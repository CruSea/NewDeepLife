package deeplife.gcme.com.deeplife.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bengeos on 12/18/16.
 */

public class GetStartFragmentStateAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> myFragmentList;
    private Context myContext;

    public GetStartFragmentStateAdapter(Context context, FragmentManager fm) {
        super(fm);
        myContext = context;
        myFragmentList = new ArrayList<Fragment>();
    }

    @Override
    public Fragment getItem(int position) {
        Toast.makeText(myContext, "Requested Fragment: " + position, Toast.LENGTH_LONG).show();
        return myFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return myFragmentList.size();
    }

    public void AddFragment(Fragment fragment) {
        myFragmentList.add(fragment);
    }
}
