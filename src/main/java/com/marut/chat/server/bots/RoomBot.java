package com.marut.chat.server.bots;

import com.marut.chat.model.ChatMessage;
import com.marut.chat.model.Room;
import com.marut.chat.model.User;
import com.marut.chat.persistence.RoomService;
import com.marut.chat.persistence.UserService;
import com.marut.chat.server.ChatApplication;
import com.marut.chat.utils.EventUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by marutsingh on 8/4/16.
 */

/**
 * Room bot will listen to events sent to room and broadcast those events to all subscribers for the room
 */
public class RoomBot extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(RoomBot.class);

    String room;

    public RoomBot(String room){
        this.room = room;
    }

    @Override
    public void start() {
        //Once a room is created
        //Subscribe to Room subscription message...
        vertx.eventBus().consumer(EventUtils.roomChatEvent(room), objectMessage -> {

            vertx.eventBus().publish(EventUtils.userRoomChatEvent(room),objectMessage);
//            //We are expecting user data to come here
//            //If some user subscribes to this room then we get the user and
//            User message = Json.decodeValue(objectMessage.body().toString(), User.class);
//            RoomService roomService = ChatApplication.getApplicationContext().getBean(RoomService.class);
//            roomService.registerUserToRoom(room,message.getUuid());
            //Tell corresponding user bot to start listening to room events as well
        });
    }

//    void subscribeChatEvent(){
//        vertx.eventBus().consumer(EventUtils.roomChatEvent(room.getRoomName()), objectMessage -> {
//            //We are expecting user data to come here
//            User message = Json.decodeValue(objectMessage.body().toString(), User.class);
//            RoomService roomService = ChatApplication.getApplicationContext().getBean(RoomService.class);
//            roomService.removeUserFromRoom(room, message.getUuid());
//        });
//    }
}
