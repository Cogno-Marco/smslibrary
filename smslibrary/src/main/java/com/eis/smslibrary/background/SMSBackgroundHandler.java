package com.eis.smslibrary.background;

import android.content.Context;
import android.content.Intent;

/**
 * Keeps the broadcast receiver alive while the app is closed
 *
 * @author Luca Crema
 */
public class SMSBackgroundHandler {

    /**
     * Method to be called when the activity is getting destroyed because the
     * app is getting closed
     * @param context current context
     */
    public static void onAppDestroy(Context context) {
        Intent service = new Intent(context, SMSKeepAliveService.class);
        context.startService(service);
    }

    /**
     * Method to be called when the app opens up and the first onCreate is called
     * @param context current context
     */
    public static void onAppCreate(Context context) {
        Intent service = new Intent(context, SMSKeepAliveService.class);
        context.stopService(service);
    }

}
