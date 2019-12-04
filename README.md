# SMS library
Android library to send and receive SMS messages quickly.

# Installation
Installation is done using [Jitpack](https://jitpack.io).

Add to the root application `build.gradle`
```java
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
In the single module `build.gradle` add
```java
dependencies {
    ...
    implementation 'com.github.Cogno-IDU:smslibrary:v-1.0'
}
```
# Usage

### Permissions
In order to send and receive SMSs you need to be granted two permissions. From Android 6.0 you have to use
`requestPermissions`, and this is the way you do it.
```java
requestPermissions(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS},SMS_PERMISSION_CUSTOM_CODE);
```

### Setup
First, the library must be initialized before every use
```java
SMSHandler.getInstance().setup(context);
```

### Creating a new message
When you want to send a message you should create it first to make sure it's valid
```java
String telehponeNumber = "+3934693437234";
String messageContent = "Message content";
if(!SMSPeer.checkPhoneNumber(telephoneNumber))
    return;
SMSPeer destinationPeer = new SMSPeer(telehponeNumber);
if(!SMSMessage.checkMessageText(messageContent))
    return;
SMSMessage msg = new SMSMessage(destinationPeer, "Content of the message");
```
Or, if you're a `try-catch` fan, and not a fan of `if`s
```java
try{
    SMSPeer destinationPeer = new SMSPeer(telehponeNumber);
}catch(InvalidTelephoneNumberException e){
    //Do something about it.
}
try{
    SMSMessage msg = new SMSMessage(destinationPeer, messageContent);
}catch(InvalidSMSMessageException e){
    //Do something about it.
}
```

### Sending a message
When the message is created you are ready to send it
```java
SMSHandler.getInstance().sendMessage(msg);
```
If you need a callback to know if the message has been sent or not you can use
```java
SMSHandler.getInstance().sendMessage(msg, new SMSSentListener() {
    @Override
    public void onSMSSent(SMSMessage message, SMSMessage.SentState sentState) {
        //Do something.
    }
});
```

If you planning to use SMS delivery reports, you can have a callback for those too
```java
SMSHandler.getInstance().sendMessage(msg, new SMSDeliveredListener() {
    @Override
    public void onSMSDelivered(SMSMessage message, SMSMessage.DeliveredState deliveredState) {
        //Do something.
    }
});
```
You can use both callbacks together.

### Receiving a message
In order to register the application to be called on message reception you have
 to register a custom listener service that extends `SMSReceivedServiceListener`.
 To do so you have to receive a class (not an instance)
 ```java
 SMSHandler.getInstance().setReceivedListener(MyCustomReceiver.class);
 ```
 When a message arrives the overridden method `onMessageReceived` will be called.
 
### Using your own message format
The default library format uses an hidden character to differentiate SMSs
sent from library and SMSs sent from another source. You can override this
format by passing a custom `MessageParseStrategy<String, SMSPeer, SMSMessage>`
to the `SMSMessageHandler` class
```java
SMSMessageHandler.getInstance().setMessageParseStrategy(customStrategy);
```