package atreides.house.arxiv_r;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by the Kwisatz Haderach on 12/14/2017.
 */

public class FavoritesAddFragment extends Fragment {
    View myView;

    private ExpandableListView listView;
    private ELVAdapter.ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;
    private int legp = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.favorites_add_layout, container, false);

        listView = myView.findViewById(R.id.favoritesAddListView);
        initData();
        listAdapter = new ELVAdapter.ExpandableListAdapter(getActivity(),listDataHeader,listHash);
        listView.setAdapter(listAdapter);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                TextView tv = v.findViewById(R.id.groupChildTextView);
                CheckBox cb = v.findViewById(R.id.childCheckBox);
                String value = tv.getText().toString();
                int pressed = childPosition;
                int pressed2 = groupPosition;
                Log.d("Favorites",pressed2 + " , " + pressed + " , " + value);
                if (cb != null){
                    cb.toggle();
                }

                return true;
            }
        });

        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                // collapse previous group
                if (groupPosition != legp) {
                    listView.collapseGroup(legp);
                }
                legp = groupPosition;
            }
        });
        return myView;
    }
    
    private void initData() {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        // tier one
        listDataHeader.add("Physics");
        listDataHeader.add("Mathematics");
        listDataHeader.add("Computer Science");
        listDataHeader.add("Quantitative Biology");
        listDataHeader.add("Quantitative Finance");
        listDataHeader.add("Statistics");
        listDataHeader.add("Electrical Engineering and Systems Science");
        listDataHeader.add("Economics");

        // tier two
        List<String> Physics = new ArrayList<>();
        Physics.add("All Physics");
        Physics.add("Accelerator Physics");
        Physics.add("Applied Physics");
        Physics.add("Atmospheric and Oceanic Physics");
        Physics.add("Atomic Physics");
        Physics.add("Biological Physics");
        Physics.add("Chemical Physics");
        Physics.add("Classical Physics");
        Physics.add("Computational Physics");
        Physics.add("Data Analysis, Statistics and Probability");
        Physics.add("Fluid Dynamics");
        Physics.add("General Physics");
        Physics.add("Geophysics");
        Physics.add("History and Philosophy of Physics");
        Physics.add("Instrumentation and Detectors");
        Physics.add("Medical Physics");
        Physics.add("Optics");
        Physics.add("Physics Education");
        Physics.add("Physics and Society");
        Physics.add("Plasma Physics");
        Physics.add("Popular Physics");
        Physics.add("Space Physics");
        Physics.add("Quantum Physics");
        Physics.add("Astrophysics (all)");
        Physics.add("Astrophysics of Galaxies");
        Physics.add("Cosmology and Nongalactic Astrophysics");
        Physics.add("Earth and Planetary Astrophysics");
        Physics.add("High Energy Astrophysical Phenomena");
        Physics.add("Instrumentation and Methods for Astrophysics");
        Physics.add("Solar and Stellar Astrophysics");
        Physics.add("General Relativity and Quantum Cosmology");
        Physics.add("Condensed Matter (all)");
        Physics.add("Disordered Systems and Neural Networks");
        Physics.add("Materials Science");
        Physics.add("Mesoscale and Nanoscale Physics");
        Physics.add("Other Condensed Matter");
        Physics.add("Quantum Gases");
        Physics.add("Soft Condensed Matter");
        Physics.add("Statistical Mechanics");
        Physics.add("Strongly Correlated Electrons");
        Physics.add("Superconductivity");
        Physics.add("High Energy Physics (all)");
        Physics.add("High Energy Physics - Experiment");
        Physics.add("High Energy Physics - Lattice");
        Physics.add("High Energy Physics - Phenomenology");
        Physics.add("High Energy Physics - Theory");
        Physics.add("Mathematical Physics");
        Physics.add("Nonlinear Sciences (all)");
        Physics.add("Adaptation and Self-Organizing Systems");
        Physics.add("Cellular Automata and Lattice Gases");
        Physics.add("Chaotic Dynamics");
        Physics.add("Exactly Solvable and Integrable Systems");
        Physics.add("Pattern Formation and Solitons");
        Physics.add("Nuclear Experiment");
        Physics.add("Nuclear Theory");
        Physics.add("Atomic and Molecular Clusters");

        List<String> Mathematics = new ArrayList<>();
        Mathematics.add("All Mathematics");
        Mathematics.add("Algebraic Geometry");
        Mathematics.add("Algebraic Topology");
        Mathematics.add("Analysis of PDEs");
        Mathematics.add("Category Theory");
        Mathematics.add("Classical Analysis and ODEs");
        Mathematics.add("Combinatorics");
        Mathematics.add("Commutative Algebra");
        Mathematics.add("Complex Variables");
        Mathematics.add("Differential Geometry");
        Mathematics.add("Dynamical Systems");
        Mathematics.add("General Mathematics");
        Mathematics.add("General Topology");
        Mathematics.add("Geometric Topology");
        Mathematics.add("Group Theory");
        Mathematics.add("History and Overview");
        Mathematics.add("Information Theory");
        Mathematics.add("K-Theory and Homology");
        Mathematics.add("Logic");
        Mathematics.add("Mathematical Physics");
        Mathematics.add("Metric Geometry");
        Mathematics.add("Number Theory");
        Mathematics.add("Numerical Analysis");
        Mathematics.add("Operator Algebras");
        Mathematics.add("Optimization and Control");
        Mathematics.add("Probability");
        Mathematics.add("Quantum Algebra");
        Mathematics.add("Representation Theory");
        Mathematics.add("Rings and Algebras");
        Mathematics.add("Spectral Theory");
        Mathematics.add("Statistics Theory");

        List<String> CompSci = new ArrayList<>();
        CompSci.add("All Computer Science");
        CompSci.add("Artificial Intelligence");
        CompSci.add("Computation and Language");
        CompSci.add("Computational Complexity");
        CompSci.add("Computational Engineering");
        CompSci.add("Finance, and Science");
        CompSci.add("Computational Geometry");
        CompSci.add("Computer Science and Game Theory");
        CompSci.add("Computer Vision and Pattern Recognition");
        CompSci.add("Computers and Society");
        CompSci.add("Cryptography and Security");
        CompSci.add("Data Structures and Algorithms");
        CompSci.add("Databases");
        CompSci.add("Digital Libraries");
        CompSci.add("Discrete Mathematics");
        CompSci.add("Distributed, Parallel, and Cluster Computing");
        CompSci.add("Emerging Technologies");
        CompSci.add("Formal Languages and Automata Theory");
        CompSci.add("General Literature");
        CompSci.add("Graphics");
        CompSci.add("Hardware Architecture");
        CompSci.add("Human-Computer Interaction");
        CompSci.add("Information Retrieval");
        CompSci.add("Information Theory");
        CompSci.add("Learning");
        CompSci.add("Logic in Computer Science");
        CompSci.add("Mathematical Software");
        CompSci.add("Multiagent Systems");
        CompSci.add("Multimedia");
        CompSci.add("Networking and Internet Architecture");
        CompSci.add("Neural and Evolutionary Computing");
        CompSci.add("Numerical Analysis");
        CompSci.add("Operating Systems");
        CompSci.add("Performance");
        CompSci.add("Programming Languages");
        CompSci.add("Robotics");
        CompSci.add("Social and Information Networks");
        CompSci.add("Software Engineering");
        CompSci.add("Sound");
        CompSci.add("Symbolic Computation");
        CompSci.add("Systems and Control");
        CompSci.add("Other Computer Science");

        List<String> QuantBio = new ArrayList<>();
        QuantBio.add("All Quantitative Biology");
        QuantBio.add("Biomolecules");
        QuantBio.add("Cell Behavior");
        QuantBio.add("Genomics");
        QuantBio.add("Molecular Networks");
        QuantBio.add("Neurons and Cognition");
        QuantBio.add("Populations and Evolution");
        QuantBio.add("Quantitative Methods");
        QuantBio.add("Subcellular Processes");
        QuantBio.add("Tissues and Organs");
        QuantBio.add("Other Quantitative Biology");

        List<String> QuantFin = new ArrayList<>();
        QuantFin.add("All Quantitative Finance");
        QuantFin.add("Computational Finance");
        QuantFin.add("Economics");
        QuantFin.add("General Finance");
        QuantFin.add("Mathematical Finance");
        QuantFin.add("Portfolio Management");
        QuantFin.add("Pricing of Securities");
        QuantFin.add("Risk Management");
        QuantFin.add("Statistical Finance");
        QuantFin.add("Trading and Market Microstructure");

        List<String> Stats = new ArrayList<>();
        Stats.add("All Statistics");
        Stats.add("Applications");
        Stats.add("Computation");
        Stats.add("Machine Learning");
        Stats.add("Methodology");
        Stats.add("Statistics Theory");
        Stats.add("Other Statistics");

        List<String> EESS = new ArrayList<>();
        EESS.add("All EE and SS");
        EESS.add("Audio and Speech Processing");
        EESS.add("Image and Video Processing");
        EESS.add("Signal Processing");

        List<String> Econ = new ArrayList<>();
        Econ.add("All Economics");
        Econ.add("Econometrics");

        listHash.put(listDataHeader.get(0),Physics);
        listHash.put(listDataHeader.get(1),Mathematics);
        listHash.put(listDataHeader.get(2),CompSci);
        listHash.put(listDataHeader.get(3),QuantBio);
        listHash.put(listDataHeader.get(4),QuantFin);
        listHash.put(listDataHeader.get(5),Stats);
        listHash.put(listDataHeader.get(6),EESS);
        listHash.put(listDataHeader.get(7),Econ);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        getActivity().setTitle(R.string.favoritesAddTitle);
    }
}
