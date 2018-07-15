/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.interceptor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.virna5.contexto.ContextUtils;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import org.openide.util.Exceptions;

/**
 *
 * @author opus
 */
public class InterceptModel {

    private static final Logger LOG = Logger.getLogger(InterceptModel.class.getName());
    
    public static enum QUALIFYTYPES {NONE,ACCEPT,INDEX,NOTNULL,RANGE,CONTAINS,MATCH,OK,BAD}
    

    public final static ImageIcon ICON_OK = new ImageIcon(InterceptorIFrame.class.getResource("/com/virna5/interceptor/ok.png")); 
    public final static ImageIcon ICON_NONE = new ImageIcon(InterceptorIFrame.class.getResource("/com/virna5/interceptor/apply.png"));
    public final static ImageIcon ICON_BAD = new ImageIcon(InterceptorIFrame.class.getResource("/com/virna5/interceptor/no.png"));
    public final static ImageIcon ICON_QUESTION = new ImageIcon(InterceptorIFrame.class.getResource("/com/virna5/interceptor/help.png"));
    public final static ImageIcon ICON_FILTER = new ImageIcon(InterceptorIFrame.class.getResource("/com/virna5/interceptor/filter.png"));
    public final static ImageIcon ICON_INDEX = new ImageIcon(InterceptorIFrame.class.getResource("/com/virna5/interceptor/window_list.png"));
    public final static ImageIcon ICON_ABOVE = new ImageIcon(InterceptorIFrame.class.getResource("/com/virna5/interceptor/above.png"));
    public final static ImageIcon ICON_BELOW = new ImageIcon(InterceptorIFrame.class.getResource("/com/virna5/interceptor/below.png"));

    
    private ResultRecord rr; 
    private InterceptorTemplate master_interceptor_template;
    private InterceptorTemplate cit;
    private String indexid;
   
    private int indexgroup;
    private List<LinkedHashMap<String,InterceptorTemplate>> indexes;
    
    private LinkedHashMap<String,InterceptorTemplate> holdruns;
    private LinkedHashMap<String,InterceptorTemplate> validatedruns;
    private LinkedHashMap<String,InterceptorTemplate> defaultruns;
   
    private InterceptorIFrame view;
     
    public InterceptModel(InterceptorIFrame _view) {
        this.view = _view;
        initModel(); 
    }
    
    public InterceptModel() {
        this.view = null;
        initModel(); 
    }
    
     public InterceptModel(InterceptorTemplate it) {
        
         this.view = null;
        
        holdruns = new LinkedHashMap<>();
        validatedruns = new LinkedHashMap<>();
        defaultruns = new LinkedHashMap<>();
        
        indexes = new ArrayList<>();
        indexes.add(defaultruns);
        indexes.add(validatedruns);
        indexes.add(holdruns);
        indexgroup = 3;
        
        master_interceptor_template = it;
        
        for (ItemDescriptor id : master_interceptor_template.getHeader_items()){
            id.setValue(null);
            if (id.getQualify_type() == null){
                id.setQualify_type(QUALIFYTYPES.NONE.toString());
                id.setValid(true);
            }
            else{
                if(id.getQualify_type().equals(QUALIFYTYPES.NONE.toString())){
                    id.setValid(true);                        
                }
                else{
                    id.setValid(false);
                }
            }                
        }

        for (String filtertag : master_interceptor_template.getAlloyfilters().getFilterTags()){
            clearValueItems(filtertag);
        }           
        master_interceptor_template.locateFilterDescriptors();
        cit = master_interceptor_template;    
    }
    
    
    private void initModel(){
        
        holdruns = new LinkedHashMap<>();
        validatedruns = new LinkedHashMap<>();
        defaultruns = new LinkedHashMap<>();
        
        indexes = new ArrayList<>();
        indexes.add(defaultruns);
        indexes.add(validatedruns);
        indexes.add(holdruns);
        indexgroup = 3;
      
        
        if ( loadTemplate(ContextUtils.CONTEXTDIR+ContextUtils.file_separator+"default_interceptor.tmpl")){  
        //if ( loadTemplate("/Bascon/BSW1/Testbench/area3/record6.tmpl")){  
            
            for (ItemDescriptor id : master_interceptor_template.getHeader_items()){
                id.setValue(null);
                if (id.getQualify_type() == null){
                    id.setQualify_type(QUALIFYTYPES.NONE.toString());
                    id.setValid(true);
                }
                else{
                    if(id.getQualify_type().equals(QUALIFYTYPES.NONE.toString())){
                        id.setValid(true);                        
                    }
                    else{
                        id.setValid(false);
                    }
                }                
            }
            
            for (String filtertag : master_interceptor_template.getAlloyfilters().getFilterTags()){
                clearValueItems(filtertag);
            }           
            master_interceptor_template.locateFilterDescriptors();
            cit = master_interceptor_template;           
        }
//        AlloyFilters alfts = master_interceptor_template.getAlloyfilters();
//        alfts.addFilter(master_interceptor_template.getValue_items(), "default");
    }
    
    public boolean loadTemplate(String file){
        
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson gson = builder.create();
            
            String payload = ContextUtils.loadFile (file);
            master_interceptor_template = gson.fromJson(payload,  InterceptorTemplate.class);
            LOG.info("Template was loaded from disk ...");
            return true;
            
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return false;
    }
    
    public void saveTemplate(String filepath){
        
        String sjson;
        
        GsonBuilder builder = new GsonBuilder(); 
        builder.setPrettyPrinting(); 
        Gson gson = builder.create();
        sjson = gson.toJson(master_interceptor_template);
        
        LOG.info("========== JSON : ==================\n\r");
        LOG.info(sjson);
        LOG.info(String.format("Json parser loaded %d chars", sjson.length()));
        
        try {
            ContextUtils.saveFile(filepath, sjson);
        } catch (IOException ex) {
            LOG.severe("Failed to save record template : " + ex.toString());
        }
  
    }
    
    
    
    public void setView (InterceptorIFrame _view) {
        this.view = _view;
    }
       
    
    private void clearValueItems(String alloy){
        
        ArrayList<ItemDescriptor> ids = master_interceptor_template.getAlloyfilters().locateFilter(alloy).getItems();
        for (ItemDescriptor id : ids){
            id.setValue(null);
            if (id.getQualify_type() == null){
                id.setQualify_type(QUALIFYTYPES.NONE.toString());
                id.setValid(true);
            }
            else{
                if(id.getQualify_type().equals(QUALIFYTYPES.NONE.toString())){
                    id.setValid(true);
                }
                else{
                    id.setValid(false);
                }
            }
        }
    }
    
    public ImageIcon getStatusIcon (String itemstatus){
        
        if (itemstatus.equals(QUALIFYTYPES.RANGE.toString()) || 
                itemstatus.equals(QUALIFYTYPES.CONTAINS.toString()) ||
                itemstatus.equals(QUALIFYTYPES.NOTNULL.toString())){
            return ICON_QUESTION;
        }
        else if (itemstatus.equals(QUALIFYTYPES.OK.toString())){
            return ICON_OK;
        }
        else if (itemstatus.equals(QUALIFYTYPES.BAD.toString())){
            return ICON_BAD;
        }
        else if (itemstatus.equals(QUALIFYTYPES.BAD.toString())){
            return ICON_BAD;
        }
        else if (itemstatus.equals(QUALIFYTYPES.ACCEPT.toString())){
            return ICON_FILTER;
        }
        else if (itemstatus.equals(QUALIFYTYPES.INDEX.toString())){
            return ICON_INDEX;
        }
        else{
            return ICON_NONE; 
        }  
    }
  
    
    public InterceptorTemplate getTemplate(){ return master_interceptor_template;}
    
    public ArrayList<ItemDescriptor> getHeaderItems() { return cit.getHeader_items();}
    
    public ArrayList<ItemDescriptor> getValueItems() { return cit.getValue_items();}
    
   
    public void fieldChanged (String field, String value, boolean isheader){
        
        if (isheader){
            for (ItemDescriptor id : cit.getHeader_items()){
                if (id.getLabel().equals(field)){
                    id.setValid(false);
                    id.setValue(value);
                    id.getHpanel().setColor(Color.RED);
                    id.setEditied(true);
                      if (id.isValid()){
                          id.getHpanel().setStatus(id.getIcon());
                      }
                      else{
                          id.getHpanel().setStatus(id.getIcon());
                      }
                    break;
                }
            }
        }
        else{
           for (ItemDescriptor id : cit.getValue_items()){
                if (id.getLabel().equals(field)){
                    id.setValid(false);
                    id.setValue(value);
                    id.setEditied(true);
                    id.getVpanel().setColor(Color.RED);
                      if (id.isValid()){
                          id.getVpanel().setStatus(id.getIcon());
                      }
                      else{
                          id.getVpanel().setStatus(id.getIcon());
                      }
                    break;
                }
            }
        }
        
    }
    
    private boolean isQualifiedRun(){
        
       String tag;
       ItemDescriptor id = master_interceptor_template.getAcceptItem();
        
        if (id != null){
            for (ResultField rf : rr.getHeader()){
                if (rf.getName().equals(id.getRecord_item())){
                    tag = master_interceptor_template.hasFilter(rf.getValue());
                    if (tag != null){
                        id.getHpanel().setColor(new java.awt.Color(0, 153, 153));
                        master_interceptor_template.setCurrentFilter(tag);
                        indexid = ContextUtils.getTimestamp()+" ! "+rf.getValue();
                        return true;
                    }
                    else{
                        indexid = ContextUtils.getTimestamp()+" ! "+"default";
                        id.getHpanel().setColor(new java.awt.Color(255, 0, 0));
                        master_interceptor_template.setCurrentFilter(master_interceptor_template.getdefaultFilter());
                        return false;
                    }

                }
            }
        }
        master_interceptor_template.setCurrentFilter(master_interceptor_template.getdefaultFilter());
        return false;
    }
    
    
    private boolean isLoadedRun(){
        
        String tag;
        
        //if (holdruns.isEmpty()) return false;
        ItemDescriptor id = master_interceptor_template.getIndexItem();
        
        if (id != null){
            for (ResultField rf : rr.getHeader()){
                if (rf.getName().equals(id.getRecord_item())){
                    if (holdruns.isEmpty()){
                        indexid = indexid + " ! "+ rf.getValue();
                        return false;
                    }
                    else{
                        tag = locateHoldRun(rf.getValue());
                        if (tag == null){
                            indexid = indexid + " ! "+ rf.getValue();
                            return false;
                        }
                        else{
                            indexid = tag;
                            return true;
                        } 
                    } 
                }
            }
        }
        indexid = indexid + " | sem index";
        return false;
    }
    
    
    private String locateHoldRun(String tag){
        
        String[] keyvalues;
        
        for (String key : holdruns.keySet()){
            keyvalues = key.split("!");
            //if (keyvalues[2].equals(tag)) return key;
            if (key.contains(tag)) return key;
        }
        return null;
    }
    
    
    private ResultField locateRecordField (String fieldname){
        
        for (ResultField rf : rr.getHeader()){
            if (rf.getName().equals(fieldname)){
                return rf;
            }
        }
        for (ResultField rf : rr.getValues()){
            if (rf.getName().equals(fieldname)){
                return rf;
            }
        }
        return null;
    }
    
    public boolean loadRecord(String file){
     
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson gson = builder.create();
            
            String payload = ContextUtils.loadFile (file);
            rr = gson.fromJson(payload,  ResultRecord.class);
 
            if (isQualifiedRun()){
                if (isLoadedRun()){
                    LOG.info("Qualified and loaded");
                    cit = holdruns.get(indexid);
                    updateTemplate();
                    if (validateRun()){
                        validatedruns.put(this.indexid, cit);
                        view.addIndexItem(indexid, 1);
                    }
                    else{
                        view.addIndexItem(indexid, 2);
                    }
                }
                else{
                    LOG.info("Qualified and not loaded"); 
                    cit = this.master_interceptor_template.clone();               
                    updateTemplate();
                    if (validateRun()){
                        validatedruns.put(this.indexid, cit);
                        view.addIndexItem(indexid, 1);
                    }
                    else{
                        holdruns.put(this.indexid, cit);
                        view.addIndexItem(indexid, 2);
                    }
                }
            }
            else{
                LOG.info("Not Qualified");
                cit = this.master_interceptor_template.clone();
                updateTemplate();
                defaultruns.put(this.indexid, cit);
                view.addIndexItem(indexid, 0);
            }
            return true;
            
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return false;
    }
    
    
    private boolean validateRun(){
      
        Boolean valid = true;
        
        for (ItemDescriptor id : cit.getHeader_items()){
            if (!id.isValid()) valid = false;
        } 
        
        for (ItemDescriptor id : cit.getValue_items()){
            if (!id.isValid()) valid = false;
        } 
        
        return false;    
    }
    
    
    private void updateTemplate(){
        
        ResultField rf;
        String runaddr = rr.getName();
        
        for (ItemDescriptor id : cit.getHeader_items()){
            if (id.getRecord_source().equals(runaddr)){
                rf = locateRecordField(id.getRecord_item());
                if (rf!=null){
                    id.setValue(rf.getValue());
                }
            }     
        }
        
        for (ItemDescriptor id : cit.getValue_items()){
            if (id.getRecord_source().equals(runaddr)){
                rf = locateRecordField(id.getRecord_item());
                if (rf!=null){
                    id.setValue(rf.getValue());
                }
            } 
        }
    }
    
    public void updateView(String run, int group){
        
        cit = indexes.get(group).get(run);
        indexgroup = group;
        
        view.populateHeader();
        for (ItemDescriptor id : cit.getHeader_items()){
            id.getHpanel().setValue(id.getValue());
        }  
        view.populateValues();
        for (ItemDescriptor id : cit.getValue_items()){
            id.getVpanel().setValue(id.getValue());
        }
        
    }
    
    
}




// private void loadItems(boolean header){
//       
//        ResultField rf;
//        String runaddr = rr.getName();
//        
//        for (ItemDescriptor id : cit.getHeader_items()){
//            if (id.getRecord_source().equals(runaddr)){
//                rf = locateRecordField(id.getRecord_item());
//                if (rf!=null){
//                    id.setValue(rf.getValue());
//                    if (header){
//                        id.getHpanel().setValue(rf.getValue());
//                    }
//                    else{
//                        id.getVpanel().setValue(rf.getValue());
//                    }
//                    
//                }
//            }     
//        }  
//        
//        
//    }

//
//
//private void loadHeaders(){
//        
//        ResultField rf;
//        String runaddr = rr.getName();
//        
//        for (ItemDescriptor id : cit.getHeader_items()){
//            if (id.getRecord_source().equals(runaddr)){
//                rf = locateRecordField(id.getRecord_item());
//                if (rf!=null){
//                    id.setValue(rf.getValue());
//                    LOG.info("Setting header "+ rf.getValue());
//                    id.getHpanel().setValue(rf.getValue());
//                }
//            }     
//        }  
//    }
//
//    private void loadvalues(){
//        
//        ResultField rf;
//        String runaddr = rr.getName();
//        
//        for (ItemDescriptor id : cit.getValue_items()){
//            if (id.getRecord_source().equals(runaddr)){
//                rf = locateRecordField(id.getRecord_item());
//                if (rf!=null){
//                    id.setValue(rf.getValue());
//                    id.getVpanel().setValue(rf.getValue());
//                }
//            } 
//        }
//    }
//
//    
//    