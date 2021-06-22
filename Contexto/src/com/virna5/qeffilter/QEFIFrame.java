/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.qeffilter;

import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.BaseService;
import com.virna5.contexto.ContextUtils;
import com.virna5.contexto.MonitorIFrameInterface;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.windows.WindowManager;


public class QEFIFrame extends JInternalFrame implements MonitorIFrameInterface, ActionListener  {

    private static final Logger LOG = Logger.getLogger(QEFIFrame.class.getName());

    
    private QEFFilterDescriptor descriptor;
    private QEFFilterService service;
    protected String iframeid ;
  
    private Timer timer;
 
    private DialogDescriptor d = null;
    
    
    public QEFIFrame() {
        initComponents();
        //timer = new Timer( 500 , new LedTimerListener(lb_led));
          
    }


    // ============================================== POINTERS DE MANEJAMENTO ================================================
    /**
     * @return the descriptor
     */
    public QEFFilterDescriptor getDescriptor() {
        return descriptor;
    }

    /**
     * @param descriptor the descriptor to set
     */
    public void setDescriptor(BaseDescriptor _descriptor) {
        this.descriptor = (QEFFilterDescriptor)_descriptor;
        //ckb_auto.setSelected(descriptor.getAutomatic());
        //bt_simulate.setEnabled(!descriptor.getAutomatic());
    }

    /**
     * @return the descriptor
     */
    public QEFFilterService getService() {
        return service;
    }

    /**
     * @param descriptor the descriptor to set
     */
    public void setService(BaseService _service) {
        this.service = (QEFFilterService)_service;
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
            }
        };
        EventQueue.invokeLater(worker);   
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LOG.info("Dialog OK !");
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
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lb_led = new javax.swing.JLabel();
        lb_timestamp = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        ckb_enable = new javax.swing.JCheckBox();
        bt_examinate = new javax.swing.JButton();
        bt_editranges = new javax.swing.JButton();
        bt_recalculate = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setOpaque(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(null);

        lb_led.setBackground(new java.awt.Color(0, 150, 50));
        org.openide.awt.Mnemonics.setLocalizedText(lb_led, org.openide.util.NbBundle.getMessage(QEFIFrame.class, "QEFIFrame.lb_led.text")); // NOI18N
        lb_led.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lb_led.setOpaque(true);

        lb_timestamp.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, "Última Conversão : "); // NOI18N

        ckb_enable.setBackground(new java.awt.Color(255, 255, 255));
        ckb_enable.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(ckb_enable, "Habilitar Conversão"); // NOI18N
        ckb_enable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckb_enableActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(bt_examinate, "Ver Resultado"); // NOI18N
        bt_examinate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_examinateActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(bt_editranges, "Editar Limites"); // NOI18N
        bt_editranges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_editrangesActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(bt_recalculate, "Recalcular"); // NOI18N
        bt_recalculate.setEnabled(false);
        bt_recalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_recalculateActionPerformed(evt);
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
                        .addComponent(ckb_enable)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                        .addComponent(bt_recalculate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_editranges)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_examinate)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_timestamp)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(lb_led)
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
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bt_examinate)
                    .addComponent(ckb_enable)
                    .addComponent(bt_editranges)
                    .addComponent(bt_recalculate))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ckb_enableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckb_enableActionPerformed
        
        boolean isauto = ckb_enable.isSelected();
        if (isauto){
             bt_examinate.setEnabled(false);            
        }
        else{
             bt_examinate.setEnabled(true);
        }
        
    }//GEN-LAST:event_ckb_enableActionPerformed

    
    private void bt_examinateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_examinateActionPerformed
       
        Frame f = WindowManager.getDefault().getMainWindow();

        QEFInspectorPanel qefipanel = new QEFInspectorPanel(descriptor);
        d = new DialogDescriptor(qefipanel, "Conversão QEF", true, this);
        d.setClosingOptions(new Object[]{});
        DialogDisplayer.getDefault().notifyLater(d);
        
    }//GEN-LAST:event_bt_examinateActionPerformed

    private void bt_editrangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_editrangesActionPerformed
        
        Frame f = WindowManager.getDefault().getMainWindow();

        QEFFieldsPanel qefipanel = new QEFFieldsPanel(descriptor.getQEFfields());
        d = new DialogDescriptor(qefipanel, "Conversão QEF", true, this);
        d.setClosingOptions(new Object[]{});
        DialogDisplayer.getDefault().notifyLater(d);
    }//GEN-LAST:event_bt_editrangesActionPerformed

    private void bt_recalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_recalculateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bt_recalculateActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_editranges;
    private javax.swing.JButton bt_examinate;
    private javax.swing.JButton bt_recalculate;
    private javax.swing.JCheckBox ckb_enable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lb_led;
    private javax.swing.JLabel lb_timestamp;
    // End of variables declaration//GEN-END:variables

}




