package com.eis.smslibrary.core;

import androidx.annotation.NonNull;

/**
 * APIMessage contains all the data Android needs to send a sms
 *
 * @author Mattia Fanan
 * @since 08/12/2019 (gg/mm/aaaa)
 */
public final class APIMessage {
    private String phoneNumber;
    private String textMessage;

    /**
     * Constructor for APIMessage
     *
     * @param phoneNumber the phone number of this message
     * @param textMessage the text of this message
     */
    public APIMessage(@NonNull final String phoneNumber, @NonNull final String textMessage) {

        if (phoneNumber == null || textMessage == null)
            throw new NullPointerException();

        this.textMessage = textMessage;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the message's phone number
     *
     * @return the phone number of the message
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Gets the text of this message
     *
     * @return the text of this message
     */
    public String getTextMessage() {
        return textMessage;
    }

}

