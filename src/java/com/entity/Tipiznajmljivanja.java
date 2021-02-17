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
@Table(name = "tipiznajmljivanja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tipiznajmljivanja.findAll", query = "SELECT t FROM Tipiznajmljivanja t")
    , @NamedQuery(name = "Tipiznajmljivanja.findByIdTipaizn", query = "SELECT t FROM Tipiznajmljivanja t WHERE t.idTipaizn = :idTipaizn")
    , @NamedQuery(name = "Tipiznajmljivanja.findByCenaIznajmljivanja", query = "SELECT t FROM Tipiznajmljivanja t WHERE t.cenaIznajmljivanja = :cenaIznajmljivanja")
    , @NamedQuery(name = "Tipiznajmljivanja.findByMaxKilometraza", query = "SELECT t FROM Tipiznajmljivanja t WHERE t.maxKilometraza = :maxKilometraza")})
public class Tipiznajmljivanja implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_TIPAIZN")
    private Integer idTipaizn;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "TIP_IZNAJMLJIVANJA")
    private String tipIznajmljivanja;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CENA_IZNAJMLJIVANJA")
    private long cenaIznajmljivanja;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MAX_KILOMETRAZA")
    private float maxKilometraza;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipaizn")
    private Collection<Iznajmljivanje> iznajmljivanjeCollection;

    public Tipiznajmljivanja() {
    }

    public Tipiznajmljivanja(Integer idTipaizn) {
        this.idTipaizn = idTipaizn;
    }

    public Tipiznajmljivanja(Integer idTipaizn, String tipIznajmljivanja, long cenaIznajmljivanja, float maxKilometraza) {
        this.idTipaizn = idTipaizn;
        this.tipIznajmljivanja = tipIznajmljivanja;
        this.cenaIznajmljivanja = cenaIznajmljivanja;
        this.maxKilometraza = maxKilometraza;
    }

    public Integer getIdTipaizn() {
        return idTipaizn;
    }

    public void setIdTipaizn(Integer idTipaizn) {
        this.idTipaizn = idTipaizn;
    }

    public String getTipIznajmljivanja() {
        return tipIznajmljivanja;
    }

    public void setTipIznajmljivanja(String tipIznajmljivanja) {
        this.tipIznajmljivanja = tipIznajmljivanja;
    }

    public long getCenaIznajmljivanja() {
        return cenaIznajmljivanja;
    }

    public void setCenaIznajmljivanja(long cenaIznajmljivanja) {
        this.cenaIznajmljivanja = cenaIznajmljivanja;
    }

    public float getMaxKilometraza() {
        return maxKilometraza;
    }

    public void setMaxKilometraza(float maxKilometraza) {
        this.maxKilometraza = maxKilometraza;
    }

    @XmlTransient
    public Collection<Iznajmljivanje> getIznajmljivanjeCollection() {
        return iznajmljivanjeCollection;
    }

    public void setIznajmljivanjeCollection(Collection<Iznajmljivanje> iznajmljivanjeCollection) {
        this.iznajmljivanjeCollection = iznajmljivanjeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipaizn != null ? idTipaizn.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tipiznajmljivanja)) {
            return false;
        }
        Tipiznajmljivanja other = (Tipiznajmljivanja) object;
        if ((this.idTipaizn == null && other.idTipaizn != null) || (this.idTipaizn != null && !this.idTipaizn.equals(other.idTipaizn))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return " " +tipIznajmljivanja;
    }
    
}
