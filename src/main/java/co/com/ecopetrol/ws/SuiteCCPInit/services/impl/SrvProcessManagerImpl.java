package co.com.ecopetrol.ws.SuiteCCPInit.services.impl;

import co.com.ecopetrol.ws.SuiteCCPInit.services.interfaces.SrvProcessManager;
import co.com.ecopetrol.ws.SuiteCCPInit.timerServices.SrvTimerLodersData;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 *
 * @author STC
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SrvProcessManagerImpl implements SrvProcessManager{

    private Map<String, ScheduledThreadPoolExecutor> mapScheduledThreadPoolExecutorLoaders = null;

    @Autowired
    private Environment env;

    public Environment getEnv() {
        return env;
    }

    public void setEnv(Environment env) {
        this.env = env;
    }

    public Map<String, ScheduledThreadPoolExecutor> getMapScheduledThreadPoolExecutorLoaders() {
        if (this.mapScheduledThreadPoolExecutorLoaders == null) {
            this.mapScheduledThreadPoolExecutorLoaders = new HashMap<>();
        }
        return this.mapScheduledThreadPoolExecutorLoaders;
    }

    public void setMapScheduledThreadPoolExecutorLoaders(Map<String, ScheduledThreadPoolExecutor> mapScheduledThreadPoolExecutorLoaders) {
        this.mapScheduledThreadPoolExecutorLoaders = mapScheduledThreadPoolExecutorLoaders;
    }

    @Override
    public void initSchedulerLoader(String sgName, Integer port) {
        this.getMapScheduledThreadPoolExecutorLoaders().put(sgName, new ScheduledThreadPoolExecutor(1));
        this.getMapScheduledThreadPoolExecutorLoaders().get(sgName).schedule(new SrvTimerLodersData(sgName, port, this.getEnv()), 2, TimeUnit.SECONDS);
    }

    @Override
    public void stopSchedulerLoader(String sgName) {
        if (this.getMapScheduledThreadPoolExecutorLoaders().containsKey(sgName)) {
            System.out.println("DATA0202!!");
            this.getMapScheduledThreadPoolExecutorLoaders().get(sgName).shutdownNow();
        }
    }

}
