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
@Entity(name = "DefModelRef")
@Table(name = "def_model_ref")
public class DefModelRef implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "h1")
    private String h1;
    @Column(name = "h2")
    private String h2;
    @Column(name = "h3")
    private String h3;
    @Column(name = "h4")
    private String h4;
    @Column(name = "res")
    private String res;
    @Column(name = "ref_model")
    private String refModel;
    @Column(name = "periodo_training")
    private Integer periodoTraining;
    @Column(name = "uom_training")
    private String uomTraining;
    @Column(name = "est_borrado")
    private Integer estBorrado;
    @Column(name = "delay_time")
    private Integer delayTime;

    public Integer getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Integer delayTime) {
        this.delayTime = delayTime;
    }

    public Integer getEstBorrado() {
        return estBorrado;
    }

    public void setEstBorrado(Integer estBorrado) {
        this.estBorrado = estBorrado;
    }

    public Integer getPeriodoTraining() {
        return periodoTraining;
    }

    public void setPeriodoTraining(Integer periodoTraining) {
        this.periodoTraining = periodoTraining;
    }

    public String getUomTraining() {
        return uomTraining;
    }

    public void setUomTraining(String uomTraining) {
        this.uomTraining = uomTraining;
    }

    public String getH1() {
        return h1;
    }

    public void setH1(String h1) {
        this.h1 = h1;
    }

    public String getH2() {
        return h2;
    }

    public void setH2(String h2) {
        this.h2 = h2;
    }

    public String getH3() {
        return h3;
    }

    public void setH3(String h3) {
        this.h3 = h3;
    }

    public String getH4() {
        return h4;
    }

    public void setH4(String h4) {
        this.h4 = h4;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String getRefModel() {
        return refModel;
    }

    public void setRefModel(String refModel) {
        this.refModel = refModel;
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
        if (!(object instanceof DefModelRef)) {
            return false;
        }
        DefModelRef other = (DefModelRef) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.ecopetrol.ws.SuiteCCPInit.entities.DefModelRef[ id=" + id + " ]";
    }

}
