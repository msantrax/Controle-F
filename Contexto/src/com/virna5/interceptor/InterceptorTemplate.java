/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.interceptor;

import com.virna5.contexto.ContextUtils;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 *
 * @author opus
 */
public class InterceptorTemplate {

    private static final Logger LOG = Logger.getLogger(InterceptorTemplate.class.getName());

    private Long uid;
    private Long created;
    private String title;
    private String indextemplate;
    
    private String name;
    
    private ArrayList<ItemDescriptor> header_items;
    //private ArrayList<ItemDescriptor> value_items;
    
    private AlloyFilters alloyfilters;
    private transient String currentFilter;
    
    public transient ItemDescriptor accept_item;
    public transient ItemDescriptor index_item;
    
    private ResultRecord resultrecord; 
    
    
    public InterceptorTemplate() {
        
        this.uid = ContextUtils.getUID();
        this.created = System.currentTimeMillis();
        header_items = new ArrayList<>();
        this.title="Title ?";
        this.indextemplate = "TIMESTAMP:Nome:Amostra";
        this.currentFilter = "default";
        this.alloyfilters = new AlloyFilters();
    }
 
    public ArrayList<ItemDescriptor> cloneItems(ArrayList<ItemDescriptor> items) {
        
        ArrayList<ItemDescriptor> cloned_items = new ArrayList<>();
        for (ItemDescriptor id : items){
            cloned_items.add(id.clone());
        }        
        return cloned_items;
    }
    
    public InterceptorTemplate clone(){
        
        InterceptorTemplate it = new InterceptorTemplate();
        
        it.uid = ContextUtils.getUID();
        it.created = System.currentTimeMillis();
        it.header_items = cloneItems(header_items);
        it.indextemplate = this.indextemplate;
        it.currentFilter = this.currentFilter;
        it.alloyfilters = this.alloyfilters.clone(this.currentFilter);
        
        it.locateFilterDescriptors();
        
        return it;
    }
    
    
    public ArrayList<ItemDescriptor> getHeader_items() {
        return header_items;
    }

    public void setHeader_items(ArrayList<ItemDescriptor> header_items) {
        this.header_items = header_items;
    }

    public ArrayList<ItemDescriptor> getValue_items() {
        return alloyfilters.locateFilter(getCurrentFilter()).getItems();
    }

    public String hasFilter (String tag){
        
        tag = tag.toUpperCase();
        String atag;
        
        for (AlloyFilter af : alloyfilters.getFilters()){
            atag = af.getAccept_tag().toUpperCase();
            if (tag.contains(atag)){
                return af.getAccept_tag();
            }
        }
        return null;
    }
    
    public void locateFilterDescriptors(){
        
        for (ItemDescriptor id : header_items){
            if(id.getQualify_type().equals(InterceptModel.QUALIFYTYPES.ACCEPT.toString())){
                accept_item = id;
                break;
            }
        }
        
        for (ItemDescriptor id : header_items){
            if(id.getQualify_type().equals(InterceptModel.QUALIFYTYPES.INDEX.toString())){
                index_item = id;
                break;
            }
        }
    }
    
    public ItemDescriptor getAcceptItem (){ return this.accept_item;}
    
    public ItemDescriptor getIndexItem (){ return this.index_item;}
    
    
    public String getdefaultFilter(){
        return alloyfilters.getFilters().get(0).getAccept_tag();
    }
    
    /**
     * @return the uid
     */
    public Long getUid() {
        return uid;
    }

    /**
     * @return the created
     */
    public Long getCreated() {
        return created;
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
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the indextemplate
     */
    public String getIndextemplate() {
        return indextemplate;
    }

    /**
     * @param indextemplate the indextemplate to set
     */
    public void setIndextemplate(String indextemplate) {
        this.indextemplate = indextemplate;
    }

    /**
     * @return the alloyfilters
     */
    public AlloyFilters getAlloyfilters() {
        return alloyfilters;
    }

    /**
     * @return the currentFilter
     */
    public String getCurrentFilter() {
        return currentFilter;
    }

    /**
     * @param currentFilter the currentFilter to set
     */
    public void setCurrentFilter(String currentFilter) {
        this.currentFilter = currentFilter;
    }

    /**
     * @return the resultrecord
     */
    public ResultRecord getResultrecord() {
        return resultrecord;
    }

    /**
     * @param resultrecord the resultrecord to set
     */
    public void setResultrecord(ResultRecord resultrecord) {
        this.resultrecord = resultrecord;
    }
    
    
}
