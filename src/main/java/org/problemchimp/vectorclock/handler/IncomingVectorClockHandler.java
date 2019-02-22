package org.problemchimp.vectorclock.handler;

import javax.jmdns.ServiceInfo;

import org.problemchimp.handler.IncomingHandler;
import org.problemchimp.handler.IncomingHandlerBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation of {@link IncomingHandler} that adds the provided information
 * to the vector clock.
 */
public final class IncomingVectorClockHandler extends IncomingHandlerBase<VectorClockMessage> {

    private static final Logger logger = LoggerFactory.getLogger(IncomingVectorClockHandler.class);

    @Autowired private ServiceInfo thisService;
    @Autowired private VectorClock vectorClock;

    /**
     * When a message is received:
     * <ol>
     * <li>Update each element in the clock to max(local, received)</li>
     * <li>If the message includes an amount, increment our clock by that
     * amount, otherwise increment our clock by 1.</li>
     * </ol>
     */
    protected void handleMessage(VectorClockMessage message) {
	logger.info("Incoming message " + message);
	if (message.getClock() == null) {
	    logger.warn("Incoming message " + message + " has no clock; not merging");
	} else {
	    logger.debug("Merging clock " + message.getClock());
	    vectorClock.merge(message.getClock());
	}
	if (!(message.getService() == null || message.getService().equals(thisService.getName()))) {
	    logger.warn("Incoming message " + message + " is not for this service; not incrementing any clock");
	} else {
	    if (message.getAmount() == null || message.getAmount() <= 1) {
		logger.debug("Incrementing " + message.getService() + " by 1");
		vectorClock.increment(thisService.getName());
	    } else {
		logger.debug("Incrementing " + message.getService() + " by " + message.getAmount());
		vectorClock.increment(thisService.getName(), message.getAmount());
	    }
	}
    }
}
