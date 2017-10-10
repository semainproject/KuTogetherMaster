package info.androidhive.firebase;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import static android.content.ContentValues.TAG;

public class MyService extends Service {
    public MyService() {
    }
    String uid;
    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(MyService.this, "service Start from myservice", Toast.LENGTH_SHORT).show();
        DatabaseReference firebaseRef,ref;
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        firebaseRef = FirebaseDatabase.getInstance().getReference("Destination Data").child(uid).child("ReceiverID");
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Notification notification =
//                        new NotificationCompat.Builder(MyService.this) // this is context
//                                .setSmallIcon(R.mipmap.ic_launcher)
//                                .setContentTitle("มีคนมารับจ้าา")
//                                .setContentText("กำลังมารับ")
//                                .setAutoCancel(true)
//                                .build();
//                Random random = new Random();
//                int randomNumber = random.nextInt(9999 - 1000) + 1000;
//                NotificationManager notificationManager =
//                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                notificationManager.notify(randomNumber, notification);
//                Intent intent = new Intent(MyService.this, MypostActivity.class);
//
//                PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this,0, intent, 0);
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    InfoUser id = data.getValue(InfoUser.class);
                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                    //Toast.makeText(MyService.this, id.getId(), Toast.LENGTH_LONG).show();
                    Notification notification =
                        new NotificationCompat.Builder(MyService.this) // this is context
                                .setSmallIcon(R.mipmap.ic_newsfeed)
                                .setContentTitle("มีคนมารับจ้าา")
                                .setContentText(id.getNickname()+"กำลังมารับ")
                                .setSound(alarmSound)
                                //.setContentIntent(pendingIntent)
                                .setAutoCancel(true)
                                .build();
                Random random = new Random();
                int randomNumber = random.nextInt(9999 - 1000) + 1000;
                if(id.getId() != null) {
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(randomNumber, notification);
                    break;
                }
                }
//                String idSTR = id.getId();
//                Notification notification =
//                        new NotificationCompat.Builder(MyService.this) // this is context
//                                .setSmallIcon(R.mipmap.ic_launcher)
//                                .setContentTitle("มีคนมารับจ้าา")
//                                .setContentText(idSTR+"กำลังมารับ")
//                                .setAutoCancel(true)
//                                .build();
//                Random random = new Random();
//                int randomNumber = random.nextInt(9999 - 1000) + 1000;
//                if(idSTR != null) {
//                    NotificationManager notificationManager =
//                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                    notificationManager.notify(randomNumber, notification);
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        ref = FirebaseDatabase.getInstance().getReference("Destination Data").child(uid).child("connect");
        final Intent i = new Intent(MyService.this, ServiceLocation.class);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(Boolean.class) != null) {
                    Boolean status = dataSnapshot.getValue(Boolean.class);
                    if (status == true) {
                        Toast.makeText(MyService.this, "TRUE", Toast.LENGTH_SHORT).show();
                        startService(i);
                    } else {
                        Toast.makeText(MyService.this, "FALSE", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        ref.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Boolean status = dataSnapshot.getValue(Boolean.class);
//                if(status == true){
//                    //Intent i = new Intent(MyService.this, ServiceLocation.class);
//                    Toast.makeText(MyService.this, "service Start", Toast.LENGTH_SHORT).show();
//                    startService(i);
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                Boolean status = dataSnapshot.getValue(Boolean.class);
//                if(status == false){
//                    stopService(i);
//                    Toast.makeText(MyService.this, "service Stop", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent,flags,startId);
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "service Stopped", Toast.LENGTH_SHORT).show();
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
