package com.marut.chat.auth;

import com.marut.chat.events.UserEvents;
import com.marut.chat.model.User;
import com.marut.chat.server.bots.UserBot;
import io.vertx.core.Vertx;

/**
 * Created by marutsingh on 7/19/16.
 */
public class UserRegistration {

    Vertx vertx;

    public void register(String emailId){
        User user = new User();
        user.setUuid(emailId);
        UserBot userBot = new UserBot(user);
        vertx.eventBus().publish(UserEvents.getEventName(UserEvents.REGISTRATION_SUCCESSFUL),userBot);
        vertx.deployVerticle(userBot); //Ready to listen to chat events
    }
}
