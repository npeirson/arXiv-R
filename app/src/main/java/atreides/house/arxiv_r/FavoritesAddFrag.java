package atreides.house.arxiv_r;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by the Kwisatz Haderach on 12/14/2017.
 */

public class FavoritesAddFrag extends Fragment {
    View myView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.favorites_add_layout, container, false);
        final ArrayList<String> favCats = new ArrayList<>();
        ListView mListView = myView.findViewById(android.R.id.list);
        // some day these really need to be expanded
        favCats.add("Astrophysics");
        favCats.add("Computer Science");
        favCats.add("Condensed Matter");
        favCats.add("General Physics");
        favCats.add("General Relativity");
        favCats.add("High Energy Physics");
        favCats.add("Mathematical Physics");
        favCats.add("Mathematics");
        favCats.add("Nonlinear Science");
        favCats.add("Nuclear Theory");
        favCats.add("Quantitative Biology");
        favCats.add("Quantum Physics");
        favCats.add("Statistics");
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, favCats);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("well,", String.valueOf(adapterView.getItemIdAtPosition(i)));

                //Intent intent = new Intent(getActivity(), MainActivity.class);
                //based on item add info to intent
                //startActivity(intent);
            }
        });

        return myView;
    }
}
