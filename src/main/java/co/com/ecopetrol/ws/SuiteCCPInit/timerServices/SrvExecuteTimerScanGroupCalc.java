package co.com.ecopetrol.ws.SuiteCCPInit.timerServices;

import co.com.ecopetrol.ws.SuiteCCPInit.entities.ScanGroupCalc;
import co.com.ecopetrol.ws.SuiteCCPInit.services.interfaces.SrvProcessManager;
import java.util.Map;

/**
 *
 * @author STC
 */
public class SrvExecuteTimerScanGroupCalc implements Runnable {

    private ScanGroupCalc scanGroupCalc = null;
    private SrvProcessManager srvProcessManager = null;

    public SrvExecuteTimerScanGroupCalc(SrvProcessManager srvProcessManager, ScanGroupCalc scanGroupCalc) {
        this.srvProcessManager = srvProcessManager;
        this.scanGroupCalc = scanGroupCalc;
    }

    public SrvProcessManager getSrvProcessManager() {
        return srvProcessManager;
    }

    public void setSrvProcessManager(SrvProcessManager srvProcessManager) {
        this.srvProcessManager = srvProcessManager;
    }

    public ScanGroupCalc getScanGroupCalc() {
        return scanGroupCalc;
    }

    public void setScanGroupCalc(ScanGroupCalc scanGroupCalc) {
        this.scanGroupCalc = scanGroupCalc;
    }

    @Override
    public void run() {
        if (this.getSrvProcessManager() == null || this.getScanGroupCalc() == null) {
            return;
        }
        String[] arrStrTags = this.getScanGroupCalc().getLstTags().split(";");
        if (this.getScanGroupCalc().getRefOperation().equals("ADD")) {
            Double valueRes = 0D;
            Map<String, Double> mapRes = this.getSrvProcessManager().getMapValuesFromRTUScadaData();
            Integer count = 0;
            for (Integer index = 0; index < arrStrTags.length; index++) {
                String tagRef = arrStrTags[index];
                if (mapRes.containsKey(tagRef)) {
                    Double res = mapRes.get(tagRef);
                    if (res != null) {
                        valueRes += res;
                        count++;
                    }
                }
            }
            if (count.equals(arrStrTags.length)) {
                try {
                    this.getSrvProcessManager().saveAnalogDataCassandra(this.getScanGroupCalc().getTagOut(), valueRes);
                } catch (Exception e) {
                }
            }
        }
    }
}
