package com.eis.smslibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eis.communication.MessageHandler;
import com.eis.communication.MessageParsingStrategy;
import com.eis.smslibrary.exceptions.InvalidTelephoneNumberException;

/**
 * Singleton class used to parse String to SMSMessage and back
 * Uses a strategy to parse messages, so that any user can update it to its preferred parser
 * By defaults it uses a default strategy, defined by the library
 *
 * @author Luca Crema, Alberto Ursino, Marco Mariotto, Giovanni Velludo
 */
public class SMSMessageHandler implements MessageHandler<String, String, SMSMessage> {

    private MessageParsingStrategy<String, SMSPeer, SMSMessage> parseStrategy;
    private static SMSMessageHandler instance;

    /**
     * Private constructor.
     */
    private SMSMessageHandler(@Nullable final MessageParsingStrategy<String, SMSPeer, SMSMessage> parseStrategy) {
        if (parseStrategy == null) {
            this.parseStrategy = new DefaultSMSMessageParsingStrategy();
        } else {
            this.parseStrategy = parseStrategy;
        }
    }

    /**
     * Sets a custom {@link MessageParsingStrategy} and retrieves a new instance of
     * {@link SMSMessageHandler}, which will use the {@link MessageParsingStrategy} that was just
     * set.
     * The strategy must be set before calling any other methods from classes in
     * {@link com.eis.smslibrary}, otherwise {@link DefaultSMSMessageParsingStrategy} will be used
     * and there will be no way to change it.
     * It's possible to set a custom {@link MessageParsingStrategy} only the first time this method
     * is called. Passing a {@link MessageParsingStrategy} in calls made after the first one won't
     * replace the strategy that was set the first time.
     *
     * @param parseStrategy the {@link MessageParsingStrategy} to use. If {@code null}, the
     *                      {@link DefaultSMSMessageParsingStrategy} will be used, unless this
     *                      method was already called and passed a {@link MessageParsingStrategy}.
     *                      Passing a {@link MessageParsingStrategy} in calls made after the first
     *                      one won't replace the strategy that was set the first time.
     * @return Singleton instance of this class.
     */
    public static SMSMessageHandler getInstance(@Nullable final MessageParsingStrategy<String, SMSPeer, SMSMessage> parseStrategy) {
        if (instance == null) {
            instance = new SMSMessageHandler(parseStrategy);
        }
        return instance;
    }

    /**
     * Parses a String received via SMS PDUs, and creates an {@link SMSMessage} according to the
     * strategy set with {@link SMSMessageHandler#getInstance(MessageParsingStrategy)}.
     *
     * @param phoneNumber Phone number of the sender, retrieved from the SMS PDUs.
     * @param text        Text of the message, retrieved from the SMS PDUs.
     * @return The resulting {@link SMSMessage} if {@code text} was parsed correctly, null
     * otherwise.
     * @author Matteo Carnelos
     */
    public SMSMessage parseData(@NonNull final String phoneNumber, @NonNull final String text) {
        try {
            SMSPeer peer = new SMSPeer(phoneNumber);
            return parseStrategy.parseData(peer, text);
        } catch (InvalidTelephoneNumberException | IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Parses an {@link SMSMessage}, and returns its {@code text} modified according to the strategy
     * set with {@link SMSMessageHandler#getInstance(MessageParsingStrategy)}.
     *
     * @param message The {@link SMSMessage}to be parsed and modified.
     * @return the message text modified according to the strategy.
     */
    public String parseMessage(@NonNull final SMSMessage message) {
        return parseStrategy.parseMessage(message);
    }

    public class DefaultSMSMessageParsingStrategy implements MessageParsingStrategy<String, SMSPeer, SMSMessage> {

        static final String HIDDEN_CHARACTER = "¤";

        /**
         * Parses a String received via SMS PDUs, and creates an {@link SMSMessage} if the PDUs came
         * from a phone using the same {@link MessageParsingStrategy}.
         *
         * @param sender The sender of the SMS, retrieved from the SMS PDUs.
         * @param text   Text of the message, retrieved from the SMS PDUs.
         * @return The resulting {@link SMSMessage} if {@code text} was parsed correctly, null
         * otherwise.
         */
        @Override
        public SMSMessage parseData(@NonNull final SMSPeer sender, @NonNull final String text) {
            // First character of the content must be the hidden char
            if (!text.startsWith(HIDDEN_CHARACTER))
                return null;
            String messageData = text.substring(1);
            return new SMSMessage(sender, messageData);
        }

        /**
         * Parses an {@link SMSMessage}, and returns its {@code text} with
         * {@link DefaultSMSMessageParsingStrategy#HIDDEN_CHARACTER} added at the beginning.
         *
         * @param message The {@link SMSMessage}to be parsed and modified.
         * @return the message text with {@link DefaultSMSMessageParsingStrategy#HIDDEN_CHARACTER}
         * added at the beginning.
         */
        @Override
        public String parseMessage(@NonNull final SMSMessage message) {
            return HIDDEN_CHARACTER + message.getData();
        }
    }

}


