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
@Table(name = "osiguranje")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Osiguranje.findAll", query = "SELECT o FROM Osiguranje o")
    , @NamedQuery(name = "Osiguranje.findByIdOsiguranja", query = "SELECT o FROM Osiguranje o WHERE o.idOsiguranja = :idOsiguranja")
    , @NamedQuery(name = "Osiguranje.findByCenaOsiguranja", query = "SELECT o FROM Osiguranje o WHERE o.cenaOsiguranja = :cenaOsiguranja")})
public class Osiguranje implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_OSIGURANJA")
    private Integer idOsiguranja;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "TIP_OSIGURANJA")
    private String tipOsiguranja;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CENA_OSIGURANJA")
    private int cenaOsiguranja;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idOsiguranja")
    private Collection<Iznajmljivanje> iznajmljivanjeCollection;
    @JoinColumn(name = "ID_FIRME", referencedColumnName = "ID_FIRME")
    @ManyToOne(optional = false)
    private Firmaosiguranja idFirme;

    public Osiguranje() {
    }

    public Osiguranje(Integer idOsiguranja) {
        this.idOsiguranja = idOsiguranja;
    }

    public Osiguranje(Integer idOsiguranja, String tipOsiguranja, int cenaOsiguranja) {
        this.idOsiguranja = idOsiguranja;
        this.tipOsiguranja = tipOsiguranja;
        this.cenaOsiguranja = cenaOsiguranja;
    }

    public Integer getIdOsiguranja() {
        return idOsiguranja;
    }

    public void setIdOsiguranja(Integer idOsiguranja) {
        this.idOsiguranja = idOsiguranja;
    }

    public String getTipOsiguranja() {
        return tipOsiguranja;
    }

    public void setTipOsiguranja(String tipOsiguranja) {
        this.tipOsiguranja = tipOsiguranja;
    }

    public int getCenaOsiguranja() {
        return cenaOsiguranja;
    }

    public void setCenaOsiguranja(int cenaOsiguranja) {
        this.cenaOsiguranja = cenaOsiguranja;
    }

    @XmlTransient
    public Collection<Iznajmljivanje> getIznajmljivanjeCollection() {
        return iznajmljivanjeCollection;
    }

    public void setIznajmljivanjeCollection(Collection<Iznajmljivanje> iznajmljivanjeCollection) {
        this.iznajmljivanjeCollection = iznajmljivanjeCollection;
    }

    public Firmaosiguranja getIdFirme() {
        return idFirme;
    }

    public void setIdFirme(Firmaosiguranja idFirme) {
        this.idFirme = idFirme;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOsiguranja != null ? idOsiguranja.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Osiguranje)) {
            return false;
        }
        Osiguranje other = (Osiguranje) object;
        if ((this.idOsiguranja == null && other.idOsiguranja != null) || (this.idOsiguranja != null && !this.idOsiguranja.equals(other.idOsiguranja))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return  " "+ cenaOsiguranja +idFirme ;
    }
    
}
