package atreides.house.arxiv_r;

import java.io.File;
import java.io.IOException;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;


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
};
