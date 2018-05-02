/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

/**
 *
 * @author opus
 */
public class VirnaPayload {
    
    public Object vobject;
    public String objecttype;
    public String vstring;
    public Integer int1, int2;
    public Long long1, long2;

    public VirnaPayload() {
        
    }
   
    public VirnaPayload setString(String s){
        vstring = s;
        return this;
    }
    
    public VirnaPayload setLong1(Long data){
        long1 = data;
        return this;
    }
    
    public VirnaPayload setLong2(Long data){
        long2 = data;
        return this;
    }
    
    
    public VirnaPayload setObject(Object data){
        vobject = data;
        return this;
    }
    
    public VirnaPayload setObjectType(String data){
        objecttype = data;
        return this;
    }
    
    public VirnaPayload setInt1(Integer data){
        int1 = data;
        return this;
    }
    
    public VirnaPayload setInt2(Integer data){
        int2 = data;
        return this;
    }
    
}
