package co.com.ecopetrol.ws.SuiteCCPInit.timerServices;

import co.com.ecopetrol.ws.SuiteCCPInit.services.interfaces.SrvProcessManager;
import java.util.Map;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author STC
 */
public class SrvTimerGetDataFromServices implements Runnable {

    private SrvProcessManager srvProcessManager = null;

    public SrvProcessManager getSrvProcessManager() {
        return srvProcessManager;
    }

    public void setSrvProcessManager(SrvProcessManager srvProcessManager) {
        this.srvProcessManager = srvProcessManager;
    }

    public SrvTimerGetDataFromServices(SrvProcessManager srvProcessManager) {
        this.srvProcessManager = srvProcessManager;
    }

    private SimpleClientHttpRequestFactory getClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(4000);
        clientHttpRequestFactory.setReadTimeout(4000);
        return clientHttpRequestFactory;
    }

    @Override
    public void run() {
        if (this.getSrvProcessManager() == null) {
            return;
        }
        Map<String, Integer> mapPorts = this.getSrvProcessManager().getMapPortsServiceData();
        if (mapPorts == null) {
            return;
        }
        if (mapPorts.keySet().isEmpty()) {
            return;
        }
        for (String sgName : mapPorts.keySet()) {
            Integer port = mapPorts.get(sgName);
            if (port == null) {
                continue;
            }
            String uri = "http://localhost:" + port.toString() + "/scangroup/getAllDataItems";
            //System.out.println("TRAYENDO DATA DESDE: " + uri);
            Map<String, Double> mapValues = null;
            try {
                RestTemplate restTemplate = new RestTemplate(this.getClientHttpRequestFactory());
                mapValues = (Map<String, Double>) restTemplate.getForObject(uri, Map.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mapValues == null) {
                continue;
            }
           // System.out.println("        --> SIZE_RESPONSE: " + mapValues.keySet().size());
            if (mapValues == null) {
                continue;
            }
            if (mapValues.isEmpty()) {
                continue;
            }
            this.getSrvProcessManager().addAllMapSetValues(mapValues);
        }
    }

}
