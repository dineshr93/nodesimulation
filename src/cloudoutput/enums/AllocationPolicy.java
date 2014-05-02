
package cloudoutput.enums;

import cloudoutput.extensions.ExtensionsLoader;
import cloudoutput.extensions.vmallocationpolicies.VmAllocationPolicyFTCloud;
import cloudoutput.extensions.vmallocationpolicies.VmAllocationPolicyROCloud;


import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.power.PowerHost;

/**
 * Defines native types of allocation policies and implements an extension type

 * to support user-implemented new types.
 * 

 */
public enum AllocationPolicy implements Serializable {

   
    
    RO_CLOUD {
        @Override
        
        public VmAllocationPolicy getPolicy(List<PowerHost> hostList, double HighRank, 
                                            double lowerUtilizationThreshold, double schedulingInterval,
                                            String policyAlias) {
            return new VmAllocationPolicyROCloud(hostList, HighRank);
        }
    },
    FT_CLOUD { 
    	
        @Override
             
        public VmAllocationPolicy getPolicy(List<PowerHost> hostList, double HighRank, 
                                            double upperUtilizationThreshold, double schedulingInterval,
                                            String policyAlias) {
            return new VmAllocationPolicyFTCloud(hostList, HighRank);
        }
    },
    
    /** The extension type. 
     *  It is used for all user-implemented new types.
     */
    EXTENSION {
        @Override
        public VmAllocationPolicy getPolicy(List<PowerHost> hostList, double upperUtilizationThreshold, 
                                            double lowerUtilizationThreshold, double schedulingInterval,
                                            String policyAlias) {
            try{
                Class<?>[] types = new Class<?>[]{ List.class, double.class, double.class, double.class };
                Object[] arguments = new Object[]{ hostList, upperUtilizationThreshold, lowerUtilizationThreshold, schedulingInterval};
                return (VmAllocationPolicy) ExtensionsLoader.getExtension("VmAllocationPolicy", policyAlias, types, arguments);
            }
            catch(Exception e) {
                return null;
            }
        }
    };

    /** 
     * An abstract getter method to be implemented by every {@link AllocationPolicy}.
     *
     * @param   hostList                    the list of hosts that will be managed
     *                                      according to this policy.
     * @param   upperUtilizationThreshold   the upper utilization threshold. Its value
     *                                      must be greater than zero and less than
     *                                      or equal to 1.
     * @param   policyAlias                 the alias to be used by extension types.
     * @return                              a CloudSim's VmAllocationPolicy subtype.
     * @since                               1.0
     */    
    public abstract VmAllocationPolicy getPolicy(List<PowerHost> hostList, double upperUtilizationThreshold, 
                                                 double lowerUtilizationThreshold, double schedulingInterval,
                                                 String policyAlias);
    
    /** 
     * Gets an instance of an allocation policy based on its alias.
     *
     * @param   policyAlias     the alias of the allocation policy.
     * @return                  an instance of the type with the given alias.
     * @since                   1.0
     */     
    public static AllocationPolicy getInstance(String policyAlias) {
        if(policyAlias.equals("ROCLOUD")) return AllocationPolicy.RO_CLOUD;
        else  if(policyAlias.equals("FTCloud")) return AllocationPolicy.FT_CLOUD;
        else return AllocationPolicy.EXTENSION;
    }

    /** 
     * Gets all active allocation policies aliases.
     *
     * @return  an array of strings containing all active allocation
     *          policies aliases.
     * @since   1.0
     */       
    public static String[] getAllocationPoliciesNames() {
        String[] nativePolicies = new String[] {"FTCloud"};
        String[] nativePolicies1 = new String[] {"ROCloud"};
        List<String> extensionPolicies = ExtensionsLoader.getExtensionsAliasesByType("VmAllocationPolicy");
        extensionPolicies.addAll(Arrays.asList(nativePolicies));
        extensionPolicies.addAll(Arrays.asList(nativePolicies1));
        
        return extensionPolicies.toArray(new String[0]);
    }

}
