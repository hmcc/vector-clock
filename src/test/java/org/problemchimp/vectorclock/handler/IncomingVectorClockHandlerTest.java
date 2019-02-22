package org.problemchimp.vectorclock.handler;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.jmdns.ServiceInfo;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.problemchimp.App;
import org.problemchimp.jmdns.JmDNSConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit tests for {@link IncomingVectorClockHandler}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { VectorClockHandlerConfig.class, JmDNSConfig.class })
@EnableConfigurationProperties
public class IncomingVectorClockHandlerTest {

    @Autowired private IncomingVectorClockHandler handler;
    @Autowired private ServiceInfo thisService;
    @Autowired private VectorClock vectorClock;

    @BeforeClass
    public static void setUpClass() {
	System.setProperty("service", "Alice");
	System.setProperty(App.PORT_PROPERTY, Integer.toString(App.DEFAULT_PORT));
    }

    @Before
    public void setUp() {
	vectorClock.clear();
    }

    /**
     * If the incoming message has no information, assume it is for us and
     * increment our own vector clock by 1.
     */
    @Test
    public void testHandleMessage_blank() {
	handler.handleMessage(new VectorClockMessage());
	
	assertEquals(1, vectorClock.size());
	assertEquals(1, vectorClock.get(thisService.getName()).intValue());
    }
    
    /**
     * If the incoming message is for another service, ignore it.
     */
    @Test
    public void testHandleMessage_wrongService() {
	VectorClockMessage message = new VectorClockMessage();
	message.setService("wrong service");
	handler.handleMessage(message);
	assertEquals(0, vectorClock.size());
    }

    @Test
    public void testHandleMessage_mergeClock_noConflict() {
	VectorClockMessage message = new VectorClockMessage();
	VectorClock clock = new VectorClock();
	clock.put("Alice", 2);
	clock.put("Bob", 1);
	clock.put("Charlie", 3);
	message.setClock(clock);

	handler.handleMessage(message);
	assertEquals(4, vectorClock.size());
	assertEquals(1, vectorClock.get(thisService.getName()).intValue());
	assertEquals(2, vectorClock.get("Alice").intValue());
	assertEquals(1, vectorClock.get("Bob").intValue());
	assertEquals(3, vectorClock.get("Charlie").intValue());
    }

    @Test
    public void testHandleMessage_mergeClock_conflict() {
	// Set up 2 clocks with conflicting values
	VectorClock clock = new VectorClock();
	clock.put("Alice", 2);
	clock.put("Bob", 1);
	clock.put("Charlie", 3);

	vectorClock.put("Alice", 1);
	vectorClock.put("Bob", 2);
	vectorClock.put("Dave", 4);
	
	VectorClockMessage message = new VectorClockMessage();
	message.setClock(clock);

	handler.handleMessage(message);
	assertEquals(5, vectorClock.size());
	assertEquals(1, vectorClock.get(thisService.getName()).intValue());
	assertEquals(2, vectorClock.get("Alice").intValue());
	assertEquals(2, vectorClock.get("Bob").intValue());
	assertEquals(3, vectorClock.get("Charlie").intValue());
	assertEquals(4, vectorClock.get("Dave").intValue());
    }

    @Test
    public void testHandleMessage_updateService_new() {
	// Send a message updating this service by 5
	VectorClockMessage message = new VectorClockMessage();
	message.setService(thisService.getName());
	message.setAmount(5);

	handler.handleMessage(message);
	assertEquals(1, vectorClock.size());
	assertEquals(5, vectorClock.get(thisService.getName()).intValue());
    }

    @Test
    public void testHandleMessage_updateService_existing() {
	// Set this service to 1
	vectorClock.put(thisService.getName(), 1);

	// Send a message updating this service by 5
	VectorClockMessage message = new VectorClockMessage();
	message.setService(thisService.getName());
	message.setAmount(5);

	handler.handleMessage(message);
	assertEquals(1, vectorClock.size());
	assertEquals(6, vectorClock.get(thisService.getName()).intValue());
    }

    @Test
    public void testHandleMessage_updateService_noAmount() {
	// Send a message updating this service by an unspecified amount
	VectorClockMessage message = new VectorClockMessage();
	message.setService(thisService.getName());
	handler.handleMessage(message);

	assertEquals(1, vectorClock.size());
	assertEquals(1, vectorClock.get(thisService.getName()).intValue());
    }

    @Test
    public void testHandleMessage_mergeAndUpdate() {
	// Set up our clock and an incoming clock with different values
	VectorClock clock = new VectorClock();
	clock.put(thisService.getName(), 2);
	vectorClock.put(thisService.getName(), 1);

	// Also update our clock by 5
	VectorClockMessage message = new VectorClockMessage();
	message.setService(thisService.getName());
	message.setAmount(5);
	message.setClock(clock);

	handler.handleMessage(message);
	assertEquals(1, vectorClock.size());
	assertEquals(7, vectorClock.get(thisService.getName()).intValue());
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
	System.clearProperty(App.PORT_PROPERTY);
    }
}
