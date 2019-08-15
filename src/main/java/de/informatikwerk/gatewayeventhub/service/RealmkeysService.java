package de.informatikwerk.gatewayeventhub.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.informatikwerk.gatewayeventhub.domain.Realmkeys;
import de.informatikwerk.gatewayeventhub.repository.RealmkeysRepository;

/**
 * Service class for managing gateways.
 */
@Service
@Transactional
public class RealmkeysService {

    private final Logger log = LoggerFactory.getLogger(RealmkeysService.class);

    @Autowired
    private EntityManager em;

    private final RealmkeysRepository realmkeysRepository;

    public RealmkeysService(RealmkeysRepository realmkeysRepository) {
        this.realmkeysRepository = realmkeysRepository;
    }

    /**
     * Save a realmkey.
     *
     * @param realmkey the entity to save
     * @return the persisted entity
     */
    public Realmkeys save(Realmkeys realmkey) {
        log.debug("Request to save Realmkey : {}", realmkey);

        List<Realmkeys> realmkeysWithRealmkey = findByRealmkey(realmkey.getRealmkey());
        if(realmkey.getId() != null && realmkeysWithRealmkey.size() > 1 || realmkey.getId() == null && realmkeysWithRealmkey.size() > 0){
            Realmkeys realmkeyToDelete = realmkeysWithRealmkey.get(0);
            if(realmkeyToDelete.getId() == realmkey.getId()){
                realmkeyToDelete = realmkeysWithRealmkey.get(1);
            }
            if(realmkeyToDelete != null) realmkeysRepository.delete(realmkeyToDelete.getId());
        }

        return realmkeysRepository.save(realmkey);
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

    /**
     * 
     * @param realmkey The realmkey of the realmkey
     * @return the list of realmkeys
     * 
     */
    public List<Realmkeys> findByRealmkey(String realmkey) {
        List<Realmkeys> realmkeys = null;

        Query q = em.createNamedQuery(Realmkeys.QUERY_FIND_BY_REALMKEY);
        q.setParameter(Realmkeys.PARAM_REALMKEY, realmkey);
        realmkeys = q.getResultList();

        return realmkeys;
    }

}
