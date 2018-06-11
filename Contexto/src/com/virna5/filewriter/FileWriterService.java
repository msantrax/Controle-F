/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.filewriter;

import com.virna5.fileobserver.*;
import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.BaseService;
import com.virna5.contexto.ContextUtils;
import com.virna5.contexto.MonitorIFrameInterface;
import com.virna5.contexto.SMTraffic;
import com.virna5.contexto.VirnaPayload;
import com.virna5.contexto.VirnaServices;
import static com.virna5.contexto.VirnaServices.STATES.IDLE;
import static com.virna5.contexto.VirnaServices.STATES.INIT;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;

public class FileWriterService extends BaseService {

    private static final Logger log = Logger.getLogger(FileWriterService.class.getName());
    
    private static FileWriterService instance;    
   
    private FileWriterService.SMThread service_thread;
 
    private Long current_handle;
    
    protected LinkedBlockingQueue<SMTraffic> smqueue;
    
    public static FileWriterService getInstance(){
        if (instance == null) {instance = new FileWriterService();}
        return instance;
    }

    public FileWriterService() {
  
        super();
        
        log.setLevel(Level.FINE);
        
        instance = this;      
     
        startService();
        
    }

    public FileWriterService getDefault() { return instance; }
  
    @Override
    public void configService(BaseDescriptor bd){
        
        FileWriterDescriptor fod = (FileWriterDescriptor) bd;
        Long uid = fod.getUID();
        if (descriptors.containsKey(uid)){
            descriptors.remove(uid);
        }
        descriptors.put(uid, fod);
        log.info(String.format("Configuring FileWriterclass %s to context : %s", bd.toString(),uid));
    }
   
    
    @Override
    public void UpdateUI (String mes, String bduid){
        
        com.virna5.filewriter.MonitorIFrame liframe = (com.virna5.filewriter.MonitorIFrame) getIFrame(bduid);
        FileWriterDescriptor fwd = (FileWriterDescriptor)descriptors.get(Long.parseLong(bduid));
        
        if ( liframe != null){
            if (mes == null){
                FileWriterUIUpdater fwuiup = new FileWriterUIUpdater();
                liframe.setWriteFile(fwd.getOutputfile());
                
                log.fine("Init UI em Filewriter");
            }           
        }
    }
  
    public void visitFile(FileObserverDescriptor lfod, long ct){
          
//        log.log(Level.FINE, "Observer Cicle called " 
//                    + lfod.getInputfile_path().getFileName()
//                    + "    @ " + dateFormat.format(new Date())
//                    + "    gap : " + (ct - lfod.getLastseen()));
//            lfod.setLastseen(ct);
//            lfod.setTimespot(ct + lfod.getInterval());
        
    }
    
    
    @Override
    public void processSignal (SMTraffic signal, BaseDescriptor bd){
        
        smqueue.add(signal);
        log.info(String.format("File writer registered %s to %d @ %s ", signal.getState().toString(), signal.getHandle(), this.toString()));
        
    }
    
    
    // ================================================================================================================
    private void stopService(){
        //services.removeUsbServicesListener(this);
        service_thread.setDone(true);    
    }
    
    private void startService(){      
        smqueue = new LinkedBlockingQueue<>() ;
        service_thread = new FileWriterService.SMThread(smqueue);
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
        private FileWriterDescriptor fwdesc;
        private com.virna5.filewriter.MonitorIFrame liframe;
        

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
                            log.log(Level.FINE, "FW em INIT");
                            break;
                            
                        case IDLE:
                            smm = tqueue.poll();                         
                            if (smm != null){
                                cmd = smm.getCommand();
                                log.info(String.format("File writer pumped %s to %d @ %s ", smm.getState().toString(), smm.getHandle(), this.toString()));
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
                            
                        case TSK_MANAGE:
                            Long lhandle = smm.getHandle();
                            temp_bd = descriptors.get(lhandle);
                            if (temp_bd !=null){
                                String action = (smm.getCode() == 1) ? "Activating" : "Deactivating;";
                                log.fine(String.format("%s task %d on FileWriterService using %s", action, lhandle, temp_bd.getName()));
                            }
                            
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;
                        
                        case RESET:
                            //log.log(Level.FINE, "FOB em RESET");
                            states_stack.push(VirnaServices.STATES.IDLE);
                            states_stack.push(VirnaServices.STATES.CONFIG);
                            states_stack.push(VirnaServices.STATES.INIT);
                            break; 
                        
                        case LOADRECORD:
                            VirnaPayload vp1 = smm.getPayload();
                            String payload1 = vp1.vstring;
                            
                            liframe = (com.virna5.filewriter.MonitorIFrame) getIFrame(String.valueOf(smm.getHandle()));
                            fwdesc = (FileWriterDescriptor)descriptors.get(smm.getHandle());
                            if (fwdesc != null){
                                try{
                                    ContextUtils.saveFile(fwdesc.getOutputfile(), payload1);
                                    log.fine(String.format("LOADRecord Wrote file %s with : %s",fwdesc.getOutputfile(), payload1));
                                    if (liframe != null){
                                        liframe.updateUIDirect(new FileWriterUIUpdater()
                                                                .setLedcolor(MonitorIFrameInterface.LED_GREEN_ON)
                                                                .setPayload(payload1)
                                                            );
                                     }
                                    }catch(Exception ex){
                                        log.warning(String.format("Failed to write %s due %s", fwdesc.getOutputfile(), ex.toString()));
                                        if (liframe != null){
                                            liframe.updateUIDirect(new FileWriterUIUpdater()
                                                                    .setLedcolor(MonitorIFrameInterface.LED_RED)
                                                                    .setPayload(ex.toString())
                                                                );
                                        }
                                    }
                                
                            }
                          
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;
                            
                            
                            
//                            states_stack.push(VirnaServices.STATES.FWRITER_WRITE);
//                            break; 
                            
                        case FWRITER_WRITE:
                            VirnaPayload vp = smm.getPayload();
                            String payload = vp.vstring;
                            
                            liframe = (com.virna5.filewriter.MonitorIFrame) getIFrame(String.valueOf(smm.getHandle()));
                            fwdesc = (FileWriterDescriptor)descriptors.get(smm.getHandle());
                            if (fwdesc != null){
                                try{
                                    ContextUtils.saveFile(fwdesc.getOutputfile(), payload);
                                    log.fine(String.format("Wrote file %s with : %s",fwdesc.getOutputfile(), payload));
                                    if (liframe != null){
                                        liframe.updateUIDirect(new FileWriterUIUpdater()
                                                                .setLedcolor(MonitorIFrameInterface.LED_GREEN_ON)
                                                                .setPayload(payload)
                                                            );
                                     }
                                    }catch(Exception ex){
                                        log.warning(String.format("Failed to write %s due %s", fwdesc.getOutputfile(), ex.toString()));
                                        if (liframe != null){
                                            liframe.updateUIDirect(new FileWriterUIUpdater()
                                                                    .setLedcolor(MonitorIFrameInterface.LED_RED)
                                                                    .setPayload(ex.toString())
                                                                );
                                        }
                                    }
                                
                            }
                          
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;
    
                            default:
                                log.fine("Undefined state on QS4Generator : " + smm.getState().toString());
                                states_stack.push(VirnaServices.STATES.IDLE);
                                break;
 
//                        case RESET:
//                            
//                            states_stack.push(VirnaServices.STATES.IDLE);
//                            break;    
                            
                            
                    }
                }
            } catch (Exception ex) {
                log.log(Level.WARNING, String.format("Falha na maquina de estados em FileWriter : %s"), ex.toString());
                startService();
            }

        }

        public void setDone(boolean done) {
            if (done) log.log(Level.FINE, "FOB Stopping Service");
            this.done = done;
        }   
    };

}

//
//if (doscan){
//    try { Thread.sleep(500); } catch (InterruptedException ex) {
//        System.out.println ("Thread de serviços foi interrompida");
//    }
//    long current_timestamp = System.currentTimeMillis();
//    for (FileObserverDescriptor lfod : observers){
//        if (lfod.getTimeSpot() < current_timestamp){
//            visitFile(lfod, current_timestamp);                   
//        }
//    }
//}
//else{
//    try { Thread.sleep(100); } catch (InterruptedException ex) {
//        System.out.println ("Thread de serviços foi interrompida");
//    }
//}



//public void visualizePayload(){
//        
////        VisualizePanel vp = new VisualizePanel();
////        
////        
////        DialogDescriptor d = new DialogDescriptor(vp, "Visualize os dados", true, this);
////        d.setClosingOptions(new Object[]{});
////        d.addPropertyChangeListener(new PropertyChangeListener() {
////            public void propertyChange(PropertyChangeEvent e) {
////                if(e.getPropertyName().equals(DialogDescriptor.PROP_VALUE)&& e.getNewValue()==DialogDescriptor.CLOSED_OPTION) {
////                    
////                }
////            }
////        });
////        DialogDisplayer.getDefault().notifyLater(d);
//        
//        
//    }