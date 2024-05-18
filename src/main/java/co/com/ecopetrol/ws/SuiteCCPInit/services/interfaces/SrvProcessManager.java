package co.com.ecopetrol.ws.SuiteCCPInit.services.interfaces;

/**
 *
 * @author STC
 */
public interface SrvProcessManager {

    public void initSchedulerLoader(String sgName, Integer port);

    public void stopSchedulerLoader(String sgName);
}
