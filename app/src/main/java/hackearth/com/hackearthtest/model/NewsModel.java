package hackearth.com.hackearthtest.model;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by apple on 14/09/17.
 */

public class NewsModel {

    //data from server
    private int ID;
    private String TITLE;
    private String URL;
    private String PUBLISHER;
    private String CATEGORY;
    private String HOSTNAME;
    private long TIMESTAMP;


    //data setup in the client side
    private boolean isFavorite;

    private Date timestampDate;



    public static Comparator<NewsModel> COMPARE_BY_TIME_ASC = new Comparator<NewsModel>() {
        public int compare(NewsModel one, NewsModel other) {
            return one.getTimestampDate().compareTo(other.getTimestampDate());
        }
    };


    public static Comparator<NewsModel> COMPARE_BY_TIME_DESC = new Comparator<NewsModel>() {
        public int compare(NewsModel one, NewsModel other) {
            return other.getTimestampDate().compareTo(one.getTimestampDate());
        }
    };


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getPUBLISHER() {
        return PUBLISHER;
    }

    public void setPUBLISHER(String PUBLISHER) {
        this.PUBLISHER = PUBLISHER;
    }

    public String getCATEGORY() {
        return CATEGORY;
    }

    public void setCATEGORY(String CATEGORY) {
        this.CATEGORY = CATEGORY;
    }

    public String getHOSTNAME() {
        return HOSTNAME;
    }

    public void setHOSTNAME(String HOSTNAME) {
        this.HOSTNAME = HOSTNAME;
    }

    public long getTIMESTAMP() {
        return TIMESTAMP;
    }

    public void setTIMESTAMP(long TIMESTAMP) {
        this.TIMESTAMP = TIMESTAMP;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public Date getTimestampDate() {
        return timestampDate;
    }

    public void setTimestampDate(Date timestampDate) {
        this.timestampDate = timestampDate;
    }
}
