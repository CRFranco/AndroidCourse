package br.com.senior.twittersearch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by cristiano.franco on 13/11/2015.
 */
public class StartupReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent it =
                new Intent(context, NotificationService.class);
        context.startService(it);
    }
}
