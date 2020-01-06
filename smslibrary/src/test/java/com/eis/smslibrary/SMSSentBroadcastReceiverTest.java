package com.eis.smslibrary;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.telephony.SmsManager;

import com.eis.smslibrary.listeners.SMSSentListener;

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
public class SMSSentBroadcastReceiverTest {

    private SMSSentBroadcastReceiver receiver;
    private SMSSentListener listenerMock = mock(SMSSentListener.class);
    private Context contextMock = mock(Context.class);
    private final SMSPeer VALID_PEER = new SMSPeer("+393423541601");
    private final String VALID_TEXT = "Some random text to send";
    private final ArrayList<String> MESSAGE_TO_SEND = new ArrayList<>();
    private final SMSMessage VALID_MESSAGE = new SMSMessage(VALID_PEER, VALID_TEXT);

    @Before
    public void setup() {
        MESSAGE_TO_SEND.add(VALID_TEXT);
        receiver = spy(new SMSSentBroadcastReceiver(MESSAGE_TO_SEND, listenerMock, VALID_PEER));
    }

    @Test
    public void constructor_doesntThrow() {
        try {
            new SMSSentBroadcastReceiver(MESSAGE_TO_SEND, listenerMock, VALID_PEER);
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
                .onSMSSent(VALID_MESSAGE, SMSMessage.SentState.MESSAGE_SENT);
        verify(contextMock).unregisterReceiver(receiver);
    }

    @Test
    public void receive_radioOff_correctListener() {
        doReturn(SmsManager.RESULT_ERROR_RADIO_OFF)
                .when((BroadcastReceiver) receiver).getResultCode();
        receiver.onReceive(contextMock, null);
        verify(listenerMock, times(1))
                .onSMSSent(VALID_MESSAGE, SMSMessage.SentState.ERROR_RADIO_OFF);
        verify(contextMock).unregisterReceiver(receiver);
    }

    @Test
    public void receive_nullPdu_correctListener() {
        doReturn(SmsManager.RESULT_ERROR_NULL_PDU)
                .when((BroadcastReceiver) receiver).getResultCode();
        receiver.onReceive(contextMock, null);
        verify(listenerMock, times(1))
                .onSMSSent(VALID_MESSAGE, SMSMessage.SentState.ERROR_NULL_PDU);
        verify(contextMock).unregisterReceiver(receiver);
    }

    @Test
    public void receive_noService_correctListener() {
        doReturn(SmsManager.RESULT_ERROR_NO_SERVICE)
                .when((BroadcastReceiver) receiver).getResultCode();
        receiver.onReceive(contextMock, null);
        verify(listenerMock, times(1))
                .onSMSSent(VALID_MESSAGE, SMSMessage.SentState.ERROR_NO_SERVICE);
        verify(contextMock).unregisterReceiver(receiver);
    }

    @Test
    public void receive_limitExceeded_correctListener() {
        doReturn(SmsManager.RESULT_ERROR_LIMIT_EXCEEDED)
                .when((BroadcastReceiver) receiver).getResultCode();
        receiver.onReceive(contextMock, null);
        verify(listenerMock, times(1))
                .onSMSSent(VALID_MESSAGE, SMSMessage.SentState.ERROR_LIMIT_EXCEEDED);
        verify(contextMock).unregisterReceiver(receiver);
    }

    @Test
    public void receive_genericFailure_correctListener() {
        doReturn(666) //an int targeting the default case
                .when((BroadcastReceiver) receiver).getResultCode();
        receiver.onReceive(contextMock, null);
        verify(listenerMock, times(1))
                .onSMSSent(VALID_MESSAGE, SMSMessage.SentState.ERROR_GENERIC_FAILURE);
        verify(contextMock).unregisterReceiver(receiver);
    }

}
