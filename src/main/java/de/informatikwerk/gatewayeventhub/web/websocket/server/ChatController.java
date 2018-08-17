package de.informatikwerk.gatewayeventhub.web.websocket.server;

import de.informatikwerk.gatewayeventhub.web.websocket.server.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {


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

        return new Message("GatewayEventHub");
    }



}
