package atreides.house.arxiv_r;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class FavoritesActivityR extends AppCompatActivity implements FavoritesInterfaces.OnStartDragListener {

    File favFile = new File(Environment.getDataDirectory() + "/data/atreides.house.arxiv_r/files/favorites");
    public FloatingActionButton fabFav;
    private ItemTouchHelper mItemTouchHelper;
    public boolean focused = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_r);
        getSupportActionBar().setHomeButtonEnabled(true);

        fabFav = findViewById(R.id.fabFavorites);
        fabFav.setImageResource(android.R.drawable.ic_menu_add);
        fabFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.favorites_frame, new FavoritesAddFrag());
                ft.addToBackStack(null);
                ft.commit();
                fabFav.setVisibility(GONE);
                focused = false;
            }
        });
        // first run?
        if (!favFile.exists()) {
            try {
                LinkedHashMap<String, String> init = new LinkedHashMap<>();
                FileOutputStream fos = new FileOutputStream(favFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(init);
                oos.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FavoritesListAdapter adapter = new FavoritesListAdapter(this, this);
        RecyclerView recyclerView = findViewById(R.id.recyclerDerp);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.Callback callback = new FavoritesCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle("Favorites");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (focused == false) {
                    getFragmentManager().popBackStack();
                    focused = true;
            } else {
                    this.finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}