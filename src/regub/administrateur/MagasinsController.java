/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub.administrateur;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import regub.AbstractController;
import regub.Auth;
import regub.Main;
import regub.util.UserBarController;

/**
 * FXML Controller class
 *
 * @author BREGMESTRE
 */
public class MagasinsController extends AbstractController {

    @FXML
    private UserBarController usermenuController;

    @FXML
    private TableView<Magasin> tableMagasin;

    @FXML
    private TableColumn<Magasin, String> colCodePostal;

    @FXML
    private TableColumn<Magasin, String> colVille;

    @FXML
    private TableColumn<Magasin, String> colNom;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;
    
    private static int ID;

    @Override
    public void setApp(Main m) {
        super.setApp(m);
        usermenuController.setApp(m);
    }

    @FXML 
    void handleSupprimer(){
    
        ID = Magasin.getCurMag().getId();

        String sql;

        sql = "DELETE FROM magasin WHERE idMagasin=" + ID + " ";
        try (Connection cn = Auth.getConnection();
                PreparedStatement st1 = cn.prepareStatement(sql)) {

            st1.execute();

        } catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Vous ne pouvez pas supprimer ce magasin !! ", ButtonType.OK);
            a.showAndWait();
        }
        getApp().gotoPage("administrateur/Magasins");
    
    }
    
    @FXML
    void handleAjouter() {
        Magasin.setCurMag(null);
         getApp().gotoPage("administrateur/MagasinAJMO");
    }
    @FXML
    void handleModifier() {
         getApp().gotoPage("administrateur/MagasinAJMO");
    }
    
    private void initTable() {
        String sql = "SELECT * FROM `Magasin` ORDER BY `nom`;";
        ObservableList<Magasin> magList = FXCollections.observableArrayList();
        try (Connection cn = Auth.getConnection();
                Statement st = cn.createStatement();
                ResultSet res = st.executeQuery(sql)) {
            while (res.next()) {
                magList.add(
                        new Magasin(
                                res.getInt("idMagasin"),
                                res.getString("nom"),
                                res.getString("addr_ligne1"),
                                res.getString("addr_ligne2"),
                                res.getString("code_postal"),
                                res.getInt("idRegion"),
                                res.getString("ville"))
                );
            }
        } 
        
      catch (SQLException ex) {
            ex.printStackTrace();
           
        }
        tableMagasin.setItems(magList);
        
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colCodePostal.setCellValueFactory(cellData -> cellData.getValue().getCodePostalProperty());
        colNom.setCellValueFactory(cellData -> cellData.getValue().getNomProperty());
        colVille.setCellValueFactory(cellData -> cellData.getValue().getVilleProperty());
        tableMagasin.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableMagasin.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener.Change<? extends Magasin> c) -> {
                    while (c.next()) {
                        if (c.getAddedSize() > 0) {
                            Magasin.setCurMag(c.getAddedSubList().get(0));
                            btnModifier.setDisable(false);
                            btnSupprimer.setDisable(false);
                        } else if (tableMagasin.getSelectionModel().getSelectedItems().size() == 0) {
                            Magasin.setCurMag(null);
                            btnModifier.setDisable(true);
                            btnSupprimer.setDisable(true);
                        }
                    }
                });
        initTable();
    }
}
