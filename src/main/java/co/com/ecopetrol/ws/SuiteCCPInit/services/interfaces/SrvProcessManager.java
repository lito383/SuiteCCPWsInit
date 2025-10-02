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
    
    public Map<String, Double> getMapSumValueTagListPiFromCassandraServerProdCBEByPeriodo(List<String> lstTags, String labelData, Calendar calendarStart, Calendar calendarEnd) throws Exception;
    
    public Map<String, Double> getMapSumValueTagListPiFromCassandraServerByPeriodo(List<String> lstTags, Calendar calendarStart, Calendar calendarEnd) throws Exception;

    public String getValueFromSystemParameter(String nameParameter);

    public Map<String, Double> getMapAvgValueTagListPiFromCassandraServerProdCBEByPeriodo(List<String> lstTags, String labelData, Calendar calendarStart, Calendar calendarEnd) throws Exception;

    public Map<String, String> getMapSystemParameter();

    public void saveAnalogDataProdCbeCassandraFromExcelData(String tagData, Double valueData, String labelData, Calendar calendarData) throws Exception;

    public Map<String, Double> getMapAvgValueTagListPiFromCassandraServerByPeriodo(List<String> lstTags, Calendar calendarStart, Calendar calendarEnd) throws Exception;

    public void saveAnalogDataCassandra(String tagData, Double valueData) throws Exception;

    public List<String> getLstTagsScadaFromCassandraData();

    public SortedMap<String, SortedMap<Calendar, Double>> getMapAvgValueTagListPiFromCassandraServerV3(List<String> lstTags, Calendar calendarStart, Calendar calendarEnd) throws Exception;

    public void addAllMapSetValues(Map<String, Double> mapValues);

    public void putMapSetValues(String tag, Double valueData);

    public Map<String, Double> getMapValuesFromRTUScadaData();

    public Map<String, Integer> getMapPortsServiceData();

    public void setMapPortsServiceData(Map<String, Integer> mapPortsService);

    public void initSchedulerLoader(String sgName, Integer port);

    public void stopSchedulerLoader(String sgName);
}
