package com.eis.smslibrary;

import android.telephony.SmsManager;

import com.eis.smslibrary.exceptions.InvalidTelephoneNumberException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Class used to test SMSManager using Mocks
 *
 * @author Marco Cognolato, Giovanni Velludo
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SmsManager.class)
public class SMSManagerTest {

    @Captor
    private ArgumentCaptor<ArrayList<String>> messageTextCaptor;

    @Captor
    private ArgumentCaptor<String> peerNumberCaptor;

    private final String VALID_PEER_NUMBER = "+393423541601";
    private final String VALID_MESSAGE_TEXT = "valid text";
    private final SMSPeer VALID_PEER = new SMSPeer(VALID_PEER_NUMBER);
    private final SMSMessage VALID_MESSAGE = new SMSMessage(VALID_PEER, VALID_MESSAGE_TEXT);
    private final SMSManager managerInstance = SMSManager.getInstance();
    private static final String EMPTY_TELEPHONE_NUMBER = "";
    private static final String TOO_SHORT_TELEPHONE_NUMBER = "+39";
    private static final String TOO_LONG_TELEPHONE_NUMBER = "+39111111111111111111";
    private static final String NO_COUNTRY_CODE_TELEPHONE_NUMBER = "1111111111";

    @Test
    public void singletonInstance() {
        assertEquals(SMSManager.getInstance(), SMSManager.getInstance());
    }

    @Test()
    public void validMessage_isSent() {
        ArrayList<String> messageText = new ArrayList<>(1);
        messageText.add(VALID_MESSAGE_TEXT);

        SmsManager managerMock = mock(SmsManager.class);
        when(managerMock.divideMessage(anyString())).thenReturn(messageText);

        PowerMockito.mockStatic(SmsManager.class);
        when(SmsManager.getDefault()).thenReturn(managerMock);

        managerInstance.sendMessage(VALID_MESSAGE);

        verify(managerMock).sendMultipartTextMessage(peerNumberCaptor.capture(),
                isNull(String.class), messageTextCaptor.capture(), isNull(ArrayList.class),
                isNull(ArrayList.class));
        assertEquals(peerNumberCaptor.getValue(), VALID_PEER_NUMBER);
        assertEquals(messageTextCaptor.getValue(), messageText);
    }

    @Test(expected = NullPointerException.class)
    public void nullMessage_throws() {
        managerInstance.sendMessage(null);
    }

    @Test(expected = InvalidTelephoneNumberException.class)
    public void sendMessage_withShortTelephoneNumber_throwsException() {
        SMSMessage message = new SMSMessage(new SMSPeer(TOO_SHORT_TELEPHONE_NUMBER), VALID_MESSAGE_TEXT);
        managerInstance.sendMessage(message);
        Assert.fail("Should have thrown an exception");
    }

    @Test(expected = InvalidTelephoneNumberException.class)
    public void sendMessage_withTooLongTelephoneNumber_throwsException() {
        SMSMessage message = new SMSMessage(new SMSPeer(TOO_LONG_TELEPHONE_NUMBER), VALID_MESSAGE_TEXT);
        managerInstance.sendMessage(message);
        Assert.fail("Should have thrown an exception");
    }

    @Test(expected = InvalidTelephoneNumberException.class)
    public void sendMessage_withNoCountryCodeTelephoneNumber_throwsException() {
        SMSMessage message = new SMSMessage(new SMSPeer(NO_COUNTRY_CODE_TELEPHONE_NUMBER), VALID_MESSAGE_TEXT);
        managerInstance.sendMessage(message);
        Assert.fail("Should have thrown an exception");
    }

    @Test(expected = InvalidTelephoneNumberException.class)
    public void sendMessage_withEmptyTelephoneNumber_throwsException() {
        SMSMessage message = new SMSMessage(new SMSPeer(EMPTY_TELEPHONE_NUMBER), VALID_MESSAGE_TEXT);
        managerInstance.sendMessage(message);
        Assert.fail("Should have thrown an exception");
    }

    @Test
    public void addPadding() {
        int notPadded = 25;
        String padded = "0000000000000000025";
        assertEquals(padded, SMSManager.addPadding(notPadded));
    }

    @Test
    public void addPadding_maxDigitsNumber() {
        long notPadded = 4512589235566663025L;
        String padded = "4512589235566663025";
        assertEquals(padded, SMSManager.addPadding(notPadded));
    }
}
