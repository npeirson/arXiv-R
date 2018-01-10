package atreides.house.arxiv_r;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

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

                Log.d("onclickthing1", key);
                Log.d("value of ",value);

                File favFile = new File(getContext().getFilesDir().getAbsolutePath() + "/favorites");
                try {
                    FileInputStream fis = new FileInputStream(favFile);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    ArrayList<String> cFavs = (ArrayList<String>) ois.readObject();
                    ois.close();
                    fis.close();
                    if (cFavs.contains(value)) {
                        Toast.makeText(view.getContext(), key + " already in favorites!", Toast.LENGTH_SHORT).show();
                    } else {
                        // add selected to favorites
                        cFavs.add(value);
                        FileOutputStream fos = new FileOutputStream(favFile);
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        oos.writeObject(cFavs);
                        oos.close();
                        fos.close();
                        // now destroy fragment
                        getFragmentManager().popBackStack();
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
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
}
