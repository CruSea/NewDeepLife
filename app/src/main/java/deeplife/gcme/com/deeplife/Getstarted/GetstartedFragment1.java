package deeplife.gcme.com.deeplife.Getstarted;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import deeplife.gcme.com.deeplife.R;

/**
 * Created by bengeos on 12/18/16.
 */

public class GetstartedFragment1 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.step_one_message, null);
        return view;
    }
}
