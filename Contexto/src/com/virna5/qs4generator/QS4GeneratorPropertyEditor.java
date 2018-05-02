/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.qs4generator;

import com.virna5.csvfilter.*;
import java.awt.Component;
import java.beans.PropertyEditorSupport;
import java.util.logging.Logger;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.PropertyEnv;

/**
 *
 * @author opus
 */
//public class CSVFieldPropertyEditor extends PropertyEditorSupport implements ExPropertyEditor, InplaceEditor.Factory {
public class QS4GeneratorPropertyEditor extends PropertyEditorSupport implements ExPropertyEditor {    

    private static final Logger log = Logger.getLogger(QS4GeneratorPropertyEditor.class.getName());
   
    protected QS4GeneratorFieldsWrapper cfw;
    
    @Override
    public void attachEnv(PropertyEnv pe) {
        
       Object[] beans = pe.getBeans();
       QS4GeneratorNode cfn = (QS4GeneratorNode) beans[0];
       cfw = cfn.getCfw();      
       log.info(String.format("Attaching Gen env : %d with %d fields", cfw.hashCode(), cfw.size()));
    }  
    
    
    @Override
    public String getAsText() {
        return String.format("%d campos",cfw.size());
    }

    @Override
    public void setAsText(String s) {
        
    }
    
    
    
    
    @Override
    public Component getCustomEditor() {
        return new QS4GeneratorFieldsPanel(cfw);
    }

    @Override
    public boolean supportsCustomEditor() {
        return true;
    }
    
    
    
    
    
    
    
    
    //    private InplaceEditor ed = null;
    
//    public void attachEnv(PropertyEnv propertyEnv) {
//        propertyEnv.registerInplaceEditorFactory(this);
//    }    
    
    
//    @Override
//    public InplaceEditor getInplaceEditor() {
//        if(ed == null)
//        ed = new CSVFieldPropertyEditor.Inplace();
//        
//        return ed;
//    }
   
//    private static class Inplace implements InplaceEditor {
//        
//        private PropertyEditor editor = null;
//        private PropertyModel model = null;
//        
//        private JComboBox genres = new JComboBox(
//            new String[] {"Techno", "Trance", "Rock", "Pop"});
//        
//        @Override
//        public JComponent getComponent() {
//            return this.genres;
//        }
//        
//        @Override
//        public Object getValue() {
//            return this.genres.getSelectedItem();
//        }
//        
//        @Override
//        public void setValue(Object object) {
//            this.genres.setSelectedItem(object);
//        }
//        
//        @Override
//        public void reset() {
//            String genre = (String) editor.getValue();
//            if(genre != null)
//            this.genres.setSelectedItem(genre);
//        }
//
//        @Override
//        public boolean isKnownComponent(Component cmpnt) {
//            return true;
//        }
//
//        @Override
//        public void connect(PropertyEditor pe, PropertyEnv pe1) {
//            //log.info("Caling connect");
//            this.editor = pe;
//            this.reset();
//        }
//
//        @Override
//        public void clear() {
//            this.editor=null;
//            this.model=null;
//        }
//
//        @Override
//        public boolean supportsTextEntry() {
//            return true;
//        }
//
//        @Override
//        public void addActionListener(ActionListener al) {
//            log.info("Adding Listener");
//        }
//
//        @Override
//        public void removeActionListener(ActionListener al) {
//            log.info("Removing listener");
//        }
//
//        @Override
//        public KeyStroke[] getKeyStrokes() {
//            return new KeyStroke[0];
//        }
//
//        @Override
//        public PropertyEditor getPropertyEditor() {
//            return this.editor;
//        }
//
//        @Override
//        public PropertyModel getPropertyModel() {
//            return this.model;
//        }
//
//        @Override
//        public void setPropertyModel(PropertyModel pm) {
//            this.model = pm;
//        }
//    }
    
    
}
