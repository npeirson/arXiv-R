package atreides.house.arxiv_r;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by the Kwisatz Haderach on 1/16/2018.
 */

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d("factory ","onGetViewFactory");
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
        }
    }


class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final int mCount = 10;
    private List<RssFeedModel> mWidgetItems = new ArrayList<>();
    private Context mContext;
    private int mAppWidgetId;

    public ListRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        // In onCreate() you setup any connections / cursors to your data source. Heavy lifting,
        // for example downloading or creating content etc, should be deferred to onDataSetChanged()
        // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.
        Log.d("factory ","onCreate");
        for (int i = 0; i < mCount; i++) {
            mWidgetItems.add(new RssFeedModel("Derp " + i,"herp lerp merp","Mr. Derpison","published","updated","id " + i));
        }
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        //The RemoteViewsFactory method getViewAt() returns a RemoteViews
        // object corresponding to the data at the specified position in
        // the data set. Here is an excerpt from the StackView Widget sample's
        // RemoteViewsFactory implementation:

        // Construct a remote views item based on the app widget item XML file,
        // and set the text based on the position.
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.list_layout);
        rv.setTextViewText(R.id.textViewSummary, mWidgetItems.get(i).title);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
    // adapter-like behaviors go here
}
