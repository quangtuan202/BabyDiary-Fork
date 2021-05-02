package com.riagon.babydiary.Notification;

import android.content.BroadcastReceiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserObject {
    public String userId;
    public List<BroadcastReceiver> broadcastReceiverListOfUser=new ArrayList<>();
    public static HashMap<String, UserObject> UserObjectMap = new HashMap<String, UserObject>();

    public UserObject(String userId){
        this.userId=userId;
    }
}
