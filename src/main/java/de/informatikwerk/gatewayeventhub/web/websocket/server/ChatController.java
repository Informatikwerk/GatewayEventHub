package de.informatikwerk.gatewayeventhub.web.websocket.server;

import de.informatikwerk.gatewayeventhub.config.ApplicationProperties;
import de.informatikwerk.gatewayeventhub.service.SyncHttpsRequestThreadRegistry;
import de.informatikwerk.gatewayeventhub.service.SyncResponseObserver;
import de.informatikwerk.gatewayeventhub.web.websocket.server.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {


    private final ApplicationProperties applicationProperties;


    public ChatController(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @MessageMapping("/delegateCommands/register")
    @SendTo("/doors/messages")
    public Message send(final Message message) throws Exception {
        System.out.println(" ======= ***************** This langateway wants to register **************** ========");
        System.out.println(" ======= *****************" + message.getAuthor() + "**************** ========");

        return new Message("GatewayEventHub");
    }

    @MessageMapping("/delegateCommands/responses")
    public Message responses(final Message message) throws Exception {
        SyncResponseObserver syncResponseObserver = SyncHttpsRequestThreadRegistry.instance().remove(message.getMessageId());
        syncResponseObserver.setMessage(message);


        return new Message("GatewayEventHub");
    }



}
