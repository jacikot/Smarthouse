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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author janat
 */
@Entity
@Table(name = "alarm")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alarm.findAll", query = "SELECT a FROM Alarm a"),
    @NamedQuery(name = "Alarm.findByIdA", query = "SELECT a FROM Alarm a WHERE a.idA = :idA"),
    @NamedQuery(name = "Alarm.findByAktivacija", query = "SELECT a FROM Alarm a WHERE a.aktivacija = :aktivacija"),
    @NamedQuery(name = "Alarm.findByPerioda", query = "SELECT a FROM Alarm a WHERE a.perioda = :perioda"),
    @NamedQuery(name = "Alarm.findByAktivan", query = "SELECT a FROM Alarm a WHERE a.aktivan = :aktivan")})
public class Alarm implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "aktivan")
    private int aktivan;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idA")
    private Integer idA;
    @Column(name = "aktivacija")
    @Temporal(TemporalType.TIMESTAMP)
    private Date aktivacija;
    @Column(name = "perioda")
    @Temporal(TemporalType.TIME)
    private Date perioda;
    @JoinColumn(name = "idP", referencedColumnName = "idP")
    @ManyToOne
    private Pesma idP;
    @JoinColumn(name = "idU", referencedColumnName = "idU")
    @ManyToOne(optional = false)
    private User idU;

    public Alarm() {
    }

    public Alarm(Integer idA) {
        this.idA = idA;
    }

    public Alarm(Integer idA, int aktivan) {
        this.idA = idA;
        this.aktivan = aktivan;
    }

    public Integer getIdA() {
        return idA;
    }

    public void setIdA(Integer idA) {
        this.idA = idA;
    }

    public Date getAktivacija() {
        return aktivacija;
    }

    public void setAktivacija(Date aktivacija) {
        this.aktivacija = aktivacija;
    }

    public Date getPerioda() {
        return perioda;
    }

    public void setPerioda(Date perioda) {
        this.perioda = perioda;
    }


    public Pesma getIdP() {
        return idP;
    }

    public void setIdP(Pesma idP) {
        this.idP = idP;
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
        hash += (idA != null ? idA.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alarm)) {
            return false;
        }
        Alarm other = (Alarm) object;
        if ((this.idA == null && other.idA != null) || (this.idA != null && !this.idA.equals(other.idA))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Alarm[ idA=" + idA + " ]";
    }

    public int getAktivan() {
        return aktivan;
    }

    public void setAktivan(int aktivan) {
        this.aktivan = aktivan;
    }
    
}
