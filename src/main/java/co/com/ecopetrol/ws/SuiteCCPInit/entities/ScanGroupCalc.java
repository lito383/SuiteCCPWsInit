/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
@Table(name = "scan_group_calc")
@Entity(name = "ScanGroupCalc")
public class ScanGroupCalc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "ref_operation")
    private String refOperation;
    @Column(name = "lst_tags")
    private String lstTags;
    @Column(name = "tag_out")
    private String tagOut;
    @Column(name = "activo")
    private Integer activo;
    @Column(name = "est_borrado")
    private Integer estBorrado;

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

    public String getRefOperation() {
        return refOperation;
    }

    public void setRefOperation(String refOperation) {
        this.refOperation = refOperation;
    }

    public String getLstTags() {
        return lstTags;
    }

    public void setLstTags(String lstTags) {
        this.lstTags = lstTags;
    }

    public String getTagOut() {
        return tagOut;
    }

    public void setTagOut(String tagOut) {
        this.tagOut = tagOut;
    }

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public Integer getEstBorrado() {
        return estBorrado;
    }

    public void setEstBorrado(Integer estBorrado) {
        this.estBorrado = estBorrado;
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
        if (!(object instanceof ScanGroupCalc)) {
            return false;
        }
        ScanGroupCalc other = (ScanGroupCalc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.ecopetrol.ws.SuiteCCPInit.entities.ScanGroupCalc[ id=" + id + " ]";
    }

}
