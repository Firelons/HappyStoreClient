/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub.util;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import regub.Auth;

/**
 *
 * @author antoine
 */
public class UserSettingsController implements Initializable {

    @FXML
    private PasswordField oldPass;
    @FXML
    private PasswordField newPass;
    @FXML
    private PasswordField confPass;
    @FXML
    private Label lblnom;
    @FXML
    private Label lblprenom;
    @FXML
    private Label lbllogin;

    @FXML
    public void handleChPassword(ActionEvent ev) {
        String newpw = newPass.getText();
        String oldpw = oldPass.getText();
        String confpw = confPass.getText();
        if (newpw.length() <= 0 && oldpw.length() <= 0 && confpw.length() <= 0) {
            close();
            return;
        }

        Alert a = new Alert(
                Alert.AlertType.ERROR,
                null,
                ButtonType.OK);
        if (newpw.length() <= 0) {
            a.setContentText("le mot de passe ne doit pas être vide");
            a.showAndWait();
            return;
        }
        if (confpw.compareTo(newpw) != 0) {
            a.setContentText("le mot de passe et sa vérification sont différent");
            a.showAndWait();
            return;
        }
        try {
            boolean ok = Auth.changePassword(oldpw, newpw);
            if (ok) {
                a.setContentText("votre mot de passe à été mis à jour");
                a.setAlertType(Alert.AlertType.CONFIRMATION);
            } else {
                a.setContentText("mauvais mot de passe");
            }
            a.showAndWait();
        } catch (SQLException ex) {
            ex.printStackTrace();
            a.setContentText("connexion impossible");
            a.showAndWait();
            return;
        }
        close();
    }

    @FXML
    public void closeWin(ActionEvent ev) {
        close();
    }

    public void close() {
        ((Stage) (oldPass.getScene().getWindow())).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lbllogin.setText((String) Auth.getUserInfo().get("login"));
        lblnom.setText((String) Auth.getUserInfo().get("nom"));
        lblprenom.setText((String) Auth.getUserInfo().get("prenom"));
    }

}
