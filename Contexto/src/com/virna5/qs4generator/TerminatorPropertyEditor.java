/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.qs4generator;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.InplaceEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.explorer.propertysheet.PropertyModel;

/**
 *
 * @author opus
 */
public class TerminatorPropertyEditor extends PropertyEditorSupport implements ExPropertyEditor, InplaceEditor.Factory {
    
    private  InplaceEditor ed = null;
   
    public void attachEnv(PropertyEnv propertyEnv) {
        propertyEnv.registerInplaceEditorFactory(this);
    }

    @Override
    public InplaceEditor getInplaceEditor() {
        if (ed == null) {
            ed = new Inplace();
        }
        return ed;
    }

    private static class Inplace implements InplaceEditor {

        private PropertyEditor editor = null;
        private PropertyModel model = null;
        private JComboBox genres = new JComboBox(new String[]{"Unix", "Windows"});

        public Inplace() {
        }

        public JComponent getComponent() {
            return this.genres;
        }

        public Object getValue() {
            return this.genres.getSelectedItem();
        }

        public void setValue(Object object) {
            this.genres.setSelectedItem(object);
        }

        public void reset() {
            String genre = (String) editor.getValue();
            if (genre != null) {
                this.genres.setSelectedItem(genre);
            }
        }

        @Override
        public void connect(PropertyEditor pe, PropertyEnv pe1) {
            this.editor = pe;
            this.reset();
        }

        @Override
        public void clear() {
            this.editor=null;
            this.model=null;
        }

        @Override
        public boolean supportsTextEntry() {
            return true;
        }

        @Override
        public void addActionListener(ActionListener al) {
            
        }

        @Override
        public void removeActionListener(ActionListener al) {
            
        }

        @Override
        public KeyStroke[] getKeyStrokes() {
            return new KeyStroke[0];
        }

        @Override
        public PropertyEditor getPropertyEditor() {
            return this.editor;
        }

        @Override
        public PropertyModel getPropertyModel() {
            return this.model;
        }

        @Override
        public void setPropertyModel(PropertyModel pm) {
            this.model = pm;
        }

        @Override
        public boolean isKnownComponent(Component cmpnt) {
            return true;
        }
    }
    
}
