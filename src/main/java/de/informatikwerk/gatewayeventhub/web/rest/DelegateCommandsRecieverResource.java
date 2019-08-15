package de.informatikwerk.gatewayeventhub.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.informatikwerk.gatewayeventhub.config.ApplicationProperties;
import de.informatikwerk.gatewayeventhub.domain.DelegateCommand;
import de.informatikwerk.gatewayeventhub.domain.Gateways;
import de.informatikwerk.gatewayeventhub.domain.Realmkeys;
import de.informatikwerk.gatewayeventhub.repository.GatewaysRepository;
import de.informatikwerk.gatewayeventhub.repository.RealmkeysRepository;
import de.informatikwerk.gatewayeventhub.service.SyncHttpsRequestThreadRegistry;
import de.informatikwerk.gatewayeventhub.service.SyncResponseObserver;
import de.informatikwerk.gatewayeventhub.web.rest.util.HeaderUtil;
import de.informatikwerk.gatewayeventhub.web.websocket.server.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing Recived messages.
 */
@RestController
@RequestMapping("/api")
public class DelegateCommandsRecieverResource {

    private final Logger log = LoggerFactory.getLogger(DelegateCommandsRecieverResource.class);

    private static final String ENTITY_NAME = "RecivedMessage";

    @Autowired
    private SimpMessagingTemplate template;

    private final GatewaysRepository gatewaysRepository;

    private final RealmkeysRepository realmkeysRepository;

    private final ApplicationProperties applicationProperties;

    public DelegateCommandsRecieverResource(GatewaysRepository gatewaysRepository,
            RealmkeysRepository realmkeysRepository, ApplicationProperties applicationProperties) {
        this.gatewaysRepository = gatewaysRepository;
        this.realmkeysRepository = realmkeysRepository;
        this.applicationProperties = applicationProperties;
    }

    /**
     * POST /delegateCommand : Passing command to langateway.
     *
     * @param delegateCommand the delegateCommand to pass.
     * @return the ResponseEntity TODO description
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/delegateCommand")
    @Timed
    public ResponseEntity<DelegateCommand> passDelegateCommandToLanGateway(
            @Valid @RequestBody DelegateCommand delegateCommand) throws URISyntaxException {
        log.debug("REST pass delegateCommand to correct LanGateway : {}", delegateCommand);
        SyncResponseObserver syncResponseObserver = new SyncResponseObserver(3000);

        Message msg = getMessageTemplateForDelegateCommand(delegateCommand);
        String uniqId = getWebsocketUniqIdForDelegateCommand(delegateCommand);
        if (uniqId == null) {
            return ResponseEntity.notFound().build();
        }
        this.template.convertAndSend("/doors/delegateCommands/" + uniqId, msg);
        SyncHttpsRequestThreadRegistry syncHttpsRequestThreadRegistry = SyncHttpsRequestThreadRegistry.instance();
        syncHttpsRequestThreadRegistry.put(msg.getMessageId(), syncResponseObserver);
        syncResponseObserver.waitForResponse();
        Message message = syncResponseObserver.getMessage();
        if (message == null) {
            return ResponseEntity.ok().body(null);
        }

        return ResponseEntity.ok().body(message.getDelegateCommand());
    }

    public String getWebsocketUniqIdForDelegateCommand(DelegateCommand delegateCommand) {
        Realmkeys realmkeys = new Realmkeys();
        String uniqId = null;
        realmkeys.setRealmkey(delegateCommand.getRealmKey());
        List<Realmkeys> matchingRealmkeys = realmkeysRepository.findAll(Example.of(realmkeys));
        if(matchingRealmkeys != null && matchingRealmkeys.size() > 0){
            if(matchingRealmkeys.size() > 1) log.warn("Found multiple realmkeys [" + delegateCommand.getRealmKey() + "]!");
            Realmkeys matchingRealmkey = matchingRealmkeys.get(0);
            if (matchingRealmkey != null) {
                Gateways gateways = gatewaysRepository.findOne(matchingRealmkey.getGateways().getId());
                if (gateways != null) {
                    uniqId = gateways.getWebsocketId();
                    return uniqId;
                }
            }
        }
        log.error("No matching realmkeys for Realmkey =[ " + delegateCommand.getRealmKey() + " ] in our database.");
        return null;
    }

    public Message getMessageTemplateForDelegateCommand(DelegateCommand delegateCommand) {
        Message msg = new Message();
        msg.setAuthor("GatewayEventHub");
        msg.setDelegateCommand(delegateCommand);
        return msg;
    }

}
