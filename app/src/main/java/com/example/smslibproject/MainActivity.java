package com.example.smslibproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis.smslibrary.listeners.SMSSentListener;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SMSManager.getInstance().setReceivedListener(ReceivedMessageListener.class, this);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendMessage();
            }
        });

    }

    public void onSendMessage() {
        SMSManager.getInstance().sendMessage(new SMSMessage(new SMSPeer("+393467965447"), "Heya"), new SMSSentListener() {
            @Override
            public void onSMSSent(SMSMessage message, SMSMessage.SentState sentState) {
                Toast.makeText(MainActivity.this, "Inviato", Toast.LENGTH_LONG).show();
            }
        }, this);
    }


}
