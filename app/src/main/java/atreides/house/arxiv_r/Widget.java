package atreides.house.arxiv_r;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Created by the Kwisatz Haderach on 1/16/2018.
 * Try looking into that place you dare not look.
 */

public class Widget extends AppWidgetProvider {
    public static final String TOAST_ACTION = "com.example.android.stackwidget.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.example.android.stackwidget.EXTRA_ITEM";

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(TOAST_ACTION)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
            Toast.makeText(context, "Touched view " + viewIndex, Toast.LENGTH_SHORT).show();
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            //updateWidget(context, appWidgetManager, appWidgetId);
            Intent intent = new Intent(context, WidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            rv.setRemoteAdapter(appWidgetIds[i], intent);

            Intent toastIntent = new Intent(context, AppWidgetProvider.class);
            toastIntent.setAction(Widget.TOAST_ACTION);
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.widgetListView, toastPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private void updateWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com"));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.theWidget, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        /*
        if (ACTION_SIMPLEAPPWIDGET.equals(intent.getAction())) {
            mCounter++;
            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.broadcast_widget);
            views.setTextViewText(R.id.tvWidget, Integer.toString(mCounter));
            // This time we dont have widgetId. Reaching our widget with that way.
            ComponentName appWidget = new ComponentName(context, BroadcastWidget.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidget, views);
        }
        */
    }

}
