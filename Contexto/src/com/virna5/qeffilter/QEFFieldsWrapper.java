/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.qeffilter;

import com.virna5.qeffilter.*;
import com.virna5.csvfilter.*;

import com.virna5.contexto.BaseDescriptor;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author opus
 */
public class QEFFieldsWrapper extends ArrayList<QEFField> {
    
    public QEFFieldsWrapper() {
         
    }

    public QEFFieldsWrapper(Collection<? extends QEFField> c) {
        super(c);
    }
 
    
    
}
