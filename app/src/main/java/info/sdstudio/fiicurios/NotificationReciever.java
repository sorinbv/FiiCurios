package info.sdstudio.fiicurios;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

public class NotificationReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notifManag = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent myIntent = new Intent(context, MainActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pend = PendingIntent.getActivity(context, 100, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        SharedPreferences.Editor editor = MainActivity.sharedPref.edit();

        long currentDate = System.currentTimeMillis();
        Uri sound = Uri.parse("android.resource://info.sdstudio.fiicurios/" + R.raw.notification);

        long alarmTime = MainActivity.sharedPref.getLong("alarmTime", 0L);
        long diffs = alarmTime - currentDate;
        int diff = (int) (diffs / (1000 * 60 * 60 * 24));

        if (diff < 0) {

            if (alarmTime + 86400000 < currentDate) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setContentIntent(pend)
                        .setSmallIcon(R.drawable.ic_stat_name)
                        //   .setColor(Color.BLUE)
                        //	.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_question_sign))
                        .setContentTitle("Fii curios și azi!").setContentText("Citește noi curiozități.")
                        .setVibrate(new long[]{500, 500, 500, 500}).setSound(sound)
                        .setLights(Color.GREEN, 500, 1000).setAutoCancel(true);

                boolean notif = MainActivity.sharedPref.getBoolean("notificari", true);
                if (notif && !MainActivity.mIsInForegroundMode) {
                    notifManag.notify(100, builder.build());
                    long timeNow = Calendar.getInstance().getTimeInMillis();
                    editor.putLong("alarmTime", timeNow);
                    editor.commit();
                }

            }

        }


    }

}
