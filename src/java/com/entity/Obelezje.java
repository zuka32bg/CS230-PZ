/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Uros Zukancic
 */
@Entity
@Table(name = "obelezje")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Obelezje.findAll", query = "SELECT o FROM Obelezje o")
    , @NamedQuery(name = "Obelezje.findByIdObelezja", query = "SELECT o FROM Obelezje o WHERE o.idObelezja = :idObelezja")})
public class Obelezje implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_OBELEZJA")
    private Integer idObelezja;
    @JoinColumn(name = "ID_VOZILA", referencedColumnName = "ID_VOZILA")
    @ManyToOne(optional = false)
    private Vozilo idVozila;
    @JoinColumn(name = "ID_MODELA", referencedColumnName = "ID_MODELA")
    @ManyToOne(optional = false)
    private Modelautomobila idModela;

    public Obelezje() {
    }

    public Obelezje(Integer idObelezja) {
        this.idObelezja = idObelezja;
    }

    public Integer getIdObelezja() {
        return idObelezja;
    }

    public void setIdObelezja(Integer idObelezja) {
        this.idObelezja = idObelezja;
    }

    public Vozilo getIdVozila() {
        return idVozila;
    }

    public void setIdVozila(Vozilo idVozila) {
        this.idVozila = idVozila;
    }

    public Modelautomobila getIdModela() {
        return idModela;
    }

    public void setIdModela(Modelautomobila idModela) {
        this.idModela = idModela;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idObelezja != null ? idObelezja.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Obelezje)) {
            return false;
        }
        Obelezje other = (Obelezje) object;
        if ((this.idObelezja == null && other.idObelezja != null) || (this.idObelezja != null && !this.idObelezja.equals(other.idObelezja))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return " " + idObelezja;
    }
    
}
