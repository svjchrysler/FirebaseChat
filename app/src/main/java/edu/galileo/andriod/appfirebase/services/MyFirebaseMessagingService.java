package edu.galileo.andriod.appfirebase.services;

import android.app.Service;
import android.content.Intent;
import android.content.ReceiverCallNotAllowedException;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by jcsalguero on 21/11/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }
}
