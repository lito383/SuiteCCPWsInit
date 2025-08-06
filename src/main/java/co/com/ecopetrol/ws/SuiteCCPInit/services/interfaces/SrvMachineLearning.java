package co.com.ecopetrol.ws.SuiteCCPInit.services.interfaces;

import co.com.ecopetrol.ws.SuiteCCPInit.entities.DefModelRef;
import java.util.List;

/**
 *
 * @author STC
 */
public interface SrvMachineLearning {
    
    
    public List<DefModelRef> getLstAllDefModelRef();

    public List<String> getPredictionDataFromModel(String locationModel, String locationInputFile) throws Exception;

    public void buildModel(String locationNameCSVInput, String locationNameModelBinary, String[] labelsInput, String labelPred, DefModelRef defModelRef) throws Exception;
}
