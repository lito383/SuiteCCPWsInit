package co.com.ecopetrol.ws.SuiteCCPInit.services.interfaces;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 *
 * @author STC
 */
public interface SrvProcessManager {
    
    public void saveAnalogDataCassandra(String tagData, Double valueData) throws Exception;

    public List<String> getLstTagsScadaFromCassandraData();

    public SortedMap<String, SortedMap<Calendar, Double>> getMapAvgValueTagListPiFromCassandraServerV3(List<String> lstTags, Calendar calendarStart, Calendar calendarEnd) throws Exception;

    public void addAllMapSetValues(Map<String, Double> mapValues);

    public Map<String, Double> getMapValuesFromRTUScadaData();

    public Map<String, Integer> getMapPortsServiceData();

    public void setMapPortsServiceData(Map<String, Integer> mapPortsService);

    public void initSchedulerLoader(String sgName, Integer port);

    public void stopSchedulerLoader(String sgName);
}
