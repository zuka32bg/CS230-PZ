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
@Table(name = "firmaosiguranja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Firmaosiguranja.findAll", query = "SELECT f FROM Firmaosiguranja f")
    , @NamedQuery(name = "Firmaosiguranja.findByIdFirme", query = "SELECT f FROM Firmaosiguranja f WHERE f.idFirme = :idFirme")})
public class Firmaosiguranja implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_FIRME")
    private Integer idFirme;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "NAZIV")
    private String naziv;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "ADRESA_FIRME")
    private String adresaFirme;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "TELEFON_FIRME")
    private String telefonFirme;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "FAKS_FIRME")
    private String faksFirme;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idFirme")
    private Collection<Osiguranje> osiguranjeCollection;

    public Firmaosiguranja() {
    }

    public Firmaosiguranja(Integer idFirme) {
        this.idFirme = idFirme;
    }

    public Firmaosiguranja(Integer idFirme, String naziv, String adresaFirme, String telefonFirme, String faksFirme) {
        this.idFirme = idFirme;
        this.naziv = naziv;
        this.adresaFirme = adresaFirme;
        this.telefonFirme = telefonFirme;
        this.faksFirme = faksFirme;
    }

    public Integer getIdFirme() {
        return idFirme;
    }

    public void setIdFirme(Integer idFirme) {
        this.idFirme = idFirme;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getAdresaFirme() {
        return adresaFirme;
    }

    public void setAdresaFirme(String adresaFirme) {
        this.adresaFirme = adresaFirme;
    }

    public String getTelefonFirme() {
        return telefonFirme;
    }

    public void setTelefonFirme(String telefonFirme) {
        this.telefonFirme = telefonFirme;
    }

    public String getFaksFirme() {
        return faksFirme;
    }

    public void setFaksFirme(String faksFirme) {
        this.faksFirme = faksFirme;
    }

    @XmlTransient
    public Collection<Osiguranje> getOsiguranjeCollection() {
        return osiguranjeCollection;
    }

    public void setOsiguranjeCollection(Collection<Osiguranje> osiguranjeCollection) {
        this.osiguranjeCollection = osiguranjeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFirme != null ? idFirme.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Firmaosiguranja)) {
            return false;
        }
        Firmaosiguranja other = (Firmaosiguranja) object;
        if ((this.idFirme == null && other.idFirme != null) || (this.idFirme != null && !this.idFirme.equals(other.idFirme))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return " " + naziv ;
    }
    
}
