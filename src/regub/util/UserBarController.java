/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub.util;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import regub.AbstractController;
import regub.Auth;

/**
 * FXML Controller class
 *
 * @author antoine
 */
public class UserBarController extends AbstractController {

    @FXML
    Label lbl_userlogin;
    private Object event;

    @FXML
    public void handleLogout(ActionEvent ev) {
        Auth.disconnect();
        getApp().gotoPage("login");
    }

    @FXML
    public void handleSetting(ActionEvent ev) throws Exception {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(
                UserSettingsController.class.getResource("UserSettings.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Paramètres");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(
                ((Node) ev.getSource()).getScene().getWindow());
        stage.show();
    }

    @FXML
    public void handleHome(ActionEvent ev) {
        String typecmpt = (String) Auth.getUserInfo().get("type");
        String type = (String) Auth.getUserInfo().get("type");
        String classname = type + "/Accueil" + type.substring(0, 1).toUpperCase() + type.substring(1);
        getApp().gotoPage(classname);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (!Auth.isConnected()) {
            getApp().gotoPage("login");
        }
        String login = (String) Auth.getUserInfo().get("login");
        lbl_userlogin.setText(login);
    }

}
