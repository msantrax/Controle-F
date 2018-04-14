/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author opus
 */
public class BaseService implements SignalListener{

    //protected static final Logger log = Logger.getLogger(BaseService.class.getName());

    protected LinkedBlockingQueue<SMTraffic> smqueue;
    
//    protected PrintWriter report_data;
//    protected PrintWriter report_error;
//    public OutHandler loghandler;
    
    
    public BaseService() {
    
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
    
    
    // ===========SIGNAL HANDLING ===================================================================
        
    /** Estrutura para armazenamento dos listeners do dispositivo*/ 
    private ArrayList<SignalListener> listeners = new ArrayList<>();
   
    private long context;
     
    @Override
    public long getContext() { return context;}
    
    public void processSignal (SMTraffic signal){
        smqueue.add(signal);
    }
    
     /** Método de registro do listener do dispositivo serial */
    public void addSignalListener (SignalListener l){
        listeners.add(l);
    }

    /** Método de remoção do registro do listener do dispositivo serial */
    public void removeSignalListener (SignalListener l){
        listeners.remove(l);
    }

    /** Esse método é chamedo quando algo acontece no dispositivo */
    protected void notify(long context, SMTraffic signal) {

        if (!listeners.isEmpty()){      
            // Rode entre os listeners
            for (Iterator<SignalListener> i=listeners.iterator(); i.hasNext();){
                SignalListener l =  i.next();
                if ((context == 0) || (l.getContext() == context) ){
                    l.processSignal(signal); //Notifique cada listener
                }
                
            }
        }
    }
    
    
    
}
