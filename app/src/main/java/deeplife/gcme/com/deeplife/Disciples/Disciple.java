package deeplife.gcme.com.deeplife.Disciples;

/**
 * Created by bengeos on 12/7/16.
 */

public class Disciple {
    public enum Stage {
        WIN("Win", 1), //briggsm: does this need to be UPPERCASE (the string) ???
        BUILD("Build", 2),
        SEND("Send", 3);

        private final String name;
        private final int serverId;
        Stage(String s, int i) { this.name = s; this.serverId = i; }
        public boolean equalsName(String otherName) { return (otherName == null) ? false : name.equals(otherName); }
        @Override public String toString() { return this.name; }
        public int toServerId() { return this.serverId; }
        public static Stage fromString(String s) { if (s != null) { for (Stage t : Stage.values()) { if (s.equalsIgnoreCase(t.toString())) { return t; } } } throw new IllegalArgumentException("No constant in <Stage> Enum found with text: " + s); }
    }

    public enum Gender {
        MALE("Male"),
        FEMALE("Female");

        private final String name;
        Gender(String s) { this.name = s; }
        public boolean equalsName(String otherName) { return (otherName == null) ? false : name.equals(otherName); }
        @Override public String toString() { return this.name; }
        public static Gender fromString(String s) { if (s != null) { for (Gender t : Gender.values()) { if (s.equalsIgnoreCase(t.toString())) { return t; } } } throw new IllegalArgumentException("No constant in <Gender> Enum found with text: " + s); }
    }

    // briggsm: Role Needed???
    public enum Role {
        DEFAULT("0"),
        USER("1"),
        SUPER_ADMIN("2"),
        AREA_ADMIN("3"),
        COUNTRY_ADMIN("4");

        private final String name;
        Role(String s) { this.name = s; }
        public boolean equalsName(String otherName) { return (otherName == null) ? false : name.equals(otherName); }
        @Override public String toString() { return this.name; }
        public static Role fromString(String s) { if (s != null) { for (Role t : Role.values()) { if (s.equalsIgnoreCase(t.toString())) { return t; } } } return DEFAULT; }
    }

    private int ID,SerID,MentorID;
    private String FullName,DisplayName,Email,Phone,Country,ImageURL,ImagePath,Created;
    private Stage stage;
    private Gender gender;
    private Role role;

    public Disciple() {
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

    public int getMentorID() {
        return MentorID;
    }

    public void setMentorID(int mentorID) {
        MentorID = mentorID;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
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

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getCreated() {
        return Created;
    }

    public void setCreated(String created) {
        Created = created;
    }

    public Disciple.Stage getStage() {
        return stage;
    }

    public void setStage(Disciple.Stage stage) {
        this.stage = stage;
    }

    public Disciple.Gender getGender() {
        return gender;
    }

    public void setGender(Disciple.Gender gender) {
        this.gender = gender;
    }

    public Disciple.Role getRole() {
        return role;
    }

    public void setRole(Disciple.Role role) {
        this.role = role;
    }
}
