package com.riagon.babydiary.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.riagon.babydiary.BathActivity;
import com.riagon.babydiary.BottlePumpedActivity;
import com.riagon.babydiary.BreastfeedActivity;
import com.riagon.babydiary.CryingActivity;
import com.riagon.babydiary.DiaperActivity;
import com.riagon.babydiary.DoctorVisitActivity;
import com.riagon.babydiary.DrinkActivity;
import com.riagon.babydiary.FoodActivity;
import com.riagon.babydiary.FormulaActivity;
import com.riagon.babydiary.MainActivity;
import com.riagon.babydiary.MassageActivity;
import com.riagon.babydiary.MedicineActivity;
import com.riagon.babydiary.PlayActivity;
import com.riagon.babydiary.PottyActivity;
import com.riagon.babydiary.PumpActivity;
import com.riagon.babydiary.SleepActivity;
import com.riagon.babydiary.SunbathTimeActivity;
import com.riagon.babydiary.SymptomActivity;
import com.riagon.babydiary.TemperatureActivity;
import com.riagon.babydiary.TummyTimeActivity;
import com.riagon.babydiary.VaccinationActivity;
import com.riagon.babydiary.R;

public class AlarmReceiver extends BroadcastReceiver {
    String TAG = "AlarmReceiver";
    public static String NOTIFICATION_ID = "notification-id";

    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
//                Notification notification = new Notification(context);
//                NotificationScheduler.setReminder(context, AlarmReceiver.class, 15, 49);
                return;
            }
        }


        String notificationTitle = context.getResources().getString(R.string.notify_title);
        String notificationContent = context.getResources().getString(R.string.notify_content);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);

        showNotification(context, getProviderClass(id), notificationTitle, notificationContent, id);

        Log.d(TAG, "onReceive: " + id);

        //Trigger the notification
//        NotificationScheduler.showNotification(context, MainActivity.class,
//                notificationTitle, notificationContent);


    }


    public void showNotification(Context context, Class<?> cls, String title, String content, int notiID) {


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = new Intent(context, cls);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(cls);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(notiID, PendingIntent.FLAG_ONE_SHOT);


        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), "YOUR_CHANNEL_ID");


        Notification notification = builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setSmallIcon(getNotificationIcon(notiID))
                .setContentIntent(pendingIntent).build();


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notiID, notification);

    }

    Class<?> getProviderClass(int notiID) {
        switch (notiID) {
            case 1: {
                return BreastfeedActivity.class;
            }
            case 2: {
                return PumpActivity.class;
            }
            case 3: {
                return FormulaActivity.class;
            }
            case 4: {
                return BottlePumpedActivity.class;
            }
            case 5: {
                return FoodActivity.class;
            }
            case 6: {
                return DiaperActivity.class;
            }
            case 7: {
                return BathActivity.class;
            }
            case 8: {
                return SleepActivity.class;
            }
            case 9: {
                return TummyTimeActivity.class;
            }
            case 10: {
                return SunbathTimeActivity.class;
            }
            case 11: {
                return PlayActivity.class;
            }
            case 12: {
                return MassageActivity.class;
            }
            case 13: {
                return DrinkActivity.class;
            }
            case 14: {
                return CryingActivity.class;
            }
            case 15: {
                return VaccinationActivity.class;
            }
            case 16: {
                return TemperatureActivity.class;
            }
            case 17: {
                return MedicineActivity.class;
            }
            case 18: {
                return DoctorVisitActivity.class;
            }
            case 19: {
                return SymptomActivity.class;
            }
            case 20: {
                return PottyActivity.class;
            }

            default:
                return MainActivity.class;
        }
    }

    public int getNotificationIcon(int notiID) {
        int icon = 0;
        if (notiID == 1) {
            icon = R.drawable.ic_cvfeedwhite;
        } else if (notiID == 2) {
            icon = R.drawable.ic_cvpumpwhite;
        } else if (notiID == 3) {
            icon = R.drawable.ic_cvformulawhite;
        } else if (notiID == 4) {
            icon = R.drawable.ic_cvpumpbottlewhite;
        } else if (notiID == 5) {
            icon = R.drawable.ic_cvfoodwhite;
        } else if (notiID == 6) {
            icon = R.drawable.ic_cvdiaperwhite;
        } else if (notiID == 7) {
            icon = R.drawable.ic_cvbathwhite;
        } else if (notiID == 8) {
            icon = R.drawable.ic_cvsleepwhite;
        } else if (notiID == 9) {
            icon = R.drawable.ic_tummytimewhite;
        } else if (notiID == 10) {
            icon = R.drawable.ic_cvsunbathewhite;
        } else if (notiID == 11) {
            icon = R.drawable.ic_cvplaywhite;
        } else if (notiID == 12) {
            icon = R.drawable.ic_cvmassagewhite;
        } else if (notiID == 13) {
            icon = R.drawable.ic_cvdrinkwhite;
        } else if (notiID == 14) {
            icon = R.drawable.ic_cvcryingwhite;
        } else if (notiID == 15) {
            icon = R.drawable.ic_cvvaccinationwhite;
        } else if (notiID == 16) {
            icon = R.drawable.ic_cvtemperaturewhite;
        } else if (notiID == 17) {
            icon = R.drawable.ic_medwhite;
        } else if (notiID == 18) {
            icon = R.drawable.ic_cvdoctorvisitwhite;
        } else if (notiID == 19) {
            icon = R.drawable.ic_cvsymptomwhite;
        } else if (notiID == 20) {
            icon = R.drawable.ic_cvpottywhite;
        }

        return icon;
    }


}