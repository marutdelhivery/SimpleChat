package com.marut.chat.persistence;

import com.marut.chat.model.User;
import com.marut.chat.server.ChatApplication;
import com.marut.chat.server.bots.UserBot;
import com.marut.chat.utils.EventUtils;
import io.vertx.core.AsyncResult;
import io.vertx.core.AsyncResultHandler;
import io.vertx.core.Handler;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by marutsingh on 7/19/16.
 */
@Service
public class UserService {

    private ConcurrentHashMap<String,Boolean> userBots = new ConcurrentHashMap<>();
    private Map<String,User> users = new HashMap<>();

    public void init(){
        create("user1");
        create("user2");
    }

    public User findUser(String uuid){
        return users.get(uuid);
    }

    public User create(String name){
        User user = new User();
        user.setName(name);
        user.setUuid(name);
        users.put(user.getUuid(),user);
        return user;
    }

    public User create(User user) {
        users.put(user.getUuid(),user);
        return user;
    }

    public void startUserBot(String userId,Handler<AsyncResult<String>> var2){
        if (!userBots.containsKey(userId)){
            UserBot userBot = new UserBot(findUser(userId));
            ChatApplication.vertx.deployVerticle(userBot, new AsyncResultHandler<String>() {
                @Override
                public void handle(AsyncResult<String> stringAsyncResult) {
                    if (stringAsyncResult.succeeded()){
                        userBots.put(userId,Boolean.TRUE);
                    }
                    var2.handle(stringAsyncResult);
                }
            });
        }
    }

    public void sendDirectChat(String userId,String chatMessage){
        ChatApplication.vertx.eventBus().publish(EventUtils.directMessageTriggerEvent(userId), chatMessage);
    }

//    public AbstractVerticle getUserBot(String userId){
//        ChatApplication.vertx.verticleFactories().stream().findFirst().get().
//    }
}
