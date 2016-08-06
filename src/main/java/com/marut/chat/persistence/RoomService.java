package com.marut.chat.persistence;

import com.marut.chat.model.ChatMessage;
import com.marut.chat.model.Room;
import com.marut.chat.model.User;
import com.marut.chat.server.ChatApplication;
import com.marut.chat.utils.EventUtils;
import io.vertx.core.AsyncResult;
import io.vertx.core.AsyncResultHandler;
import io.vertx.core.Verticle;
import io.vertx.core.shareddata.AsyncMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by marutsingh on 7/19/16.
 */
@Service
public class RoomService {

    @Autowired
    UserService userService;

    Map<String,List<String>> roomUserMap = new HashMap<>();
    Map<String,Verticle> userBots = new HashMap<>();
    Map<String,Room> userRooms = new HashMap<>();

    public Room findRoom(String ownerId, String roomId){
        if (!userRooms.containsKey(roomId)){
            Room room = new Room();
            room.setRoomName(roomId);
            userRooms.put(ownerId,room);
        }
        return userRooms.get(ownerId);
    }

    public void registerUserToRoom(Room room,String userId){
        if (!room.getUserIds().contains(userId)){
            //Create a new Bot and start listening to Room Events
            room.getUserIds().add(userId);
            //Notify User Bot that you are subscribed to this room's chats
            ChatApplication.vertx.eventBus()
                    .publish(EventUtils.userRoomSubscriptionEvent(userId), room.getRoomName());
        }
    }

    public void removeUserFromRoom(Room room,String userId){
        if (room.getUserIds().contains(userId)){
            //Create a new Bot and start listening to Room Events
            room.getUserIds().remove(userId);
            ChatApplication.vertx.eventBus()
                    .publish(Room.getEventName(room.getRoomName(), Room.RoomEvents.UNSUBSCRIBE_FROM_ROOM)
                            , room.getRoomName());
        }
    }

    public void sendRoomChat(String chatMessage,String roomId){
        //Broadcast to all subscribers new message has arrived
        ChatApplication.vertx.eventBus().publish(EventUtils.roomChatEvent(roomId),chatMessage);
    }
}
