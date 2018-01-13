package atreides.house.arxiv_r;

import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedHashMap;

/**
 * Created by the Kwisatz Haderach on 1/9/2018.
 */

public class FavoritesInterfaces {
    public interface touchHelperAdapter {
        boolean onItemMove(int fromPosition, int toPosition);
        void onItemDismiss(int position);
    }
    public interface touchHelperHolder {
        void onItemSelected();
        void onItemClear();
    }
    public interface OnStartDragListener {
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }

    static LinkedHashMap<String, String> getFavorites() {
        LinkedHashMap<String,String> favorites = new LinkedHashMap<>();
        File favFile = new File(Environment.getDataDirectory() + "/data/atreides.house.arxiv_r/files/favorites");
        try {
            FileInputStream fis = new FileInputStream(favFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            favorites = (LinkedHashMap<String,String>) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return favorites;
    }
}
