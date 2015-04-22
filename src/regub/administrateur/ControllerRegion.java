package regub.administrateur;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import regub.Auth;

public class ControllerRegion implements Initializable{
        
	
	@FXML
    private TextField NomRegion ;
	
	@FXML
	private void AjouterRegion(ActionEvent event) {
		try{
                    
        Parent PageAccueilParent = FXMLLoader.load(getClass().getResource("PaneRegion1.fxml"));
        Scene PageAccueilScene = new Scene(PageAccueilParent);
        Node node= (Node)event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setScene(PageAccueilScene);
        stage.show();
		}catch(Exception ex){
			System.err.println(ex.getMessage());
		}
	}
	
	
	@FXML
	private void ValiderRegion(){
            
              System.out.println(Auth.getUserInfo().toString());

        try (Connection cn = Auth.getConnection();
                Statement st = cn.createStatement()) {
            String sql = "INSERT INTO region(libelle)"
                    + "VALUES (?);";
            PreparedStatement st1 = cn.prepareStatement(sql);
            st1.setString(1, this.NomRegion.getText());
           

            st1.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
            
	}
}
	
	



