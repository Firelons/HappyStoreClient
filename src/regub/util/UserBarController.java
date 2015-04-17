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
import javafx.scene.control.Label;
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

    @FXML
    public void handleLogout(ActionEvent ev) {
        Auth.disconnect();
        getApp().gotoPage("login");
    }

    @FXML
    public void handleSetting(ActionEvent ev) throws Exception {
        throw new Exception("non implémenté");
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
        if(!Auth.isConnected()){
            getApp().gotoPage("login");
        }
        String login = (String) Auth.getUserInfo().get("login");
        lbl_userlogin.setText(login);
    }

}
