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
    public int int1, int2;

    public VirnaPayload() {
        
    }
   
    public VirnaPayload setString(String s){
        vstring = s;
        return this;
    }
    
    
}
