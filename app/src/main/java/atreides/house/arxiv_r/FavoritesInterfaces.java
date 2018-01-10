package atreides.house.arxiv_r;

import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Created by the Kwisatz Haderach on 1/9/2018.
 */

public class FavoritesInterfaces {
    public interface touchHelperAdapter {
        boolean onItemMove(int fromPosition, int toPosition);
    }
    public interface touchHelperHolder {
        void onItemSelected();
        void onItemClear();
    }
    public interface OnStartDragListener {
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }

    public static ArrayList<String> getFavorites() {
        ArrayList<String> favorites = new ArrayList<>();
        //File favFile = new File(ctx.getFilesDir().getAbsolutePath() + "/favorites");
        //File favFile = new File(Environment.getDataDirectory().getAbsolutePath() + "/favorites");
        File favFile = new File(Environment.getDataDirectory() + "/data/atreides.house.arxiv_r/files/favorites");
        try {
            FileInputStream fis = new FileInputStream(favFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            favorites = (ArrayList<String>) ois.readObject();
            ois.close();
            fis.close();
            // notify dataset changed
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return favorites;
    }
}
