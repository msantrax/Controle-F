/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;


public class SMTraffic {
    
    private VirnaServices.CMDS command;
    private int code;
    private VirnaServices.STATES state;
    private VirnaPayload payload;

    public SMTraffic(VirnaServices.CMDS command, int code, VirnaServices.STATES state, VirnaPayload payload) {
        this.command = command;
        this.code = code;
        
        if (payload == null){
            this.payload = new VirnaPayload().setString("");
        }
        else{
            this.payload = payload;
        }
        this.code=code;
        this.state=state;
    }

    public VirnaServices.CMDS getCommand() {
        return command;
    }

    public int getCode() {
        return code;
    }

    public VirnaServices.STATES getState() {
        return state;
    }
    
    public VirnaPayload getPayload() {
        return payload;
    }
    
    
    
    
}
