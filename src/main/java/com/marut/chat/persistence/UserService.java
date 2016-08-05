package com.marut.chat.persistence;

import com.marut.chat.model.User;
import com.marut.chat.server.ChatApplication;
import com.marut.chat.server.bots.UserBot;
import com.marut.chat.utils.EventUtils;
import io.vertx.core.AsyncResult;
import io.vertx.core.AsyncResultHandler;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by marutsingh on 7/19/16.
 */
@Service
public class UserService {

    private Map<String,String> userBots = new HashMap<>();

    public void init(){
        startUserBot("user1");
        startUserBot("user2");
    }

    public User findUser(String uuid){
        User user = new User();
        user.setUuid(uuid);
        return user;
    }

    public void startUserBot(String userId){
        UserBot userBot = new UserBot(findUser(userId));
        ChatApplication.vertx.deployVerticle(userBot, new AsyncResultHandler<String>() {
            @Override
            public void handle(AsyncResult<String> stringAsyncResult) {
                if (stringAsyncResult.succeeded()){
                    userBots.put(userId,stringAsyncResult.result());
                }
            }
        });
    }

    public void sendDirectChat(String chatMessage,String userId){
        ChatApplication.vertx.eventBus().send(EventUtils.directMessageTriggerEvent(userId),chatMessage);
    }

//    public AbstractVerticle getUserBot(String userId){
//        ChatApplication.vertx.verticleFactories().stream().findFirst().get().
//    }
}
