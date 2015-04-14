/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub;

import java.net.URL;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author antoine
 */
public class LoginController extends AbstractController {

    @FXML
    private TextField loginField;

    @FXML
    private TextField psswdField;

    @FXML
    private Label errField;

    @FXML
    public void handleActionLogin(ActionEvent ev) throws ClassNotFoundException, SQLException {
        errField.setText("");
        if (loginField.getText().length() == 0) {
            errField.setText("login vide");
            return;
        }
        try {
            Auth.connect(loginField.getText(), psswdField.getText());
        } catch (SQLException | ClassNotFoundException ex) {
            errField.setText("connexion impossible");
            ex.printStackTrace();
            return;
        } catch (NoSuchElementException ex) {
            errField.setText("mauvais login ou mot de passe");
            return;
        }
        String type = (String) Auth.getUserInfo().get("type");
        System.out.println("login success type : "+type);
        String classname = type+"/Accueil" + type.substring(0, 1).toUpperCase() + type.substring(1) ;
        System.out.println("chargement : "+classname);
        getApp().gotoPage(classname);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
