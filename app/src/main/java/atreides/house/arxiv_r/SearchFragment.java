package atreides.house.arxiv_r;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by the Kwisatz Haderach on 12/14/2017.
 */

public class SearchFragment extends Fragment implements View.OnClickListener {
    View myView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.search_layout, container, false);

        // hide floating action button
        getActivity().findViewById(R.id.fab).setVisibility(View.GONE);

        // initialize spinners
        Spinner spinner1 = myView.findViewById(R.id.search_spinner1);
        Spinner spinner2 = myView.findViewById(R.id.search_spinner2);
        Spinner spinner3 = myView.findViewById(R.id.search_spinner3);

        /*
        ArrayAdapter spinner1adapter = (ArrayAdapter) spinner1.getAdapter();
        ArrayAdapter spinner2adapter = (ArrayAdapter) spinner2.getAdapter();
        ArrayAdapter spinner3adapter = (ArrayAdapter) spinner3.getAdapter();
        */

        spinner1.setSelection(1);
        spinner2.setSelection(0);
        spinner3.setSelection(2);


        final Spinner searchSubjectSpinner = myView.findViewById(R.id.search_subject_spinner);
        searchSubjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // physics sub-spinner
                if (searchSubjectSpinner.getId() == R.id.search_subject_spinner) {
                    if (searchSubjectSpinner.getSelectedItemPosition() == 1) {
                        myView.findViewById(R.id.search_subject_physics_spinner).setVisibility(View.VISIBLE);
                    } else {
                            myView.findViewById(R.id.search_subject_physics_spinner).setVisibility(View.GONE);
                        }
                    }
                }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return myView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.searchTitle);
    }

    @Override
    public void onClick(View view) {

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // show floating action button
        getActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);

    }
}
