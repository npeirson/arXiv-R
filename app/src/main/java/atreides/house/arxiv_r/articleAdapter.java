package atreides.house.arxiv_r;

/**
 * Created by the Kwisatz Haderach on 12/15/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView.Adapter
 * RecyclerView.ViewHolder
 */

public class articleAdapter extends RecyclerView.Adapter<articleAdapter.articleViewHolder>{

    private Context mCtx;
    private List<article> articleList = new ArrayList<>();

    public articleAdapter(Context mCtx, List<article> articleList) {
        this.mCtx = mCtx;
        this.articleList = articleList;
    }

    @Override
    public articleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_layout, null);
        articleViewHolder holder = new articleViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(articleViewHolder holder, int position) {
        article article = articleList.get(position);

        holder.textViewTitle.setText(article.getTitle());
        holder.textViewSummary.setText(article.getSummary());
        holder.textViewAuthors.setText(article.getAuthors());
        holder.textViewPublished.setText(article.getPublished());
        holder.textViewUpdated.setText(article.getUpdated());
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    class articleViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle, textViewSummary, textViewAuthors, textViewPublished, textViewUpdated;

        public articleViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewSummary = itemView.findViewById(R.id.textViewSummary);
            textViewAuthors = itemView.findViewById(R.id.textViewAuthors);
            textViewPublished = itemView.findViewById(R.id.textViewPublished);
            textViewUpdated = itemView.findViewById(R.id.textViewUpdated);
        }
    }
}
