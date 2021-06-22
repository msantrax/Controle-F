/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

import java.util.ArrayList;

/**
 *
 * @author opus
 */
public class ResultRecord {
    
    private Long uid;
    private String owner;
    private Long created;
    private String name;
    
    private transient boolean flag = false;
    private transient int iflag = 0;
    
    private ArrayList<ResultField> fields;

    
    public static ResultRecord ResultRecordFactory(){
        ResultRecord instance = new ResultRecord()
                .setUid(ContextUtils.getUID())
                .setOwner(ContextUtils.OWNER)
                .setCreated(System.currentTimeMillis());
        return instance;
    }
    
    
    public ResultRecord() {
        fields = new ArrayList<>();
    }
    

    /**
     * @return the uid
     */
    public Long getUid() {
        return uid;
    }

    /**
     * @param uid the uid to set
     */
    public ResultRecord setUid(Long uid) {
        this.uid = uid;
        return this;
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
    public ResultRecord setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    /**
     * @return the created
     */
    public Long getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public ResultRecord setCreated(Long created) {
        this.created = created;
        return this;
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
    public ResultRecord setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @return the flag
     */
    public boolean isFlag() {
        return flag;
    }

    /**
     * @param flag the flag to set
     */
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    /**
     * @return the iflag
     */
    public int getIflag() {
        return iflag;
    }

    /**
     * @param iflag the iflag to set
     */
    public void setIflag(int iflag) {
        this.iflag = iflag;
    }

    /**
     * @return the fields
     */
    public ArrayList<ResultField> getFields() {
        return fields;
    }

    /**
     * @param fields the fields to set
     */
    public void setFields(ArrayList<ResultField> fields) {
        this.fields = fields;
    }
    
    public int getSize(){
        return  fields.size();
    }
    
    public ResultField getField (int index){
        return fields.get(index);
    }
     
    public ArrayList<ResultField> getHeader(){
        
        ArrayList<ResultField> header = new ArrayList<>();
        for (ResultField rf : fields){
            if (rf.getRealm().equals(ResultField.FREALM.HEADER.toString())){
                header.add(rf);
            }
        }
        return header;
    }
    
    public ArrayList<ResultField> getValues(){
        
        ArrayList<ResultField> values = new ArrayList<>();
        for (ResultField rf : fields){
            if (rf.getRealm().equals(ResultField.FREALM.VALUE.toString())){
                values.add(rf);
            }
        }
        return values;
    }
    
    public ResultField findField(String name){
        
        for (ResultField rf : fields){
            if (rf.getName().equals(name)) return rf;
        }
        return null;
    }
    
    
}
