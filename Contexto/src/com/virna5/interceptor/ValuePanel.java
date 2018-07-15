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
public class ValuePanel extends javax.swing.JPanel {

    public static enum HEADERFLAGS { UNLOADED, NOTCHECKED, OK, ABOVE, BELLOW, NOTEQUAL};
    
    private InterceptModel im;
    
    /**
     * Creates new form ValuePanel
     */
    public ValuePanel() {
        initComponents();
    }

    public ValuePanel(String header, String value, InterceptModel _im){
        initComponents();
        this.lb_header.setText(header);
        this.tf_value.setText(value);
        this.im = _im;
    }
    
    public ValuePanel(int seq){
        initComponents();
        this.lb_header.setText(String.format("%2d", seq));
        this.tf_value.setText("0.0034");
    }
    
    public void setHeader (String header){
        this.lb_header.setText(header);
    }
    
    
    public void setValue(String value){
        tf_value.setText(value);
        
    }
    
    public void setColor (Color color){
        tf_value.setForeground(color);
    }
    
    public void setStatus (ImageIcon icon){
        lb_header.setIcon(icon);
    }
    
    
    
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of
     * this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lb_header = new javax.swing.JLabel();
        tf_value = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(125, 75));

        lb_header.setFont(new java.awt.Font("DejaVu Sans", 1, 18)); // NOI18N
        lb_header.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(lb_header, "Mn"); // NOI18N
        lb_header.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lb_header.setDoubleBuffered(true);
        lb_header.setOpaque(true);

        tf_value.setFont(new java.awt.Font("DejaVu Sans", 1, 18)); // NOI18N
        tf_value.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tf_value.setText("0.381"); // NOI18N
        tf_value.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        tf_value.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_valueActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lb_header, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
            .addComponent(tf_value)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lb_header)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tf_value)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tf_valueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_valueActionPerformed
        getIm().fieldChanged(this.lb_header.getText(), this.tf_value.getText(), false);
    }//GEN-LAST:event_tf_valueActionPerformed

    /**
     * @return the im
     */
    public InterceptModel getIm() {
        return im;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lb_header;
    private javax.swing.JTextField tf_value;
    // End of variables declaration//GEN-END:variables

    

}
