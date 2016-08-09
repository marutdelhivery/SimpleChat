package com.marut.chat.server;

import com.hazelcast.config.Config;
import com.marut.chat.broadcast.MessageBroadcaster;
import com.marut.chat.persistence.UserService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.cli.Argument;
import io.vertx.core.cli.CLI;
import io.vertx.core.cli.CommandLine;
import io.vertx.core.cli.Option;
import io.vertx.core.json.JsonArray;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;
import java.util.List;

/**
 * Created by marutsingh on 7/15/16.
 */
@SpringBootApplication
@ComponentScan("com.marut")
public class ChatApplication {

    public static Vertx vertx;
    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext(){return applicationContext;}

    public static LocalMap<String,String> getUserBotsMap(){
        return ChatApplication.vertx.sharedData().getLocalMap("user-bots");
    }

    public static LocalMap<String,String> getRoomBotsMap(){
        return ChatApplication.vertx.sharedData().getLocalMap("room-bots");
    }

    public static LocalMap<String,Boolean> getRoomsMap(){
        return ChatApplication.vertx.sharedData().getLocalMap("rooms");
    }

    public static void main(String[] args){

        applicationContext =
                SpringApplication.run(ChatApplication.class, args);

        Option cmdoptions = new Option();

        CLI cli = CLI.create("copy")
                .setSummary("A command line interface to copy files.")
                .addOption(new Option()
                        .setLongName("Mode")
                        .setShortName("M")
                        .setDescription("Runs the application in master or child mode")
                        .setFlag(true))
                .addArgument(new Argument()
                        .setIndex(0)
                        .setDescription("The Mode")
                        .setArgName("mode"))
             ;

        StringBuilder builder = new StringBuilder();
        cli.usage(builder);

//        CommandLine commandLine = cli.parse(Arrays.asList(args));
        String mode = "master";//commandLine.getArgumentValue(0);
        Config hazelcastConfig = new Config();
        hazelcastConfig.getNetworkConfig().getJoin().getTcpIpConfig().addMember("127.0.0.1").setEnabled(true);
        hazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);

//        AwsConfig awsConfig = new AwsConfig();
//        awsConfig.setAccessKey();
//        awsConfig.setSecretKey();
//        awsConfig.setRegion("us-west-2");
//        awsConfig.setHostHeader("ec2.amazonaws.com");
//        awsConfig.setSecurityGroupName("hazelcast-sg");
//        awsConfig.setTagKey("type");
//        awsConfig.setTagValue("hz-nodes");
//        hazelcastConfig.getNetworkConfig().getJoin().setAwsConfig(awsConfig);


        ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);
        VertxOptions options = new VertxOptions().setClusterManager(mgr);
        Vertx.clusteredVertx(options, res -> {
            if (res.succeeded()) {
                vertx = res.result();
                applicationContext.getBean(UserService.class).init();
                if (mode.equals("master")){
                    ChatHTTPServer chatHTTPServer = applicationContext.getBean(ChatHTTPServer.class);
                    vertx.deployVerticle(chatHTTPServer);
                }
               //vertx.deployVerticle(new MessageBroadcaster());
            }
        });
    }

    public static void deployVerticles(ApplicationContext applicationContext){
        applicationContext
                .getBeansWithAnnotation(com.marut.chat.annotation.ChatVerticle.class)
                .values().stream().forEach(veticle -> vertx.deployVerticle((AbstractVerticle)veticle));
    }
}
