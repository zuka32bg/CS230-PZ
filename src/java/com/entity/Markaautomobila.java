/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Uros Zukancic
 */
@Entity
@Table(name = "markaautomobila")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Markaautomobila.findAll", query = "SELECT m FROM Markaautomobila m")
    , @NamedQuery(name = "Markaautomobila.findByIdMarke", query = "SELECT m FROM Markaautomobila m WHERE m.idMarke = :idMarke")})
public class Markaautomobila implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_MARKE")
    private Integer idMarke;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "MARKA")
    private String marka;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idMarke")
    private Collection<Modelautomobila> modelautomobilaCollection;

    public Markaautomobila() {
    }

    public Markaautomobila(Integer idMarke) {
        this.idMarke = idMarke;
    }

    public Markaautomobila(Integer idMarke, String marka) {
        this.idMarke = idMarke;
        this.marka = marka;
    }

    public Integer getIdMarke() {
        return idMarke;
    }

    public void setIdMarke(Integer idMarke) {
        this.idMarke = idMarke;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    @XmlTransient
    public Collection<Modelautomobila> getModelautomobilaCollection() {
        return modelautomobilaCollection;
    }

    public void setModelautomobilaCollection(Collection<Modelautomobila> modelautomobilaCollection) {
        this.modelautomobilaCollection = modelautomobilaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMarke != null ? idMarke.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Markaautomobila)) {
            return false;
        }
        Markaautomobila other = (Markaautomobila) object;
        if ((this.idMarke == null && other.idMarke != null) || (this.idMarke != null && !this.idMarke.equals(other.idMarke))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return " " +marka;
    }
    
}
