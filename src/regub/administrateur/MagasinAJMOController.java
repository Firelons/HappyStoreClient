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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
public class MagasinAJMOController  extends AbstractController {

    @FXML
    private TextField textNom;
    @FXML
    private TextField textEmail;
    @FXML
    private TextField textRue;
    @FXML
    private TextField textCodePostal;
    @FXML
    private TextField textVille;
    @FXML
    private ListView listTypeRayon;
    @FXML
    private Label Message;
    @FXML
    private UserBarController usermenuController;
    
        private boolean Verifier_Saisie() throws IOException {

        String message_error = "";
        Boolean retour = true;

        if (textNom.getText().length() == 0) {
            message_error = "Entrez le nom du magasin";
            retour = false;
        }

        if (!retour) {
            Alert a = new Alert(Alert.AlertType.WARNING, message_error, ButtonType.OK);
            a.showAndWait();
            Message.setText(message_error);
        }
        
        if (textEmail.getText().length() == 0) {
            message_error = "Entrez l'email";
            retour = false;
        }

        if (!retour) {
            Alert a = new Alert(Alert.AlertType.WARNING, message_error, ButtonType.OK);
            a.showAndWait();
            Message.setText(message_error);
        }
        
        if (textRue.getText().length() == 0) {
            message_error = "Entrez la rue";
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

    @Override
    public void setApp(Main m) {
        super.setApp(m);
        usermenuController.setApp(m);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
