
package co.com.ecopetrol.ws.SuiteCCPInit.repositories;

import co.com.ecopetrol.ws.SuiteCCPInit.entities.ScanGroupCalc;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author stell
 */
public interface ScanGroupCalcRepository extends JpaRepository<ScanGroupCalc, Long> {

    @Query("SELECT obj FROM ScanGroupCalc obj WHERE obj.activo = 1 AND obj.estBorrado = 0")
    public List<ScanGroupCalc> getLstScanGroupCalcActivate();

}
