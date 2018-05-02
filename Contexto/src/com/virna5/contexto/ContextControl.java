/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

/**
 *
 * @author opus
 */
public class ContextControl {

    private static final Logger log = Logger.getLogger(ContextControl.class.getName());

    private Controler ctrl;
    private RootDescriptor descriptor;
    private long contextid = 0L;
    private long load_tm = 0L;
    
    private String descriptorfile;
    
    private boolean running = false;
    private boolean loaded = false;
    private boolean refresh = true;
    
    private LinkedHashMap<String, BaseDescriptor>  signalmap;
    
    
    public ContextControl(Controler ctrl) {
        this.ctrl = ctrl;
        signalmap = new LinkedHashMap<>();
    }
    
    public void loadDescriptor(RootDescriptor cd, String filename){
        
        this.descriptor = cd;
        this.contextid = cd.getContext();
        this.descriptorfile = filename;
        this.load_tm = System.currentTimeMillis();
        this.descriptorfile = filename;
    }

    
    public String[] getInterfacesNames(){
        
        ArrayList<String> intnames = new ArrayList<>();
        intnames.add("Selecione uma ferramenta");
        
        ContextNodes cn = descriptor.getContextNodes();
        for (BaseDescriptor bd : cn){
            UIInterface[] uiis = bd.getInterfaces();
            if (uiis != null){
                for (UIInterface uii: uiis){
                    intnames.add(uii.getDisplay_name()+"@"+bd.getUID());
                }
            }
        }
        String[] out = new String[intnames.size()];
        intnames.toArray(out);
        
        return out;
    }
    
    public void addMap (String widgetid, BaseDescriptor descriptor){
        signalmap.put(widgetid, descriptor);
    }
    
    public BaseDescriptor getMap(String widgetid){
        return signalmap.get(widgetid);
    }
    
    
    /**
     * @return the descriptor
     */
    public RootDescriptor getDescriptor() {
        return descriptor;
    }

    /**
     * @return the contextid
     */
    public long getContextid() {
        return contextid;
    }

    /**
     * @return the load_tm
     */
    public long getLoad_tm() {
        return load_tm;
    }

    /**
     * @return the running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * @param running the running to set
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * @return the loaded
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * @param loaded the loaded to set
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    /**
     * @return the refresh
     */
    public boolean isRefresh() {
        return refresh;
    }

    /**
     * @param refresh the refresh to set
     */
    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }
    
    
    
}
