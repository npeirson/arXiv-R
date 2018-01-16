package atreides.house.arxiv_r;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedHashMap;

import static android.view.View.VISIBLE;

/**
 * Created by the Kwisatz Haderach on 12/14/2017.
 */

public class FavoritesAddFrag extends Fragment {
    View myView;
    final FavoritesAdapter adapter = new FavoritesAdapter();
    private FavoritesAddFrag mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.favorites_add_layout, container, false);
        final ListView mListView = myView.findViewById(android.R.id.list);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // this is where the click is recognized
                String value = adapter.getItem(pos).getValue();
                String key = adapter.getItem(pos).getKey();

                File favFile = new File(Environment.getDataDirectory() + "/data/atreides.house.arxiv_r/files/favorites");

                // check if it's a custom fav or pre-defined fav
                if (pos == 0) {
                // open custom favorite dialog for result
                    Log.d("Favorites","Custom favorite selected");
                } else {
                    try {
                        // check if already in favs
                        FileInputStream fis = new FileInputStream(favFile);
                        ObjectInputStream ois = new ObjectInputStream(fis);
                        LinkedHashMap<String, String> cFavs = (LinkedHashMap<String, String>) ois.readObject();
                        ois.close();
                        fis.close();
                        if (cFavs.get(key) != null) {
                            Toast.makeText(view.getContext(), key + " already in favorites!", Toast.LENGTH_SHORT).show();
                        } else {
                            // add selected to favorites
                            selectedListener.favoriteSelected(key, value);
                            getFragmentManager().popBackStack();
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return myView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.favoritesAddTitle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FloatingActionButton fabFav = getActivity().findViewById(R.id.fabFavorites);
        fabFav.setVisibility(VISIBLE);
        getActivity().setTitle("Favorites");
    }
    public interface onFavoriteSelectedListener {
        void favoriteSelected(String key, String value);
    }
    onFavoriteSelectedListener selectedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            selectedListener = (onFavoriteSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement some shit that you didn't implement.\njesus, you call yourself a programmer?\nrookie");
        }
    }
}
