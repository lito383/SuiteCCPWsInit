package co.com.ecopetrol.ws.SuiteCCPInit.services.impl;

import co.com.ecopetrol.ws.SuiteCCPInit.entities.SystemParameter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.com.ecopetrol.ws.SuiteCCPInit.repositories.SystemParameterRepository;
import co.com.ecopetrol.ws.SuiteCCPInit.services.interfaces.SrvSystemParameter;

/**
 *
 * @author STC
 */
@Service
public class SrvSystemParameterImpl implements SrvSystemParameter{

    @Autowired
    private SystemParameterRepository systemParameterRepository;

    public SystemParameterRepository getSystemParameterRepository() {
        return systemParameterRepository;
    }

    public void setSystemParameterRepository(SystemParameterRepository systemParameterRepository) {
        this.systemParameterRepository = systemParameterRepository;
    }

    public List<SystemParameter> getLstAllSystemParameter() throws Exception {
        return this.getSystemParameterRepository().getLstAllSystemParameter();
    }
}
