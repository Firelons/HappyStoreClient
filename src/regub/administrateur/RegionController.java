/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub.administrateur;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import regub.AbstractController;
import regub.Main;
import regub.util.UserBarController;

/**
 *
 * @author Souad
 */
public class RegionController extends AbstractController {

    @FXML
    private UserBarController usermenuController;

    @Override
    public void setApp(Main m) {
        super.setApp(m);
        usermenuController.setApp(m);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
