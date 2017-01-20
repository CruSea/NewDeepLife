package deeplife.gcme.com.deeplife.Wbs;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bengeos on 12/20/16.
 */

public class WbsQuestion implements Parent<WbsQuestion> {

    public enum Type {
        YESNO,
        NUMBER,
        FOLDER
    }

    private int ID, SerID, Category, mCountry, Mandatory;
    private String Question, Description, Created;
    private Type mType;

    private List<WbsQuestion> mChildren;  // Only applicable if this instance is a FOLDER.

    // Constructors
    public WbsQuestion() {
        mChildren = new ArrayList<WbsQuestion>();
    }

    public WbsQuestion(List<WbsQuestion> children) {
        mChildren = children;
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


    public void setChildren(List<WbsQuestion> children) {
        if (getType() == Type.FOLDER) {
            mChildren = children;
        }
    }

    public void addChild(WbsQuestion child) {
        if (getType() == Type.FOLDER) {
            mChildren.add(child);
        }
    }

    @Override
    public List<WbsQuestion> getChildList() {
        return mChildren;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
