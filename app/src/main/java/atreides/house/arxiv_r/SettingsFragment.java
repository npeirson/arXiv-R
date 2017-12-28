package atreides.house.arxiv_r;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by the Kwisatz Haderach on 12/14/2017.
 */

public class SettingsFragment extends Fragment {
    View myView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.settings_layout, container, false);
        final Button button = myView.findViewById(R.id.settingsFavoritesBtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getActivity().setTitle("Favorites");
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new FavoritesFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return myView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        getActivity().setTitle(R.string.settingsTitle);
    }
}