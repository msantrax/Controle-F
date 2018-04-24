/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.edge;

import com.virna5.fileobserver.*;
import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.ContextUtils;
import com.virna5.contexto.DescriptorConnector;
import java.beans.IntrospectionException;
import org.openide.util.Exceptions;



/**
 *
 * @author opus
 */
public class EdgeDescriptor extends BaseDescriptor{
    
    private Double sourcept_x;
    private Double sourcept_y;
    private Double targetpt_x;
    private Double targetpt_y;
    
    private String source_widget;
    private String target_widget;
    
    

    public EdgeDescriptor() {
        
        super();
        
        dependencies = new String[] {};
        
        name="Conector";
        desc = "Conector para transmissão de dados e comandos";
        
        nodetype = "edge.EdgeDescriptor";
        version = "1.0.0";
            
    }

    @Override
    public DescriptorConnector buildConnector(){
        
        EdgeConnector foc = new EdgeConnector();
        try {
            foc.setNode(new EdgeNode(this));
            foc.setID(ContextUtils.getUID());
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return foc;
    }
 
    
    /**
     * @return the sourcept_x
     */
    public Double getSourcept_x() {
        return sourcept_x;
    }

    /**
     * @param sourcept_x the sourcept_x to set
     */
    public void setSourcept_x(Double sourcept_x) {
        this.sourcept_x = sourcept_x;
    }

    /**
     * @return the sourcept_y
     */
    public Double getSourcept_y() {
        return sourcept_y;
    }

    /**
     * @param sourcept_y the sourcept_y to set
     */
    public void setSourcept_y(Double sourcept_y) {
        this.sourcept_y = sourcept_y;
    }

    /**
     * @return the targetpt_x
     */
    public Double getTargetpt_x() {
        return targetpt_x;
    }

    /**
     * @param targetpt_x the targetpt_x to set
     */
    public void setTargetpt_x(Double targetpt_x) {
        this.targetpt_x = targetpt_x;
    }

    /**
     * @return the targetpt_y
     */
    public Double getTargetpt_y() {
        return targetpt_y;
    }

    /**
     * @param targetpt_y the targetpt_y to set
     */
    public void setTargetpt_y(Double targetpt_y) {
        this.targetpt_y = targetpt_y;
    }

    /**
     * @return the source_widget
     */
    public String getSource_widget() {
        return source_widget;
    }

    /**
     * @param source_widget the source_widget to set
     */
    public void setSource_widget(String source_widget) {
        this.source_widget = source_widget;
    }

    /**
     * @return the target_widget
     */
    public String getTarget_widget() {
        return target_widget;
    }

    /**
     * @param target_widget the target_widget to set
     */
    public void setTarget_widget(String target_widget) {
        this.target_widget = target_widget;
    }

    
    
}
