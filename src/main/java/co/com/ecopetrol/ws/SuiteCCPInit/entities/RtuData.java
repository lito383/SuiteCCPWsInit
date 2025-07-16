package co.com.ecopetrol.ws.SuiteCCPInit.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author STC
 */
@Entity(name = "RtuData")
@Table(name = "rtu_data")
public class RtuData implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "ip")
    private String ip;
    @Column(name = "port_modbus")
    private Integer portModbus;
    @Column(name = "est_borrado")
    private Integer estBorrado;
    @Column(name = "direccion_modbus")
    private Integer direccionModbus;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPortModbus() {
        return portModbus;
    }

    public void setPortModbus(Integer portModbus) {
        this.portModbus = portModbus;
    }

    public Integer getEstBorrado() {
        return estBorrado;
    }

    public void setEstBorrado(Integer estBorrado) {
        this.estBorrado = estBorrado;
    }

    public Integer getDireccionModbus() {
        return direccionModbus;
    }

    public void setDireccionModbus(Integer direccionModbus) {
        this.direccionModbus = direccionModbus;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RtuData)) {
            return false;
        }
        RtuData other = (RtuData) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.ecopetrol.suiteccp.ws.entities.RtuData[ id=" + id + " ]";
    }

}
