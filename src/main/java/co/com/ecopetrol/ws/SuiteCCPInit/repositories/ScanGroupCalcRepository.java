/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */
package co.com.ecopetrol.ws.SuiteCCPInit.repositories;

import co.com.ecopetrol.ws.SuiteCCPInit.entities.ScanGroupCalc;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author stell
 */
public interface ScanGroupCalcRepository extends JpaRepository<ScanGroupCalc, Long> {

    @Query("SELECT obj FROM ScanGroupCalc obj WHERE obj.activo = 1 AND obj.estBorrado = 0")
    public List<ScanGroupCalc> getLstScanGroupCalcActivate();

}
