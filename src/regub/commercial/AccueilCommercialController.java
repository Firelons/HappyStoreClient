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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Mesmerus
 */
public class AccueilCommercialController implements Initializable {

    @FXML
    private void AjouterContrat(ActionEvent event) throws IOException {
        Parent PageAccueilParent = FXMLLoader.load(getClass().getResource("Contrat.fxml"));
        Scene PageAccueilScene = new Scene(PageAccueilParent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(PageAccueilScene);
        stage.show();
    }
    @FXML
    private void AjouterClient(ActionEvent event) throws IOException {
        Parent PageAccueilParent = FXMLLoader.load(getClass().getResource("Client.fxml"));
        Scene PageAccueilScene = new Scene(PageAccueilParent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(PageAccueilScene);
        stage.show();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
