package com.eis.communication.listener;

import com.eis.communication.DataUnit;

/**
 * ReceivedMessageListener interface
 * @author gruppo1
 *
 * Contains the interface of the method to call when an SMS is received
 * @param <T> Message received data-type
 */
public interface ReceivedMessageListener<T extends DataUnit> {
    /**
     * Called by NotificatonEraser whenever a message is received.
     * @param message The message received
     */
    void onMessageReceived(T message);

}
