package com.govida.push_notification;

import android.os.Bundle;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class NotificationConfig {
    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";


    public Bundle convertToNotificationBundle(RemoteMessage message){
        Bundle bundleData = new Bundle ();
        Map<String,String> receivedData=message.getData();
        String demoValue="";

        if(receivedData.containsKey("type")){
            if(receivedData.get("type")!=null){
                bundleData.putString("type",receivedData.get("type"));
            }else{
                bundleData.putString("type",demoValue);
            }
        }
        if(receivedData.containsKey("title")){
            if(receivedData.get("title")!=null){
                bundleData.putString("title",receivedData.get("title"));
            }else{
                bundleData.putString("title",demoValue);
            }
        }
        if(receivedData.containsKey("body")){
            if(receivedData.get("body")!=null){
                bundleData.putString("body",receivedData.get("body"));
            }else{
                bundleData.putString("body",demoValue);
            }
        }
        return bundleData;
    }


}
