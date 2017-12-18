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
        }
    }

    public RssFeedListAdapter(List<RssFeedModel> rssFeedModels) {
        mRssFeedModels = rssFeedModels;
    }

    @Override
    public FeedModelViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_layout, parent, false); //was item_rss_feed
        FeedModelViewHolder holder = new FeedModelViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(FeedModelViewHolder holder, int position) {
        final RssFeedModel rssFeedModel = mRssFeedModels.get(position);
        Log.d("Adapter","feed model position = " + rssFeedModel);
        ((TextView)holder.rssFeedView.findViewById(R.id.textViewTitle)).setText(rssFeedModel.title);
        ((TextView)holder.rssFeedView.findViewById(R.id.textViewSummary)).setText(rssFeedModel.summary);
        ((TextView)holder.rssFeedView.findViewById(R.id.textViewAuthors)).setText(rssFeedModel.author);
        ((TextView)holder.rssFeedView.findViewById(R.id.textViewPublished)).setText(rssFeedModel.published);
        ((TextView)holder.rssFeedView.findViewById(R.id.textViewUpdated)).setText(rssFeedModel.updated);
    }

    @Override
    public int getItemCount() {
        Log.d("Adapter","Item count = "+ mRssFeedModels.size());
        return mRssFeedModels.size();
    }

}
