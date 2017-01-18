package deeplife.gcme.com.deeplife.Wbs;

/**
 * Created by bengeos on 12/20/16.
 */

public class WbsQuestion {

    public enum Type {
        YESNO,
        NUMBER,
        FOLDER
    }

    private int ID, SerID, Category, mCountry, Mandatory;
    private String Question, Description, Created;
    private Type mType;

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

    public int getCategory() {
        return Category;
    }

    public void setCategory(int category) {
        Category = category;
    }

    public int getCountry() {
        return mCountry;
    }

    public void setCountry(int country) {
        mCountry = country;
    }

    public int getMandatory() {
        return Mandatory;
    }

    public void setMandatory(int mandatory) {
        Mandatory = mandatory;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public Type getType() {
        return mType;
    }

    public void setType(Type type) {
        mType = type;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCreated() {
        return Created;
    }

    public void setCreated(String created) {
        Created = created;
    }
}
