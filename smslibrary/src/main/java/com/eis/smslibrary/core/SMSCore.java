package com.eis.smslibrary.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.eis.communication.exception.InvalidHeaderException;
import com.eis.communication.exception.InvalidPayloadException;
import com.eis.communication.exception.InvalidPeerException;
import com.eis.smslibrary.manager.SMSManager;

/**
 *@author Mattia Fanan
 *
 * basic class scheme from gruppo1
 * it interfaces SMSMDataUnit with API
 */
public class SMSCore extends BroadcastReceiver {


    /**
     * Sends APIMessage
     *
     * @param apiMessage APIMessage message to send
     */
    public static void sendMessage(APIMessage apiMessage) {
        SmsManager.getDefault().sendTextMessage(apiMessage.getDestination(), null, apiMessage.getTextMessage(), null, null);
    }

    /**
     * Function called when a message is received. It delegates the message to the SMSDataUnit
     * Handler which analyzes its content.
     *
     * @param context Received message context.
     * @param intent  Received message Intent.
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        if (pdus != null) {
            SmsMessage shortMessage;
            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M))
                shortMessage = SmsMessage.createFromPdu((byte[]) pdus[0], intent.getExtras().getString("format"));
            else
                shortMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);

            if (shortMessage != null) {
                APIMessage apiMessage=new APIMessage(null, shortMessage.getDisplayOriginatingAddress(), shortMessage.getDisplayMessageBody());

                //try to pass the SMSDataUnit built from incoming Sms to SMSManager
                try {
                    SMSManager.getDefault().handleMessage(SMSAdapter.adaptToSMSDataUnit(apiMessage));
                }
                catch (InvalidPayloadException e) { }
                catch (InvalidPeerException e) { }
                catch (InvalidHeaderException e) { }
            }
        }
    }

}
