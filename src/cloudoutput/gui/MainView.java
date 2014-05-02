
package cloudoutput.gui;

import cloudoutput.dao.CustomerRegistryDAO;

import cloudoutput.dao.DatacenterRegistryDAO;
import cloudoutput.database.Database;
import cloudoutput.database.HibernateUtil;
import cloudoutput.gui.customers.OverallCustomerView;
import cloudoutput.gui.customers.SpecificCustomerView;
import cloudoutput.gui.datacenters.OverallDatacenterView;
import cloudoutput.gui.datacenters.SpecificDatacenterView;
import cloudoutput.models.CustomerRegistry;
import cloudoutput.models.DatacenterRegistry;
import cloudoutput.simulation.Simulation;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;


public class MainView extends javax.swing.JFrame {


    private OverallDatacenterView overallDatacenterView;
 
    private SpecificDatacenterView specificDatacenterView;
    
  
    private OverallCustomerView overallCostumerView;
    
    
    private SpecificCustomerView specificCostumerView;
    
    
    private CloudView cloudView;
    
 
    private LogoPanel logoPanel;
    
   
    private static SimulationView simulationView;
    
    
    private static CardLayout cl;
    
    private static boolean datacenterModified;
    
    
    private static boolean customerModified;
    
   
    private static Thread simulationThread;    
    
  
    public static CloudTree getCloudTree() {
        return (CloudTree)jTree;
    }

   
    public static boolean isDatacenterModified() {
        return datacenterModified;
    }

    
    public static void setDatacenterModified(boolean datacenterModifiedValue) {
        datacenterModified = datacenterModifiedValue;
    }

   
    public static boolean isCustomerModified() {
        return customerModified;
    }

    
    public static void setUserGroupModified(boolean customerModifiedValue) {
        customerModified = customerModifiedValue;
    }

    
    public static SimulationView getSimulationView() {
        return simulationView;
    }

   
    public static Thread getSimulationThread() {
        return simulationThread;
    }

   
    public MainView() throws IOException {
        URL url  = this.getClass().getResource("/cloudoutput/gui/resources/blue_logo.gif");
        getFrames()[0].setIconImage(new ImageIcon(url).getImage());

        DatacenterRegistryDAO drDAO = new DatacenterRegistryDAO();
        CustomerRegistryDAO crDAO = new CustomerRegistryDAO();
       
        File dbDirectory = new File("db");
        if(!dbDirectory.exists()) {
            dbDirectory.mkdir();
        }
        
        File[] dbArray = dbDirectory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".cre");
            }
        });
        
        if (dbArray.length == 0) {
            HibernateUtil.setActiveDatabase("New Environment.cre");
            Database.createDatabase();
            drDAO.insertNewDatacenterRegistry(new DatacenterRegistry("Datacenter1"));
            crDAO.insertNewCustomerRegistry(new CustomerRegistry("Customer1"));            
        }
        else {
            for(File dbFile : dbArray) {
                if(dbFile.isFile() && dbFile.getName().endsWith(".cre")) {
                    HibernateUtil.setActiveDatabase(dbFile.getName());
                    break;
                }
            }
        }
        
        overallDatacenterView = new OverallDatacenterView();
        overallCostumerView = new OverallCustomerView();
        specificDatacenterView = new SpecificDatacenterView(drDAO.getDatacenterRegistry(drDAO.getAllDatacentersNames()[0]));
        specificCostumerView = new SpecificCustomerView(crDAO.getCustomerRegistry(crDAO.getCustomersNames()[0]));
        cloudView = new CloudView();
        logoPanel = new LogoPanel();
        simulationView = new SimulationView();
        setDatacenterModified(false);
        setUserGroupModified(false);
        initComponents();
        cl = (CardLayout)(cardPanel.getLayout());
    }

   
 // @SuppressWarnings("unchecked")
    
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree = new CloudTree();
        startButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        environmentsBox = new javax.swing.JComboBox();
        cardPanel = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        environmentsMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        quitMenuItem = new javax.swing.JMenuItem();
        settingsMenu = new javax.swing.JMenu();
        simulationSettingsMenuItem = new javax.swing.JMenuItem();
        randomnessSettingsMenuItem = new javax.swing.JMenuItem();
        mailNotificationMenuItem = new javax.swing.JMenuItem();
        aboutMenu = new javax.swing.JMenu();
        aboutViewMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("BACKUP - Node simulation");
        setForeground(java.awt.Color.white);
        setResizable(false);

        jTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTreeValueChanged(evt);
            }
        });
        jTree.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentRemoved(java.awt.event.ContainerEvent evt) {
                jTreeComponentRemoved(evt);
            }
        });
        jScrollPane1.setViewportView(jTree);

        startButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudoutput/gui/resources/play.png"))); // NOI18N
        startButton.setText("Run Simulation");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("Simulation environments:");
        jLabel2.setFont(new Font(jLabel2.getFont().getName(), Font.BOLD, jLabel2.getFont().getSize()));

        environmentsBox.setModel(new javax.swing.DefaultComboBoxModel(getEnvironmentsNames()));
        environmentsBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                environmentsBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 176, Short.MAX_VALUE)
                    .addComponent(startButton, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                    .addComponent(environmentsBox, 0, 176, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(environmentsBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(startButton)
                .addContainerGap(137, Short.MAX_VALUE))
        );

        cardPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        cardPanel.setLayout(new java.awt.CardLayout());

        fileMenu.setText("File");

        environmentsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        environmentsMenuItem.setMnemonic('C');
        environmentsMenuItem.setText("Manage environments");
        environmentsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                environmentsMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(environmentsMenuItem);
        fileMenu.add(jSeparator1);

        quitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        quitMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudoutput/gui/resources/close_small.png"))); // NOI18N
        quitMenuItem.setText("Quit");
        quitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(quitMenuItem);

        jMenuBar1.add(fileMenu);

        settingsMenu.setText("Settings");

        simulationSettingsMenuItem.setText("Simulation");
        simulationSettingsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simulationSettingsMenuItemActionPerformed(evt);
            }
        });
        settingsMenu.add(simulationSettingsMenuItem);

        randomnessSettingsMenuItem.setText("Randomness source");
        randomnessSettingsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                randomnessSettingsMenuItemActionPerformed(evt);
            }
        });
        settingsMenu.add(randomnessSettingsMenuItem);

        mailNotificationMenuItem.setText("Mail notification");
        mailNotificationMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mailNotificationMenuItemActionPerformed(evt);
            }
        });
        settingsMenu.add(mailNotificationMenuItem);

        jMenuBar1.add(settingsMenu);

      //  aboutMenu.setText("About");

        aboutViewMenuItem.setText("About cloudoutput");
        aboutViewMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutViewMenuItemActionPerformed(evt);
            }
        });
        aboutMenu.add(aboutViewMenuItem);

        jMenuBar1.add(aboutMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cardPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        addTabbedPanel("logoPanel",logoPanel);
        addTabbedPanel("cloudView",cloudView);
        addTabbedPanel("overallDatacenterView",overallDatacenterView);
        addTabbedPanel("specificDatacenterView",specificDatacenterView);
        addTabbedPanel("overallCostumerView", overallCostumerView);
        addTabbedPanel("specificCostumerView", specificCostumerView);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /** 
     * Verifies if the view needs to be changed whenever a CloudTree node is
     * selected.
     *
     * @param   evt     a tree selection event.
     * @since           1.0
     */       
    private void jTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTreeValueChanged
        try{
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
            if(node == null) return;

            if(node.isLeaf()) {
                if(node.getParent().toString().equalsIgnoreCase("Provider")) {
                    DatacenterRegistryDAO drDAO = new DatacenterRegistryDAO();
                    specificDatacenterView.changeRegistry(drDAO.getDatacenterRegistry(node.toString()));
                    cl.show(cardPanel, "specificDatacenterView");
                }
                else {
                    CustomerRegistryDAO crDAO = new CustomerRegistryDAO();
                    specificCostumerView.changeRegistry(crDAO.getCustomerRegistry(node.toString()));
                    cl.show(cardPanel, "specificCostumerView");
                }
            }
            else {
                if(node.toString().equalsIgnoreCase("cloudoutput")) cl.show(cardPanel, "logoPanel");
                else if(node.toString().equalsIgnoreCase("Provider")) updateOverallDatacenterView();
                else if(node.toString().equalsIgnoreCase("Customers")) updateOverallCustomerView();
            }
        }
        catch(Exception ex) {
            Logger.getLogger(Database.class.getName()).log(Level.WARNING, null, ex);
            cl.show(cardPanel, "logoPanel");
        }
    }//GEN-LAST:event_jTreeValueChanged

    /** 
     * Updated views when a CloudTree node is removed.
     *
     * @param   evt     a container event.
     * @since           1.0
     */           
    private void jTreeComponentRemoved(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_jTreeComponentRemoved
        if(isDatacenterModified()){
            updateOverallDatacenterView();
            return;
        }
        updateOverallCustomerView();
    }//GEN-LAST:event_jTreeComponentRemoved

    /** 
     * Starts simulations when the start button is clicked.
     *
     * @param   evt     an action event.
     * @since           1.0
     */         
    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        simulationThread = new Thread(new Simulation());
        simulationThread.start();
        getSimulationView().setLocationRelativeTo(MainView.getFrames()[1]);
        getSimulationView().setVisible(true);
    }//GEN-LAST:event_startButtonActionPerformed

    /** 
     * Closes the application when the Quit menu item is clicked.
     *
     * @param   evt     an action event.
     * @since           1.0
     */      
    private void quitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_quitMenuItemActionPerformed

    /** 
     * Launches the AboutView when the About View menu item is clicked.
     *
     * @param   evt     an action event.
     * @since           1.0
     */        
    private void aboutViewMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutViewMenuItemActionPerformed
        AboutView aboutView = new AboutView();
        aboutView.setLocationRelativeTo(this);
        aboutView.setVisible(true);        
    }//GEN-LAST:event_aboutViewMenuItemActionPerformed

     
    private void randomnessSettingsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_randomnessSettingsMenuItemActionPerformed
        RandomnessSettings randomnessSettings = new RandomnessSettings();
        randomnessSettings.setLocationRelativeTo(this);
        randomnessSettings.setVisible(true);
    }//GEN-LAST:event_randomnessSettingsMenuItemActionPerformed
      
    private void simulationSettingsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simulationSettingsMenuItemActionPerformed
        SimulationSettings simulationSettings = new SimulationSettings();
        simulationSettings.setLocationRelativeTo(this);
        simulationSettings.setVisible(true);
    }//GEN-LAST:event_simulationSettingsMenuItemActionPerformed

    private void mailNotificationMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mailNotificationMenuItemActionPerformed
        MailNotificationSettings mailNotificationSettings = new MailNotificationSettings();
        mailNotificationSettings.setLocationRelativeTo(this);
        mailNotificationSettings.setVisible(true);
    }//GEN-LAST:event_mailNotificationMenuItemActionPerformed

         
    private void environmentsBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_environmentsBoxItemStateChanged
        HibernateUtil.setActiveDatabase(environmentsBox.getSelectedItem().toString() + ".cre");
        getCloudTree().updateNodes();
        jTree.updateUI();
        cl.show(cardPanel, "logoPanel");
    }//GEN-LAST:event_environmentsBoxItemStateChanged

       
    private void environmentsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_environmentsMenuItemActionPerformed
        ManageEnvironments manageEnvironments = new ManageEnvironments();
        manageEnvironments.setLocationRelativeTo(this);
        manageEnvironments.setVisible(true);
    }//GEN-LAST:event_environmentsMenuItemActionPerformed

    /** 
     * Adds a panel to the card layout.
     *
     * @param   name    the name of the new panel.
     * @param   panel   the new panel.
     * @since           1.0
     */       
    public boolean addTabbedPanel(String name, JPanel panel) {
        Component[] components = cardPanel.getComponents();

        for (Component c : components) {
            if(c.getName()!=null && c.getName().equals(name)) return false;
        }

        cardPanel.add(name, panel);
        return true;
    }

    /** 
     * Updates the displayed overall datacenter view.
     *
     * @since   1.0
     */       
    private void updateOverallDatacenterView() {
        cardPanel.remove(overallDatacenterView);
        overallDatacenterView = new OverallDatacenterView();
        addTabbedPanel("overallDatacenterView",overallDatacenterView);
        setDatacenterModified(false);
        cl.show(cardPanel, "overallDatacenterView");
    }
    
    /** 
     * Updates the displayed overall customer view.
     *
     * @since   1.0
     */ 
    private void updateOverallCustomerView() {
        cardPanel.remove(overallCostumerView);
        overallCostumerView = new OverallCustomerView();
        addTabbedPanel("overallUserGroupView", overallCostumerView);
        setUserGroupModified(false);
        cl.show(cardPanel, "overallUserGroupView");
    }

    /** 
     * Enables/disables the start simulation button.
     *
     * @since   1.0
     */     
    public static void setStartButtonEnabled(boolean b) {
        startButton.setEnabled(b);
    }
    
    /** 
     * Updates the environments combo box.
     *
     * @since   1.0
     */        
    public static void updateEnvironmentsBox() {
        environmentsBox.setModel(new javax.swing.DefaultComboBoxModel(getEnvironmentsNames()));
        environmentsBox.updateUI();
        HibernateUtil.setActiveDatabase(environmentsBox.getSelectedItem().toString() + ".cre");
        cl.show(cardPanel, "overallUserGroupView");
    }
    
    /** 
     * Gets all environments names.
     * It reads all .cre files in the db directory.
     *
     * @since   1.0
     */     
    public static String[] getEnvironmentsNames() {
        File dbDir = new File("db");
        File[] dbFiles = dbDir.listFiles();
        
        int numberOfFiles = 0;
        for(File dbFile : dbFiles) {
            if (dbFile.isFile() && dbFile.getName().endsWith(".cre")) numberOfFiles++;
        }
        
        String[] dbNames = new String[numberOfFiles];
        int index = 0;
        for(File dbFile : dbFiles) {
            if (dbFile.isFile() && dbFile.getName().endsWith(".cre")) {
                String name = dbFile.getName().replace(".cre", "");
                dbNames[index] = name;
                index++;
            }
        }
        
        return dbNames;
    }

    /**
     * The entry point of the application.
     * 
     * @param args the command line arguments.
     */
    public static void main(String args[]) throws IOException {

        try {
            boolean wasFound=false;
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.put("nimbusBase", new Color(152,153,155));
                    UIManager.put("nimbusBlueGrey", new Color(178,178,178));
                    UIManager.put("control", new Color(232,232,232));
                    UIManager.setLookAndFeel(info.getClassName());
                    wasFound=true;
                    break;
                }
            }
            if(!wasFound){
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                Dialog.showWarning(new JFrame(), "The default Java \"Look and Feel\" of this application is not available\n" +
                        "on your system. This can be solved by updating your current Java enviroment.\n"+
                        "Due to this problem, some UI elements might appear different than intended.");
            }
        } 
        catch (Exception ex) {
            Logger.getLogger(Database.class.getName()).log(Level.INFO, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new MainView().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu aboutMenu;
    private javax.swing.JMenuItem aboutViewMenuItem;
    private static javax.swing.JPanel cardPanel;
    private static javax.swing.JComboBox environmentsBox;
    private javax.swing.JMenuItem environmentsMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private static javax.swing.JTree jTree;
    private javax.swing.JMenuItem mailNotificationMenuItem;
    private javax.swing.JMenuItem quitMenuItem;
    private javax.swing.JMenuItem randomnessSettingsMenuItem;
    private javax.swing.JMenu settingsMenu;
    private javax.swing.JMenuItem simulationSettingsMenuItem;
    private static javax.swing.JButton startButton;
    // End of variables declaration//GEN-END:variables

}
