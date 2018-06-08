/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.sapfilter;

import com.virna5.csvfilter.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.BaseService;
import com.virna5.contexto.ResultField;
import com.virna5.contexto.ResultRecord;
import com.virna5.contexto.SMTraffic;
import com.virna5.contexto.VirnaPayload;
import com.virna5.contexto.VirnaServices;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;


public class SAPFilterService extends BaseService {

    private static final Logger log = Logger.getLogger(SAPFilterService.class.getName());
    private SAPFilterService.SMThread service_thread;    
   
    private static SAPFilterService instance;    
    public static SAPFilterService getInstance(){
        if (instance == null) {instance = new SAPFilterService();}
        return instance;
    }
    public SAPFilterService getDefault() { return instance; }
    
  
    public SAPFilterService() {
        
        super();
        log.setLevel(Level.FINE);
        
        descriptors = new LinkedHashMap<>();
        
        startService();
        
        instance = this;      
    }
    
    
     @Override
    public void configService(BaseDescriptor bd){
        
        CSVFilterDescriptor fod = (CSVFilterDescriptor) bd;
        Long uid = fod.getUID();
        if (descriptors.containsKey(uid)){
            descriptors.remove(uid);
        }
        descriptors.put(uid, fod);
        log.info(String.format("Configuring CVSFilterService [%s] to context : %s", bd.toString(),uid));
    }
 
    
    public String parseLoad(String pld, CSVFilterDescriptor fodesc){
      
        
        ResultRecord rr = ResultRecord.ResultRecordFactory();
        ArrayList<ResultField> fields = rr.getFields();
        String sjson = "";
     
        rr.setName(fodesc.getName());
        
        if (pld.contains("\r\n") || pld.contains("\n")){
            // Has header
            pld = pld.replace("\r", "");
            String[] lpld = pld.split("\n");
            
            String [] cheaders = lpld[0].split(fodesc.getSeparator());;
            String [] cfields = lpld[1].split(fodesc.getSeparator());
            
            if (cfields.length != fodesc.getCsvfields().size()){
                log.fine("CSVFields size do not match");
            }
            
            int counter=0;
            for (CSVField csvf : fodesc.getCsvfields()){
                //csvf.setValue(cfields[counter]);
                if (csvf.isReadcsv()){
                    ResultField rf = new ResultField(   rr.getUid(),
                                                        csvf.getResultfield(), 
                                                        CSVField.getRealmEnum(csvf.getRealm()),
                                                        CSVField.getTypeEnum(csvf.getType()),
                                                        cfields[counter],
                                                        csvf.getResultseq());
                    fields.add(rf);
                }
                counter++;
            }
            
            GsonBuilder builder = new GsonBuilder(); 
            builder.setPrettyPrinting(); 
            Gson gson = builder.create();
            sjson = gson.toJson(rr);

            log.info("========== JSON : ==================\n\r");
            log.info(sjson);
            log.info(String.format("Json parser loaded %d chars", sjson.length()));
        }
        else{
            // Single Line
        }

        return sjson;
    }
    
    
    
    
    
    // ================================================================================================================
    private void stopService(){
        //services.removeUsbServicesListener(this);
        service_thread.setDone(true);    
    }
    
    private void startService(){      
        smqueue = new LinkedBlockingQueue<>() ;
        service_thread = new SAPFilterService.SMThread(smqueue);
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
        private CSVFilterDescriptor fodesc;
        private com.virna5.csvfilter.MonitorIFrame liframe;
        

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
                            log.log(Level.FINE, "CSVF em INIT");
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
                        
                        case TSK_MANAGE:
                            Long lhandle = smm.getHandle();
                            temp_bd = descriptors.get(lhandle);
                            if (temp_bd !=null){
                                String action = (smm.getCode() == 1) ? "Activating" : "Deactivating;";
                                log.fine(String.format("%s task %d on FileWriterService using %s", action, lhandle, temp_bd.getName()));
                            }
                            
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;    
                        
                       case TSK_CLEAR:
                            liframe = (com.virna5.csvfilter.MonitorIFrame) getIFrame(String.valueOf(smm.getHandle()));
                            if (liframe != null){
                                JDesktopPane mon = liframe.getDesktopPane();
                                mon.remove(liframe);
                                log.fine("UI Interface removed @ CSVFilter");
                            }
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;        
                               
                            
                        case CSV_CONVERT:
                            log.fine("CSVFilter is converting");                       
                            fodesc = (CSVFilterDescriptor)descriptors.get(smm.getHandle());
                            VirnaPayload vp = smm.getPayload();
                            String pld = vp.vstring;
                            String record = parseLoad(pld, fodesc);
                            if (!record.equals("")){
                                fodesc.notifySignalListeners(0, new SMTraffic(fodesc.getUID(),
                                            VirnaServices.CMDS.LOADSTATE,
                                            0, 
                                            VirnaServices.STATES.LOADRECORD, 
                                            new VirnaPayload().setString(record)
                                        ));
                            }
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;    
                            
                        default:
                            log.fine("Undefined state on CSVFilter : " + smm.getState().toString());
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

