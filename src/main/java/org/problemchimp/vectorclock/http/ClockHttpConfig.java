package org.problemchimp.vectorclock.http;

import org.problemchimp.http.Endpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for message handlers. Provides 2 default handlers: 1. An
 * incoming handler that echos everything it receives to stdout 2. An outgoing
 * handler that sends everything to all other connected instances
 */
@Configuration
public class ClockHttpConfig {

    @Bean
    @ConditionalOnProperty(name = "org.problemchimp.handler", havingValue = "vectorClock", matchIfMissing = true)
    public Endpoint<?> clockEndpoint() {
	return new ClockEndpoint();
    }
}
