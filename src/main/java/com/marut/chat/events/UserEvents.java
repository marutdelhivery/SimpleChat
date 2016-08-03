package com.marut.chat.events;

/**
 * Created by marutsingh on 7/19/16.
 */
public enum UserEvents {

    REGISTER,UNREGISTER,REGISTRATION_SUCCESSFUL,REGISTRATION_UNSUCCESSFUL;

    public static final String prefix = "user.event";

    public static String getEventName(UserEvents userEvents){
        return String.format("%s.%s",prefix,userEvents.name());
    }
}
