package de.informatikwerk.gatewayeventhub.web.websocket.server;

import de.informatikwerk.gatewayeventhub.config.ApplicationProperties;
import de.informatikwerk.gatewayeventhub.web.websocket.server.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Controller
public class ChatController {


    private final ApplicationProperties applicationProperties;

    private JedisPool jedisPool;

    public ChatController(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        if(this.jedisPool == null){
            this.jedisPool = new JedisPool(applicationProperties.buildPoolConfig(), applicationProperties.getJedisIp(), applicationProperties.getJedisPort());
        }
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
        Jedis jedis = jedisPool.getResource();
        jedis.set(message.getMessageId(), message.getAction().getData());
        jedis.expire(message.getMessageId(), applicationProperties.getJedisMsgExpire());
        jedis.close();


        return new Message("GatewayEventHub");
    }



}
