package deeplife.gcme.com.deeplife.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import deeplife.gcme.com.deeplife.Models.Country;
import deeplife.gcme.com.deeplife.R;

/**
 * Created by bengeos on 12/23/16.
 */

public class CountryListAdapter extends ArrayAdapter<Country> {
    private Context myContext;
    private List<Country> myCountries;

    public CountryListAdapter(Context context, int resource, List<Country> countries) {
        super(context, resource, countries);
        myContext = context;
        myCountries = countries;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mySpinner = inflater.inflate(R.layout.login_countries_item, parent, false);
        TextView main_text = (TextView) mySpinner.findViewById(R.id.spinner_text);
        main_text.setText(myCountries.get(position).getName());
        return mySpinner;
    }
}
