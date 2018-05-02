/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

import java.util.LinkedHashMap;

/**
 *
 * @author opus
 */
public interface ContextEventListener {
  
    public void addContextEventListener(ContextEventListener listener);
    
    public void removeContextEventListener(ContextEventListener listener);
    
    public void notifyEvent(long uid, SMTraffic message);
    
    public void eventGate(SMTraffic message);
    
    
    
    public Long getContext();
    
    public Long getUID();
    
    public String getNodetype();
    
    public String getVersion();
    
    
    
}
