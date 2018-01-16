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
        // also, this is how it's done: http://export.arxiv.org/api/query?search_query=cat:astro-ph+cat:cs*&sortBy=lastUpdatedDate&sortOrder=ascending
        map.put("Custom Favorite...","custom");
        map.put("Astrophysics","cat:astro-ph");
        map.put("Computer Science","cat:cs*");
        map.put("Condensed Matter","cat:cond-mat*");
        map.put("General Physics","cat:physics-gen.ph");
        map.put("General Relativity","cat:gr-qc");
        map.put("High Energy Physics","cat:hep*");
        map.put("Mathematical Physics","cat:math-ph");
        map.put("Mathematics","cat:math*");
        map.put("Nonlinear Science","cat:nlin*");
        map.put("Nuclear Theory","cat:nucl-th");
        map.put("Quantitative Biology","cat:q-bio*");
        map.put("Quantum Physics","cat:quant-ph");
        map.put("Statistics","cat:stat*");
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