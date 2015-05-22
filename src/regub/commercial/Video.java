/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub.commercial;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import regub.Auth;

/**
 *
 * @author Mesmerus
 */
public class Video {

    private static HashMap<String, Integer> rayondata = new HashMap<>();
    private static HashMap<String, Integer> regiondata = new HashMap<>();

    private final IntegerProperty idVideo;
    private final StringProperty titre;
    private final IntegerProperty frequence;
    private final IntegerProperty duree;
    private final StringProperty date_debut;
    private final StringProperty date_fin;
    private final StringProperty date_reception;
    private final StringProperty date_validation;
    private final DoubleProperty tarif;
    private final IntegerProperty statut;

    private static Video curVideo;

    /**
     * Default constructor.
     */
    public Video() {
        this(0, null, 0, 0, null, null, null, null, 0, 1);
    }

    /**
     * Constructor with some initial data.
     *
     * @param idVid
     * @param titre
     * @param freq
     * @param duree
     * @param date_d
     * @param date_f
     * @param date_r
     * @param date_v
     * @param tarif
     * @param statut
     *
     *
     */
    public Video(int idVid, String titre, int freq, int duree, String date_d, String date_f, String date_r, String date_v, double tarif, int statut) {

        this.idVideo = new SimpleIntegerProperty(idVid);
        this.titre = new SimpleStringProperty(titre);
        this.frequence = new SimpleIntegerProperty(freq);
        this.duree = new SimpleIntegerProperty(duree);
        this.date_debut = new SimpleStringProperty(date_d);
        this.date_fin = new SimpleStringProperty(date_f);
        this.date_reception = new SimpleStringProperty(date_r);
        this.date_validation = new SimpleStringProperty(date_v);
        this.tarif = new SimpleDoubleProperty(tarif);
        this.statut = new SimpleIntegerProperty(statut);

    }

    public int getidVideo() {
        return idVideo.get();
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

    public int getDuree() {
        return duree.get();
    }

    public void setDuree(int duree) {
        this.duree.set(duree);
    }

    public IntegerProperty dureeProperty() {
        return duree;
    }

    public int getfrequence() {
        return frequence.get();
    }

    public void setfrequence(int freq) {
        this.frequence.set(freq);
    }

    public String getDate_debut() {
        return date_debut.get();
    }

    public void setDate_debut(String datedeb) {
        this.date_debut.set(datedeb);
    }

    public StringProperty date_debutProperty() {
        return date_debut;
    }

    public String getDate_fin() {
        return date_fin.get();
    }

    public void setDate_fin(String datefin) {
        this.date_fin.set(datefin);
    }

    public StringProperty date_finProperty() {
        return date_fin;
    }

    public String getDate_reception() {
        return date_reception.get();
    }

    public void setDate_reception(String daterec) {
        this.date_reception.set(daterec);
    }

    public String getDate_validation() {
        return date_validation.get();
    }

    public void setDate_validation(String dateval) {
        this.date_validation.set(dateval);
    }

    public int getStatut() {
        return statut.get();
    }

    public void setStatut(int stat) {
        this.statut.set(stat);
    }
    
    public IntegerProperty StatutProperty() {
        return statut;
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

    public static void setCurVideo(Video vid) {
        curVideo = vid;
    }

    public static Video getCurVideo() {
        return curVideo;
    }

    public HashMap<String, Integer> getcurRegions() {
        regiondata.clear();
        String sql = "SELECT * FROM Region INNER JOIN DiffusionRegions ON  Region.idRegion=DiffusionRegions.idRegion "
                + " WHERE DiffusionRegions.idVideo= ? ;";
        try (Connection cn = Auth.getConnection();
                PreparedStatement st = cn.prepareStatement(sql)) {
            st.setInt(1, Video.getCurVideo().getidVideo());
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                regiondata.put(rs.getString("libelle"), rs.getInt("idRegion"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
               return regiondata;
    }
    public  void setCurRegions(HashMap<String, Integer> reg) {
        regiondata = reg;
    }
    
    public HashMap<String, Integer> getcurTypeRayon() {
        rayondata.clear();
        String sql = "SELECT * FROM TypeRayon INNER JOIN DiffusionsTypesRayons ON TypeRayon.idTypeRayon = DiffusionsTypesRayons.idTypeRayon WHERE DiffusionsTypesRayons.idVideo = ? ;";
         System.out.println( "On a ça "+Video.getCurVideo().getidVideo());
        try (Connection cn = Auth.getConnection();
                PreparedStatement st = cn.prepareStatement(sql)) {
            st.setInt(1, Video.getCurVideo().getidVideo());
            ResultSet rs = st.executeQuery();
          
            while (rs.next()) {
                rayondata.put(rs.getString("libelle"), rs.getInt("idTypeRayon"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rayondata;
    }
     public  void setCurTypeRayon(HashMap<String, Integer> ray) {
        rayondata = ray;
    }

}
