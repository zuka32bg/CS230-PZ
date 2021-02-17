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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
@Table(name = "modelautomobila")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Modelautomobila.findAll", query = "SELECT m FROM Modelautomobila m")
    , @NamedQuery(name = "Modelautomobila.findByIdModela", query = "SELECT m FROM Modelautomobila m WHERE m.idModela = :idModela")
    , @NamedQuery(name = "Modelautomobila.findBySnaga", query = "SELECT m FROM Modelautomobila m WHERE m.snaga = :snaga")})
public class Modelautomobila implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_MODELA")
    private Integer idModela;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "TIP_AUTA")
    private String tipAuta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SNAGA")
    private int snaga;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idModela")
    private Collection<Obelezje> obelezjeCollection;
    @JoinColumn(name = "ID_MARKE", referencedColumnName = "ID_MARKE")
    @ManyToOne(optional = false)
    private Markaautomobila idMarke;

    public Modelautomobila() {
    }

    public Modelautomobila(Integer idModela) {
        this.idModela = idModela;
    }

    public Modelautomobila(Integer idModela, String tipAuta, int snaga) {
        this.idModela = idModela;
        this.tipAuta = tipAuta;
        this.snaga = snaga;
    }

    public Integer getIdModela() {
        return idModela;
    }

    public void setIdModela(Integer idModela) {
        this.idModela = idModela;
    }

    public String getTipAuta() {
        return tipAuta;
    }

    public void setTipAuta(String tipAuta) {
        this.tipAuta = tipAuta;
    }

    public int getSnaga() {
        return snaga;
    }

    public void setSnaga(int snaga) {
        this.snaga = snaga;
    }

    @XmlTransient
    public Collection<Obelezje> getObelezjeCollection() {
        return obelezjeCollection;
    }

    public void setObelezjeCollection(Collection<Obelezje> obelezjeCollection) {
        this.obelezjeCollection = obelezjeCollection;
    }

    public Markaautomobila getIdMarke() {
        return idMarke;
    }

    public void setIdMarke(Markaautomobila idMarke) {
        this.idMarke = idMarke;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idModela != null ? idModela.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Modelautomobila)) {
            return false;
        }
        Modelautomobila other = (Modelautomobila) object;
        if ((this.idModela == null && other.idModela != null) || (this.idModela != null && !this.idModela.equals(other.idModela))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return " " +idModela;
    }
    
}
