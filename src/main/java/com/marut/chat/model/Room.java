package com.marut.chat.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marutsingh on 7/19/16.
 */
public class Room {
    //Some regex has to be here to fix room name
    private String roomName;
    private String owner; //User Who owns this room
    private ROOM_TYPE roomType;

    private List<String> userIds = new ArrayList<>(); //List of user Ids registered for this room

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public ROOM_TYPE getRoomType() {
        return roomType;
    }

    public void setRoomType(ROOM_TYPE roomType) {
        this.roomType = roomType;
    }

    public void registerUser(String userId){
        userIds.add(userId);
    }

    public static enum ROOM_TYPE{
        PRIVATE,PUBLIC
    }

    public static enum  RoomEvents {
        SUBSCRIBE_TO_ROOM,UNSUBSCRIBE_FROM_ROOM,MESSAGE_TO_ROOM;
    }

    public static String getEventName(String roomName,RoomEvents roomEvents){
        return String.format("room.%s.%s",roomName,roomEvents.name());
    }
}
