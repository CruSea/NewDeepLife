package deeplife.gcme.com.deeplife.Models;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import deeplife.gcme.com.deeplife.DeepLife;

/**
 * Created by BENGEOS on 4/5/16.
 */
public class Country {
    private static final String TAG = "Country";
    private int ID, SerID, Code;
    private String ISO, ISO3, Name;

    public Country() {
    }

    public Country(String name) {
        Name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getSerID() {
        return SerID;
    }

    public void setSerID(int serID) {
        SerID = serID;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getISO() {
        return ISO;
    }

    public void setISO(String ISO) {
        this.ISO = ISO;
    }

    public String getISO3() {
        return ISO3;
    }

    public void setISO3(String ISO3) {
        this.ISO3 = ISO3;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public static Country getCountryFromGeoLocation(Context context, GoogleApiClient mGoogleApiClient) {
        Location mLastLocation;

        // If we have permission, then get Location.
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                Log.d(TAG, "Latitude: " + mLastLocation.getLatitude());
                Log.d(TAG, "Longitude: " + mLastLocation.getLongitude());

                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
                    if (addresses.size() > 0) {
                        String countryCode = addresses.get(0).getCountryCode();
                        String countryName = addresses.get(0).getCountryName();

                        Log.d(TAG, "Country Code: " + addresses.get(0).getCountryCode());
                        Log.d(TAG, "Country Name: " + addresses.get(0).getCountryName());

                        return DeepLife.myDATABASE.getCountryByISO(countryCode);
                    }
                } catch (IOException e) {
                    //e.printStackTrace();
                    Log.e(TAG, "Unable to get Country from Geocoder. Error: " + e.getLocalizedMessage());
                    return null;
                }
            } else {
                Log.e(TAG, "mLastLocation is NULL!");
                return null;
            }
        }
        return null;
    }
}
