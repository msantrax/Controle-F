/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

import java.util.LinkedHashMap;
import java.util.logging.Logger;

/**
 *
 * @author opus
 */
public class VirnaServices {
    
    private static final Logger log = Logger.getLogger(VirnaServices.class.getName());
  
    public static LinkedHashMap<String,Integer>comand_map; 

    public VirnaServices() {
        
         loadMaps();
        
    }
    
      
    private void loadMaps(){
        
        comand_map = new LinkedHashMap<>();
        
        comand_map.put("CALLBACK_REQ", 0);
        comand_map.put("CALLBACK_OK", 1);
        comand_map.put("CALLBACK_NONE", 3);
        comand_map.put("COMAND_ACK", 4);
        comand_map.put("CMD_STRING", 5);
        comand_map.put("CMD_TESTE1", 6);
        comand_map.put("CMD_TESTE2", 7);
        
        comand_map.put("CMD_CSTATUS", 20);  
    }
    
    
    
    //=======================================================================================================
    
    public static enum CMDS {LOADSTATE, RESET, SENDU, SENDN, RECVU, RECVN };
    
    public static enum SMEVENTS {ATTACH, DETACH, RESET };
    
    public static enum STATES { INIT, IDLE, CONFIG, RESET,
                                ATTACH, ATTACHED, DETACH, DETACHED,
                                FOB_DOSCAN, FOB_STOPSCAN,
                                CTRL_LOADTASK, CTRL_REMOVETASK, CTRL_AUTOLOAD,
                                CTRL_LOADMONITOR, CTRL_REMOVEMONITOR,
                                CTRL_ADDALARM, CTRL_REMOVEALARM,
                                CTRL_ACTIVATETASK, CTRL_DEACTIVATETASK,
                                CTRL_HOUSEKEEP,
                                
                                TSK_MANAGE, TSK_REQUESTALARM, TASK_CANCELALARM,
                                TSK_INITUI, TSK_SUSPEND, TSK_CLEAR,
                                
                                FOB_READ,FOB_SETPATH,
                                QS4GEN_GEN,FWRITER_WRITE,CSV_CONVERT,
                                
                                LOADRECORD, SENDRECORD
                                
                                
                                
                              };
    
    
    
}
