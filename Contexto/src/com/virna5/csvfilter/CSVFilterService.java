/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.csvfilter;

import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.BaseService;
import com.virna5.contexto.ContextUtils;
import com.virna5.contexto.MonitorIFrameInterface;
import com.virna5.contexto.SMTraffic;
import com.virna5.contexto.VirnaPayload;
import com.virna5.contexto.VirnaServices;
import com.virna5.filewriter.FileWriterUIUpdater;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CSVFilterService extends BaseService {

    private static final Logger log = Logger.getLogger(CSVFilterService.class.getName());
    private CSVFilterService.SMThread service_thread;    
    private Long current_handle;
    
    
    private static CSVFilterService instance;    
    public static CSVFilterService getInstance(){
        if (instance == null) {instance = new CSVFilterService();}
        return instance;
    }
    public CSVFilterService getDefault() { return instance; }
    
    
    // ========================================================================
    private ArrayList<String> input_fields;
    
    public String rawpayload;
    
    public CSVFilterDescriptor csvd;
    
    public ArrayList<String> lines;
    
    public ArrayList<CSVField> csvfields;
    
    
    public CSVFilterService() {
        
        super();
       
        lines = new ArrayList<>();
        csvfields = new ArrayList<>();
     
        startService();
        
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
    
    
    public void updateDescriptor(CSVFilterDescriptor csvfd){
             
        CSVFieldsWrapper dfields = csvfd.getCsvfields();      
        //ArrayList<CSVField> dfields = csvfd.getCSVfields();      
        for (CSVField csvf : csvfields){
            dfields.add(csvf);
        }
        
    }
    
    
    @Override
    public void UpdateUI (String mes, String bduid){
        
//        com.virna5.filewriter.MonitorIFrame liframe = (com.virna5.filewriter.MonitorIFrame) getIFrame(bduid);
//        FileWriterDescriptor fwd = (FileWriterDescriptor)descriptors.get(Long.parseLong(bduid));
//        
//        if ( liframe != null){
//            if (mes == null){
//                FileWriterUIUpdater fwuiup = new FileWriterUIUpdater();
//                liframe.setWriteFile(fwd.getOutputfile());
//                
//                log.fine("Init UI em CSV Filter");
//            }           
//        }
    }
       
    
    
    
    
    
    
    // ================================================================================================================
    private void stopService(){
        //services.removeUsbServicesListener(this);
        service_thread.setDone(true);    
    }
    
    private void startService(){      
        smqueue = new LinkedBlockingQueue<>() ;
        service_thread = new CSVFilterService.SMThread(smqueue);
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
        CSVFilterDescriptor csvdesc;
        

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
                        
                        case CSV_CONVERT:
                            VirnaPayload vp = smm.getPayload();
                            String payload = vp.vstring;
                            
//                            com.virna5.filewriter.MonitorIFrame liframe = 
//                                    (com.virna5.filewriter.MonitorIFrame) getIFrame(String.valueOf(smm.getHandle()));
//                            
//                            if (liframe != null){
//                                liframe.updateUIDirect(new FileWriterUIUpdater()
//                                                            .setLedcolor(MonitorIFrameInterface.LED_GREEN_ON)
//                                                        );
//                                //csvdesc = liframe.getDescriptor();
//                                try{
//                                    ContextUtils.saveFile(fwdesc.getOutputfile(), payload);
//                                    log.fine(String.format("Wrote file %s with :\n\r%s",fwdesc.getOutputfile(), payload));
//                                    liframe.updateUIDirect(new FileWriterUIUpdater()
//                                                            .setLedcolor(MonitorIFrameInterface.LED_GREEN_OFF)
//                                                            .setPayload(payload)
//                                                        );
//                                }catch(Exception ex){
//                                    log.warning(String.format("Failed to write %s due %s", fwdesc.getOutputfile(), ex.toString()));
//                                    liframe.updateUIDirect(new FileWriterUIUpdater()
//                                                            .setLedcolor(MonitorIFrameInterface.LED_RED)
//                                                            .setPayload(ex.toString())
//                                                        );
//                                }
//                            }
                                                
                            
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

//
//
// public void buildHeader(){
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