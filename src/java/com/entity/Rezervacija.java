/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Uros Zukancic
 */
@Entity
@Table(name = "rezervacija")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rezervacija.findAll", query = "SELECT r FROM Rezervacija r")
    , @NamedQuery(name = "Rezervacija.findByIdRezervacije", query = "SELECT r FROM Rezervacija r WHERE r.idRezervacije = :idRezervacije")
    , @NamedQuery(name = "Rezervacija.findByDatumPocetka", query = "SELECT r FROM Rezervacija r WHERE r.datumPocetka = :datumPocetka")
    , @NamedQuery(name = "Rezervacija.findByDatunKraja", query = "SELECT r FROM Rezervacija r WHERE r.datunKraja = :datunKraja")})
public class Rezervacija implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_REZERVACIJE")
    private Integer idRezervacije;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATUM_POCETKA")
    @Temporal(TemporalType.DATE)
    private Date datumPocetka;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATUN_KRAJA")
    @Temporal(TemporalType.DATE)
    private Date datunKraja;
    @JoinColumn(name = "ID_KLIJENTA", referencedColumnName = "ID_KLIJENTA")
    @ManyToOne(optional = false)
    private Klijent idKlijenta;
    @JoinColumn(name = "ID_VOZILA", referencedColumnName = "ID_VOZILA")
    @ManyToOne(optional = false)
    private Vozilo idVozila;
    @JoinColumn(name = "ID_IZNAJMLJIVANJA", referencedColumnName = "ID_IZNAJMLJIVANJA")
    @ManyToOne(optional = false)
    private Iznajmljivanje idIznajmljivanja;

    public Rezervacija() {
    }

    public Rezervacija(Integer idRezervacije) {
        this.idRezervacije = idRezervacije;
    }

    public Rezervacija(Integer idRezervacije, Date datumPocetka, Date datunKraja) {
        this.idRezervacije = idRezervacije;
        this.datumPocetka = datumPocetka;
        this.datunKraja = datunKraja;
    }

    public Integer getIdRezervacije() {
        return idRezervacije;
    }

    public void setIdRezervacije(Integer idRezervacije) {
        this.idRezervacije = idRezervacije;
    }

    public Date getDatumPocetka() {
        return datumPocetka;
    }

    public void setDatumPocetka(Date datumPocetka) {
        this.datumPocetka = datumPocetka;
    }

    public Date getDatunKraja() {
        return datunKraja;
    }

    public void setDatunKraja(Date datunKraja) {
        this.datunKraja = datunKraja;
    }

    public Klijent getIdKlijenta() {
        return idKlijenta;
    }

    public void setIdKlijenta(Klijent idKlijenta) {
        this.idKlijenta = idKlijenta;
    }

    public Vozilo getIdVozila() {
        return idVozila;
    }

    public void setIdVozila(Vozilo idVozila) {
        this.idVozila = idVozila;
    }

    public Iznajmljivanje getIdIznajmljivanja() {
        return idIznajmljivanja;
    }

    public void setIdIznajmljivanja(Iznajmljivanje idIznajmljivanja) {
        this.idIznajmljivanja = idIznajmljivanja;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRezervacije != null ? idRezervacije.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rezervacija)) {
            return false;
        }
        Rezervacija other = (Rezervacija) object;
        if ((this.idRezervacije == null && other.idRezervacije != null) || (this.idRezervacije != null && !this.idRezervacije.equals(other.idRezervacije))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return " " +idRezervacije;
    }
    
}
