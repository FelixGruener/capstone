package com.mycompany.android.imageclassifier.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mycompany.android.imageclassifier.R;
import com.mycompany.android.imageclassifier.model.WidgetType;

import java.util.List;

import static com.mycompany.android.imageclassifier.data.ClassificationDatabaseContract.ClassificationEntry.CONTENT_URI;

public class ClassificationWidgetService extends IntentService {

    private static final String TAG = ClassificationWidgetService.class.getSimpleName();

    public static void startActionUpdateWidgets(@NonNull Context context) {
        Intent intent = new Intent(context, ClassificationWidgetService.class);
        context.startService(intent);
    }

    public ClassificationWidgetService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {

            WidgetType type;
                Cursor cursor = getContentResolver().query(
                        CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                );

                boolean isClassificationExist;

                try {
                    if (cursor != null && cursor.getCount() > 0) {
                        isClassificationExist = true;
                    } else {
                        isClassificationExist = false;
                    }
                } catch (Exception ex) {
                    isClassificationExist = false;
                    ex.printStackTrace();
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }

                type = isClassificationExist ? WidgetType.CLASSIFIED : WidgetType.NONE;

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, ClassificationWidgetProvider.class));
            //Trigger data update to handle the GridView widgets and force a data refresh
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
            //Now update all widgets
            ClassificationWidgetProvider.updateClassificationWidgets(this, appWidgetManager, appWidgetIds, type);

        }
    }
}
