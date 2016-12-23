package deeplife.gcme.com.deeplife.Models;

/**
 * Created by Roger on 3/27/16.
 */
public class User {
    private String id,UserName,UserPass, UserEmail,UserPhone,UserCountry,UserPicture,UserGender,UserFavorite_Scripture;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserPass() {
        return UserPass;
    }

    public void setUserPass(String userPass) {
        UserPass = userPass;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getUserCountry() {
        return UserCountry;
    }

    public void setUserCountry(String userCountry) {
        UserCountry = userCountry;
    }

    public String getUserPicture() {
        return UserPicture;
    }

    public void setUserPicture(String userPicture) {
        UserPicture = userPicture;
    }

    public String getUserGender() {
        return UserGender;
    }

    public void setUserGender(String userGender) {
        UserGender = userGender;
    }

    public String getUserFavorite_Scripture() {
        return UserFavorite_Scripture;
    }

    public void setUserFavorite_Scripture(String userFavorite_Scripture) {
        UserFavorite_Scripture = userFavorite_Scripture;
    }
}
