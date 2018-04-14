/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Tools",
        id = "com.virna5.context.BuildDescriptorAction"
)
@ActionRegistration(
        iconBase = "com/virna5/contexto/network_local.png",
        displayName = "Build Descriptor"
)
@ActionReference(path = "Menu/Tools", position = 600)
@Messages("CTL_BuildDescriptorAction=Build Descriptor")
public final class BuildDescriptorAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Controler ctrl = Controler.getInstance();
        ctrl.loadSystem();
    }
}
