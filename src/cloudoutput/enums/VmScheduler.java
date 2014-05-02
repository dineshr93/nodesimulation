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

package cloudoutput.enums;

import cloudoutput.extensions.ExtensionsLoader;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;


public enum VmScheduler implements Serializable{

      
    SPACE_SHARED {
        @Override
        public org.cloudbus.cloudsim.VmScheduler getScheduler(List<Pe> peList, String schedulerAlias) {
            return new VmSchedulerSpaceShared(peList);
        }
    },

       
    TIME_SHARED {
        @Override
        public org.cloudbus.cloudsim.VmScheduler getScheduler(List<Pe> peList, String schedulerAlias) {
            return new VmSchedulerTimeShared(peList);
        }
    },
      
    EXTENSION {
        @Override
        public org.cloudbus.cloudsim.VmScheduler getScheduler(List<Pe> peList, String schedulerAlias) {
            try {
                Class<?>[] types = new Class<?>[]{List.class};
                Object[] arguments = new Object[]{peList};
                return (org.cloudbus.cloudsim.VmScheduler) ExtensionsLoader.getExtension("VmScheduler", schedulerAlias, types, arguments);
            } catch (Exception e) {
                return null;
            }
        }
    };
    
   
    public abstract org.cloudbus.cloudsim.VmScheduler getScheduler(List<Pe> peList, String schedulerAlias);

    /** 
     * Gets an instance of virtual machines scheduler based on its alias.
     *
     * @param   schedulerAlias  the alias of the scheduler.
     * @return                  an instance of the type with the given alias.
     * @since                   1.0
     */          
    public static VmScheduler getInstance(String schedulerAlias) {
        if(schedulerAlias.equals("Space shared")) return VmScheduler.SPACE_SHARED;
        else if(schedulerAlias.equals("Time shared")) return VmScheduler.TIME_SHARED;
        else return VmScheduler.EXTENSION;
    }

    /** 
     * Gets all active virtual machines schedulers aliases.
     *
     * @return  an array of strings containing all active virtual machines
     *          schedulers aliases.
     * @since   1.0
     */    
    public static String[] getVmSchedulerNames() {
        String[] nativePolicies = new String[]{"Space shared", "Time shared"};
        List<String> extensionPolicies = ExtensionsLoader.getExtensionsAliasesByType("VmScheduler");
        extensionPolicies.addAll(Arrays.asList(nativePolicies));

        return extensionPolicies.toArray(new String[0]);
    }

}
