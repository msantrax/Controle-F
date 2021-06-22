/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.interceptor;

import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.BaseService;
import com.virna5.contexto.SMTraffic;
import com.virna5.contexto.VirnaPayload;
import com.virna5.contexto.VirnaServices;
import static com.virna5.contexto.VirnaServices.STATES.IDLE;
import static com.virna5.contexto.VirnaServices.STATES.INIT;
import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;

public class InterceptorService extends BaseService {

    private static final Logger log = Logger.getLogger(InterceptorService.class.getName());
    
    private static InterceptorService instance; 
    
    private InterceptorDescriptor fod;
   
    private InterceptorService.SMThread service_thread;
 
    private Long current_handle;
    
    protected LinkedBlockingQueue<SMTraffic> smqueue;
    
    public static InterceptorService getInstance(){
        if (instance == null) {instance = new InterceptorService();}
        return instance;
    }

    public InterceptorService() {
  
        super();
        
        log.setLevel(Level.FINE);
        
        instance = this;      
     
        startService();
        
    }

    public InterceptorService getDefault() { return instance; }
  
    
    
    
    @Override
    public void configService(BaseDescriptor bd){
        
        fod = (InterceptorDescriptor) bd;
        
        Long uid = fod.getUID();
        if (descriptors.containsKey(uid)){
            descriptors.remove(uid);
        }
        descriptors.put(uid, fod);
//        fod.initModel();
//        if (fod.isAutoloadui()){
//            
//        }
        
        log.info(String.format("Configuring Interceptorclass %s to context : %s", bd.toString(),uid));
    }
   
   
    
    @Override
    public void UpdateUI (String mes, String bduid){
        
//        com.virna5.interceptor.MonitorIFrame liframe = (com.virna5.interceptor.MonitorIFrame) getIFrame(bduid);
//        InterceptorDescriptor fwd = (InterceptorDescriptor)descriptors.get(Long.parseLong(bduid));
        
//        if ( liframe != null){
//            if (mes == null){
//                FileWriterUIUpdater fwuiup = new FileWriterUIUpdater();
//                liframe.setWriteFile(fwd.getOutputfile());
//                
//                log.fine("Init UI em Filewriter");
//            }           
//        }
    }
 
    
    @Override
    public void processSignal (SMTraffic signal, BaseDescriptor bd){
        
        smqueue.add(signal);
        log.info(String.format("Interceptor registered %s to %d @ %s ", signal.getState().toString(), signal.getHandle(), this.toString()));
        
    }
    
    
    // ================================================================================================================
    private void stopService(){
        //services.removeUsbServicesListener(this);
        service_thread.setDone(true);    
    }
    
    private void startService(){      
        smqueue = new LinkedBlockingQueue<>() ;
        service_thread = new InterceptorService.SMThread(smqueue);
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
        private InterceptorDescriptor fwdesc;
        private com.virna5.interceptor.InterceptorIFrame liframe;
        

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
                            log.log(Level.FINE, "Inteceptor em INIT");
                            break;
                            
                        case IDLE:
                            smm = tqueue.poll();                         
                            if (smm != null){
                                cmd = smm.getCommand();
                                log.info(String.format("Interceptor pumped %s to %d @ %s ", smm.getState().toString(), smm.getHandle(), this.toString()));
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
                                log.fine(String.format("%s task %d on InterceptorService using %s", action, lhandle, temp_bd.getName()));
                            }
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;
                        
                        case TSK_CLEAR:
                            liframe = (com.virna5.interceptor.InterceptorIFrame) getIFrame(String.valueOf(smm.getHandle()));
                            if (liframe != null){
                                JDesktopPane mon = liframe.getDesktopPane();
//                                mon.getDesktopManager().deiconifyFrame(liframe); 
//                                //liframe.iconifyFrame(false);
//                                mon.remove(liframe);
                                
                                mon.getDesktopManager().closeFrame(liframe);
                                log.fine("UI Interface removed @ Interceptor");
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
                            
                            liframe = (com.virna5.interceptor.InterceptorIFrame) getIFrame(String.valueOf(smm.getHandle()));
                            InterceptModel im = liframe.getModel();
                            im.loadRecord(payload1, true);
 
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;
 
                        case SENDRECORD:
                            
                            VirnaPayload vp2 = smm.getPayload();
                            String payload2 = vp2.vstring;
                            
                            fwdesc = fod;
                            if (fwdesc != null){
                                log.info("Interceptor is sending record");
                                
                                fwdesc.notifySignalListeners(0, new SMTraffic(fwdesc.getUID(),
                                            VirnaServices.CMDS.LOADSTATE,
                                            0, 
                                            VirnaServices.STATES.LOADRECORD, 
                                            new VirnaPayload().setString(payload2)
                                ));
                            }
                           
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;    
                            
                            
                        default:
                            log.fine("Undefined state on Interceptor : " + smm.getState().toString());
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;
 

                    }
                }
            } catch (Exception ex) {
                log.log(Level.WARNING, "Falha na maquina de estados no Interceptor : " + ex.toString());
                //ex.printStackTrace();
                startService();
            }

        }

        public void setDone(boolean done) {
            if (done) log.log(Level.FINE, "Interceptor Stopping Service");
            this.done = done;
        }   
    };

}

