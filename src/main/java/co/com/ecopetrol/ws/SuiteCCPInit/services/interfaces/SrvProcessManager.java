package co.com.ecopetrol.ws.SuiteCCPInit.services.interfaces;

import java.util.Map;

/**
 *
 * @author STC
 */
public interface SrvProcessManager {

    public void addAllMapSetValues(Map<String, Double> mapValues);

    public Map<String, Double> getMapValuesFromRTUScadaData();

    public Map<String, Integer> getMapPortsServiceData();

    public void setMapPortsServiceData(Map<String, Integer> mapPortsService);

    public void initSchedulerLoader(String sgName, Integer port);

    public void stopSchedulerLoader(String sgName);
}
