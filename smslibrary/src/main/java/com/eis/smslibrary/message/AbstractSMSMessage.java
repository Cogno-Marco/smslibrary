package com.eis.smslibrary.message;

import androidx.annotation.NonNull;

import com.eis.communication.header.Header;
import com.eis.communication.Message;
import com.eis.smslibrary.exceptions.InvalidSMSMessageException;

/**
 *AbstractSMSMessage is the base {@link Message} having all the common properties an {@link Message} in this layer must have
 * <ul>
 *     <li>an {@link Header} to contain service information</li>
 *     <li><code>MAX_MESSAGE_TEXT_LENGTH</code> to check if the whole data chunk can be stored</li>
 *     <li>a <code>String</code> data filed to store data</li>
 * </ul>
 * <i>Used as a base for building other more complex data-link messages without code replication</i>
 *
 * @param <H> type of header of the <code>AbstractSMSMessage</code>
 *
 * @author Luca Crema
 * @author Marco Mariotto
 * @author Alberto Ursino
 * @author Mattia Fanan
 *
 * @see Message
 * @since 08/12/2019 (gg/mm/aaaa)
 */
public class AbstractSMSMessage<H extends Header<String>> implements Message<H,String> {
    /**
     * Kind of a magic number, should be around 160 but doesn't work all the times.
     * (suggestions accepted)
     */
    public static final int MAX_MESSAGE_TEXT_LENGTH = 155;
    protected String messageContent;
    protected H messageHeader;

    /**
     * Constructor for <code>AbstractSMSMessage</code>
     *
     * @param messageHeader the header of this message
     * @param messageText   the content of this message, can be empty but not null
     * @throws InvalidSMSMessageException if {@link #checkMessageText} is different from MESSAGE_TEXT_VALID
     */
    public AbstractSMSMessage(@NonNull final H messageHeader, @NonNull final String messageText) throws InvalidSMSMessageException {

        if(messageHeader == null || messageText == null)
            throw new NullPointerException();

        //Checks on the message text
        ContentState contentState = checkMessageText(messageText);

        if (contentState != ContentState.MESSAGE_TEXT_VALID)
            throw new InvalidSMSMessageException("Message text length exceeds maximum allowed", contentState);

        this.messageContent = messageText;
        this.messageHeader = messageHeader;
    }

    /**
     * Checks if the message content is valid.
     *
     * @param messageText the message to be checked.
     * @return the state of the message after the validity tests.
     */
    public static ContentState checkMessageText(@NonNull final String messageText) {

        if (messageText.length() > SMSMessageReceived.MAX_MESSAGE_TEXT_LENGTH) {
            return ContentState.MESSAGE_TEXT_TOO_LONG;
        }
        return ContentState.MESSAGE_TEXT_VALID;
    }

    /**
     * Gets the data stored in this message
     *
     * @return the data contained in this message
     */
    @Override
    public String getData() {
        return messageContent;
    }

    /**
     * Gets the header contained in this message
     *
     * @return the header contained in this message
     */
    @Override
    public H getHeader() {
        return messageHeader;
    }

    /**
     * Possible states of a message after a check
     * There could be more in future so it has been used an enum
     * <p>
     * These are used in {@link #checkMessageText}
     */
    public enum ContentState {
        MESSAGE_TEXT_VALID,
        MESSAGE_TEXT_TOO_LONG
    }

    /**
     * Possible states of a message after it has been sent
     * These are given by Android library as an int, they have been put in an enum
     * so that one can see all the possible values without having to
     * look at the official Android documentation
     * <p>
     * These are used in {@link com.eis.smslibrary.listeners.SMSSentListener}
     */
    public enum SentState {
        MESSAGE_SENT,
        ERROR_GENERIC_FAILURE,
        ERROR_RADIO_OFF,
        ERROR_NULL_PDU,
        ERROR_NO_SERVICE,
        ERROR_LIMIT_EXCEEDED
    }

    /**
     * Possible states of a message after it has been delivered,
     * These are given by Android library as an int, they have been put in an enum so
     * that one can see al the possible values without having to look
     * at the official Android documentation
     *
     * These are used in {@link com.eis.smslibrary.listeners.SMSDeliveredListener}
     */
    public enum DeliveredState {
        MESSAGE_DELIVERED,
        DELIVERY_ERROR,
        ERROR_GENERIC_FAILURE
    }

}
