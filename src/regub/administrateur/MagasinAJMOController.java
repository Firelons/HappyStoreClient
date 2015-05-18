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
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.Observable;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
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
public class MagasinAJMOController extends AbstractController {

    @FXML
    private TextField textNom;
    @FXML
    private TextField textRue;
    @FXML
    private TextField textComp;
    @FXML
    private TextField textCodePostal;
    @FXML
    private TextField textVille;
    @FXML
    private ComboBox<String> combRegion;
    @FXML
    private ListView<String> listTypeRayon;
    @FXML
    private Label lblMessage;
    @FXML
    private Label lblTitre;
    @FXML
    private UserBarController usermenuController;

    private boolean insertion;

    private final HashMap<String, Integer> typeRayon = new HashMap<>();

    private final HashMap<String, Integer> lstRegion = new HashMap<>();

    private boolean Verifier_Saisie() {
        if (textNom.getLength() == 0) {
            lblMessage.setText("Vous devez entrer un nom");
            return false;
        }
        if (textRue.getLength() == 0) {
            lblMessage.setText("Vous devez entrer une rue");
            return false;
        }
        if (textCodePostal.getLength() == 0) {
            lblMessage.setText("Vous devez entrer un code postal");
            return false;
        }
        if (!textCodePostal.getText().matches("^\\d{5}$")) {
            lblMessage.setText("Vous devez entrer un code postal valide");
            return false;
        }
        if (textVille.getLength() == 0) {
            lblMessage.setText("Vous devez entrer une ville");
            return false;
        }
        if (combRegion.getSelectionModel().isEmpty()) {
            lblMessage.setText("Vous devez choisir une région");
            return false;
        }
        return true;
    }

    private void Save_Magasin() {
        String sql;
        Alert al = new Alert(Alert.AlertType.NONE);
        int idmag = -1;
        if (insertion) {
            sql = "INSERT INTO `Magasin`(`nom`,`addr_ligne1`, `addr_ligne2`, `code_postal`, `idRegion`, `ville`) "
                    + "VALUES( ? , ? , ? , ?,(SELECT `idRegion` FROM `Region` WHERE(libelle = ?)) , ? );";
        } else {
            sql = "UPDATE `Magasin` SET `nom` = ?,`addr_ligne1` = ?, `addr_ligne2` = ?, `code_postal` = ?, "
                    + "`idRegion`= ?, `ville` = ? "
                    + "WHERE(`idMagasin` = ? );";
        }
        try (
                Connection cn = Auth.getConnection();
                PreparedStatement st1 = cn.prepareStatement(sql)) {
            st1.setString(1, textNom.getText());
            st1.setString(2, textRue.getText());
            st1.setString(3, textComp.getText());
            st1.setString(4, textCodePostal.getText());
            st1.setInt(5, lstRegion.get(combRegion.getValue()));
            st1.setString(6, textVille.getText());
            if (!insertion) {
                st1.setInt(7, Magasin.getCurMag().getId());
            }
            st1.execute();
            if (insertion) {
                try (ResultSet res = st1.getGeneratedKeys()) {
                    res.first();
                    idmag = res.getInt("idMagasin");
                }
            } else {
                idmag = Magasin.getCurMag().getId();
            }
            updateRayons(idmag);
            al.setAlertType(Alert.AlertType.INFORMATION);
            al.setHeaderText("Confirmation");
            al.setContentText("Le magasin a bien été enregistré ");
        } catch (SQLException e) {
            al.setAlertType(Alert.AlertType.ERROR);
            al.setContentText("Une erreur est survenue lors de l'enregistrement");
            e.printStackTrace();
        }
        al.showAndWait();
    }

    private void updateRayons(int idmag) {
        try (Connection cn = Auth.getConnection()) {
            if (!insertion) {
                String sqlDelete = "DELETE FROM `Rayons` WHERE(`idMagasin` = ?);";
                try (PreparedStatement st = cn.prepareStatement(sqlDelete)) {
                    st.setInt(1, idmag);
                    st.executeQuery();
                }
            }
            int nbrVal = listTypeRayon.getSelectionModel().getSelectedItems().size();
            if (nbrVal <= 0) {
                return;
            }
            StringBuilder buildSqlInsert = new StringBuilder(
                    "INSERT INTO `Rayons`(`idMagasin`, `idTypeRayon`) VALUES ");
            String value = "( ? , ? )";
            for (int i = 0; i < nbrVal; i++) {
                buildSqlInsert.append(value);
                if (i < nbrVal - 1) {
                    buildSqlInsert.append(',');
                }
            }
            buildSqlInsert.append(';');
            System.out.println(buildSqlInsert);
            try (PreparedStatement sti = cn.prepareStatement(buildSqlInsert.toString())) {
                int i = 1;
                for (String rayon : listTypeRayon.getSelectionModel().getSelectedItems()) {
                    sti.setInt(i++, idmag);
                    sti.setInt(i++, typeRayon.get(rayon));
                }
                sti.executeQuery();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleValider(ActionEvent event) {
        if (Verifier_Saisie()) {
            Save_Magasin();
            Magasin.setCurMag(null);
            getApp().gotoPage("administrateur/Magasins");
        }

    }

    @FXML
    private void handleAnnuler(ActionEvent event) {
        Magasin.setCurMag(null);
        getApp().gotoPage("administrateur/Magasins");
    }

    @Override
    public void setApp(Main m) {
        super.setApp(m);
        usermenuController.setApp(m);
    }

    private void initRegComb() {
        String sql = "SELECT `idRegion`,`libelle` FROM `Region`;";
        String curReg = null;
        try (
                Connection cn = Auth.getConnection();
                Statement st = cn.createStatement();
                ResultSet res = st.executeQuery(sql);) {
            while (res.next()) {
                int idr = res.getInt("idRegion");
                String lib = res.getString("libelle");
                lstRegion.put(lib, idr);
                if (!insertion && idr == Magasin.getCurMag().getIdRegion()) {
                    curReg = lib;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        combRegion.setItems(FXCollections.observableArrayList(lstRegion.keySet()));
        if (!insertion) {
            combRegion.setValue(curReg);
        }
    }
    @FXML
    private void initTypeRayList() {
        String sql = "SELECT `idTypeRayon`,`libelle` FROM `TypeRayon`;";
        try (Connection cn = Auth.getConnection()) {
            try (
                    Statement st = cn.createStatement();
                    ResultSet res = st.executeQuery(sql);) {
                typeRayon.clear();
                while (res.next()) {
                    String s = res.getString(2);
                    int i = res.getInt(1);
                    typeRayon.put(s, i);
                }
                listTypeRayon.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                listTypeRayon.setItems(FXCollections.observableArrayList(typeRayon.keySet()));
            }

            if (insertion) {
                return;
            }
            sql = "SELECT `libelle` FROM `Rayons` INNER JOIN `TypeRayon` "
                    + "ON `TypeRayon`.`idTypeRayon` = `Rayons`.`idTypeRayon`"
                    + "WHERE(`Rayons`.`idMagasin` = ? );";
            try (PreparedStatement st = cn.prepareStatement(sql)) {
                st.setInt(1, Magasin.getCurMag().getId());
                st.executeQuery();
                listTypeRayon.getSelectionModel().clearSelection();
                try (ResultSet res = st.getResultSet()) {
                    System.out.println(res);
                    while (res.next()) {
                        listTypeRayon.getSelectionModel().select(res.getString("libelle"));
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        insertion = Magasin.getCurMag() == null;
        if(insertion){
            lblTitre.setText("Ajouter");
        }else{
            lblTitre.setText("Modifier");
        }
        initRegComb();
        initTypeRayList();
        textCodePostal.textProperty().addListener((Observable str) -> {
            String s = ((StringProperty) str).get();
            if (!s.matches("^\\d{5}$")) {
                textCodePostal.setStyle("-fx-background-color: #FFB2B4;");
            } else {
                textCodePostal.setStyle("");
            }
        });
        if (!insertion) {
            textCodePostal.setText(Magasin.getCurMag().getCodePostal());
            textRue.setText(Magasin.getCurMag().getAddr_l1());
            textComp.setText(Magasin.getCurMag().getAddr_l2());
            textVille.setText(Magasin.getCurMag().getVille());
            textNom.setText(Magasin.getCurMag().getNom());
        }
    }
}
