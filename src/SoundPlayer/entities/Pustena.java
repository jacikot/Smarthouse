/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

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
 * @author janat
 */
@Entity
@Table(name = "pustena")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pustena.findAll", query = "SELECT p FROM Pustena p"),
    @NamedQuery(name = "Pustena.findByIdPust", query = "SELECT p FROM Pustena p WHERE p.idPust = :idPust")})
public class Pustena implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPust")
    private Integer idPust;
    @JoinColumn(name = "idP", referencedColumnName = "idP")
    @ManyToOne(optional = false)
    private Pesma idP;
    @JoinColumn(name = "idU", referencedColumnName = "idU")
    @ManyToOne(optional = false)
    private User idU;

    public Pustena() {
    }

    public Pustena(Integer idPust) {
        this.idPust = idPust;
    }

    public Integer getIdPust() {
        return idPust;
    }

    public void setIdPust(Integer idPust) {
        this.idPust = idPust;
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
        hash += (idPust != null ? idPust.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pustena)) {
            return false;
        }
        Pustena other = (Pustena) object;
        if ((this.idPust == null && other.idPust != null) || (this.idPust != null && !this.idPust.equals(other.idPust))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Pustena[ idPust=" + idPust + " ]";
    }
    
}
