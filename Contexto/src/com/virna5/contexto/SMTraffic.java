/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;


public class SMTraffic {
    
    protected Long addr;
    private VirnaServices.CMDS command;
    private int code;
    protected Long handle;
    private VirnaServices.STATES state;
    private VirnaPayload payload;

    public SMTraffic( Long addr, VirnaServices.CMDS command, int code, VirnaServices.STATES state, VirnaPayload payload) {
        
        this.addr = addr;
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

     public SMTraffic( Long addr, Long handle, int code, VirnaServices.STATES state, VirnaPayload payload) {
        
        this.addr = addr;
        this.command = VirnaServices.CMDS.LOADSTATE;
        this.code = code;
        this.handle = handle;
        
        if (payload == null){
            this.payload = new VirnaPayload().setString("");
        }
        else{
            this.payload = payload;
        }
        this.code=code;
        this.state=state;
    }
    
    public SMTraffic clone(){
        
        SMTraffic smt = new SMTraffic(new Long(this.addr), new Long(this.handle), new Integer(code).intValue(), this.state, this.payload);
        smt.command = this.getCommand();
        return smt;
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

    /**
     * @return the uid
     */
    public Long getAddr() {
        return addr;
    }

    /**
     * @param uid the uid to set
     */
    public void setAddr(Long addr) {
        this.addr = addr;
    }

    /**
     * @return the handle
     */
    public Long getHandle() {
        return handle;
    }

    /**
     * @param handle the handle to set
     */
    public void setHandle(Long handle) {
        this.handle = handle;
    }
    
    
    
    
}
