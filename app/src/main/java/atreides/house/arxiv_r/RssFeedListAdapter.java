package atreides.house.arxiv_r;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by obaro on 27/11/2016.
 * Check him out: https://github.com/obaro/
 * Thanks for sharing, obaro
 *
 * Customizations made by the Kwisatz Haderach on 15/12/2017.
 */

public class RssFeedListAdapter
        extends RecyclerView.Adapter<RssFeedListAdapter.FeedModelViewHolder> {

    private ArrayList<String> mRssFeedModels;

    public static class FeedModelViewHolder extends RecyclerView.ViewHolder {
        private View rssFeedView;

        public FeedModelViewHolder(View v) {
            super(v);
            rssFeedView = v;
        }
    }

    public RssFeedListAdapter(ArrayList<String> rssFeedModels) {
        mRssFeedModels = rssFeedModels;
    }

    @Override
    public FeedModelViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_layout, parent, false);
        FeedModelViewHolder holder = new FeedModelViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(FeedModelViewHolder holder, int position) {
        ((TextView)holder.rssFeedView.findViewById(R.id.textViewTitle)).setText(mRssFeedModels.get(0));
        ((TextView)holder.rssFeedView.findViewById(R.id.textViewSummary)).setText(mRssFeedModels.get(1));
        ((TextView)holder.rssFeedView.findViewById(R.id.textViewAuthors)).setText(mRssFeedModels.get(2));
        ((TextView)holder.rssFeedView.findViewById(R.id.textViewPublished)).setText(mRssFeedModels.get(3));
        ((TextView)holder.rssFeedView.findViewById(R.id.textViewUpdated)).setText(mRssFeedModels.get(4));
    }

    @Override
    public int getItemCount() {
        return mRssFeedModels.size();
    }
}