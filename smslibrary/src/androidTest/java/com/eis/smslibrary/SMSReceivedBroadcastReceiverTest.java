package com.eis.smslibrary;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.eis.smslibrary.listeners.TestReceivedListener;

import org.junit.Test;

import static org.junit.Assert.*;

public class SMSReceivedBroadcastReceiverTest {

    private Context instrumContext;

    @Test
    public void onReceive() {
        instrumContext = InstrumentationRegistry.getInstrumentation().getContext();
        SMSManager.getInstance().setup(instrumContext);
        TestReceivedListener testListener = new TestReceivedListener();
        SMSManager.getInstance().setReceivedListener(TestReceivedListener.class);
    }
}