/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.csvfilter;

import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.BaseService;
import com.virna5.contexto.ContextUtils;
import com.virna5.contexto.MonitorIFrameInterface;
import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JInternalFrame;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;


public class MonitorIFrame extends JInternalFrame implements MonitorIFrameInterface{

    private CSVFilterDescriptor descriptor;
    private CSVFilterService service;
    protected String iframeid ;
    
    private String payload;
  
    
    public MonitorIFrame() {
        initComponents();
        //timer = new Timer( 500 , new LedTimerListener(lb_led));
          
    }

    
    // ============================================== POINTERS DE MANEJAMENTO ================================================
    /**
     * @return the descriptor
     */
    public CSVFilterDescriptor getDescriptor() {
        return descriptor;
    }

    /**
     * @param descriptor the descriptor to set
     */
    public void setDescriptor(BaseDescriptor _descriptor) {
        this.descriptor = (CSVFilterDescriptor)_descriptor;
        
    }

    /**
     * @return the descriptor
     */
    public CSVFilterService getService() {
        return service;
    }

    /**
     * @param descriptor the descriptor to set
     */
    public void setService(BaseService _service) {
        this.service = (CSVFilterService)_service;
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
                    payload = message;
                }
            }
        };
        EventQueue.invokeLater(worker);   
    }
    
    public void updateUIDirect(CSVFilterUIUpdater fwuu){
        
        if (fwuu.getLedcolor() != null){
            lb_led.setBackground(fwuu.getLedcolor());
            lb_timestamp.setText(ContextUtils.getTimestamp());
        }
        if (fwuu.getPayload() != null){
            this.payload = fwuu.getPayload();
        }
        if (fwuu.getFilename() != null){
            this.payload = fwuu.getFilename();
        }
        
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lb_led = new javax.swing.JLabel();
        lb_timestamp = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lb_arquivo = new javax.swing.JLabel();
        bt_visualize = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        lb_converted = new javax.swing.JLabel();

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
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, "Última Conversão : "); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(bt_visualize, "Visualizar"); // NOI18N
        bt_visualize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_visualizeActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, "Campos Convertidos : "); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(lb_arquivo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bt_visualize))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_timestamp)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(lb_led))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                        .addComponent(lb_converted)
                        .addGap(228, 228, 228)))
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
                    .addComponent(jLabel2)
                    .addComponent(lb_converted))
                .addGap(12, 12, 12)
                .addComponent(bt_visualize)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lb_arquivo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_visualize;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lb_arquivo;
    private javax.swing.JLabel lb_converted;
    private javax.swing.JLabel lb_led;
    private javax.swing.JLabel lb_timestamp;
    // End of variables declaration//GEN-END:variables

}




