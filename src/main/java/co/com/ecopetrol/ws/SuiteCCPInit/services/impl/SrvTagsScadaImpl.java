package co.com.ecopetrol.ws.SuiteCCPInit.services.impl;

import co.com.ecopetrol.ws.SuiteCCPInit.entities.ScanGroupEngItem;
import co.com.ecopetrol.ws.SuiteCCPInit.repositories.ScanGroupEngItemRepository;
import co.com.ecopetrol.ws.SuiteCCPInit.services.interfaces.SrvTagsScada;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author STC
 */
@Service
public class SrvTagsScadaImpl implements SrvTagsScada {

    @Autowired
    private ScanGroupEngItemRepository scanGroupEngItemRepository;

    public ScanGroupEngItemRepository getScanGroupEngItemRepository() {
        return scanGroupEngItemRepository;
    }

    public void setScanGroupEngItemRepository(ScanGroupEngItemRepository scanGroupEngItemRepository) {
        this.scanGroupEngItemRepository = scanGroupEngItemRepository;
    }

    @Override
    public Map<Long, List<ScanGroupEngItem>> getMapScanGroupEngItemByIdScanGroupEng() throws Exception {
        Map<Long, List<ScanGroupEngItem>> mapRes = new HashMap<Long, List<ScanGroupEngItem>>();
        List<ScanGroupEngItem> lstScanGroupEngItems = this.getScanGroupEngItemRepository().findAll();
        for (ScanGroupEngItem scanGroupEngItem : lstScanGroupEngItems) {
            if (!mapRes.containsKey(scanGroupEngItem.getScanGroupEng().getId())) {
                mapRes.put(scanGroupEngItem.getScanGroupEng().getId(), new ArrayList<ScanGroupEngItem>());
            }
            mapRes.get(scanGroupEngItem.getScanGroupEng().getId()).add(scanGroupEngItem);
        }
        return mapRes;
    }
}
