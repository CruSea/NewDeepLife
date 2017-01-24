package deeplife.gcme.com.deeplife.Models;

/**
 * Created by Roger on 3/27/16.
 */
public class User {
    private int ID, SerID;
    private String User_Name, User_Pass, Full_Name, User_Email, User_Phone, User_Country, User_Picture, User_Gender, User_Favorite_Scripture;

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

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getFull_Name() {
        return Full_Name;
    }

    public void setFull_Name(String full_Name) {
        Full_Name = full_Name;
    }

    public String getUser_Pass() {
        return User_Pass;
    }

    public void setUser_Pass(String user_Pass) {
        User_Pass = user_Pass;
    }

    public String getUser_Email() {
        return User_Email;
    }

    public void setUser_Email(String user_Email) {
        User_Email = user_Email;
    }

    public String getUser_Phone() {
        return User_Phone;
    }

    public void setUser_Phone(String user_Phone) {
        User_Phone = user_Phone;
    }

    public String getUser_Country() {
        return User_Country;
    }

    public void setUser_Country(String user_Country) {
        User_Country = user_Country;
    }

    public String getUser_Picture() {
        return User_Picture;
    }

    public void setUser_Picture(String user_Picture) {
        User_Picture = user_Picture;
    }

    public String getUser_Gender() {
        return User_Gender;
    }

    public void setUser_Gender(String user_Gender) {
        User_Gender = user_Gender;
    }

    public String getUser_Favorite_Scripture() {
        return User_Favorite_Scripture;
    }

    public void setUser_Favorite_Scripture(String user_Favorite_Scripture) {
        User_Favorite_Scripture = user_Favorite_Scripture;
    }
}
