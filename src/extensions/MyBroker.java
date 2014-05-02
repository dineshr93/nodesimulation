package extensions;

import cloudoutput.extensions.brokers.Broker;
import java.util.List;

public class MyBroker extends Broker {

  public MyBroker(String name) throws Exception {
    super(name);
  }

  @Override
  public List<Integer> getDatacenterIdList() {
    return super.getDatacenterIdsList();
  }

  @Override
  public int getDatacenterId() {
    return getId();
  }
}