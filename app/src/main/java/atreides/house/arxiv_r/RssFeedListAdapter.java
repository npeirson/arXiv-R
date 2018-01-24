package atreides.house.arxiv_r;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by the Kwisatz Haderach on 16/12/2017
 *
 * Used pieces from a lot of people
 * Check out the acknowledgements section in "about"
 */

// TODO verify that viewholder is working properly... I kinda doubt it.

public class RssFeedListAdapter
        extends RecyclerView.Adapter<RssFeedListAdapter.FeedModelViewHolder> {

    private List<RssFeedModel> mRssFeedModels;
    private ArrayList<String> bkmx;
    private View XrssFeedView;

    public class FeedModelViewHolder extends RecyclerView.ViewHolder {
        View rssFeedView;
        public String status;

        FeedModelViewHolder(View v) {
            super(v);
            rssFeedView = v;
            XrssFeedView = v;

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int sumState = itemView.findViewById(R.id.textViewSummary).getVisibility();
                    if (sumState != 0) {
                        // bookmark dynamics
                        final RssFeedModel cardId = mRssFeedModels.get(getAdapterPosition());
                        final String trimmed = cardId.id.replace("http://arxiv.org/abs/","");
                        // does bookmark exist?
                        final File bmFile = new File(itemView.getContext().getFilesDir().getAbsolutePath() + "/bookmarks");

                        // expand view
                        itemView.findViewById(R.id.textViewSummary).setVisibility(itemView.VISIBLE);
                        itemView.findViewById(R.id.buttonBookmark).setVisibility(itemView.VISIBLE);
                        itemView.findViewById(R.id.buttonShare).setVisibility(itemView.VISIBLE);

                        // workaround for dynamic button text
                        File docfile = new File((Environment.getExternalStorageDirectory() + "/arXiv/" + trimmed.replace("/","") + ".pdf"));
                        Log.d("download", " ------ >>>>>" + docfile.exists());
                        if (docfile.exists()) {
                            itemView.findViewById(R.id.buttonRead).setVisibility(itemView.VISIBLE);
                        } else {
                            itemView.findViewById(R.id.buttonDownload).setVisibility(itemView.VISIBLE);
                        }

                        FileInputStream fis;
                        try {
                            //fos = this.openFileOutput("bookmarks", Context.MODE_PRIVATE);
                            fis = new FileInputStream(bmFile);
                            ObjectInputStream ois = new ObjectInputStream(fis);
                            bkmx = (ArrayList<String>) ois.readObject();
                            if (bkmx.contains(trimmed)){
                                // change to bookmarked state
                                itemView.findViewById(R.id.buttonBookmarked).setVisibility(itemView.VISIBLE);
                            } else {
                                itemView.findViewById(R.id.buttonBookmark).setVisibility(itemView.VISIBLE);
                            }
                            ois.close();
                            fis.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // bookmark button press
                        itemView.findViewById(R.id.buttonBookmark).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    FileInputStream fis = new FileInputStream(bmFile);
                                    ObjectInputStream ois = new ObjectInputStream(fis);
                                    bkmx = (ArrayList<String>) ois.readObject();
                                    ois.close();
                                    fis.close();
                                    bkmx.add(trimmed);
                                    FileOutputStream fos = new FileOutputStream(bmFile);
                                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                                    oos.writeObject(bkmx);
                                    oos.close();
                                    fos.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    itemView.findViewById(R.id.buttonBookmarked).setVisibility(itemView.VISIBLE);
                                    itemView.findViewById(R.id.buttonBookmark).setVisibility(itemView.INVISIBLE);
                                }
                            }
                        });

                        itemView.findViewById(R.id.buttonBookmarked).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    FileInputStream fis = new FileInputStream(bmFile);
                                    ObjectInputStream ois = new ObjectInputStream(fis);
                                    bkmx = (ArrayList<String>) ois.readObject();
                                    ois.close();
                                    fis.close();
                                    bkmx.remove(trimmed);
                                    FileOutputStream fos = new FileOutputStream(bmFile);
                                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                                    oos.writeObject(bkmx);
                                    oos.close();
                                    fos.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    itemView.findViewById(R.id.buttonBookmark).setVisibility(itemView.VISIBLE);
                                    itemView.findViewById(R.id.buttonBookmarked).setVisibility(itemView.GONE);
                                }
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
                        itemView.findViewById(R.id.buttonRead).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                File pdfFile = new File(Environment.getExternalStorageDirectory() + "/arXiv/" + trimmed.replace("/","") + ".pdf");
                                Uri sharedFileUri = FileProvider.getUriForFile(view.getContext(), "atreides.house.arxiv_r.fileProvider", pdfFile);
                                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                                pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                pdfIntent.setDataAndType(sharedFileUri, "application/pdf");

                                try{
                                    view.getContext().startActivity(pdfIntent);
                                }catch(ActivityNotFoundException e){
                                    Toast.makeText(view.getContext(), "No PDF application found.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        // download button press
                        itemView.findViewById(R.id.buttonDownload).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.d("download", "about to download");
                                AsyncTask<String, Void, Void> dl = new DownloadFile();
                                dl.execute("https://arxiv.org/pdf/" + trimmed + ".pdf", trimmed.replace("/","") + ".pdf");
                                Log.d("ummmm", "well " + dl.getStatus());
                                XrssFeedView = itemView;
                                if (dl.getStatus() == AsyncTask.Status.RUNNING) {
                                    itemView.findViewById(R.id.buttonDownloading).setVisibility(itemView.VISIBLE);
                                    itemView.findViewById(R.id.buttonDownload).setVisibility(itemView.GONE);
                                }
                            }
                        });
                    } else {
                        // collapse view
                        itemView.findViewById(R.id.textViewSummary).setVisibility(itemView.GONE);
                        itemView.findViewById(R.id.buttonBookmark).setVisibility(itemView.GONE);
                        itemView.findViewById(R.id.buttonBookmarked).setVisibility(itemView.GONE);
                        itemView.findViewById(R.id.buttonShare).setVisibility(itemView.GONE);
                        itemView.findViewById(R.id.buttonDownload).setVisibility(itemView.GONE);
                        itemView.findViewById(R.id.buttonDownloading).setVisibility(itemView.GONE);
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
        // date string conversion and clean-up
        SimpleDateFormat of = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat nf = new SimpleDateFormat("dd MMMM yyyy");
        String pubstring = null;
        String upstring = null;
        try {
            Date pubdate = of.parse(rssFeedModel.published);
            Date update = of.parse(rssFeedModel.updated);
            pubstring = nf.format(pubdate);
            upstring = nf.format(update);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        // this is where an "et al." modifier could be added
        // post fields
        ((TextView)holder.rssFeedView.findViewById(R.id.textViewTitle)).setText(rssFeedModel.title);
        ((TextView)holder.rssFeedView.findViewById(R.id.textViewSummary)).setText(rssFeedModel.summary);
        ((TextView)holder.rssFeedView.findViewById(R.id.textViewAuthors)).setText(rssFeedModel.author);
        ((TextView)holder.rssFeedView.findViewById(R.id.textViewPublished)).setText("Published: " + pubstring);
        ((TextView)holder.rssFeedView.findViewById(R.id.textViewUpdated)).setText("Updated: " + upstring);
    }

    @Override
    public int getItemCount() {
        return mRssFeedModels.size();
    }

    public class DownloadFile extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];
            String fileName = strings[1];
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "arXiv");
            Log.d("DF","Gonna make dir");
            folder.mkdir();
            Log.d("DF","made dir");
            File pdfFile = new File(folder, fileName);
            try{
                Log.d("DF","Gonna make file");
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // sometimes you just have to spank it
            XrssFeedView.findViewById(R.id.buttonRead).setVisibility(XrssFeedView.VISIBLE);
            XrssFeedView.findViewById(R.id.buttonDownloading).setVisibility(XrssFeedView.GONE);
        }
    }
}



