package co.com.ecopetrol.ws.SuiteCCPInit.timerServices;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.springframework.core.env.Environment;

/**
 *
 * @author
 */
public class SrvTimerLodersData implements Runnable {

    private String sgName = null;
    private Integer port = null;
    private Environment env = null;

    public SrvTimerLodersData(String sgName, Integer port, Environment env) {
        this.sgName = sgName;
        this.port = port;
        this.env = env;
    }

    public Environment getEnv() {
        return env;
    }

    public void setEnv(Environment env) {
        this.env = env;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getSgName() {
        return sgName;
    }

    public void setSgName(String sgName) {
        this.sgName = sgName;
    }

    @Override
    public void run() {
        try {
            //System.out.println("D1: " + sgName);
            String pathFileJar = this.getEnv().getProperty("path.jarfiles");
            String pathJavaBin = this.getEnv().getProperty("path.javaBin");
            //System.out.println("D2: " + port.toString());
            //System.out.println("D3");
            //Runtime.getRuntime().exe
            String cmdLine = pathJavaBin; //+ " -jar " + pathFileJar + " " + sgName + " --server.port=" + port.toString();
            ProcessBuilder processBuilder = new ProcessBuilder(new String[]{pathJavaBin, "-jar", pathFileJar, sgName, "--server.port=" + port.toString()});
            //processBuilder.command();
            Process process = processBuilder.start();
            ProcessHandle processHandle = process.toHandle();
            processHandle.pid();

            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            //process.wait();
        } catch (Exception e) {
        }
    }

}
