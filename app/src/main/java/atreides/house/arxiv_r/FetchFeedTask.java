package atreides.house.arxiv_r;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.Window;
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
import org.apache.commons.io.FileUtils;
import static android.content.ContentValues.TAG;

/**
 * Created by the Kwisatz Haderach on 12/29/2017.
 * The idea was to keep the heavy lifting out of the MainActivity to keep start-up time low, and hopefully make feature expansion easy in the future.
 */

public class FetchFeedTask extends AppCompatActivity {
    // initialize stuff and things
    public Context context;
    public FragmentManager fragMan;
    public Fragment frag;
    public List<RssFeedModel> mFeedModelList;
    public Dialog loadingBlip;
    public String mFeedTitle, mFeedSummary, mFeedPublished, mFeedUpdated, mFeedId;
    public String fpSave; // until I figure out how to pass this...
    public String baseUrl = "https://export.arxiv.org/api/query?";
    public String mFeedAuthor;
    public Boolean preD = false;
    public Boolean love = false;
    public Boolean hate = false;
    public Boolean bmks = false;
    public Boolean stackMe = false;

    // for "more cards" calls
    public FetchFeedTask(String fp, int pos, Context ctx, Fragment fragment){
        // blip me please, daddy
        loadingBlip = new Dialog(ctx);
        loadingBlip.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingBlip.setContentView(R.layout.loading_blip);
        loadingBlip.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingBlip.show();
        // could add old data concatenation for increased efficiency
        context = ctx;
        frag = fragment;
        hate = true;
        // helps if you make it actually do something, huh
        new FetchFeedAsync().execute(baseUrl + fp + "&max_results=10");
        // remember to set "send message" to true

    }

    // general caller
    public FetchFeedTask(String fp, boolean dc, Context ctx, FragmentManager frag){
        loadingBlip = new Dialog(ctx);
        loadingBlip.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingBlip.setContentView(R.layout.loading_blip);
        loadingBlip.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingBlip.show();

        fragMan = frag;
        fpSave = fp;
        context = ctx;
        if ( dc == true ) {
            // for activity drawer calls
            preD = true;
            stackMe = false;

            checkExists(fp, ctx);
        } else {
            // for searches
            preD = false;
            stackMe = true;
            Log.d("url",baseUrl + fp );
            new FetchFeedAsync().execute(baseUrl + fp + "&max_results=10");
        }
    }

    // for bookmarks
    public FetchFeedTask(ArrayList<String> targetUrlList, Context ctx, FragmentManager frag) throws IOException, ClassNotFoundException {
        // blip me please, daddy
        loadingBlip = new Dialog(ctx);
        loadingBlip.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingBlip.setContentView(R.layout.loading_blip);
        loadingBlip.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingBlip.show();

        fragMan = frag;
        context = ctx;
        bmks = true;
        File oldBmks = new File(ctx.getFilesDir().getAbsolutePath() + "/oldbookmarks");
        File bmFile = new File(ctx.getFilesDir().getAbsolutePath() + "/bookmarks");
        if (oldBmks == null || !oldBmks.exists()) {
            // pop the cherry
            syncRegisteries(bmFile, oldBmks);
            new FetchFeedAsync().execute(targetUrlList.toArray(new String[targetUrlList.size()]));
        } else {
            // if !virgin
            if (FileUtils.contentEquals(oldBmks, bmFile)) {
                // bookmarks haven't changed
                FileInputStream fis;
                try {
                    fis = ctx.openFileInput("bookmarksFile");
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    mFeedModelList = (ArrayList<RssFeedModel>) ois.readObject();
                    ois.close();
                    fis.close();
                } catch (IOException e) {
                    Log.e("InternalStorage", e.getMessage());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                sendMessenger(false);
            } else {
                // bookmarks have changed
                syncRegisteries(bmFile, oldBmks);
                new FetchFeedAsync().execute(targetUrlList.toArray(new String[targetUrlList.size()]));
            }
        }
    }

    // for favorites
    public FetchFeedTask(String targetUrl, Context ctx, FragmentManager frag, boolean favorites) throws IOException, ClassNotFoundException {
        fragMan = frag;
        File favFile = new File(Environment.getDataDirectory() + "/data/atreides.house.arxiv_r/files/favorites");
        File favFileFetched = new File(Environment.getDataDirectory() + "/data/atreides.house.arxiv_r/files/favoritesFetched");
        File favFileCached = new File(Environment.getDataDirectory() + "/data/atreides.house.arxiv_r/files/favoritesCache");
        if (!favFile.exists()) {
            // make the file, then go get the goods
            //syncRegisteries(favFile,favFileFetched);
            new FetchFeedAsync().execute(targetUrl);
        } else {
            // check last updated
            SimpleDateFormat df = new SimpleDateFormat("MMdd");
            Date todayDate = new Date();
            Date lastModDate = new Date(favFileCached.lastModified());
            String today = df.format(todayDate);
            String lastMod = df.format(lastModDate);
            try {
                if (Integer.parseInt(today) != Integer.parseInt(lastMod) || !FileUtils.contentEquals(favFile, favFileFetched)) {
                    // outdated or list changed, get new
                    //syncRegisteries(favFile,favFileFetched);
                    new FetchFeedAsync().execute(targetUrl);
                } else {
                    // current, send cached
                    FileInputStream fis = new FileInputStream(favFileCached);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    mFeedModelList = (ArrayList<RssFeedModel>) ois.readObject();
                    ois.close();
                    fis.close();
                    sendMessenger(false);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void syncRegisteries(File newFile, File oldFile) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(newFile);
        ObjectInputStream ois = new ObjectInputStream(fis);
        ArrayList<String> cbkmx = (ArrayList<String>) ois.readObject();
        ois.close();
        fis.close();
        FileOutputStream fos = new FileOutputStream(oldFile);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(cbkmx);
        oos.close();
        fos.close();
    }

    // See if requested data already exist
    private void checkExists(String fp, Context context) {
        // see if file exists
        File catFile = context.getFileStreamPath(fp + "File");
        if (catFile == null || !catFile.exists()) {
            Log.d("redundancy checker","doesn't exist");
            new FetchFeedAsync().execute(baseUrl + "search_query=cat:" + fp + "*&max_results=10");
        } else {
            Log.d("redundancy checker","exists");
            SimpleDateFormat df = new SimpleDateFormat("MMdd");
            Date todayDate = new Date();
            Date lastModDate = new Date(catFile.lastModified());
            String today = df.format(todayDate);
            String lastMod = df.format(lastModDate);

            if (Integer.parseInt(today) != Integer.parseInt(lastMod)) {
                // outdated, get new
                new FetchFeedAsync().execute(baseUrl + "search_query=cat:" + fp + "*&max_results=10");
            } else {
                // current, send cached
                FileInputStream fis;
                try {
                    fis = context.openFileInput(fp + "File");
                    ObjectInputStream oi = new ObjectInputStream(fis);
                    mFeedModelList = (ArrayList<RssFeedModel>) oi.readObject();
                    oi.close();
                    fis.close();
                } catch (IOException e) {
                    Log.e("InternalStorage", e.getMessage());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Log.d("redundancy checker","sending old info");
                sendMessenger(false);
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
        int j = 0;

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
                    title = result.replace("\n","");
                } else if (name.equalsIgnoreCase("summary")) {
                    summary = result.replace("\n"," ");
                } else if (name.equalsIgnoreCase("author")) {
                    xmlPullParser.next();
                    if ( j == 0 ) {
                        author = xmlPullParser.getText();
                    } if ( j >= 1 ) {
                        author = author + ", " + xmlPullParser.getText();
                    }
                    Log.d("author",author);
                    j++;

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
                    j = 0;
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
            if (strings.length == 0 ) {
                if ( bmks == true ) {
                    mFeedModelList = new ArrayList<>();
                    love = true;
                } else {
                    mFeedModelList = new ArrayList<>();
                    love = false;
                } return love;
            } else if (strings.length == 1) {
                String urlLink = null;
                // one url
                if ( bmks == true ) {
                    // one bookmark
                    urlLink = baseUrl + "id_list=" + strings[0];
                } else {
                    // category/search
                    urlLink = strings[0];
                }
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
            } else {
                // many urls
                for(int i=0;i<strings.length;i++) {
                    String urlLink = baseUrl + "id_list=" + strings[i];
                    try {
                        URL url = new URL(urlLink);
                        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                        InputStream inputStream = conn.getInputStream();
                        if (i == 0) {
                            mFeedModelList = parseFeed(inputStream);
                        } else {
                            mFeedModelList.addAll(parseFeed(inputStream));
                        }
                        love = true;

                    } catch (IOException e) {
                        Log.e(TAG, "Error", e);
                    } catch (XmlPullParserException e) {
                        Log.e(TAG, "Error", e);
                    }
                }
                return love;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (mFeedModelList.isEmpty()) {
                if ( bmks == true ) {
                    loadingBlip.dismiss();
                    Toast.makeText(context,
                            "You have no bookmarks",
                            Toast.LENGTH_LONG).show();
                } else if ( hate == true ) {
                    loadingBlip.dismiss();
                    Toast.makeText(context,
                            "There are no more articles to show",
                            Toast.LENGTH_LONG).show();
                } else {
                    loadingBlip.dismiss();
                    Toast.makeText(context,
                            "Search returned no results",
                            Toast.LENGTH_LONG).show();
                }
            } else {
                if (success) {
                    if (preD == true || bmks == true) {
                        saveData();
                        sendMessenger(false);
                    } else if ( hate == true ) {
                        Log.d("sending the","glorious truth");
                        sendMessenger(true);
                    } else {
                        sendMessenger(false);
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
            String dest;
            if ( bmks == true ) {
                dest = "bookmarksFile";
            } else {
                dest = fpSave + "File";
            }
            FileOutputStream fos = context.openFileOutput(dest, MODE_PRIVATE);
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

    // bundle and send data to fragment
    private void sendMessenger(Boolean add) {
        // make an entire fragment
        FirstFragment newFragment = new FirstFragment();
        ParcelableArrayList pal = new ParcelableArrayList();
        pal.setThing(mFeedModelList);
        Bundle bundle = new Bundle();
        bundle.putParcelable("articles", pal);
        if (add) {
            // just adding new info, dawg
            // use for "more articles" calls
            Log.d("sendmessenger", String.valueOf(add));
            bundle.putString("category", fpSave);
            frag.setArguments(bundle);
        } else {
            if (loadingBlip != null) {
                loadingBlip.dismiss();
            }
            if (bundle != null) {
                newFragment.setArguments(bundle);
                if ( stackMe == true ) {
                    fragMan.beginTransaction()
                            .replace(R.id.content_frame, newFragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    fragMan.beginTransaction()
                            .replace(R.id.content_frame, newFragment)
                            .commit();
                }
            } else {
                Log.d("MainActivity", "null message :( :( :(");
            }
        }
    }
}

