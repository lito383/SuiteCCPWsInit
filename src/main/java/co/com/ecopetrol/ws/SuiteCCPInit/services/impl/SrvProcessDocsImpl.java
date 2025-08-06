package co.com.ecopetrol.ws.SuiteCCPInit.services.impl;

import co.com.ecopetrol.ws.SuiteCCPInit.commons.GeneralsEjb;
import co.com.ecopetrol.ws.SuiteCCPInit.services.interfaces.SrvProcessDocs;
import co.com.ecopetrol.ws.SuiteCCPInit.services.interfaces.SrvProcessManager;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author STC
 */
@Service
public class SrvProcessDocsImpl implements SrvProcessDocs {

    @Autowired
    private SrvProcessManager srvProcessManager;

    public SrvProcessManager getSrvProcessManager() {
        return srvProcessManager;
    }

    public void setSrvProcessManager(SrvProcessManager srvProcessManager) {
        this.srvProcessManager = srvProcessManager;
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
