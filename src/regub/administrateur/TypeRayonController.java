

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub.administrateur;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import regub.AbstractController;
import regub.Auth;
import regub.Main;
import static regub.administrateur.TypeRayonController.select_rayon;
import static regub.administrateur.TypeRayonController.select_rayon_id;
import regub.commercial.ContratController;
import regub.util.UserBarController;

/**
 * FXML Controller class
 *
 * @author BREGMESTRE
 */
public class TypeRayonController extends AbstractController {

    @FXML
    private UserBarController usermenuController;

    @FXML
    private Button ModifierRayon;
    
    @FXML
    private Button SupprimerRayon;
    
    @FXML
    private Button AjouterRayon;
    
    @FXML 
    private ListView<String> listerayon;
    
    private HashMap<String, Integer> rayonData;
    
    @FXML
    private ResultSet rsrayon;
    
    public static int select_rayon_id;
    public static String select_rayon;
    
    @FXML 
       private void RayonAjouter(ActionEvent event) {
        select_rayon_id=0;
        select_rayon=null;
        getApp().gotoPage("administrateur/TypeRayonAJMO");
    }
       
         @FXML
    private void RayonModifier(ActionEvent event) {
        
        select_rayon_id=rayonData.get(listerayon.getSelectionModel().getSelectedItem());
        select_rayon=listerayon.getSelectionModel().getSelectedItem();
        System.out.println(rayonData.get(listerayon.getSelectionModel().getSelectedItem()));
        getApp().gotoPage("administrateur/TypeRayonAJMO");
    }
    
     @FXML
    private void RayonSupprimer(ActionEvent event) {
        
        select_rayon_id=rayonData.get(listerayon.getSelectionModel().getSelectedItem());
        select_rayon=listerayon.getSelectionModel().getSelectedItem();     
    
        String sql;
        
        sql = "DELETE FROM TypeRayon WHERE idTypeRayon="+select_rayon_id+" ;";
        try (Connection cn = Auth.getConnection();
                PreparedStatement st1 = cn.prepareStatement(sql)) {
            
            st1.execute();
           
            } catch (SQLException e) {
            e.printStackTrace();
        }
        getApp().gotoPage("administrateur/TypeRayon");
    }
    
    
    @FXML  
    public void getRayonData()  {
        try {
            rayonData = getliste("TypeRayon");
            listerayon.setItems(FXCollections.observableArrayList(rayonData.keySet()));
        } catch (IOException ex) {
            Logger.getLogger(ContratController.class.getName()).log(Level.SEVERE, null, ex);
        }
        listerayon.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }
 
    private HashMap<String, Integer> getliste(String Table) throws IOException {
        System.out.println(Auth.getUserInfo().toString());
        HashMap<String, Integer> resuMap = new HashMap<>();
        ResultSet res = null;
        try (Connection cn = Auth.getConnection();
                Statement st = cn.createStatement()) {
            String sql = "SELECT * FROM " + Table + " ORDER BY libelle ASC";
            res = st.executeQuery(sql);
            while (res.next()) {
                resuMap.put(res.getString("libelle"), res.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (res != null) {
                try {
                    res.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return resuMap;
    }
    
      private void gestionRayonButton() {
         
         ModifierRayon.setDisable(true);
        SupprimerRayon.setDisable(true);
        
        listerayon.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener) (c) -> {
                    listerayon.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                    int nbSelection = listerayon.getSelectionModel().getSelectedItems().size();
                    System.out.println(nbSelection);
                    if (nbSelection == 1) {
                        ModifierRayon.setDisable(false);
                        SupprimerRayon.setDisable(false);
                        /*if(magasin_ou_pas()){
                            SupprimerRegion.setDisable(false);
                        }else{
                            SupprimerRegion.setDisable(true);
                        }*/
                        
                    } else if (nbSelection > 1) {
                        ModifierRayon.setDisable(true);
                        SupprimerRayon.setDisable(false);
                    } else if (nbSelection == 0) {
                        ModifierRayon.setDisable(true);
                        SupprimerRayon.setDisable(true);

                    }
                });
    }
    @Override
    public void setApp(Main m) {
        super.setApp(m);
        usermenuController.setApp(m);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gestionRayonButton() ;
        getRayonData();
    }
}
