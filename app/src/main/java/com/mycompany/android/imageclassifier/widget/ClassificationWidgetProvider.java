package com.mycompany.android.imageclassifier.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.mycompany.android.imageclassifier.DetailActivity;
import com.mycompany.android.imageclassifier.MainActivity;
import com.mycompany.android.imageclassifier.R;
import com.mycompany.android.imageclassifier.model.WidgetType;

public class ClassificationWidgetProvider extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, WidgetType widgetType) {

        RemoteViews views;

        if (widgetType == WidgetType.CLASSIFIED) {
            views = getListRemoteView(context);
        } else {
            views = getEmptyRemoteView(context);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateClassificationWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, WidgetType widgetType) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, widgetType);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ClassificationWidgetService.startActionUpdateWidgets(context);
    }


    private static RemoteViews getListRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);
        views.setTextViewText(R.id.title_text_view, "Classification details");

        Intent intent = new Intent(context, ListRemoteService.class);
        views.setRemoteAdapter(R.id.widget_list_view, intent);

        Intent appIntent = new Intent(context, DetailActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list_view, appPendingIntent);
        views.setEmptyView(R.id.widget_list_view, R.id.empty_view);
        return views;
    }

    private static RemoteViews getEmptyRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.empty_widget_view);

        Intent appIntent = new Intent(context, MainActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.empty_widget_image_view, appPendingIntent);
        views.setOnClickPendingIntent(R.id.empty_widget_image_view, appPendingIntent);

        return views;
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}
