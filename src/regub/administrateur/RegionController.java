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
 *
 * @author Souad
 */
public class RegionController extends AbstractController {

    @FXML
    private UserBarController usermenuController;

    @FXML
    private TextField textRegion;
    
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
        getApp().gotoPage("administrateur/RegionAccueil");
    }

    private boolean Verifier_Saisie() throws IOException {

        String message_error = "";
        Boolean retour = true;

        if (textRegion.getText().length() == 0) {
            message_error = "Entrez le nom d'une Region";
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
    private void Save_Region() throws IOException {

        System.out.println(Auth.getUserInfo().toString());

        try (Connection cn = Auth.getConnection();
                Statement st = cn.createStatement()) {
            String sql = "INSERT INTO Region(libelle)"
                    + "VALUES (?);";
            PreparedStatement st1 = cn.prepareStatement(sql);
            st1.setString(1, this.textRegion.getText());
            st1.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Valider_Region(ActionEvent event) throws IOException {
        if (Verifier_Saisie()) {
            Save_Region();
            getApp().gotoPage("administrateur/RegionAccueil");
        }

    }

    @FXML
    private void getMagazinDB() throws IOException {
    int id_client;
            System.out.println(Auth.getUserInfo().toString());
        
        id_client=RegionAccueilController.select_region_id;
        System.out.println(id_client);
        try (Connection cn = Auth.getConnection();
                Statement st = cn.createStatement()) {
            String sql = "SELECT * FROM magasin where idRegion="+id_client+" ";

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
        
        this.textRegion.setText(RegionAccueilController.select_region);  
    }
}
