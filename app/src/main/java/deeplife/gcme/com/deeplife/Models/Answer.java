package deeplife.gcme.com.deeplife.Models;

import deeplife.gcme.com.deeplife.Disciples.Disciple;

/**
 * Created by bengeos on 12/20/16.
 */

public class Answer {
    private int ID, SerID, QuestionID;
    private String DisciplePhone, Answer;
    Disciple.STAGE BuildStage;

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

    public int getQuestionID() {
        return QuestionID;
    }

    public void setQuestionID(int questionID) {
        QuestionID = questionID;
    }

    public String getDisciplePhone() {
        return DisciplePhone;
    }

    public void setDisciplePhone(String disciplePhone) {
        DisciplePhone = disciplePhone;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public Disciple.STAGE getBuildStage() {
        return BuildStage;
    }

    public void setBuildStage(Disciple.STAGE buildStage) {
        this.BuildStage = buildStage;
    }
}
