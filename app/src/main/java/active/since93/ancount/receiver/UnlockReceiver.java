package active.since93.ancount.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Calendar;

import active.since93.ancount.constants.Constants;
import active.since93.ancount.database.DatabaseHandler;
import active.since93.ancount.model.UnlockDataItem;

public class UnlockReceiver extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            Log.i("RECEIVER_CALLED", "USER PRESENT");
            incrementCount();
        }
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.i("RECEIVER_CALLED", "SCREEN ON");
        }
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.i("RECEIVER_CALLED", "SCREEN OFF");
        }
    }

    private void incrementCount() {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        UnlockDataItem unlockDataItem = new UnlockDataItem();
        Calendar cal = Calendar.getInstance();
        unlockDataItem.setTime(String.valueOf(cal.getTimeInMillis()));
        databaseHandler.addData(unlockDataItem);
        updateActivities();
    }

    private void updateActivities() {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context);
        Intent intent = new Intent(Constants.UPDATE_ACTIVITIES);
        broadcastManager.sendBroadcast(intent);
    }
}