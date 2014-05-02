

package cloudoutput.extensions;

import cloudoutput.extensions.vmallocationpolicies.VmAllocationPolicyExtensible;
import cloudoutput.models.Migration;
import cloudoutput.simulation.Simulation;
import java.util.List;
import java.util.Map;
import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.predicates.PredicateType;
import org.cloudbus.cloudsim.power.PowerHost;


public class PowerDatacenter extends org.cloudbus.cloudsim.power.PowerDatacenter {
    

    private double monitoringInterval;
    
    private double lastMonitoringTime;

   
    public PowerDatacenter(String name, DatacenterCharacteristics characteristics,
                           VmAllocationPolicy vmAllocationPolicy, List<Storage> storageList,
                           double schedulingInterval, double monitoringInterval) throws Exception {

        super(name,characteristics,vmAllocationPolicy,storageList, schedulingInterval);
        this.monitoringInterval = monitoringInterval;
        this.lastMonitoringTime = 0;
    }

    
    @Override
    protected void updateCloudletProcessing() {
        if (getCloudletSubmitted() == -1 || getCloudletSubmitted() == CloudSim.clock()) {
            CloudSim.cancelAll(getId(), new PredicateType(CloudSimTags.VM_DATACENTER_EVENT));
            schedule(getId(), getSchedulingInterval(), CloudSimTags.VM_DATACENTER_EVENT);
            return;
        }
        double currentTime = CloudSim.clock();
        double timeframePower = 0.0;

        // if some time passed since last processing
        if (currentTime > getLastProcessTime()) {
            double timeDiff = currentTime - getLastProcessTime();
            double minTime = Double.MAX_VALUE;

            Log.printLine("\n");

            for (PowerHost host : this.<PowerHost>getHostList()) {
                double hostPower = 0.0;
                if (host.getUtilizationOfCpu() > 0) {
                    try {
                        hostPower = host.getPower() * timeDiff;
                        timeframePower += hostPower;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            for (PowerHost host : this.<PowerHost>getHostList()) {
                double time = host.updateVmsProcessing(currentTime); // inform VMs to update processing
                if (time < minTime) {
                    minTime = time;
                }
            }
            setPower(getPower() + timeframePower);
            
            if((currentTime - this.lastMonitoringTime) >= this.getMonitoringInterval()) {
                this.lastMonitoringTime = currentTime;

                if (!isDisableMigrations()) {

                    //Get VM migration list according to active policies
                    List<Migration> migrationList = ((VmAllocationPolicyExtensible) getVmAllocationPolicy()).getListOfMigrationsToBeExecuted(getVmList());

                    for (Migration migration : migrationList) {
                        Vm vm = migration.getVm();
                        PowerHost targetHost = (PowerHost) migration.getTargetHost();
                        PowerHost oldHost = (PowerHost) migration.getSourceHost();
                        migration.setTime(CloudSim.clock());

                        targetHost.addMigratingInVm(vm);

                        if (oldHost == null) {
                            Log.formatLine("%.2f: Migration of VM #%d to Host #%d has started", CloudSim.clock(), vm.getId(), targetHost.getId());
                        } else {
                            Log.formatLine("%.2f: Migration of VM #%d from Host #%d to Host #%d has started", CloudSim.clock(), vm.getId(), oldHost.getId(), targetHost.getId());
                        }

                        incrementMigrationCount();

                        vm.setInMigration(true);

                      
                        send(getId(), vm.getRam() / ((double) vm.getBw() / 8000) + 10, CloudSimTags.VM_MIGRATE, migration.getMigrationMap());
                    }

                    if(!migrationList.isEmpty()) Simulation.getDataCollector().flushMigrations(migrationList);
                }
                
                //Collect monitored used resources
                Simulation.getDataCollector().collectMonitoredUsedResources();
            }

            // schedules an event to the next time
            if (minTime != Double.MAX_VALUE) {
                CloudSim.cancelAll(getId(), new PredicateType(CloudSimTags.VM_DATACENTER_EVENT));
                send(getId(), getSchedulingInterval(), CloudSimTags.VM_DATACENTER_EVENT);
            }

            setLastProcessTime(currentTime);
            Simulation.getDataCollector().collectData();
        }
    }
    
    /**
     * Gets the debts of this datacenter.
     *
     * @return the debts of this datacenter.
     */    
    @Override
    public Map<Integer, Double> getDebts() {
        return super.getDebts();
    }

    /**
     * Gets the monitoring interval of this datacenter.
     *
     * @return the period between data collections.
     */ 
    public double getMonitoringInterval() {
        return monitoringInterval;
    }
    
}
