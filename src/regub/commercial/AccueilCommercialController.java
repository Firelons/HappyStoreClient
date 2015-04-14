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

/**
 *
 * @author Mesmerus
 */
public class AccueilCommercialController extends AbstractController {

    @FXML
    private void AjouterContrat(ActionEvent event) throws IOException {
        getApp().gotoPage("commercial/Contrat");
    }

    @FXML
    private void AjouterClient(ActionEvent event) throws IOException {
        getApp().gotoPage("commercial/Client");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
