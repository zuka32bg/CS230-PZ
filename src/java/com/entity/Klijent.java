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
@Table(name = "klijent")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Klijent.findAll", query = "SELECT k FROM Klijent k")
    , @NamedQuery(name = "Klijent.findByIdKlijenta", query = "SELECT k FROM Klijent k WHERE k.idKlijenta = :idKlijenta")})
public class Klijent implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_KLIJENTA")
    private Integer idKlijenta;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "IME_KLIJENTA")
    private String imeKlijenta;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "ADRESA_KLIJENTA")
    private String adresaKlijenta;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "TELEFON_KLIJENTA")
    private String telefonKlijenta;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "STAROST")
    private String starost;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idKlijenta")
    private Collection<Rezervacija> rezervacijaCollection;

    public Klijent() {
    }

    public Klijent(Integer idKlijenta) {
        this.idKlijenta = idKlijenta;
    }

    public Klijent(Integer idKlijenta, String imeKlijenta, String adresaKlijenta, String telefonKlijenta, String starost) {
        this.idKlijenta = idKlijenta;
        this.imeKlijenta = imeKlijenta;
        this.adresaKlijenta = adresaKlijenta;
        this.telefonKlijenta = telefonKlijenta;
        this.starost = starost;
    }

    public Integer getIdKlijenta() {
        return idKlijenta;
    }

    public void setIdKlijenta(Integer idKlijenta) {
        this.idKlijenta = idKlijenta;
    }

    public String getImeKlijenta() {
        return imeKlijenta;
    }

    public void setImeKlijenta(String imeKlijenta) {
        this.imeKlijenta = imeKlijenta;
    }

    public String getAdresaKlijenta() {
        return adresaKlijenta;
    }

    public void setAdresaKlijenta(String adresaKlijenta) {
        this.adresaKlijenta = adresaKlijenta;
    }

    public String getTelefonKlijenta() {
        return telefonKlijenta;
    }

    public void setTelefonKlijenta(String telefonKlijenta) {
        this.telefonKlijenta = telefonKlijenta;
    }

    public String getStarost() {
        return starost;
    }

    public void setStarost(String starost) {
        this.starost = starost;
    }

    @XmlTransient
    public Collection<Rezervacija> getRezervacijaCollection() {
        return rezervacijaCollection;
    }

    public void setRezervacijaCollection(Collection<Rezervacija> rezervacijaCollection) {
        this.rezervacijaCollection = rezervacijaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idKlijenta != null ? idKlijenta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Klijent)) {
            return false;
        }
        Klijent other = (Klijent) object;
        if ((this.idKlijenta == null && other.idKlijenta != null) || (this.idKlijenta != null && !this.idKlijenta.equals(other.idKlijenta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return " " + imeKlijenta;
    }
    
}
