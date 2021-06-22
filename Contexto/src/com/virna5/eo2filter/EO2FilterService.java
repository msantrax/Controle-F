/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.eo2filter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.BaseService;
import com.virna5.contexto.MonitorIFrameInterface;
import com.virna5.contexto.ResultField;
import com.virna5.contexto.ResultRecord;
import com.virna5.contexto.SMTraffic;
import com.virna5.contexto.VirnaPayload;
import com.virna5.contexto.VirnaServices;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;


public class EO2FilterService extends BaseService {

    private static final Logger log = Logger.getLogger(EO2FilterService.class.getName());
    private EO2FilterService.SMThread service_thread;    
    
    protected LinkedBlockingQueue<SMTraffic> smqueue;
    
    private ArrayList<String> outrecord;
    public static final String timestamp_format = "%1$tH,%1$tM   %1$td,%1$tm,%1$ty";
    
    private static EO2FilterService instance;    
    public static EO2FilterService getInstance(){
        if (instance == null) {instance = new EO2FilterService();}
        return instance;
    }
    public EO2FilterService getDefault() { return instance; }
    
  
    public EO2FilterService() {
        
        super();
        log.setLevel(Level.FINE);
        
        descriptors = new LinkedHashMap<>();
        
        startService();
        
        instance = this;      
    }
    
    
     @Override
    public void configService(BaseDescriptor bd){
        
        EO2FilterDescriptor fod = (EO2FilterDescriptor) bd;
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
    
    
    
    
    
    public String parseLoad(String pld, EO2FilterDescriptor fodesc){
           
        StringBuilder sb = new StringBuilder();
        String EO2_header = "";
        String EO2_limit = "";
        
        GsonBuilder builder = new GsonBuilder(); 
        builder.setPrettyPrinting(); 
        Gson gson = builder.create();      
        ResultRecord rr = gson.fromJson(pld,  ResultRecord.class);
       
        outrecord = new ArrayList<>();
        int elements_num = 0;
        
        for (EO2Field csvf : fodesc.getEO2fields()){
            String fieldname = csvf.getFieldname();
            
            if (csvf.getRecord().equals("{SEQ}")){
                outrecord.add(csvf.getFieldname()+":"+" 1");
            }
            else if (csvf.getRecord().equals("{DATE}")){
                outrecord.add(csvf.getFieldname()+": "+ String.format(timestamp_format, Calendar.getInstance()));
            }
            else if (csvf.getRecord().equals("{ELEMENTS}")){
                outrecord.add("Elements: ");
            }
            else if (csvf.getFieldindex() == -1){
                String rfield = csvf.getRecord();
                String rvalue="";
                for (ResultField fheader : rr.getHeader()){
                    if (fheader.getName().equals(rfield)){
                        rvalue=fheader.getValue();
                        break;
                    }
                }
                outrecord.add(csvf.getFieldname()+": "+rvalue);
            }
            else if (csvf.getFieldindex() == 0){
                String rfield = csvf.getRecord();
                String rvalue="";
                for (ResultField felm : rr.getValues()){
                    if (felm.getName().equals(rfield)){
                        rvalue=felm.getValue();
                        break;
                    }
                }
                if (!rvalue.equals("")){
                    outrecord.add(csvf.getFieldname()+": "+rvalue);
                    elements_num++;
                }
                
            }
            else if (csvf.getFieldindex() == 1){
                String rfield = csvf.getRecord();
                String rvalue="";
                for (ResultField fheader : rr.getHeader()){
                    if (fheader.getName().equals(rfield)){
                        rvalue=fheader.getValue();
                        break;
                    }
                }
                if (csvf.getFieldname().contains("Sample")){
                    if (rvalue.equals("")){
                        rvalue = "0";
                    }
                }
                
                outrecord.add(csvf.getFieldname()+": " + rvalue + ",SampleID,GN,,1,,,,,");
            }
            
            else if (csvf.getFieldindex() == 2){
                String rfield = csvf.getFieldname();
                String rrecord = csvf.getRecord();
                String or_item="SampleID";
                
                for (int i = 0; i < outrecord.size(); i++) {                   
                    if (rfield.contains(outrecord.get(i))){
                        for (ResultField fheader : rr.getHeader()){
                            if (fheader.getName().equals(rrecord)){
                                or_item=fheader.getValue();
                                break;
                            }
                        }
                    }
                }
            }
                
        }
        
        
        
        for (String item : outrecord){
            if (item.contains("Elements")){
                item = item + elements_num;
            }
            sb.append(item);
            sb.append("\n\r");
        }
        
        return sb.toString();
        
    }
 
    private String buildEO2Record(String field, String value, String template){
        
        String temp = "";
        StringBuilder sb = new StringBuilder();
        
        
        
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
        service_thread = new EO2FilterService.SMThread(smqueue);
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
        private EO2FilterDescriptor fodesc;
        private com.virna5.eo2filter.EO2IFrame liframe;
        

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
                            log.log(Level.FINE, "EO2F em INIT");
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
                                log.fine(String.format("%s task %d on EO2FilterService using %s", action, lhandle, temp_bd.getName()));
                            }
                            
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;    
                        
                       case TSK_CLEAR:
                            liframe = (com.virna5.eo2filter.EO2IFrame) getIFrame(String.valueOf(smm.getHandle()));
                            if (liframe != null){
                                JDesktopPane mon = liframe.getDesktopPane();
//                                mon.getDesktopManager().deiconifyFrame(liframe); 
//                                mon.remove(liframe);
                                
                                mon.getDesktopManager().closeFrame(liframe);
                                log.fine("UI Interface removed @ CSVFilter");
                            }
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;        
 
                        case TSK_INITUI:
                            log.fine("EO2 Generator doing initui @ "+ smm.getHandle());
                            liframe = (com.virna5.eo2filter.EO2IFrame) getIFrame(String.valueOf(smm.getHandle()));
                            fodesc = (EO2FilterDescriptor)descriptors.get(smm.getHandle());
                            liframe.setIcon(fodesc.getIconifyui());
                            liframe.setLocation(fodesc.getUilandx(), fodesc.getUilandy());
                            
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;      
                            
                            
                        case LOADRECORD:  
                            log.fine("EO2Filter is converting");                       
                            fodesc = (EO2FilterDescriptor)descriptors.get(smm.getHandle());
                            VirnaPayload vp = smm.getPayload();
                            String pld = vp.vstring;
                            String record = parseLoad(pld, fodesc);
                            //log.info(record);
                            if (!record.equals("")){
                                
                                liframe = (com.virna5.eo2filter.EO2IFrame) getIFrame(String.valueOf(smm.getHandle()));
                                if (liframe != null){
                                    liframe.updateUI(MonitorIFrameInterface.LED_GREEN_ON, " ");    
                                }
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
                log.log(Level.SEVERE,"Falha na máquina de estados no Filtro EO2 : " + ex.toString());
                startService();
            }

        }

        public void setDone(boolean done) {
            if (done) log.log(Level.FINE, "SAPFilter Stopping Service");
            this.done = done;
        }   
    };

}

