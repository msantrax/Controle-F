/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.sapfilter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.BaseService;
import com.virna5.contexto.ResultField;
import com.virna5.contexto.ResultRecord;
import com.virna5.contexto.SMTraffic;
import com.virna5.contexto.VirnaPayload;
import com.virna5.contexto.VirnaServices;
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
    
    protected LinkedBlockingQueue<SMTraffic> smqueue;
   
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
        
        SAPFilterDescriptor fod = (SAPFilterDescriptor) bd;
        Long uid = fod.getUID();
        if (descriptors.containsKey(uid)){
            descriptors.remove(uid);
        }
        descriptors.put(uid, fod);
        //log.info(String.format("Configuring SAPFilterService [%s] to context : %s", bd.toString(),uid));
    }
 
    
    
    @Override
    public void processSignal (SMTraffic signal, BaseDescriptor bd){
        
        smqueue.add(signal);
    }
    
    public String parseLoad(String pld, SAPFilterDescriptor fodesc){
    
        GsonBuilder builder = new GsonBuilder(); 
        builder.setPrettyPrinting(); 
        Gson gson = builder.create();
       
        ResultRecord rr = gson.fromJson(pld,  ResultRecord.class);
        //ArrayList<ResultField> fields = rr.getFields();
        String value;
        String saprec;
        StringBuilder sb = new StringBuilder();
                
        for (SAPField csvf : fodesc.getSapfields()){
            value = getRecordValue (rr, csvf.getFieldname().trim());
            if (value != null){
                saprec = buildSAPRecord(csvf.getFieldname().trim(), value, csvf.getTemplate());
                if (saprec != null) sb.append(saprec);
            }
        }

        return sb.toString();
        
    }
 
    private String buildSAPRecord(String field, String value, String template){
        
        String temp = "";
        StringBuilder sb = new StringBuilder();
        
        String[] tplfs = template.split("@");
        
        for (String tplf: tplfs){
            if (tplf.contains("[")){
                String[] cnvfields = tplf.split(":");
                if (cnvfields.length == 2){
                    temp = cnvfields[0].replace("[", "");
                    if (temp.equals("nome")){
                        temp = cnvfields[1].replace("]", "");
                        sb.append(String.format(temp, field));
                        sb.append("@");
                    }
                    else if (temp.equals("valor")){
                        temp = cnvfields[1].replace("]", "");
                        try{
                            Double vl = Double.parseDouble(value);
                            sb.append(String.format(temp, vl));
                            sb.append("@");
                        }
                        catch (Exception ex ){
                            sb.append("erro de conversão na template@");
                        }
                    } 
                }
                else{
                    sb.append("erro de sintaxe na template@");
                }
            }
            else{
                sb.append(tplf);
                sb.append("@");
            }
            
        }
        
        sb.append("\r\n");
        temp = sb.toString();
        return temp;
    }
    
    
    
    private String getRecordValue (ResultRecord rr, String fieldname){
        
        ArrayList<ResultField> fields = rr.getFields();
        
        for (ResultField rf : fields){
            if (rf.getName().equals(fieldname)){
                return rf.getValue();
            }
        }
        return null;
    }
    
    
    
    
    
    
    // ================================================================================================================
    private void stopService(){
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
        private SAPFilterDescriptor fodesc;
        private com.virna5.sapfilter.MonitorIFrame liframe;
        

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
                            log.log(Level.FINE, "SAPF em INIT");
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
                                log.fine(String.format("%s task %d on SAPFilterService using %s", action, lhandle, temp_bd.getName()));
                            }
                            
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;    
                        
                       case TSK_CLEAR:
                            liframe = (com.virna5.sapfilter.MonitorIFrame) getIFrame(String.valueOf(smm.getHandle()));
                            if (liframe != null){
                                JDesktopPane mon = liframe.getDesktopPane();
                                //mon.remove(liframe);
                                
                                mon.getDesktopManager().closeFrame(liframe);
                                log.fine("UI Interface removed @ SAPFilter");
                            }
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;        
 
                        case LOADRECORD:  
                            log.fine("SAPFilter is converting");                       
                            fodesc = (SAPFilterDescriptor)descriptors.get(smm.getHandle());
                            VirnaPayload vp = smm.getPayload();
                            String pld = vp.vstring;
                            String record = parseLoad(pld, fodesc);
                            if (!record.equals("")){
                                fodesc.notifySignalListeners(0, new SMTraffic(fodesc.getUID(),
                                            VirnaServices.CMDS.LOADSTATE,
                                            0, 
                                            VirnaServices.STATES.FWRITER_WRITE, 
                                            new VirnaPayload().setString(record)
                                        ));
                            }
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;    
                            
                        default:
                            log.fine("Undefined state on SAPFilter : " + smm.getState().toString());
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;  
                            
                    }
                }
            } catch (Exception ex) {
                log.log(Level.SEVERE,"Falha na máquina de estados no Filtro SAP : " + ex.toString());
                startService();
            }

        }

        public void setDone(boolean done) {
            if (done) log.log(Level.FINE, "SAPFilter Stopping Service");
            this.done = done;
        }   
    };

}

