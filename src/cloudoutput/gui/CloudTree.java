

package cloudoutput.gui;

import cloudoutput.dao.CustomerRegistryDAO;
import cloudoutput.dao.DatacenterRegistryDAO;
import cloudoutput.models.CustomerRegistry;
import cloudoutput.models.DatacenterRegistry;
import java.awt.Component;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class CloudTree extends JTree{
   
    private DefaultMutableTreeNode cloudTreeNode;

   
    DefaultMutableTreeNode providerTreeNode;
    
 
    DefaultMutableTreeNode customersTreeNode;

    {
        if(this.treeModel.getRoot() instanceof DefaultMutableTreeNode)
            cloudTreeNode = (DefaultMutableTreeNode) this.treeModel.getRoot();

        providerTreeNode = new DefaultMutableTreeNode("Provider");
        customersTreeNode = new DefaultMutableTreeNode("Customers");
        getCloudTreeNode().add(providerTreeNode);
        getCloudTreeNode().add(customersTreeNode);

        this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.setCellRenderer(new CustomRenderer(
                new javax.swing.ImageIcon(getClass().getResource("/cloudoutput/gui/resources/cloud.png")),
                new javax.swing.ImageIcon(getClass().getResource("/cloudoutput/gui/resources/provider.png")),
                new javax.swing.ImageIcon(getClass().getResource("/cloudoutput/gui/resources/costumers.png")),
                new javax.swing.ImageIcon(getClass().getResource("/cloudoutput/gui/resources/dot.png"))));
    }

    
    public CloudTree() {
        super(new DefaultMutableTreeNode("BackupnodeSimulation"));
        
        DatacenterRegistryDAO drDAO = new DatacenterRegistryDAO();
        String[] datacenterNames = drDAO.getAllDatacentersNames();
        for(String datacenterName : datacenterNames) {
            providerTreeNode.add(new DefaultMutableTreeNode(datacenterName,false));
        }
        
        CustomerRegistryDAO crDAO = new CustomerRegistryDAO();
        String[] customerNames = crDAO.getCustomersNames();
        for(String customerName : customerNames) {
            customersTreeNode.add(new DefaultMutableTreeNode(customerName,false));
        }
            
        this.expandPath(new TreePath(getCloudTreeNode()));
    }

    /** 
     * Updates the state of all CloudTree nodes.
     *
     * @since           1.0
     */     
    public void updateNodes() {
        providerTreeNode.removeAllChildren();
        customersTreeNode.removeAllChildren();
        DatacenterRegistryDAO drDAO = new DatacenterRegistryDAO();
        
        for(DatacenterRegistry dcr : drDAO.getListOfDatacenters()) {
            providerTreeNode.insert(new DefaultMutableTreeNode(dcr.getName(),false),0);
        }

        CustomerRegistryDAO crDAO = new CustomerRegistryDAO();
        for(CustomerRegistry c : crDAO.getListOfCustomers()) {
            customersTreeNode.insert(new DefaultMutableTreeNode(c.getName(),false),0);
        }
        this.expandPath(new TreePath(getCloudTreeNode()));
    }

       
    public boolean insertNode(String name, int type) {
        DefaultMutableTreeNode node;
        if(type==0) {
            Enumeration e = providerTreeNode.children();
            try{
                while(e.hasMoreElements()) {
                node = (DefaultMutableTreeNode) e.nextElement();
                if(node.toString().equalsIgnoreCase(name)) return false;
                }
            }
            catch(NoSuchElementException ex) {
                ex.printStackTrace();
            }
            providerTreeNode.insert(new DefaultMutableTreeNode(name,false),0);
            this.updateUI();
            return true;
        }
        else if(type==1) {
            Enumeration e = customersTreeNode.children();
            try{
                while(e.hasMoreElements()) {
                node = (DefaultMutableTreeNode) e.nextElement();
                if(node.toString().equalsIgnoreCase(name)) return false;
                }
            }
            catch(NoSuchElementException ex) {
                ex.printStackTrace();
            }
            customersTreeNode.insert(new DefaultMutableTreeNode(name,false),0);
            this.updateUI();
            return true;
        }
        return false;
    }

     
    public boolean removeNode(String name, int type) {
        DefaultMutableTreeNode node;
        if(type==0) {
            Enumeration e = providerTreeNode.children();
            try{
                while(e.hasMoreElements()) {
                    node = (DefaultMutableTreeNode) e.nextElement();
                    if(node.toString().equalsIgnoreCase(name)){
                        providerTreeNode.remove(node);
                        this.updateUI();
                        return true;
                    }
                }
            }
            catch(NoSuchElementException ex) {
                ex.printStackTrace();
            }
            return false;
        }
        else if(type==1) {
            Enumeration e = customersTreeNode.children();
            try{
                while(e.hasMoreElements()) {
                    node = (DefaultMutableTreeNode) e.nextElement();
                    if(node.toString().equalsIgnoreCase(name)) {
                        customersTreeNode.remove(node);
                        this.updateUI();
                        return true;
                    }
                }
            }
            catch(NoSuchElementException ex) {
                ex.printStackTrace();
            }
            return false;
        }
        return false;
    }

    /** 
     * Gets the current CloudTree node.
     * 
     * @return      a CLoudTree node.
     * @since       1.0
     */  
    public DefaultMutableTreeNode getCloudTreeNode() {
        return cloudTreeNode;
    }

    /**
     * An inner class that implements a custom TreeCellRenderer and inherits
     * from {@link DefaultTreeCellRenderer}.
     * 
     * @author      Thiago T. Sá
     * @since       1.0
     */    
    class CustomRenderer extends DefaultTreeCellRenderer {
        Icon icon0, icon1, icon2, icon3, icon4;

        /** Creates a new CustomRenderer. */
        public CustomRenderer(Icon icon0, Icon icon1, Icon icon2, Icon icon3) {
            this.icon0 = icon0;
            this.icon1 = icon1;
            this.icon2 = icon2;
            this.icon3 = icon3;
        }
      
        public Component getTreeCellRendererComponent(
                            JTree tree,
                            Object value,
                            boolean sel,
                            boolean expanded,
                            boolean leaf,
                            int row,
                            boolean hasFocus) {

            super.getTreeCellRendererComponent(
                            tree, value, sel,
                            expanded, leaf, row,
                            hasFocus);
            if (leaf) {
                setIcon(icon3);
            }
            else if(isCloud(value)){
                setIcon(icon0);
            }
            else if(isProvider(value)) {
                setIcon(icon1);
            }
            else {
                setIcon(icon2);
            }

            return this;
        }

        /** 
         * Checks if the node is the root node.
         *
         * @param   value   the value to be checked.
         * @since           1.0
         */             
        protected boolean isCloud(Object value) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
            if (node.toString().equalsIgnoreCase("cloudoutput")) return true;
            return false;
        }

        /** 
         * Checks if the node is the providers root node.
         *
         * @since           1.0
         */          
        protected boolean isProvider(Object value) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
            if (node.toString().equalsIgnoreCase("Provider")) return true;
            return false;
        }
    }
}
