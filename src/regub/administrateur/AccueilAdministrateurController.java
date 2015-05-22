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
    private void AccueilAdministrateurCompteUtil(ActionEvent event) {
        getApp().gotoPage("administrateur/CompteUtil");
    }
    
    @FXML
    private void AccueilAdministrateurRegion(ActionEvent event) {
        getApp().gotoPage("administrateur/RegionAccueil");
    }
    
    @FXML
    private void AccueilAdministrateurMagasins(ActionEvent event) {
        getApp().gotoPage("administrateur/Magasins");
    }
    
    @FXML
    private void AccueilAdministrateurTypeRayon(ActionEvent event) {
        getApp().gotoPage("administrateur/TypeRayon");
    }
    @FXML
    private void AccueilCommercial(ActionEvent event) {
        getApp().gotoPage("commercial/AccueilCommercial");
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
