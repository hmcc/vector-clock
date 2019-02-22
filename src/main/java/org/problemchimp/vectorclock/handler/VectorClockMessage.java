package org.problemchimp.vectorclock.handler;

import java.util.Map;

/**
 * JSON message format to exchange.
 */
public class VectorClockMessage {
    
    private Integer amount;
    private Map<String, Integer> clock;
    private String service;
    
    public Integer getAmount() {
        return amount;
    }
    
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    
    public Map<String, Integer> getClock() {
        return clock;
    }
    
    public void setClock(Map<String, Integer> clock) {
        this.clock = clock;
    }
    
    public String getService() {
        return service;
    }
    
    public void setService(String service) {
        this.service = service;
    }
}