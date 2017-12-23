package atreides.house.arxiv_r;

import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
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
    private String docname;

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
                        // expand view
                        itemView.findViewById(R.id.textViewSummary).setVisibility(itemView.VISIBLE);
                        itemView.findViewById(R.id.buttonBookmark).setVisibility(itemView.VISIBLE);
                        itemView.findViewById(R.id.buttonShare).setVisibility(itemView.VISIBLE);

                        // workaround for dynamic button text
                        String docname = "maven";
                        File docfile = new File((Environment.getExternalStorageDirectory() + "/arXiv/" + docname + ".pdf"));
                        Log.d("download"," ------ >>>>>" + docfile.exists());
                        if (docfile.exists()) {
                            itemView.findViewById(R.id.buttonRead).setVisibility(itemView.VISIBLE);
                        } else {
                            itemView.findViewById(R.id.buttonDownload).setVisibility(itemView.VISIBLE);
                        }

                        // bookmark button press
                        itemView.findViewById(R.id.buttonBookmark).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.d("you pressed it","noob face");
                            }
                        });

                        // share button press
                        itemView.findViewById(R.id.buttonShare).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent sdi = new Intent(android.content.Intent.ACTION_SEND);
                                sdi.setType("text/plain");
                                sdi.putExtra(android.content.Intent.EXTRA_SUBJECT, "arXiv paper");
                                sdi.putExtra(android.content.Intent.EXTRA_TEXT, "check out this dope paper I found");
                                view.getContext().startActivity(Intent.createChooser(sdi, "Share via"));
                            }
                        });

                        // read button press
                        itemView.findViewById(R.id.buttonDownload).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.d("download","about to download");
                                new DownloadFile().execute("https://arxiv.org/pdf/1712.07660", "test1.pdf");
                                Log.d("download","download complete");
                                itemView.findViewById(R.id.buttonDownload).setVisibility(itemView.GONE);
                                itemView.findViewById(R.id.buttonRead).setVisibility(itemView.VISIBLE);

                            }
                        });
                        itemView.findViewById(R.id.buttonRead).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.d("download","about to open");
                                File pdfFile = new File(Environment.getExternalStorageDirectory() + "/arXiv/" + "test1.pdf");  // -> filename = maven.pdf
                                Uri path = Uri.fromFile(pdfFile);
                                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                                pdfIntent.setDataAndType(path, "application/pdf");
                                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                try{
                                    view.getContext().startActivity(pdfIntent);
                                }catch(ActivityNotFoundException e){
                                    Toast.makeText(view.getContext(), "No PDF application found.", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    } else {
                        // collapse view
                        itemView.findViewById(R.id.textViewSummary).setVisibility(itemView.GONE);
                        itemView.findViewById(R.id.buttonBookmark).setVisibility(itemView.GONE);
                        itemView.findViewById(R.id.buttonShare).setVisibility(itemView.GONE);
                        itemView.findViewById(R.id.buttonRead).setVisibility(itemView.GONE);

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

