package Utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

/**
 * Created by lenser on 3/28/17.
 */

public class Notify {
    public static void notfy(Activity activity){
        Vibrator vibrator = (Vibrator)activity.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(300);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(activity)
                        .setSmallIcon(android.R.drawable.sym_def_app_icon)
                        .setContentTitle("TOO FAR")
                        .setContentText("Keep your cart close");
        NotificationManager mNotificationManager = (NotificationManager)activity.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(12, mBuilder.build());
    }
}
