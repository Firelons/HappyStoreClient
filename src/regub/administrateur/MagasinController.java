/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub.administrateur;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javax.swing.text.TableView;
import regub.AbstractController;

/**
 *
 * @author antoine
 */
public class MagasinController extends AbstractController {

    @FXML
    TableView tableMagasin;

    @FXML
    public void handleAjout(ActionEvent ev) {
        System.err.println("à implémenter");
    }

    @FXML
    public void handleSuppr(ActionEvent ev) {
        System.err.println("à implémenter");
    }

    @FXML
    public void handleModif(ActionEvent ev) {
        System.err.println("à implémenter");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
