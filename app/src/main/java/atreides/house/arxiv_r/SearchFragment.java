package atreides.house.arxiv_r;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by the Kwisatz Haderach on 12/14/2017.
 */

public class SearchFragment extends Fragment implements View.OnClickListener {
    View myView;
    public String oldTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oldTitle = (String) getActivity().getTitle();
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

        spinner1.setSelection(1);
        spinner2.setSelection(0);
        spinner3.setSelection(2);

        // spinner listeners
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
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // search button listener
        final Button searchButton = myView.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                // search parameters
                int sspos = ((Spinner)myView.findViewById(R.id.search_subject_spinner)).getSelectedItemPosition();
                String sq1 = "";
                String sq2 = "";
                String sq3 = "";
                if (sspos == 1) {
                    Spinner sps = myView.findViewById(R.id.search_subject_physics_spinner);
                    int p1 = sps.getSelectedItemPosition();
                } else { int p1 = sspos; }

                EditText et1 = myView.findViewById(R.id.search_edittext1);
                EditText et2 = myView.findViewById(R.id.search_edittext2);
                EditText et3 = myView.findViewById(R.id.search_edittext3);
                if (et1.getText().toString().isEmpty() == false) {
                    String qt1 = searchSpinnerChecker((Spinner)myView.findViewById(R.id.search_spinner1));
                    String qs1 = et1.getText().toString();
                    sq1 = qt1 + ":" + qs1;
                }
                if (et2.getText().toString().isEmpty() == false) {
                    String qt2 = searchSpinnerChecker((Spinner)myView.findViewById(R.id.search_spinner2));
                    String qs2 = et2.getText().toString();
                    if ( sq1 != "" ) {
                        sq2 = "AND" + qt2 + ":" + qs2;
                    } else {
                        sq1 = qt2 + ":" + qs2;
                    }
                }
                if (et3.getText().toString().isEmpty() == false) {
                    String qt3 = searchSpinnerChecker((Spinner)myView.findViewById(R.id.search_spinner3));
                    String qs3 = et3.getText().toString();
                    if ( sq1 != "" ) {
                        if ( sq2 != "" ) {
                            sq3 = "AND" + qt3 + ":" + qs3;
                        } else {
                            sq2 = "AND" + qt3 + ":" + qs3;
                        }
                    } else {
                        sq3 = "AND" + qt3 + ":" + qs3;
                    }

                }
                if (sq1 == "" && sq2 == "" && sq3 == "") {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Enter at least one query",
                            Toast.LENGTH_LONG).show();
                } else {
                    // do the search!
                    String query = "search_query=" + sq1 + sq2 + sq3;
                    ((MainActivity)getActivity()).search(query);
                }
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

        getActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE); // show floating action button
        getActivity().setTitle(oldTitle); // return to previous title

    }
    public String searchSpinnerChecker(Spinner spinner) {
        String value = "";
        int pos = spinner.getSelectedItemPosition();
        if ( pos == 0) {
            value = "au";
        } else if ( pos == 1) {
            value = "ti";
        } else if ( pos == 2) {
            value = "abs";
        } else if ( pos == 3) {
            value = "cat";
        } else if ( pos == 4 ) {
            value = "co";
        } else if ( pos == 5) {
            value = "jr";
        } else if ( pos == 6) {
            value = "rn";
        } else if ( pos == 7) {
            value = "id";
        } else if ( pos == 8) {
            value = "all";
        } else {
            Log.d("Search","The spinners are jacked.");
        }
        return value;
    }
}
