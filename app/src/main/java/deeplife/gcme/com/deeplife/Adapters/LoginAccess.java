package deeplife.gcme.com.deeplife.Adapters;

import android.content.ContentValues;
import android.util.Log;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Login;
import deeplife.gcme.com.deeplife.Models.User;
import deeplife.gcme.com.deeplife.R;
import deeplife.gcme.com.deeplife.SyncService.SyncDatabase;
import deeplife.gcme.com.deeplife.SyncService.SyncService;
import kotlin.Pair;

/**
 * Created by bengeos on 12/22/16.
 */

public class LoginAccess {
    private User myUser;
    private Fuel myFuel;
    private List<Pair<String, String>> Send_Param;
    private SyncDatabase mySyncDatabase;
    private Gson myParser;

    public LoginAccess(User user) {
        Send_Param = new ArrayList<Pair<String, String>>();
        mySyncDatabase = new SyncDatabase();
        myUser = user;
        myParser = new Gson();

    }

    public User getMyUser() {
        return myUser;
    }

    public void setMyUser(User myUser) {
        this.myUser = myUser;
    }

    private static final String TAG = "LoginAccess";

    public boolean LogInAuthnticate() {
        if (myUser.getEmail() != null) {
            Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.USER_NAME.toString(), myUser.getEmail()));
        } else {
            Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.USER_NAME.toString(), myUser.getPhone()));
        }
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.USER_PASS.toString(), myUser.getPass()));
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.COUNTRY.toString(), myUser.getCountry()));
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.SERVICE.toString(), SyncService.API_SERVICE.LOG_IN.toString()));
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.PARAM.toString(), "[]"));
        try {
            Fuel.post(DeepLife.API_URL, Send_Param).responseString(new Handler<String>() {
                @Override
                public void success(@NotNull Request request, @NotNull Response response, String s) {
                    Log.i(TAG, "Server Request  -> \n" + request.toString());
                    Log.i(TAG, "Server Response -> \n" + s);
                    JSONObject myObject = null;
                    try {
                        myObject = (JSONObject) new JSONTokener(s).nextValue();
                        if (!myObject.isNull(SyncDatabase.ApiResponseKey.RESPONSE.toString())) {
                            mySyncDatabase.ProcessResponse(s);
                            User user = DeepLife.myDATABASE.getMainUser();
                            if (user != null) {
                                user.setPass(myUser.getPass());
                                long state = DeepLife.myDATABASE.updateMainUser(user);
                                User myUser1 = DeepLife.myDATABASE.getMainUser();
                                if (myUser1 != null && state >0) {
                                    Login.GetNextActivity();
                                } else {
                                    Log.w(TAG, "LogInAuthenticate onSuccess(): unable to update Main User");
                                    Login.showDialog(DeepLife.getContext().getString(R.string.dlg_msg_login_failure));
                                }
                            } else {
                                Log.w(TAG, "LogInAuthenticate onSuccess(): failed to get main user");
                                Login.showDialog(DeepLife.getContext().getString(R.string.dlg_msg_login_failure));
                            }
                        } else {
                            Log.w(TAG, "LogInAuthenticate onSuccess(): JSONObject 'Response' is null");
                            Login.showDialog(DeepLife.getContext().getString(R.string.dlg_msg_login_failure));
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "LogInAuthenticate onSuccess(): JSONException");
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(@NotNull Request request, @NotNull Response response, @NotNull FuelError fuelError) {
                    Log.i(TAG, "Server Response Error-> \n" + fuelError);
                }
            });
        } catch (Exception e) {

        }
        return false;
    }

    public boolean GetMetaData() {
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.USER_NAME.toString(), ""));
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.USER_PASS.toString(), ""));
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.COUNTRY.toString(), ""));
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.SERVICE.toString(), SyncService.API_SERVICE.META_DATA.toString()));
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.PARAM.toString(), "[]"));
        Log.i(TAG, "Request Sent to Server (Meta Data): " + Send_Param.toString());
        try {
            myFuel.post(DeepLife.API_URL, Send_Param).responseString(new Handler<String>() {
                @Override
                public void success(@NotNull Request request, @NotNull Response response, String s) {
                    Log.i(TAG, "Server Request (Meta Data): " + request.toString());
                    Log.i(TAG, "Server Response (Meta Data): " + s);
                    JSONObject myObject = null;
                    try {
                        myObject = (JSONObject) new JSONTokener(s).nextValue();
                        if (!myObject.isNull(SyncDatabase.ApiResponseKey.RESPONSE.toString())) {
                            mySyncDatabase.ProcessResponse(s);
                            Login.showDialog(DeepLife.getContext().getString(R.string.dlg_msg_login));
                        } else {
                            Login.showDialog(DeepLife.getContext().getString(R.string.dlg_msg_meta_download_failed));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(@NotNull Request request, @NotNull Response response, @NotNull FuelError fuelError) {
                    Log.i(TAG, "Server Response Error Meta data-> \n" + fuelError);
                    Login.showDialog(DeepLife.getContext().getString(R.string.dlg_msg_meta_download_failed));
                }
            });
        } catch (Exception e) {

        }
        return false;
    }

    public boolean SignupAuthnticate(final User user) {
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.USER_NAME.toString(), user.getEmail()));
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.USER_PASS.toString(), user.getPass()));
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.COUNTRY.toString(), user.getCountry()));
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.SERVICE.toString(), SyncService.API_SERVICE.SIGN_UP.toString()));
        Send_Param.add(new kotlin.Pair<String, String>(SyncService.API_REQUEST.PARAM.toString(), myParser.toJson(user)));
        myFuel.post(DeepLife.API_URL, Send_Param).responseString(new Handler<String>() {
            @Override
            public void success(@NotNull Request request, @NotNull Response response, String s) {
                Log.i(TAG, "Server Request  -> \n" + request.toString());
                Log.i(TAG, "Server Response -> \n" + s);
                JSONObject myObject = null;
                try {
                    myObject = (JSONObject) new JSONTokener(s).nextValue();
                    if (!myObject.isNull(SyncDatabase.ApiResponseKey.RESPONSE.toString())) {
                        mySyncDatabase.ProcessResponse(s);
                        DeepLife.myDATABASE.Delete_All(deeplife.gcme.com.deeplife.Database.Database.Table_USER);
                        ContentValues cv = new ContentValues();
                        cv.put(Database.UserColumn.EMAIL.toString(), user.getEmail());
                        cv.put(Database.UserColumn.PHONE.toString(), user.getPhone());
                        cv.put(Database.UserColumn.PASSWORD.toString(), user.getPass());
                        long state = DeepLife.myDATABASE.insert(Database.Table_USER, cv);
                        if (state > 0) {
                            Login.GetNextActivity();
                        } else {
                            Login.showDialog(DeepLife.getContext().getString(R.string.dlg_msg_login_failure));
                        }
                    } else {
                        Login.showDialog(DeepLife.getContext().getString(R.string.dlg_msg_login_failure));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(@NotNull Request request, @NotNull Response response, @NotNull FuelError fuelError) {
                Log.i(TAG, "Server Response Error-> \n" + fuelError);
            }
        });
        return false;
    }
}
