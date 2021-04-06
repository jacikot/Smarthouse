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
@Table(name = "vreme")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Vreme.findAll", query = "SELECT v FROM Vreme v"),
    @NamedQuery(name = "Vreme.findByIdPV", query = "SELECT v FROM Vreme v WHERE v.idPV = :idPV"),
    @NamedQuery(name = "Vreme.findByVreme", query = "SELECT v FROM Vreme v WHERE v.vreme = :vreme")})
public class Vreme implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "vreme")
    @Temporal(TemporalType.TIME)
    private Date vreme;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPV")
    private Integer idPV;

    public Vreme() {
    }

    public Vreme(Integer idPV) {
        this.idPV = idPV;
    }

    public Vreme(Integer idPV, Date vreme) {
        this.idPV = idPV;
        this.vreme = vreme;
    }

    public Integer getIdPV() {
        return idPV;
    }

    public void setIdPV(Integer idPV) {
        this.idPV = idPV;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPV != null ? idPV.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vreme)) {
            return false;
        }
        Vreme other = (Vreme) object;
        if ((this.idPV == null && other.idPV != null) || (this.idPV != null && !this.idPV.equals(other.idPV))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Vreme[ idPV=" + idPV + " ]";
    }

    public Date getVreme() {
        return vreme;
    }

    public void setVreme(Date vreme) {
        this.vreme = vreme;
    }
    
}
