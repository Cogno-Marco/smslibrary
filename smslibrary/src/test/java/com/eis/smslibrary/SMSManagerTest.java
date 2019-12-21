package com.eis.smslibrary;

import android.telephony.SmsManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Class used to test SMSManager using Mocks
 *
 * @author Marco Cognolato, Giovanni Velludo
 */
@RunWith(MockitoJUnitRunner.class)
public class SMSManagerTest {

    @Mock
    private SmsManager managerMock;

    @Captor
    private ArgumentCaptor<ArrayList<String>> messageTextCaptor;

    @Captor
    private ArgumentCaptor<String> peerNumberCaptor;

    private final String PEER_NUMBER = "+393423541601";
    private final String MESSAGE_TEXT = "valid text";
    private final SMSPeer VALID_PEER = new SMSPeer(PEER_NUMBER);
    private final SMSMessage VALID_MESSAGE = new SMSMessage(VALID_PEER, MESSAGE_TEXT);
    private SMSManager instance = SMSManager.getInstance();

    @Test
    public void singletonInstance() {
        assertEquals(SMSManager.getInstance(), SMSManager.getInstance());
    }

    @Test()
    public void validMessage_isSent() {
        ArrayList<String> messageText = new ArrayList<>(1);
        messageText.add(MESSAGE_TEXT);
        when(managerMock.divideMessage(anyString())).thenReturn(messageText);

        SMSCore.setManager(managerMock);
        instance.sendMessage(VALID_MESSAGE);
        verify(managerMock).sendMultipartTextMessage(peerNumberCaptor.capture(), isNull(String.class), messageTextCaptor.capture(), isNull(ArrayList.class), isNull(ArrayList.class));
        assertEquals(peerNumberCaptor.getValue(), PEER_NUMBER);
        assertEquals(messageTextCaptor.getValue(), messageText);
    }

    //TODO: add tests to check how message splitting is done, recompose message from ArrayList and check if it's ok

    @Test(expected = NullPointerException.class)
    public void nullMessage_throws() {
        instance.sendMessage(null);
    }
}
