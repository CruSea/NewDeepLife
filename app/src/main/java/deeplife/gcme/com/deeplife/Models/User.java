package deeplife.gcme.com.deeplife.Models;

/**
 * Created by Roger on 3/27/16.
 */
public class User {
    private int ID, SerID;
    private String Name, Pass, FullName, Email, Phone, Country, Picture, Gender, Favorite_Scripture;

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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getFavorite_Scripture() {
        return Favorite_Scripture;
    }

    public void setFavorite_Scripture(String favorite_Scripture) {
        Favorite_Scripture = favorite_Scripture;
    }
}
