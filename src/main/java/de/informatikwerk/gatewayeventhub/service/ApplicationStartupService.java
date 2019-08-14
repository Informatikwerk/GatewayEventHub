package de.informatikwerk.gatewayeventhub.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing the application startup.
 */
@Service
@Transactional
public class ApplicationStartupService {

    private final Logger log = LoggerFactory.getLogger(ApplicationStartupService.class);

    @Autowired
    ConfigElementService configElementService;

    public ApplicationStartupService() {

    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        log.debug("Initializing Application...");
        configElementService.init();
        log.debug("...configElementService init finished");
        log.debug("...Application Initialization done.");
    }

}
