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

import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import regub.AbstractController;
import regub.Auth;

import regub.Main;

import regub.util.UserBarController;


/**
 * FXML Controller class
 *
 * @author BREGMESTRE
 */
public class TypeRayonAJMOController extends AbstractController {
    
   @FXML
    private TextField NomRayon;

   @FXML
    private UserBarController usermenuController;
    @FXML
    private ListView MagasinTable;

    @FXML
    private ObservableList<String> magazinData = FXCollections.observableArrayList();

    @FXML
    private Label Message;

    @FXML
    private ResultSet rsMagazin;
    
    
   

    @FXML
    private void Annuler(ActionEvent event) throws IOException {
        getApp().gotoPage("administrateur/TypeRayon");
    }
    
    @FXML
    private void Magasins(ActionEvent event) {
        getApp().gotoPage("administrateur/Magasins");
    }

    private boolean Verifier_Saisie() throws IOException {

        String message_error = "";
        Boolean retour = true;

        if (NomRayon.getText().length() == 0) {
            message_error = "Entrez le nom d'un Type Rayon";
            retour = false;
        }

        if (!retour) {
            Alert a = new Alert(Alert.AlertType.WARNING, message_error, ButtonType.OK);
            a.showAndWait();
            Message.setText(message_error);
        }

        return retour;

    }

    @FXML
    private void Save_TypeRayon() throws IOException {

        System.out.println(Auth.getUserInfo().toString());

         boolean update = false;
         String sql;
        if (TypeRayonController.select_rayon_id!=0) {
            sql = "UPDATE TypeRayon SET libelle=? WHERE idTypeRayon=?;";
            update = true;
        } else {
            sql = "INSERT INTO TypeRayon(libelle) VALUES (?);";
        }
        try (Connection cn = Auth.getConnection();
                Statement st = cn.createStatement()) {
            
            PreparedStatement st1 = cn.prepareStatement(sql);
            st1.setString(1, this.NomRayon.getText());
            if(update){
                 st1.setInt(2,TypeRayonController.select_rayon_id );
            }
            st1.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Valider_Rayon(ActionEvent event) throws IOException {
        if (Verifier_Saisie()) {
            Save_TypeRayon();
            getApp().gotoPage("administrateur/TypeRayon");
        }
    }

    @FXML
    private void getMagazinDB() throws IOException {
    int id_Rayon;
            System.out.println(Auth.getUserInfo().toString());
        
        id_Rayon=TypeRayonController.select_rayon_id;
        System.out.println(id_Rayon);
        try (Connection cn = Auth.getConnection();
                Statement st = cn.createStatement()) {
            String sql = "SELECT * FROM magasin INNER JOIN rayons ON rayons.idMagasin =magasin.idMagasin WHERE idTypeRayon="+id_Rayon+" ";

            rsMagazin = st.executeQuery(sql);
            while (rsMagazin.next()) {
                magazinData.add(new String(rsMagazin.getString("nom")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public ObservableList<String> getMagazinData() {

        try {
            this.getMagazinDB();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //magazinData.add(new String("4"));
        return magazinData;
    }

    @Override
    public void setApp(Main m) {
        super.setApp(m);
        usermenuController.setApp(m);
        MagasinTable.setItems(getMagazinData());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.NomRayon.setText(TypeRayonController.select_rayon);  
    }
}

    
    

    
    

