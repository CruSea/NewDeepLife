package deeplife.gcme.com.deeplife.Disciples;

/**
 * Created by bengeos on 12/7/16.
 */

public class Disciple {
    private String Name,Email,Phone,Country;

    public Disciple() {
        Name = " ";
        Email = " ";
        Phone = " ";
        Country = " ";
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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
}
