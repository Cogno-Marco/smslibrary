# SMS library
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/6a35c6f870564e06a4ddbc15c0299e86)](https://www.codacy.com/manual/CremaLuca/smslibrary?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Cogno-IDU/smslibrary&amp;utm_campaign=Badge_Grade)

Android library to send and receive SMS messages quickly.

## Installation
Installation is done using [Jitpack](https://jitpack.io).

Add to the root application `build.gradle`:
```java
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
In the single module `build.gradle` add:
```java
dependencies {
    ...
    implementation 'com.github.Cogno-IDU:smslibrary:v2.1'
}
```
## Usage

### Permissions
In order to send and receive SMSs you need to be granted two permissions. From Android 6.0 you have to use
`requestPermissions`, and this is the way you do it:
```java
requestPermissions(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS}, SMS_PERMISSIONS_CUSTOM_CODE);
```

### Setup
By default, the library uses an hidden character to differentiate SMSs sent from library and SMSs
sent from another source. You can override this way of differentiating SMSs by passing a custom
`MessageParseStrategy<String, SMSPeer, SMSMessage>` to the `SMSMessageHandler` class:
```java
SMSMessageHandler.getInstance(customStrategy);
```
This must be done before using any other constructors or methods from the library. If this is not
done, or if the argument of `getInstance()` is `null`, `DefaultSMSMessageParsingStrategy` will be
used.

### Creating a new message
When you want to send a message you should create it first to make sure it's valid:
```java
String telehponeNumber = "+393401234567";
String messageContent = "Message content";

try {
    SMSPeer destinationPeer = new SMSPeer(telehponeNumber);
} catch(InvalidTelephoneNumberException e) {
    // Do something about it
}
try {
    SMSMessage msg = new SMSMessage(destinationPeer, messageContent);
} catch(InvalidSMSMessageException e) {
    // Do something about it
}
```

### Sending a message
When the message is created you are ready to send it with:
```java
SMSManager.getInstance().sendMessage(msg);
```
If you need a callback to know if the message has been sent or not you can use:
```java
SMSManager.getInstance().sendMessage(msg, new SMSSentListener() {
    @Override
    public void onSMSSent(SMSMessage message, SMSMessage.SentState sentState) {
        // Do something
    }
});
```
If you are planning to use SMS delivery reports, you can have a callback for those too:
```java
SMSManager.getInstance().sendMessage(msg, new SMSDeliveredListener() {
    @Override
    public void onSMSDelivered(SMSMessage message, SMSMessage.DeliveredState deliveredState) {
        // Do something
    }
});
```
You can use both callbacks together.

### Receiving a message
In order to register the application to be called on message reception you have
 to register a custom listener service that extends `SMSReceivedServiceListener`.
 To do so you have to receive a class (not an instance):
 ```java
 SMSManager.getInstance().setReceivedListener(MyCustomReceiver.class);
 ```
 This service must be registered in the manifest inside the `<application>` tags like this:
 ```java
 <service
            android:name=".MyCustomReceiver"
            android:enabled="true"
            android:exported="true"
            android:permission="android.permission.BIND_JOB_SERVICE" />
```
 When a message arrives the overridden method `onMessageReceived` will be called.