package com.eis.smslibrary;

import android.telephony.SmsManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SMSHandlerTest {

    private SmsManager managerMock = mock(SmsManager.class);

    private final String PEER_TEXT = "+393423541601";
    private final String MESSAGE_TEXT = "valid text";
    private final String SENT_TEXT = (char) 0x02 + MESSAGE_TEXT;
    private final SMSPeer VALID_PEER = new SMSPeer(PEER_TEXT);
    private final SMSMessage VALID_MESSAGE = new SMSMessage(VALID_PEER, MESSAGE_TEXT);
    private SMSManager instance = SMSManager.getInstance();

    @Test
    public void singletonInstance() {
        assertEquals(SMSManager.getInstance(), SMSManager.getInstance());
    }

    @Test()
    public void validMessage_isSent() {
        SMSCore.setManager(managerMock);
        instance.sendMessage(VALID_MESSAGE);
        verify(managerMock).sendTextMessage(PEER_TEXT, null, SENT_TEXT, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void nullMessage_throws() {
        instance.sendMessage(null);
    }
}
