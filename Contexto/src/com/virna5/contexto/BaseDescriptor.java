/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

import static com.virna5.contexto.ContextUtils.datefullFormat;
import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Exceptions;


public class BaseDescriptor implements SignalListener{

    private static final Logger log = Logger.getLogger(BaseDescriptor.class.getName());

    
    protected String nodetype;
    protected String version; 
    private Long context;
    protected Long uid;
    //private LinkedHashMap<Long, ContextEventListener> eventlisteners;
    
    protected transient BaseService service;
    
    protected String name;
    protected String desc;
    
    private String designer;
    private String owner;
    private Long created;
    private Long loaded;
    
    private String graph_widget;
    
    private Double xpos;
    private Double ypos;
    private Double width;
    private Double height;
    
    protected String style;
    
    protected String observ;
    
    protected String[] dependencies;
    protected UIInterface[] interfaces;
    
    private transient Integer alarm_handle = 0;

    protected Boolean autoloadui = true;
    protected Boolean iconifyui = false;
    protected Integer uilandx = 100;
    protected Integer uilandy = 100;

    

    
    public BaseDescriptor() {
        
        log.setLevel(Level.FINE);
        
        //eventlisteners =  new LinkedHashMap<>();
        
        nodetype = "contexto.BaseDescriptor";
        version = "1.0.0";
        
        this.uid = 1002l;
        this.name="Descritor base";
        this.desc = "Estrutura base - (TODO) não deveria estar sendo usada !";
        this.designer = "msantos";
        this.owner = "BSW";
        this.created = System.currentTimeMillis();
        this.loaded = System.currentTimeMillis();
        this.observ = "Observações gerais";
    }
 
    public DescriptorConnector buildConnector(){
        
        DescriptorConnector foc = new DescriptorConnector();
        try {
            foc.setNode(new DescriptorNode(this));
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return foc;
    }
   
   // ===========SIGNAL HANDLING ===================================================================
        
    /** Estrutura para armazenamento dos listeners do dispositivo*/ 
    //private transient LinkedHashMap<Long,SignalListener> listeners = new LinkedHashMap<>();
    
    private transient ArrayList<SignalListener> listeners = new ArrayList<>();
    
    @Override
    public Long getContext() {
        return context;
    }
    
    /**
     * @return the uid
     */
    public Long getUID() {
        return uid;
    }
    
    public void processSignal (SMTraffic signal){
        //String mstate = signal.getState().toString();
        //log.info(String.format("Processing message of type %s to %d @ %s ", mstate, signal.handle, this.toString()));
        service.processSignal(signal, this);
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
    public void notifySignalListeners(long uid, SMTraffic signal) {

        if (!listeners.isEmpty()){      
            //log.fine("Notifying "+ context);
            for (SignalListener sl : listeners){
                if (sl.getUID() == uid || uid == 0){
                    signal.setHandle(sl.getUID());
                    sl.processSignal(signal.clone());
                }
            }
        }
    }
    
    

    /**
     * @param context the context to set
     */
    public void setContext(long context) {
        this.context = context;
    }

    
    /**
     * @param uid the uid to set
     */
    public void setUID(Long uid) {
        this.uid = uid;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @return the designer
     */
    public String getDesigner() {
        return designer;
    }

    /**
     * @param designer the designer to set
     */
    public void setDesigner(String designer) {
        this.designer = designer;
    }

    /**
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @return the created
     */
    public Long getCreated() {
        return created;
    }

    public String getCreatedTime(){
        return datefullFormat.format(created);
    }
    
    public String getLoadedTime(){
        return datefullFormat.format(loaded);
    }
    
    /**
     * @param created the created to set
     */
    public void setCreated(Long created) {
        this.created = created;
    }

    /**
     * @return the loaded
     */
    public Long getLoaded() {
        return loaded;
    }

    /**
     * @param loaded the loaded to set
     */
    public void setLoaded(Long loaded) {
        this.loaded = loaded;
    }

    /**
     * @return the graph_widget
     */
    public String getGraph_widget() {
        return graph_widget;
    }

    /**
     * @param graph_widget the graph_widget to set
     */
    public void setGraph_widget(String graph_widget) {
        this.graph_widget = graph_widget;
    }

    /**
     * @return the xpos
     */
    public Double getXpos() {
        return xpos;
    }

    /**
     * @param xpos the xpos to set
     */
    public void setXpos(Double xpos) {
        this.xpos = xpos;
    }

    /**
     * @return the ypos
     */
    public Double getYpos() {
        return ypos;
    }

    /**
     * @param ypos the ypos to set
     */
    public void setYpos(Double ypos) {
        this.ypos = ypos;
    }

    /**
     * @return the width
     */
    public Double getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(Double width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public Double getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(Double height) {
        this.height = height;
    }

    public void setPosition(Double x, Double y){
        this.xpos = x;
        this.ypos = y;
    }
    
    public void setDimension(Double w, Double h){
        this.width = w;
        this.height = h;
    }

    /**
     * @return the nodetype
     */
    public String getNodetype() {
        return nodetype;
    }

    /**
     * @param nodetype the nodetype to set
     */
    public void setNodetype(String nodetype) {
        this.nodetype = nodetype;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

   
    /**
     * @return the style
     */
    public String getStyle() {
        return style;
    }

    /**
     * @param style the style to set
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * @return the observ
     */
    public String getObserv() {
        return observ;
    }

    /**
     * @param observ the observ to set
     */
    public void setObserv(String observ) {
        this.observ = observ;
    }

    
    /**
     * @return the dependencies
     */
    public String[] getDependencies() {
        return dependencies;
    }

    /**
     * @return the interfaces
     */
    public UIInterface[] getInterfaces() {
        return interfaces;
    }

    /**
     * @return the service
     */
    public BaseService getService() {
        return service;
    }

    /**
     * @param service the service to set
     */
    public void setService(BaseService service) {
        this.service = service;
    }

    /**
     * @return the alarm_handle
     */
    public Integer getAlarm_handle() {
        return alarm_handle;
    }

    /**
     * @param alarm_handle the alarm_handle to set
     */
    public void setAlarm_handle(Integer alarm_handle) {
        this.alarm_handle = alarm_handle;
    }

    
    public void setAutoloadui(Boolean autoloadui) {
        this.autoloadui = autoloadui;
    }

    public Boolean getAutoloadui() {
        return autoloadui;
    }

    

    public Integer getUilandx() {
        return uilandx;
    }

    public void setUilandx(Integer uilandx) {
        this.uilandx = uilandx;
    }

    public Integer getUilandy() {
        return uilandy;
    }

    public void setUilandy(Integer uilandy) {
        this.uilandy = uilandy;
    }

    public Boolean getIconifyui() {
        return iconifyui;
    }

    public void setIconifyui(Boolean iconifyui) {
        this.iconifyui = iconifyui;
    }

    
    
}
