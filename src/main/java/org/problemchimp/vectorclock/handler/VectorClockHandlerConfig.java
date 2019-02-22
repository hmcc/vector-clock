package org.problemchimp.vectorclock.handler;

import org.problemchimp.handler.IncomingHandler;
import org.problemchimp.handler.OutgoingHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for message handlers.
 * Provides 2 handlers:
 * 1. An incoming handler that does vector clock things
 * 2. An outgoing handler that sends only to the instance given in the message
 */
@Configuration
public class VectorClockHandlerConfig {
    
    @Bean
    @ConditionalOnProperty(name = "org.problemchimp.handler.incoming", havingValue = "vectorClock", matchIfMissing = true)
    public IncomingHandler<?> incomingHandler() {
	return new IncomingVectorClockHandler();
    }

    @Bean
    @ConditionalOnProperty(name = "org.problemchimp.handler.outgoing", havingValue = "vectorClock", matchIfMissing = true)
    public OutgoingHandler<?> outgoingHandler() {
	return new OutgoingVectorClockHandler();
    }
    
    @Bean
    public VectorClock vectorClock() {
	return new VectorClock();
    }
}
