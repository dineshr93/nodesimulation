
package cloudoutput.business;

import cloudoutput.dao.CustomerRegistryDAO;
import cloudoutput.models.CustomerRegistry;
import java.util.List;


public class CustomerRegistryBusiness {
    
    private static CustomerRegistryDAO crDAO = new CustomerRegistryDAO();
    
    public static List<CustomerRegistry> getListOfCustomers() {
        return crDAO.getListOfCustomers();
    }
    
}
