/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author janat
 */
@Entity
@Table(name = "obaveza")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Obaveza.findAll", query = "SELECT o FROM Obaveza o"),
    @NamedQuery(name = "Obaveza.findByIdO", query = "SELECT o FROM Obaveza o WHERE o.idO = :idO"),
    @NamedQuery(name = "Obaveza.findByPocetak", query = "SELECT o FROM Obaveza o WHERE o.pocetak = :pocetak"),
    @NamedQuery(name = "Obaveza.findByKraj", query = "SELECT o FROM Obaveza o WHERE o.kraj = :kraj"),
    @NamedQuery(name = "Obaveza.findByMesto", query = "SELECT o FROM Obaveza o WHERE o.mesto = :mesto")})
public class Obaveza implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idO")
    private Integer idO;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pocetak")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pocetak;
    @Basic(optional = false)
    @NotNull
    @Column(name = "kraj")
    @Temporal(TemporalType.TIMESTAMP)
    private Date kraj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "mesto")
    private String mesto;
    @JoinColumn(name = "idA", referencedColumnName = "idA")
    @ManyToOne
    private Alarm idA;
    @JoinColumn(name = "idU", referencedColumnName = "idU")
    @ManyToOne(optional = false)
    private User idU;

    public Obaveza() {
    }

    public Obaveza(Integer idO) {
        this.idO = idO;
    }

    public Obaveza(Integer idO, Date pocetak, Date kraj, String mesto) {
        this.idO = idO;
        this.pocetak = pocetak;
        this.kraj = kraj;
        this.mesto = mesto;
    }

    public Integer getIdO() {
        return idO;
    }

    public void setIdO(Integer idO) {
        this.idO = idO;
    }

    public Date getPocetak() {
        return pocetak;
    }

    public void setPocetak(Date pocetak) {
        this.pocetak = pocetak;
    }

    public Date getKraj() {
        return kraj;
    }

    public void setKraj(Date kraj) {
        this.kraj = kraj;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public Alarm getIdA() {
        return idA;
    }

    public void setIdA(Alarm idA) {
        this.idA = idA;
    }

    public User getIdU() {
        return idU;
    }

    public void setIdU(User idU) {
        this.idU = idU;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idO != null ? idO.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Obaveza)) {
            return false;
        }
        Obaveza other = (Obaveza) object;
        if ((this.idO == null && other.idO != null) || (this.idO != null && !this.idO.equals(other.idO))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Obaveza[ idO: " + idO + " vreme: "+pocetak+"-"+kraj+" mesto: "+mesto+" ]";
    }
    
}
