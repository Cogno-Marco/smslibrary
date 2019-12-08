package com.eis.smslibrary.message;

import androidx.annotation.NonNull;

import com.eis.smslibrary.exceptions.InvalidSMSMessageException;
import com.eis.smslibrary.header.SMSToSendHeader;

/**
 * SMSMessageToSend is {@link AbstractSMSMessage} extension taking as <code>Header</code> {@link SMSToSendHeader}
 *
 * @author Mattia Fanan
 * @since 08/12/2019 (gg/mm/aaaa)
 */
public final class SMSMessageToSend extends AbstractSMSMessage<SMSToSendHeader> {
    /**
     * Constructor for SMSMessageToSend
     *
     * @param messageHeader the header of the this message
     * @param messageText   the content of this message, can be empty but not null
     * @throws InvalidSMSMessageException if {@link AbstractSMSMessage#checkMessageText} is different from MESSAGE_TEXT_VALID
     */
    public SMSMessageToSend(@NonNull SMSToSendHeader messageHeader, @NonNull String messageText) throws InvalidSMSMessageException {
        super(messageHeader, messageText);
    }
}
