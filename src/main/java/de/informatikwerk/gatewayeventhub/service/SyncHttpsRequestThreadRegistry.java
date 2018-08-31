package de.informatikwerk.gatewayeventhub.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SyncHttpsRequestThreadRegistry {

    private static SyncHttpsRequestThreadRegistry instance;

    private Map<String, SyncResponseObserver> syncRequestThreadMap;

    private static final Logger logger = LoggerFactory.getLogger(SyncHttpsRequestThreadRegistry.class);

    private SyncHttpsRequestThreadRegistry() {
    }

    public static SyncHttpsRequestThreadRegistry instance() {
        if (instance == null) {
            instance = new SyncHttpsRequestThreadRegistry();
            instance.syncRequestThreadMap = new HashMap<>();
        }
        return instance;
    }

    public synchronized SyncResponseObserver put(String key, SyncResponseObserver value) {
        logger.debug("Thread " + Thread.currentThread().getId() + " is putting Thread " + key
            + " into SyncHttpsRequestThreadRegistry.");
        return syncRequestThreadMap.put(key, value);
    }

    public synchronized SyncResponseObserver remove(String key) {
        SyncResponseObserver remove = syncRequestThreadMap.remove(key);
        if (remove != null) {
            logger.debug("Thread " + Thread.currentThread().getId() + " is removing Thread " + key
                + " from SyncHttpsRequestThreadRegistry.");
        }
        return remove;
    }

}
