package atreides.house.arxiv_r;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by obaro on 27/11/2016.
 * Check him out: https://github.com/obaro/
 * Thanks for sharing, obaro
 *
 * Modifications by the Kwisatz Haderach on 16/12/2017
 */

public class RssFeedListAdapter
        extends RecyclerView.Adapter<RssFeedListAdapter.FeedModelViewHolder> {

    private List<RssFeedModel> mRssFeedModels;

    public static class FeedModelViewHolder extends RecyclerView.ViewHolder {
        private View rssFeedView;

        public FeedModelViewHolder(View v) {
            super(v);
            rssFeedView = v;

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int sumState = itemView.findViewById(R.id.textViewSummary).getVisibility();
                    if (sumState != 0) {
                        itemView.findViewById(R.id.textViewSummary).setVisibility(itemView.VISIBLE);
                    } else {
                        itemView.findViewById(R.id.textViewSummary).setVisibility(itemView.GONE);
                    }
                }
            });
        }
    }

    public RssFeedListAdapter(List<RssFeedModel> rssFeedModels) {
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
        final RssFeedModel rssFeedModel = mRssFeedModels.get(position);
        ((TextView)holder.rssFeedView.findViewById(R.id.textViewTitle)).setText(rssFeedModel.title);
        ((TextView)holder.rssFeedView.findViewById(R.id.textViewSummary)).setText(rssFeedModel.summary);
        ((TextView)holder.rssFeedView.findViewById(R.id.textViewAuthors)).setText(rssFeedModel.author); // TODO dynamic adaptation
        ((TextView)holder.rssFeedView.findViewById(R.id.textViewPublished)).setText("Published: " + rssFeedModel.published);
        ((TextView)holder.rssFeedView.findViewById(R.id.textViewUpdated)).setText("Updated: " + rssFeedModel.updated);
    }

    @Override
    public int getItemCount() {
        return mRssFeedModels.size();
    }

}
