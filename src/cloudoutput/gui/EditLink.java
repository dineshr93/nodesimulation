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

import cloudoutput.dao.NetworkMapEntryDAO;
import cloudoutput.gui.customers.SpecificCustomerView;
import cloudoutput.gui.datacenters.SpecificDatacenterView;
import cloudoutput.models.NetworkMapEntry;

/**
 * The EditLink form.
 * Most of its code is generated automatically by the NetBeans IDE.
 * 
 * @author      Thiago T. Sá
 * @since       1.0
 */
public class EditLink extends javax.swing.JDialog {

    /** The source of the link. */
    String source;
    
    /** The destination of the link. */
    String destination;
    
    /** The specific datacenter view. */
    SpecificDatacenterView sdcv;
    
    /** The specific customer view. */
    SpecificCustomerView sugv;
    
    /** Creates a new EditLink form from a specific datacenter view. */
    public EditLink(String source, String dest, SpecificDatacenterView sdcv) {
        this.source = source;
        this.destination = dest;
        this.sdcv=sdcv;
        initComponents();
        NetworkMapEntryDAO neDAO = new NetworkMapEntryDAO();
        bwSpinner.setValue(neDAO.getNetworkMapEntry(source, destination).getBandwidth());
        latSpinner.setValue(neDAO.getNetworkMapEntry(source, destination).getLatency());
    }

    /** Creates a new EditLink form from a specific customer view. */    
    public EditLink(String source, String dest, SpecificCustomerView sugv) {
        this.source = source;
        this.destination=dest;
        this.sugv=sugv;
        initComponents();
        NetworkMapEntryDAO neDAO = new NetworkMapEntryDAO();
        bwSpinner.setValue(neDAO.getNetworkMapEntry(source, destination).getBandwidth());
        latSpinner.setValue(neDAO.getNetworkMapEntry(source, destination).getLatency());
    }

    /** 
     * Updates the values of the link.
     *
     * @since           1.0
     */      
    private void updateValues() {
        Double bandwidth = Double.valueOf(String.valueOf(bwSpinner.getValue()));
        Double latency = Double.valueOf(String.valueOf(latSpinner.getValue()));
        
        NetworkMapEntryDAO neDAO = new NetworkMapEntryDAO();
        NetworkMapEntry entry = neDAO.getNetworkMapEntry(source, destination);
        entry.setBandwidth(bandwidth);
        entry.setLatency(latency);        
        neDAO.updateNetworkMapEntry(entry);

        try{
            sdcv.updateNetworkTable();
        } catch(Exception e) {}

        try{
            sugv.updateNetworkTable();
        } catch(Exception e) {}
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        bwLabel = new javax.swing.JLabel();
        latLabel = new javax.swing.JLabel();
        bwSpinner = new javax.swing.JSpinner();
        latSpinner = new javax.swing.JSpinner();
        okButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Edit Link");
        setModal(true);
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Settings"));

        bwLabel.setText("Bandwidth:");

        latLabel.setText("Latency:");

        bwSpinner.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), Double.valueOf(0.0d), null, Double.valueOf(0.5d)));
        bwSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                bwSpinnerStateChanged(evt);
            }
        });

        latSpinner.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), Double.valueOf(0.0d), null, Double.valueOf(0.5d)));
        latSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                latSpinnerStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bwLabel)
                    .addComponent(latLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(latSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bwSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(155, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {bwSpinner, latSpinner});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bwLabel)
                    .addComponent(bwSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(latLabel)
                    .addComponent(latSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(okButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /** 
     * Updates the values of the link whenever the bandwidth spinner is used.
     *
     * @param   evt     a change event.
     * @since           1.0
     */   
    private void bwSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_bwSpinnerStateChanged
        updateValues();
    }//GEN-LAST:event_bwSpinnerStateChanged

    /** 
     * Updates the values of the link whenever the latency spinner is used.
     *
     * @param   evt     a change event.
     * @since           1.0
     */       
    private void latSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_latSpinnerStateChanged
        updateValues();
    }//GEN-LAST:event_latSpinnerStateChanged

    /** 
     * Disposes the form when the OK button is clicked.
     *
     * @param   evt     an action event.
     * @since           1.0
     */      
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        dispose();
    }//GEN-LAST:event_okButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bwLabel;
    private javax.swing.JSpinner bwSpinner;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel latLabel;
    private javax.swing.JSpinner latSpinner;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables

}
