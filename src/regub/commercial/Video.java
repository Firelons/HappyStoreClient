/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package regub.commercial;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Lons
 */
public class Video {
    
    private final StringProperty titre;
    private final IntegerProperty duree;
    private final DoubleProperty tarif;
    private final StringProperty date_debut;
    private final StringProperty  date_fin;


    /**
     * Default constructor.
     */
    
    public Video() {
        this(null, 0, 0, null, null);
    }

    /**
     * Constructor with some initial data.
     * 
     * @param titre
     * @param duree
     * @param tarif
     * @param date_debut
     * @param date_fin
    
    
     */
    public Video(String titre, int duree, double tarif, String date_d, String date_f ) {
       
        this.titre = new SimpleStringProperty(titre);
        this.duree = new SimpleIntegerProperty(duree);
        this.tarif = new SimpleDoubleProperty(tarif);
        this.date_debut = new SimpleStringProperty(date_d);
        this.date_fin = new SimpleStringProperty(date_f);
        
    }

    public int getDuree() {
        return duree.get();
    }

    public void setDuree(int duree) {
        this.duree.set(duree);
    }

    public IntegerProperty dureeProperty() {
        return duree;
    }
    public String getTitre() {
        return titre.get();
    }

    public void setTitre(String firstName) {
        this.titre.set(firstName);
    }

    public StringProperty titreProperty() {
        return titre;
    }

  
  

    public String getDate_debut() {
        return date_debut.get();
    }

    public void setDate_debut(String postalCode) {
        this.date_debut.set(postalCode);
    }

    public StringProperty date_debutProperty() {
        return date_debut;
    }
    
    public String getDate_fin() {
        return date_fin.get();
    }

    public void setDate_fin(String postalCode) {
        this.date_fin.set(postalCode);
    }

    public StringProperty date_finProperty() {
        return date_fin;
    }

    public double getTarif() {
        return tarif.get();
    }

    public void setTarif(double city) {
        this.tarif.set(city);
    }

    public DoubleProperty tarifProperty() {
        return tarif;
    }

 
}