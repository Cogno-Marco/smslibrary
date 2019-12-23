package com.eis.smslibrary;

import android.telephony.SmsManager;

import org.junit.Test;

import java.util.ArrayList;

import static com.eis.smslibrary.SMSMessage.MAX_MSG_TEXT_LEN;

/**
 * This needs to be an instrumented test because before sending multi-part text messages, we need to
 * use an Android library to divide them.
 */
public class SMSManagerInstrumentedTest {

    @Test
    public void validMessage_isSent() {
        String validMessage = new String(new char[MAX_MSG_TEXT_LEN/2]).replaceAll("\u0000", "ab");
        ArrayList<String> texts = SmsManager.getDefault().divideMessage(validMessage);
        System.out.println(texts);
    }
}
