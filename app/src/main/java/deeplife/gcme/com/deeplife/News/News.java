package deeplife.gcme.com.deeplife.News;

/**
 * Created by bengeos on 12/6/16.
 */

public class News {
    private int id,SerID;
    private String Title,Content,ImageURL,ImagePath,Category,PubDate;

    public int getId() {
        return id;
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

    public void setId(int id) {
        this.id = id;
    }

    public int getSerID() {
        return SerID;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getPubDate() {
        return PubDate;
    }

    public void setPubDate(String pubDate) {
        PubDate = pubDate;
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
}
