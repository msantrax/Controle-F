/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.fileobserver;

import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.BaseService;
import com.virna5.contexto.ContextUtils;
import com.virna5.contexto.MonitorIFrameInterface;
import com.virna5.contexto.SMTraffic;
import com.virna5.contexto.VirnaPayload;
import com.virna5.contexto.VirnaServices;
import java.awt.Color;
import javax.swing.JInternalFrame;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;


public class MonitorIFrame extends JInternalFrame implements MonitorIFrameInterface{

    private FileObserverDescriptor descriptor;
    private FileObserverService service;
    protected String iframeid ;
    
    private String payload = "Nenhum dado disponível";
  
    
    public MonitorIFrame() {
        initComponents();
        //timer = new Timer( 500 , new LedTimerListener(lb_led));
          
    }

    
    
    // ============================================== POINTERS DE MANEJAMENTO ================================================
    /**
     * @return the descriptor
     */
    public FileObserverDescriptor getDescriptor() {
        return descriptor;
    }

    /**
     * @param descriptor the descriptor to set
     */
    public void setDescriptor(BaseDescriptor _descriptor) {
        this.descriptor = (FileObserverDescriptor)_descriptor;
        
    }

    /**
     * @return the descriptor
     */
    public FileObserverService getService() {
        return service;
    }

    /**
     * @param descriptor the descriptor to set
     */
    public void setService(BaseService _service) {
        this.service = (FileObserverService)_service;
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
   
    public void updateUIDirect(FileObserverUIUpdater fwuu){
        
        if (fwuu.getLedcolor() != null){
            lb_led.setBackground(fwuu.getLedcolor());
            lb_timestamp.setText(ContextUtils.getTimestamp());
        }
        if (fwuu.getPayload() != null){
            this.payload = fwuu.getPayload();
        }
        if (fwuu.getFilename() != null){
            this.lb_observado.setText(fwuu.getFilename());
        }
        if (fwuu.getDirbackup() != null){
            this.lb_backup.setText(fwuu.getDirbackup());
        }
        
        if (fwuu.getInterval() != null){
            this.lb_interval.setText(fwuu.getInterval().toString() + " ms");
        }
        
        if (fwuu.getTimeout() != null){
            Long tm = fwuu.getTimeout();
            if (tm < 10){
                this.lb_timeout.setText("Dasabilitado");
            }
            else if (tm > 60){
                tm = tm/60;
                this.lb_timeout.setText(tm.toString() + " min");
            }
            this.lb_timeout.setText(fwuu.getInterval().toString() + " seg");
        }
     
        if (fwuu.getMultiline() != null){
            ckb_multiline.setSelected(fwuu.getMultiline());
        }
        
        if (fwuu.getAuto() != null){
            
            if (fwuu.getAuto()){
                this.ckb_suspend.setEnabled(true);
                this.ckb_suspend.setSelected(true);
                this.bt_observe.setEnabled(false);
                this.lb_observado.setEnabled(false);
                this.lb_backup.setEnabled(false);
            }
            else{
                this.ckb_suspend.setEnabled(false);
                this.ckb_suspend.setSelected(false);
                this.bt_observe.setEnabled(true);
                this.lb_observado.setEnabled(true);
                this.lb_backup.setEnabled(true);
            }
        }
        
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lb_led = new javax.swing.JLabel();
        lb_timestamp = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        bt_visualize = new javax.swing.JButton();
        ckb_multiline = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lb_interval = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lb_timeout = new javax.swing.JLabel();
        ckb_suspend = new javax.swing.JCheckBox();
        bt_observe = new javax.swing.JButton();
        lb_observado = new javax.swing.JTextField();
        lb_backup = new javax.swing.JTextField();

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
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, "Última Observação : "); // NOI18N

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, "Observado : "); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(bt_visualize, "Visualizar"); // NOI18N
        bt_visualize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_visualizeActionPerformed(evt);
            }
        });

        ckb_multiline.setBackground(new java.awt.Color(255, 255, 255));
        org.openide.awt.Mnemonics.setLocalizedText(ckb_multiline, "Multi Linha"); // NOI18N

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, "Dir Backup : "); // NOI18N

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, "Intervalo : "); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(lb_interval, "4000 ms"); // NOI18N
        lb_interval.setToolTipText(org.openide.util.NbBundle.getMessage(MonitorIFrame.class, "MonitorIFrame.lb_interval.toolTipText")); // NOI18N

        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, "tempo Limite : "); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(lb_timeout, "5 min"); // NOI18N
        lb_timeout.setToolTipText(org.openide.util.NbBundle.getMessage(MonitorIFrame.class, "MonitorIFrame.lb_timeout.toolTipText")); // NOI18N

        ckb_suspend.setBackground(new java.awt.Color(255, 255, 255));
        org.openide.awt.Mnemonics.setLocalizedText(ckb_suspend, "Automático"); // NOI18N
        ckb_suspend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckb_suspendActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(bt_observe, "Observar"); // NOI18N
        bt_observe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_observeActionPerformed(evt);
            }
        });

        lb_observado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lb_observadoActionPerformed(evt);
            }
        });

        lb_backup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lb_backupActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(lb_interval)
                        .addGap(63, 63, 63)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lb_timeout)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lb_observado)
                                    .addComponent(lb_backup)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lb_timestamp)
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addComponent(lb_led))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(ckb_multiline)
                                .addGap(18, 18, 18)
                                .addComponent(ckb_suspend)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                                .addComponent(bt_observe)
                                .addGap(33, 33, 33)
                                .addComponent(bt_visualize)))
                        .addGap(32, 32, 32))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_led)
                    .addComponent(lb_timestamp)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lb_observado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lb_backup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lb_interval)
                    .addComponent(jLabel5)
                    .addComponent(lb_timeout))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ckb_multiline)
                    .addComponent(bt_visualize)
                    .addComponent(ckb_suspend)
                    .addComponent(bt_observe))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bt_visualizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_visualizeActionPerformed
 
        NotifyDescriptor nd = new NotifyDescriptor.Message(
                    payload, 
                    NotifyDescriptor.INFORMATION_MESSAGE);
        nd.setTitle("Dados gravados no arquivo :");
        Object retval = DialogDisplayer.getDefault().notify(nd);
  
    }//GEN-LAST:event_bt_visualizeActionPerformed

    private void bt_observeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_observeActionPerformed
       
        SMTraffic suspend_req = new SMTraffic(descriptor.getUID(), descriptor.getUID(), 1,
                                            VirnaServices.STATES.FOB_READ, 
                                            null);
        service.processSignal(suspend_req, descriptor);
    }//GEN-LAST:event_bt_observeActionPerformed

    private void ckb_suspendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckb_suspendActionPerformed
        
        this.bt_observe.setEnabled(!this.ckb_suspend.isSelected());
        
        int code = this.ckb_suspend.isSelected() ? 0:1;
        
        SMTraffic suspend_req = new SMTraffic(descriptor.getUID(), descriptor.getUID(), code,
                                            VirnaServices.STATES.TSK_SUSPEND, 
                                            null);
        service.processSignal(suspend_req, descriptor);
       
    }//GEN-LAST:event_ckb_suspendActionPerformed

    private void lb_observadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lb_observadoActionPerformed
        
        SMTraffic suspend_req = new SMTraffic(descriptor.getUID(), descriptor.getUID(), 0,
                                            VirnaServices.STATES.FOB_SETPATH, 
                                            new VirnaPayload().setString(this.lb_observado.getText()));
        service.processSignal(suspend_req, descriptor);

        // TODO add your handling code here:
    }//GEN-LAST:event_lb_observadoActionPerformed

    private void lb_backupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lb_backupActionPerformed
        SMTraffic suspend_req = new SMTraffic(descriptor.getUID(), descriptor.getUID(), 1,
                                            VirnaServices.STATES.FOB_SETPATH, 
                                            new VirnaPayload().setString(this.lb_backup.getText()));
        service.processSignal(suspend_req, descriptor);
    }//GEN-LAST:event_lb_backupActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_observe;
    private javax.swing.JButton bt_visualize;
    private javax.swing.JCheckBox ckb_multiline;
    private javax.swing.JCheckBox ckb_suspend;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField lb_backup;
    private javax.swing.JLabel lb_interval;
    private javax.swing.JLabel lb_led;
    private javax.swing.JTextField lb_observado;
    private javax.swing.JLabel lb_timeout;
    private javax.swing.JLabel lb_timestamp;
    // End of variables declaration//GEN-END:variables

    @Override
    public void updateUI(Color ledcolor, String message) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}




