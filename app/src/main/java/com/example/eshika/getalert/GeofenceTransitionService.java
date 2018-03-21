package com.example.eshika.getalert;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;


public class GeofenceTransitionService extends IntentService {

    private static final int GEOFENCE_NOTID=0;

    public GeofenceTransitionService() {
        super(GeofenceTransitionService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        //retrieve geofencing intent which contains event
        GeofencingEvent geofencingEvent=GeofencingEvent.fromIntent(intent);

        //handle error
        if(geofencingEvent.hasError()){
            String msg= getErrorString(geofencingEvent.getErrorCode());
            Log.i("Error ",msg);
            return;
        }

        int geofenceTransition=geofencingEvent.getGeofenceTransition();
        if(geofenceTransition== Geofence.GEOFENCE_TRANSITION_ENTER||geofenceTransition==Geofence.GEOFENCE_TRANSITION_EXIT){
            List<Geofence> triggeredGeoFences=geofencingEvent.getTriggeringGeofences();
            String transitionDetails=getGeofenceTransitionDetails(geofenceTransition, triggeredGeoFences );
            //here is my list of geofences if in any one geofence transition ocuurs notify me
            // Send notification details as a String
            sendNotification(transitionDetails);
        }


    }

    private void sendNotification(String transitionDetails) {
  //stack is created from navActivity back to navActivity only
        Intent notificationIntent=navActivity.makeNotificationIntent(getApplicationContext(),transitionDetails);
        TaskStackBuilder taskStackBuilder=TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(navActivity.class);
               taskStackBuilder.addNextIntent(notificationIntent);
               // taskStackBuilder.addNextIntent();
        PendingIntent notificationpendingIntent=taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        //creating and sending notification
        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(GEOFENCE_NOTID,createNotification(transitionDetails,notificationpendingIntent));
//id,notification

    }

    private Notification createNotification(String transitionDetails, PendingIntent notificationIntent) {

        NotificationCompat.Builder notify=new NotificationCompat.Builder(this);
        notify.setSmallIcon(R.drawable.ic_launcher_background)
                .setColor(Color.RED)
                .setContentTitle(transitionDetails)
                .setContentText("Geofence Notification")
                .setContentIntent(notificationIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setAutoCancel(true);
        return notify.build();

    }


    //setting title of notification -status+ requestid(constant)
    private String getGeofenceTransitionDetails(int geofencetransition,List<Geofence> triggeredGeofences){
           //getting requestid of all geofences and storing as string
        List<String> triggerGeofenceList=new ArrayList<>();
        for(Geofence geofence:triggeredGeofences){
            triggerGeofenceList.add(geofence.getRequestId());

        }
        String status=null;
        if(geofencetransition==Geofence.GEOFENCE_TRANSITION_ENTER)
            status="ENTERING : ";
        else if(geofencetransition==Geofence.GEOFENCE_TRANSITION_EXIT)
            status="EXITING";
        return status+ TextUtils.join(",",triggerGeofenceList);

    }

    private static String getErrorString(int errorCode) {
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "GeoFence not available";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "Too many GeoFences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "Too many pending intents";
            default:
                return "Unknown error.";
        }

    }







}
