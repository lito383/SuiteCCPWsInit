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
import java.io.Serializable;

/**
 *
 * @author STC
 */
@Entity(name = "ScanGroupEngItem")
@Table(name = "scan_group_eng_item")
public class ScanGroupEngItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(targetEntity = ScanGroupEng.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_scan_group_eng")
    private ScanGroupEng scanGroupEng;
    @Column(name = "pos_modbus")
    private Integer posModbus;
    @Column(name = "tag_ref")
    private String tagRef;
    @Column(name = "div_proc")
    private Integer divProc;
    @Column(name = "min_eng")
    private Double minEng;
    @Column(name = "max_eng")
    private Double maxEng;
    @Column(name = "min_raw")
    private Double minRaw;
    @Column(name = "max_raw")
    private Double maxRaw;
    @Column(name = "est_borrado")
    private Integer estBorrado;
    @Column(name = "type_register")
    private Integer typeRegister;

    public Double getMinEng() {
        return minEng;
    }

    public void setMinEng(Double minEng) {
        this.minEng = minEng;
    }

    public Double getMaxEng() {
        return maxEng;
    }

    public void setMaxEng(Double maxEng) {
        this.maxEng = maxEng;
    }

    public Double getMinRaw() {
        return minRaw;
    }

    public void setMinRaw(Double minRaw) {
        this.minRaw = minRaw;
    }

    public Double getMaxRaw() {
        return maxRaw;
    }

    public void setMaxRaw(Double maxRaw) {
        this.maxRaw = maxRaw;
    }

    public ScanGroupEng getScanGroupEng() {
        return scanGroupEng;
    }

    public void setScanGroupEng(ScanGroupEng scanGroupEng) {
        this.scanGroupEng = scanGroupEng;
    }

    public Integer getPosModbus() {
        return posModbus;
    }

    public void setPosModbus(Integer posModbus) {
        this.posModbus = posModbus;
    }

    public String getTagRef() {
        return tagRef;
    }

    public void setTagRef(String tagRef) {
        this.tagRef = tagRef;
    }

    public Integer getDivProc() {
        return divProc;
    }

    public void setDivProc(Integer divProc) {
        this.divProc = divProc;
    }

    public Integer getEstBorrado() {
        return estBorrado;
    }

    public void setEstBorrado(Integer estBorrado) {
        this.estBorrado = estBorrado;
    }

    public Integer getTypeRegister() {
        return typeRegister;
    }

    public void setTypeRegister(Integer typeRegister) {
        this.typeRegister = typeRegister;
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
        if (!(object instanceof ScanGroupEngItem)) {
            return false;
        }
        ScanGroupEngItem other = (ScanGroupEngItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.ecopetrol.suiteccp.ws.entities.ScanGroupEngItem[ id=" + id + " ]";
    }

}
