package com.eis.communication;

/**
 * Class used to switch from actual communication data to Message and back
 * Should use a <a href='https://en.wikipedia.org/wiki/Singleton_pattern'>Singleton pattern</a>
 *
 * @param <M> The type of message to parse and output
 */
public interface MessageHandler<M extends Message> {

    /**
     * Interprets a string arrived via the communication channel and parses it to a message
     *
     * @param data data from the communication channel
     * @return the message if the string has been parsed correctly, null otherwise
     */
    M parseMessage(String data, String peerData);

    /**
     * Translates a message into a string that can be sent via communication channel
     *
     * @param message message to be translated
     * @return the string to send
     */
    String getOutput(M message);
}
