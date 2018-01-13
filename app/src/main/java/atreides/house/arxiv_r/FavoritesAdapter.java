package atreides.house.arxiv_r;

/**
 * Created by the Kwisatz Haderach on 1/5/2018.
 * Based on demonstration by Oleskii K. Thanks dude.
 * Check him out: https://github.com/shamanland
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class FavoritesAdapter extends BaseAdapter {
    private final ArrayList mData;

    public FavoritesAdapter() {
        mData = new ArrayList();
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        // oh look, it's the categories
        map.put("Custom Favorite...","url");
        map.put("Astrophysics","url");
        map.put("Computer Science","urk");
        map.put("Condensed Matter","urj");
        map.put("General Physics","urh");
        map.put("General Relativity","urg");
        map.put("High Energy Physics","urf");
        map.put("Mathematical Physics","urd");
        map.put("Mathematics","urs");
        map.put("Nonlinear Science","ura");
        map.put("Nuclear Theory","urp");
        map.put("Quantitative Biology","uro");
        map.put("Quantum Physics","rui");
        map.put("Statistics","uri");
        mData.addAll(map.entrySet());
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Map.Entry<String, String> getItem(int position) {
        return (Map.Entry) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;
        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_item, parent, false);
        } else {
            result = convertView;
        }
        Map.Entry<String, String> item = getItem(position);
        ((TextView) result.findViewById(android.R.id.text1)).setText(item.getKey());

        return result;
    }
}