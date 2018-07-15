/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.interceptor;

import java.awt.Color;
import javax.swing.ImageIcon;

/**
 *
 * @author opus
 */
public class HeaderItemPanel  extends javax.swing.JPanel{
    
    private javax.swing.JLabel flabel;
    private javax.swing.JTextField fvalue;
    private int psize;
    
    
    private InterceptModel im;
    
    public HeaderItemPanel() {
        initComponents(200, "header", "value");
    }
    
    public HeaderItemPanel(String  _size, String _label, String _value, InterceptModel _im) {
        
        try {
            psize = Integer.parseInt(_size);
        }
        catch (Exception ex){
            psize = 200;
        }
        
        if (_value == null) _value="";
        initComponents(psize, _label, _value);
        this.im = _im;
    }
    
    
    public HeaderItemPanel clone(){
        
        HeaderItemPanel hip = new HeaderItemPanel();
        hip.flabel = this.flabel;
        hip.fvalue = this.fvalue;
        hip.psize = this.psize;
        hip.im = this.im;
        
        return hip;
    }
    
    
    
    public void setValue (String value){
        fvalue.setText(value);
        this.updateUI();
    }
    
    public void setColor (Color color){
        fvalue.setForeground(color);
        
    }
    
    public void setStatus (ImageIcon icon){
        flabel.setIcon(icon);
    }
    
    private void initComponents(int size, String _label, String _value) {

        flabel = new javax.swing.JLabel();
        fvalue = new javax.swing.JTextField();

        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(size, 30));

        flabel.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        flabel.setForeground(new java.awt.Color(255, 255, 255));
        flabel.setText(_label); // NOI18N
        //flabel.setIcon(InterceptModel.ICON_OK); // NOI18N

        fvalue.setFont(new java.awt.Font("DejaVu Sans", 0, 14)); // NOI18N
        fvalue.setText(_value); // NOI18N
        fvalue.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        fvalue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fvalueActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(flabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fvalue, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(flabel)
                .addComponent(fvalue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }
    
    
    private void fvalueActionPerformed(java.awt.event.ActionEvent evt) {                                       
        im.fieldChanged(flabel.getText(), fvalue.getText(), true);
    }  
    
    
    
    
}
