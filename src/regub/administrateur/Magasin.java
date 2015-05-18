/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub.administrateur;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author antoine
 */
public class Magasin {

    private final IntegerProperty idMagasin;
    private final StringProperty nom;
    private final StringProperty addrL1;
    private final StringProperty addrL2;
    private final StringProperty codePostal;
    private final IntegerProperty idRegion;
    private final StringProperty ville;

    private static Magasin curMag;

    public Magasin() {
        this(0, null, null, null, null, 0, null);
    }

    public Magasin(int id, String nom, String addr_1, String addr_2, String CP, int id_region, String ville) {
        this.idMagasin = new SimpleIntegerProperty(id);
        this.nom = new SimpleStringProperty(nom);
        this.addrL1 = new SimpleStringProperty(addr_1);
        this.addrL2 = new SimpleStringProperty(addr_2);
        this.codePostal = new SimpleStringProperty(CP);
        this.idRegion = new SimpleIntegerProperty(id_region);
        this.ville = new SimpleStringProperty(ville);
    }

    public int getId() {
        return idMagasin.get();
    }

    public IntegerProperty getIdProperty() {
        return idMagasin;
    }

    public void setId(int id) {
        idMagasin.set(id);
    }

    public String getNom() {
        return nom.get();
    }

    public StringProperty getNomProperty() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public String getAddr_l1() {
        return addrL1.get();
    }

    public StringProperty getAddrL1Property() {
        return addrL1;
    }

    public void setAddr_l1(String addr_l1) {
        addrL1.set(addr_l1);
    }

    public String getAddr_l2() {
        return addrL2.get();
    }

    public StringProperty getAddrL2Property() {
        return addrL2;
    }

    public void setAddr_l2(String addr_l2) {
        addrL1.set(addr_l2);
    }

    public String getCodePostal() {
        return codePostal.get();
    }

    public StringProperty getCodePostalProperty() {
        return codePostal;
    }

    public void setCodePostal(String cp) {
        codePostal.set(cp);
    }

    public int getIdRegion() {
        return idRegion.get();
    }

    public IntegerProperty getIdRegionProperty() {
        return idRegion;
    }

    public void setIdRegion(int id) {
        this.idRegion.set(id);
    }

    public String getVille() {
        return ville.get();
    }

    public StringProperty getVilleProperty() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville.set(ville);
    }

    public static Magasin getCurMag() {
        return curMag;
    }

    public static void setCurMag(Magasin mag) {
        curMag = mag;
    }
}
