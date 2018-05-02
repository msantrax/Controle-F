/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

import java.awt.Color;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author opus
 */
public interface MonitorIFrameInterface {
    
    
    static final Color LED_GREEN_OFF = new Color (0,150,50);
    static final Color LED_GREEN_ON = new Color (100,255,100);
    static final Color LED_YELLOW = new Color (255,255,100);
    static final Color LED_RED = new Color (255,100,100);
    
    
    
   
    void setDescriptor(BaseDescriptor bd);
    void setService(BaseService _service);
    
    BaseDescriptor getDescriptor();
    BaseService getService();
    
    String getIframeid();
    void setIframeid(String iframeid);
    
    void updateUI(Color ledcolor, String  message);
    
    
    
    
}
