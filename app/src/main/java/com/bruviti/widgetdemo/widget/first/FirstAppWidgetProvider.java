package com.bruviti.widgetdemo.widget.first;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.bruviti.widgetdemo.R;
import com.bruviti.widgetdemo.ui.ItemListActivity;

import static com.bruviti.widgetdemo.widget.first.FirstAppWidgetConfigureActivity.KEY_BUTTON_TEXT;
import static com.bruviti.widgetdemo.widget.first.FirstAppWidgetConfigureActivity.SHARED_PREFS;



public class FirstAppWidgetProvider extends AppWidgetProvider {
    public static final String EXTRA_ITEM_POSITION = "extraItemPosition";
    public FirstAppWidgetProvider() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            // refresh all your widgets
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, FirstAppWidgetProvider.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.first_widget_stack_view);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetid: appWidgetIds ) {
            updateFirstAppWidget(context,appWidgetManager,appWidgetid);

        }
    }

    /**
     * To update single widget there can be multiple widget instances user would have created
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     */
    private static void updateFirstAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Intent intent = new Intent(context, ItemListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

        SharedPreferences sharedprefs = context.getSharedPreferences(SHARED_PREFS,context.MODE_PRIVATE);
        String buttonText = sharedprefs.getString(KEY_BUTTON_TEXT+"-"+ appWidgetId,"Launch");


        Intent serviceIntent = new Intent(context, FirstWidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

        Intent clickIntent = new Intent(context, FirstAppWidgetProvider.class);
        clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context,
                0, clickIntent, 0);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.first_widget);
        views.setOnClickPendingIntent(R.id.launch_button, pendingIntent);
        views.setCharSequence(R.id.launch_button, "setText", buttonText);
        views.setRemoteAdapter(R.id.first_widget_stack_view, serviceIntent);
        views.setEmptyView(R.id.first_widget_stack_view, R.id.example_widget_empty_view);
        views.setPendingIntentTemplate(R.id.first_widget_stack_view, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId,views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.first_widget_stack_view);
    }

    public static void sendRefreshBroadcast(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, FirstAppWidgetProvider.class));
        context.sendBroadcast(intent);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }
}
