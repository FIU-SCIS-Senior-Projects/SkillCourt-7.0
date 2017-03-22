package fiu.com.skillcourt.fcm;

/**
 * Created by alvaro on 3/21/17.
 */

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import fiu.com.skillcourt.R;

public class NotificationService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(remoteMessage.getNotification().getTitle());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentText(remoteMessage.getNotification().getBody());
        NotificationManagerCompat.from(this).notify(0, builder.build());
    }
}
