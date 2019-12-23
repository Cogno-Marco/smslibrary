package com.eis.smslibrary;

import android.Manifest;
import android.content.Context;
import android.telephony.TelephonyManager;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;

import com.eis.smslibrary.listeners.ListenerToMock;
import com.eis.smslibrary.listeners.TestSMSReceivedServiceListener;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class SMSReceivedInstrumentedTest {

    //TODO: check if BroadcastReceiver for SMS messages is actually called, consider lowering target API level to register BroadcastReceiver in Manifest

    @Rule
    public GrantPermissionRule sendSmsPermissionRule = GrantPermissionRule.grant(Manifest.permission.SEND_SMS);
    @Rule
    public GrantPermissionRule receiveSmsPermissionRule = GrantPermissionRule.grant(Manifest.permission.RECEIVE_SMS);
    @Rule
    public GrantPermissionRule readPhoneStatePermissionRule = GrantPermissionRule.grant(Manifest.permission.READ_PHONE_STATE);

    @Test
    public void onReceive() {
        // Mockito setup
        ListenerToMock mockListener = mock(ListenerToMock.class);
        ArgumentCaptor<SMSMessage> messageReceived = ArgumentCaptor.forClass(SMSMessage.class);

        // SMSManager setup
        Context instrumContext = InstrumentationRegistry.getInstrumentation().getContext();
        TestSMSReceivedServiceListener testListener = new TestSMSReceivedServiceListener();
        testListener.addListener(mockListener);
        SMSManager.getInstance().setReceivedListener(TestSMSReceivedServiceListener.class, instrumContext);

        TelephonyManager tMgr = (TelephonyManager) instrumContext.getSystemService(Context.TELEPHONY_SERVICE);
        String myPhoneNumber = tMgr.getLine1Number();

        SMSMessage testMessage = new SMSMessage(new SMSPeer(myPhoneNumber), "prova");
        SMSManager.getInstance().sendMessage(testMessage);
        try {
            Thread.sleep(5000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        Mockito.verify(mockListener).onMessageReceived(messageReceived.capture());
        String textReceived = messageReceived.getValue().getData();
        assertEquals(testMessage.getData(), textReceived);
    }
}