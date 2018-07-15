/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.interceptor;

import com.virna5.contexto.ContextUtils;
import com.virna5.sapfilter.SAPField;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author opus
 */
public class ItemDescriptor {

    private static final Logger LOG = Logger.getLogger(ItemDescriptor.class.getName());

    private String record_item;
    private String record_source;     
    
    private transient String value;
    private String label;
    
    private String length;
   
    private transient Double dvalue;
    private transient Double hvalue;
    private transient Double lvalue;
    private transient Boolean valid;
    private transient int rangestatus;
    private transient ImageIcon icon;
    private transient boolean editied = false; 
    
    
    private transient ValuePanel vpanel;
    private transient HeaderItemPanel hpanel;
    
    
    private String qualify_type;
    private String qualify_var1;
    private String qualify_var2;
    private String qualify_var3;
    private String qualify_var4;
    
    
    public static ItemDescriptor ItemDescriptorFactory(){
        ItemDescriptor instance = new ItemDescriptor();
        return instance;
    }
    
    
    public ItemDescriptor() {
        
        this.value= null;
        this.dvalue = -1.0;
        this.hvalue = -1.0;
        this.lvalue = -1.0;
        this.valid = false;
        this.rangestatus = -1;
    }
    
    public ItemDescriptor(String item, String source) {
        
        this.value=null;
        this.record_source = source;
        this.label=item;
        this.length = "250";
        this.record_item = item;
        this.dvalue = -1.0;
        this.hvalue = -1.0;
        this.lvalue = -1.0;
        this.valid = false;
        this.rangestatus = -1;
    
    }
  
    public void estimateRange(){
        
        if (value.equals("0") || value.equals("") || value == null){
            this.qualify_var1 = "0";
            this.qualify_var2 = "0";
            this.qualify_type = InterceptModel.QUALIFYTYPES.NONE.toString();
        }
        else{
            try {
                dvalue = Double.parseDouble(value);
                Double dhigh = dvalue + (dvalue/10);
                Double dlow =  dvalue - (dvalue/10);
                this.qualify_var1 = String.valueOf(dhigh);
                this.qualify_var2 = String.valueOf(dlow);
                this.qualify_type = InterceptModel.QUALIFYTYPES.RANGE.toString();
                this.length = null;
            }
            catch (Exception ex){
                this.qualify_var1 = "0";
                this.qualify_var2 = "0";
                this.qualify_type = InterceptModel.QUALIFYTYPES.NONE.toString();
            }
        }
    }
    
    
    public ItemDescriptor clone(){
        
        ItemDescriptor id = new ItemDescriptor();
        
        id.record_item = this.record_item;
        id.record_source = this.record_source;
        id.value = this.value;
        id.label = this.label;
        id.length = this.length;
        
        id.dvalue = new Double(this.dvalue);
        id.hvalue = new Double (this.hvalue);
        id.lvalue = new Double (this.lvalue);
        id.valid = new Boolean(this.valid);
        id.rangestatus = this.rangestatus;
        
        
        if (vpanel !=null){
            id.vpanel = new ValuePanel(this.label, this.value, this.vpanel.getIm());
        }
        
        if (hpanel !=null){
            id.hpanel = hpanel.clone();
        }
        
        id.qualify_type = this.qualify_type;
        id.qualify_var1 = this.qualify_var1;
        id.qualify_var2 = this.qualify_var2;
        id.qualify_var3 = this.qualify_var3;
        id.qualify_var4 = this.qualify_var4;
 
        return id;        
    }
    
    // ======================================
    
    public String getRecord_item() {
        return record_item;
    }

    public void setRecord_item(String record_item) {
        this.record_item = record_item;
    }

        

    /**
     * Get the value of record_source
     *
     * @return the value of record_source
     */
    public String getRecord_source() {
        return record_source;
    }

    /**
     * Set the value of record_source
     *
     * @param record_source new value of record_source
     */
    public void setRecord_source(String record_source) {
        this.record_source = record_source;
    }

    
    public Boolean checkvalid(){
        
        
        if (qualify_type.equals(InterceptModel.QUALIFYTYPES.NONE.toString())){
            icon = InterceptModel.ICON_NONE;
            return true;
        }
        
        if (qualify_type.equals(InterceptModel.QUALIFYTYPES.INDEX.toString())){
            icon = InterceptModel.ICON_FILTER;
            return true;
        }
 
        if (qualify_type.equals(InterceptModel.QUALIFYTYPES.INDEX.toString())){
            icon = InterceptModel.ICON_INDEX;
            return true;
        }
        
        
        
        if (qualify_type.equals(InterceptModel.QUALIFYTYPES.RANGE.toString())){
     
            if (value == null){
                icon = InterceptModel.ICON_QUESTION;
                return false;
            }
   
            if (dvalue == -1.0) {
                try{
                    dvalue = Double.parseDouble(this.value);
                    hvalue = Double.parseDouble(this.qualify_var1);
                    lvalue = Double.parseDouble(this.qualify_var2);
                }
                catch (Exception ex){
                    icon = InterceptModel.ICON_QUESTION;
                    this.qualify_type = InterceptModel.QUALIFYTYPES.BAD.toString();
                    return false;
                }
            }
           
            if (dvalue < lvalue){
                icon = InterceptModel.ICON_BELOW;
                this.rangestatus = -1;
                return false;
            }
            else if(dvalue > hvalue){
                icon = InterceptModel.ICON_ABOVE;
                this.rangestatus = 1;
                return false;
            }
            else {
                icon = InterceptModel.ICON_OK;
                this.rangestatus = 0;
                return true;
            }
            
        }
        
        if (qualify_type.equals(InterceptModel.QUALIFYTYPES.NOTNULL.toString())){
            if (this.value == null){
                icon = InterceptModel.ICON_BAD;
                return false;
            }
            else if (this.value.equals("")){
                icon = InterceptModel.ICON_BAD;
                return false;
            }
            icon = InterceptModel.ICON_OK;
            return true;
        }
        
        if (qualify_type.equals(InterceptModel.QUALIFYTYPES.CONTAINS.toString())){
            
            String temp = this.value.toUpperCase();
            
            if (temp.contains(this.qualify_var1.toUpperCase())){
                icon = InterceptModel.ICON_OK;
                return true;
            }
            else{
                icon = InterceptModel.ICON_BAD;
                return false;
            }
        }
        
        if (qualify_type.equals(InterceptModel.QUALIFYTYPES.MATCH.toString())){
           
            if (this.value.equals(this.qualify_var1)){
                icon = InterceptModel.ICON_OK;
                return true;
            }
            else{
                icon = InterceptModel.ICON_BAD;
                return false;
            }
            
        }
    
        icon = InterceptModel.ICON_QUESTION;
        return false;
    }
    
        

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

        
    // Qualify Vars
     public String getQualify_type() {
        return qualify_type;
    }

    public void setQualify_type(String qualify_type) {
        this.qualify_type = qualify_type;
    }
    
    public String getQualify_var1() {
        return qualify_var1;
    }

    public void setQualify_var1(String qualify_var1) {
        this.qualify_var1 = qualify_var1;
    }

    public String getQualify_var2() {
        return qualify_var2;
    }

    public void setQualify_var2(String qualify_var2) {
        this.qualify_var2 = qualify_var2;
    }

    public String getQualify_var3() {
        return qualify_var3;
    }

    public void setQualify_var3(String qualify_var3) {
        this.qualify_var3 = qualify_var3;
    }

    public String getQualify_var4() {
        return qualify_var4;
    }

    public void setQualify_var4(String qualify_var4) {
        this.qualify_var4 = qualify_var4;
    }
    
    
    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the length
     */
    public String getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(String length) {
        this.length = length;
    }

    /**
     * @return the valid
     */
    public Boolean isValid() {
        if (!valid){
            valid =  checkvalid();
        }
        return valid;
    }

    /**
     * @param valid the valid to set
     */
    public void setValid(Boolean _valid) {    
        this.valid = _valid;
        if (!valid) dvalue = -1.0;
    }

    /**
     * @return the vpanel
     */
    public ValuePanel getVpanel() {
        return vpanel;
    }

    /**
     * @param vpanel the vpanel to set
     */
    public void setVpanel(ValuePanel vpanel) {
        this.vpanel = vpanel;
    }

    /**
     * @return the hpanel
     */
    public HeaderItemPanel getHpanel() {
        return hpanel;
    }

    /**
     * @param hpanel the hpanel to set
     */
    public void setHpanel(HeaderItemPanel hpanel) {
        this.hpanel = hpanel;
    }

    /**
     * @return the icon
     */
    public ImageIcon getIcon() {
        return icon;
    }

    /**
     * @return the editied
     */
    public boolean isEditied() {
        return editied;
    }

    /**
     * @param editied the editied to set
     */
    public void setEditied(boolean editied) {
        this.editied = editied;
    }

    
    
    
}


