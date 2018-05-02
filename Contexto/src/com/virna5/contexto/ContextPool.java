/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author opus
 */
public class ContextPool extends ArrayList<ContextControl>{

    public ContextPool() {
    }

    public ContextPool(Collection<? extends ContextControl> c) {
        super(c);
    }
    
    
    public boolean hasTask(Long uid){
        
        for (ContextControl cc : this){
            if ( cc.getContextid() == uid) return true;
        }
        return  false;
    }
    
    
    
}
