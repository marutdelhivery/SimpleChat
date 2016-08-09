package com.marut.chat.persistence;

import com.marut.chat.model.ChatMessage;
import com.marut.chat.model.Room;
import com.marut.chat.model.User;
import com.marut.chat.server.ChatApplication;
import com.marut.chat.server.bots.RoomBot;
import com.marut.chat.utils.EventUtils;
import io.vertx.core.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.shareddata.AsyncMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by marutsingh on 7/19/16.
 */
@Service
public class RoomService {

    @Autowired
    UserService userService;
    Map<String,Room> userRooms = new HashMap<>();

    public Room findRoom(String ownerId, String roomId){
        if (!userRooms.containsKey(roomId)){
            Room room = new Room();
            room.setRoomName(roomId);
            userRooms.put(ownerId,room);
        }
        return userRooms.get(ownerId);
    }


    public Room createRoom(String room){
        //Create a new Bot and start listening to Room Events
        Room room1 = new Room();
        room1.setRoomName(room);
        RoomBot roomBot = new RoomBot(room);
        if (ChatApplication.getRoomBotsMap().get(room) == null) {
            ChatApplication.vertx.deployVerticle(roomBot, new Handler<AsyncResult<String>>() {
                @Override
                public void handle(AsyncResult<String> stringAsyncResult) {
                    if (stringAsyncResult.succeeded()) {
                        ChatApplication.getRoomBotsMap().put(room, stringAsyncResult.result());
                    }
                }
            });
        }
        return room1;
    }

    public void deleteRoom(String roomName){
        String deploymentId = ChatApplication.getRoomBotsMap().get(roomName);
        if ( deploymentId != null) {
            ChatApplication.vertx.undeploy(deploymentId);
        }
    }

    public void registerUserToRoom(String room,String userId){
        //Create a new Bot and start listening to Room Events
        //Notify User Bots that you are subscribed to this room's chat
        ChatApplication.vertx.eventBus()
                    .publish(EventUtils.userRoomSubscriptionEvent(userId), room);
    }

    public void removeUserFromRoom(String room,String userId){
        ChatApplication.vertx.eventBus()
                .publish(EventUtils.userRoomUnsubscriptionEvent(userId)
                        , room);
    }

    public void sendRoomChat(String chatMessage,String roomId){
        //Broadcast to all subscribers new message has arrived
        ChatApplication.vertx.eventBus().publish(EventUtils.roomChatEvent(roomId), chatMessage);
    }

    /*private JsonArray getSubscribedUsers(String room){
        return ChatApplication.getRoomsMap().get(room);
    }*/
}
