package atreides.house.arxiv_r;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by the Kwisatz Haderach on 1/9/2018.
 * Adapted from Paul Burke's demonstration. Thank you sir.
 * Check him out: https://github.com/iPaulPro
 */

public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesListAdapter.viewHolder> implements FavoritesInterfaces.touchHelperAdapter {

    public View view;
    public LinkedHashMap<String, String> mFavorites = new LinkedHashMap<>();

    private final FavoritesInterfaces.OnStartDragListener mDragStartListener;

    public FavoritesListAdapter(Context context, FavoritesInterfaces.OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;

        File favFile = new File(Environment.getDataDirectory() + "/data/atreides.house.arxiv_r/files/favorites");
        try {
            FileInputStream fis = new FileInputStream(favFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            mFavorites = (LinkedHashMap<String, String>) ois.readObject();
            ois.close();
            fis.close();
            // notify dataset changed
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public FavoritesListAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_list_item, parent, false);
        viewHolder itemViewHolder = new viewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final FavoritesListAdapter.viewHolder holder, int position) {
        final ArrayList mSet;
        mSet = new ArrayList();
        mSet.addAll(mFavorites.entrySet());
        holder.textView.setText(((Map.Entry<String, String>) mSet.get(position)).getKey());
        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }

        });
    }

    @Override
    public int getItemCount() {
        return mFavorites.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Log.d("Trying to move an item", "or something like that");
        mFavorites = hashSwap(mFavorites, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        mFavorites = hashRemove(mFavorites, position);
        //mFavorites.remove(position);
        notifyItemRemoved(position);
        Snackbar.make(view, "Item removed", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new FavoritesInterfaces.undoListener()).show();
    }

    public static class viewHolder extends RecyclerView.ViewHolder implements FavoritesInterfaces.touchHelperHolder {

        public final TextView textView;
        public final ImageView handleView;

        public viewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
            handleView = itemView.findViewById(R.id.handle);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }

    }

    /**
     * swaps any two sets (keys and values) in a LinkedHashMap
     * it's basically Collections.swap, but for LinkedHashMaps.
     * used for re-ordering favorites when it so pleases the user
     */

    private static LinkedHashMap<String,String> hashSwap(LinkedHashMap<String, String> input, int index, int outdex) {
        LinkedHashMap<String,String> output = new LinkedHashMap<>();
        if (index >= 0 && index <= input.size()) {
            final ArrayList mData;
            mData = new ArrayList();
            mData.addAll(input.entrySet());
            String keyIn = ((Map.Entry<String, String>) mData.get(index)).getKey();
            String valueIn = ((Map.Entry<String, String>) mData.get(index)).getValue();
            String keyOut = ((Map.Entry<String, String>) mData.get(outdex)).getKey();
            String valueOut = ((Map.Entry<String, String>) mData.get(outdex)).getValue();

            for (int i = 0; i < input.size(); i++) {
                if (i == index) {
                    output.put(keyIn, valueIn);
                } else if (i == outdex) {
                    output.put(keyOut, valueOut);
                } else {
                    for (Map.Entry<String, String> entry : input.entrySet()) {
                        output.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
        return output;
    }
    /**
     * Removes an entry from a LinkedHashMap
     * .remove() didn't seem to be working for LinkedHashMaps... it didn't throw an error, but it also didn't... do... anything.
     */
}

