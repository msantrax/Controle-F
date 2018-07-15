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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;

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
        
        vm = new InterceptModel(this);
        this.setTitle(vm.getTemplate().getTitle());
        
        populateHeader();
        //populateValues();
        
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
        this.cb_index.updateUI();
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
    
    public void activateIndexGroup (int group){
        
        index_cbmodel.setGroup(group);
        this.cb_index.setSelectedIndex(this.cb_index.getItemCount()-1);
        this.cb_index.updateUI();
    }

    @Override
    public void updateUI(Color ledcolor, String message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        
        @Override
        public int getSize(){
            return groups.get(group).size();
        }
    };
  
    
    public void populateHeader(){
        
        HeaderItemPanel hip;
        pnl_id.removeAll();
        
        for (ItemDescriptor id : vm.getHeaderItems()){
            hip = new HeaderItemPanel(id.getLength(), id.getLabel(), id.getValue(), vm);
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
        
        for (ItemDescriptor id : vm.getValueItems()){
            vp = new ValuePanel(id.getLabel(), id.getValue(), vm);
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
        tb_edit = new javax.swing.JToggleButton();
        pnl_header = new javax.swing.JPanel();
        pnl_id = new javax.swing.JPanel();
        pnl_values = new javax.swing.JPanel();
        pnl_buttons = new javax.swing.JPanel();
        bt_loadrecord = new javax.swing.JButton();
        bt_savetemplate = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(org.openide.util.NbBundle.getMessage(InterceptorIFrame.class, "InterceptorIFrame.title")); // NOI18N
        setPreferredSize(new java.awt.Dimension(1200, 600));
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        pnl_index.setBackground(new java.awt.Color(0, 153, 153));
        pnl_index.setForeground(new java.awt.Color(0, 102, 0));
        pnl_index.setPreferredSize(new java.awt.Dimension(1028, 50));

        cb_index.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        cb_index.setModel(index_cbmodel);
        cb_index.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_indexActionPerformed(evt);
            }
        });

        rb_default.setBackground(new java.awt.Color(0, 153, 153));
        btg_indextypes.add(rb_default);
        rb_default.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        rb_default.setForeground(new java.awt.Color(255, 255, 255));
        rb_default.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(rb_default, "Não Qualificadas"); // NOI18N
        rb_default.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_defaultActionPerformed(evt);
            }
        });

        rb_validated.setBackground(new java.awt.Color(0, 153, 153));
        btg_indextypes.add(rb_validated);
        rb_validated.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        rb_validated.setForeground(new java.awt.Color(255, 255, 255));
        org.openide.awt.Mnemonics.setLocalizedText(rb_validated, "Processadas"); // NOI18N
        rb_validated.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_validatedActionPerformed(evt);
            }
        });

        rb_hold.setBackground(new java.awt.Color(0, 153, 153));
        btg_indextypes.add(rb_hold);
        rb_hold.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        rb_hold.setForeground(new java.awt.Color(255, 255, 255));
        org.openide.awt.Mnemonics.setLocalizedText(rb_hold, "Aguardando"); // NOI18N
        rb_hold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_holdActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(tb_edit, "Editar"); // NOI18N

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
                .addComponent(cb_index, 0, 487, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tb_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(rb_hold)
                    .addComponent(tb_edit))
                .addGap(13, 13, 13))
        );

        rb_default.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(InterceptorIFrame.class, "InterceptorIFrame.rb_default.AccessibleContext.accessibleName")); // NOI18N
        rb_validated.getAccessibleContext().setAccessibleName("validated_rbt");

        getContentPane().add(pnl_index);

        pnl_header.setBackground(new java.awt.Color(0, 153, 153));
        pnl_header.setPreferredSize(new java.awt.Dimension(853, 150));
        pnl_header.setLayout(new javax.swing.BoxLayout(pnl_header, javax.swing.BoxLayout.Y_AXIS));

        pnl_id.setBackground(new java.awt.Color(215, 215, 225));
        pnl_id.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)), org.openide.util.NbBundle.getMessage(InterceptorIFrame.class, "InterceptorIFrame.pnl_id.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        pnl_id.setOpaque(false);
        pnl_id.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));
        pnl_header.add(pnl_id);

        getContentPane().add(pnl_header);

        pnl_values.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true), org.openide.util.NbBundle.getMessage(InterceptorIFrame.class, "InterceptorIFrame.pnl_values.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 1, 14))); // NOI18N
        pnl_values.setPreferredSize(new java.awt.Dimension(853, 300));
        pnl_values.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        getContentPane().add(pnl_values);

        org.openide.awt.Mnemonics.setLocalizedText(bt_loadrecord, "Load Record"); // NOI18N
        bt_loadrecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_loadrecordActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(bt_savetemplate, "Save Template"); // NOI18N
        bt_savetemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_savetemplateActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, "Interceptor"); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, "Teste"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_buttonsLayout = new javax.swing.GroupLayout(pnl_buttons);
        pnl_buttons.setLayout(pnl_buttonsLayout);
        pnl_buttonsLayout.setHorizontalGroup(
            pnl_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_buttonsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 662, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_loadrecord)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_savetemplate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addGap(56, 56, 56))
        );
        pnl_buttonsLayout.setVerticalGroup(
            pnl_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_buttonsLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pnl_buttonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bt_loadrecord)
                    .addComponent(jLabel2)
                    .addComponent(bt_savetemplate)
                    .addComponent(jButton1))
                .addGap(1, 1, 1))
        );

        getContentPane().add(pnl_buttons);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bt_loadrecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_loadrecordActionPerformed
       
        String loadpath = ContextUtils.selectFile(false, ContextUtils.CONTEXTDIR+"area3", "rec");
        vm.loadRecord(loadpath);
    }//GEN-LAST:event_bt_loadrecordActionPerformed

    private void bt_savetemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_savetemplateActionPerformed
        
        String loadpath = ContextUtils.selectFile(true,ContextUtils.CONTEXTDIR+"area3", "tmpl");
        vm.saveTemplate(loadpath);
        
    }//GEN-LAST:event_bt_savetemplateActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        //vm.TestHook1();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void rb_defaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_defaultActionPerformed
        activateIndexGroup (0);
    }//GEN-LAST:event_rb_defaultActionPerformed

    private void rb_validatedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_validatedActionPerformed
        activateIndexGroup (1);
    }//GEN-LAST:event_rb_validatedActionPerformed

    private void rb_holdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_holdActionPerformed
        activateIndexGroup (2);
    }//GEN-LAST:event_rb_holdActionPerformed

    private void cb_indexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_indexActionPerformed
        
        JComboBox jcb = (JComboBox)evt.getSource();
        String s = jcb.getSelectedItem().toString();
        
        if (!s.equals(IndexComboBoxModel.EMPTYLABEL)){
            //LOG.info("Index Selected");
            vm.updateView(s, index_cbmodel.getGroup());
        }
        
        
        
    }//GEN-LAST:event_cb_indexActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_loadrecord;
    private javax.swing.JButton bt_savetemplate;
    private javax.swing.ButtonGroup btg_indextypes;
    private javax.swing.JComboBox<String> cb_index;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel pnl_buttons;
    private javax.swing.JPanel pnl_header;
    private javax.swing.JPanel pnl_id;
    private javax.swing.JPanel pnl_index;
    private javax.swing.JPanel pnl_values;
    private javax.swing.JRadioButton rb_default;
    private javax.swing.JRadioButton rb_hold;
    private javax.swing.JRadioButton rb_validated;
    private javax.swing.JToggleButton tb_edit;
    // End of variables declaration//GEN-END:variables
}
