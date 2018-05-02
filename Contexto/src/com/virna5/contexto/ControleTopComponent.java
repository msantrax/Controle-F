/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//com.virna5.context//Controle//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "ControleTopComponent",
        iconBase = "com/virna5/context/eraser.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "com.virna5.context.ControleTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ControleAction",
        preferredID = "ControleTopComponent"
)
@Messages({
    "CTL_ControleAction=Controle",
    "CTL_ControleTopComponent=Painel de comando",
    "HINT_ControleTopComponent=Painel de comando das tarefas"
})
public final class ControleTopComponent extends TopComponent implements SignalListener{

    private static final Logger log = Logger.getLogger(ControleTopComponent.class.getName());

    private Controler ctrl;
    private LinkedBlockingQueue<SMTraffic> smqueue;
    
    private JComboBox tools_combobox;
    private GroupsComboBoxModel tools_model;
    
    ArrayList<RunTaskValue> tasks;
    
    public ControleTopComponent() {
        
        log.setLevel(Level.FINE);
        initComponents();
        setName("Janela Controle");
        setToolTipText("Painel de comando do Controle");
       
        tasks = new ArrayList<>();
        
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                
                int w = lp1.getBounds().width;
                int h = lp1.getBounds().height;
                
                pnl_back.setBounds(0, 0, w, h );
                pnl_layer1.setBounds(0, 0, w, h );
            }
        });
        //pnl_runtable.setVisible(false);
        
        
        TableColumn tc;
        TableColumnModel tcm = tbl_running.getColumnModel();
        tc = tcm.getColumn(3);
        tc.setMaxWidth(50);
        tc = tcm.getColumn(4);
        tc.setMinWidth(220);
       
        
        tools_combobox = new JComboBox();
        tools_model = new GroupsComboBoxModel();
        tools_combobox.setModel(tools_model);
        TableCellEditor tools_editor = new DefaultCellEditor(tools_combobox);
        TableColumn tools_column = tbl_running.getColumnModel().getColumn(4);
        tools_column.setCellEditor(tools_editor);
        
        tbl_running.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                DefaultListSelectionModel dlsm = (DefaultListSelectionModel)event.getSource();            
                tools_model.setGroup(dlsm.getLeadSelectionIndex());
            }
        });
        
        ctrl = Controler.getInstance();
        ctrl.setView(this);
        ctrl.addSignalListener(this);
        ctrl.startService();
     
        this.requestActive();
        
        ctrl.addContext("/Bascon/BSW1/Testbench/Ctx/task4.tsk");
        //ctrl.addContext("/Bascon/BSW1/Testbench/Ctx/task5.tsk");
        
    
    }
    
    public void publishTask(Long uid, String desc, String owner, String start, String[] tools, String obs){
     
        tasks.add(new RunTaskValue(uid, desc, start, owner, false, tools, obs));
        tools_model.addGroup(tools);
        tbl_running.updateUI();
    }
    
    
    public class GroupsComboBoxModel extends DefaultComboBoxModel{

        public GroupsComboBoxModel() {
            
        }
        
        int group = 0;
        ArrayList<String[]> groups = new ArrayList<>(); 
        
        public void setGroup(int _group){
            //log.fine("Setting group to :"+ _group);
            this.group = _group; 
        }
        
        public void removeGroup(int _group){
            groups.remove(_group);
        }
        
        public void addGroup(String[] newgroup){
            groups.add(newgroup);
        }
        
        @Override
        public Object getElementAt(int pos){
            //log.fine(String.format("Getting element %d/%d", group, pos));
            return groups.get(group)[pos];
        }
        
        @Override
        public int getSize(){
            return groups.get(group).length;
        }
        
        
    
    };
            
   
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
            return tasks.size();
        }
        
        @Override
        public Object getValueAt(int row, int column) {
            RunTaskValue csvf = tasks.get(row);
            Object o = new Object();
            
            switch (column) {
                case 0:
                    o=csvf.id;
                    break;
                case 1:
                    o=csvf.start;
                    break;
                case 2:
                    o=csvf.owner;
                    break;
                case 3:
                    o= csvf.active;
                    break;
                case 4:
                    o= csvf.tool;
                    break;    
                case 5:
                    o= csvf.obs;
                    break;    
                    
                default:
                    break;
            }
            
            return o;
        }

        public void setValueAt(Object value, int row, int column) {
            RunTaskValue csvf = tasks.get(row);
            
            switch (column) {
                case 0:
                    csvf.id = (String)value;
                    break;
                case 1:
                    csvf.start = (String)value;
                    break;
                case 2:
                    csvf.owner = (String)value;
                    break;
                case 3:
                    csvf.active = (Boolean)value;
                    int active_code = csvf.active ? 1 : 0;
                    ctrl.processSignal(new SMTraffic( ctrl.getContext(), csvf.context, active_code, 
                                    VirnaServices.STATES.CTRL_ACTIVATETASK, 
                                    null));
                    break;
                case 4:
                    csvf.tool = (String)value;
                    if (!value.equals("Selecione uma ferramenta")){
                        VirnaPayload payload = new VirnaPayload().setString((String)value).setLong1(csvf.context);
                        SMTraffic smtf = new SMTraffic( ctrl.getContext(),
                                    VirnaServices.CMDS.LOADSTATE, 0, 
                                    VirnaServices.STATES.CTRL_LOADMONITOR, 
                                    payload);
                        ctrl.processSignal(smtf);
                    }
                    
                    break;    
                case 5:
                    csvf.obs = (String)value;
                    break;    
                    
                default:
                    break;
            }
   
        }

        
        String[] cnames = new String[]{
           "Identificação", "Inicio", "Proprietário", "Ativa", "Ferramentas", "Comentários"  
        };
        
        Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class,
                java.lang.Boolean.class,
                java.lang.String.class, java.lang.String.class,
                
        };

        @Override
        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            if (column == 3 || column == 4) {
                return true;
            }
            else {
                return false;
            }
        }
    };
    
    public class RunTaskValue {
    
        public Long context;
        
        public String id;
        public String start;
        public String owner;
        public Boolean active;
        public String tool;
        public String[]tools;
        public String obs;
        
    
        public RunTaskValue() {
        }

        public RunTaskValue(Long context, String id, String start, String owner, Boolean active, String[] tools, String obs) {
            
            this.context = context;
            this.id = id;
            this.start = start;
            this.owner = owner;
            this.active = active;
            this.tools = tools;
            this.obs = obs;           
            
            this.tool = "Selecione uma Ferramenta";
        }
        
    };
    
    
    // ===========SIGNAL HANDLING ===================================================================
        
    /** Estrutura para armazenamento dos listeners do dispositivo*/ 
    //private transient LinkedHashMap<Long,SignalListener> listeners = new LinkedHashMap<>();
    
    private transient ArrayList<SignalListener> listeners = new ArrayList<>();
    
    @Override
    public Long getContext() {
        return -2L;
    }
    
    /**
     * @return the uid
     */
    public Long getUID() {
        return -2L;
    }
    
    public void processSignal (SMTraffic signal){
        smqueue.add(signal);
    }
    
     /** Método de registro do listener do dispositivo serial */
    public void addSignalListener (SignalListener l){
        listeners.add(l);
    }

    /** Método de remoção do registro do listener do dispositivo serial */
    public void removeSignalListener (SignalListener l){
        listeners.remove(l);
    }

    /** Esse método é chamedo quando algo acontece no dispositivo */
    protected void notifySignalListeners(long context, SMTraffic signal) {

        if (!listeners.isEmpty()){      
            // Rode entre os listeners
//            for (Iterator<SignalListener> i=listeners.iterator(); i.hasNext();){
//                SignalListener l =  i.next();
//                if ((context == 0) || (l.getContext() == context) ){
//                    l.processSignal(signal); //Notifique cada listener
//                }
//                
//            }
        }
    }
    

    
    
    
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lp1 = new javax.swing.JLayeredPane();
        pnl_back = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        pnl_layer1 = new javax.swing.JPanel();
        pnl_runtable = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_running = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        bt_removetask = new javax.swing.JButton();
        bt_addtask = new javax.swing.JButton();

        lp1.setBackground(new java.awt.Color(204, 255, 255));
        lp1.setOpaque(true);

        pnl_back.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnl_back.setOpaque(false);
        pnl_back.setLayout(new java.awt.BorderLayout());

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon("/Bascon/BSW1/Resources/ControlLogo/Controle_logo_80x520_A.png")); // NOI18N
        jLabel1.setToolTipText(org.openide.util.NbBundle.getMessage(ControleTopComponent.class, "ControleTopComponent.jLabel1.toolTipText")); // NOI18N
        jLabel1.setOpaque(true);
        jLabel1.setPreferredSize(new java.awt.Dimension(44, 100));
        pnl_back.add(jLabel1, java.awt.BorderLayout.NORTH);

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/virna5/contexto/backdrop.png"))); // NOI18N
        jLabel2.setOpaque(true);
        pnl_back.add(jLabel2, java.awt.BorderLayout.CENTER);

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setIcon(new javax.swing.ImageIcon("/Bascon/BSW1/Resources/ControlCanvas/bsw_label1.png")); // NOI18N
        jLabel3.setOpaque(true);
        pnl_back.add(jLabel3, java.awt.BorderLayout.SOUTH);

        lp1.setLayer(pnl_back, 1);
        lp1.add(pnl_back);
        pnl_back.setBounds(0, 0, 1200, 540);

        pnl_layer1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnl_layer1.setOpaque(false);

        pnl_runtable.setBackground(new java.awt.Color(255, 255, 255));
        pnl_runtable.setBorder(null);
        pnl_runtable.setOpaque(false);
        pnl_runtable.setPreferredSize(new java.awt.Dimension(835, 80));
        pnl_runtable.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);
        jScrollPane1.setMaximumSize(new java.awt.Dimension(32767, 80));
        jScrollPane1.setOpaque(true);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(456, 80));

        tbl_running.setBorder(null);
        tbl_running.setFont(new java.awt.Font("DejaVu Sans", 0, 14)); // NOI18N
        tbl_running.setModel(model);
        tbl_running.setToolTipText("<html>\nSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium <br>\ndoloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore <br>\nveritatis et quasi architecto beatae vitae dicta sunt explicabo.<br> \nNemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, <br>\nsed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. <br>\nNeque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, <br>\nadipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et <br>\ndolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, <br>\n</html>"); // NOI18N
        tbl_running.setGridColor(new java.awt.Color(255, 255, 255));
        tbl_running.setMaximumSize(new java.awt.Dimension(2147483647, 80));
        tbl_running.setPreferredSize(new java.awt.Dimension(450, 80));
        jScrollPane1.setViewportView(tbl_running);

        pnl_runtable.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 24)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, "Tarefas em Andamento    "); // NOI18N
        jLabel4.setPreferredSize(new java.awt.Dimension(345, 40));
        pnl_runtable.add(jLabel4, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBorder(null);
        jPanel2.setMaximumSize(new java.awt.Dimension(32767, 40));
        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(1054, 40));
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 5));

        bt_removetask.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/virna5/graphimages/remove.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(bt_removetask, "Remover Tarefa"); // NOI18N
        bt_removetask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_removetaskActionPerformed(evt);
            }
        });
        jPanel2.add(bt_removetask);

        bt_addtask.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/virna5/contexto/exec.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(bt_addtask, "Adicionar Tarefa");
        bt_addtask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_addtaskActionPerformed(evt);
            }
        });
        jPanel2.add(bt_addtask);

        pnl_runtable.add(jPanel2, java.awt.BorderLayout.SOUTH);

        javax.swing.GroupLayout pnl_layer1Layout = new javax.swing.GroupLayout(pnl_layer1);
        pnl_layer1.setLayout(pnl_layer1Layout);
        pnl_layer1Layout.setHorizontalGroup(
            pnl_layer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_layer1Layout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .addComponent(pnl_runtable, javax.swing.GroupLayout.PREFERRED_SIZE, 1054, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(100, Short.MAX_VALUE))
        );
        pnl_layer1Layout.setVerticalGroup(
            pnl_layer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_layer1Layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(pnl_runtable, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(212, Short.MAX_VALUE))
        );

        lp1.setLayer(pnl_layer1, 2);
        lp1.add(pnl_layer1);
        pnl_layer1.setBounds(0, 0, 1200, 430);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lp1, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lp1, javax.swing.GroupLayout.DEFAULT_SIZE, 597, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void bt_addtaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_addtaskActionPerformed
        
        SMTraffic smtf = new SMTraffic(ctrl.getContext(),
                                    VirnaServices.CMDS.LOADSTATE, 0, 
                                    VirnaServices.STATES.CTRL_LOADTASK, 
                                    null);
        ctrl.processSignal(smtf);
    }//GEN-LAST:event_bt_addtaskActionPerformed

    private void bt_removetaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_removetaskActionPerformed
        
        SMTraffic alarm_config = new SMTraffic(-1l,
                                    VirnaServices.CMDS.LOADSTATE, 0, 
                                    VirnaServices.STATES.CTRL_HOUSEKEEP, 
                                    null);
        
        SMTraffic smtf = new SMTraffic(ctrl.getContext(),
                                    VirnaServices.CMDS.LOADSTATE,
                                    Controler.getAlarmID(), 
                                    VirnaServices.STATES.CTRL_ADDALARM, 
                                    new VirnaPayload()
                                            .setObject(alarm_config)
                                            .setObjectType("com.virna5.contexto.SMTraffic")
                                            .setLong1(1000L)
                                            .setLong2(1000L) );
        ctrl.processSignal(smtf);
    }//GEN-LAST:event_bt_removetaskActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_addtask;
    private javax.swing.JButton bt_removetask;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLayeredPane lp1;
    private javax.swing.JPanel pnl_back;
    private javax.swing.JPanel pnl_layer1;
    private javax.swing.JPanel pnl_runtable;
    private javax.swing.JTable tbl_running;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
}


//javax.swing.JLabel lb = new javax.swing.JLabel();
//Image image = Toolkit.getDefaultToolkit().createImage("Your animated GIF");
//ImageIcon xIcon = new ImageIcon(image);
//xIcon.setImageObserver(this);
//lb.setIcon(xIcon);