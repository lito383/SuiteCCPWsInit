package co.com.ecopetrol.ws.SuiteCCPInit.controllers;

import co.com.ecopetrol.ws.SuiteCCPInit.beans.BeanRequestCassandraData;
import co.com.ecopetrol.ws.SuiteCCPInit.services.interfaces.SrvProcessManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author STC
 */
@RestController
@RequestMapping("/getDataFromCassandra")
public class GetDataFromCassandraController {

    @Autowired
    private SrvProcessManager srvProcessManager;

    public SrvProcessManager getSrvProcessManager() {
        return srvProcessManager;
    }

    public void setSrvProcessManager(SrvProcessManager srvProcessManager) {
        this.srvProcessManager = srvProcessManager;
    }

    @GetMapping(path = "getMessage")
    public String getMessage() {
        return "Hola Mundo!";
    }

    @GetMapping(path = "getLstTagsScadaFromCassandra")
    public List<String> getLstTagsScadaFromCassandra() {
        return this.getSrvProcessManager().getLstTagsScadaFromCassandraData();
    }

    @PostMapping(path = "getMapAvgValueTagListPiFromCassandraServerByPeriodo")
    public Map<String, Double> getMapAvgValueTagListPiFromCassandraServerByPeriodo(@RequestBody() BeanRequestCassandraData beanRequestCassandraData) {
        Map<String, Double> mapRes = new HashMap<>();
        if (beanRequestCassandraData == null) {
            return mapRes;
        }
        String strLstTags = beanRequestCassandraData.getStrLstTags();
        String strCalendarStart = beanRequestCassandraData.getStrCalendarStart();
        String strCalendarEnd = beanRequestCassandraData.getStrCalendarEnd();
        if (strLstTags == null || strCalendarStart == null || strCalendarEnd == null || this.getSrvProcessManager() == null) {
            return mapRes;
        }
        if (strLstTags.trim().equals("") || strCalendarStart.trim().equals("") || strCalendarEnd.trim().equals("")) {
            return mapRes;
        }
        String[] arrLstTags = strLstTags.split(";");
        if (arrLstTags.length == 0) {
            return mapRes;
        }
        SimpleDateFormat simnDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendarStart = Calendar.getInstance();
        try {
            Date dateStart = simnDateFormat.parse(strCalendarStart);
            calendarStart.setTime(dateStart);
        } catch (ParseException e) {
            return mapRes;
        }
        Calendar calendarEnd = Calendar.getInstance();
        try {
            Date dateEnd = simnDateFormat.parse(strCalendarEnd);
            calendarEnd.setTime(dateEnd);
        } catch (ParseException e) {
            return mapRes;
        }
        try {
            return this.getSrvProcessManager().getMapAvgValueTagListPiFromCassandraServerByPeriodo(Arrays.asList(arrLstTags), calendarStart, calendarEnd);
        } catch (Exception e) {
            return mapRes;
        }
    }

    @PostMapping(path = "getMapAvgValueTagListPiFromCassandraServerV3")
    public SortedMap<String, SortedMap<Calendar, Double>> getMapAvgValueTagListPiFromCassandraServerV3(@RequestBody() BeanRequestCassandraData beanRequestCassandraData) {
        SortedMap<String, SortedMap<Calendar, Double>> mapRes = new TreeMap<>();
        if (beanRequestCassandraData == null) {
            return mapRes;
        }
        String strLstTags = beanRequestCassandraData.getStrLstTags();
        String strCalendarStart = beanRequestCassandraData.getStrCalendarStart();
        String strCalendarEnd = beanRequestCassandraData.getStrCalendarEnd();
        if (strLstTags == null || strCalendarStart == null || strCalendarEnd == null || this.getSrvProcessManager() == null) {
            return mapRes;
        }
        if (strLstTags.trim().equals("") || strCalendarStart.trim().equals("") || strCalendarEnd.trim().equals("")) {
            return mapRes;
        }
        String[] arrLstTags = strLstTags.split(";");
        if (arrLstTags.length == 0) {
            return mapRes;
        }
        SimpleDateFormat simnDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendarStart = Calendar.getInstance();
        try {
            Date dateStart = simnDateFormat.parse(strCalendarStart);
            calendarStart.setTime(dateStart);
        } catch (ParseException e) {
            return mapRes;
        }
        Calendar calendarEnd = Calendar.getInstance();
        try {
            Date dateEnd = simnDateFormat.parse(strCalendarEnd);
            calendarEnd.setTime(dateEnd);
        } catch (ParseException e) {
            return mapRes;
        }
        try {
            return this.getSrvProcessManager().getMapAvgValueTagListPiFromCassandraServerV3(Arrays.asList(arrLstTags), calendarStart, calendarEnd);
        } catch (Exception e) {
            return mapRes;
        }
    }

}
