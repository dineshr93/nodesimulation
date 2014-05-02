
package cloudoutput.enums;

import cloudoutput.extensions.ExtensionsLoader;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.UtilizationModelStochastic;

public enum UtilizationModel implements Serializable{

    /** The full utilization model.
     *  Its {@link #getModel()} method returns an instance of
     *  CloudSim's UtilizationModelFull class.
     */        
    FULL {
        @Override
        public org.cloudbus.cloudsim.UtilizationModel getModel(String modelAlias) {
            return new UtilizationModelFull();
        }
    },

    /** The stochastic utilization model.
     *  Its {@link #getModel()} method returns an instance of
     *  CloudSim's UtilizationModelStochastic class.
     */     
    STOCHASTIC {
        @Override
        public org.cloudbus.cloudsim.UtilizationModel getModel(String modelAlias) {
            return new UtilizationModelStochastic();
        }
    },
    
    /** The extension type. 
     *  It is used for all user-implemented new types.
     */      
    EXTENSION {
        @Override
        public org.cloudbus.cloudsim.UtilizationModel getModel(String modelAlias) {
            try {
                Class<?>[] types = new Class<?>[]{int.class};
                Object[] arguments = new Object[]{};
                return (org.cloudbus.cloudsim.UtilizationModel) ExtensionsLoader.getExtension("UtilizationModel", modelAlias, types, arguments);
            } catch (Exception e) {
                return null;
            }
        }
    };
    
    /** 
     * An abstract method to be implemented by every {@link UtilizationModel}.
     *
     * @return                  a CloudSim's UtilizationModel subtype.
     * @since                   1.0
     */      
    public abstract org.cloudbus.cloudsim.UtilizationModel getModel(String modelAlias);

    /** 
     * Gets an instance of utilization model based on its alias.
     *
     * @param   modelAlias  the alias of the utilization model.
     * @return              an instance of the type with the given alias.
     * @since               1.0
     */       
    public static UtilizationModel getInstance(String modelAlias) {
        if(modelAlias.equals("Full")) return UtilizationModel.FULL;
        else if(modelAlias.equals("Stochastic")) return UtilizationModel.STOCHASTIC;
        else return UtilizationModel.EXTENSION;
    }
    
    /** 
     * Gets all active utilization model aliases.
     *
     * @return  an array of strings containing all active utilization model
     *          aliases.
     * @since   1.0
     */      
    public static String[] getUtilizationModelNames() {
        String[] nativeModels = new String[]{"Full", "Stochastic"};
        List<String> extensionModels = ExtensionsLoader.getExtensionsAliasesByType("UtilizationModel");
        extensionModels.addAll(Arrays.asList(nativeModels));

        return extensionModels.toArray(new String[0]);
    }

}
