package co.com.ecopetrol.ws.SuiteCCPInit.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author STC
 */
@Entity(name = "ModelRefBin")
@Table(name = "model_ref_bin")
public class ModelRefBin implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "csv_training")
    private byte[] csvTraining;
    @Column(name = "model_bin")
    private byte[] modelBin;
    @Column(name = "ref_model")
    private String refModel;
    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Column(name = "description")
    private String description;

    public byte[] getCsvTraining() {
        return csvTraining;
    }

    public void setCsvTraining(byte[] csvTraining) {
        this.csvTraining = csvTraining;
    }

    public byte[] getModelBin() {
        return modelBin;
    }

    public void setModelBin(byte[] modelBin) {
        this.modelBin = modelBin;
    }

    public String getRefModel() {
        return refModel;
    }

    public void setRefModel(String refModel) {
        this.refModel = refModel;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(object instanceof ModelRefBin)) {
            return false;
        }
        ModelRefBin other = (ModelRefBin) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.ecopetrol.ws.SuiteCCPInit.entities.ModelRefBin[ id=" + id + " ]";
    }

}
