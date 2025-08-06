package co.com.ecopetrol.ws.SuiteCCPInit.timerServices;

import co.com.ecopetrol.ws.SuiteCCPInit.commons.GeneralsEjb;
import co.com.ecopetrol.ws.SuiteCCPInit.entities.DefModelRef;
import co.com.ecopetrol.ws.SuiteCCPInit.services.interfaces.SrvMachineLearning;
import co.com.ecopetrol.ws.SuiteCCPInit.services.interfaces.SrvProcessManager;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author STC
 */
public class SrvThreadGenCsvML implements Runnable {

    private DefModelRef defModelRef = null;
    private SrvProcessManager srvProcessManager = null;
    private SrvMachineLearning srvMachineLearning = null;

    public SrvThreadGenCsvML(SrvProcessManager srvProcessManager, DefModelRef defModelRef, SrvMachineLearning srvMachineLearning) {
        this.srvProcessManager = srvProcessManager;
        this.defModelRef = defModelRef;
        this.srvMachineLearning = srvMachineLearning;
    }

    public DefModelRef getDefModelRef() {
        return defModelRef;
    }

    public void setDefModelRef(DefModelRef defModelRef) {
        this.defModelRef = defModelRef;
    }

    public SrvMachineLearning getSrvMachineLearning() {
        return srvMachineLearning;
    }

    public void setSrvMachineLearning(SrvMachineLearning srvMachineLearning) {
        this.srvMachineLearning = srvMachineLearning;
    }

    public SrvProcessManager getSrvProcessManager() {
        return srvProcessManager;
    }

    public void setSrvProcessManager(SrvProcessManager srvProcessManager) {
        this.srvProcessManager = srvProcessManager;
    }

    @Override
    public void run() {

        if (this.getSrvProcessManager() == null || this.getDefModelRef() == null || this.getSrvMachineLearning() == null) {
            return;
        }

        String locationFileCSVInputML = this.getSrvProcessManager().getValueFromSystemParameter("path_csv_input_ml");
        String locationFileModelOutputML = this.getSrvProcessManager().getValueFromSystemParameter("path_model_output_ml");
        if (locationFileCSVInputML == null || locationFileModelOutputML == null) {
            return;
        }

        if (locationFileCSVInputML.equals("") || locationFileModelOutputML.equals("")) {
            return;
        }

        if (this.getDefModelRef().getRes() == null) {
            return;
        }
        if (this.getDefModelRef().getRes().equals("")) {
            return;
        }

        Calendar calendarCurrent = Calendar.getInstance();

        String[] arrHeader1 = this.getDefModelRef().getH1().split(";");
        String[] arrHeader2 = (this.getDefModelRef().getH2() != null ? this.getDefModelRef().getH2().split(";") : null);
        String[] arrHeader3 = (this.getDefModelRef().getH3() != null ? this.getDefModelRef().getH3().split(";") : null);
        String[] arrHeader4 = (this.getDefModelRef().getH4() != null ? this.getDefModelRef().getH4().split(";") : null);

        String[] headers = new String[4];
        String[] tags = new String[4];

        String[] arrRes = this.getDefModelRef().getRes().split(";");

        headers[0] = arrHeader1[0];
        if (arrHeader1.length > 1) {
            tags[0] = arrHeader1[1];
        }

        if (arrHeader2 != null) {
            headers[1] = arrHeader2[0];
            if (arrHeader2.length > 1) {
                tags[1] = arrHeader2[1];
            }
        }
        if (arrHeader3 != null) {
            headers[2] = arrHeader3[0];
            if (arrHeader3.length > 1) {
                tags[2] = arrHeader3[1];
            }
        }
        if (arrHeader4 != null) {
            headers[3] = arrHeader4[0];
            if (arrHeader4.length > 1) {
                tags[3] = arrHeader4[1];
            }
        }

        switch (this.getDefModelRef().getRefModel()) {

            case "DEF_AVG_DAY_CBE_01" -> {

                String nameFileCSVIN = this.getDefModelRef().getRefModel() + "_" + GeneralsEjb.getDesFechaSqlFormatFromCalendarV2(calendarCurrent) + ".csv";

                Calendar calendarStart = Calendar.getInstance();
                calendarStart.setTime(calendarCurrent.getTime());
                calendarStart.add(Calendar.YEAR, -2);
                calendarStart.set(Calendar.HOUR_OF_DAY, 0);
                calendarStart.set(Calendar.MINUTE, 0);
                calendarStart.set(Calendar.SECOND, 0);
                calendarStart.set(Calendar.MILLISECOND, 0);

                Calendar calendarEnd = Calendar.getInstance();
                calendarEnd.setTime(calendarCurrent.getTime());
                calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
                calendarEnd.set(Calendar.MINUTE, 59);
                calendarEnd.set(Calendar.SECOND, 59);
                calendarEnd.set(Calendar.MILLISECOND, 999);

                Calendar calendarAux = Calendar.getInstance();
                calendarAux.setTime(calendarStart.getTime());

                List<String> lstTags = new ArrayList<>();

                for (Integer index = 0; index < tags.length; index++) {
                    if (tags[index] != null) {
                        lstTags.add(tags[index]);
                    }
                }
                lstTags.add(arrRes[1]);//TARGET VAR!!

                CSVWriter cSVWriter = null;
                String fullNameFile = locationFileCSVInputML + nameFileCSVIN;
                try {
                    cSVWriter = new CSVWriter(new FileWriter(fullNameFile), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
                } catch (Exception e) {
                    System.out.println("Error al crear el archivo " + fullNameFile);
                    cSVWriter = null;
                }

                if (cSVWriter == null) {
                    return;
                }

                List<String> lstHeadersData = new ArrayList<>();
                for (Integer index = 0; index < headers.length; index++) {
                    if (headers[index] == null) {
                        break;
                    }
                    lstHeadersData.add(headers[index]);
                }
                lstHeadersData.add(arrRes[0]);

                if (lstTags.size() != lstHeadersData.size()) {
                    return;
                }

                String[] arrHeaderCSV = new String[lstHeadersData.size()];
                Integer indexAux = 0;
                for (String header : lstHeadersData) {
                    arrHeaderCSV[indexAux] = header;
                    indexAux++;
                }

                cSVWriter.writeNext(arrHeaderCSV);
                Integer sizeLstTags = lstTags.size();

                List<String> lstTagsCCP = new ArrayList<>();
                List<String> lstTagsCBE = new ArrayList<>();
                Map<String, String> mapTagsRel = new HashMap<>();
                for (String tag : lstTags) {
                    String[] arrTag = tag.split("@");
                    if (arrTag.length < 1) {
                        continue;
                    }
                    switch (arrTag[0]) {
                        case "CCP" -> {
                            lstTagsCCP.add(arrTag[1]);
                        }
                        case "CBE" -> {
                            lstTagsCBE.add(arrTag[1]);
                        }
                    }
                    mapTagsRel.put(tag, arrTag[1]);
                }

                do {

                    Calendar calendarStartAux = Calendar.getInstance();
                    calendarStartAux.setTime(calendarAux.getTime());
                    calendarStartAux.set(Calendar.HOUR_OF_DAY, 0);
                    calendarStartAux.set(Calendar.MINUTE, 0);
                    calendarStartAux.set(Calendar.SECOND, 0);
                    calendarStartAux.set(Calendar.MILLISECOND, 0);

                    Calendar calendarEndAux = Calendar.getInstance();
                    calendarEndAux.setTime(calendarAux.getTime());
                    calendarEndAux.set(Calendar.HOUR_OF_DAY, 23);
                    calendarEndAux.set(Calendar.MINUTE, 59);
                    calendarEndAux.set(Calendar.SECOND, 59);
                    calendarEndAux.set(Calendar.MILLISECOND, 999);

                    try {

                        Map<String, Double> mapValuesCCP = this.getSrvProcessManager().getMapAvgValueTagListPiFromCassandraServerByPeriodo(lstTagsCCP, calendarStartAux, calendarEndAux);
                        Map<String, Double> mapValuesCBE = this.getSrvProcessManager().getMapAvgValueTagListPiFromCassandraServerProdCBEByPeriodo(lstTagsCBE, null, calendarStartAux, calendarEndAux);

                        Map<String, Double> mapValues = new HashMap<>();
                        mapValues.putAll(mapValuesCCP);
                        mapValues.putAll(mapValuesCBE);

                        String[] arrNewLine = new String[sizeLstTags];
                        Boolean valid = Boolean.FALSE;
                        for (Integer index = 0; index < sizeLstTags; index++) {
                            String tagReal = lstTags.get(index);
                            String tagAux = mapTagsRel.get(tagReal);
                            if (tagAux != null) {
                                if (mapValues.containsKey(tagAux)) {
                                    Double valueData = mapValues.get(tagAux);
                                    if (valueData != null) {
                                        valid = Boolean.TRUE;
                                        arrNewLine[index] = valueData.toString();
                                    }
                                }
                            }
                        }
                        if (valid.equals(Boolean.TRUE)) {
                            cSVWriter.writeNext((String[]) arrNewLine);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    calendarAux.add(Calendar.DAY_OF_YEAR, 1);
                } while (calendarEnd.after(calendarAux));
                try {
                    cSVWriter.close();
                } catch (Exception e) {
                }
                String[] arrHeaderPred = new String[arrHeaderCSV.length - 1];
                Integer  sizearrHeaderCSV = arrHeaderCSV.length;
                for (Integer indexHead = 0; indexHead < sizearrHeaderCSV -2; indexHead ++){
                    arrHeaderPred[indexHead] = arrHeaderCSV[indexHead];
                }
                try {
                    this.getSrvMachineLearning().buildModel(fullNameFile, locationFileModelOutputML, arrHeaderPred, arrHeaderCSV[sizearrHeaderCSV - 1], this.getDefModelRef());
                } catch (Exception e) {
                    e.printStackTrace();
                }                
            }

        }

    }

}
