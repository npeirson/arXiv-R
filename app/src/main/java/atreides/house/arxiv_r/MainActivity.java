package atreides.house.arxiv_r;

import android.Manifest;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public String category;
    public ArrayList<String> bkmx;
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
            List<String> bkmx = new ArrayList<>();

            FileOutputStream fos;
            try {
                //fos = this.openFileOutput("bookmarks", Context.MODE_PRIVATE);
                fos = new FileOutputStream(bmFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(bkmx);
                oos.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
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
            new FetchFeedTask(category,true,this,getFragmentManager());
        } else if (id == R.id.nav_second_layout) {
            category = "math";
            setTitle("Mathematics");
            new FetchFeedTask(category,true,this,getFragmentManager());

        } else if (id == R.id.nav_slideshow) {
            try {
                new bewkmarx();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (id == R.id.nav_manage) {
            FavoritesFragment newFrag = new FavoritesFragment();
            FragmentManager fragMan = getFragmentManager();
            fragMan.beginTransaction()
                    .replace(R.id.content_frame, newFrag)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(this, FavoritesActivityR.class);
            startActivity(intent);
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
        File bmFile = getApplicationContext().getFileStreamPath("bookmarks");
        private bewkmarx() throws IOException {
            try {
                FileInputStream fis = new FileInputStream(bmFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                setTitle("Bookmarks");
                new FetchFeedTask((ArrayList<String>) ois.readObject(), MainActivity.this, getFragmentManager());
                ois.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}




