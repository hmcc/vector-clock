package org.problemchimp.vectorclock.handler;

import javax.jmdns.ServiceInfo;

import org.problemchimp.handler.OutgoingHandler;
import org.problemchimp.handler.OutgoingHandlerBase;
import org.problemchimp.jmdns.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * Implementation of {@link OutgoingHandler} that echoes messages to only the
 * specified client.
 */
public final class OutgoingVectorClockHandler extends OutgoingHandlerBase<VectorClockMessage> {

    private static final Logger logger = LoggerFactory.getLogger(OutgoingVectorClockHandler.class);

    private @Autowired ServiceRegistry registry;
    private @Autowired VectorClock vectorClock;

    protected void handleMessage(VectorClockMessage message) {
	message.setClock(vectorClock);
	if (StringUtils.isEmpty(message.getService())) {
	    logger.warn("Message " + message + " has no service: sending to all");
	    super.sendToAll(message);
	} else {
	    ServiceInfo service = registry.find(message.getService());
	    if (service == null) {
		logger.warn("Service " + message.getService() + " not found: sending to all");
		super.sendToAll(message);
	    } else {
		logger.debug("Sending " + message + " to " + service.getName());
		super.sendToService(service, message);
	    }
	}

    }
}
