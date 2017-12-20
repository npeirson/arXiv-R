package atreides.house.arxiv_r;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        return myView;
        //touch event?
        //myView.findViewById(R.id.textViewSummary).setVisibility(View.VISIBLE);
    }
}
