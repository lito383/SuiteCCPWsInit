
package co.com.ecopetrol.ws.SuiteCCPInit.repositories;

import co.com.ecopetrol.ws.SuiteCCPInit.entities.SystemParameter;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author STC
 */
public interface SystemParameterRepository extends JpaRepository<SystemParameter, Long> {

    @Query("SELECT obj FROM SystemParameter obj ORDER BY obj.id ASC")
    public List<SystemParameter> getLstAllSystemParameter();

}
