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
    public String cfSuf;
    public String mFeedTitle;
    public String mFeedSummary;
    public String mFeedAuthor;
    public String mFeedPublished;
    public String mFeedUpdated;
    public String mFeedId;
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

        // create bookmarks file if non exists
        File bmFile = getApplicationContext().getFileStreamPath("bookmarks");
        if (bmFile == null || !bmFile.exists()) {
            new File("bookmarks");
            FileOutputStream bos = null;
            String binit = "bookmarks:";
            try {
                bos = new FileOutputStream(bmFile);
                bos.write(binit.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bos != null) {
                        bos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

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
            new FetchFeedTask().FetchFeedTask(category,true);
        } else if (id == R.id.nav_second_layout) {
            category = "math";
            setTitle("Mathematics");
            new FetchFeedTask().FetchFeedTask(category,true);

            // ahhhhhhhh
            String urlLink = ("https://export.arxiv.org/api/query?search_query=cat:" + category + "*&max_results=5");
        } else if (id == R.id.nav_slideshow) {
            try {
                new bewkmarx();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
    private class bewkmarx {
        private bewkmarx() throws IOException {
            File bmFile = getApplicationContext().getFileStreamPath("bookmarks");
            FileInputStream fis;
            fis = new FileInputStream(bmFile);
            byte fileContent[] = new byte[(int)bmFile.length()];
            fis.read(fileContent);
            String bm = new String(fileContent);
            // gonna be a bitch cuz you can only search for one at a time. shit.

            int lidx = 0;
            int idx = 0;
            String cat = "";

            while ((idx = bm.indexOf("\n", idx)) != -1)
            {
                lidx = idx;
                idx++;
                Log.d("derp", String.valueOf(idx));
                cat = cat + "id_list=" + bm.indexOf(lidx)+idx;

            }

            String substr=bm.substring(bm.indexOf("\n"));
            Log.d("oi", cat);
            category = cat;
        }
    }
}



