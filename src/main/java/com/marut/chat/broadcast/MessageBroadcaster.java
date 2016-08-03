package com.marut.chat.broadcast;

import com.marut.chat.annotation.ChatVerticle;
import com.marut.chat.model.ChatMessage;
import com.marut.chat.model.User;
import com.marut.chat.persistence.UserService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import org.springframework.stereotype.Service;

/**
 * Created by marutsingh on 7/19/16.
 * Main Verticle where all chat messages will come
 */
@Service
@ChatVerticle
public class MessageBroadcaster extends AbstractVerticle {

    Vertx vertx;

    @Override
    public void start() {
//        vertx.eventBus().consumer(ChatMessage.ChatEvents.DIRECT_MESSAGE_RECEIVED.name(), new Handler<Message<String>>() {
//            @Override
//            public void handle(Message<String> objectMessage) {
//                ChatMessage chatMessage = Json.decodeValue(objectMessage.body(),ChatMessage.class);
//                broadcastMessage(chatMessage);
//            }
//        });
    }

    public void sendDirectMessage(String toUser,String chatMessage){
        String eventName = User.getEventName(toUser,ChatMessage.ChatEvents.DIRECT_MESSAGE_RECEIVED);
        vertx.eventBus().publish(eventName, chatMessage);
    }

    public void sendToRoom(String room, ChatMessage chatMessage) {
        UserService userService = new UserService();
        //Send the event to To-User Verticle which will eventually save the event
        String evtName = ChatMessage.ChatEvents.getRoomMessageEvent(room);
        vertx.eventBus().publish(evtName, Json.encode(chatMessage));
    }
}
