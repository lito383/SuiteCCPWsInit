package co.com.ecopetrol.ws.SuiteCCPInit.services.impl;

import co.com.ecopetrol.ws.SuiteCCPInit.commons.GeneralsEjb;
import co.com.ecopetrol.ws.SuiteCCPInit.entities.ScanGroupCalc;
import co.com.ecopetrol.ws.SuiteCCPInit.entities.ScanGroupEngItem;
import co.com.ecopetrol.ws.SuiteCCPInit.services.interfaces.SrvProcessManager;
import co.com.ecopetrol.ws.SuiteCCPInit.services.interfaces.SrvScanGroupCalc;
import co.com.ecopetrol.ws.SuiteCCPInit.services.interfaces.SrvTagsScada;
import co.com.ecopetrol.ws.SuiteCCPInit.timerServices.SrvExecuteTimerScanGroupCalc;
import co.com.ecopetrol.ws.SuiteCCPInit.timerServices.SrvTimerGetDataFromServices;
import co.com.ecopetrol.ws.SuiteCCPInit.timerServices.SrvTimerLodersData;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author STC
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SrvProcessManagerImpl implements SrvProcessManager {

    private Map<String, ScheduledThreadPoolExecutor> mapScheduledThreadPoolExecutorLoaders = null;
    private Map<String, Integer> mapPortsService = null;
    private Map<String, Double> mapValuesFromRTUScada = null;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutorData = null;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutorRefreshTagScadaCassandra = null;
    private Map<Long, ScheduledThreadPoolExecutor> mapScheduledThreadPoolExecutorExecuteScanGroupCalc = null;
    private List<String> lstTagsScadaFromCassandra = null;
    private SimpleDateFormat simpleDateFormat = null;
    private CqlSession session = null;

    @Autowired
    private ApplicationArguments args;
    @Autowired
    private Environment env;
    @Autowired
    private SrvTagsScada srvTagsScada;
    @Autowired
    private SrvScanGroupCalc srvScanGroupCalc;

    public SrvScanGroupCalc getSrvScanGroupCalc() {
        return srvScanGroupCalc;
    }

    public void setSrvScanGroupCalc(SrvScanGroupCalc srvScanGroupCalc) {
        this.srvScanGroupCalc = srvScanGroupCalc;
    }

    public SimpleDateFormat getSimpleDateFormat() {
        if (this.simpleDateFormat == null) {
            this.simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        }
        return simpleDateFormat;
    }

    public void setSimpleDateFormat(SimpleDateFormat simpleDateFormat) {
        this.simpleDateFormat = simpleDateFormat;
    }

    @Override
    public void saveAnalogDataProdCbeCassandraFromExcelData(String tagData, Double valueData, String labelData, Calendar calendarData) throws Exception {
        if (this.getSession().isClosed()) {
            this.buildSession();
        }
        Calendar calendarCurrent = Calendar.getInstance();
        calendarCurrent.setTime(calendarData.getTime());
        String desTime = this.getSimpleDateFormat().format(calendarCurrent.getTime());
        desTime += "-0000";
        if (!this.getSession().isClosed()) {
            String jql = "INSERT INTO ccp_data.data_prod_cbe(id, tag_data, time_stamp_local_data, value_data, label_data) VALUES(UUID(), '" + tagData + "', '" + desTime + "', " + valueData.toString() + ", '" + labelData + "')";
            //Logger logger = LoggerFactory.getLogger(SrvTimerGetEngData.class);
            //logger.info("QUERY CASSANDRA INSERT: " + jql);
            System.out.println("QUERY CASSANDRA: " + jql);
            this.getSession().execute(jql);
        }
    }

    @Override
    public Map<String, Double> getMapAvgValueTagListPiFromCassandraServerByPeriodo(List<String> lstTags, Calendar calendarStart, Calendar calendarEnd) throws Exception {

        Logger logger = LoggerFactory.getLogger(SrvProcessManagerImpl.class);
        Map<String, Double> mapRes = new HashMap<>();
        if (lstTags == null) {
            return mapRes;
        }
        if (lstTags.isEmpty()) {
            return mapRes;
        }

        Calendar calendarStartAux = Calendar.getInstance();
        calendarStartAux.setTime(calendarStart.getTime());

        Calendar calendarEndAux = Calendar.getInstance();
        calendarEndAux.setTime(calendarEnd.getTime());
        //System.out.println("START QUERY-CASSANDRA");
        try {
            String sql = "SELECT tag_data,  AVG(value_data) FROM ccp_data.analog WHERE tag_data IN (";
            Integer sizeList = lstTags.size();
            for (Integer i = 0; i < sizeList; i++) {
                sql += "'" + lstTags.get(i) + "'";
                if (i != (sizeList - 1)) {
                    sql += ",";
                }
            }
            sql += ")";
            sql += " AND time_stamp_local_data >= '" + GeneralsEjb.getDesFechaDiaMesAnioHorasSqlFormat(calendarStartAux) + "-0500' AND  time_stamp_local_data <= '" + GeneralsEjb.getDesFechaDiaMesAnioHorasSqlFormat(calendarEndAux) + "-0500' GROUP BY tag_data";

            ResultSet rs = this.getLstRowFromCassandra(sql);

            if (rs == null) {
                return mapRes;
            }
            String keyTag = null;
            Double valueData = null;
            List<Row> lstRows = rs.all();
            Integer index = 0;
            for (Row row : lstRows) {
                index++;
                if (row.isNull(0)) {
                    continue;
                }
                if (row.isNull(1)) {
                    continue;
                }

                keyTag = row.getString(0);

                valueData = row.getDouble(1);
                try {
                    mapRes.put(keyTag, valueData);
                } catch (Exception e) {
                    logger.info("ERROR: " + e.getMessage());
                    valueData = null;
                }
            }
            //logger.info("END PROCESSING-CASSANDRA");
        } catch (Exception e) {
            logger.info("ERROR01: " + e.getMessage());
            e.printStackTrace();
            return mapRes;
        }
        return mapRes;
    }

    @Override
    public void saveAnalogDataCassandra(String tagData, Double valueData) throws Exception {
        if (this.getSession().isClosed()) {
            this.buildSession();
        }
        Calendar calendarCurrent = Calendar.getInstance();
        String desTime = this.getSimpleDateFormat().format(calendarCurrent.getTime());
        desTime += "-0500";
        if (!this.getSession().isClosed()) {
            String jql = "INSERT INTO ccp_data.analog(id, tag_data, time_stamp_local_data, value_data) VALUES(UUID(), '" + tagData + "', '" + desTime + "', " + valueData.toString() + ")";
            //Logger logger = LoggerFactory.getLogger(SrvTimerGetEngData.class);
            //logger.info("QUERY CASSANDRA INSERT: " + jql);
            //System.out.println("QUERY CASSANDRA: " + jql);
            this.getSession().execute(jql);
        }
    }

    public SrvTagsScada getSrvTagsScada() {
        return srvTagsScada;
    }

    public void setSrvTagsScada(SrvTagsScada srvTagsScada) {
        this.srvTagsScada = srvTagsScada;
    }

    public Map<Long, ScheduledThreadPoolExecutor> getMapScheduledThreadPoolExecutorExecuteScanGroupCalc() {
        if (this.mapScheduledThreadPoolExecutorExecuteScanGroupCalc == null) {
            this.mapScheduledThreadPoolExecutorExecuteScanGroupCalc = new HashMap<>();
        }
        return this.mapScheduledThreadPoolExecutorExecuteScanGroupCalc;
    }

    public void setMapScheduledThreadPoolExecutorExecuteScanGroupCalc(Map<Long, ScheduledThreadPoolExecutor> mapScheduledThreadPoolExecutorExecuteScanGroupCalc) {
        this.mapScheduledThreadPoolExecutorExecuteScanGroupCalc = mapScheduledThreadPoolExecutorExecuteScanGroupCalc;
    }

    public ScheduledThreadPoolExecutor getScheduledThreadPoolExecutorRefreshTagScadaCassandra() {
        if (this.scheduledThreadPoolExecutorRefreshTagScadaCassandra == null) {
            this.scheduledThreadPoolExecutorRefreshTagScadaCassandra = new ScheduledThreadPoolExecutor(0);
        }
        return this.scheduledThreadPoolExecutorRefreshTagScadaCassandra;
    }

    public void setScheduledThreadPoolExecutorRefreshTagScadaCassandra(ScheduledThreadPoolExecutor scheduledThreadPoolExecutorRefreshTagScadaCassandra) {
        this.scheduledThreadPoolExecutorRefreshTagScadaCassandra = scheduledThreadPoolExecutorRefreshTagScadaCassandra;
    }

    @Override
    public List<String> getLstTagsScadaFromCassandraData() {
        return this.getLstTagsScadaFromCassandra();
    }

    public void addItemLstTagsScadaFromCassandraData(String tag) {
        if (!this.getLstTagsScadaFromCassandra().contains(tag)) {
            this.getLstTagsScadaFromCassandra().add(tag);
        }
    }

    public void setLstTagsScadaFromCassandraData(List<String> lstTagsScadaFromCassandra) {
        this.setLstTagsScadaFromCassandra(lstTagsScadaFromCassandra);
    }

    public List<String> getLstTagsScadaFromCassandra() {
        if (this.lstTagsScadaFromCassandra == null) {
            this.lstTagsScadaFromCassandra = new ArrayList<>();
        }
        return this.lstTagsScadaFromCassandra;
    }

    public void setLstTagsScadaFromCassandra(List<String> lstTagsScadaFromCassandra) {
        this.lstTagsScadaFromCassandra = lstTagsScadaFromCassandra;
    }

    public void buildSession() {

        String ipCassandraServer = "127.0.0.1";
        Integer portCassandraServer = 9042;
        String dataCenterNameCassandraServer = "datacenter1";

        Logger logger = LoggerFactory.getLogger(SrvProcessManagerImpl.class);
        String ipCluster1 = ipCassandraServer;

        CqlSession cqlSessionAux = CqlSession.builder()
                .addContactPoint(new InetSocketAddress(ipCassandraServer, portCassandraServer))
                .withLocalDatacenter(dataCenterNameCassandraServer)
                .build();
        try {
            this.setSession(cqlSessionAux);
        } catch (Exception e) {
        }
    }

    public CqlSession getSession() {
        if (this.session == null) {
            this.buildSession();
        }
        return session;
    }

    public void setSession(CqlSession session) {
        this.session = session;
    }

    public static String getDesFechaDiaMesAnioHorasSqlFormatDDMMYYYYHHMMSS(Calendar calendar) {
        String desFecha = GeneralsEjb.getDesFechaSqlFormatFromCalendar(calendar);
        String horas = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minutos = String.valueOf(calendar.get(Calendar.MINUTE));
        String segundos = String.valueOf(calendar.get(Calendar.SECOND));
        while (horas.length() < 2) {
            horas = "0" + horas;
        }
        while (minutos.length() < 2) {
            minutos = "0" + minutos;
        }
        while (segundos.length() < 2) {
            segundos = "0" + segundos;
        }
        desFecha += " " + horas + ":" + minutos + ":" + segundos;
        return desFecha;
    }

    private ResultSet getLstRowFromCassandra(String sql) throws Exception {
        if (this.getSession().isClosed()) {
            this.buildSession();
        }
        if (!this.getSession().isClosed()) {
            return this.getSession().execute(sql);
        }
        return null;
    }

    @Override
    public SortedMap<String, SortedMap<Calendar, Double>> getMapAvgValueTagListPiFromCassandraServerV3(List<String> lstTags, Calendar calendarStart, Calendar calendarEnd) throws Exception {

        Logger logger = LoggerFactory.getLogger(SrvProcessManagerImpl.class);
        SortedMap<String, SortedMap<Calendar, Double>> mapRes = new TreeMap<>();
        if (lstTags == null) {
            return mapRes;
        }
        if (lstTags.isEmpty()) {
            return mapRes;
        }

        Calendar calendarStartAux = Calendar.getInstance();
        calendarStartAux.setTime(calendarStart.getTime());

        Calendar calendarEndAux = Calendar.getInstance();
        calendarEndAux.setTime(calendarEnd.getTime());
        //System.out.println("START QUERY-CASSANDRA");
        try {
            String sql = "SELECT tag_data, time_stamp_local_data, value_data FROM ccp_data.analog WHERE tag_data IN (";
            Integer sizeList = lstTags.size();
            for (Integer i = 0; i < sizeList; i++) {
                mapRes.put(lstTags.get(i), new TreeMap<>());
                sql += "'" + lstTags.get(i) + "'";
                if (i != (sizeList - 1)) {
                    sql += ",";
                }
            }
            sql += ")";
            sql += " AND time_stamp_local_data >= '" + GeneralsEjb.getDesFechaDiaMesAnioHorasSqlFormat(calendarStartAux) + "-0500' AND  time_stamp_local_data <= '" + GeneralsEjb.getDesFechaDiaMesAnioHorasSqlFormat(calendarEndAux) + "-0500' ORDER BY time_stamp_local_data ASC";
            //System.out.println("SQL EXEC CASSANDRA: " + sql);
            //logger.info("SQL EXEC CASSANDRA: " + sql);
            ResultSet rs = this.getLstRowFromCassandra(sql);
            //logger.info("----->END QUERY-CASSANDRA");
            //System.out.println("----->END QUERY-CASSANDRA");
            if (rs == null) {
                return mapRes;
            }
            String keyTag = null;
            Double valueData = null;
            java.util.Date dateTimeValue;
            List<Row> lstRows = rs.all();
            //logger.info("START PROCESSING-CASSANDRA");
            //logger.info("CNT REGS: " + lstRows.size());
            Integer index = 0;
            for (Row row : lstRows) {
                //logger.info("INDEX: " + index.toString());
                index++;
                //logger.info("DEBUG01");
                if (row.isNull(2)) {
                    continue;
                }
                //logger.info("DEBUG02");
                if (row.isNull(1)) {
                    continue;
                }
                //logger.info("DEBUG03");
                keyTag = row.getString(0);
                //logger.info("DEBUG04: " + keyTag);
                Instant instant = row.getInstant(1);
                instant.atZone(ZoneId.of("America/Bogota"));
                //logger.info("DATASTR: " + row.getObject(1).getClass().getName());
                //logger.info("DEBUG05");                
                dateTimeValue = Date.from(instant);
                valueData = row.getDouble(2);
                try {
                    Calendar calendarAux = Calendar.getInstance(TimeZone.getTimeZone("America/Bogota"));
                    calendarAux.setTime(dateTimeValue);
                    //logger.info("DATASTR_01: " + dateTimeValue.toString());
                    //calendarAux.setTimeZone(TimeZone.getTimeZone("America/Bogota"));
                    //calendarAux.add(Calendar.HOUR_OF_DAY, -5);
                    //String keyTime = GeneralsEjb.getDesFechaDiaMesAnioHorasSqlFormatDDMMYYYYHHMMSSQQQQ(calendarAux);
                    //logger.info("DATASTR_01: " + calendarAux.toString());
                    mapRes.get(keyTag).put(calendarAux, valueData);
                } catch (Exception e) {
                    logger.info("ERROR: " + e.getMessage());
                    valueData = null;
                }
            }
            //logger.info("END PROCESSING-CASSANDRA");
        } catch (Exception e) {
            logger.info("ERROR01: " + e.getMessage());
            e.printStackTrace();
            return mapRes;
        }
        return mapRes;
    }

    public ScheduledThreadPoolExecutor getScheduledThreadPoolExecutorData() {
        if (this.scheduledThreadPoolExecutorData == null) {
            this.scheduledThreadPoolExecutorData = new ScheduledThreadPoolExecutor(1);
        }
        return this.scheduledThreadPoolExecutorData;
    }

    public void setScheduledThreadPoolExecutorData(ScheduledThreadPoolExecutor scheduledThreadPoolExecutorData) {
        this.scheduledThreadPoolExecutorData = scheduledThreadPoolExecutorData;
    }

    @Override
    public void addAllMapSetValues(Map<String, Double> mapValues) {
        this.getMapValuesFromRTUScada().putAll(mapValues);
    }

    @Override
    public void putMapSetValues(String tag, Double valueData) {
        this.getMapValuesFromRTUScada().put(tag, valueData);
    }

    @Override
    public Map<String, Double> getMapValuesFromRTUScadaData() {
        return this.getMapValuesFromRTUScada();
    }

    public Map<String, Double> getMapValuesFromRTUScada() {
        if (this.mapValuesFromRTUScada == null) {
            this.mapValuesFromRTUScada = new HashMap<>();
        }
        return this.mapValuesFromRTUScada;
    }

    public void setMapValuesFromRTUScada(Map<String, Double> mapValuesFromRTUScada) {
        this.mapValuesFromRTUScada = mapValuesFromRTUScada;
    }

    public SrvProcessManager getSrvProcessManager() {
        return this;
    }

    public ApplicationArguments getArgs() {
        return args;
    }

    public void setArgs(ApplicationArguments args) {
        this.args = args;
    }

    public Environment getEnv() {
        return env;
    }

    public void setEnv(Environment env) {
        this.env = env;
    }

    public Map<String, Integer> getMapPortsServiceData() {
        return this.getMapPortsService();
    }

    public void setMapPortsServiceData(Map<String, Integer> mapPortsService) {
        this.setMapPortsService(mapPortsService);
    }

    public Map<String, Integer> getMapPortsService() {
        if (this.mapPortsService == null) {
            this.mapPortsService = new HashMap<>();
        }
        return mapPortsService;
    }

    public void setMapPortsService(Map<String, Integer> mapPortsService) {
        this.mapPortsService = mapPortsService;
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
            if (this.getMapPortsService().containsKey(sgName)) {
                Integer portData = this.getMapPortsService().get(sgName);
                if (portData != null) {
                    String uri = "http://localhost:" + portData.toString() + "/closeProject";
                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.execute(uri, HttpMethod.GET, null, null);
                }
            }
            this.getMapScheduledThreadPoolExecutorLoaders().get(sgName).shutdownNow();
        }
    }

    private void initServiceClient(List<String> lstSgNames) {
        try {
            Thread runnableProcess = new Thread() {
                @Override
                public void run() {
                    try {
                        Integer portSelected = 8090;
                        Process process = null;
                        for (String sgName : lstSgNames) {
                            portSelected++;
                            //Map<String, Integer> mapData = getSrvProcessManager().getMapPortsServiceData();

                            System.out.println("DATATATATA --- > " + sgName + "----> SUBIENDO CON PUERTO:      " + portSelected);
                            //System.out.println("D1: " + sgName);
                            String pathFileJar = getEnv().getProperty("path.jarfiles");
                            System.out.println("DATA: " + pathFileJar);
                            String pathJavaBin = getEnv().getProperty("path.javaBin");
                            //System.out.println("D2: " + port.toString());
                            //System.out.println("D3");
                            //Runtime.getRuntime().exe
                            String cmdLine = pathJavaBin; //+ " -jar " + pathFileJar + " " + sgName + " --server.port=" + port.toString();                        
                            ProcessBuilder processBuilder = new ProcessBuilder(new String[]{pathJavaBin, "-jar", pathFileJar, sgName, "--server.port=" + portSelected.toString()});
                            //processBuilder.command();
                            process = processBuilder.start();

                            InputStream is = process.getInputStream();
                            InputStreamReader isr = new InputStreamReader(is);
                            BufferedReader br = new BufferedReader(isr);
                            String line;
                            StringBuilder strData = new StringBuilder();
                            Boolean puertoAgregado = Boolean.FALSE;
                            Integer count = 0;
                            getSrvProcessManager().getMapPortsServiceData().put(sgName, portSelected);
//                            while ((line = br.readLine()) != null) {
//                                count++;
//                                System.out.println(line);
//                                strData.append(line);
//                                if (line.contains("process running for") && puertoAgregado.equals(Boolean.FALSE)) {
//                                    System.out.println("Init Data para el port: " + portSelected.toString());
//                                    if (portSelected != null) {
//                                        getSrvProcessManager().getMapPortsServiceData().put(sgName, portSelected);
//                                        puertoAgregado = Boolean.TRUE;
//                                        getSrvProcessManager().initSchedulerLoader(sgName, portSelected);
//                                        break;
//                                    }
//                                }
//                                if (count >= 1000) {
//                                    break;
//                                }
//                            }
//                            if (process != null) {
//                                process.wait();
//                            }
                        }
                    } catch (Exception e) {
                    }
                }
            };
            runnableProcess.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initServiceClient(String sgName) {
        try {
            Thread runnableProcess = new Thread() {
                @Override
                public void run() {
                    try {

                        Map<String, Integer> mapData = getSrvProcessManager().getMapPortsServiceData();
                        Integer portSelected = 8090;
                        while (mapData.containsValue(portSelected)) {
                            portSelected++;
                        }
                        //System.out.println("D1: " + sgName);
                        String pathFileJar = getEnv().getProperty("path.jarfiles");
                        System.out.println("DATA: " + pathFileJar);
                        String pathJavaBin = getEnv().getProperty("path.javaBin");
                        //System.out.println("D2: " + port.toString());
                        //System.out.println("D3");
                        //Runtime.getRuntime().exe
                        String cmdLine = pathJavaBin; //+ " -jar " + pathFileJar + " " + sgName + " --server.port=" + port.toString();                        
                        ProcessBuilder processBuilder = new ProcessBuilder(new String[]{pathJavaBin, "-jar", pathFileJar, sgName, "--server.port=" + portSelected.toString()});
                        //processBuilder.command();
                        Process process = processBuilder.start();

                        InputStream is = process.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr);
                        String line;
                        StringBuilder strData = new StringBuilder();
                        while ((line = br.readLine()) != null) {
                            System.out.println(line);
                            strData.append(line);
                        }
                        if (strData.toString().contains("process running for")) {
                            System.out.println("Init Data para el port: " + portSelected.toString());
                            getSrvProcessManager().getMapPortsServiceData().put(sgName, portSelected);
                            if (portSelected != null) {
                                getSrvProcessManager().initSchedulerLoader(sgName, portSelected);
                            }
                        } else if (strData.toString().contains("was already in use")) {

                        }
                        process.wait();
                    } catch (Exception e) {
                    }
                }
            };
            runnableProcess.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initScanGroupCalc() {
        List<ScanGroupCalc> lstScanGroupCalcs = this.getSrvScanGroupCalc().getLstScanGroupCalcActivate();
        Logger logger = LoggerFactory.getLogger(SrvProcessManagerImpl.class);
        for (ScanGroupCalc scanGroupCalc : lstScanGroupCalcs) {
            logger.info("STARTING SCANGROUP CALC: " + scanGroupCalc.getNombre());
            ScheduledThreadPoolExecutor scanScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(0);
            logger.info("AGREGAR TAG DATA: " + scanGroupCalc.getTagOut());
            this.addItemLstTagsScadaFromCassandraData(scanGroupCalc.getTagOut());
            scanScheduledThreadPoolExecutor.scheduleWithFixedDelay(new SrvExecuteTimerScanGroupCalc(this, scanGroupCalc), 60, 2, TimeUnit.SECONDS);
            this.getMapScheduledThreadPoolExecutorExecuteScanGroupCalc().put(scanGroupCalc.getId(), scanScheduledThreadPoolExecutor);
        }
    }

    @PostConstruct
    public void initServices() {

        //this.buildSessionSparkSession();
        String[] args = this.getArgs().getSourceArgs();
        //System.out.println("SIZE_DATA: " + args.length);
        if (args == null) {
            return;
        }
        if (args.length == 0) {
            return;
        }
        List<String> lstSgNames = new ArrayList<>();
        for (Integer index = 1; index < args.length; index++) {
            lstSgNames.add(args[index].trim());
        }
        this.initServiceClient(lstSgNames);

        this.getScheduledThreadPoolExecutorData().scheduleWithFixedDelay(new SrvTimerGetDataFromServices(this.getSrvProcessManager()), 4, 1, TimeUnit.SECONDS);

        this.initScanGroupCalc();

        this.getScheduledThreadPoolExecutorRefreshTagScadaCassandra().scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                List<String> lstTagsScadaFromScada = new ArrayList<>();
                try {
                    Map<Long, List<ScanGroupEngItem>> mapScanGroupEngItem = getSrvTagsScada().getMapScanGroupEngItemByIdScanGroupEng();
                    for (Long idScanGroupItem : mapScanGroupEngItem.keySet()) {
                        List<ScanGroupEngItem> lstGroupEngItems = mapScanGroupEngItem.get(idScanGroupItem);
                        if (lstGroupEngItems != null) {
                            for (ScanGroupEngItem scanGroupEngItem : lstGroupEngItems) {
                                if (!lstTagsScadaFromScada.contains(scanGroupEngItem.getTagRef())) {
                                    lstTagsScadaFromScada.add(scanGroupEngItem.getTagRef());
                                }
                            }
                        }
                    }
                    for (String tagRef : lstTagsScadaFromScada) {
                        addItemLstTagsScadaFromCassandraData(tagRef);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1, TimeUnit.HOURS);
    }

    @PreDestroy

    public void stopServices() {
        for (String sgName : this.getMapPortsService().keySet()) {
            this.stopSchedulerLoader(sgName);
        }
    }
}
