package de.informatikwerk.gatewayeventhub.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import de.informatikwerk.gatewayeventhub.domain.Realmkeys;

/**
 * Service class for managing gateways.
 */
@Service
@Transactional
public class RealmkeysService {

    private final Logger log = LoggerFactory.getLogger(RealmkeysService.class);

    @Autowired
    private EntityManager em;

    public RealmkeysService() {
    }

    /**
     * 
     * @param gatewayId The gatewayId of the realmkey
     * @return the list of realmkeys
     * 
     */
    public List<Realmkeys> findByGatewayId(String gatewayId) {
        List<Realmkeys> realmkeys = null;

        Query q = em.createNamedQuery(Realmkeys.QUERY_FIND_BY_GATEWAYID);
        q.setParameter(Realmkeys.PARAM_GATEWAY_ID, gatewayId);
        realmkeys = q.getResultList();

        return realmkeys;
    }

}
