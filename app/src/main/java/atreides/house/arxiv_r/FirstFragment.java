package atreides.house.arxiv_r;

import android.Manifest;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Created by the Kwisatz Haderach on 12/14/2017.
 */

public class FirstFragment extends Fragment {
    View myView;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FirstFragment", "Fragment Created!");
        this.requestPermissions(PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ParcelableArrayList pal = getArguments().getParcelable("articles");
        Log.d("FirstFragment", pal.getThing().toString());

        List<RssFeedModel> mFeedModelList = pal.getThing();

        myView = inflater.inflate(R.layout.first_layout, container, false);
        mRecyclerView = myView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new RssFeedListAdapter(mFeedModelList));
        return myView;
    }
}
