package com.eis.smslibrary;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Alberto Ursino & Luca Crema
 */
public class SMSPeerTest{

    private static final String EMPTY_TELEPHONE_NUMBER = "";
    private static final String TOO_SHORT_TELEPHONE_NUMBER = "+39";
    private static final String TOO_LONG_TELEPHONE_NUMBER = "+39111111111111111111";
    private static final String NO_COUNTRY_CODE_TELEPHONE_NUMBER = "1111111111";
    private static final String LETTERS_TELEPHONE_NUMBER = "+391111111ABC";
    private static final String VALID_TELEPHONE_NUMBER = "+391111111111";

    private SMSPeer defaultPeer1;
    private SMSPeer defaultPeer2;

    @Before
    public void setup(){
        defaultPeer1 = new SMSPeer(VALID_TELEPHONE_NUMBER + "1");
        defaultPeer2 = new SMSPeer(VALID_TELEPHONE_NUMBER + "2");
    }

    @Test
    public void checkPhoneNumber_telephoneNumber_isTooShort() {
        Assert.assertEquals(SMSPeer.TelephoneNumberState.TELEPHONE_NUMBER_TOO_SHORT, SMSPeer.checkPhoneNumber(TOO_SHORT_TELEPHONE_NUMBER));
    }

    @Test
    public void checkPhoneNumber_telephoneNumber_isTooLong() {
        Assert.assertEquals(SMSPeer.TelephoneNumberState.TELEPHONE_NUMBER_TOO_LONG, SMSPeer.checkPhoneNumber(TOO_LONG_TELEPHONE_NUMBER));
    }

    @Test
    public void checkPhoneNumber_telephoneNumber_missingCountryCode() {
        Assert.assertEquals(SMSPeer.TelephoneNumberState.TELEPHONE_NUMBER_NO_COUNTRY_CODE, SMSPeer.checkPhoneNumber(NO_COUNTRY_CODE_TELEPHONE_NUMBER));
    }

    @Test
    public void checkPhoneNumber_telephoneNumber_hasLetters() {
        Assert.assertEquals(SMSPeer.TelephoneNumberState.TELEPHONE_NUMBER_NOT_A_NUMBER, SMSPeer.checkPhoneNumber(LETTERS_TELEPHONE_NUMBER));
    }

    @Test
    public void checkPhoneNumber_telephoneNumber_isEmpty() {
        Assert.assertEquals(SMSPeer.TelephoneNumberState.TELEPHONE_NUMBER_TOO_SHORT, SMSPeer.checkPhoneNumber(EMPTY_TELEPHONE_NUMBER));
    }

    @Test
    public void staticCheckPhoneNumber_telephoneNumber_isValid() {
        Assert.assertEquals(SMSPeer.TelephoneNumberState.TELEPHONE_NUMBER_VALID, SMSPeer.checkPhoneNumber(VALID_TELEPHONE_NUMBER));
    }

    @Test
    public void constructor_telephoneNumber_noException() {
        new SMSPeer(VALID_TELEPHONE_NUMBER);
    }

    @Test
    public void constructor_shortTelephoneNumber_trowsException() {
        try{
            new SMSPeer(TOO_SHORT_TELEPHONE_NUMBER);
            Assert.fail("Should have thrown an exception");
        }catch (InvalidTelephoneNumberException e){
            //It's  ok!
        }
    }

    @Test (expected = InvalidTelephoneNumberException.class)
    public void constructor_tooLongTelephoneNumber_trowsException() {
            new SMSPeer(TOO_LONG_TELEPHONE_NUMBER);
            Assert.fail("Should have thrown an exception");
    }

    @Test(expected = InvalidTelephoneNumberException.class)
    public void constructor_noCountryCodeTelephoneNumber_trowsException() {
            new SMSPeer(NO_COUNTRY_CODE_TELEPHONE_NUMBER);
    }

    @Test(expected = InvalidTelephoneNumberException.class)
    public void constructor_emptyTelephoneNumber_trowsException() {
            new SMSPeer(EMPTY_TELEPHONE_NUMBER);
            Assert.fail("Should have thrown an exception");
    }

    @Test
    public void compareTo_differentPeer_isOrdered() {
        Assert.assertTrue(defaultPeer1.compareTo(defaultPeer2) < 0);
    }

    @Test
    public void compareTo_invertedOrder_isOrdered() {
        Assert.assertTrue(defaultPeer2.compareTo(defaultPeer1) > 0);
    }

    @Test
    public void compareTo_samePeer_isZero() {
        Assert.assertTrue(defaultPeer1.compareTo(defaultPeer1) == 0);
    }

    @Test
    public void getAddress_isSame() {
        Assert.assertEquals(VALID_TELEPHONE_NUMBER + "1", defaultPeer1.getAddress());
    }

    @Test
    public void getAddress_isNotSame() {
        Assert.assertNotEquals(VALID_TELEPHONE_NUMBER, defaultPeer1.getAddress());
    }

    @Test
    public void equals_self_isTrue() {
        Assert.assertTrue(defaultPeer1.equals(defaultPeer1));
    }

    @Test
    public void equals_different_isFalse() {
        Assert.assertFalse(defaultPeer1.equals(defaultPeer2));
    }
    @Test
    public void equals_null_isFalse() {
        Assert.assertFalse(defaultPeer1.equals(null));
    }

    @Test
    public void equals_differentClass_isFalse() {
        Assert.assertFalse(defaultPeer1.equals(new Object()));
    }
}

