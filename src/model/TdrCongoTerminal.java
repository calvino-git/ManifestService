/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Calvin ILOKI
 */
@Entity
@Table(name = "TDR_CONGO_TERMINAL", catalog = "", schema = "PPNCARGO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TdrCongoTerminal.findAll", query = "SELECT t FROM TdrCongoTerminal t"),
    @NamedQuery(name = "TdrCongoTerminal.findById", query = "SELECT t FROM TdrCongoTerminal t WHERE t.id = :id"),
    @NamedQuery(name = "TdrCongoTerminal.findByDepartEff", query = "SELECT t FROM TdrCongoTerminal t WHERE t.departEff = :departEff"),
    @NamedQuery(name = "TdrCongoTerminal.findByAnnee", query = "SELECT t FROM TdrCongoTerminal t WHERE t.annee = :annee"),
    @NamedQuery(name = "TdrCongoTerminal.findByNavire", query = "SELECT t FROM TdrCongoTerminal t WHERE t.navire = :navire"),
    @NamedQuery(name = "TdrCongoTerminal.findByMois", query = "SELECT t FROM TdrCongoTerminal t WHERE t.mois = :mois"),
    @NamedQuery(name = "TdrCongoTerminal.findByTrafic", query = "SELECT t FROM TdrCongoTerminal t WHERE t.trafic = :trafic"),
    @NamedQuery(name = "TdrCongoTerminal.findByPays", query = "SELECT t FROM TdrCongoTerminal t WHERE t.pays = :pays"),
    @NamedQuery(name = "TdrCongoTerminal.findByClients", query = "SELECT t FROM TdrCongoTerminal t WHERE t.clients = :clients"),
    @NamedQuery(name = "TdrCongoTerminal.findByDebaPlein20", query = "SELECT t FROM TdrCongoTerminal t WHERE t.debaPlein20 = :debaPlein20"),
    @NamedQuery(name = "TdrCongoTerminal.findByDebaVide20", query = "SELECT t FROM TdrCongoTerminal t WHERE t.debaVide20 = :debaVide20"),
    @NamedQuery(name = "TdrCongoTerminal.findByDebaSum20", query = "SELECT t FROM TdrCongoTerminal t WHERE t.debaSum20 = :debaSum20"),
    @NamedQuery(name = "TdrCongoTerminal.findByDebaPlein40", query = "SELECT t FROM TdrCongoTerminal t WHERE t.debaPlein40 = :debaPlein40"),
    @NamedQuery(name = "TdrCongoTerminal.findByDebaVide40", query = "SELECT t FROM TdrCongoTerminal t WHERE t.debaVide40 = :debaVide40"),
    @NamedQuery(name = "TdrCongoTerminal.findByDebaSum40", query = "SELECT t FROM TdrCongoTerminal t WHERE t.debaSum40 = :debaSum40"),
    @NamedQuery(name = "TdrCongoTerminal.findByDebaSum", query = "SELECT t FROM TdrCongoTerminal t WHERE t.debaSum = :debaSum"),
    @NamedQuery(name = "TdrCongoTerminal.findByEmbaPlein20", query = "SELECT t FROM TdrCongoTerminal t WHERE t.embaPlein20 = :embaPlein20"),
    @NamedQuery(name = "TdrCongoTerminal.findByEmbaVide20", query = "SELECT t FROM TdrCongoTerminal t WHERE t.embaVide20 = :embaVide20"),
    @NamedQuery(name = "TdrCongoTerminal.findByEmbaSum20", query = "SELECT t FROM TdrCongoTerminal t WHERE t.embaSum20 = :embaSum20"),
    @NamedQuery(name = "TdrCongoTerminal.findByEmbaPlein40", query = "SELECT t FROM TdrCongoTerminal t WHERE t.embaPlein40 = :embaPlein40"),
    @NamedQuery(name = "TdrCongoTerminal.findByEmbaVide40", query = "SELECT t FROM TdrCongoTerminal t WHERE t.embaVide40 = :embaVide40"),
    @NamedQuery(name = "TdrCongoTerminal.findByEmbaSum40", query = "SELECT t FROM TdrCongoTerminal t WHERE t.embaSum40 = :embaSum40"),
    @NamedQuery(name = "TdrCongoTerminal.findByEmbaSum", query = "SELECT t FROM TdrCongoTerminal t WHERE t.embaSum = :embaSum"),
    @NamedQuery(name = "TdrCongoTerminal.findBySum20", query = "SELECT t FROM TdrCongoTerminal t WHERE t.sum20 = :sum20"),
    @NamedQuery(name = "TdrCongoTerminal.findBySum40", query = "SELECT t FROM TdrCongoTerminal t WHERE t.sum40 = :sum40"),
    @NamedQuery(name = "TdrCongoTerminal.findByEvp", query = "SELECT t FROM TdrCongoTerminal t WHERE t.evp = :evp"),
    @NamedQuery(name = "TdrCongoTerminal.findByTotal", query = "SELECT t FROM TdrCongoTerminal t WHERE t.total = :total"),
    @NamedQuery(name = "TdrCongoTerminal.findByPoids", query = "SELECT t FROM TdrCongoTerminal t WHERE t.poids = :poids")})
public class TdrCongoTerminal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "ID")
    @Id
    private BigInteger id;
    @Size(max = 8)
    @Column(name = "DEPART_EFF", length = 8)
    private String departEff;
    @Size(max = 4)
    @Column(name = "ANNEE", length = 4)
    private String annee;
    @Size(max = 40)
    @Column(name = "NAVIRE", length = 40)
    private String navire;
    @Size(max = 6)
    @Column(name = "MOIS", length = 6)
    private String mois;
    @Size(max = 8)
    @Column(name = "TRAFIC", length = 8)
    private String trafic;
    @Size(max = 40)
    @Column(name = "PAYS", length = 40)
    private String pays;
    @Size(max = 50)
    @Column(name = "CLIENTS", length = 50)
    private String clients;
    @Column(name = "DEBA_PLEIN_20")
    private BigInteger debaPlein20;
    @Column(name = "DEBA_VIDE_20")
    private BigInteger debaVide20;
    @Column(name = "DEBA_SUM_20")
    private BigInteger debaSum20;
    @Column(name = "DEBA_PLEIN_40")
    private BigInteger debaPlein40;
    @Column(name = "DEBA_VIDE_40")
    private BigInteger debaVide40;
    @Column(name = "DEBA_SUM_40")
    private BigInteger debaSum40;
    @Column(name = "DEBA_SUM")
    private BigInteger debaSum;
    @Column(name = "EMBA_PLEIN_20")
    private BigInteger embaPlein20;
    @Column(name = "EMBA_VIDE_20")
    private BigInteger embaVide20;
    @Column(name = "EMBA_SUM_20")
    private BigInteger embaSum20;
    @Column(name = "EMBA_PLEIN_40")
    private BigInteger embaPlein40;
    @Column(name = "EMBA_VIDE_40")
    private BigInteger embaVide40;
    @Column(name = "EMBA_SUM_40")
    private BigInteger embaSum40;
    @Column(name = "EMBA_SUM")
    private BigInteger embaSum;
    @Column(name = "SUM_20")
    private BigInteger sum20;
    @Column(name = "SUM_40")
    private BigInteger sum40;
    @Column(name = "EVP")
    private BigInteger evp;
    @Column(name = "TOTAL")
    private BigInteger total;
    @Column(name = "POIDS")
    private BigInteger poids;

    public TdrCongoTerminal() {
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getDepartEff() {
        return departEff;
    }

    public void setDepartEff(String departEff) {
        this.departEff = departEff;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public String getNavire() {
        return navire;
    }

    public void setNavire(String navire) {
        this.navire = navire;
    }

    public String getMois() {
        return mois;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

    public String getTrafic() {
        return trafic;
    }

    public void setTrafic(String trafic) {
        this.trafic = trafic;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getClients() {
        return clients;
    }

    public void setClients(String clients) {
        this.clients = clients;
    }

    public BigInteger getDebaPlein20() {
        return debaPlein20;
    }

    public void setDebaPlein20(BigInteger debaPlein20) {
        this.debaPlein20 = debaPlein20;
    }

    public BigInteger getDebaVide20() {
        return debaVide20;
    }

    public void setDebaVide20(BigInteger debaVide20) {
        this.debaVide20 = debaVide20;
    }

    public BigInteger getDebaSum20() {
        return debaSum20;
    }

    public void setDebaSum20(BigInteger debaSum20) {
        this.debaSum20 = debaSum20;
    }

    public BigInteger getDebaPlein40() {
        return debaPlein40;
    }

    public void setDebaPlein40(BigInteger debaPlein40) {
        this.debaPlein40 = debaPlein40;
    }

    public BigInteger getDebaVide40() {
        return debaVide40;
    }

    public void setDebaVide40(BigInteger debaVide40) {
        this.debaVide40 = debaVide40;
    }

    public BigInteger getDebaSum40() {
        return debaSum40;
    }

    public void setDebaSum40(BigInteger debaSum40) {
        this.debaSum40 = debaSum40;
    }

    public BigInteger getDebaSum() {
        return debaSum;
    }

    public void setDebaSum(BigInteger debaSum) {
        this.debaSum = debaSum;
    }

    public BigInteger getEmbaPlein20() {
        return embaPlein20;
    }

    public void setEmbaPlein20(BigInteger embaPlein20) {
        this.embaPlein20 = embaPlein20;
    }

    public BigInteger getEmbaVide20() {
        return embaVide20;
    }

    public void setEmbaVide20(BigInteger embaVide20) {
        this.embaVide20 = embaVide20;
    }

    public BigInteger getEmbaSum20() {
        return embaSum20;
    }

    public void setEmbaSum20(BigInteger embaSum20) {
        this.embaSum20 = embaSum20;
    }

    public BigInteger getEmbaPlein40() {
        return embaPlein40;
    }

    public void setEmbaPlein40(BigInteger embaPlein40) {
        this.embaPlein40 = embaPlein40;
    }

    public BigInteger getEmbaVide40() {
        return embaVide40;
    }

    public void setEmbaVide40(BigInteger embaVide40) {
        this.embaVide40 = embaVide40;
    }

    public BigInteger getEmbaSum40() {
        return embaSum40;
    }

    public void setEmbaSum40(BigInteger embaSum40) {
        this.embaSum40 = embaSum40;
    }

    public BigInteger getEmbaSum() {
        return embaSum;
    }

    public void setEmbaSum(BigInteger embaSum) {
        this.embaSum = embaSum;
    }

    public BigInteger getSum20() {
        return sum20;
    }

    public void setSum20(BigInteger sum20) {
        this.sum20 = sum20;
    }

    public BigInteger getSum40() {
        return sum40;
    }

    public void setSum40(BigInteger sum40) {
        this.sum40 = sum40;
    }

    public BigInteger getEvp() {
        return evp;
    }

    public void setEvp(BigInteger evp) {
        this.evp = evp;
    }

    public BigInteger getTotal() {
        return total;
    }

    public void setTotal(BigInteger total) {
        this.total = total;
    }

    public BigInteger getPoids() {
        return poids;
    }

    public void setPoids(BigInteger poids) {
        this.poids = poids;
    }
    
}
