/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

import java.util.LinkedHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author opus
 */
public class BaseService {

    //protected static final Logger log = Logger.getLogger(BaseService.class.getName());

    protected LinkedBlockingQueue<SMTraffic> smqueue;
    protected LinkedHashMap<Long, BaseDescriptor> descriptors;
    
    protected LinkedHashMap<String, MonitorIFrameInterface> iframes;
   
    public BaseService() {
    
         descriptors = new LinkedHashMap<>();
         iframes = new LinkedHashMap<>();
        
        // crie nova janela de report na área de saida
//        InputOutput io = IOProvider.getDefault().getIO("Report", false);
//        report_data=io.getOut();
//        report_error=io.getErr();
//        loghandler = new OutHandler(report_error, report_data);
        //log.addHandler(OutHandler.getInstance());
         
    }
    
    public void configService(BaseDescriptor bd){
        System.err.println("Error ! - Configuring base class ...");
        
    }
    
    public void processSignal (SMTraffic signal, BaseDescriptor bd){
        //System.err.println("Error ! - Processing on  base class ...");
        smqueue.add(signal);
    }
    
  
    /**
     * @return the descriptors
     */
    public LinkedHashMap<Long, BaseDescriptor> getDescriptors() {
        return descriptors;
    }

    
    
    public void addIFrame(String descriptor_uid, MonitorIFrameInterface _iframe){
        iframes.put(descriptor_uid, _iframe);
    }
    
    public void removeIFrame(String uid){
        iframes.remove(uid);
    }
    
    public MonitorIFrameInterface getIFrame(String uid){
        return iframes.get(uid);
    }
    
    public void UpdateUI (String mes, String bduid){
            
            
    }
        
    
    
}
