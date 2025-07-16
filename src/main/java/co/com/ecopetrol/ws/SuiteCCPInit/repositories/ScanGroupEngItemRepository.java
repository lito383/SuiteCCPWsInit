
package co.com.ecopetrol.ws.SuiteCCPInit.repositories;

import co.com.ecopetrol.ws.SuiteCCPInit.entities.ScanGroupEngItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author STC
 */
public interface ScanGroupEngItemRepository extends JpaRepository<ScanGroupEngItem, Long> {
    
    @Query("SELECT obj FROM ScanGroupEngItem obj WHERE obj.estBorrado = 0 AND obj.scanGroupEng.estBorrado = 0 AND obj.scanGroupEng.activo = ?1  ORDER BY obj.posModbus ASC")
    public List<ScanGroupEngItem> getLstScanGroupEngAndItems(Integer activo);
    
     @Query("SELECT obj FROM ScanGroupEngItem obj WHERE obj.scanGroupEng.id = ?1 AND obj.estBorrado = 0 AND obj.scanGroupEng.estBorrado = 0 AND obj.scanGroupEng.activo = ?1  ORDER BY obj.posModbus ASC")
    public List<ScanGroupEngItem> getLstScanGroupEngItemByIdScanGroupEng(Long idScanGroupEng);
   
    
}
