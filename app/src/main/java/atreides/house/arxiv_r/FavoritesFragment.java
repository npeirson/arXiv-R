package atreides.house.arxiv_r;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Created by the Kwisatz Haderach on 12/14/2017.
 */

public class FavoritesFragment extends Fragment {
    View myView;
    File favFile = new File(getActivity().getBaseContext().getFilesDir().getAbsolutePath() + "bookmarks");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.favorites_layout, container, false);

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
        return myView;
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.favoritesTitle);
        //if (FileUtils.contentEquals())
    }
}
