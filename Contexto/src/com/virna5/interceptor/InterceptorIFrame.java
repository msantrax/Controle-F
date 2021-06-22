/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.interceptor;

import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.BaseService;
import com.virna5.contexto.ContextUtils;
import com.virna5.contexto.MonitorIFrameInterface;
import java.awt.Color;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import org.openide.util.Exceptions;

/**
 *
 * @author opus
 */
public class InterceptorIFrame extends JInternalFrame implements MonitorIFrameInterface {

    private static final Logger LOG = Logger.getLogger(InterceptorIFrame.class.getName());
 
    private InterceptorDescriptor descriptor;
    private InterceptorService service;
    protected String iframeid ;
  
    private InterceptModel vm ;
    private IndexComboBoxModel index_cbmodel;
    
    
    /**
     * Creates new form InterceptorIFrame
     */
    public InterceptorIFrame() {
        
        index_cbmodel = new IndexComboBoxModel();
        initComponents();
        this.cb_index.setSelectedIndex(0);
        this.cb_index.updateUI();
        
        
        //populateValues();
        
    }

    public void initModel(InterceptorDescriptor desc){
        
        vm = new InterceptModel(this, desc);
        this.setTitle(vm.getTemplate().getTitle());        
        populateHeader();
    }
    
    
    public void iconifyFrame(boolean icon){
        
        try {
            this.setIcon(icon);
        } catch (PropertyVetoException ex) {
            Exceptions.printStackTrace(ex);
        }
        
    }
    
    public void updateStatusBar(String mes){
        
        this.statusbar.setText(ContextUtils.getCompactTimestamp()+" => "+mes);
    }
    
    
    // ============================================== POINTERS DE MANEJAMENTO ================================================
    /**
     * @return the descriptor
     */
    public InterceptorDescriptor getDescriptor() {
        return descriptor;
    }

    /**
     * @param descriptor the descriptor to set
     */
    public void setDescriptor(BaseDescriptor _descriptor) {
        this.descriptor = (InterceptorDescriptor)_descriptor;
        initModel(descriptor);
        
    }

    /**
     * @return the descriptor
     */
    public InterceptorService getService() {
        return service;
    }

    /**
     * @param descriptor the descriptor to set
     */
    public void setService(BaseService _service) {
        this.service = (InterceptorService)_service;
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
    
 
    // ============================================== Particular  ================================================
    
    public void addIndexItem (String item, int group){  
        setIndexGroup(group);
        index_cbmodel.addItem(item);
        this.cb_index.setSelectedIndex(this.cb_index.getItemCount()-1);
        //this.cb_index.updateUI();
    }
    
    public void removeIndexItem (String item){  
        
        index_cbmodel.removeItem(item);
        
        //this.cb_index.setSelectedIndex(this.cb_index.getItemCount()-1);
        //this.cb_index.updateUI();
    }
    
    
  
    public void setIndexGroup(int group) {
        index_cbmodel.setGroup(group);
        switch (group) {
            case 1:
                this.rb_validated.setSelected(true);
                break;
            case 2:
                this.rb_hold.setSelected(true);
                break;
            default:
                this.rb_default.setSelected(true);
                break;
        }
        
    }
    
    public int getIndexGroup() { return index_cbmodel.group;}
    
    public String getActiveElement() { return index_cbmodel.getActive(); }
    
    public void activateIndexGroup (int group){
        
        index_cbmodel.setGroup(group);
        this.cb_index.setSelectedIndex(this.cb_index.getItemCount()-1);
        this.cb_index.updateUI();
    }

    @Override
    public void updateUI(Color ledcolor, String message) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public class IndexComboBoxModel extends DefaultComboBoxModel{

        public static final String EMPTYLABEL = "Não há analises nessa categoria...";
        
        private int group = 0;
        private List<List<String>> groups = new ArrayList<List<String>>();
        
        
        public IndexComboBoxModel() {
           
            for (int i = 0; i < 3; i++) {
                ArrayList<String> lgroup = new ArrayList<>();
                lgroup.add( EMPTYLABEL);
                groups.add(lgroup);
            }
        }

        
        public void setGroup(int _group){this.group = _group; }
        
        public int getGroup(){return group;}
        
        public void addItem (String item){
            
            if (groups.get(group).contains(item)) return;
            
            if (groups.get(group).get(0).equals(EMPTYLABEL)){
                groups.get(group).clear();
            }
            groups.get(group).add(item);
        }
        
        public void removeItem(String sitem){
            
            if (groups.get(group).size() == 1){
                groups.get(group).clear();
                groups.get(group).add(EMPTYLABEL);
            }
            else{
               groups.get(group).remove(sitem);
            }
        }
 
        @Override
        public Object getElementAt(int pos){
            return groups.get(group).get(pos);
        }
        
        public String getActive(){
            
            String out = (String)getSelectedItem();
            return out;
        }
        
        @Override
        public int getSize(){
            return groups.get(group).size();
        }
    };
  
    public void clearCanvas(){
        
        pnl_id.removeAll();
        pnl_values.removeAll();
        
    }
    
    
    public void populateHeader(){
        
        HeaderItemPanel hip;
        pnl_id.removeAll();
        
        for (ItemDescriptor id : getModel().getHeaderItems()){
            hip = new HeaderItemPanel(id.getLength(), id.getLabel(), id.getValue(), getModel());
            id.setHpanel(hip);
            hip.setStatus(id.getIcon());
            //hip.setStatus(vm.getStatusIcon(id.getQualify_type()));
            this.pnl_id.add(hip);
        }
        this.pnl_id.updateUI();
    }
    
    
    public void populateValues(){
        
        ValuePanel vp;
        pnl_values.removeAll();
        
        for (ItemDescriptor id : getModel().getValueItems()){
            vp = new ValuePanel(id.getLabel(), id.getValue(), getModel());
            vp.setStatus(id.getIcon());
            //vp.setStatus(vm.getStatusIcon(id.getQualify_type()));
            id.setVpanel(vp);
            this.pnl_values.add(vp);
        }
        this.pnl_values.updateUI();
        
    }
  
    
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of
     * this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btg_indextypes = new javax.swing.ButtonGroup();
        pnl_index = new javax.swing.JPanel();
        cb_index = new javax.swing.JComboBox<>();
        rb_default = new javax.swing.JRadioButton();
        rb_validated = new javax.swing.JRadioButton();
        rb_hold = new javax.swing.JRadioButton();
        pnl_header = new javax.swing.JPanel();
        pnl_id = new javax.swing.JPanel();
        pnl_calib = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        pnl_values = new javax.swing.JPanel();
        pnl_buttons = new javax.swing.JPanel();
        bt_loadrecord = new javax.swing.JButton();
        bt_saverecord = new javax.swing.JButton();
        statusbar = new javax.swing.JLabel();
        bt_sendrecord = new javax.swing.JButton();
        cb_intercept = new javax.swing.JCheckBox();

        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(org.openide.util.NbBundle.getMessage(InterceptorIFrame.class, "InterceptorIFrame.title")); // NOI18N
        setPreferredSize(new java.awt.Dimension(1000, 480));
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        pnl_index.setBackground(new java.awt.Color(255, 255, 255));
        pnl_index.setForeground(new java.awt.Color(0, 102, 0));

        cb_index.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cb_index.setForeground(new java.awt.Color(102, 102, 102));
        cb_index.setModel(index_cbmodel);
        cb_index.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_indexActionPerformed(evt);
            }
        });

        rb_default.setBackground(new java.awt.Color(255, 255, 255));
        btg_indextypes.add(rb_default);
        rb_default.setFont(new java.awt.Font("DejaVu Sans", 1, 10)); // NOI18N
        rb_default.setForeground(new java.awt.Color(102, 102, 102));
        rb_default.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(rb_default, "Não Qualificadas"); // NOI18N
        rb_default.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_defaultActionPerformed(evt);
            }
        });

        rb_validated.setBackground(new java.awt.Color(255, 255, 255));
        btg_indextypes.add(rb_validated);
        rb_validated.setFont(new java.awt.Font("DejaVu Sans", 1, 10)); // NOI18N
        rb_validated.setForeground(new java.awt.Color(102, 102, 102));
        org.openide.awt.Mnemonics.setLocalizedText(rb_validated, "Processadas"); // NOI18N
        rb_validated.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_validatedActionPerformed(evt);
            }
        });

        rb_hold.setBackground(new java.awt.Color(255, 255, 255));
        btg_indextypes.add(rb_hold);
        rb_hold.setFont(new java.awt.Font("DejaVu Sans", 1, 10)); // NOI18N
        rb_hold.setForeground(new java.awt.Color(102, 102, 102));
        org.openide.awt.Mnemonics.setLocalizedText(rb_hold, "Aguardando"); // NOI18N
        rb_hold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_holdActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_indexLayout = new javax.swing.GroupLayout(pnl_index);
        pnl_index.setLayout(pnl_indexLayout);
        pnl_indexLayout.setHorizontalGroup(
            pnl_indexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_indexLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(rb_default)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rb_validated)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rb_hold)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cb_index, 0, 686, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnl_indexLayout.setVerticalGroup(
            pnl_indexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_indexLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pnl_indexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cb_index, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rb_default)
                    .addComponent(rb_validated)
                    .addComponent(rb_hold))
                .addGap(13, 13, 13))
        );

        rb_default.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(InterceptorIFrame.class, "InterceptorIFrame.rb_default.AccessibleContext.accessibleName")); // NOI18N
        rb_validated.getAccessibleContext().setAccessibleName("validated_rbt");

        getContentPane().add(pnl_index);

        pnl_header.setBackground(new java.awt.Color(0, 153, 153));
        pnl_header.setPreferredSize(new java.awt.Dimension(853, 150));
        pnl_header.setLayout(new javax.swing.BoxLayout(pnl_header, javax.swing.BoxLayout.Y_AXIS));

        pnl_id.setBackground(new java.awt.Color(255, 255, 255));
        pnl_id.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(InterceptorIFrame.class, "InterceptorIFrame.pnl_id.border.title_1"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 1, 10), new java.awt.Color(102, 102, 102))); // NOI18N
        pnl_id.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));
        pnl_header.add(pnl_id);

        getContentPane().add(pnl_header);

        pnl_calib.setBackground(new java.awt.Color(255, 255, 255));
        pnl_calib.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(InterceptorIFrame.class, "InterceptorIFrame.pnl_calib.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 1, 10), new java.awt.Color(102, 102, 102))); // NOI18N
        pnl_calib.setPreferredSize(new java.awt.Dimension(1037, 70));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 153, 153));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/virna5/interceptor/no.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, "Calibração associada a liga : "); // NOI18N
        jLabel1.setOpaque(true);

        jTextField1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jTextField1.setText("Nenhum registro associado"); // NOI18N
        jTextField1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 5));

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 153, 153));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, "Por : "); // NOI18N

        jTextField2.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextField2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(153, 153, 153));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, "Analise de Validação : "); // NOI18N

        jTextField3.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextField3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        javax.swing.GroupLayout pnl_calibLayout = new javax.swing.GroupLayout(pnl_calib);
        pnl_calib.setLayout(pnl_calibLayout);
        pnl_calibLayout.setHorizontalGroup(
            pnl_calibLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_calibLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnl_calibLayout.setVerticalGroup(
            pnl_calibLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_calibLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnl_calibLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getContentPane().add(pnl_calib);

        pnl_values.setBackground(new java.awt.Color(255, 255, 255));
        pnl_values.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(InterceptorIFrame.class, "InterceptorIFrame.pnl_values.border.title_1"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 1, 10), new java.awt.Color(102, 102, 102))); // NOI18N
        pnl_values.setPreferredSize(new java.awt.Dimension(853, 300));
        pnl_values.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 7, 7));
        getContentPane().add(pnl_values);

        org.openide.awt.Mnemonics.setLocalizedText(bt_loadrecord, "Recuperar"); // NOI18N
        bt_loadrecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_loadrecordActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(bt_saverecord, "Arquivar"); // NOI18N
        bt_saverecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_saverecordActionPerformed(evt);
            }
        });

        statusbar.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(statusbar, "Interceptor ativado"); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(bt_sendrecord, "Enviar Análise"); // NOI18N
        bt_sendrecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_sendrecordActionPerformed(evt);
            }
        });

        cb_intercept.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        cb_intercept.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(cb_intercept, org.openide.util.NbBundle.getMessage(InterceptorIFrame.class, "InterceptorIFrame.cb_intercept.text")); // NOI18N
        cb_intercept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_interceptActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_buttonsLayout = new javax.swing.GroupLayout(pnl_buttons);
        pnl_buttons.setLayout(pnl_buttonsLayout);
        pnl_buttonsLayout.setHorizontalGroup(
            pnl_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_buttonsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusbar, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(bt_loadrecord)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_saverecord)
                .addGap(18, 18, 18)
                .addComponent(bt_sendrecord)
                .addGap(18, 18, 18)
                .addComponent(cb_intercept)
                .addContainerGap())
        );
        pnl_buttonsLayout.setVerticalGroup(
            pnl_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_buttonsLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pnl_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bt_loadrecord)
                    .addComponent(bt_saverecord)
                    .addComponent(bt_sendrecord)
                    .addComponent(cb_intercept)
                    .addComponent(statusbar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(1, 1, 1))
        );

        getContentPane().add(pnl_buttons);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bt_loadrecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_loadrecordActionPerformed
       
        String loadpath = ContextUtils.selectFile(false, ContextUtils.CONTEXTDIR + ContextUtils.file_separator+"records" , "rec");
        if (loadpath != null){
            getModel().loadRecord(loadpath, false);
        }
        
    }//GEN-LAST:event_bt_loadrecordActionPerformed

    private void bt_saverecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_saverecordActionPerformed
        
        String loadpath = ContextUtils.selectFile(true,ContextUtils.CONTEXTDIR + ContextUtils.file_separator+"records" , "rec"); 
        if (loadpath != null){
            getModel().saveTemplate(loadpath);
        }   
    }//GEN-LAST:event_bt_saverecordActionPerformed

    private void bt_sendrecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_sendrecordActionPerformed

        getModel().sendRecord();
    }//GEN-LAST:event_bt_sendrecordActionPerformed

    private void rb_defaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_defaultActionPerformed
        activateIndexGroup (0);
        //if (cb_index != null){
             //updateResultsView(cb_index);
        //}
    }//GEN-LAST:event_rb_defaultActionPerformed

    private void rb_validatedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_validatedActionPerformed
        activateIndexGroup (1);
        //updateResultsView(cb_index);
    }//GEN-LAST:event_rb_validatedActionPerformed

    private void rb_holdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_holdActionPerformed
        activateIndexGroup (2);
        //updateResultsView(cb_index);
    }//GEN-LAST:event_rb_holdActionPerformed

    private void cb_indexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_indexActionPerformed
        
        JComboBox jcb = (JComboBox)evt.getSource();
        updateResultsView(jcb);
        
   
    }//GEN-LAST:event_cb_indexActionPerformed

    private void updateResultsView(JComboBox jcb){
        
        if (getModel() ==  null) return;
        
        if (jcb != null){
            Object obj = jcb.getSelectedItem();
            if (obj != null) {
                String s = jcb.getSelectedItem().toString();
                if (!s.equals(IndexComboBoxModel.EMPTYLABEL)){
                    //LOG.info("Index Selected");
                    getModel().updateView(s, index_cbmodel.getGroup());
                }
                else{
                    clearCanvas();
                }
            }   
        }
    }
    
    
    private void cb_interceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_interceptActionPerformed
        getModel().setIntercept(cb_intercept.isSelected());
    }//GEN-LAST:event_cb_interceptActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_loadrecord;
    private javax.swing.JButton bt_saverecord;
    private javax.swing.JButton bt_sendrecord;
    private javax.swing.ButtonGroup btg_indextypes;
    private javax.swing.JComboBox<String> cb_index;
    private javax.swing.JCheckBox cb_intercept;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JPanel pnl_buttons;
    private javax.swing.JPanel pnl_calib;
    private javax.swing.JPanel pnl_header;
    private javax.swing.JPanel pnl_id;
    private javax.swing.JPanel pnl_index;
    private javax.swing.JPanel pnl_values;
    private javax.swing.JRadioButton rb_default;
    private javax.swing.JRadioButton rb_hold;
    private javax.swing.JRadioButton rb_validated;
    private javax.swing.JLabel statusbar;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the vm
     */
    public InterceptModel getModel() {
        return vm;
    }
}
