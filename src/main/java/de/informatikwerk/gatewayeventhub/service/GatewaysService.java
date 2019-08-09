package de.informatikwerk.gatewayeventhub.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import de.informatikwerk.gatewayeventhub.domain.Gateways;

/**
 * Service class for managing gateways.
 */
@Service
@Transactional
public class GatewaysService {

    private final Logger log = LoggerFactory.getLogger(GatewaysService.class);

    @Autowired
    private EntityManager em;

    public GatewaysService() {
    }

    /**
     * 
     * @param websocketId The websocketId of the gateway
     * @return the gateway
     */
    public Gateways findByWebsocketId(String websocketId) {
        Gateways gateway = null;

        List<Gateways> gateways = null;

        Query q = em.createNamedQuery(Gateways.QUERY_FIND_BY_WEBSOCKETID);
        q.setParameter(Gateways.PARAM_WEBSOCKET_ID, websocketId);
        gateways = q.getResultList();

        if(gateways.size() > 0){
            gateway = gateways.get(0);
        }

        return gateway;
    }

}
