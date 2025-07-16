package co.com.ecopetrol.ws.SuiteCCPInit.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author STC
 */
@Entity(name = "ScanGroupRtu")
@Table(name = "scan_group_rtu")
public class ScanGroupRtu implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @ManyToOne(targetEntity = RtuData.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rtu_data")
    private RtuData rtuData;
    @Column(name = "est_borrado")
    private Integer estBorrado;
    @Column(name = "min_pos_modbus_to_read")
    private Integer minPosModbusToRead;
    @Column(name = "max_pos_modbus_to_read")
    private Integer maxPosModbusToRead;
    @Column(name = "fecha_ultima_ejec")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaUltimaEjec;
    @Column(name = "activo")
    private Integer activo;
    @Column(name = "period_msec")
    private Integer periodMSec;

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

    public RtuData getRtuData() {
        return rtuData;
    }

    public void setRtuData(RtuData rtuData) {
        this.rtuData = rtuData;
    }

    public Integer getEstBorrado() {
        return estBorrado;
    }

    public void setEstBorrado(Integer estBorrado) {
        this.estBorrado = estBorrado;
    }

    public Integer getMinPosModbusToRead() {
        return minPosModbusToRead;
    }

    public void setMinPosModbusToRead(Integer minPosModbusToRead) {
        this.minPosModbusToRead = minPosModbusToRead;
    }

    public Integer getMaxPosModbusToRead() {
        return maxPosModbusToRead;
    }

    public void setMaxPosModbusToRead(Integer maxPosModbusToRead) {
        this.maxPosModbusToRead = maxPosModbusToRead;
    }

    public Date getFechaUltimaEjec() {
        return fechaUltimaEjec;
    }

    public void setFechaUltimaEjec(Date fechaUltimaEjec) {
        this.fechaUltimaEjec = fechaUltimaEjec;
    }

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public Integer getPeriodMSec() {
        return periodMSec;
    }

    public void setPeriodMSec(Integer periodMSec) {
        this.periodMSec = periodMSec;
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
        if (!(object instanceof ScanGroupRtu)) {
            return false;
        }
        ScanGroupRtu other = (ScanGroupRtu) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.ecopetrol.suiteccp.ws.entities.ScanGroupRtu[ id=" + id + " ]";
    }

}
