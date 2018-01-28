package atreides.house.arxiv_r;

import android.Manifest;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by the Kwisatz Haderach on 12/14/2017.
 */

public class FirstFragment extends Fragment {
    View myView;

    public RecyclerView.Adapter mAdapter;
    public List<RssFeedModel> mFeedModelList;
    public String fp = null;
    public int articlesPerPull = 10; // placeholder for user option
    public int articleCounter = articlesPerPull + 1;
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
        assert pal != null;
        mFeedModelList = pal.getThing();
        fp = getArguments().getString("category");
        myView = inflater.inflate(R.layout.first_layout, container,false);
        RecyclerView mRecyclerView = myView.findViewById(R.id.recyclerView);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RssFeedListAdapter(mFeedModelList);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            boolean loading = true;
            int pastVisiblesItems, visibleItemCount, totalItemCount;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            Snackbar.make(getView(), "Fetching more articles...", Snackbar.LENGTH_LONG);
                            loading = false;
                            Log.v("article fetcher", "Scrolled to bottom");
                            //Do pagination.. i.e. fetch new data
                            new FetchFeedTask(fp, articleCounter, getContext(), getParentFragment());
                            articleCounter = articleCounter + articlesPerPull;
                            // get our new data
                            Bundle oldArgs = getArguments();
                            Boolean timeout = true;

                                if (getArguments() == oldArgs) {
                                    Log.d("just","sittin on a dock on a bay");
                                } else {
                                    timeout = false;
                                }

                            if (!timeout) {
                                fp = getArguments().getString("category");
                                ParcelableArrayList pal = getArguments().getParcelable("articles");
                                if (pal != null) {
                                    mFeedModelList.addAll(pal.getThing());
                                    mAdapter.notifyItemRangeInserted(articleCounter - 10, articleCounter);
                                }
                            } else {
                                Toast.makeText(getContext(),"Something went wrong...", Toast.LENGTH_LONG).show();
                            }
                            }
                    }
                }
            }
        });

        return myView;
    }
    public void newContent() {
        // placeholder for if the user scrolls to the bottom like a noob

    }
}
