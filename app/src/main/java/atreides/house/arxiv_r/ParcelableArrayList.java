package atreides.house.arxiv_r;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by the Kwisatz Haderach on 12/17/2017.
 */

public class ParcelableArrayList extends ArrayList<String> implements Parcelable {

    public void setThing(List<RssFeedModel> thing) { Log.d("ParcelableArrayList","Thing set"); this.myThing = thing; }
    public void setTitle(String title) { this.title = title; }
    public List<RssFeedModel> getThing() { Log.d("ParcelableArrayList","Thing got"); return this.myThing; }
    public String getTitle() { return this.title; }

    //private List<RssFeedModel> itemsBox;
    //public List<RssFeedModel> getItemsBox() { return itemsBox; }
    private String title;
    private List<RssFeedModel> myThing;
    public ParcelableArrayList(){
        super();
    }//List<RssFeedModel> derp

    public ParcelableArrayList(Parcel in) {
        Log.d("ParcelableArrayList","Parcel in");
        //in.readList(this.myThing,ArrayList.class.getClassLoader());
        this.myThing=in.readArrayList(null);
        this.title=in.readString();
        //String.class.getClassLoader()
        //this.itemsBox = in.createTypedArrayList(RssFeedModel.CREATOR);
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeList(this.myThing);
        out.writeString(this.title);
    }

    public static final Parcelable.Creator<ParcelableArrayList> CREATOR = new Parcelable.Creator<ParcelableArrayList>() {
        public ParcelableArrayList createFromParcel(Parcel in) {
            return new ParcelableArrayList(in);
        }
        public ParcelableArrayList[] newArray(int size) {
            return new ParcelableArrayList[size];
        }
    };
}
