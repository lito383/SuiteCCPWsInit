package co.com.ecopetrol.ws.SuiteCCPInit.controllers;

import co.com.ecopetrol.ws.SuiteCCPInit.services.interfaces.SrvProcessManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author STC
 */
@RestController
@RequestMapping("/")
public class InitDataController {

    @Autowired
    private Environment env;
    @Autowired
    private SrvProcessManager srvProcessManager;

    public SrvProcessManager getSrvProcessManager() {
        return srvProcessManager;
    }

    public void setSrvProcessManager(SrvProcessManager srvProcessManager) {
        this.srvProcessManager = srvProcessManager;
    }

    public Environment getEnv() {
        return env;
    }

    public void setEnv(Environment env) {
        this.env = env;
    }

    @GetMapping(path = "stopDataSG")
    public String stopDataSG(@RequestParam("sgName") String sgName) {
        this.getSrvProcessManager().stopSchedulerLoader(sgName);
        return "";
    }

    @GetMapping(path = "initDataSG")
    public String initDataSG(@RequestParam("sgName") String sgName, @RequestParam("port") Integer port) {
//        try {
//            Thread runnableProcess = new Thread() {
//                @Override
//                public void run() {
//                    try {
//                        //System.out.println("D1: " + sgName);
//                        String pathFileJar = getEnv().getProperty("path.jarfiles");
//                        String pathJavaBin = getEnv().getProperty("path.javaBin");
//                        //System.out.println("D2: " + port.toString());
//                        //System.out.println("D3");
//                        //Runtime.getRuntime().exe
//                        String cmdLine = pathJavaBin; //+ " -jar " + pathFileJar + " " + sgName + " --server.port=" + port.toString();
//                        ProcessBuilder processBuilder = new ProcessBuilder(new String[]{pathJavaBin, "-jar", pathFileJar, sgName, "--server.port=" + port.toString()});
//                        //processBuilder.command();
//                        Process process = processBuilder.start();
//
//                        InputStream is = process.getInputStream();
//                        InputStreamReader isr = new InputStreamReader(is);
//                        BufferedReader br = new BufferedReader(isr);
//                        String line;
//                        while ((line = br.readLine()) != null) {
//                            System.out.println(line);
//                        }
//                        process.wait();
//                    } catch (Exception e) {
//                    }
//                }
//            };
//            runnableProcess.start();
//
////System.out.println(cmdLine);
//            //Process ps = Runtime.getRuntime().exec(new String[]{pathJavaBin + "java", "-jar", sgName, "--server.port=" + port.toString()});
//            // Process ps = Runtime.getRuntime().exec(cmdLine);
//            //System.out.println(ps.);
//            //ps.waitFor();
//            //            System.out.println("D5");
//            //            java.io.InputStream is = ps.getInputStream();
//            //            System.out.println("D6");
//            //            byte b[] = new byte[is.available()];
//            //            is.read(b, 0, b.length);
//            //            System.out.println(new String(b));
//            // System.out.println(url.getPath());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        this.getSrvProcessManager().initSchedulerLoader(sgName, port);
        return "";
    }

}
