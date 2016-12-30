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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Login;
import deeplife.gcme.com.deeplife.Models.User;
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

    private static String TAG = "Login";
    public boolean LogInAuthnticate(){
        if(myUser.getUser_Email()!=null){
            Send_Param.add(new kotlin.Pair<String, String>("User_Name", myUser.getUser_Email()));
        }else {
            Send_Param.add(new kotlin.Pair<String, String>("User_Name", myUser.getUser_Phone()));
        }
        Send_Param.add(new kotlin.Pair<String, String>("User_Pass", myUser.getUser_Pass()));
        Send_Param.add(new kotlin.Pair<String, String>("Country", myUser.getUser_Country()));
        Send_Param.add(new kotlin.Pair<String, String>("Service", "Log_In"));
        Send_Param.add(new kotlin.Pair<String, String>("Param", "[]"));

        try{
            Fuel.post(DeepLife.API_URL,Send_Param).responseString(new Handler<String>() {
                @Override
                public void success(@NotNull Request request, @NotNull Response response, String s) {
                    Log.i(TAG, "Server Request  -> \n" + request.toString());
                    Log.i(TAG, "Server Response -> \n" + s);
                    JSONObject myObject = null;
                    try {
                        myObject = (JSONObject) new JSONTokener(s).nextValue();
                        if (!myObject.isNull("Response")) {
                            mySyncDatabase.ProcessResponse(s);
                            User user = DeepLife.myDATABASE.getMainUser();
                            if(user != null){
                                user.setUser_Pass(myUser.getUser_Pass());
                                long state = DeepLife.myDATABASE.updateMainUser(user);
                                User myUser1 = DeepLife.myDATABASE.getMainUser();
                                if(state>0){
                                    Login.GetNextActivity();
                                }else {
                                    Login.DialogState(0);
                                    Login.showDialog("Unable to login now! please retry again!");
                                }
                            }else {
                                Login.DialogState(0);
                                Login.showDialog("Unable to login now! please retry again!");
                            }
                        }else {
                            Login.DialogState(0);
                            Login.showDialog("Unable to login now! please retry again!");
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
        }catch (Exception e){

        }
        return false;
    }
    public boolean GetMetaData(){
        Send_Param.add(new kotlin.Pair<String, String>("User_Name", ""));
        Send_Param.add(new kotlin.Pair<String, String>("User_Pass", ""));
        Send_Param.add(new kotlin.Pair<String, String>("Country", ""));
        Send_Param.add(new kotlin.Pair<String, String>("Service", "Meta_Data"));
        Send_Param.add(new kotlin.Pair<String, String>("Param", "[]"));
        try{
            myFuel.post(DeepLife.API_URL,Send_Param).responseString(new Handler<String>() {
                @Override
                public void success(@NotNull Request request, @NotNull Response response, String s) {
                    Log.i(TAG, "Server Request  -> \n" + request.toString());
                    Log.i(TAG, "Server Response -> \n" + s);
                    JSONObject myObject = null;
                    try {
                        myObject = (JSONObject) new JSONTokener(s).nextValue();
                        if (!myObject.isNull("Response")) {
                            mySyncDatabase.ProcessResponse(s);
                            Login.DialogState(0);
                            Login.showDialog("Please Enter your Phone or Email and Password to login");
                        }else {
                            Login.showDialog("Unable to download all the necessary files! please retry again!");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void failure(@NotNull Request request, @NotNull Response response, @NotNull FuelError fuelError) {
                    Log.i(TAG, "Server Response Error-> \n" + fuelError);
                    Login.showDialog("Unable to download all the necessary files! please retry again!");
                }
            });
        }catch (Exception e){

        }
        return false;
    }

    public boolean SignupAuthnticate(final User user){
        Send_Param.add(new kotlin.Pair<String, String>("User_Name", user.getUser_Email()));
        Send_Param.add(new kotlin.Pair<String, String>("User_Pass", user.getUser_Pass()));
        Send_Param.add(new kotlin.Pair<String, String>("Country", user.getUser_Country()));
        Send_Param.add(new kotlin.Pair<String, String>("Service", "Sign_Up"));
        Send_Param.add(new kotlin.Pair<String, String>("Param", myParser.toJson(user)));
        myFuel.post(DeepLife.API_URL,Send_Param).responseString(new Handler<String>() {
            @Override
            public void success(@NotNull Request request, @NotNull Response response, String s) {
                Log.i(TAG, "Server Request  -> \n" + request.toString());
                Log.i(TAG, "Server Response -> \n" + s);
                JSONObject myObject = null;
                try {
                    myObject = (JSONObject) new JSONTokener(s).nextValue();
                    if (!myObject.isNull("Response")) {
                        mySyncDatabase.ProcessResponse(s);
                        DeepLife.myDATABASE.Delete_All(deeplife.gcme.com.deeplife.Database.Database.Table_USER);
                        ContentValues cv = new ContentValues();
                        cv.put(Database.USER_FIELDS[1], user.getUser_Email());
                        cv.put(Database.USER_FIELDS[2], user.getUser_Phone());
                        cv.put(Database.USER_FIELDS[3], user.getUser_Pass());
                        long state = DeepLife.myDATABASE.insert(Database.Table_USER,cv);
                        if(state>0){
                            Login.GetNextActivity();
                        }else {
                            Login.DialogState(0);
                            Login.showDialog("Unable to login now! please retry again!");
                        }
                    }else {
                        Login.DialogState(0);
                        Login.showDialog("Unable to login now! please retry again with correct Phone or Email and Password!");
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
