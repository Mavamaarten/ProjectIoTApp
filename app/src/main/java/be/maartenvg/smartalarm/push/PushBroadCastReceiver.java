package be.maartenvg.smartalarm.push;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import be.maartenvg.smartalarm.R;
import be.maartenvg.smartalarm.activities.MainActivity;

public class PushBroadCastReceiver extends ParsePushBroadcastReceiver {
    public static String BROADCAST_ACTION = "be.maartenvg.smartalarm.SHOW_TOAST";

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        try {
            JSONObject object = new JSONObject(bundle.getString("com.parse.Data"));
            String title = object.getString("title");
            String alert = object.getString("alert");

            if (!MainActivity.isForeground) {

                android.support.v4.app.NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.ic_notif_warning)
                                .setContentTitle(title)
                                .setContentText(alert)
                                .setAutoCancel(true);

                Intent resultIntent = new Intent(context, MainActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(MainActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(1, mBuilder.build());

                Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(new long[]{0, 300, 100, 300, 100, 300}, -1);
            }

            Intent broadcast = new Intent();
            broadcast.putExtra("alert", alert);
            broadcast.putExtra("title", title);
            broadcast.putExtra("extraData", new JSONObject(object.getString("extraData")).toString());
            broadcast.setAction(BROADCAST_ACTION);
            context.sendBroadcast(broadcast);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
