package co.com.ecopetrol.ws.SuiteCCPInit.services.impl;

import co.com.ecopetrol.ws.SuiteCCPInit.beans.FormulasMath;
import co.com.ecopetrol.ws.SuiteCCPInit.commons.GeneralsEjb;
import co.com.ecopetrol.ws.SuiteCCPInit.services.interfaces.SrvProcessDocs;
import co.com.ecopetrol.ws.SuiteCCPInit.services.interfaces.SrvProcessManager;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 *
 * @author STC
 */
@Service
public class SrvProcessDocsImpl implements SrvProcessDocs {

    @Autowired
    private SrvProcessManager srvProcessManager;
    @Autowired
    private Environment environment;

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public SrvProcessManager getSrvProcessManager() {
        return srvProcessManager;
    }

    public void setSrvProcessManager(SrvProcessManager srvProcessManager) {
        this.srvProcessManager = srvProcessManager;
    }

    @Override
    public void processFileTemp03() throws Exception {
        String fullPath = this.getEnvironment().getProperty("path.location.import_files2");
        File filePathImportFilesDirectory = new File(fullPath);
        File[] arrFiles = filePathImportFilesDirectory.listFiles();
        
        Map<String, String> mapTagsCassandra = new HashMap<>();
        mapTagsCassandra.put("CSRGCS", "CBESUR.BK.CSRGCS.MW");
        mapTagsCassandra.put("CSR3", "CBESUR.BK.CSR3.MW");
        mapTagsCassandra.put("CSRPC", "CBESUR.BK.CSRPC.MW");
        mapTagsCassandra.put("CSRET", "CBESUR.BK.CSRET.MW");
        for (Integer indexFile = 0; indexFile < arrFiles.length; indexFile++) {
            System.out.println("PROCESANDO ARCHIVO: " + arrFiles[indexFile].getName());
            try {
                Workbook workbook = new XSSFWorkbook(arrFiles[indexFile]);
                Sheet sheet = workbook.getSheetAt(0);

                // Iterate through each row
                List<String> lstTagsData = new ArrayList<>();
                Integer index = 0;
                Map<String, String> mapTags = new HashMap<>();
                Map<String, Integer> mapColIndex = new HashMap<>();

                mapTags.put("FLUJO", "FI-3631/AI1/PV.CV");
                mapTags.put("V1", "LI-3631A/AI1/PV.CV");
                mapTags.put("V2", "LI-3631B/AI1/PV.CV");
                Integer rowValidData = null;
                Map<Calendar, Double[]> mapData = new LinkedHashMap<>();
                for (Row row : sheet) {
                    if (rowValidData == null) {
                        for (Cell cell : row) {
                            if (cell.getCellType() == CellType.STRING) {

                            }
                            if (cell.getCellType() != CellType.STRING) {
                                continue;
                            }
                            String tagName = cell.getStringCellValue();
                            if (tagName == null) {
                                break;
                            }
                            tagName = tagName.trim();
                            for (String keyTag : mapTags.keySet()) {
                                if (mapTags.get(keyTag).equals(tagName)) {
                                    mapColIndex.put(keyTag, cell.getColumnIndex());
                                }
                            }
                        }
                        if (!mapColIndex.keySet().isEmpty()) {
                            rowValidData = row.getRowNum();
                            if (mapColIndex.keySet().size() != 3) {
                                break;
                            }
                        }
                    } else {
                        System.out.println(index.toString());

                        Cell cellFechaHora = row.getCell(1);
                        Cell cellFlujo = row.getCell(mapColIndex.get("FLUJO"));
                        Cell cellL1 = row.getCell(mapColIndex.get("V1"));
                        Cell cellL2 = row.getCell(mapColIndex.get("V2"));

                        Calendar calendarDateValue = Calendar.getInstance();
                        calendarDateValue.setTime(cellFechaHora.getDateCellValue());
                        if (!cellFlujo.getCellType().equals(CellType.NUMERIC) || !cellL1.getCellType().equals(CellType.NUMERIC) || !cellL2.getCellType().equals(CellType.NUMERIC)) {
                            continue;
                        }
                        Double flujo = cellFlujo.getNumericCellValue();
                        Double l1 = cellL1.getNumericCellValue();
                        Double l2 = cellL2.getNumericCellValue();

                        if (flujo == null || l1 == null || l2 == null) {
                            continue;
                        }

                        Double v1 = FormulasMath.getVolCBE(l1);
                        Double v2 = FormulasMath.getVolCBE(l2);

                        Double vTotal = v1 + v2;

                        mapData.put(calendarDateValue, new Double[]{flujo, vTotal, null, null});

                    }
                }
                PolynomialCurveFitter fitter = PolynomialCurveFitter.create(3);
                WeightedObservedPoints puntos = new WeightedObservedPoints();

                for (Calendar calendarAux : mapData.keySet()) {
                    puntos.add(calendarAux.getTimeInMillis(), mapData.get(calendarAux)[1]);
                }

                double[] coefs = fitter.fit(puntos.toList());
                UnivariateFunction polynomialFunctionDeerivada = (new PolynomialFunction(coefs)).derivative();
                
                
                for (Calendar calendarAux : mapData.keySet()) {                    
                    mapData.get(calendarAux)[2] = polynomialFunctionDeerivada.value(calendarAux.getTimeInMillis());
                    mapData.get(calendarAux)[3] = mapData.get(calendarAux)[0] + mapData.get(calendarAux)[2];
                }
                
                String a = "";

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void procerssFileTemp02() throws Exception {

        String fullPath = this.getEnvironment().getProperty("path.location.import_files");
        File filePathImportFilesDirectory = new File(fullPath);

        File[] arrFiles = filePathImportFilesDirectory.listFiles();

        for (Integer indexFile = 0; indexFile < arrFiles.length; indexFile++) {
            System.out.println("PROCESANDO ARCHIVO: " + arrFiles[indexFile].getName());
            try {
                Workbook workbook = new XSSFWorkbook(arrFiles[indexFile]);
                Sheet sheet = workbook.getSheetAt(0);

                // Iterate through each row
                List<String> lstTagsData = new ArrayList<>();
                Integer index = 0;
                for (Row row : sheet) {
                    Integer indexRowValidData = 0;
                    if (row.getRowNum() < 4) {
                        continue;
                    }
                    if (row.getRowNum() == 4) {
                        for (Cell cell : row) {
                            if (cell.getColumnIndex() < 2) {
                                continue;
                            }
                            if (cell.getCellType() == CellType.BLANK) {
                                break;
                            }
                            String tagName = cell.getStringCellValue();
                            if (tagName == null) {
                                break;
                            }
                            if (tagName.trim().equals("")) {
                                break;
                            }
                            lstTagsData.add(tagName.trim());
                            indexRowValidData++;
                        }
                    } else {
                        System.out.println(index.toString());
                        Calendar calendarDateValue = null;
                        for (Cell cell : row) {
                            if (cell.getColumnIndex() == 0) {
                                continue;
                            }
                            if (cell.getColumnIndex() == 1) {
                                calendarDateValue = Calendar.getInstance();
                                calendarDateValue.setTime(cell.getDateCellValue());
                            } else {
                                if (cell.getCellType().equals(CellType.NUMERIC)) {
                                    Double valueData = cell.getNumericCellValue();
                                    this.getSrvProcessManager().saveAnalogDataProdCbeCassandraFromExcelData(lstTagsData.get(cell.getColumnIndex() - 2), valueData, "", calendarDateValue);
                                }
                            }
                        }
                    }
                }
                arrFiles[indexFile].delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void procerssFileTemp01(String locationFile) throws Exception {
        try {
            FileInputStream fis = new FileInputStream(locationFile);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            // Iterate through each row
            List<String> lstLabels = new ArrayList<>();
            Integer index = 0;
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    for (Cell cell : row) {
                        lstLabels.add(cell.getStringCellValue());
                    }
                } else {
                    System.out.println(index.toString());
                    index++;
                    String tagData = null;
                    Calendar calendarDate = null;
                    String labelData = null;
                    Double valueData = null;
                    for (Cell cell : row) {
                        switch (cell.getColumnIndex()) {
                            case 0:
                                if (cell.getCellType().equals(CellType.NUMERIC)) {
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        calendarDate = Calendar.getInstance();
                                        calendarDate.setTime(cell.getDateCellValue());
                                    }
                                }
                                break;
                            case 2:
                                if (cell.getCellType().equals(CellType.STRING)) {
                                    tagData = cell.getStringCellValue();
                                }
                                break;
                            default:
                                if (cell.getCellType().equals(CellType.NUMERIC)) {
                                    labelData = lstLabels.get(cell.getColumnIndex());
                                    valueData = cell.getNumericCellValue();
                                    if (tagData != null && calendarDate != null && labelData != null) {
                                        System.out.println("****************DATA****************");
                                        System.out.println("tagData: " + tagData);
                                        System.out.println("calendarDate: " + GeneralsEjb.getDesFechaDiaMesAnioHorasSqlFormat(calendarDate));
                                        System.out.println("labelData: " + labelData);
                                        System.out.println("valueData: " + valueData);
                                        System.out.println("----->GUARDANDO");
                                        this.getSrvProcessManager().saveAnalogDataProdCbeCassandraFromExcelData((tagData + ":" + labelData), valueData, labelData, calendarDate);
                                        System.out.println("--------------->END GUARDANDO");
                                        System.out.println("****************END_DATA****************");
                                    }
                                }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
