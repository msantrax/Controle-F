/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

import static com.virna5.contexto.ContextUtils.datefullFormat;
import com.virna5.fileobserver.FileObserverConnector;
import com.virna5.fileobserver.FileObserverNode;
import java.beans.IntrospectionException;
import java.util.ArrayList;
import org.openide.util.Exceptions;


public class BaseDescriptor {

    
    protected String nodetype;
    protected String version;
    
    private long context;
   
    protected Long uid;
    
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
    
    public String[] dependencies;

    
    public BaseDescriptor() {
        
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
   
    /**
     * @return the context
     */
    public long getContext() {
        return context;
    }

    /**
     * @param context the context to set
     */
    public void setContext(long context) {
        this.context = context;
    }

    /**
     * @return the uid
     */
    public Long getUID() {
        return uid;
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
    
}
