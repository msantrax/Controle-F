/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.qs4generator;

import com.virna5.csvfilter.*;
import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.BaseService;
import com.virna5.contexto.Controler;
import com.virna5.contexto.MonitorIFrameInterface;
import com.virna5.contexto.SMTraffic;
import com.virna5.contexto.VirnaPayload;
import com.virna5.contexto.VirnaServices;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;


public class QS4GeneratorService extends BaseService {

    private static final Logger log = Logger.getLogger(QS4GeneratorService.class.getName());
    
    private QS4GeneratorService.SMThread service_thread;    
    
    protected LinkedBlockingQueue<SMTraffic> smqueue;
    
    private static QS4GeneratorService instance;    
    public static QS4GeneratorService getInstance(){
        if (instance == null) {instance = new QS4GeneratorService();}
        return instance;
    }
    public QS4GeneratorService getDefault() { return instance; }
    
    
    private Integer alarm_handle = 0;
    
  
    // ========================================================================
    private ArrayList<String> input_fields;
    
    public String rawpayload;
    
    public CSVFilterDescriptor csvd;
    
    public ArrayList<String> lines;
    
    public ArrayList<CSVField> csvfields;
    
    
    public QS4GeneratorService() {
        
        super();
        
        log.setLevel(Level.FINE);
        
        lines = new ArrayList<>();
        csvfields = new ArrayList<>();
       
        startService();
        
    }
    
    
    @Override
    public void configService(BaseDescriptor bd){
        
        QS4GeneratorDescriptor fod = (QS4GeneratorDescriptor) bd;
        Long uid = fod.getUID();
        if (getDescriptors().containsKey(uid)){
            getDescriptors().remove(uid);
        }
        getDescriptors().put(uid, fod);
        log.info(String.format("Configuring QS4GeneratorDescriptor [%s] to context : %s", bd.toString(),uid));
    }
    
    @Override
    public void processSignal (SMTraffic signal, BaseDescriptor bd){
        
        smqueue.add(signal);
    }
    
    
    public String buildResultHeader(QS4GeneratorDescriptor desc){
        
        StringBuilder sb = new StringBuilder();
        
        for (QS4GeneratorField rf: desc.generatorfields){
            sb.append(rf.getFieldname());
            sb.append(desc.getSeparator());
        }
 
        String sout = sb.toString();
        sout = sout.substring(0, (sout.length()-1));
        if (desc.getEndline().equals("Unix")){
            sout = sout+"\r\n";
        }
        else{
            sout = sout+"\n";
        }
        
        return sout;
    }
    
    
    private String buildResultValues(QS4GeneratorDescriptor desc){
        
        StringBuilder sb = new StringBuilder();
        desc.updateField_sequence();
        Locale lc = new Locale(desc.getLocale());
        
        for (QS4GeneratorField rf: desc.generatorfields){
            sb.append(rf.calculateField(desc.getField_sequence(), lc));
            sb.append(desc.getSeparator());
        }
        
        
        String sout = sb.toString();
        sout = sout.substring(0, (sout.length()-1))+"\n\r";
        
        return sout;
        
    }


    
  
    // ================================================================================================================
    private void stopService(){
        //services.removeUsbServicesListener(this);
        service_thread.setDone(true);    
    }
    
   private void startService(){      
        smqueue = new LinkedBlockingQueue<>() ;
        service_thread = new QS4GeneratorService.SMThread(smqueue);
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
        QS4GeneratorDescriptor qs4desc;
         

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
                            
                         
                        case TSK_MANAGE:
                            Long lhandle = smm.getHandle();
                            temp_bd = getDescriptors().get(lhandle);
                            if (temp_bd !=null){
                                qs4desc = (QS4GeneratorDescriptor) temp_bd;
                                String action = (smm.getCode() == 1) ? "Activating" : "Deactivating;";
                                log.fine(String.format("%s task %d on QS4GeneratorService using %s", action, lhandle, qs4desc.getName()));
                                if (smm.getCode() == 1) {
                                    // Activate
                                    qs4desc.setField_sequence(1);
                                    if (qs4desc.getAutomatic()){ 
                                        requestAlarm(qs4desc.getInterval());
                                    }  
                                }
                                else{
                                    cancelAlarm();
                                }
                            }
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;    
                        
                        case QS4GEN_GEN:
                            log.fine("Generate result requested");
                            com.virna5.qs4generator.MonitorIFrame liframe = 
                                    (com.virna5.qs4generator.MonitorIFrame) getIFrame(String.valueOf(smm.getHandle()));
                            qs4desc = (QS4GeneratorDescriptor)descriptors.get(smm.getHandle());
                            
                            if (liframe != null){
                                liframe.updateUI(MonitorIFrameInterface.LED_GREEN_ON, " ");    
                            }
                            
                            
                            String header = "";
                            if (qs4desc .isUseheader()){
                                header = buildResultHeader(qs4desc);
                            }
                            String values = buildResultValues(qs4desc);
                            String rout = header+values;
                            qs4desc.notifySignalListeners(0, new SMTraffic(qs4desc.getUID(),
                                            VirnaServices.CMDS.LOADSTATE,
                                            0, 
                                            VirnaServices.STATES.FWRITER_WRITE, 
                                            new VirnaPayload().setString(rout)
                            ));
                            //liframe.updateUI(MonitorIFrameInterface.LED_GREEN_OFF, null);
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;    
     
                            
                        default:
                            log.fine("Undefined state on QS4Generator : " + smm.getState().toString());
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;
                            
                            
//                           case QS4GEN_GEN:
//                           
//                           states_stack.push(VirnaServices.STATES.IDLE);
//                           break;   
                        
                    }
                }
            } catch (Exception ex) {
                log.log(Level.WARNING, ex.toString());
                startService();
            }

        }

        
        // ==============================================================================================
        
        private void requestAlarm(Long period){
            
            alarm_handle = Controler.getAlarmID();
            
            SMTraffic alarm_config = new SMTraffic(qs4desc.getUID(),qs4desc.getUID(), 0,
                                            VirnaServices.STATES.QS4GEN_GEN, 
                                            null);
                                        
            Controler.getInstance().processSignal(new SMTraffic(qs4desc.getUID(),
                                            VirnaServices.CMDS.LOADSTATE,
                                            alarm_handle, 
                                            VirnaServices.STATES.CTRL_ADDALARM, 
                                            new VirnaPayload()
                                                    .setObject(alarm_config)
                                                    .setObjectType("com.virna5.contexto.SMTraffic")
                                                    .setLong1(period)
                                                    .setLong2(period) 
            ));
        }
        
        private void cancelAlarm(){
            
            if (alarm_handle == 0){
                log.warning("Trying to cancel non existent alarm on QS4Gen");
            }
            else{ 
                Controler.getInstance().processSignal(new SMTraffic(qs4desc.getUID(),
                                                    VirnaServices.CMDS.LOADSTATE,
                                                    alarm_handle, 
                                                    VirnaServices.STATES.CTRL_REMOVEALARM, 
                                                    null
                ));
                alarm_handle = 0;
            }
        }
        
        
        public void setDone(boolean done) {
            if (done) log.log(Level.FINE, "FOB Stopping Service");
            this.done = done;
        }   
    };

    
    
    
}


//
//    private void buildHeader(){
//        
//        int count = 0;
//        String[] lines = rawpayload.split("\n");
//        
//        String header = lines[0];
//        header = header.replace("\r", "");
//        String[] fields = header.split(";");
//                
//        for (String s : fields){
//            CSVField csvf = new CSVField();
//            csvf.setCSVfield(s);
//            //csvf.setId(filteredName(s));
//            csvf.setReadcsv(false);
//            if (count < 6){
//                csvf.setRealm("header");
//                csvf.setType("string");
//            }
//            else{
//                csvf.setRealm("value");
//                csvf.setType("number");
//            }
//            csvfields.add(csvf);
//        }       
//    }
//    




//    
//    public String getJson(){
//        
//        int count = 0;
//        StringBuilder sb = new StringBuilder();
//        
//        String[] lines = rawpayload.split("\n");
//        String header = lines[0];
//        header = header.replace("\r", "");
//        String[] fields = header.split(";");
//        
//        sb.append("fields: [\n");        
//                
//        for (String s : fields){
//            
//            sb.append("\t{\n");
//            
//            sb.append("\t    \"csvfield\": \"" + s + "\",\n");
//            sb.append("\t    \"id\":\"" + filteredName(s) + "\",\n");
//            
//            if (count < 6){
//                sb.append("\t    \"realm\": \"header\",\n");
//                sb.append("\t    \"type\": \"string\",\n");
//            }
//            else{
//                sb.append("\t    \"realm\": \"value\",\n");
//                sb.append("\t    \"type\": \"number\",\n");
//            }
//            count++;    
//            sb.append("\t},\n");
//        }
//        sb.append("]\n");    
//        
//        return sb.toString();
//    }
//    
//    
//    
//    private String filteredName(String fname){
//        
//        // Filtre ruídos e normalize o nome    
//        fname = fname.toUpperCase().trim();
//        fname = fname.replace(" ", "_");
//        fname = fname.replace(".", "");
//        fname = fname.replace("'", "");
//        fname = fname.replace(":", "");
//        
//        fname = Normalizer.normalize(fname, Normalizer.Form.NFC);
//        fname = fname.toUpperCase();
//        
//        fname = fname.replace("Ã", "A");
//        fname = fname.replace("Õ", "O");
//        fname = fname.replace("Á", "A");
//        fname = fname.replace("Ó", "O");
//        fname = fname.replace("Â", "A");
//        fname = fname.replace("Õ", "O");
//        fname = fname.replace("É", "E");
//        fname = fname.replace("Ê", "E");
//        fname = fname.replace("Ç", "C");
//        
//        return fname;
//        
//    }
//    
//    