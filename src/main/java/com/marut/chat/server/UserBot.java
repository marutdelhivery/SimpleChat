package com.marut.chat.server;

import com.marut.chat.model.ChatMessage;
import com.marut.chat.model.User;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by marutsingh on 7/15/16.
 */
public class UserBot extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(AbstractVerticle.class);

    User user;

    public UserBot(User user){
        this.user = user;
    }

    @Override
    public void start() {
        //Subscribe to all direct messages
        vertx.eventBus().consumer(User.getEventName(user,ChatMessage.ChatEvents.DIRECT_MESSAGE_RECEIVED), objectMessage -> {
            ChatMessage message = Json.decodeValue(objectMessage.body().toString(), ChatMessage.class);
            System.out.print(message);
            sendChatToUser(message.getMessage());
        });

        vertx.eventBus().consumer(User.getEventName(user,ChatMessage.ChatEvents.SUBSCRIBE_TO_ROOM), objectMessage -> {
            String roomName = objectMessage.body().toString();
            subscribeToRoom(roomName);
        });
    }

    public void sendChatToUser(String chatMessage){
        String userUrl = String.format("http://localhost:8080/user/message");
        vertx.createHttpClient().postAbs(userUrl,
                response -> {
                    response.handler(body -> {

                    });
            }).end(chatMessage);
    }

    public void subscribeToRoom(String room){
        ChatApplication.vertx.eventBus().consumer(ChatMessage.ChatEvents.getRoomMessageEvent(room), objectMessage -> {
            ChatMessage chatMessage = Json.decodeValue(objectMessage.body().toString(), ChatMessage.class);
            String message = chatMessage.getMessage();
            sendChatToUser(objectMessage.body().toString());
        });
    }

    public void unsubscribeFromRoom(String room){

    }
}
