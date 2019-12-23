package com.eis.smslibrary;

import android.telephony.SmsManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static com.eis.smslibrary.SMSMessage.MAX_MSG_TEXT_LEN;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.verify;

/**
 * This needs to be an instrumented test, because before sending multi-part text messages we need to
 * use an Android library to divide them.
 */
public class SMSManagerInstrumentedTest {

    //@Rule
    //public GrantPermissionRule sendSmsPermissionRule = GrantPermissionRule.grant(Manifest.permission.SEND_SMS);
    
    @Mock
    SmsManager mockManager;

    @Captor
    private ArgumentCaptor<ArrayList<String>> messageTextCaptor;

    @Captor
    private ArgumentCaptor<String> peerNumberCaptor;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validMultiPartMessage_isSent() {
        String validText =
                new String(new char[MAX_MSG_TEXT_LEN/2]).replaceAll("\u0000", "ab");
        String validNumber = "+393491643349";
        SMSMessage validMessage = new SMSMessage(new SMSPeer(validNumber), validText);
        
        SMSCore.setManager(mockManager);
        SMSManager instance = SMSManager.getInstance();
        instance.sendMessage(validMessage);

        verify(mockManager).sendMultipartTextMessage(peerNumberCaptor.capture(),
                isNull(), messageTextCaptor.capture(), isNull(), isNull());
        assertEquals(peerNumberCaptor.getValue(), validNumber);
        StringBuilder sentText = new StringBuilder();
        for (String text : messageTextCaptor.getValue()) {
            sentText.append(text);
        }
        assertEquals(sentText.toString(), validText);
    }
}
