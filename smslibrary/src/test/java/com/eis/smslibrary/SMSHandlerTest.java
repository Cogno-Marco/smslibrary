package com.eis.smslibrary;

import android.content.Context;
import android.telephony.SmsManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SMSHandlerTest {

    @Mock SmsManager managerMock;
    @Mock Context contextMock;

    private final String PEER_TEXT = "5554";
    private final String MESSAGE_TEXT = "valid text";
    private final SMSPeer VALID_PEER = new SMSPeer(PEER_TEXT);
    private final SMSMessage VALID_MESSAGE = new SMSMessage(VALID_PEER, MESSAGE_TEXT);

    @Test
    public static void sendValidTextMessage(){

    }

}
