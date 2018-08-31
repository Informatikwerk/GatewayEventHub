package de.informatikwerk.gatewayeventhub.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.Serializable;

import de.informatikwerk.gatewayeventhub.web.websocket.server.model.Message;

/***
 * @author Sven Gabel
 */
public class SyncResponseObserver implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The logger for this class. */
    private static final Logger logger = LoggerFactory.getLogger(SyncResponseObserver.class);

    /**
     * The time stamp when the request started. This is required to calculate the timeout.
     */
    private long requestStart;

    /**
     * The response from the langateway to pass back to the SuperSlimClient.
     */
    private Message message;

    private long receiveTimeout;


    public SyncResponseObserver(long receiveTimeout) {
        requestStart = System.currentTimeMillis();
        this.receiveTimeout = receiveTimeout;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message aspmlMessageResponse) {
        synchronized (this) {
            this.message = aspmlMessageResponse;
            this.notify();
        }
    }


    /**
     * Get the time in milliseconds until the timeout of the request is reached. This is calculated
     * based on the request start time and the
     *
     * @return the time in milliseconds until the sync request should time out.
     */
    public long getTimeoutWait() {
        return receiveTimeout;
    }

    /**
     * Implementation of the waiting for a response on this observer. This call will return after
     * one of the 3 following conditions are true:
     * <p>
     * <ol>
     * <li>{#setSendSyncDataResponse(SendSyncDataResponse)} is called with a valid response or
     * was called before.</li>
     * <li>{#setStcdsException(StcdsException)} is called with an exception or was called
     * before.</li>
     * <li>the timeout as calculated with {@link #getTimeoutWait()} is 0 or less.</li>
     * </ol>
     */
    public void waitForResponse() {

        synchronized (this) {
            long waitTime = getTimeoutWait();
            // Only wait if no response received yet and there is still a timeout
            if (message == null && waitTime > 0) {
                logger.debug("Waiting "+waitTime);
                try {
                    this.wait(waitTime);
                } catch (InterruptedException e) {
                    // Drop the interrupted exception
                    logger.debug("Observer interrupted: [{0}]", e.toString());
                }
            }
        }
    }
}
