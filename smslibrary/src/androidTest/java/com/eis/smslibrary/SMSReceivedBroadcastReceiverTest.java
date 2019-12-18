package com.eis.smslibrary;

import android.Manifest;
import android.content.Context;
import android.telephony.TelephonyManager;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;

import com.eis.smslibrary.listeners.TestReceivedListener;

import org.junit.Rule;
import org.junit.Test;

public class SMSReceivedBroadcastReceiverTest {

    @Rule
    public GrantPermissionRule sendSmsPermissionRule = GrantPermissionRule.grant(Manifest.permission.SEND_SMS);
    @Rule
    public GrantPermissionRule receiveSmsPermissionRule = GrantPermissionRule.grant(Manifest.permission.RECEIVE_SMS);
    @Rule
    public GrantPermissionRule readPhoneStatePermissionRule = GrantPermissionRule.grant(Manifest.permission.READ_PHONE_STATE);

    private Context instrumContext;

    @Test
    public void onReceive() {
        // SMSManager setup
        instrumContext = InstrumentationRegistry.getInstrumentation().getContext();
        SMSManager.getInstance().setup(instrumContext);
        TestReceivedListener testListener = new TestReceivedListener();
        SMSManager.getInstance().setReceivedListener(TestReceivedListener.class);

        TelephonyManager tMgr = (TelephonyManager) instrumContext.getSystemService(Context.TELEPHONY_SERVICE);
        String myPhoneNumber = tMgr.getLine1Number();

        SMSManager.getInstance().sendMessage(new SMSMessage(new SMSPeer(myPhoneNumber), "prova"));

    }


}