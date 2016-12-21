package deeplife.gcme.com.deeplife.WinBuildSend;

/**
 * Created by bengeos on 12/20/16.
 */

public class WinBuildSendQuestion{
    private int ID, SerID, Category, Cauntry, Mandatory;
    private String Question, Type, Description, Created;

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

    public int getCauntry() {
        return Cauntry;
    }

    public void setCauntry(int cauntry) {
        Cauntry = cauntry;
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

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
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
