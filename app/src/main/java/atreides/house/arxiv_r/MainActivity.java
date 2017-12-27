package atreides.house.arxiv_r;

import android.Manifest;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    public List<RssFeedModel> mFeedModelList;
    public String category;
    public String mFeedTitle;
    public String mFeedSummary;
    public String mFeedAuthor;
    public String mFeedPublished;
    public String mFeedUpdated;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActivityCompat.requestPermissions(this,PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                SearchFragment newFragment = new SearchFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame
                                    , newFragment)
                            .addToBackStack(null)
                            .commit();
                }
                /*

                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

                Dialog generalDialog = new Dialog(MainActivity.this);
                generalDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                generalDialog.setContentView(R.layout.search_layout);
                generalDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                generalDialog.show();

                //category = "cs";
                //new FetchFeedTask().execute((Void) null);
            }
                 */
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_about) {
            Dialog generalDialog = new Dialog(MainActivity.this);
            generalDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            generalDialog.setContentView(R.layout.action_about_layout);
            generalDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            generalDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    // How about making a function for category, set title, etc? Keep it contained.
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_first_layout) {
            category = "cs";
            setTitle("Computer Science");
            checkExists();
        } else if (id == R.id.nav_second_layout) {
            category = "math";
            checkExists();
            setTitle("Mathematics");
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public List<RssFeedModel> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        String title = null;
        String summary = null;
        String author = null;
        String published = null;
        String updated = null;
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
                } else if (name.equalsIgnoreCase("author")) {
                    xmlPullParser.next();
                    author = xmlPullParser.getText();
                } else if (name.equalsIgnoreCase("published")) {
                    published = result;
                } else if (name.equalsIgnoreCase("updated")) {
                    updated = result;
                }
                Log.d("MainActivity", "title = " + title + "\nsummary = " + summary + "\nauthor = " + author);
                if (title != null && summary != null && author != null) {
                    if (isItem) {
                        RssFeedModel item = new RssFeedModel(title, summary, author, published, updated);
                        items.add(item);
                        Log.d("MainActivity", "item added");
                    } else {
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

    private class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {
        private String urlLink;
        Dialog loadingBlip = new Dialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            // pls wait for load my dude
            loadingBlip.requestWindowFeature(Window.FEATURE_NO_TITLE);
            loadingBlip.setContentView(R.layout.loading_blip);
            loadingBlip.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            loadingBlip.show();

            mFeedTitle = "title";
            mFeedSummary = "summary";
            mFeedAuthor = "author";
            mFeedPublished = "published";
            mFeedUpdated = "updated";

            Log.d("MainActivity", "Category (when it counts) = " + category);
            urlLink = ("https://export.arxiv.org/api/query?search_query=cat:" + category + "*&max_results=2");
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
            Log.d("postexecute","has begun");
            if (success) {
                loadingBlip.dismiss();
                sendMessenger();
            } else {
                Toast.makeText(MainActivity.this,
                        "Questionable internet connectivity...",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sendMessenger() {

        try {
            FileOutputStream fos = this.openFileOutput(category + "File", MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(mFeedModelList);
            of.flush();
            of.close();
            fos.close();
            Log.d("sendMessenger","Closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String test = "Rubber duckies are the SHIT";
        FragmentManager fragmentManager = getFragmentManager();
        FirstFragment newFragment = new FirstFragment();
        ParcelableArrayList pal = new ParcelableArrayList();
        pal.setThing(mFeedModelList);
        pal.setTitle(test);
        Log.d("sendMessenger", String.valueOf(mFeedModelList));
        Bundle bundle = new Bundle();
        bundle.putParcelable("articles", pal);
        bundle.putString("test", test);
        if (bundle != null) {
            newFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , newFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            Log.d("MainActivity", "It's null");
        }
    }

    private void checkExists() {
        // see if file exists
        File catFile = getApplicationContext().getFileStreamPath(category + "File");
        if (catFile == null || !catFile.exists()) {
            Log.d("redundancy checker","doesn't exist");
            // This means we should write a new one
            new FetchFeedTask().execute((Void) null);
        } else {
            Log.d("redundancy checker","exists");

            // file exists, so check date
            SimpleDateFormat df = new SimpleDateFormat("MMdd");
            Date todayDate = new Date();
            Date lastModDate = new Date(catFile.lastModified());
            String today = df.format(todayDate);
            String lastMod = df.format(lastModDate);

            if (Integer.parseInt(today) != Integer.parseInt(lastMod)) {
                // outdated, get new
                Log.d("wat",today +" is today and last mod is " + lastMod);
                Log.d("redundancy checker","outdated, fetching new data");
                new FetchFeedTask().execute((Void) null);

            } else {
                // current, send cached
                Log.d("redundancy checker","current");

                ArrayList<RssFeedModel> cachedData;
                FileInputStream fis;
                try {
                    fis = this.openFileInput(category + "File");
                    ObjectInputStream oi = new ObjectInputStream(fis);
                    cachedData = (ArrayList<RssFeedModel>) oi.readObject();
                    oi.close();
                    mFeedModelList = cachedData;
                } catch (FileNotFoundException e) {
                    Log.e("InternalStorage", e.getMessage());
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
}



