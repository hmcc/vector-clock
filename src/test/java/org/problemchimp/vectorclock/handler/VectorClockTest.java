package org.problemchimp.vectorclock.handler;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link VectorClock}.
 */
public class VectorClockTest {

    private VectorClock vectorClock;
    
    @Before
    public void setUp() {
	vectorClock = new VectorClock();
	vectorClock.put("Alice", 1);
	vectorClock.put("Bob", 2);
    }
    
    private void checkExisting() {
	assertEquals(1, vectorClock.get("Alice").intValue());
	assertEquals(2, vectorClock.get("Bob").intValue());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testIncrementBy1_null() {
	vectorClock.increment(null);
    }
    
    @Test
    public void testIncrementBy1_new() {
	String key = "Charlie";
	vectorClock.increment(key);
	assertEquals(1, vectorClock.get(key).intValue());
	checkExisting();
    }
    
    @Test
    public void testIncrementBy1_existing() {
	String key = "Charlie";
	vectorClock.put(key, 5);
	vectorClock.increment(key);
	assertEquals(6, vectorClock.get(key).intValue());
	checkExisting();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testIncrementByMany_null() {
	vectorClock.increment(null, 5);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testIncrementByMany_negative() {
	vectorClock.increment("Charlie", -5);
    }
    
    @Test
    public void testIncrementByMany_new() {
	String key = "Charlie";
	vectorClock.increment(key);
	assertEquals(1, vectorClock.get(key).intValue());
	checkExisting();
    }
    
    @Test
    public void testIncrementByMany_existing() {
	String key = "Charlie";
	vectorClock.put(key, 5);
	vectorClock.increment(key);
	assertEquals(6, vectorClock.get(key).intValue());
	checkExisting();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testMerge_blank() {
	Map<String, Integer> other = new HashMap<>();
	other.put("", 5);
	vectorClock.merge(other);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testMerge_null() {
	Map<String, Integer> other = new HashMap<>();
	other.put("Charlie", null);
	vectorClock.merge(other);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testMerge_negative() {
	Map<String, Integer> other = new HashMap<>();
	other.put("Charlie", -1);
	vectorClock.merge(other);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testMerge_zero() {
	Map<String, Integer> other = new HashMap<>();
	other.put("Charlie", 0);
	vectorClock.merge(other);
    }
    
    @Test
    public void testMerge_noConflict() {
	String key = "Charlie";
	Map<String, Integer> other = new HashMap<>();
	other.put(key, 3);
	vectorClock.merge(other);
	assertEquals(other.get(key), vectorClock.get(key));
	checkExisting();
    }
    
    @Test
    public void testMerge_conflict() {
	Map<String, Integer> other = new HashMap<>();
	other.put("Alice", 2);
	other.put("Bob", 1);
	vectorClock.merge(other);
	assertEquals(2, vectorClock.get("Alice").intValue());
	assertEquals(2, vectorClock.get("Bob").intValue());
    }
}
