/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.fileobserver;

import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.BaseService;
import com.virna5.contexto.ContextUtils;
import com.virna5.contexto.Controler;
import com.virna5.contexto.MonitorIFrameInterface;
import com.virna5.contexto.SMTraffic;
import com.virna5.contexto.VirnaPayload;
import com.virna5.contexto.VirnaServices;
import static com.virna5.contexto.VirnaServices.STATES.FOB_DOSCAN;
import static com.virna5.contexto.VirnaServices.STATES.FOB_STOPSCAN;
import static com.virna5.contexto.VirnaServices.STATES.IDLE;
import static com.virna5.contexto.VirnaServices.STATES.INIT;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;

public class FileObserverService extends BaseService {

    private static final Logger log = Logger.getLogger(FileObserverService.class.getName());
    
    private static FileObserverService instance;    
   
    private FileObserverService.SMThread service_thread;
    
    protected LinkedBlockingQueue<SMTraffic> smqueue;
    
    public static FileObserverService getInstance(){
        if (instance == null) {instance = new FileObserverService();}
        return instance;
    }

    public FileObserverService() {
  
        super();
        
        log.setLevel(Level.FINE);
        
        descriptors = new LinkedHashMap<>();
        
        startService();
        
        instance = this;      
        
    }

    public FileObserverService getDefault() { return instance; }
  
    @Override
    public void configService(BaseDescriptor bd){
        
        FileObserverDescriptor fod = (FileObserverDescriptor) bd;
        Long uid = fod.getUID();
        if (descriptors.containsKey(uid)){
            descriptors.remove(uid);
        }
        descriptors.put(uid, fod);
        log.info(String.format("Configuring FileObserverclass %s to context : %s", bd.toString(),uid));
    }
    
    
    @Override
    public void processSignal (SMTraffic signal, BaseDescriptor bd){
        
        smqueue.add(signal);
    }
     
    @Override
    public void UpdateUI (String mes, String bduid){
        
        com.virna5.fileobserver.MonitorIFrame liframe = (com.virna5.fileobserver.MonitorIFrame) getIFrame(bduid);
        FileObserverDescriptor fwd = (FileObserverDescriptor)descriptors.get(Long.parseLong(bduid));
        
        if ( liframe != null){
            if (mes == null){
                FileObserverUIUpdater fwuiup = new FileObserverUIUpdater();
                //liframe.setWriteFile(fwd.getOutputfile());
                
                log.fine("Init UI em Filewriter");
            }           
        }
    }
 
    
    // ================================================================================================================
    private void stopService(){
        //services.removeUsbServicesListener(this);
        service_thread.setDone(true);    
    }
    
    private void startService(){      
        smqueue = new LinkedBlockingQueue<>() ;
        service_thread = new FileObserverService.SMThread(smqueue);
        new Thread(service_thread).start();
    }
    
    private class SMThread extends Thread {
    
        private VirnaServices.STATES state;
        private boolean done;
        protected BlockingQueue<SMTraffic> tqueue;
        private VirnaServices.CMDS cmd;
        private ArrayDeque <VirnaServices.STATES>states_stack;
        private SMTraffic smm;
        
        private BaseDescriptor temp_bd;
        private FileObserverDescriptor fodesc;
        private com.virna5.fileobserver.MonitorIFrame liframe;
        
        protected boolean doscan = false;
     
        private LinkOption[] lkop = new LinkOption[]{ LinkOption.NOFOLLOW_LINKS};
        

        public SMThread(BlockingQueue<SMTraffic> tqueue) {
            this.tqueue = tqueue;
            states_stack = new ArrayDeque<>();
            states_stack.push(VirnaServices.STATES.RESET);
            setDone(false);
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
                            else{
                                Thread.sleep(200);
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
                        
                        case TSK_MANAGE:
                            Long lhandle = smm.getHandle();
                            temp_bd = getDescriptors().get(lhandle);
                            if (temp_bd !=null){
                                fodesc = (FileObserverDescriptor) temp_bd;
                                String action = (smm.getCode() == 1) ? "Activating" : "Deactivating;";
                                log.fine(String.format("%s task %d on FileObserverService using %s", action, lhandle, fodesc.getName()));
                                if (smm.getCode() == 1) {
                                    // Activate
                                    Long interv = fodesc.getInterval();
                                    if (interv > 800){ 
                                        requestAlarm(interv);
                                        fodesc.setAutomatic(true);
                                        liframe = (com.virna5.fileobserver.MonitorIFrame) getIFrame(String.valueOf(smm.getHandle()));
                                        if (liframe != null){
                                            liframe.updateUIDirect(new FileObserverUIUpdater()
                                                                        .setAuto(true)
                                            );
                                        }
                                    }   
                                }
                                else{
                                    cancelAlarm();
                                    fodesc.setAutomatic(false);
                                    liframe = (com.virna5.fileobserver.MonitorIFrame) getIFrame(String.valueOf(smm.getHandle()));
                                    if (liframe != null){
                                        liframe.updateUIDirect(new FileObserverUIUpdater()
                                                                    .setAuto(false)
                                        );
                                    }    
                                }
                            }
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;    
                        
                         case TSK_CLEAR:
                            liframe = (com.virna5.fileobserver.MonitorIFrame) getIFrame(String.valueOf(smm.getHandle()));
                            if (liframe != null){
                                JDesktopPane mon = liframe.getDesktopPane();
                                mon.remove(liframe);
                                log.fine("UI Interface removed @ FileObserver");
                            }
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;        
                            
                        case TSK_INITUI:
                            log.fine("FOB doing initui @ "+ smm.getHandle());
                            liframe = (com.virna5.fileobserver.MonitorIFrame) getIFrame(String.valueOf(smm.getHandle()));
                            fodesc = (FileObserverDescriptor)descriptors.get(smm.getHandle());
                            if (liframe != null){
                                liframe.updateUIDirect(new FileObserverUIUpdater()
                                                            .setFilename(fodesc.getInputfile_path())
                                                            .setDirbakup(fodesc.getOutputfile())
                                                            .setInterval(fodesc.getInterval())
                                                            .setTimeout(fodesc.getTimeout()/1000)
                                                            .setAuto(false)               
                                );
                            }
                            
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;  
                        
                        case TSK_SUSPEND:
                            
                            fodesc = (FileObserverDescriptor)descriptors.get(smm.getHandle());
                            fodesc.setAutomatic(smm.getCode() == 1);
                            if (smm.getCode() == 1){
                                log.fine("FOB RESUMING @ "+ smm.getHandle());
                            }
                            else{
                                log.fine("FOB SUSPENDIG @ "+ smm.getHandle());
                            }
                            
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;
                        
                        case FOB_SETPATH:
                            fodesc = (FileObserverDescriptor)descriptors.get(smm.getHandle());
                            if (smm.getCode() == 0 ){
                                fodesc.setInputfile_path(smm.getPayload().vstring);
                            }
                            else{
                                fodesc.setOutputfile(smm.getPayload().vstring);
                            }
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;
                            
                        case FOB_READ:
                           
                            liframe = (com.virna5.fileobserver.MonitorIFrame) getIFrame(String.valueOf(smm.getHandle()));
                            fodesc = (FileObserverDescriptor)descriptors.get(smm.getHandle());
                            
                            boolean dorequest = false;
                            if (fodesc.getAlarm_handle() != 0 && fodesc.getAutomatic() ){
                                 log.fine("Observ Request - automatic mode :");
                                 dorequest = true;
                            }
                            else if (fodesc.getAlarm_handle() != 0 && !fodesc.getAutomatic() ){
                                if (smm.getCode() == 1){
                                    log.fine("Observ Request - suspended manual mode :");
                                    dorequest = true;
                                }
                                else{
                                    log.fine("Observ Request - aborted :");
                                }
                            }
                            else if (fodesc.getAlarm_handle() == 0 && smm.getCode() == 1 ){
                                log.fine("Observ Request - inactive task manual mode :");
                                dorequest = true;
                            }
                            
                            if (dorequest){
                                try{
                                    Path obspath = Paths.get(fodesc.getInputfile_path());
                                    Boolean phe = Files.exists(obspath, lkop);
                                    log.fine(String.format("File %s exists=%b", obspath.toString(), phe));
                                    boolean isdir = Files.isDirectory(obspath, lkop);
                                    log.fine(String.format("Directory=%b", isdir));
                                    if (phe && !isdir){
                                        String pld = ContextUtils.loadFile(obspath.toString());
                                        fodesc.setPayload(pld);
                                        if (liframe != null){
                                            liframe.updateUIDirect(new FileObserverUIUpdater()
                                                                        .setLedcolor(MonitorIFrameInterface.LED_GREEN_ON)
                                            );
                                        }
                                        if (!fodesc.getMultiline()){
                                            Files.delete(obspath);
                                        }
                                        
                                        fodesc.notifySignalListeners(0, new SMTraffic(fodesc.getUID(),
                                            VirnaServices.CMDS.LOADSTATE,
                                            0, 
                                            VirnaServices.STATES.CSV_CONVERT, 
                                            new VirnaPayload().setString(pld)
                                        ));
                                     
                                    }
                                    else if (!phe){
                                        fodesc.setPayload("Não há dados disponíveis ...");
                                        if (liframe != null){
                                            liframe.updateUIDirect(new FileObserverUIUpdater()
                                                                        .setLedcolor(MonitorIFrameInterface.LED_YELLOW)
                                            );
                                        } 
                                    }
                                    
                                    
                                }catch (Exception ex){
                                    log.fine(String.format("Failed to read due : %s", ex.toString()));
                                    if (liframe != null){
                                        liframe.updateUIDirect(new FileObserverUIUpdater()
                                                                    .setLedcolor(MonitorIFrameInterface.LED_RED)
                                        );
                                    }  
                                }
                              
                            }
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;    
                        
                            
                        default:
                            log.fine("Undefined state on FileObserver : " + smm.getState().toString());
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;    
                            
                    }
                }
            } catch (Exception ex) {
                log.log(Level.WARNING, ex.toString());
                startService();
            }

        }

        
        // ==============================================================================================
        
        private void requestAlarm(Long period){
            
            fodesc.setAlarm_handle(Controler.getAlarmID());
            
            SMTraffic alarm_config = new SMTraffic(fodesc.getUID(),fodesc.getUID(), 0,
                                            VirnaServices.STATES.FOB_READ, 
                                            null);
                                        
            Controler.getInstance().processSignal(new SMTraffic(fodesc.getUID(),
                                            VirnaServices.CMDS.LOADSTATE,
                                            fodesc.getAlarm_handle(), 
                                            VirnaServices.STATES.CTRL_ADDALARM, 
                                            new VirnaPayload()
                                                    .setObject(alarm_config)
                                                    .setObjectType("com.virna5.contexto.SMTraffic")
                                                    .setLong1(period)
                                                    .setLong2(period) 
            ));
        }
        
        private void cancelAlarm(){
            
            if (fodesc.getAlarm_handle() == 0){
                log.warning("Trying to cancel non existent alarm on QS4Gen");
            }
            else{ 
                Controler.getInstance().processSignal(new SMTraffic(fodesc.getUID(),
                                                    VirnaServices.CMDS.LOADSTATE,
                                                    fodesc.getAlarm_handle(), 
                                                    VirnaServices.STATES.CTRL_REMOVEALARM, 
                                                    null
                ));
                fodesc.setAlarm_handle(0);
            }
        }
        
        
        
        
        public void setDone(boolean done) {
            if (done) log.log(Level.FINE, "FOB Stopping Service");
            this.done = done;
        }   
    };

}



 
//    if (doscan){
//        try { Thread.sleep(500); } catch (InterruptedException ex) {
//            System.out.println ("Thread de serviços foi interrompida");
//        }
//        long current_timestamp = System.currentTimeMillis();
//        for (FileObserverDescriptor lfod : observers){
//            if (lfod.getTimeSpot() < current_timestamp){
//                visitFile(lfod, current_timestamp);                   
//            }
//        }
//    }
//    else{
//        try { Thread.sleep(100); } catch (InterruptedException ex) {
//            System.out.println ("Thread de serviços foi interrompida");
//        }
//    }


//BasicFileAttributes attrs = Files.readAttributes(obspath, BasicFileAttributes.class);
//                                log.fine(String.format("Directory attr=%b", attrs.isDirectory()));