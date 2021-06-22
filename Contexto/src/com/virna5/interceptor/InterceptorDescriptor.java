/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.interceptor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.ContextUtils;
import com.virna5.contexto.DescriptorConnector;
import com.virna5.contexto.UIInterface;
import java.beans.IntrospectionException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.openide.util.Exceptions;


public class InterceptorDescriptor extends BaseDescriptor{
    
    private InterceptorTemplate interceptor;
    
    private transient InterceptModel model;
    private boolean expert = false;
    
    
    public InterceptorDescriptor() {
        
        super();
        dependencies = new String[] { "com.virna5.interceptor.InterceptorService" };
        interfaces = new UIInterface[] { new UIInterface("Interceptor de resultados", "com.virna5.interceptor.InterceptorIFrame", "iframe") };
            
        name="Interceptor de Resultados";
        desc = "Dispositivo de tratamento de resultados";
        
        nodetype = "interceptor.InterceptorDescriptor";
        version = "1.0.0";
        
        //model = new InterceptModel(interceptor);
    }

    @Override
    public DescriptorConnector buildConnector(){
        
        InterceptorConnector foc = new InterceptorConnector();
        try {
            foc.setNode(new InterceptorNode(this));
            foc.setID(ContextUtils.getUID());
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return foc;
        
    }

    public void initModel(){
        setModel(new InterceptModel(interceptor));
    }
    
    
    public void buildDefaults(){
   
        String skull = "";
        
        InputStream is = InterceptorIFrame.class.getResourceAsStream("/com/virna5/interceptor/DefaultSkull.tmpl");
        if (is != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            skull = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
   
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson gson = builder.create();
           
            interceptor = gson.fromJson(skull,  InterceptorTemplate.class);
            
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
  
    }
    
    
    /**
     * @return the interceptor
     */
    public InterceptorTemplate getInterceptor() {
        return interceptor;
    }

    /**
     * @param interceptor the interceptor to set
     */
    public void setInterceptor(InterceptorTemplate interceptor) {
        this.interceptor = interceptor;
    }

    /**
     * @return the header_items
     */
    public ArrayList<ItemDescriptor> getHeader_items() {
        return interceptor.getHeader_items();
    }

    /**
     * @param header_items the header_items to set
     */
    public void setHeader_items(ArrayList<ItemDescriptor> header_items) {
        this.interceptor.setHeader_items(header_items);
    }

    /**
     * @return the alloyfilters
     */
    public AlloyFilters getAlloyfilters() {
        return interceptor.getAlloyfilters();
    }

    /**
     * @param alloyfilters the alloyfilters to set
     */
    public void setAlloyfilters(AlloyFilters alloyfilters) {
        //this.interceptor. = alloyfilters;
    }

    /**
     * @return the expert
     */
    public boolean isExpert() {
        return expert;
    }

    /**
     * @param expert the expert to set
     */
    public void setExpert(boolean expert) {
        this.expert = expert;
    }

    /**
     * @return the model
     */
    public InterceptModel getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(InterceptModel model) {
        this.model = model;
    }
    
    
    
    
}
