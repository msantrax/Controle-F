/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.LifecycleManager;
import org.openide.util.Lookup;

public class LoginHandler implements ActionListener{

    private static final LoginHandler instance = new LoginHandler();
    private LoginDLG panel = new LoginDLG();
    private DialogDescriptor d;
    
    
    public LoginHandler(){
        
    }
    
    public static LoginHandler getDefault(){ return instance;}
    
    public void showLoginDlg() {
        
        d = new DialogDescriptor(panel, "Login", true, this);
        d.setClosingOptions(new Object[]{});
        d.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if(e.getPropertyName().equals(DialogDescriptor.PROP_VALUE) && 
                            e.getNewValue()==DialogDescriptor.CLOSED_OPTION) {
                    LifecycleManager.getDefault().exit();
                }
            }
        });
        //d.setValid(false);
        DialogDisplayer.getDefault().notifyLater(d);
       
         
        Controler ctrl = Controler.getInstance();
        ctrl.startService();
        d.setValid(true);
        
        
        
//        if (sys_start.startSystem(panel)){
//            d.setValid(true); 
//        }
//        else {
//            panel.showInfo();
//        }
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
        
        if(event.getSource() == DialogDescriptor.CANCEL_OPTION) {
            LifecycleManager.getDefault().exit();
        }
        else{
            if(!checkPermission(panel.getPassword())) {
                panel.setInfo("Usuário ou Senha errada");
            }
            else{
                d.setClosingOptions(null);
                //ClassmapNode.setCurrentuser(1);
                //ClassmapNode.setCurrentgroup(3);
            }
        }
    }

    /**
     * Verifica permissões do usuário
     * @param user
     *  nome do usuario
     * @param passw
     *  senha (por enquanto nẽ é encriptada
     * @return 
     */
    private boolean checkPermission (String passw){
        return true;
    }
    
    
    
}
