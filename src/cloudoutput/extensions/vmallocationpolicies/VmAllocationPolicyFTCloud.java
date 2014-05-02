

package cloudoutput.extensions.vmallocationpolicies;

import cloudoutput.business.SettingBusiness;
import cloudoutput.enums.AllocationPolicy;
import cloudoutput.models.Migration;
import cloudoutput.simulation.Simulation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerHost;


public class VmAllocationPolicyFTCloud extends VmAllocationPolicySimple implements VmAllocationPolicyExtensible {

    private double upperUtilizationThreshold;
    
          
    public VmAllocationPolicyFTCloud(List<? extends PowerHost> list, double utilizationThreshold) {
        super(list);
        this.upperUtilizationThreshold = utilizationThreshold;
    }
    @Override
    public List<Migration> getListOfMigrationsToBeExecuted(List<? extends Vm> vmList) {
        List<Migration> migrationList = new ArrayList<Migration>();
        if (vmList.isEmpty()) {
            return migrationList;
        }
        
        //Verify the existence of overused hosts
        List<PowerHost> overusedHosts = getOverusedHosts();
        //If there is any overused host, then distribute vms
        if(!overusedHosts.isEmpty()) {
            distributeVms(migrationList, overusedHosts);
        }
        
        consolidateVms(migrationList);

        return migrationList;
    }
       
    public void distributeVms(List<Migration> migrationList, List<PowerHost> overusedHosts) {
        List<PowerHost> targetHosts = getNotOverusedHosts();
        
        if(!targetHosts.isEmpty()) {
       
            
  
            for(Migration migration : migrationList) {
                migration.setDescription("Distribution");
            }
        }
    }
    
          
    public void consolidateVms(List<Migration> migrationList) {
        List<PowerHost> activeHosts = getActiveHosts();        
        List<PowerHost> targetHosts = activeHosts;
        
        List<PowerHost> sourceHosts = activeHosts;
        List<Integer> hostsToBeTurnedOffIds = new ArrayList<Integer>();
        
        for (PowerHost sourceHost : sourceHosts) {
            List<Migration> newMigrationList = new ArrayList<Migration>();
     //       getConsolidatingMigrationList(newMigrationList, sourceHost, targetHosts, hostsToBeTurnedOffIds, getUpperUtilizationThreshold());
            migrationList.addAll(newMigrationList);
            hostsToBeTurnedOffIds.add(sourceHost.getId());
        }
        
        for (Migration migration : migrationList) {
            if(migration.getDescription() == null) {
                migration.setDescription("Consolidation");
            }
        }
    }

    public double getUpperUtilizationThreshold() {
        return this.upperUtilizationThreshold;
    }

      
    public List<PowerHost> getOverusedHosts() {
        List<PowerHost> overusedHosts = new ArrayList<PowerHost>();
        for (PowerHost host : this.<PowerHost>getHostList()) {            
            double cpuUtilizationRate = getHostCpuUtilizationRate(host);
            double ramUtilizationRate = getHostRamUtilizationRate(host);
            
            if (cpuUtilizationRate >= getUpperUtilizationThreshold()
                    || ramUtilizationRate >= getUpperUtilizationThreshold()) {
                overusedHosts.add(host);
            }
        }
        return overusedHosts;
    }

           
    public List<PowerHost> getNotOverusedHosts() {
        List<PowerHost> notOverusedHosts = new ArrayList<PowerHost>();
        for (PowerHost host : this.<PowerHost>getHostList()) {
            double cpuUtilizationRate = getHostCpuUtilizationRate(host);
            double ramUtilizationRate = getHostRamUtilizationRate(host);
            
            if ( cpuUtilizationRate < getUpperUtilizationThreshold()
                    && ramUtilizationRate  < getUpperUtilizationThreshold()) {
                notOverusedHosts.add(host);
            }
        }
        return notOverusedHosts;
    }
    
           
    public List<PowerHost> getActiveHosts() {
        List<PowerHost> activeHosts = new ArrayList<PowerHost>();
        for (PowerHost host : this.<PowerHost>getHostList()) {
            double cpuUtilization = host.getUtilizationOfCpuMips()/host.getTotalMips();
            double ramUtilization = host.getUtilizationOfRam()/host.getRam();
           
            if ( cpuUtilization > 0 && ramUtilization > 0) {
                activeHosts.add(host);
            }
        }
        return activeHosts;
    }
    
  
    @Override
    public boolean allocateHostForVm(Vm vm) {
        PowerHost allocatedHost = findHostForVm(vm);
        if (allocatedHost != null && allocatedHost.vmCreate(vm)) { //if vm has been succesfully created in the host
            getVmTable().put(vm.getUid(), allocatedHost);
            if (!Log.isDisabled()) {
                Log.print(String.format("%.2f: VM #" + vm.getId() + " has been allocated to the host #" + allocatedHost.getId() + "\n", CloudSim.clock()));
            }
            return true;
        }
        return false;
    }

    public PowerHost findHostForVm(Vm vm) {
        double minPower = Double.MAX_VALUE;
        PowerHost allocatedHost = null;

        for (PowerHost host : this.<PowerHost>getHostList()) {
            if (host.isSuitableForVm(vm)) {
                double maxUtilization = getMaxUtilizationAfterAllocation(host, vm);
                if ((!vm.isBeingInstantiated() && maxUtilization > getUpperUtilizationThreshold()) || (vm.isBeingInstantiated() && maxUtilization > 1.0)) {
                    continue;
                }
                
                            allocatedHost = host;
                   
            }
        }

        return allocatedHost;
    }

    
    protected double getMaxUtilizationAfterAllocation(PowerHost host, Vm vm) {
        List<Double> allocatedMipsForVm = null;
        PowerHost allocatedHost = (PowerHost) vm.getHost();

        if (allocatedHost != null) {
            allocatedMipsForVm = vm.getHost().getAllocatedMipsForVm(vm);
        }

        if (!host.allocatePesForVm(vm, vm.getCurrentRequestedMips())) {
            return -1;
        }

        double maxUtilization = host.getMaxUtilizationAmongVmsPes(vm);

        host.deallocatePesForVm(vm);

        if (allocatedHost != null && allocatedMipsForVm != null) {
            vm.getHost().allocatePesForVm(vm, allocatedMipsForVm);
        }

        return maxUtilization;
    }

   
    @Override
    public void deallocateHostForVm(Vm vm) {
        if (getVmTable().containsKey(vm.getUid())) {
            PowerHost host = (PowerHost) getVmTable().remove(vm.getUid());
            if (host != null) {
                host.vmDestroy(vm);
            }
        }
    }

    
 
    
    protected static double getHostCpuUtilization(PowerHost host) {
        double cpuUtilization = host.getUtilizationOfCpuMips();

        //Disconsider the resources of vms migrating out
        for (Vm vm : host.getVmList()) {
            if (vm.isInMigration()) {
                cpuUtilization -= host.getTotalAllocatedMipsForVm(vm);
            }
        }

        //Include resources of vms migrating in
        for (Vm vm : host.getVmsMigratingIn()) {
            if (vm.isInMigration()) {
                cpuUtilization += vm.getCurrentRequestedTotalMips();
            }
        }

        return cpuUtilization;
    }

    protected static double getHostRamUtilization(PowerHost host) {
        double ramUtilization = host.getUtilizationOfRam();

        //Disconsider the resources of vms migrating out
        for (Vm vm : host.getVmList()) {
            if (vm.isInMigration()) {
                ramUtilization -= vm.getRam();
            }
        }

        //Include resources of vms migrating in
        for (Vm vm : host.getVmsMigratingIn()) {
            if (vm.isInMigration()) {
                ramUtilization += vm.getRam();
            }
        }

        return ramUtilization;
    }

    
    public static double getHostCpuUtilizationRate(PowerHost host) {
        double cpuUtilizationRate = getHostCpuUtilization(host) / host.getTotalMips();

        List<Double> previousValuesCpu = Simulation.getDataCollector().getMonitoredUsedResources(host.getDatacenter().getName(), host.getId(), "CPU");
        for (Double previousValue : previousValuesCpu) {
            cpuUtilizationRate += (previousValue / 100);
        }
        cpuUtilizationRate /= (previousValuesCpu.size() + 1);

        return cpuUtilizationRate;
    }

    
    public static double getHostRamUtilizationRate(PowerHost host) {
        double ramUtilizationRate = getHostRamUtilization(host) / host.getRam();

        List<Double> previousValuesRam = Simulation.getDataCollector().getMonitoredUsedResources(host.getDatacenter().getName(), host.getId(), "RAM");
        for (Double previousValue : previousValuesRam) {
            ramUtilizationRate += (previousValue / 100);
        }
        ramUtilizationRate /= (previousValuesRam.size() + 1);

        return ramUtilizationRate;
    }

}
