package com.marut.chat.server.bots;

import com.marut.chat.model.ChatMessage;
import com.marut.chat.model.User;
import com.marut.chat.server.ChatApplication;
import com.marut.chat.utils.EventUtils;
import com.sun.javafx.event.EventUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

/**
 * Created by marutsingh on 7/15/16.
 */
public class UserBot extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(UserBot.class);

    final String userChatUrl = "http://localhost:8080/user/message";

    User user;

    public UserBot(User user){
        this.user = user;
    }

    @Override
    public void start(Future<Void> startFuture) {
        //Subscribe to all direct messages
        vertx.eventBus().consumer(EventUtils.directMessageEvent(user.getUuid()), objectMessage -> {
            ChatMessage message = Json.decodeValue(objectMessage.body().toString(), ChatMessage.class);
            System.out.print(message);
            sendChatToUser(message.getMessage());
        });

        //Subscribe to subscription events for room chat
        vertx.eventBus().consumer(EventUtils.userRoomSubscriptionEvent(user.getUuid()), objectMessage -> {
            String roomName = objectMessage.body().toString();
            subscribeToRoomChat(roomName);
        });

        subscribeToDirectChat();
        triggerDirectChat();
        startFuture.complete();
    }

    /**
     * This will send chat event to HTTP url registered
     * @param chatMessage
     */
    public void sendChatToUser(final String chatMessage){
        String chatUrl = String.format("%s/%s",userChatUrl,user.getUuid());
        vertx.createHttpClient().postAbs(chatUrl,
                response -> {
                    response.handler(body -> {

                    });
            }).end(chatMessage);
    }

    /**
     *  //Subscribe to all chats coming to this room
     * @param room Name of the room
     */
    public void subscribeToRoomChat(String room){
        //Subscribe to actual room chat event
        ChatApplication.vertx.eventBus().consumer(EventUtils.roomChatEvent(room), objectMessage -> {
            ChatMessage chatMessage = Json.decodeValue(objectMessage.body().toString(), ChatMessage.class);
            String message = chatMessage.getMessage();
            sendChatToUser(objectMessage.body().toString());
        });
    }

    public void subscribeToDirectChat(){
        ChatApplication.vertx.eventBus().consumer(EventUtils.directMessageEvent(user.getUuid()), objectMessage -> {
            ChatMessage chatMessage = Json.decodeValue(objectMessage.body().toString(), ChatMessage.class);
            String message = chatMessage.getMessage();
            System.out.println(message);
            sendChatToUser(objectMessage.body().toString());
        });
    }

    //Subscribes for Direct Chat Event Trigger and send it to right user
    public void triggerDirectChat(){
        ChatApplication.vertx.eventBus().consumer(EventUtils.directMessageTriggerEvent(user.getUuid()), objectMessage -> {
            sendDirectChat(objectMessage.body().toString());
        });
    }

    public void sendDirectChat(String chatMessage){
        ChatMessage chatMessage1 = Json.decodeValue(chatMessage, ChatMessage.class);
        String endUser = chatMessage1.getToUser();
        ChatApplication.vertx.eventBus().publish(EventUtils.directMessageEvent(endUser),chatMessage);
    }

    public void unsubscribeFromRoom(String room){

    }
}
