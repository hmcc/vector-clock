package org.problemchimp.vectorclock.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.StringUtils;

public class VectorClock extends ConcurrentHashMap<String, Integer> {

    private static final long serialVersionUID = 4116957704535786400L;

    public void increment(String target) {
	increment(target, 1);
    }

    public void increment(String target, int amount) {
	if (StringUtils.isEmpty(target)) {
	    throw new IllegalArgumentException("Target is required");
	}
	if (amount <= 0) {
	    throw new IllegalArgumentException("Amount must be 1 or more");
	}
	if (containsKey(target)) {
	    put(target, get(target) + amount);
	} else {
	    put(target, amount);
	}
    }

    public void merge(Map<String, Integer> other) {
	if (other == null) {
	    return;
	}
	for (Map.Entry<String, Integer> entry : other.entrySet()) {
	    if (containsKey(entry.getKey())) {
		int currentValue = get(entry.getKey());
		put(entry.getKey(), Math.max(currentValue, entry.getValue()));
	    } else {
		put(entry.getKey(), entry.getValue());
	    }
	}
    }
    
    @Override
    public Integer put(String key, Integer value) {
	if (StringUtils.isEmpty(key)) {
	    throw new IllegalArgumentException("Key is required");
	}
	if (value == null || value <= 0) {
	    throw new IllegalArgumentException("Value must be 1 or more");
	}
	return super.put(key, value);
    }
}
