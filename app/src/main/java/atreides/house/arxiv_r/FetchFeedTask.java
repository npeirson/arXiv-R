package atreides.house.arxiv_r;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

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
    public Context context;
    public Boolean preD = false;
    public Boolean add = false;
    public List<RssFeedModel> mFeedModelList;
    public List<String> urlList;
    public String mFeedTitle;
    public String mFeedSummary;
    public String mFeedAuthor;
    public String mFeedPublished;
    public String mFeedUpdated;
    public String mFeedId;
    public String fpSave; // until I figure out how to pass this...
    public String baseUrl = "https://export.arxiv.org/api/query?";

    // for pulling info
    public FetchFeedTask() { }

    // for "more cards" calls
    public FetchFeedTask(String cat, int pos){
        // could add old data concatenation for increased efficiency
    }

    public FetchFeedTask(String fp, boolean dc, Context ctx){
        if ( dc == true ) {
            // for activity drawer calls
            Log.d("fp","equals: " + fp);
            preD = true;
            // for everyone else's sake...
            fpSave = fp;
            context = ctx;
            checkExists(fp, ctx);
        } else {
            // for searches
        }
    }

    // for bookmarks
    public FetchFeedTask(ArrayList<String> targetUrlList){
        urlList = targetUrlList;
    }

    // See requested data already exist
    private void checkExists(String fp, Context context) {
        // see if file exists
        File catFile = context.getFileStreamPath(fp + "File");
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
                new FetchFeedAsync().execute(baseUrl + "search_query=cat:" + fp + "*&max_results=10");

            } else {
                // current, send cached
                Log.d("redundancy checker","current");

                ArrayList<RssFeedModel> cachedData;
                FileInputStream fis;
                try {
                    fis = context.openFileInput(fp + "File");
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
    public class FetchFeedAsync extends AsyncTask<String, Void, Boolean> {

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
        protected Boolean doInBackground(String... strings) {
            String urlLink = strings[0];
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
            if (mFeedModelList.isEmpty()) {
                //loadingBlip.dismiss();
                Toast.makeText(context,
                        "Search returned no results",
                        Toast.LENGTH_LONG).show();
            } else {
                if (success) {
                    if (preD == true) {
                        saveData();
                        sendMessenger();
                    } else {
                        sendMessenger();
                    }
                } else {
                    // idk, it failed, what else do you want? Deal with it.
                    Log.d("what","dafuq");
                }
            }
        }
    }
    // save data if reasonable
    private void saveData() {
        try {
            FileOutputStream fos = context.openFileOutput(fpSave + "File", MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(mFeedModelList);
            of.flush();
            of.close();
            fos.close();
            Log.d("saver","saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getAddState(){
        return add;
    }

    public List<RssFeedModel> getmFeedModelList() {
        return mFeedModelList;
    }

    // bundle and send data
    private void sendMessenger() {
        new MainActivity().doTheThing();
    }
}