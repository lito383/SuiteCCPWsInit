package co.com.ecopetrol.ws.SuiteCCPInit.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author STC
 */
@Entity(name = "ScanGroupEng")
public class ScanGroupEng implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(targetEntity = ScanGroupRtu.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_scan_group_rtu")
    private ScanGroupRtu scanGroupRtu;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "period_msec")
    private Integer periodMSec;
    @Column(name = "est_borrado")
    private Integer estBorrado;
    @Column(name = "activo")
    private Integer activo;

    @Transient
    private List<ScanGroupEngItem> lstGroupEngItems = null;

    public List<ScanGroupEngItem> getLstGroupEngItems() {
        if (this.lstGroupEngItems == null) {
            this.lstGroupEngItems = new ArrayList<>();
        }
        return this.lstGroupEngItems;
    }

    public void setLstGroupEngItems(List<ScanGroupEngItem> lstGroupEngItems) {
        this.lstGroupEngItems = lstGroupEngItems;
    }

    public ScanGroupRtu getScanGroupRtu() {
        return scanGroupRtu;
    }

    public void setScanGroupRtu(ScanGroupRtu scanGroupRtu) {
        this.scanGroupRtu = scanGroupRtu;
    }

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

    public Integer getPeriodMSec() {
        return periodMSec;
    }

    public void setPeriodMSec(Integer periodMSec) {
        this.periodMSec = periodMSec;
    }

    public Integer getEstBorrado() {
        return estBorrado;
    }

    public void setEstBorrado(Integer estBorrado) {
        this.estBorrado = estBorrado;
    }

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
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
        if (!(object instanceof ScanGroupEng)) {
            return false;
        }
        ScanGroupEng other = (ScanGroupEng) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.ecopetrol.suiteccp.ws.entities.ScanGroupEng[ id=" + id + " ]";
    }

}
