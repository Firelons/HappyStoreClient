/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub.administrateur;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import regub.AbstractController;
import regub.Main;
import regub.util.UserBarController;

/**
 * FXML Controller class
 *
 * @author BREGMESTRE
 */
public class CompteAJMOController extends AbstractController {

    @FXML
    private UserBarController usermenuController;

    @FXML
    private TextField tfLogin; 
    @FXML
    private TextField tfPrenom; 
    @FXML
    private TextField tfNom; 
    @FXML
    private PasswordField pwfPassWord; 
    
    @Override
    public void setApp(Main m) {
        super.setApp(m);
        usermenuController.setApp(m);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
