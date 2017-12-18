package atreides.house.arxiv_r;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by the Kwisatz Haderach on 12/14/2017.
 */

public class FirstFragment extends Fragment{
    View myView;

    private RecyclerView mRecyclerView;
    //private SwipeRefreshLayout mSwipeLayout;
    private TextView mFeedTitleTextView;
    private TextView mFeedSummaryTextView;
    private TextView mFeedAuthorTextView;
    private TextView mFeedPublishedTextView;
    private TextView mFeedUpdatedTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FirstFragment","Fragment Created!");
        /**
         Intent i = new Intent(getActivity(), MainActivity.class);
         i.putExtra("receiverName", mReceiver);
         getActivity().startActivityIfNeeded(i,0);
         Log.d("FirstFragment","intent started <(o.o<)");
         */
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("FirstFragment", "onCreateView started");
        // get the birdie
        //Bundle resultData = getArguments();
        //String mFeedTitle = getArguments().getString("test");
        ParcelableArrayList pal = getArguments().getParcelable("articles");
        //Log.d("FirstFragment", mFeedTitle);
        Log.d("FirstFragment", pal.getThing().toString());


        List<RssFeedModel> mFeedModelList = pal.getThing();

        //String mFeedTitle = pal.getTitle();
        //List<RssFeedModel> mFeedModelList = getArguments().getParcelable("articles");
        //ArrayList<String> mResultData = resultData.getStringArrayList("contents");

        /**
         String mFeedTitle = mResultData.get(0);
         String mFeedSummary = mResultData.get(1);
        String mFeedAuthor = mResultData.get(2);
        String mFeedPublished = mResultData.get(3);
        String mFeedUpdated = mResultData.get(4);
         */
        // set the birdie
        myView = inflater.inflate(R.layout.first_layout, container, false);
        mRecyclerView = myView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new RssFeedListAdapter(mFeedModelList));
        mFeedTitleTextView = myView.findViewById(R.id.textViewTitle);
        /**
        mFeedSummaryTextView = (TextView) myView.findViewById(R.id.textViewSummary);
        mFeedAuthorTextView = (TextView) myView.findViewById(R.id.textViewAuthors);
        mFeedPublishedTextView = (TextView) myView.findViewById(R.id.textViewPublished);
        mFeedUpdatedTextView = (TextView) myView.findViewById(R.id.textViewUpdated);
         */
        // show me the birdie
        //mFeedTitleTextView.setText(mFeedTitle);
        /**
        mFeedSummaryTextView.setText(mFeedSummary);
        mFeedAuthorTextView.setText(mFeedAuthor);
        mFeedPublishedTextView.setText(mFeedPublished);
        mFeedUpdatedTextView.setText(mFeedUpdated);
         */
        //mRecyclerView.setAdapter(new RssFeedListAdapter(mFeedModelList));

        return myView;

    }
}
