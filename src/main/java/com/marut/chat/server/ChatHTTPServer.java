package com.marut.chat.server;

import com.marut.chat.broadcast.MessageBroadcaster;
import com.marut.chat.model.ChatMessage;
import com.marut.chat.model.Room;
import com.marut.chat.model.User;
import com.marut.chat.persistence.RoomService;
import com.marut.chat.persistence.UserService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by marutsingh on 7/15/16.
 */
@Service
public class ChatHTTPServer extends AbstractVerticle {

    @Autowired
    RoomService roomService;

    @Autowired
    UserService userService;

    @Autowired
    MessageBroadcaster messageBroadcaster;

    @Override
    public void start(Future<Void> fut) {

        Router router = Router.router(vertx);

        router.route("/user").handler(BodyHandler.create());
        router.route("/user").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            User user = userService.create(Json.decodeValue(routingContext.getBodyAsString(), User.class));
            response.end(Json.encode(user));
        });

        //Creates a new Room
        router.route("/room").handler(BodyHandler.create());
        router.route("/room").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            Room room = roomService.createRoom(Json.decodeValue(routingContext.getBodyAsString(), Room.class));
            response.end(Json.encode(room));
        });

        router.route("/user/login").handler(BodyHandler.create());
        router.route("/user/login").handler(routingContext -> {
            String userId = routingContext.request().getParam("user");
            HttpServerResponse response = routingContext.response();
            userService.startUserBot(userId,result -> {
                if (result.succeeded()){
                    response.end();
                }else{
                    response.setStatusCode(500);
                    response.end();
                }
            });
        });

        router.route("/user/logout").handler(BodyHandler.create());
        router.route("/user/logout").handler(routingContext -> {
            String userId = routingContext.request().getParam("user");
            HttpServerResponse response = routingContext.response();
            userService.stopUserBot(userId);
        });

        // Bind "/" to our hello message - so we are still compatible.
        router.route("/channel/direct/message").handler(BodyHandler.create());
        router.route("/channel/direct/message").handler(routingContext -> {
            ChatMessage chatMessage = Json.decodeValue(routingContext.getBodyAsString(),ChatMessage.class);
            userService.sendDirectChat(chatMessage.getFromUser(),chatMessage.getToUser(), routingContext.getBodyAsString());
            HttpServerResponse response = routingContext.response();
            response.end();
        });

        router.route("/channel/room/message").handler(BodyHandler.create());
        router.post("/channel/room/message").handler(routingContext -> {
            String roomName = routingContext.request().getParam("room");
            roomService.sendRoomChat(routingContext.getBodyAsString(),roomName);
            HttpServerResponse response = routingContext.response();
            response.end();
        });

        router.route("/channel/room/register").handler(BodyHandler.create());
        router.post("/channel/room/register").handler(routingContext -> {
            String roomName = routingContext.request().getParam("room");
            User user = Json.decodeValue(routingContext.getBodyAsString(), User.class);
            roomService.registerUserToRoom(roomName, user.getUuid());
            HttpServerResponse response = routingContext.response();
            response.end();
        });

        router.route("/channel/room/unregister").handler(BodyHandler.create());
        router.post("/channel/room/unregister").handler(routingContext -> {
            String roomName = routingContext.request().getParam("room");
            User user = Json.decodeValue(routingContext.getBodyAsString(), User.class);
            roomService.removeUserFromRoom(roomName, user.getUuid());
            HttpServerResponse response = routingContext.response();
            response.end();
        });

        vertx
        .createHttpServer()
        .requestHandler(router::accept)
        .listen(8080, result -> {
            if (result.succeeded()) {
                fut.complete();
            } else {
                fut.fail(result.cause());
            }
        });
    }
}
