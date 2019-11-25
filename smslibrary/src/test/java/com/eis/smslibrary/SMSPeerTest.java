package com.eis.smslibrary;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Alberto Ursino
 */
public class SMSPeerTest{

    protected static final String EMPTY_TELEPHONE_NUMBER = "";
    protected static final String TOO_SHORT_TELEPHONE_NUMBER = "+39111";
    protected static final String TOO_LONG_TELEPHONE_NUMBER = "+39111111111111111111";
    protected static final String NO_COUNTRY_CODE_TELEPHONE_NUMBER = "1111111111";
    protected static final String LETTERS_TELEPHONE_NUMBER = "+391111111ABC";
    protected static final String VALID_TELEPHONE_NUMBER = "+391111111111";

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
}

