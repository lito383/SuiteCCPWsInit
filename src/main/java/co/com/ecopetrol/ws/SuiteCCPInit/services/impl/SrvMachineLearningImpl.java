package co.com.ecopetrol.ws.SuiteCCPInit.services.impl;

import co.com.ecopetrol.ws.SuiteCCPInit.entities.DefModelRef;
import co.com.ecopetrol.ws.SuiteCCPInit.repositories.DefModelRefRepository;
import co.com.ecopetrol.ws.SuiteCCPInit.services.interfaces.SrvMachineLearning;
import hex.Model;
import hex.deeplearning.DeepLearning;
import hex.deeplearning.DeepLearningModel;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import water.H2O;
import water.H2OApp;
import water.Key;
import water.fvec.Frame;
import water.fvec.NFSFileVec;
import water.fvec.Vec;
import water.parser.ParseDataset;

/**
 *
 * @author STC
 */
@Service
public class SrvMachineLearningImpl implements SrvMachineLearning {

    @Autowired
    private DefModelRefRepository defModelRefRepository;

    public DefModelRefRepository getDefModelRefRepository() {
        return defModelRefRepository;
    }

    public void setDefModelRefRepository(DefModelRefRepository defModelRefRepository) {
        this.defModelRefRepository = defModelRefRepository;
    }

    @Override
    public List<DefModelRef> getLstAllDefModelRef() {
        return this.getDefModelRefRepository().getLstAllDefModelRef();
    }

    @Override
    public List<String> getPredictionDataFromModel(String locationModel, String locationInputFile) throws Exception {

        if (H2O.getCloudSize() <= 0) {
            // Iniciar el clúster H2O
            H2OApp.main(new String[]{"-name", "H2OClusterCCP"});

            // Esperar a que el clúster esté listo
            H2O.waitForCloudSize(1, 10000); // Espera 1 nodo, timeout de 10 segundos
        }

        NFSFileVec fileVec = NFSFileVec.make(locationInputFile);
        Frame frame = ParseDataset.parse(Key.make(), fileVec.getKey());
        DeepLearningModel deepLearningModel = (DeepLearningModel) Model.importBinaryModel(locationModel);
        Frame predictions = deepLearningModel.score(frame);
        Vec predictVec = predictions.vec("predict");
        List<String> lstRes = new ArrayList<>();
        for (long i = 0; i < predictVec.length(); i++) {
            String predictedValue = predictVec.isCategorical()
                    ? predictVec.domain()[(int) predictVec.at(i)]
                    : String.valueOf(predictVec.at(i));
            lstRes.add(predictedValue);
        }
        return lstRes;
    }

    @Override
    public void buildModel(String locationNameCSVInput, String locationNameModelBinary, String[] labelsInput, String labelPred) throws Exception {
        if (H2O.getCloudSize() <= 0) {
            // Iniciar el clúster H2O
            H2OApp.main(new String[]{"-name", "H2OClusterCCP"});

            // Esperar a que el clúster esté listo
            H2O.waitForCloudSize(1, 10000); // Espera 1 nodo, timeout de 10 segundos
        }

        try {
            // Cargar el conjunto de datos (por ejemplo, Iris)
            String datasetPath = locationNameCSVInput;
            // Convertir la URL en un FileVec
            NFSFileVec fileVec = NFSFileVec.make(datasetPath);
            // Parsear el archivo a un Frame
            Frame frame = ParseDataset.parse(Key.make(), fileVec.getKey());

            // Especificar las columnas predictoras y la variable objetivo
            String[] x = labelsInput;
            String y = labelPred;

            // Configurar los parámetros del modelo de Deep Learning
            DeepLearningModel.DeepLearningParameters params = new DeepLearningModel.DeepLearningParameters();
//            params._train = frame._key;
//            params._response_column = y;
//            params._hidden = new int[]{10, 10}; // Capas ocultas con 10 neuronas cada una
//            params._epochs = 10; // Número de épocas
//            params._activation = DeepLearningModel.DeepLearningParameters.Activation.Rectifier; // Función de activación
//            params._seed = 1234; // Semilla para reproducibilidad

            params._train = frame._key;
            params._response_column = y;
            params._mini_batch_size = 64; // Reducir uso de memoria
            params._hidden = new int[]{100, 100}; // Arquitectura más ligera
            params._seed = 1234; // Semilla para reproducibilidad
            params._activation = DeepLearningModel.DeepLearningParameters.Activation.RectifierWithDropout;
            params._hidden_dropout_ratios = new double[]{0.2, 0.2}; // 20% dropout
            params._epochs = 10; // Menos épocas con early stopping
            params._adaptive_rate = true; // Tasa de aprendizaje adaptativa
            params._stopping_rounds = 5; // Early stopping
            //params._stopping_metric = "AUTO";
            params._sparse = true; // Si el dataset es disperso
            params._categorical_encoding = DeepLearningModel.DeepLearningParameters.CategoricalEncodingScheme.OneHotInternal;
            params._ignore_const_cols = true;

            // Entrenar el modelo
            DeepLearning dl = new DeepLearning(params);
            DeepLearningModel model = dl.trainModel().get();

            model.exportBinaryModel(locationNameModelBinary, true);

//            // Realizar predicciones
//            Frame predictions = model.score(frame);
//            //Log.info("Predicciones: ");
//            Vec predictVec = predictions.vec("predict");
//
//            System.out.println("Valores predichos:");
//            for (long i = 0; i < predictVec.length(); i++) {
//                String predictedClass = predictVec.isCategorical()
//                        ? predictVec.domain()[(int) predictVec.at(i)] // Obtener la clase como string
//                        : String.valueOf(predictVec.at(i)); // Para regresión, obtener el valor numérico
//                System.out.println("Fila " + i + ": " + predictedClass);
//            }
            // Liberar recursos
            model.delete();
            frame.delete();
            fileVec.remove();

            // H2O.shutdown(0);
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            // Apagar el clúster H2O
            H2O.orderlyShutdown();
        }
    }
}
