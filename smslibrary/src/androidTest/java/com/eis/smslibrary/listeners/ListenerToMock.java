package com.eis.smslibrary.listeners;

import com.eis.smslibrary.SMSMessage;

public interface ListenerToMock {

    void onMessageReceived(SMSMessage message);
}
