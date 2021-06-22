/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.virna5.edge.EdgeDescriptor;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.windows.WindowManager;



public class Controler implements SignalListener{

    private static final Logger log = Logger.getLogger(Controler.class.getName());

    public static  MonitorTopComponent monitor; 
    private ControleTopComponent top;
    private Controler.SMThread service_thread;     
    private LinkedBlockingQueue<SMTraffic> smqueue;
    
    
    private final ScheduledExecutorService scheduler;
    
    
    //private LinkedHashMap<Long, SignalListener> eventlisteners;
    
    public OutHandler loghandler;
   
    private LinkedHashMap<String, BaseService> artifacts;
    private LinkedHashMap<String, BaseDescriptor> loaded_descriptors;
    
    private ContextPool  contextpool;
    
    private LinkedHashMap<String,String> modn;
    
    
    private static Controler instance; 
    public static Controler getInstance(){
        if (instance == null) {instance = new Controler();}
        return instance;
    }
   
    public Controler() {
        
        log.setLevel(Level.FINE);
        
        ContextUtils.loadConfig();
        
        contextpool = new ContextPool();
        
        artifacts = new LinkedHashMap<>();
        loaded_descriptors = new LinkedHashMap<>();
        
        smqueue = new LinkedBlockingQueue<>();
        service_thread = new SMThread(smqueue);
        addSignalListener(this);
        
        alarms = new LinkedHashMap<>();
        scheduler = Executors.newScheduledThreadPool(5);
        
        loghandler = OutHandler.getInstance();
        log.addHandler(loghandler);
    
        monitor = getMonitorCanvas();
      
    }
    
    
    public MonitorTopComponent getMonitorCanvas(){
        
        if (monitor == null){
            WindowManager wm = WindowManager.getDefault();
            Runnable worker = new Runnable() {
                public void run() {
                    MonitorTopComponent monitor_tc = (MonitorTopComponent)WindowManager.getDefault().findTopComponent("MonitorTopComponent");
                    monitor_tc.open();
                    Controler.monitor = monitor_tc;
                    //monitor.requestActive();  
                }
            };
            EventQueue.invokeLater(worker);        
        }
        return monitor;
    }
    
    
    public void setView(ControleTopComponent top){
        this.top = top;
        //top.addContextEventListener(this);
    }
    
    public ContextControl isContextLoaded(long uid){
        
        for (ContextControl cc : getContextpool()){
            if (cc.getContextid() == uid){
                return cc;
            }
        }
        return null;
    }
    
    
    // ================================================================ALARMS  =====================================================
    private LinkedHashMap<Integer, AlarmHandle> alarms;
    private static Integer alarmid = 1;
    
    
    public static Integer getAlarmID(){
        return alarmid++;
    }
    
    public boolean hasAlarmSet(Long uid){
        
        for (AlarmHandle al : alarms.values()){
            if (al.handleid == uid) return true; 
        }
        return false;
    }
    
    public void setAlarm (final Long addr, Integer id, final SMTraffic message, long init, long period){
        
        if (alarms.containsKey(id)){
            log.warning("Trying to set an already loaded alarm");
            return;
        }
        
        log.fine(String.format("Setting alarm to %d with id = %d", addr, id));
        
        final Runnable alarm = new Runnable() {
            
            public void run() {
                Controler al_ctrl = Controler.getInstance();
                al_ctrl.notifySignalListeners(addr, message);             
                log.finer(String.format("Alarm to addr %d : %s to %d",
                        addr,
                        message.getState().name(),
                        message.getHandle()
                )) ; 
            }
        };
        
        final ScheduledFuture<?> alarmhandle = scheduler.scheduleAtFixedRate(alarm, init, period, TimeUnit.MILLISECONDS); 
        
        alarms.put(id, new AlarmHandle(addr, 0l, alarmhandle));
    }
    
    private class AlarmHandle{
      
        public Long handleid;
        public Long context;
        
        public int type = 0;
        
        public ScheduledFuture<?> handle;

        public AlarmHandle(Long handleid, Long context, ScheduledFuture<?> handle) {
            this.handleid = handleid;
            this.context = context;
            this.handle = handle;
        }
        
    };
    
    
    public void removeAlarm(Integer id){
        
        log.fine(String.format("Renmoving alarm id = %d", id));
        AlarmHandle handle = alarms.get(id); 
        
        if (handle != null){
            handle.handle.cancel(true);
            alarms.remove(id);
        }
    }
    
    public void isAlarmSet (Long uid){
        
       
        
    }
    
    
    // ==============================================================================================================
    
    private ArrayList<String> scanDescriptorsDir (String dir){
    
        ArrayList<String> descriptors_payload = new ArrayList<>();
        Path path = Paths.get(dir);

        try (DirectoryStream<Path> ds = Files.newDirectoryStream(path)) {
            for (Path file : ds) {
                descriptors_payload.add(file.toAbsolutePath().toString());
            }
        }catch(IOException e) {
            System.err.println(e);
        }

        return descriptors_payload;
    }
    
 
    
    private String[] getInterfaceClassName(Long ctx, String id){
        
        String[] idfields = id.split("@");
        Long artifact_id = Long.parseLong(idfields[1]);
        String data[] = new String[3];
        data[0] = null;
         
        for (ContextControl cc : contextpool){
            RootDescriptor rd = cc.getDescriptor();
            for (BaseDescriptor bd: rd.getContextNodes()){
                if (Objects.equals(bd.getUID(), artifact_id)){
                    UIInterface[] ais = bd.getInterfaces();
                    if (ais.length > 0 ){
                        data[1] = bd.getName();
                        data[2] = rd.getDesc();
                        if (ais.length == 1 ){ 
                            data[0]= ais[0].getClass_name();
                        }
                        else{
                            for (UIInterface uii : ais){
                                if (uii.getDisplay_name().equals(idfields[0])){
                                    data[0] = uii.getClass_name();
                                }
                            }
                        }
                    }    
                }
            }
        }
        
        return data;
    }
    
   
    private BaseService getArtifact(String fqn){
    
        if (artifacts.containsKey(fqn)){
            log.info(String.format("Resource class %s is already loaded", fqn));
            return artifacts.get(fqn);
        }
        else{
            try {       
                Class clazz = Class.forName(fqn);
                BaseService bs = (BaseService)clazz.newInstance();          
                log.info(String.format("Resource class %s was registered", fqn));
                return bs;
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                log.info(String.format("Unable to load Resource class %s ", fqn));
            }
        }
        return null;
    }
    
    
    private void loadArtifacts (RootDescriptor rd, ContextControl cc){
        
        for (BaseDescriptor bd: rd.getContextNodes()){
            String[] deps = bd.getDependencies();
            if (deps.length !=0){
                for (String sdep : deps){
                    if (sdep.contains("Service")){
                        BaseService service = getArtifact(sdep);
                        artifacts.put(sdep, service);
                        loaded_descriptors.put(String.valueOf(bd.getUID()), bd);
                        bd.setService(service);
                        service.configService(bd);
                        cc.addMap(bd.getGraph_widget(), bd);
                        listeners.add(bd);  
                    }
                }
            }              
        }  
    }
    
    
    private void connectArtifacts(RootDescriptor rd, ContextControl cc){
        
        for (BaseDescriptor bd: rd.getContextNodes()){
            if (bd instanceof EdgeDescriptor){
                EdgeDescriptor ed = (EdgeDescriptor)bd;
                String source = ed.getSource_widget();
                BaseDescriptor source_descriptor = cc.getMap(source);
                
                String target = ed.getTarget_widget();
                BaseDescriptor target_descriptor = cc.getMap(target);
                
                source_descriptor.addSignalListener(target_descriptor);                         
            }               
        } 
    }
    
    private void activateTask (Long uid, boolean activate){
        
        //log.fine(String.format("task %d is %b", uid, activate ));
        Integer ac = activate ? 1 : 0;
        
        for (ContextControl cc : contextpool){
            if (cc.getContextid() == uid){
                RootDescriptor rd = cc.getDescriptor();
                cc.setRunning(activate);
                for (BaseDescriptor bd : rd.getContextNodes()){
                    if (!(bd instanceof EdgeDescriptor)){ 
                        bd.processSignal(new SMTraffic(bd.uid, bd.uid, ac, 
                                    VirnaServices.STATES.TSK_MANAGE, 
                                    null));
                    }
                }
            }         
        }
    }
    
     private void removeTask (ContextControl cc){
      
        RootDescriptor rd = cc.getDescriptor();
        for (BaseDescriptor bd : rd.getContextNodes()){
            if (!(bd instanceof EdgeDescriptor)){ 
                bd.processSignal(new SMTraffic(bd.uid, bd.uid, 0, 
                            VirnaServices.STATES.TSK_CLEAR, 
                            null));
            }
        }
    }
    
    
    
    public void addContext(String filename){
        
        ContextControl cc;
        
        try {       
            String json_out = ContextUtils.loadFile(filename);
            RootDescriptor lcd = loadContextDescriptor(json_out);
            if (lcd == null) return;
            
            long contextid = lcd.getContext();
            cc = isContextLoaded(contextid);
            if (cc == null){
                cc = new ContextControl(this);
                cc.loadDescriptor(lcd, filename);
                cc.setRefresh(true);
                contextpool.add(cc);
             
                RootDescriptor rd = cc.getDescriptor();
                
                loadArtifacts (rd, cc);
                connectArtifacts(rd, cc);           
                
                // Update UI table
//                Long uid = rd.getContext();                
//                String desc = rd.getDesc();
//                String owner = ContextUtils.OWNER;
//                String start = new Date().toString();
                String [] tools = cc.getInterfacesNames();
//                String obs = rd.getObs();  
                top.publishTask(rd.getContext(), 
                                rd.getName(), 
                                ContextUtils.OWNER, 
                                ContextUtils.getTimestamp(), 
                                tools, 
                                rd.getObs());      
                
                log.fine(String.format("Adding context %s to contextpool", filename));
                
            }
            else{
                //cc.loadDescriptor(lcd, filename);
                //cc.setRefresh(true);
                log.fine(String.format("Context %s is already in contextpool", filename));
            }     
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Unable to load file : {0}", ex.getMessage());
        }   
    }

    public RootDescriptor loadContextDescriptor(String payload){
        
        String temp;
        boolean abort = false;
        ArrayList<String> atempts = new ArrayList<>(); 
        
        GsonBuilder builder = new GsonBuilder(); 
        builder.registerTypeAdapter(ContextNodes.class, new ContextNodesDeserializer()); 
        builder.setPrettyPrinting(); 
        Gson gson = builder.create();
        
        while (!abort){
            try{
                RootDescriptor jctx = gson.fromJson(payload, RootDescriptor.class);
                //log.info("==== Context loaded");
                return jctx;
            } catch (JsonParseException ex) {
                
                String failed_class = ex.getMessage();
                for (String atempt : atempts){
                    if (atempt.equals(failed_class)){
                        log.info(String.format("==== Second atempt do load module %s", ex.getMessage()));
                        return null;
                    }
                }
                atempts.add(failed_class);
                log.info(String.format("Error loading descriptor: %s - trying to load module", ex.getMessage()));
                if (!loadModule(failed_class)){
                   log.info(String.format("==== Failed to load module %s - aborting", ex.getMessage()));
                   return null;
                }
                log.info(String.format("Module %s loaded - trying to load context again", ex.getMessage())) ;
            }
        }
        return null;
    } 
    
    private boolean loadModule( String mod){
        
        try {
            String flocator = modn.get(mod);
            if (flocator != null){
                File modjar = new File(flocator);
                URLClassLoader classLoader = new URLClassLoader(new URL[]{modjar.toURI().toURL()});
                Class clazz = classLoader.loadClass(mod);
                Object o = clazz.newInstance();
                
                String s = "";
                return true;
            }            
        } catch (MalformedURLException | ClassNotFoundException ex) {
           log.log(Level.SEVERE, "Falha na carga do modulo {0}", ex.getMessage());
           return false;
        } catch (InstantiationException ex) {
            Logger.getLogger(Controler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Controler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
   
    public ArrayList<ContextControl> getContextpool() {
        return contextpool;
    }

    
    // ===========SIGNAL HANDLING ===================================================================
        
    /** Estrutura para armazenamento dos listeners do dispositivo*/ 
    //private transient LinkedHashMap<Long,SignalListener> listeners = new LinkedHashMap<>();
    
    private transient ArrayList<SignalListener> listeners = new ArrayList<>();
    
    @Override
    public Long getContext() {
        return -1L;
    }
    
    /**
     * @return the uid
     */
    public Long getUID() {
        return -1L;
    }
    
    public void processSignal (SMTraffic signal){
        smqueue.add(signal);
    }
    
     /** Método de registro do listener do dispositivo serial */
    public void addSignalListener (SignalListener l){
        listeners.add(l);
    }

    /** Método de remoção do registro do listener do dispositivo serial */
    public void removeSignalListener (SignalListener l){
        listeners.remove(l);
    }

    /** Esse método é chamedo quando algo acontece no dispositivo */
    protected void notifySignalListeners(long uid_addr, SMTraffic signal) {

        if (!listeners.isEmpty()){      
            //log.fine("Notifying "+ uid_addr);
            for (SignalListener sl : listeners){
                if (sl.getUID() == uid_addr){
                    sl.processSignal(signal);
                }
            }
        }
    }
    

   
    // ================================================================================================================
    public void stopService(){
        //services.removeUsbServicesListener(this);
        service_thread.setDone(true);    
    }
    
    public void startService(){      
        if (service_thread.done == true){
            smqueue.clear();
            new Thread(service_thread).start();
        }
        
        //setAlarm (1000L, null, 1, 1);
//        setAlarm (2000L, null, 2, 2);
        
    }

    
    private class SMThread extends Thread {
    
        private VirnaServices.STATES state;
        private boolean done;
        protected BlockingQueue<SMTraffic> tqueue;
        private VirnaServices.CMDS cmd;
        private ArrayDeque <VirnaServices.STATES>states_stack;
        private SMTraffic smm;
        private VirnaPayload payload;
        
        private MonitorTopComponent mon;
        

        public SMThread(BlockingQueue<SMTraffic> tqueue) {
            this.tqueue = tqueue;
            states_stack = new ArrayDeque<>();
            states_stack.push(VirnaServices.STATES.RESET);
            setDone(true);
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
                            log.log(Level.FINE, "Controle iniciando Serviços");
                            
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
                            log.log(Level.FINE, "Controler em CONFIG");
                            break; 
                            
                        case RESET:
                            //log.log(Level.FINE, "FOB em RESET");
                            states_stack.push(VirnaServices.STATES.IDLE);
                            states_stack.push(VirnaServices.STATES.CONFIG);
                            states_stack.push(VirnaServices.STATES.INIT);
                            break;
                        
                        case CTRL_ADDALARM:
                            payload = smm.getPayload();
                            if (payload.objecttype.contains("SMTraffic")){
                                SMTraffic alm_traffic = (SMTraffic)payload.vobject; 
                                setAlarm (smm.getAddr(), smm.getCode(), alm_traffic, payload.long1, payload.long2);
                            }
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;   
                        
                        case CTRL_REMOVEALARM:
                            Integer rmid = smm.getCode();
                            removeAlarm(rmid);
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;     
                            
                        case CTRL_HOUSEKEEP:
                            payload = smm.getPayload();
                            //log.fine("Doing Housekeep...");
                            
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;    
                        
                        case CTRL_AUTOLOAD:
                            try{
                                //addContext("/Bascon/BSW1/Testbench/Ctx/task6.tsk");
                                addContext("/Bascon/BSW1/Testbench/Ctx/task11.tsk");
                                
                                
                            } catch (Exception ex) {
                                NotifyDescriptor nd = new NotifyDescriptor.Message(
                                    "<html>Erro na carga da tarefa, verifique se : "
                                  + "<ul>"
                                  + "<li>Há permissões de leitura no diretório e no arquivo</li>"
                                  + "<li>Esse é realmente um arquivo de descrição de tarefas (.tsk).</li>"
                                  + "</ul>"
                                  + "</html>", 
                                    NotifyDescriptor.ERROR_MESSAGE);
                                    Object retval = DialogDisplayer.getDefault().notify(nd);
                            }                          
                            states_stack.push(VirnaServices.STATES.IDLE);
                            
                            break;
                        
                        case CTRL_LOADTASK:
                            log.log(Level.FINE, "Loading Task");
                            String s = ContextUtils.selectFile(false, ContextUtils.TASKSDIR, "tsk");
                            if (s !=null){
                                try{
                                    addContext(s);
                                } catch (Exception ex) {
                                    NotifyDescriptor nd = new NotifyDescriptor.Message(
                                        "<html>Erro na carga da tarefa, verifique se : "
                                      + "<ul>"
                                      + "<li>Há permissões de leitura no diretório e no arquivo</li>"
                                      + "<li>Esse é realmente um arquivo de descrição de tarefas (.tsk).</li>"
                                      + "</ul>"
                                      + "</html>", 
                                        NotifyDescriptor.ERROR_MESSAGE);
                                        Object retval = DialogDisplayer.getDefault().notify(nd);
                                    }
                            }                          
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;
                            
                        case CTRL_ACTIVATETASK:
                            activateTask(smm.getHandle(), smm.getCode() == 1);
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;    

                        case CTRL_REMOVETASK:
                            log.log(Level.FINE, "Removing Task");
                            VirnaPayload pld = smm.getPayload();
                            Long uid = pld.long1;
                            int selected = pld.int1;
                            
                            for (ContextControl cc : contextpool){
                                if (cc.getContextid() == uid){
                                    if(cc.isRunning()){
                                        NotifyDescriptor nd = new NotifyDescriptor.Message(
                                        "<html><h3 style=\"color:red;\">A Tarefa está em execução. Desative-a primeiro por favor.</h2></html>", 
                                        NotifyDescriptor.ERROR_MESSAGE);
                                        nd.setTitle("Ooops !! - Mensagem do Gerente de Processos");
                                        Object retval = DialogDisplayer.getDefault().notify(nd); 
                                    }
                                    else{
                                        removeTask (cc);
                                        //contextpool.remove(cc);
                                        top.removeTask(uid, selected);
                                    }
                                }
                            }
                            
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;   
                            
                            
                        case CTRL_LOADMONITOR:
                            String artifact = smm.getPayload().vstring;  
                            mon = getMonitorCanvas();          
                            if (mon.isLoaded(artifact)){
                                NotifyDescriptor nd = new NotifyDescriptor.Message(
                                        "<html><h2 style=\"color:red;\">A ferramenta já está carregada...</h2></html>", 
                                        NotifyDescriptor.ERROR_MESSAGE);
                                nd.setTitle("Ooops !! - Mensagem do Gerente de Processos");
                                Object retval = DialogDisplayer.getDefault().notify(nd); 
                            }
                            else{
                                String[] cn = getInterfaceClassName(smm.getPayload().long1, artifact);
                                if (cn[0] !=null){
                                    String desc_uid = artifact.split("@")[1];
                                    BaseDescriptor bd = loaded_descriptors.get(desc_uid);
                                    if (bd != null){                                          
                                        mon.addIFrame(cn, bd, artifact);
                                    }
                                } 
                            }
                            log.log(Level.FINE, "Loading tool :"+ smm.getPayload().vstring);
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;
                        
                        default:
                            log.warning("Undefined state on Controler : " + smm.getState().toString());
                            states_stack.push(VirnaServices.STATES.IDLE);
                            break;      
 
                    }
                }
            } catch (Exception ex) {
                log.log(Level.WARNING, "Falha na maquina de estados do Controle : " + ex.toString());
                ex.printStackTrace();
                NotifyDescriptor nd = new NotifyDescriptor.Message(
                                        "<html><h3 style=\"color:red;\">Erro na maquina de estados no modulo controle :</h3></html>", 
                                        NotifyDescriptor.ERROR_MESSAGE);
                                nd.setTitle("Ooops !! - Mensagem do Gerente de Processos");
                                
                                Object retval = DialogDisplayer.getDefault().notify(nd); 
            }

        }

        public void setDone(boolean done) {
            if (done) log.log(Level.FINE, "CTRL está paralizando Serviços");
            this.done = done;
        }   
    };

    
    
    
    
    
}



   
//    public void loadSystem(){
//
//        String basedir = ContextUtils.CONTEXTDIR + "Ctx";
//        
//        //loadModule("com.virna5.fileobserver.FileObserverDescriptor");
// 
//        ArrayList<String> descriptors_payload = scanDescriptorsDir(basedir);
//       
//        // Load available Descriptoers from the disk
//        for (String dpld : descriptors_payload){
//            addContext(dpld);
//        }
//        log.info(String.format("Loaded %d contexts from disk", contextpool.size()));
//        
//        for (ContextControl cc : contextpool){          
//            RootDescriptor cd = cc.getDescriptor();
//            if (loadNodes(cd.getContextNodes())){
//                
//            }        
//        }
//        log.info(String.format("All contexts were loaded ..."));
//    }
    
  
    
//    private boolean loadNodes(ContextNodes cn){
//        
//        for (BaseDescriptor bd : cn){
//            String[] dependencies = bd.getDependencies();
//            for (String dep : dependencies){
//                BaseService bs = getArtifact(dep);
//                if (bs != null){
//                    //updateServiceData(bs, bd);   
//                }
//                else{
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
    