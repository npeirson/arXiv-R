package atreides.house.arxiv_r;

import android.Manifest;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Created by the Kwisatz Haderach on 12/14/2017.
 */

public class FirstFragment extends Fragment {
    View myView;

    public RecyclerView.Adapter mAdapter;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestPermissions(PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ParcelableArrayList pal = getArguments().getParcelable("articles");

        List<RssFeedModel> mFeedModelList = pal.getThing();

        myView = inflater.inflate(R.layout.first_layout, container, false);
        RecyclerView mRecyclerView = myView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new RssFeedListAdapter(mFeedModelList);
        mRecyclerView.setAdapter(mAdapter);
        return myView;
    }
    public void newContent() {
        // placeholder for if the user scrolls to the bottom like a noob

    }
}
