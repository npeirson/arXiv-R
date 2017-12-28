package atreides.house.arxiv_r;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by the Kwisatz Haderach on 12/14/2017.
 *
 * Apparently I need a View Holder...
 */

public class ELVAdapter extends Fragment {
    View myView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.favorites_add_layout, container, false);
        return myView;
    }

    public static class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context context;
        private List<String> listDataHeader;
        private HashMap<String,List<String>> listHashMap;

        public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listHashMap) {
            this.context = context;
            this.listDataHeader = listDataHeader;
            this.listHashMap = listHashMap;
        }

        @Override
        public int getGroupCount() {
            return listDataHeader.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return listHashMap.get(listDataHeader.get(i)).size();
        }

        @Override
        public Object getGroup(int i) {
            return listDataHeader.get(i);
        }

        @Override
        public Object getChild(int i, int i1) {
            return listHashMap.get(listDataHeader.get(i)).get(i1);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            String headerTitle = (String)getGroup(i);
            if (view == null) {
                LayoutInflater inf = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inf.inflate(R.layout.group_items,null);
            }
            TextView favHeader = (TextView)view.findViewById(R.id.groupHeadingTextView);
            favHeader.setText(headerTitle);
            return view;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            final String childText = (String)getChild(i,i1);
            if(view == null) {
                LayoutInflater inf = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inf.inflate(R.layout.child_items,null);
            }
            TextView favChild = view.findViewById(R.id.groupChildTextView);
            favChild.setText(childText);
            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        getActivity().setTitle(R.string.favoritesAddTitle);
    }
}
