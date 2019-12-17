package com.eis.smslibrary;

import android.content.Context;
import android.telephony.SmsManager;

import com.eis.smslibrary.listeners.SMSReceivedListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SMSHandlerTest {

    private SmsManager managerMock = mock(SmsManager.class);
    private SMSReceivedListener listenerMock = mock(SMSReceivedListener.class);

    private final String PEER_TEXT = "+393423541601";
    private final String MESSAGE_TEXT = "valid text";
    private final String SENT_TEXT = (char) 0x02 + MESSAGE_TEXT;
    private final SMSPeer VALID_PEER = new SMSPeer(PEER_TEXT);
    private final SMSMessage VALID_MESSAGE = new SMSMessage(VALID_PEER, MESSAGE_TEXT);
    private SMSHandler instance = SMSHandler.getInstance();

    @Test
    public void singletonInstance() {
        assertEquals(SMSHandler.getInstance(), SMSHandler.getInstance());
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

    @Test
    public void receiveMessage_correctMessage(){
        instance.setReceivedListener(listenerMock);
        instance.callReceivedListener(VALID_MESSAGE);
        verify(listenerMock, times(1))
                .onMessageReceived(VALID_MESSAGE);
    }

    @Test
    public void listenerNullified_nothingHappens(){
        instance.setReceivedListener(listenerMock);
        instance.setReceivedListener(null);
        instance.callReceivedListener(VALID_MESSAGE);
        verify(listenerMock, times(0))
                .onMessageReceived(any(SMSMessage.class));
    }
}
