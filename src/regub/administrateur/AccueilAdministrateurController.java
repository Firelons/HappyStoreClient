/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package regub.administrateur;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import regub.AbstractController;
import regub.Main;
import regub.util.UserBarController;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class AccueilAdministrateurController extends AbstractController {

    @FXML
    private UserBarController usermenuController;

    @FXML
    private void RegionAccueil(ActionEvent event) {
        getApp().gotoPage("administrateur/RegionAccueil");
    }

    @Override
    public void setApp(Main m) {
        super.setApp(m);
        usermenuController.setApp(m);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }
}
