package de.informatikwerk.gatewayeventhub.web.websocket.server;

import de.informatikwerk.gatewayeventhub.config.ApplicationProperties;
import de.informatikwerk.gatewayeventhub.web.websocket.server.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.TimeUnit;

@Controller
public class ChatController {


    private final ApplicationProperties applicationProperties;

    public ChatController(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @MessageMapping("/actions/register")
    @SendTo("/doors/messages")
    public Message send(final Message message) throws Exception {
        System.out.println(" ======= ***************** This langateway want to register **************** ========");
        System.out.println(" ======= *****************" + message.getAuthor() + "**************** ========");

        return new Message("GatewayEventHub");
    }

    @MessageMapping("/actions/responses")
    public Message responses(final Message message) throws Exception {
        System.out.println(" ======= ***************** This langateway want to response **************** ========");
        System.out.println(" ======= *****************" + message.getAuthor() + "**************** ========");
        System.out.println(" ======= *****************" + message.getAction().getData() + "**************** ========");
        JedisPool jedisPool = new JedisPool(applicationProperties.getJedisIp(), applicationProperties.getJedisPort());
        Jedis jedis = jedisPool.getResource();
        jedis.set(message.getMessageId(), message.getAction().getData());
        jedis.expire(message.getMessageId(), applicationProperties.getJedisMsgExpire());


        return new Message("GatewayEventHub");
    }



}
