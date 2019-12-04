package com.eis.smslibrary;

import android.content.Context;
import android.content.IntentFilter;
import android.telephony.SmsManager;

import com.eis.smslibrary.listeners.SMSSentListener;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SMSHandlerTest {

    @Mock SmsManager managerMock;
    @Mock Context contextMock;

    private final String PEER_TEXT = "+393423541601";
    private final String MESSAGE_TEXT = "valid text";
    private final String SENT_TEXT = (char)0x02 + MESSAGE_TEXT;
    private final SMSPeer VALID_PEER = new SMSPeer(PEER_TEXT);
    private final SMSMessage VALID_MESSAGE = new SMSMessage(VALID_PEER, MESSAGE_TEXT);
    private SMSHandler instance;

    @Before
    public void setup(){
        instance = SMSHandler.getInstance();
        instance.clearSetup();
    }

    @Test(expected = IllegalStateException.class)
    public void sendMessageWithNullContext_throws(){
        instance.sendMessage(VALID_MESSAGE);
    }

    @Test
    public void singletonInstance(){
        assertEquals(SMSHandler.getInstance(), SMSHandler.getInstance());
    }

    @Test(expected = NullPointerException.class)
    public void setupNullContext_throws(){
        instance.setup(null);
    }

    @Test()
    public void validMessage_isSent(){
        SMSCore.setManager(managerMock);
        instance.setup(contextMock);
        instance.sendMessage(VALID_MESSAGE);
        verify(managerMock).sendTextMessage(PEER_TEXT, null, SENT_TEXT, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void nullMessage_throws(){
        instance.setup(contextMock);
        instance.sendMessage(null);
    }
}
