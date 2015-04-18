/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub.commercial;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import regub.AbstractController;
import regub.Main;
import regub.util.UserBarController;

/**
 *
 * @author Mesmerus
 */
public class AccueilCommercialController extends AbstractController {
    @FXML
    private UserBarController usermenuController;

    @FXML
    private void AjouterContrat(ActionEvent event) throws IOException {
        getApp().gotoPage("commercial/Contrat");
    }

    @FXML
    private void AjouterClient(ActionEvent event) throws IOException {
        getApp().gotoPage("commercial/Client");
    }
    @Override
    public void setApp(Main main) {
        super.setApp(main);
        usermenuController.setApp(main);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
