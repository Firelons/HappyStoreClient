/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub.commercial;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import regub.AbstractController;
import regub.Main;
import regub.util.UserBarController;

/**
 * FXML Controller class
 *
 * @author Mesmerus
 */
public class ContratController extends AbstractController {
    private File fichier;
    
    @FXML
    private UserBarController usermenuController;
            
    @FXML
    private void Annuler(ActionEvent event) throws IOException {
        getApp().gotoPage("commercial/AccueilCommercial");
    }
    
    @FXML
    private void browse(ActionEvent event) {
     
        final FileChooser dialog = new FileChooser(); 
        dialog.setTitle("Choisir un fichier mp4");
        dialog.showOpenDialog(getstage());
       

        if (fichier != null) { 
            // Effectuer la sauvegarde. 
            
            
        } 
  
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
