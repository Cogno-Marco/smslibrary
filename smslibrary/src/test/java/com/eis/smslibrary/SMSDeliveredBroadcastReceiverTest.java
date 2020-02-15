package com.eis.smslibrary;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;

import com.eis.smslibrary.listeners.SMSDeliveredListener;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Marco Cognolato
 */
public class SMSDeliveredBroadcastReceiverTest {
    private SMSDeliveredBroadcastReceiver receiver;
    private SMSDeliveredListener listenerMock = mock(SMSDeliveredListener.class);
    private Context contextMock = mock(Context.class);
    private static final SMSPeer VALID_PEER = new SMSPeer("+393423541601");
    private static final String VALID_TEXT = "Some random text to send";
    private static final ArrayList<String> MESSAGE_TO_SEND = new ArrayList<>();
    private static final SMSMessage VALID_MESSAGE = new SMSMessage(VALID_PEER, VALID_TEXT);

    @Before
    public void setup() {
        MESSAGE_TO_SEND.clear();
        MESSAGE_TO_SEND.add(VALID_TEXT);
        receiver = spy(new SMSDeliveredBroadcastReceiver(MESSAGE_TO_SEND, listenerMock, VALID_PEER));
    }

    @Test
    public void constructor_doesntThrow() {
        try {
            new SMSDeliveredBroadcastReceiver(MESSAGE_TO_SEND, listenerMock, VALID_PEER);
            //Success
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void receive_success_correctListener() {
        doReturn(Activity.RESULT_OK).when((BroadcastReceiver) receiver).getResultCode();
        receiver.onReceive(contextMock, null);
        verify(listenerMock, times(1))
                .onSMSDelivered(VALID_MESSAGE, SMSMessage.DeliveredState.MESSAGE_DELIVERED);
        verify(contextMock).unregisterReceiver(receiver);
    }

    @Test
    public void receive_radioOff_correctListener() {
        doReturn(Activity.RESULT_CANCELED)
                .when((BroadcastReceiver) receiver).getResultCode();
        receiver.onReceive(contextMock, null);
        verify(listenerMock, times(1))
                .onSMSDelivered(VALID_MESSAGE, SMSMessage.DeliveredState.DELIVERY_ERROR);
        verify(contextMock).unregisterReceiver(receiver);
    }

    @Test
    public void receive_nullPdu_correctListener() {
        doReturn(666) //an int targeting the default case
                .when((BroadcastReceiver) receiver).getResultCode();
        receiver.onReceive(contextMock, null);
        verify(listenerMock, times(1))
                .onSMSDelivered(VALID_MESSAGE, SMSMessage.DeliveredState.ERROR_GENERIC_FAILURE);
        verify(contextMock).unregisterReceiver(receiver);
    }

}
