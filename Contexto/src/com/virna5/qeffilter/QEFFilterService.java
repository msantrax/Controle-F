/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.qeffilter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.BaseService;
import com.virna5.contexto.ContextUtils;
import com.virna5.contexto.MonitorIFrameInterface;
import com.virna5.contexto.ResultField;
import com.virna5.contexto.ResultRecord;
import com.virna5.contexto.SMTraffic;
import com.virna5.contexto.VirnaPayload;
import com.virna5.contexto.VirnaServices;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JDesktopPane;


public class QEFFilterService extends BaseService {

    private static final Logger log = Logger.getLogger(QEFFilterService.class.getName());
    private QEFFilterService.SMThread service_thread;    
    
    protected LinkedBlockingQueue<SMTraffic> smqueue;
    
    private String qef_haeder="";
   
    private static QEFFilterService instance;    
    public static QEFFilterService getInstance(){
        if (instance == null) {instance = new QEFFilterService();}
        return instance;
    }
    public QEFFilterService getDefault() { return instance; }
    
  
    public QEFFilterService() {
        
        super();
        log.setLevel(Level.FINE);
        
        descriptors = new LinkedHashMap<>();
        
        startService();
        
        instance = this;      
    }
    
    
     @Override
    public void configService(BaseDescriptor bd){
        
        QEFFilterDescriptor fod = (QEFFilterDescriptor) bd;
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
    
    private String buildCardName(String cardname, ResultRecord rr){
        
        StringBuilder sb = new StringBuilder();
        
        String[] namefields = cardname.split(":");
        if (namefields.length >1){
            
            for (String field : namefields){
                if (field.contains("timestamp")){
                    sb.append(ContextUtils.getCompactTimestamp());
                }
                else if (field.contains("field=")){
                    String fieldname = field.substring(6);
                    ResultField rf = rr.findField(fieldname);
                    if (rf != null){
                        sb.append(rf.getValue());
                    }
                }
                else{
                    sb.append(field);
                }
            }
            return sb.toString();
        } 
        
        return cardname;
    }
    
    public String parseLoad(String pld, QEFFilterDescriptor fodesc){
           
        StringBuilder sb = new StringBuilder();
        String qef_header = "";
        String qef_limit = "";
        
        GsonBuilder builder = new GsonBuilder(); 
        builder.setPrettyPrinting(); 
        Gson gson = builder.create();      
        ResultRecord rr = gson.fromJson(pld,  ResultRecord.class);
        
        String cardname = buildCardName(fodesc.getCardname(), rr);
        
        InputStream header_is = QEFFilterService.class.getResourceAsStream("qef_header.dat");
        if (header_is != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(header_is));
            qef_header = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
        
        InputStream limit_is = QEFFilterService.class.getResourceAsStream("qef_limit.dat");
        if (limit_is != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(limit_is));
            qef_limit = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
   
        
        qef_header = qef_header.replaceAll("token:limitcard_name", cardname);
        qef_header = qef_header.replaceAll("token:limit_group", fodesc.getGroupname());
  
        
     
        for (QEFField csvf : fodesc.getQEFfields()){
            String fieldname = csvf.getFieldname();
            ResultField rf = rr.findField(fieldname);
            if (rf == null){
                log.warning("Failed to parse QEF template - field not found : "+ fieldname);
                continue;
            }
            csvf.setValue(QEFField.encodeField(rf.getValue()));
            String limit_temp = qef_limit;
            limit_temp = limit_temp.replaceAll("token:limitcard_name", cardname);
            limit_temp = limit_temp.replaceAll("token:limit_group", fodesc.getGroupname());
            limit_temp = limit_temp.replaceAll("token:compoundinfo_name", fieldname);
            limit_temp = limit_temp.replaceAll("token:nominal_value", rf.getValue());
            
            limit_temp = limit_temp.replaceAll("token:alarm_high", QEFField.convertField(csvf.getRangehigh()));
            limit_temp = limit_temp.replaceAll("token:alarm_low", QEFField.convertField(csvf.getRangelow()));
            limit_temp = limit_temp.replaceAll("token:warning_high", QEFField.convertField(csvf.getAlarmhigh()));
            limit_temp = limit_temp.replaceAll("token:warning_low", QEFField.convertField(csvf.getAlarmlow()));
            
            
            sb.append(limit_temp);
            
        }
        String limits = sb.toString();
        qef_header = qef_header.replaceAll("token:body", limits);
        
        return qef_header;
        
    }
 
    private String buildQEFRecord(String field, String value, String template){
        
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
        service_thread = new QEFFilterService.SMThread(smqueue);
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
        private QEFFilterDescriptor fodesc;
        private com.virna5.qeffilter.QEFIFrame liframe;
        

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
                            log.log(Level.FINE, "QEFF em INIT");
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
                                log.fine(String.format("%s task %d on QEFFilterService using %s", action, lhandle, temp_bd.getName()));
                            }
                            
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;    
                        
                       case TSK_CLEAR:
                            liframe = (com.virna5.qeffilter.QEFIFrame) getIFrame(String.valueOf(smm.getHandle()));
                            if (liframe != null){
                                JDesktopPane mon = liframe.getDesktopPane();
//                                mon.getDesktopManager().deiconifyFrame(liframe); 
//                                mon.remove(liframe);
                                
                                mon.getDesktopManager().closeFrame(liframe);
                                log.fine("UI Interface removed @ QEFFilter");
                            }
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;        
 
                        case TSK_INITUI:
                            log.fine("QEF Generator doing initui @ "+ smm.getHandle());
                            liframe = (com.virna5.qeffilter.QEFIFrame) getIFrame(String.valueOf(smm.getHandle()));
                            fodesc = (QEFFilterDescriptor)descriptors.get(smm.getHandle());
                            liframe.setIcon(fodesc.getIconifyui());
                            liframe.setLocation(fodesc.getUilandx(), fodesc.getUilandy());
                            
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;      
                            
                            
                        case LOADRECORD:  
                            log.fine("QEFFilter is converting");                       
                            fodesc = (QEFFilterDescriptor)descriptors.get(smm.getHandle());
                            VirnaPayload vp = smm.getPayload();
                            String pld = vp.vstring;
                            String record = parseLoad(pld, fodesc);
                            //log.info(record);
                            if (!record.equals("")){
                                
                                liframe = (com.virna5.qeffilter.QEFIFrame) getIFrame(String.valueOf(smm.getHandle()));
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
                log.log(Level.WARNING, ex.toString());
                
            }

        }

        public void setDone(boolean done) {
            if (done) log.log(Level.FINE, "SAPFilter Stopping Service");
            this.done = done;
        }   
    };

}

