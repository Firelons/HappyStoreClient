/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package commercial;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import static javafx.application.Application.launch;
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
 * @author Lons
 */


public class COMMERCIAL_ContratController implements Initializable {

    @FXML
    private void Annuler(ActionEvent event) throws IOException {
        Parent PageAccueilParent = FXMLLoader.load(getClass().getResource("COMMERCIAL_Accueil.fxml"));
        Scene PageAccueilScene = new Scene(PageAccueilParent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        //stage.hide();
        stage.setScene(PageAccueilScene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}
