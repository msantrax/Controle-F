/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.csvfilter;

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


public class CSVFilterService extends BaseService {

    private static final Logger log = Logger.getLogger(CSVFilterService.class.getName());
    private CSVFilterService.SMThread service_thread;    
   
    protected LinkedBlockingQueue<SMTraffic> smqueue;
    
    
    private static CSVFilterService instance;    
    public static CSVFilterService getInstance(){
        if (instance == null) {instance = new CSVFilterService();}
        return instance;
    }
    public CSVFilterService getDefault() { return instance; }
    
  
    public CSVFilterService() {
        
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
 
    
    
    @Override
    public void processSignal (SMTraffic signal, BaseDescriptor bd){
        
        smqueue.add(signal);
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

//            log.info("========== JSON : ==================\n\r");
//            log.info(sjson);
//            log.info(String.format("Json parser loaded %d chars", sjson.length()));
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


// 
//    @Override
//    public void UpdateUI (String mes, String bduid){
//        
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
//    }