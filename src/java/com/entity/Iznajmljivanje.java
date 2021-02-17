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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Uros Zukancic
 */
@Entity
@Table(name = "iznajmljivanje")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Iznajmljivanje.findAll", query = "SELECT i FROM Iznajmljivanje i")
    , @NamedQuery(name = "Iznajmljivanje.findByIdIznajmljivanja", query = "SELECT i FROM Iznajmljivanje i WHERE i.idIznajmljivanja = :idIznajmljivanja")
    , @NamedQuery(name = "Iznajmljivanje.findByUkupnaCena", query = "SELECT i FROM Iznajmljivanje i WHERE i.ukupnaCena = :ukupnaCena")})
public class Iznajmljivanje implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_IZNAJMLJIVANJA")
    private Integer idIznajmljivanja;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UKUPNA_CENA")
    private long ukupnaCena;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idIznajmljivanja")
    private Collection<Rezervacija> rezervacijaCollection;
    @JoinColumn(name = "ID_OSIGURANJA", referencedColumnName = "ID_OSIGURANJA")
    @ManyToOne(optional = false)
    private Osiguranje idOsiguranja;
    @JoinColumn(name = "ID_TIPAIZN", referencedColumnName = "ID_TIPAIZN")
    @ManyToOne(optional = false)
    private Tipiznajmljivanja idTipaizn;

    public Iznajmljivanje() {
    }

    public Iznajmljivanje(Integer idIznajmljivanja) {
        this.idIznajmljivanja = idIznajmljivanja;
    }

    public Iznajmljivanje(Integer idIznajmljivanja, long ukupnaCena) {
        this.idIznajmljivanja = idIznajmljivanja;
        this.ukupnaCena = ukupnaCena;
    }

    public Integer getIdIznajmljivanja() {
        return idIznajmljivanja;
    }

    public void setIdIznajmljivanja(Integer idIznajmljivanja) {
        this.idIznajmljivanja = idIznajmljivanja;
    }

    public long getUkupnaCena() {
        return ukupnaCena;
    }

    public void setUkupnaCena(long ukupnaCena) {
        this.ukupnaCena = ukupnaCena;
    }

    @XmlTransient
    public Collection<Rezervacija> getRezervacijaCollection() {
        return rezervacijaCollection;
    }

    public void setRezervacijaCollection(Collection<Rezervacija> rezervacijaCollection) {
        this.rezervacijaCollection = rezervacijaCollection;
    }

    public Osiguranje getIdOsiguranja() {
        return idOsiguranja;
    }

    public void setIdOsiguranja(Osiguranje idOsiguranja) {
        this.idOsiguranja = idOsiguranja;
    }

    public Tipiznajmljivanja getIdTipaizn() {
        return idTipaizn;
    }

    public void setIdTipaizn(Tipiznajmljivanja idTipaizn) {
        this.idTipaizn = idTipaizn;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idIznajmljivanja != null ? idIznajmljivanja.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Iznajmljivanje)) {
            return false;
        }
        Iznajmljivanje other = (Iznajmljivanje) object;
        if ((this.idIznajmljivanja == null && other.idIznajmljivanja != null) || (this.idIznajmljivanja != null && !this.idIznajmljivanja.equals(other.idIznajmljivanja))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return " " + idIznajmljivanja;
    }
    
}
