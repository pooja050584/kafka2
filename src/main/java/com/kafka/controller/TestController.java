package com.kafka.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController {

    @RequestMapping("/welcome")
    public ModelAndView firstPage() {
        System.out.println("here");
        RedisClient redisClient = RedisClient.create(RedisURI.create("redis://redis-17932.c9.us-east-1-4.ec2.cloud.redislabs.com:17932")); // change to reflect your environment
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisCommands<String, String> syncCommands = connection.sync();

        Map<String, String> messageBody = new HashMap<>();
        messageBody.put( "speed", "15" );
        messageBody.put( "direction", "271" );
        messageBody.put( "sensor_ts", String.valueOf(System.currentTimeMillis()) );

        String messageId = syncCommands.xadd(
                "weather_sensor:wind",
                messageBody);

        System.out.println( String.format("Message %s : %s posted", messageId, messageBody) );

        connection.close();
        redisClient.shutdown();
        return new ModelAndView("welcome");
    }

}