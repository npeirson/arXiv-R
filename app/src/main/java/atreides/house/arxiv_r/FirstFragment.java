package atreides.house.arxiv_r;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by the Kwisatz Haderach on 12/14/2017.
 */

public class FirstFragment extends Fragment implements articleResultReceiver.Receiver {
    // recyclerView variables
        List<article> articleList;
    // other variables
    View myView;
    public articleResultReceiver mReceiver;
    private RecyclerView mRecyclerView;
    public ArrayList<String> results = new ArrayList<>(Arrays.asList("title","summary","author","published","updated"));
    public ArrayList<String> mFeedModelList = new ArrayList<>(4);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mReceiver = new articleResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        String category = "cs"; // explicit for easy swapping
        // go fetch
        Intent i = new Intent(FirstFragment.this.getActivity(), feedHandler.class);
        i.putExtra("category",category);
        i.putExtra("receiverName", mReceiver);
        getActivity().startService(i);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.first_layout, container, false);
        //mRecyclerView = myView.findViewById(R.id.recyclerView);
        //mRecyclerView.setHasFixedSize(true);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRecyclerView.setAdapter(new RssFeedListAdapter(results));
        //mRecyclerView.setAdapter(new RssFeedListAdapter(mFeedModelList));
        /**
        xRecyclerView = myView.findViewById(R.id.recyclerView);
        xRecyclerView.setHasFixedSize(true);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        xLayoutManager = new LinearLayoutManager(getActivity());
         */
        //articleAdapter adapter = new articleAdapter(getContext(), articleList);
        //RssFeedListAdapter adapter2 = new RssFeedListAdapter();

        return myView;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
            mFeedModelList = resultData.getStringArrayList("mFeedModelList");
            ArrayList<String> results = resultData.getStringArrayList("rssShit");
            mRecyclerView = myView.findViewById(R.id.recyclerView);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setAdapter(new RssFeedListAdapter(results));

            /**
            articleList = new ArrayList<>();
            articleList.add(
                    new article(
                            1,
                            results.get(0),
                            results.get(1),
                            results.get(2),
                            results.get(3),
                            results.get(4)
                    )

            );
             */

    }
}
