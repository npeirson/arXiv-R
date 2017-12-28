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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by the Kwisatz Haderach on 16/12/2017
 *
 * Used pieces from a lot of people
 * Check out the acknowledgements section in "about"
 */

public class RssFeedListAdapter
        extends RecyclerView.Adapter<RssFeedListAdapter.FeedModelViewHolder> {

    private List<RssFeedModel> mRssFeedModels;
    public View XrssFeedView;

    public class FeedModelViewHolder extends RecyclerView.ViewHolder {
        public View rssFeedView;
        public String status;

        public FeedModelViewHolder(View v) {
            super(v);
            rssFeedView = v;
            XrssFeedView = v;

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
                        String docname = "test13";
                        File docfile = new File((Environment.getExternalStorageDirectory() + "/arXiv/" + docname + ".pdf"));
                        Log.d("download", " ------ >>>>>" + docfile.exists());
                        if (docfile.exists()) {
                            itemView.findViewById(R.id.buttonRead).setVisibility(itemView.VISIBLE);
                        } else {
                            itemView.findViewById(R.id.buttonDownload).setVisibility(itemView.VISIBLE);
                        }

                        // bookmark dynamics
                        final RssFeedModel cardId = mRssFeedModels.get(getAdapterPosition());
                        final String trimmed = cardId.id.replace("http://arxiv.org/abs/","");
                        // does bookmark exist?
                        final File bmFile = new File(itemView.getContext().getFilesDir().getAbsolutePath() + "/bookmarks");
                        FileInputStream fis = null;
                        try{
                            fis = new FileInputStream(bmFile);
                            byte fileContent[] = new byte[(int)bmFile.length()];
                            fis.read(fileContent);
                            String bm = new String(fileContent);
                            if (bm.contains(trimmed)){
                                // change to bookmarked state
                                itemView.findViewById(R.id.buttonBookmarked).setVisibility(itemView.VISIBLE);
                            } else {
                                itemView.findViewById(R.id.buttonBookmark).setVisibility(itemView.VISIBLE);
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try{
                                if (fis != null) {
                                    fis.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        // bookmark button press
                        itemView.findViewById(R.id.buttonBookmark).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                        FileOutputStream fos = null;
                        FileInputStream fin = null;
                                try {
                                    fin = new FileInputStream(bmFile);
                                    byte fileContent[] = new byte[(int)bmFile.length()];
                                    fin.read(fileContent);
                                    String bm = new String(fileContent);
                                    fos = new FileOutputStream(bmFile);
                                    String add = bm + "\n" + trimmed;
                                    fos.write(add.getBytes());
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {
                                    itemView.findViewById(R.id.buttonBookmarked).setVisibility(itemView.VISIBLE);
                                    //itemView.findViewById(R.id.buttonBookmark).setVisibility(itemView.GONE); // for some reason this flips out??
                                    try {
                                        fin.close();
                                        fos.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });

                        itemView.findViewById(R.id.buttonBookmarked).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FileOutputStream fos = null;
                                FileInputStream fin = null;
                                try {
                                    fin = new FileInputStream(bmFile);
                                    byte fileContent[] = new byte[(int)bmFile.length()];
                                    fin.read(fileContent);
                                    String bm = new String(fileContent);
                                    fos = new FileOutputStream(bmFile);
                                    String remove = bm.replace("\n" + trimmed, "");
                                    fos.write(remove.getBytes());
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {
                                    itemView.findViewById(R.id.buttonBookmark).setVisibility(itemView.VISIBLE);
                                    itemView.findViewById(R.id.buttonBookmarked).setVisibility(itemView.GONE);
                                    try {
                                        fin.close();
                                        fos.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
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
                                Log.d("download","about to open");
                                File pdfFile = new File(Environment.getExternalStorageDirectory() + "/arXiv/" + "test13.pdf");  // -> filename = maven.pdf
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
                                dl.execute("https://arxiv.org/pdf/1712.07660.pdf", "test13.pdf");
                                Log.d("ummmm", "well " + dl.getStatus());
                                XrssFeedView = itemView;
                                if (dl.getStatus() == AsyncTask.Status.RUNNING) {
                                    itemView.findViewById(R.id.buttonDownload).setVisibility(itemView.GONE);
                                    itemView.findViewById(R.id.buttonDownloading).setVisibility(itemView.VISIBLE);
                                }
                            }
                        });
                    } else {
                        // collapse view
                        itemView.findViewById(R.id.textViewSummary).setVisibility(itemView.GONE);
                        itemView.findViewById(R.id.buttonBookmark).setVisibility(itemView.GONE);
                        itemView.findViewById(R.id.buttonBookmarked).setVisibility(itemView.GONE);
                        itemView.findViewById(R.id.buttonShare).setVisibility(itemView.GONE);
                        itemView.findViewById(R.id.buttonRead).setVisibility(itemView.GONE);
                        itemView.findViewById(R.id.buttonDownload).setVisibility(itemView.GONE);
                        itemView.findViewById(R.id.buttonDownloading).setVisibility(itemView.GONE);
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

    public class DownloadFile extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
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
            XrssFeedView.findViewById(R.id.buttonDownloading).setVisibility(XrssFeedView.GONE);
            XrssFeedView.findViewById(R.id.buttonRead).setVisibility(XrssFeedView.VISIBLE);
        }
    }
}



