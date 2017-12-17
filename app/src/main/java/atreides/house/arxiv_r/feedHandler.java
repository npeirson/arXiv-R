package atreides.house.arxiv_r;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.widget.TextView;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by the Kwisatz Haderach on 12/16/2017.
 */

public class feedHandler extends IntentService {

    // feed parsing variables

    private TextView mFeedTitleTextView;
    private TextView mFeedSummaryTextView;
    private TextView mFeedAuthorsTextView;
    private TextView mFeedPublishedTextView;
    private TextView mFeedUpdatedTextView;

    private RecyclerView mRecyclerView;
    private List<String> mFeedModelList;
    private String mFeedTitle;
    private String mFeedSummary;
    private String mFeedAuthor;
    private String mFeedPublished;
    private String mFeedUpdated;
    public String urlLink;
    public ResultReceiver receiver;
    public ArrayList<String> items = new ArrayList<>();

    public feedHandler(){
        super(feedHandler.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent){

        String category = intent.getStringExtra("category");
        receiver = intent.getParcelableExtra("receiverName");
        urlLink = new String ("http://export.arxiv.org/api/query?search_query=cat:"+category+"*&max_results=1");
        new FetchFeedTask().execute((Void) null);
    }

    public List<String> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        String title = null;
        String summary = null;
        String author = null;
        String published = null;
        String updated = null;
        boolean isItem = false;


        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if(name == null)
                    continue;

                if(eventType == XmlPullParser.END_TAG) {
                    if(name.equalsIgnoreCase("entry")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if(name.equalsIgnoreCase("entry")) {
                        isItem = true;
                        continue;
                    }
                }

                Log.d("MainActivity", "Parsing name ==> " + name);
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("summary")) {
                    summary = result;
                } else if (name.equalsIgnoreCase("name")) {
                    author = result;
                } else if (name.equalsIgnoreCase("published")) {
                    published = result;
                } else if (name.equalsIgnoreCase("updated")) {
                    updated = result;
                }

                if (title != null && summary != null && author != null) {
                    ArrayList<String> item = new ArrayList<>(Arrays.asList(title, summary, author, published, updated));
                    if(isItem) {
                        item.add(0, title);
                        item.add(1, summary);
                        item.add(2, author);
                        item.add(3, published);
                        item.add(4, updated);
                    }
                    else {
                        mFeedTitle = title;
                        mFeedSummary = summary;
                        mFeedAuthor = author;
                        mFeedPublished = published;
                        mFeedUpdated = updated;
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

    public class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            mFeedTitle = null;
            mFeedSummary = null;
            mFeedAuthor = null;
            mFeedPublished = null;
            mFeedUpdated = null;
/**
            mFeedTitleTextView.setText(mFeedTitle);
            mFeedSummaryTextView.setText(mFeedSummary);
            mFeedAuthorsTextView.setText(mFeedAuthor);
            mFeedPublishedTextView.setText(mFeedPublished);
            mFeedUpdatedTextView.setText(mFeedUpdated);
 */
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(urlLink))
                return false;

            try {
                if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                    urlLink = "http://" + urlLink;

                URL url = new URL(urlLink);
                InputStream inputStream = url.openConnection().getInputStream();
                mFeedModelList = parseFeed(inputStream);
                return true;
            } catch (IOException e) {
                Log.e(TAG, "Error", e);
            } catch (XmlPullParserException e) {
                Log.e(TAG, "Error XMLPullParserException", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {

            if (success) {
                returnToSender();

            } else {
                Toast.makeText(feedHandler.this,
                        "Error: something went wrong. Formatting local storage...",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
    private void returnToSender() {
        Bundle bundle = new Bundle();
        ArrayList<String> articleInfo = new ArrayList<>();
        ArrayList<String> rssShit = new ArrayList<>();
        int i = 0;
        for (String item : mFeedModelList) {
            rssShit.add(i, item);
            i++;
        }
        // for article in list?
        articleInfo.add(0, mFeedTitle);
        articleInfo.add(1, mFeedSummary);
        articleInfo.add(2, mFeedAuthor);
        articleInfo.add(3, mFeedPublished);
        articleInfo.add(4, mFeedUpdated);
        // Fill RecyclerView

        bundle.putStringArrayList("mFeedModelList", (ArrayList<String>) mFeedModelList);
        bundle.putStringArrayList("rssShit", items);

        receiver.send(1, bundle);
    }
}
