package atreides.house.arxiv_r;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by obaro on 27/11/2016.
 * Check him out: https://github.com/obaro/
 * Thanks for sharing, obaro
 *
 * Customized by the Kwisatz Haderach on 15/12/2017.
 */

public class RssFeedModel extends ArrayList<String> implements Parcelable {

    public String title;
    public String summary;
    public String author;
    public String published;
    public String updated;

    public RssFeedModel(String title, String summary, String author, String published, String updated) {
        this.title = title;
        this.summary = summary;
        this.author = author;
        this.published = published;
        this.updated = updated;
    }

    public RssFeedModel(Parcel parcel){
        title = parcel.readString();
        summary = parcel.readString();
        author = parcel.readString();
        published = parcel.readString();
        updated = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(this.title);
        out.writeString(this.summary);
        out.writeString(this.author);
        out.writeString(this.published);
        out.writeString(this.updated);
    }

    public static final Parcelable.Creator<RssFeedModel> CREATOR=new Parcelable.Creator<RssFeedModel>() {
        @Override
        public RssFeedModel createFromParcel(Parcel in) {
            return new RssFeedModel(in);
        }

        @Override
        public RssFeedModel[] newArray(int i) {
            return new RssFeedModel[i];
        }
    };

    public void setTitle(String title) { this.title = title; }
    public void setSummary(String summary) { this.summary = summary; }
    public void setAuthor(String author) { this.author = author; }
    public void setPublished(String published) { this.published = published; }
    public void setUpdated(String updated) { this.updated = updated; }

    public String getTitle() { return title; }
    public String getSummary() { return summary; }
    public String getAuthor() { return author; }
    public String getPublished() { return published; }
    public String getUpdated() { return updated; }
}