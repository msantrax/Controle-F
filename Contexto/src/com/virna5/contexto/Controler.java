/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Controler {

    private static final Logger log = Logger.getLogger(Controler.class.getName());

    private static Controler instance; 
    private ControleTopComponent top;
     
    private LinkedBlockingQueue<SMTraffic> smqueue;
    
    private PrintWriter report_data;
    private PrintWriter report_error;
    public OutHandler loghandler;
   
    private LinkedHashMap<String, BaseService> artifacts;
    
    private ArrayList<ContextControl> contextpool;
    
    private String descriptorsWatchPath;
    
    private LinkedHashMap<String,String> modn;
    
    public static Controler getInstance(){
        if (instance == null) {instance = new Controler();}
        return instance;
    }
   
    public Controler() {
        
        log.setLevel(Level.ALL);
        contextpool = new ArrayList<>();
        
//        modn = new LinkedHashMap<>();
//        modn.put("com.virna5.fileobserver.FileObserverDescriptor", 
//                    "/Bascon/BSW1/Sandbox/Controle-D/build/cluster/modules/com-virna5-fileobserver.jar");
//        
//        artifacts = new LinkedHashMap<>();
//        log.addHandler(OutHandler.getInstance());

    }
    
    public void setView(ControleTopComponent top){
        this.top = top;
    }
    
    
    public ContextControl isContextLoaded(long uid){
        
        for (ContextControl cc : getContextpool()){
            if (cc.getContextid() == uid){
                return cc;
            }
        }
        return null;
    }
    
    
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
    
    
    public void loadSystem(){

        String basedir = ContextUtils.CONTEXTDIR + "Ctx";
        
        //loadModule("com.virna5.fileobserver.FileObserverDescriptor");
 
        ArrayList<String> descriptors_payload = scanDescriptorsDir(basedir);
       
        // Load available Descriptoers from the disk
        for (String dpld : descriptors_payload){
            addContext(dpld);
        }
        log.info(String.format("Loaded %d contexts from disk", contextpool.size()));
        
        for (ContextControl cc : contextpool){          
            RootDescriptor cd = cc.getDescriptor();
            if (loadNodes(cd.getContextNodes())){
                
            }        
        }
        log.info(String.format("All contexts were loaded ..."));
    }
    
  
    
    private boolean loadNodes(ContextNodes cn){
        
        for (BaseDescriptor bd : cn){
            String[] dependencies = bd.dependencies;
            for (String dep : dependencies){
                BaseService bs = getArtifact(dep);
                if (bs != null){
                    updateServiceData(bs, bd);   
                }
                else{
                    return false;
                }
            }
        }
        return true;
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
                artifacts.put(fqn, bs);
                log.info(String.format("Resource class %s was registered", fqn));
                return bs;
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                log.info(String.format("Unable to load Resource class %s ", fqn));
            }
        }
        return null;
    }
    
    private void updateServiceData(BaseService bs, BaseDescriptor bd){
        //FileObserverService fos = (FileObserverService) bs;
        bs.configService(bd);  
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
                log.fine(String.format("Adding context %s to contextpool", filename));
            }
            else{
                cc.loadDescriptor(lcd, filename);
                cc.setRefresh(true);
                log.fine(String.format("Context %s is already in contextpool, updating descriptor", filename));
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

    public String getDescriptorsWatchPath() {
        return descriptorsWatchPath;
    }

    public void setDescriptorsWatchPath(String descriptorsWatchPath) {
        this.descriptorsWatchPath = descriptorsWatchPath;
    }
    
}
