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

package cloudoutput.extensions.vmallocationpolicies;

import cloudoutput.enums.AllocationPolicy;
import cloudoutput.models.Migration;
import java.util.*;
import org.cloudbus.cloudsim.Vm;

/**
 * A subtype of CloudSim's VmAllocationPolicySimple class.
 * All user-implemented new virtual machine allocation policies must be
 * a subtype of this class.
 */
public interface VmAllocationPolicyExtensible  {
	
    
    /** 
     * A method whose implementations must optimize the allocation
     * of virtual machines.
     *
     * @param   vmList  a list of virtual machines whose allocation must be
     *                  optimized.
     * @return          a list of executed migrations.
     * @since   1.1
     */      
    List<Migration> getListOfMigrationsToBeExecuted(List<? extends Vm> vmList);
    
}
