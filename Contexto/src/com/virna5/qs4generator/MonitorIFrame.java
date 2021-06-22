/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.qs4generator;

import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.BaseService;
import com.virna5.contexto.ContextUtils;
import com.virna5.contexto.MonitorIFrameInterface;
import com.virna5.contexto.SMTraffic;
import com.virna5.contexto.VirnaServices;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.LifecycleManager;
import org.openide.util.Exceptions;


public class MonitorIFrame extends JInternalFrame implements MonitorIFrameInterface, ActionListener {

    private QS4GeneratorDescriptor descriptor;
    private QS4GeneratorService service;
    protected String iframeid ;
  
    private Timer timer;
 
    private QS4GeneratorFieldsWrapper cfw;
    DialogDescriptor d;
    
    public MonitorIFrame() {
        initComponents();
        //timer = new Timer( 500 , new LedTimerListener(lb_led));
          
    }

    public void iconifyFrame(boolean icon){
        
        try {
            this.setIcon(icon);
        } catch (PropertyVetoException ex) {
            Exceptions.printStackTrace(ex);
        }
        
    }

    // ============================================== POINTERS DE MANEJAMENTO ================================================
    /**
     * @return the descriptor
     */
    public QS4GeneratorDescriptor getDescriptor() {
        return descriptor;
    }

    /**
     * @param descriptor the descriptor to set
     */
    public void setDescriptor(BaseDescriptor _descriptor) {
        this.descriptor = (QS4GeneratorDescriptor)_descriptor;
        ckb_auto.setSelected(descriptor.getAutomatic());
        bt_simulate.setEnabled(!descriptor.getAutomatic());
    }

    /**
     * @return the descriptor
     */
    public QS4GeneratorService getService() {
        return service;
    }

    /**
     * @param descriptor the descriptor to set
     */
    public void setService(BaseService _service) {
        this.service = (QS4GeneratorService)_service;
    }
    
    
    /**
     * @return the iframeid
     */
    public String getIframeid() {
        return iframeid;
    }

    /**
     * @param iframeid the iframeid to set
     */
    public void setIframeid(String iframeid) {
        this.iframeid = iframeid;
    }
    
    
    
    // ================================================= ROOT SERVICES =========================================
    public void flashLed(){
        
        if (lb_led.getBackground() != LED_GREEN_OFF){
            lb_led.setBackground(LED_GREEN_OFF);
        }
        else{
            lb_led.setBackground(LED_GREEN_ON); 
        }
      
        
        //timer.setInitialDelay(0);
        //timer.start();
    }
    
    @Override
    public void updateUI(final Color ledcolor, final String  message){
        
        Runnable worker = new Runnable() {
            public void run() {
                if (ledcolor !=null){
                    lb_led.setBackground(ledcolor);
                }
                if (message !=null){
                    //lb_timestamp.setText(LocalDate.now().format(date_formatter));
                    lb_timestamp.setText(ContextUtils.getTimestamp());
                }
                else{
                    
                }
            }
        };
        EventQueue.invokeLater(worker);   
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() == DialogDescriptor.OK_OPTION) {
            //LifecycleManager.getDefault().exit();
        } 
        
        d.setClosingOptions(null);
        
    }
   
    
    private class LedTimerListener implements ActionListener {
    
        JLabel led;
        
        public LedTimerListener(JLabel lb){
            this.led = lb;
        }
        
        public void actionPerformed(ActionEvent e) {
            led.setBackground(LED_GREEN_OFF);
        }
    }
    
    
    public void editValues(){
        
        cfw = descriptor.getGeneratorfields();
        QS4GeneratorEditorPanel gep = new QS4GeneratorEditorPanel(cfw);
        
        d = new DialogDescriptor(gep, "Editor", true, this);
        d.setClosingOptions(new Object[]{});
        
        d.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                if(e.getPropertyName().equals(DialogDescriptor.PROP_VALUE) && e.getNewValue()==DialogDescriptor.CLOSED_OPTION) {
//                    LifecycleManager.getDefault().exit();
                }
            }
        });
       
        DialogDisplayer.getDefault().notifyLater(d);
 
    }
    
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lb_led = new javax.swing.JLabel();
        lb_timestamp = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        ckb_auto = new javax.swing.JCheckBox();
        bt_simulate = new javax.swing.JButton();
        bt_edit = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setOpaque(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(null);

        lb_led.setBackground(new java.awt.Color(0, 150, 50));
        org.openide.awt.Mnemonics.setLocalizedText(lb_led, org.openide.util.NbBundle.getMessage(MonitorIFrame.class, "MonitorIFrame.lb_led.text")); // NOI18N
        lb_led.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lb_led.setOpaque(true);

        lb_timestamp.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, "Última Simulação : "); // NOI18N

        ckb_auto.setBackground(new java.awt.Color(255, 255, 255));
        org.openide.awt.Mnemonics.setLocalizedText(ckb_auto, "Modo Automático"); // NOI18N
        ckb_auto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckb_autoActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(bt_simulate, org.openide.util.NbBundle.getMessage(MonitorIFrame.class, "MonitorIFrame.bt_simulate.text")); // NOI18N
        bt_simulate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_simulateActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(bt_edit, "Editar Resultado"); // NOI18N
        bt_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_editActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_timestamp)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(lb_led))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(ckb_auto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                        .addComponent(bt_edit)
                        .addGap(18, 18, 18)
                        .addComponent(bt_simulate)))
                .addGap(32, 32, 32))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_led)
                    .addComponent(lb_timestamp)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bt_simulate)
                    .addComponent(ckb_auto)
                    .addComponent(bt_edit))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ckb_autoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckb_autoActionPerformed
        
        boolean isauto = ckb_auto.isSelected();
        if (isauto){
             bt_simulate.setEnabled(false);            
        }
        else{
             bt_simulate.setEnabled(true);
        }
        
    }//GEN-LAST:event_ckb_autoActionPerformed

    private void bt_simulateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_simulateActionPerformed
        
        long uid = descriptor.getUID();
        SMTraffic alarm_config = new SMTraffic(uid, uid, 0,
                                            VirnaServices.STATES.QS4GEN_GEN, 
                                            null);
        descriptor.getService().processSignal(alarm_config, descriptor);

    }//GEN-LAST:event_bt_simulateActionPerformed

    private void bt_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_editActionPerformed
        editValues();
    }//GEN-LAST:event_bt_editActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_edit;
    private javax.swing.JButton bt_simulate;
    private javax.swing.JCheckBox ckb_auto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lb_led;
    private javax.swing.JLabel lb_timestamp;
    // End of variables declaration//GEN-END:variables

}




