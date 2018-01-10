package atreides.house.arxiv_r;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ArrayRes;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by the Kwisatz Haderach on 1/9/2018.
 */

public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesListAdapter.viewHolder> implements FavoritesInterfaces.touchHelperAdapter {

    ArrayList<String> mFavorites = new ArrayList<>(FavoritesInterfaces.getFavorites());
    //private final AdapterView.OnItemLongClickListener mClickListener;
    private final FavoritesInterfaces.OnStartDragListener mDragStartListener;

    //public FavoritesListAdapter(AdapterView.OnItemLongClickListener mClickListener) {
     public FavoritesListAdapter(Context context, FavoritesInterfaces.OnStartDragListener dragStartListener) {
        //this.mClickListener = mClickListener;
         mDragStartListener = dragStartListener;
    }

    @Override
    public FavoritesListAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_list_item, parent, false);
        viewHolder itemViewHolder = new viewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final FavoritesListAdapter.viewHolder holder, int position) {
        holder.textView.setText(mFavorites.get(position));
        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
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
        Collections.swap(mFavorites, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
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
}

