/* 
 * Copyright (c) 2010-2012 Thiago T. Sá
 * 
 * This file is part of cloudoutput.
 *
 * cloudoutput is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * cloudoutput is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * For more information about your rights as a user of cloudoutput,
 * refer to the LICENSE file or see <http://www.gnu.org/licenses/>.
 */

package cloudoutput.gui;

import cloudoutput.dao.CustomerRegistryDAO;
import cloudoutput.dao.DatacenterRegistryDAO;
import cloudoutput.database.Database;
import cloudoutput.database.HibernateUtil;
import cloudoutput.models.CustomerRegistry;
import cloudoutput.models.DatacenterRegistry;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The NewEnvironment form.
 * Most of its code is generated automatically by the NetBeans IDE.
 * 
 * @author      Thiago T. Sá
 * @since       1.0
 */
public class NewEnvironment extends javax.swing.JDialog {

    /** Creates a new NewEnvironment form. */
    public NewEnvironment() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nameLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New Environment");
        setModal(true);
        setResizable(false);

        nameLabel.setText("New environment's name:");

        nameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nameTextFieldKeyPressed(evt);
            }
        });

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudoutput/gui/resources/cancel.png"))); // NOI18N
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        okButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudoutput/gui/resources/ok.png"))); // NOI18N
        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(nameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(okButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cancelButton)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /** 
     * Closes the form when the Cancel button is clicked.
     *
     * @param   evt     an action event.
     * @since           1.0
     */     
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    /** 
     * Creates the new environment when the OK button is clicked.
     *
     * @param   evt     an action event.
     * @since           1.0
     */     
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        String text = nameTextField.getText();
        if(text.contains(".") || text.contains("/")) {
            Dialog.showErrorMessage(this, "Error creating new environment.\nPlease try another name.");
            return;
        }
        
        File newEnvironment = new File("db/" + nameTextField.getText() + ".cre");
        if(newEnvironment.exists()) {
            Dialog.showErrorMessage(this, "Error creating new environment.\nPlease try another name.");
        }
        else {
            try {
                newEnvironment.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(NewEnvironment.class.getName()).log(Level.SEVERE, null, ex);
            }
            HibernateUtil.setActiveDatabase(newEnvironment.getName());
            Database.createDatabase();
            DatacenterRegistryDAO drDAO = new DatacenterRegistryDAO();
            drDAO.insertNewDatacenterRegistry(new DatacenterRegistry("Datacenter1"));
            CustomerRegistryDAO crDAO = new CustomerRegistryDAO();
            crDAO.insertNewCustomerRegistry(new CustomerRegistry("Customer1"));
            
            ManageEnvironments.updateList();
            MainView.updateEnvironmentsBox();
            dispose();
        }
    }//GEN-LAST:event_okButtonActionPerformed

    /** 
     * Triggers a click on the OK button when the ENTER key is pressed.
     *
     * @param   evt     an action event.
     * @since           1.0
     */      
    private void nameTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameTextFieldKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER) okButtonActionPerformed(null);
    }//GEN-LAST:event_nameTextFieldKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
}
