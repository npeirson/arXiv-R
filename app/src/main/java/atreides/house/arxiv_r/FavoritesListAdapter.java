package atreides.house.arxiv_r;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
    private LinkedHashMap<String, String> mFavorites;
    private final FavoritesInterfaces.OnStartDragListener mDragStartListener;

    FavoritesListAdapter(Context context, FavoritesInterfaces.OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;
        mFavorites = FavoritesInterfaces.getFavorites();
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
        mFavorites = hashSwap(mFavorites, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        final ArrayList mData;
        mData = new ArrayList();
        mData.addAll(mFavorites.entrySet());
        String keyDel = ((Map.Entry<String, String>) mData.get(position)).getKey();
        String valueDel = ((Map.Entry<String, String>) mData.get(position)).getValue();
        mFavorites = hashRemove(mFavorites, position);
        notifyDataSetChanged();
        Snackbar.make(view, "Item removed", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new undoListener(mFavorites,position,keyDel,valueDel)).show();
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
    private LinkedHashMap<String, String> hashSwap(LinkedHashMap<String, String> input, int index, int outdex) {
        LinkedHashMap<String, String> output = new LinkedHashMap<>();
        if (index >= 0 && index <= input.size()) {
            final ArrayList mData = new ArrayList();
            mData.addAll(input.entrySet());
            String keyIn = ((Map.Entry<String, String>) mData.get(index)).getKey();
            String valueIn = ((Map.Entry<String, String>) mData.get(index)).getValue();
            String keyOut = ((Map.Entry<String, String>) mData.get(outdex)).getKey();
            String valueOut = ((Map.Entry<String, String>) mData.get(outdex)).getValue();

            for (int i = 0; i < input.size(); i++) {
                if (i == index) {
                    output.put(keyOut, valueOut);
                } else if (i == outdex) {
                    output.put(keyIn, valueIn);
                } else {
                    output.put(((Map.Entry<String, String>) mData.get(i)).getKey(), ((Map.Entry<String, String>) mData.get(i)).getValue());
                }
            }
        }
        saveMyShit(output);
        return output;
    }

    /**
     * Removes an entry from a LinkedHashMap
     * .remove() didn't seem to be working for LinkedHashMaps... it didn't throw an error, but it also didn't... do... anything.
     */
    private LinkedHashMap<String, String> hashRemove(LinkedHashMap<String, String> input, int index) {
        LinkedHashMap<String, String> output = new LinkedHashMap<>();
        if (index >= 0 && index <= input.size()) {
            final ArrayList nData;
            boolean collapse = false;
            nData = new ArrayList();
            nData.addAll(input.entrySet());
            for (int i = 0; i < (input.size() - 1); i++) {
                if (collapse == false) {
                    if (i == index) {
                        collapse = true;
                        i--;
                    } else {
                        output.put(((Map.Entry<String, String>) nData.get(i)).getKey(), ((Map.Entry<String, String>) nData.get(i)).getValue());
                    }
                } else {
                    output.put(((Map.Entry<String, String>) nData.get(i+1)).getKey(), ((Map.Entry<String, String>) nData.get(i+1)).getValue());
                }
            }
        }
        saveMyShit(output);
        return output;
    }

    /**
     * Adds a value at a specific index.
     */
    private LinkedHashMap<String, String> hashAdd(LinkedHashMap<String, String> input, int index, String keyDel, String valueDel) {
        LinkedHashMap<String, String> output = new LinkedHashMap<>();
        if (index >= 0 && index <= (input.size() + 1)) {
            final ArrayList nData;
            boolean expanded = false;
            nData = new ArrayList();
            nData.addAll(input.entrySet());
            for (int i = 0; i <= (input.size()); i++) {
                if (expanded == false) {
                    if (i == index) {
                        expanded = true;
                        output.put(keyDel, valueDel);
                    } else {
                        output.put(((Map.Entry<String, String>) nData.get(i)).getKey(), ((Map.Entry<String, String>) nData.get(i)).getValue());
                    }
                } else {
                    output.put(((Map.Entry<String, String>) nData.get(i-1)).getKey(), ((Map.Entry<String, String>) nData.get(i-1)).getValue());
                }
            }
        }
        saveMyShit(output);
        return output;
    }

    /**
     * I dunno, what do you think it does?
     */
    private void saveMyShit(LinkedHashMap<String, String> shit) {
        File favFile = new File(Environment.getDataDirectory() + "/data/atreides.house.arxiv_r/files/favorites");
        try {
            FileOutputStream fos = new FileOutputStream(favFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(shit);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds the newly selected favorite
     */
    void oblivious(String key, String value) {
        mFavorites.put(key, value);
        notifyDataSetChanged();
        saveMyShit(mFavorites);
    }

    public class undoListener implements View.OnClickListener{
        LinkedHashMap<String,String> map;
        int pos;
        String keyDel, valueDel;

        undoListener(LinkedHashMap<String,String> hashmap, int position, String keyD, String valueD){
            map = hashmap;
            pos = position;
            keyDel = keyD;
            valueDel = valueD;
        }

        @Override
        public void onClick(View v) {
            // Code to undo the user's last action
            mFavorites = hashAdd(map,pos,keyDel,valueDel);
            notifyDataSetChanged();
        }
    }
}


