package active.since93.ancount.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import active.since93.ancount.activities.MainActivity;
import active.since93.ancount.R;
import active.since93.ancount.database.DatabaseHandler;

/**
 * Created by Darshan on 13-Sep-15.
 */
public class UnlockCountWidget extends AppWidgetProvider {

    DatabaseHandler databaseHandler;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        databaseHandler = new DatabaseHandler(context);
        for(int i=0; i<appWidgetIds.length; i++){
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            views.setOnClickPendingIntent(R.id.RelativeLayout1, pendingIntent);

            int count = databaseHandler.getTodayUnlockCount();
			views.setTextViewText(R.id.txtUnlocksCount, String.valueOf(count));

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if(intent.getAction().equals("android.intent.action.USER_PRESENT")) {
//            Toast.makeText(context, "WIDGET CALLED", Toast.LENGTH_LONG).show();
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            databaseHandler = new DatabaseHandler(context);
            int count = databaseHandler.getTodayUnlockCount();
            views.setTextViewText(R.id.txtUnlocksCount, String.valueOf(count));
            AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, UnlockCountWidget.class),views);
        }
    }
}
