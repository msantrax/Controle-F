/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.filewriter;

import com.virna5.fileobserver.*;
import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.BaseService;
import com.virna5.contexto.OutHandler;
import com.virna5.contexto.SMTraffic;
import com.virna5.contexto.VirnaServices;
import static com.virna5.contexto.VirnaServices.STATES.FOB_DOSCAN;
import static com.virna5.contexto.VirnaServices.STATES.FOB_STOPSCAN;
import static com.virna5.contexto.VirnaServices.STATES.IDLE;
import static com.virna5.contexto.VirnaServices.STATES.INIT;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileWriterService extends BaseService {

    private static final Logger log = Logger.getLogger(FileWriterService.class.getName());
    
    private static FileWriterService instance;    
   
    private FileWriterService.SMThread service_thread;
    
    private LinkedHashMap<Long, FileObserverDescriptor> descriptors;
    
    
    public static FileWriterService getInstance(){
        if (instance == null) {instance = new FileWriterService();}
        return instance;
    }

    public FileWriterService() {
  
        super();
        log.addHandler(OutHandler.getInstance());
        descriptors = new LinkedHashMap<>();
        instance = this;      
        
    }

    public FileWriterService getDefault() { return instance; }
  
    @Override
    public void configService(BaseDescriptor bd){
        
        FileObserverDescriptor fod = (FileObserverDescriptor) bd;
        Long uid = fod.getContext();
        if (descriptors.containsKey(uid)){
            descriptors.remove(uid);
        }
        descriptors.put(uid, fod);
        log.info(String.format("Configuring FileObserverclass %s to context : %s", bd.toString(),uid));
    }
    
    
    
    public void visitFile(FileObserverDescriptor lfod, long ct){
          
//        log.log(Level.FINE, "Observer Cicle called " 
//                    + lfod.getInputfile_path().getFileName()
//                    + "    @ " + dateFormat.format(new Date())
//                    + "    gap : " + (ct - lfod.getLastseen()));
//            lfod.setLastseen(ct);
//            lfod.setTimespot(ct + lfod.getInterval());
        
    }
    
    
    
    
    // ================================================================================================================
    private void stopService(){
        //services.removeUsbServicesListener(this);
        service_thread.setDone(true);    
    }
    
    private void startService(){      
        smqueue.clear();
        //services.addUsbServicesListener(this);
        new Thread(service_thread).start();
    }
    
    private class SMThread extends Thread {
    
        private VirnaServices.STATES state;
        private boolean done;
        protected BlockingQueue<SMTraffic> tqueue;
        private VirnaServices.CMDS cmd;
        private ArrayDeque <VirnaServices.STATES>states_stack;
        private SMTraffic smm;
        
        protected ArrayList<FileObserverDescriptor> observers;
        protected boolean doscan = false;
        

        public SMThread(BlockingQueue<SMTraffic> tqueue) {
            this.tqueue = tqueue;
            states_stack = new ArrayDeque<>();
            states_stack.push(VirnaServices.STATES.RESET);
            setDone(false);
            observers = new ArrayList<>();
        }
        
        
        public void addObserver(FileObserverDescriptor fod){
            observers.add(fod);
        }
        
        
        @Override
        public void run(){
   
            //log.log(Level.FINE, "Iniciando Thread de Serviços principal");
            states_stack.clear();
            states_stack.push(VirnaServices.STATES.RESET);
            setDone(false);
           
            try {
                while (!done){
                    
                    if (!states_stack.isEmpty()) state = states_stack.pop();
                    //System.out.println(".");
                    
                    switch (state){
                        
                        case INIT:
                            log.log(Level.FINE, "FOB em INIT");
                            break;
                            
                        case IDLE:
                            smm = tqueue.poll();                         
                            if (smm != null){
                                cmd = smm.getCommand();
                                if (cmd == VirnaServices.CMDS.LOADSTATE){
                                    state = smm.getState();
                                }
                            }
                            
                            if (doscan){
                                try { Thread.sleep(500); } catch (InterruptedException ex) {
                                    System.out.println ("Thread de serviços foi interrompida");
                                }
                                long current_timestamp = System.currentTimeMillis();
                                for (FileObserverDescriptor lfod : observers){
                                    if (lfod.getTimeSpot() < current_timestamp){
                                        visitFile(lfod, current_timestamp);                   
                                    }
                                }
                            }
                            else{
                                try { Thread.sleep(100); } catch (InterruptedException ex) {
                                    System.out.println ("Thread de serviços foi interrompida");
                                }
                            }
                            
                            break;    

                        case CONFIG:
                            //log.log(Level.FINE, "Controler em CONFIG");
                            break; 
                            
                        case RESET:
                            //log.log(Level.FINE, "FOB em RESET");
                            states_stack.push(VirnaServices.STATES.IDLE);
                            states_stack.push(VirnaServices.STATES.CONFIG);
                            states_stack.push(VirnaServices.STATES.INIT);
                            break;
                                
                        case FOB_DOSCAN:
                            doscan=true;
                            log.log(Level.FINE, "FOB Scan = true" );
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;
                            
                        case FOB_STOPSCAN:
                            doscan=false;
                            log.log(Level.FINE, "FOB Scan = false");
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;    
                            
                    }
                }
            } catch (Exception ex) {
                log.log(Level.WARNING, ex.toString());
            }

        }

        public void setDone(boolean done) {
            if (done) log.log(Level.FINE, "FOB Stopping Service");
            this.done = done;
        }   
    };

}
