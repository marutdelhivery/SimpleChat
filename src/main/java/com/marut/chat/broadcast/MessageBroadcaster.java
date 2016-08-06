package com.marut.chat.broadcast;

import com.marut.chat.annotation.ChatVerticle;
import com.marut.chat.model.ChatMessage;
import com.marut.chat.model.User;
import com.marut.chat.persistence.UserService;
import com.marut.chat.server.ChatApplication;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by marutsingh on 7/19/16.
 * Main Verticle where all chat messages will come
 */
@Service
public class MessageBroadcaster {

    Vertx vertx;
    @PostConstruct
    void init(){
        vertx = ChatApplication.vertx;
    }
}
