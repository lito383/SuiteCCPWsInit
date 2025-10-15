package co.com.ecopetrol.ws.SuiteCCPInit.controllers;

import co.com.ecopetrol.ws.SuiteCCPInit.services.interfaces.SrvProcessDocs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author STC
 */
@RestController
@RequestMapping("/importFormatsData")
public class ImportFormatsData {

    @Autowired
    private SrvProcessDocs srvProcessDocs;

    public SrvProcessDocs getSrvProcessDocs() {
        return srvProcessDocs;
    }

    public void setSrvProcessDocs(SrvProcessDocs srvProcessDocs) {
        this.srvProcessDocs = srvProcessDocs;
    }

    @GetMapping(path = "importTemp01s")
    public String importTemp01s(@RequestParam("fullPathFile") String fullPathFile) {
        try {
            this.getSrvProcessDocs().procerssFileTemp01(fullPathFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "SUCCESS";
    }

    @GetMapping(path = "importTemp02s")
    public String importTemp02s() {
        try {
            this.getSrvProcessDocs().procerssFileTemp02();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "SUCCESS";
    }
    
     @GetMapping(path = "importTemp03s")
    public String importTemp03s() {
        try {
            this.getSrvProcessDocs().processFileTemp03();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "SUCCESS";
    }

}
