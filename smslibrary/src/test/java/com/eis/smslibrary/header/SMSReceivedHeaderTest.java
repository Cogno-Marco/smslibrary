package com.eis.smslibrary.header;

import com.eis.smslibrary.SMSPeer;
import com.eis.smslibrary.SMSPeerTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Mattia Fanan
 */
public class SMSReceivedHeaderTest {

    private SMSReceivedHeader validHeader;
    private SMSPeer validPeer;
    private SMSPeer nullPeer = null;

    @Before
    public void init() {
        validPeer = new SMSPeer(SMSPeerTest.VALID_TELEPHONE_NUMBER);
        validHeader = new SMSReceivedHeader(validPeer);
    }

    @Test
    public void getID(){
        Assert.assertTrue(validHeader.getID().equals(AbstractSMSHeader.ID));
    }

    @Test
    public void setUP(){
        try {
            new SMSReceivedHeader(validPeer);
        }
        catch (NullPointerException e){Assert.fail("shouldn't throw InvalidTelephoneNumberException");}
    }

    @Test(expected = NullPointerException.class)
    public void setUPNull(){
        new SMSReceivedHeader(nullPeer);
        Assert.fail("should throw NullPointerException");
    }

    @Test
    public void getSource_validPeer_isEqual(){
        Assert.assertEquals(validHeader.getSource(), validPeer);
    }

    @Test
    public void equals(){
        SMSReceivedHeader equalHeader = new SMSReceivedHeader( new SMSPeer(SMSPeerTest.VALID_TELEPHONE_NUMBER));
        Assert.assertEquals(validHeader, equalHeader);
    }

    @Test
    public void equalsSame(){
        Assert.assertEquals(validHeader,validHeader);
    }

    @Test
    public void equalsAnotherClass(){
        Assert.assertNotEquals(validHeader,validPeer);
    }

    @Test
    public void equalsDifferent(){
        SMSReceivedHeader differentHeader = new SMSReceivedHeader( new SMSPeer(SMSPeerTest.ANOTHER_VALID_TELEPHONE_NUMBER));
        Assert.assertNotEquals(validHeader, differentHeader);
    }
}
