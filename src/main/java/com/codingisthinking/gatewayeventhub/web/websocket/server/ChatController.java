package com.codingisthinking.gatewayeventhub.web.websocket.server;

import com.codingisthinking.gatewayeventhub.web.websocket.server.model.Message;
import com.codingisthinking.gatewayeventhub.web.websocket.server.model.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate template;


    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ResponseMessage send(final Message message) throws Exception {
        System.out.println(message.getFrom() + ": "+message.getText());
        return new ResponseMessage(message.getText());
    }

//    @Scheduled(fixedRate = 1000)
    public void fireGreeting() {
        System.out.println("Fire");
        this.template.convertAndSend("/topic/messages", new ResponseMessage("Fire"));
    }
}
