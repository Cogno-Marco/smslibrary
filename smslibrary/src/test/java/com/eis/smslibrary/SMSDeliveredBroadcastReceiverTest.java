package com.eis.smslibrary;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.telephony.SmsManager;

import com.eis.smslibrary.listeners.SMSDeliveredListener;
import com.eis.smslibrary.listeners.SMSSentListener;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SMSDeliveredBroadcastReceiverTest {
    private SMSDeliveredBroadcastReceiver receiver;
    private SMSDeliveredListener listenerMock = mock(SMSDeliveredListener.class);
    private Context contextMock = mock(Context.class);
    private final SMSPeer VALID_PEER = new SMSPeer("+393423541601");
    private final String VALID_TEXT = "Some random text to send";
    private final SMSMessage VALID_MESSAGE = new SMSMessage(VALID_PEER, VALID_TEXT);

    @Before
    public void setup(){
        receiver = spy(new SMSDeliveredBroadcastReceiver(VALID_MESSAGE, listenerMock));
    }

    @Test
    public void constructor_doesntThrow(){
        SMSDeliveredBroadcastReceiver rec = new SMSDeliveredBroadcastReceiver(VALID_MESSAGE, listenerMock);
    }

    @Test
    public void receive_success_correctListener(){
        doReturn(Activity.RESULT_OK).when((BroadcastReceiver)receiver).getResultCode();
        receiver.onReceive(contextMock, null);
        verify(listenerMock, times(1))
                .onSMSDelivered(VALID_MESSAGE, SMSMessage.DeliveredState.MESSAGE_DELIVERED);
        verify(contextMock).unregisterReceiver(receiver);
    }

    @Test
    public void receive_radioOff_correctListener(){
        doReturn(Activity.RESULT_CANCELED)
                .when((BroadcastReceiver)receiver).getResultCode();
        receiver.onReceive(contextMock, null);
        verify(listenerMock, times(1))
                .onSMSDelivered(VALID_MESSAGE, SMSMessage.DeliveredState.DELIVERY_ERROR);
        verify(contextMock).unregisterReceiver(receiver);
    }

    @Test
    public void receive_nullPdu_correctListener(){
        doReturn(666)
                .when((BroadcastReceiver)receiver).getResultCode();
        receiver.onReceive(contextMock, null);
        verify(listenerMock, times(1))
                .onSMSDelivered(VALID_MESSAGE, SMSMessage.DeliveredState.ERROR_GENERIC_FAILURE);
        verify(contextMock).unregisterReceiver(receiver);
    }

}
