package atreides.house.arxiv_r;

import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import static android.content.ContentValues.TAG;

/**
 * Created by the Kwisatz Haderach on 12/29/2017.
 */

public class FetchFeedTask extends AppCompatActivity {
    // initialize stuff and things
    public Boolean preD = false;
    public Boolean add = false;
    public List<RssFeedModel> mFeedModelList;
    public List<String> urlList;
    public String urlLink;
    public String mFeedTitle;
    public String mFeedSummary;
    public String mFeedAuthor;
    public String mFeedPublished;
    public String mFeedUpdated;
    public String mFeedId;
    public String filePrefix;


    // for "more cards" calls
    public void FetchFeedTask(String cat, int pos){
        // could add old data concatenation for increased efficiency
    }

    public void FetchFeedTask(String string, boolean dc){
        if ( dc == true ) {
            // for activity drawer calls
            filePrefix = string;
            preD = true;
            checkExists();
        } else {
            // for searches
            urlLink = string;
        }
    }

    // for bookmarks
    public void FetchFeedTask(ArrayList<String> targetUrlList){
        urlList = targetUrlList;
    }

    // See requested data already exist
    private void checkExists() {
        // see if file exists
        File catFile = getApplicationContext().getFileStreamPath(filePrefix + "File");
        if (catFile == null || !catFile.exists()) {
            Log.d("redundancy checker","doesn't exist");
            // set save tag
        } else {
            Log.d("redundancy checker","exists");
            SimpleDateFormat df = new SimpleDateFormat("MMdd");
            Date todayDate = new Date();
            Date lastModDate = new Date(catFile.lastModified());
            String today = df.format(todayDate);
            String lastMod = df.format(lastModDate);

            if (Integer.parseInt(today) != Integer.parseInt(lastMod)) {
                // outdated, get new
                Log.d("wat",today +" is today and last mod is " + lastMod);
                Log.d("redundancy checker","outdated, fetching new data");
                new FetchFeedAsync().execute((Void) null);

            } else {
                // current, send cached
                Log.d("redundancy checker","current");

                ArrayList<RssFeedModel> cachedData;
                FileInputStream fis;
                try {
                    fis = this.openFileInput(filePrefix + "File");
                    ObjectInputStream oi = new ObjectInputStream(fis);
                    cachedData = (ArrayList<RssFeedModel>) oi.readObject();
                    oi.close();
                    fis.close();
                    mFeedModelList = cachedData;
                } catch (IOException e) {
                    Log.e("InternalStorage", e.getMessage());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Log.d("redundancy checker","sending old info");
                sendMessenger();
            }
        }
    }

    // Parses feed. Called from an AsyncTask.
    public List<RssFeedModel> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        String title = null;
        String summary = null;
        String author = null;
        String published = null;
        String updated = null;
        String id = null;

        boolean isItem = false;
        List<RssFeedModel> items = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if (name == null)
                    continue;

                if (eventType == XmlPullParser.END_TAG) {
                    if (name.equalsIgnoreCase("entry")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if (name.equalsIgnoreCase("entry")) {
                        isItem = true;
                        continue;
                    }
                }

                //Log.d("MainActivity", "Parsing name ==> " + name);
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("summary")) {
                    summary = result;
                } else if (name.equalsIgnoreCase("author")) {
                    xmlPullParser.next();
                    author = xmlPullParser.getText();
                } else if (name.equalsIgnoreCase("published")) {
                    published = result;
                } else if (name.equalsIgnoreCase("updated")) {
                    updated = result;
                } else if (name.equalsIgnoreCase("id")) {
                    id = result;
                }
                if (title != null && summary != null && author != null && id != null) {
                    if (isItem) {
                        RssFeedModel item = new RssFeedModel(title, summary, author, published, updated, id);
                        items.add(item);
                        Log.d("MainActivity", "item added");
                    } else {
                        mFeedTitle = title;
                        mFeedSummary = summary;
                        mFeedAuthor = author;
                        mFeedPublished = published;
                        mFeedUpdated = updated;
                        mFeedId = id;
                    }
                    title = null;
                    summary = null;
                    author = null;
                    published = null;
                    updated = null;
                    isItem = false;
                }
            }

            return items;
        } finally {
            inputStream.close();
        }
    }

    // call parser and group data
    public class FetchFeedAsync extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            // to avoid null errors
            mFeedTitle = "title";
            mFeedSummary = "summary";
            mFeedAuthor = "author";
            mFeedPublished = "published";
            mFeedUpdated = "updated";
            mFeedId = "id";
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                URL url = new URL(urlLink);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                InputStream inputStream = conn.getInputStream();
                mFeedModelList = parseFeed(inputStream);
                return true;

            } catch (IOException e) {
                Log.e(TAG, "Error", e);
            } catch (XmlPullParserException e) {
                Log.e(TAG, "Error", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                if ( preD == true ) {
                    saveData();
                    sendMessenger();
                } else {
                    sendMessenger();
                }
            } else {
                // idk, it failed, what else do you want? Deal with it.
            }
        }
    }

    // save data if reasonable
    private void saveData() {
        try {
            FileOutputStream fos = this.openFileOutput(filePrefix + "File", MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(mFeedModelList);
            of.flush();
            of.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // bundle and send data
    private void sendMessenger() {
        if ( add = true ) {
            // just adding new info, dawg
        } else {
            // make an entire fragment
            FragmentManager fragmentManager = getFragmentManager();
            FirstFragment newFragment = new FirstFragment();
            ParcelableArrayList pal = new ParcelableArrayList();
            pal.setThing(mFeedModelList);
            Bundle bundle = new Bundle();
            bundle.putParcelable("articles", pal);
            if (bundle != null) {
                newFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame
                                , newFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Log.d("MainActivity", "null message :( :( :(");
            }
        }
    }
}