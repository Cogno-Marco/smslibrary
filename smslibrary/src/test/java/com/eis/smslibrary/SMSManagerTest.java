package com.eis.smslibrary;

import android.telephony.SmsManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Class used to test SMSManager using Mocks
 *
 * @author Marco Cognolato, Giovanni Velludo
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SmsManager.class)
public class SMSManagerTest {

    private SmsManager managerMock = mock(SmsManager.class);

    private final String PEER_TEXT = "+393423541601";
    private final String MESSAGE_TEXT = "valid text";
    private final String SENT_TEXT = (char) 0x02 + MESSAGE_TEXT;
    private final SMSPeer VALID_PEER = new SMSPeer(PEER_TEXT);
    private final SMSMessage VALID_MESSAGE = new SMSMessage(VALID_PEER, MESSAGE_TEXT);
    private SMSManager smsManager = SMSManager.getInstance();

    @Before
    public void setup(){
        PowerMockito.mockStatic(SmsManager.class);
        when(SmsManager.getDefault()).thenReturn(managerMock);
    }

    @Test
    public void singletonInstance() {
        assertEquals(SMSManager.getInstance(), SMSManager.getInstance());
    }

    @Test()
    public void validMessage_isSent() {
        smsManager.sendMessage(VALID_MESSAGE);
        verify(managerMock).sendTextMessage(PEER_TEXT, null, SENT_TEXT, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void nullMessage_throws() {
        smsManager.sendMessage(null);
    }
}
