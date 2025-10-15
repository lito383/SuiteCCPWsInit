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

        if (this.getScanGroupCalc().getRefOperation().equals("ADD")) {
            Double valueRes = 0D;
            Map<String, Double> mapRes = this.getSrvProcessManager().getMapValuesFromRTUScadaData();

            if (this.getScanGroupCalc().getT1() != null && this.getScanGroupCalc().getM1() != null) {
                String t1 = this.getScanGroupCalc().getT1();
                if (mapRes.containsKey(t1)) {
                    Double r1 = mapRes.get(t1);
                    if (r1 != null) {
                        valueRes += (r1 * this.getScanGroupCalc().getM1());
                    }
                }
            }

            if (this.getScanGroupCalc().getT2() != null && this.getScanGroupCalc().getM2() != null) {
                String t2 = this.getScanGroupCalc().getT2();
                if (mapRes.containsKey(t2)) {
                    Double r2 = mapRes.get(t2);
                    if (r2 != null) {
                        valueRes += (r2 * this.getScanGroupCalc().getM2());
                    }
                }
            }

            if (this.getScanGroupCalc().getT3() != null && this.getScanGroupCalc().getM3() != null) {
                String t3 = this.getScanGroupCalc().getT3();
                if (mapRes.containsKey(t3)) {
                    Double r3 = mapRes.get(t3);
                    if (r3 != null) {
                        valueRes += (r3 * this.getScanGroupCalc().getM3());
                    }
                }
            }

            if (this.getScanGroupCalc().getT4() != null && this.getScanGroupCalc().getM4() != null) {
                String t4 = this.getScanGroupCalc().getT4();
                if (mapRes.containsKey(t4)) {
                    Double r4 = mapRes.get(t4);
                    if (r4 != null) {
                        valueRes += (r4 * this.getScanGroupCalc().getM4());
                    }
                }
            }

            if (this.getScanGroupCalc().getT5() != null && this.getScanGroupCalc().getM5() != null) {
                String t5 = this.getScanGroupCalc().getT5();
                if (mapRes.containsKey(t5)) {
                    Double r5 = mapRes.get(t5);
                    if (r5 != null) {
                        valueRes += (r5 * this.getScanGroupCalc().getM5());
                    }
                }
            }

            if (this.getScanGroupCalc().getT6() != null && this.getScanGroupCalc().getM6() != null) {
                String t6 = this.getScanGroupCalc().getT6();
                if (mapRes.containsKey(t6)) {
                    Double r6 = mapRes.get(t6);
                    if (r6 != null) {
                        valueRes += (r6 * this.getScanGroupCalc().getM6());
                    }
                }
                
            }
            
            if (this.getScanGroupCalc().getNombre().equals("RED_CSBE")){
                if (valueRes < 0D){
                    valueRes = 0D;
                }
            }

            try {
                this.getSrvProcessManager().putMapSetValues(this.getScanGroupCalc().getTagOut(), valueRes);
                this.getSrvProcessManager().saveAnalogDataCassandra(this.getScanGroupCalc().getTagOut(), valueRes);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
