package atreides.house.arxiv_r;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by the Kwisatz Haderach on 12/14/2017.
 */

public class FavoritesFragment extends Fragment implements FavoritesInterfaces.OnStartDragListener {
    View myView;
    //File favFile = new File(getContext().getFilesDir().getAbsolutePath() + "/favorites");
    //File favFile = new File(Environment.getDataDirectory().getAbsolutePath() + "/favorites");
    File favFile = new File(Environment.getDataDirectory() + "/data/atreides.house.arxiv_r/files/favorites");
    private ItemTouchHelper mItemTouchHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // build on first run
        if (!favFile.exists()) {
            try {
                ArrayList<String> init = new ArrayList<>();
                FileOutputStream fos = new FileOutputStream(favFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(init);
                oos.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.favorites_layout, container, false);
        return myView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FavoritesListAdapter adapter = new FavoritesListAdapter(getActivity(), this);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerDerp);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper.Callback callback = new FavoritesCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        final FloatingActionButton fabFav = myView.findViewById(R.id.fabFavorites);
        fabFav.setImageResource(android.R.drawable.ic_menu_add);
        fabFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("FAB", "Fabulous!" + getView());
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new FavoritesAddFrag());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.favoritesTitle);
        FavoritesInterfaces.getFavorites();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}

