/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Uros Zukancic
 */
@Entity
@Table(name = "vozilo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Vozilo.findAll", query = "SELECT v FROM Vozilo v")
    , @NamedQuery(name = "Vozilo.findByIdVozila", query = "SELECT v FROM Vozilo v WHERE v.idVozila = :idVozila")
    , @NamedQuery(name = "Vozilo.findByDatumVracanja", query = "SELECT v FROM Vozilo v WHERE v.datumVracanja = :datumVracanja")
    , @NamedQuery(name = "Vozilo.findByKupovnaCena", query = "SELECT v FROM Vozilo v WHERE v.kupovnaCena = :kupovnaCena")
    , @NamedQuery(name = "Vozilo.findByKilometraza", query = "SELECT v FROM Vozilo v WHERE v.kilometraza = :kilometraza")
    , @NamedQuery(name = "Vozilo.findByPopravka", query = "SELECT v FROM Vozilo v WHERE v.popravka = :popravka")
    , @NamedQuery(name = "Vozilo.findByDatumPopravke", query = "SELECT v FROM Vozilo v WHERE v.datumPopravke = :datumPopravke")})
public class Vozilo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_VOZILA")
    private Integer idVozila;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATUM_VRACANJA")
    @Temporal(TemporalType.DATE)
    private Date datumVracanja;
    @Basic(optional = false)
    @NotNull
    @Column(name = "KUPOVNA_CENA")
    private float kupovnaCena;
    @Basic(optional = false)
    @NotNull
    @Column(name = "KILOMETRAZA")
    private float kilometraza;
    @Basic(optional = false)
    @NotNull
    @Column(name = "POPRAVKA")
    private boolean popravka;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATUM_POPRAVKE")
    @Temporal(TemporalType.DATE)
    private Date datumPopravke;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idVozila")
    private Collection<Rezervacija> rezervacijaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idVozila")
    private Collection<Obelezje> obelezjeCollection;

    public Vozilo() {
    }

    public Vozilo(Integer idVozila) {
        this.idVozila = idVozila;
    }

    public Vozilo(Integer idVozila, Date datumVracanja, float kupovnaCena, float kilometraza, boolean popravka, Date datumPopravke) {
        this.idVozila = idVozila;
        this.datumVracanja = datumVracanja;
        this.kupovnaCena = kupovnaCena;
        this.kilometraza = kilometraza;
        this.popravka = popravka;
        this.datumPopravke = datumPopravke;
    }

    public Integer getIdVozila() {
        return idVozila;
    }

    public void setIdVozila(Integer idVozila) {
        this.idVozila = idVozila;
    }

    public Date getDatumVracanja() {
        return datumVracanja;
    }

    public void setDatumVracanja(Date datumVracanja) {
        this.datumVracanja = datumVracanja;
    }

    public float getKupovnaCena() {
        return kupovnaCena;
    }

    public void setKupovnaCena(float kupovnaCena) {
        this.kupovnaCena = kupovnaCena;
    }

    public float getKilometraza() {
        return kilometraza;
    }

    public void setKilometraza(float kilometraza) {
        this.kilometraza = kilometraza;
    }

    public boolean getPopravka() {
        return popravka;
    }

    public void setPopravka(boolean popravka) {
        this.popravka = popravka;
    }

    public Date getDatumPopravke() {
        return datumPopravke;
    }

    public void setDatumPopravke(Date datumPopravke) {
        this.datumPopravke = datumPopravke;
    }

    @XmlTransient
    public Collection<Rezervacija> getRezervacijaCollection() {
        return rezervacijaCollection;
    }

    public void setRezervacijaCollection(Collection<Rezervacija> rezervacijaCollection) {
        this.rezervacijaCollection = rezervacijaCollection;
    }

    @XmlTransient
    public Collection<Obelezje> getObelezjeCollection() {
        return obelezjeCollection;
    }

    public void setObelezjeCollection(Collection<Obelezje> obelezjeCollection) {
        this.obelezjeCollection = obelezjeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVozila != null ? idVozila.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vozilo)) {
            return false;
        }
        Vozilo other = (Vozilo) object;
        if ((this.idVozila == null && other.idVozila != null) || (this.idVozila != null && !this.idVozila.equals(other.idVozila))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return " " +idVozila;
    }
    
}
