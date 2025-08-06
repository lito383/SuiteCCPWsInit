package co.com.ecopetrol.ws.SuiteCCPInit.services.impl;

import co.com.ecopetrol.ws.SuiteCCPInit.entities.ScanGroupCalc;
import co.com.ecopetrol.ws.SuiteCCPInit.repositories.ScanGroupCalcRepository;
import co.com.ecopetrol.ws.SuiteCCPInit.services.interfaces.SrvScanGroupCalc;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author STC
 */
@Service
public class SrvScanGroupCalcImpl implements SrvScanGroupCalc{

    @Autowired
    private ScanGroupCalcRepository scanGroupCalcRepository;

    public ScanGroupCalcRepository getScanGroupCalcRepository() {
        return scanGroupCalcRepository;
    }

    public void setScanGroupCalcRepository(ScanGroupCalcRepository scanGroupCalcRepository) {
        this.scanGroupCalcRepository = scanGroupCalcRepository;
    }
    
    @Override
     public List<ScanGroupCalc> getLstScanGroupCalcActivate(){
         return this.getScanGroupCalcRepository().getLstScanGroupCalcActivate();
     }

}
