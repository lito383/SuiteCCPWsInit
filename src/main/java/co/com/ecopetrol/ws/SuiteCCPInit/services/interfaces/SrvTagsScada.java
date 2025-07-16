package co.com.ecopetrol.ws.SuiteCCPInit.services.interfaces;

import co.com.ecopetrol.ws.SuiteCCPInit.entities.ScanGroupEngItem;
import java.util.List;
import java.util.Map;

/**
 *
 * @author STC
 */
public interface SrvTagsScada {

    public Map<Long, List<ScanGroupEngItem>> getMapScanGroupEngItemByIdScanGroupEng() throws Exception;
}
