package deeplife.gcme.com.deeplife.LearningTools;

/**
 * Created by bengeos on 12/7/16.
 */

public class LearningTool {
    private int ID, SerID, Country;
    private String Title, Content, VideoURL, DefaultLearn, Created;

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

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getCountry() {
        return Country;
    }

    public void setCountry(int country) {
        Country = country;
    }

    public String getVideoURL() {
        return VideoURL;
    }

    public void setVideoURL(String videoURL) {
        VideoURL = videoURL;
    }

    public String getDefaultLearn() {
        return DefaultLearn;
    }

    public void setDefaultLearn(String defaultLearn) {
        DefaultLearn = defaultLearn;
    }

    public String getCreated() {
        return Created;
    }

    public void setCreated(String created) {
        Created = created;
    }
}
