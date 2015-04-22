package regub.administrateur;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import regub.AbstractController;

public class RegionAccueilController extends AbstractController{
        
	
	@FXML
    private TextField NomRegion ;
	
	@FXML
           private void RegionAjouter(ActionEvent event) {
            getApp().gotoPage("administrateur/RegionAJMOSU");
        }
           
	@FXML
           private void RegionModifier(ActionEvent event) {
            getApp().gotoPage("administrateur/RegionAJMOSU");
        }
        
        

	@Override
	public void initialize(URL location, ResourceBundle resources) {
            
	}
}
	
	



