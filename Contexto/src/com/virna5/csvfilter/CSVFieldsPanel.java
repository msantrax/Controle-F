/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.csvfilter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.virna5.contexto.ContextUtils;
import java.io.IOException;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;

/**
 *
 * @author opus
 */
public class CSVFieldsPanel extends javax.swing.JPanel implements TableModelListener{

    private static final Logger log = Logger.getLogger(CSVFieldsPanel.class.getName());

    
    protected CSVFieldsWrapper cfw;
    
    
    CSVFieldsPanel(CSVFieldsWrapper cfw) {
        this.cfw = cfw;
        initComponents();
        
        JComboBox realmcb = new JComboBox(CSVField.getRealmTypes());
        TableCellEditor realmeditor = new DefaultCellEditor(realmcb);
        TableColumn realmcolumn = jtbl_fields.getColumnModel().getColumn(3);
        realmcolumn.setCellEditor(realmeditor);
        
        JComboBox typecb = new JComboBox(CSVField.getTypeTypes());
        TableCellEditor typeeditor = new DefaultCellEditor(typecb);
        TableColumn typecolumn = jtbl_fields.getColumnModel().getColumn(4);
        typecolumn.setCellEditor(typeeditor);
        
        model.addTableModelListener(this);
        
    }

    
    @Override
    public void tableChanged(TableModelEvent e) {
        log.info("table changed"); 
    }
    
    private void addRow(){
        
        int srow=jtbl_fields.getSelectedRow();
        
        if(srow == -1){
            cfw.add(CSVField.CSVFieldFactory(cfw.size()));
        }
        else{
            cfw.add(srow, CSVField.CSVFieldFactory(cfw.size()));
        }
        
        jtbl_fields.updateUI();
        
    }
    
    private void removeRow(){
        
        int srow=jtbl_fields.getSelectedRow();
        
        if(srow == -1){
            if (cfw.size() != 0){
                cfw.remove(cfw.size()-1);
            }
        }
        else{
            cfw.remove(srow);
        }       
        jtbl_fields.updateUI();
        
    }
    
    
    private void riseRow(){
        
        int srow=jtbl_fields.getSelectedRow();
            
         if(srow > 0){
             CSVField thisrow = cfw.get(srow);
             CSVField previousrow = cfw.get(srow-1);
             cfw.set(srow-1, thisrow);
             cfw.set(srow, previousrow);
             jtbl_fields.changeSelection(srow-1, 0, false, false);
             jtbl_fields.updateUI();
        }
    
    }
    
    private void lowerRow(){
        
        int srow=jtbl_fields.getSelectedRow();
            
         if(srow != -1 && srow < cfw.size()-1){
             CSVField thisrow = cfw.get(srow);
             CSVField previousrow = cfw.get(srow+1);
             cfw.set(srow+1, thisrow);
             cfw.set(srow, previousrow);
             jtbl_fields.changeSelection(srow+1, 0, false, false);
             jtbl_fields.updateUI();
        }
    
    }
    
    final TableModel model = new AbstractTableModel(){
    
        @Override
        public int getColumnCount() {
            return 6;
        }
        
        @Override
        public String getColumnName(int column) {
            return cnames[column];
        }
        
        @Override
        public int getRowCount() {
            return cfw.size();
        }
        
        @Override
        public Object getValueAt(int row, int column) {
            CSVField csvf = cfw.get(row);
            Object o = new Object();
            
            switch (column) {
                case 0:
                    o=csvf.getCSVfield();
                    break;
                case 1:
                    o=csvf.getResultfield();
                    break;
                case 2:
                    o=csvf.getResultseq();
                    break;
                case 3:
                    o= csvf.getRealm();
                    break;
                case 4:
                    o= csvf.getType();
                    break;    
                case 5:
                    o= csvf.isReadcsv();
                    break;    
                    
                default:
                    break;
            }
            
            return o;
        }

        public void setValueAt(Object value, int row, int column) {
            CSVField csvf = cfw.get(row);
            
            switch (column) {
                case 0:
                    csvf.setCSVfield((String)value);
                    break;
                case 1:
                    csvf.setResultfield((String)value);
                    break;
                case 2:
                    csvf.setResultseq((Integer)value);
                    break;
                case 3:
                    csvf.setRealm((String)value);
                    break;
                case 4:
                    csvf.setType((String)value);
                    break;    
                case 5:
                    csvf.setReadcsv((Boolean)value);
                    break;    
                    
                default:
                    break;
            }
   
        }

        
        String[] cnames = new String[]{
           "Campo CSV", "Resultado", "Seq", "Classe", "Tipo", "Incluir"  
        };
        
        Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class,
                java.lang.Integer.class,
                java.lang.String.class, java.lang.String.class,
                java.lang.Boolean.class
        };

        @Override
        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return true;
        }

    };
    
    private void buildTemplate(){
        
        String s = ContextUtils.selectInputFile(null);
        if (s !=null){
            NotifyDescriptor nd = new NotifyDescriptor.Message(
                    "<html>Erro na interpretação do arquivo, verifique se : "
                  + "<ul><li>O separador de campos corresponde ao selecionado</li>"
                  + "<li>A codificação dos caracteres é o padrão</li>"
                  + "</ul></html>", 
                    NotifyDescriptor.ERROR_MESSAGE);
            Object retval = DialogDisplayer.getDefault().notify(nd);
        }
    }
    
    private void loadTemplate(){
        
        String s = ContextUtils.selectInputFile(null);
        if (s !=null){
            try {
                String payload = ContextUtils.loadFile (s);
                GsonBuilder builder = new GsonBuilder(); 
                //builder.registerTypeAdapter(ContextNodes.class, new ContextNodesDeserializer()); 
                builder.setPrettyPrinting(); 
                Gson gson = builder.create();
                CSVFieldsWrapper tmpl = gson.fromJson(payload, CSVFieldsWrapper.class);
                cfw = tmpl;
                jtbl_fields.updateUI();                
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }  
    }
    
    private void saveTemplate(){
        
         NotifyDescriptor nd = new NotifyDescriptor.Confirmation(
                    "<html>Devo ordenar primeiro os campos de sequencia ?<br>"
                  + "A alteração"
                  + "</html>", 
                    NotifyDescriptor.ERROR_MESSAGE);
        Object retval = DialogDisplayer.getDefault().notify(nd);
        if ((Integer)retval == 0){
            int counter = 1;
            for (CSVField csvf : cfw){
                if (csvf.isReadcsv()){
                    csvf.setResultseq(counter);
                    counter++;
                }
                else{
                     csvf.setResultseq(0);
                }
            }
        }
        
        
        String s = ContextUtils.selectInputFile(null);
        if (s !=null){
            try {
                GsonBuilder builder = new GsonBuilder(); 
                //builder.registerTypeAdapter(ContextNodes.class, new ContextNodesDeserializer()); 
                builder.setPrettyPrinting(); 
                Gson gson = builder.create();
                String sjson = gson.toJson(cfw);
                ContextUtils.saveJson(s, sjson);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }  
        
        
        
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtbl_fields = new javax.swing.JTable();
        bt_add = new javax.swing.JButton();
        bt_sub = new javax.swing.JButton();
        bt_up = new javax.swing.JButton();
        bt_down = new javax.swing.JButton();
        bt_load = new javax.swing.JButton();
        br_import = new javax.swing.JButton();
        bt_save = new javax.swing.JButton();

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 18)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/virna5/graphimages/control1.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, "   Filtro CSV - Edição do gabarito de campos"); // NOI18N
        jLabel1.setToolTipText(org.openide.util.NbBundle.getMessage(CSVFieldsPanel.class, "CSVFieldsPanel.jLabel1.toolTipText")); // NOI18N
        jLabel1.setOpaque(true);

        jtbl_fields.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jtbl_fields.setModel(model);
        jtbl_fields.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(jtbl_fields);

        bt_add.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        bt_add.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/virna5/graphimages/add.png"))); // NOI18N
        bt_add.setToolTipText("Adicione um Campo a lista"); // NOI18N
        bt_add.setPreferredSize(new java.awt.Dimension(60, 29));
        bt_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_addActionPerformed(evt);
            }
        });

        bt_sub.setFont(new java.awt.Font("DejaVu Sans", 1, 18)); // NOI18N
        bt_sub.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/virna5/graphimages/remove.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(bt_sub, org.openide.util.NbBundle.getMessage(CSVFieldsPanel.class, "CSVFieldsPanel.bt_sub.text")); // NOI18N
        bt_sub.setToolTipText("Remova o campo selecionado"); // NOI18N
        bt_sub.setPreferredSize(new java.awt.Dimension(60, 29));
        bt_sub.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_subActionPerformed(evt);
            }
        });

        bt_up.setFont(new java.awt.Font("DejaVu Sans", 1, 18)); // NOI18N
        bt_up.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/virna5/graphimages/1uparrow.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(bt_up, org.openide.util.NbBundle.getMessage(CSVFieldsPanel.class, "CSVFieldsPanel.bt_up.text")); // NOI18N
        bt_up.setToolTipText("Mova o Campo abaixo"); // NOI18N
        bt_up.setPreferredSize(new java.awt.Dimension(60, 29));
        bt_up.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_upActionPerformed(evt);
            }
        });

        bt_down.setFont(new java.awt.Font("DejaVu Sans", 1, 18)); // NOI18N
        bt_down.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/virna5/graphimages/1downarrow.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(bt_down, org.openide.util.NbBundle.getMessage(CSVFieldsPanel.class, "CSVFieldsPanel.bt_down.text")); // NOI18N
        bt_down.setToolTipText("Mova o Campo acima"); // NOI18N
        bt_down.setPreferredSize(new java.awt.Dimension(60, 29));
        bt_down.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_downActionPerformed(evt);
            }
        });

        bt_load.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/virna5/graphimages/open.gif"))); // NOI18N
        bt_load.setToolTipText("<html>\nInicie uma estrutura de campos padrão.<br>\n<p style =\"color:red\"><b>Atenção ! :</b>Isso irá apagar a edição atual</p>\n</html>"); // NOI18N
        bt_load.setPreferredSize(new java.awt.Dimension(60, 29));
        bt_load.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_loadActionPerformed(evt);
            }
        });

        br_import.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/virna5/graphimages/image.gif"))); // NOI18N
        br_import.setToolTipText("<html>\nInterprete um arquivo CSV existente e monte a estrutura\nde campos<br>conforme o conteúdo. O arquivo deverá:<br>\n<ul>\n<li>Possuir um cabeçalho com o nome dos campos.</li>\n<li>Preferencialmente ter os nomes sem caracteres acentuados.</li>\n</ul>\n</html>"); // NOI18N
        br_import.setPreferredSize(new java.awt.Dimension(60, 29));
        br_import.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                br_importActionPerformed(evt);
            }
        });

        bt_save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/virna5/graphimages/save.gif"))); // NOI18N
        bt_save.setToolTipText("<html>\nInicie uma estrutura de campos padrão.<br>\n<p style =\"color:red\"><b>Atenção ! :</b>Isso irá apagar a edição atual</p>\n</html>"); // NOI18N
        bt_save.setPreferredSize(new java.awt.Dimension(60, 29));
        bt_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_saveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(bt_add, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_sub, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(bt_down, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_up, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(bt_load, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_save, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(br_import, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(bt_up, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bt_add, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bt_sub, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(bt_down, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(bt_load, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(br_import, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(bt_save, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void bt_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_addActionPerformed
        addRow();
    }//GEN-LAST:event_bt_addActionPerformed

    private void bt_subActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_subActionPerformed
        removeRow();
    }//GEN-LAST:event_bt_subActionPerformed

    private void bt_downActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_downActionPerformed
        riseRow();
        //lowerRow();
    }//GEN-LAST:event_bt_downActionPerformed

    private void bt_upActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_upActionPerformed
        lowerRow();
        //riseRow();
    }//GEN-LAST:event_bt_upActionPerformed

    private void br_importActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_br_importActionPerformed
        buildTemplate();
    }//GEN-LAST:event_br_importActionPerformed

    private void bt_loadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_loadActionPerformed
        loadTemplate();
    }//GEN-LAST:event_bt_loadActionPerformed

    private void bt_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_saveActionPerformed
        saveTemplate();
    }//GEN-LAST:event_bt_saveActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton br_import;
    private javax.swing.JButton bt_add;
    private javax.swing.JButton bt_down;
    private javax.swing.JButton bt_load;
    private javax.swing.JButton bt_save;
    private javax.swing.JButton bt_sub;
    private javax.swing.JButton bt_up;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jtbl_fields;
    // End of variables declaration//GEN-END:variables

}



//jTable1.setModel(new javax.swing.table.DefaultTableModel(
//            new Object [][] {
//                {"Corrida", "Header", "Texto",  new Boolean(true)},
//                {"C", "Valor", "Numero",  new Boolean(true)},
//                {"V", "Valor", "Numero",  new Boolean(true)}
//            },
//            new String [] {
//                "Nome", "Classe", "Tipo", "Usado"
//            }
//        ) {
//            Class[] types = new Class [] {
//                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
//            };
//
//            public Class getColumnClass(int columnIndex) {
//                return types [columnIndex];
//            }
//        });
//        jScrollPane3.setViewportView(jTable1);
