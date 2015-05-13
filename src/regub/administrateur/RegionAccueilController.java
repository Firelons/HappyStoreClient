package regub.administrateur;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import regub.AbstractController;
import regub.Auth;
import regub.Main;
import regub.util.UserBarController;

public class RegionAccueilController extends AbstractController {

    @FXML
    private TextField NomRegion;

    @FXML
    private UserBarController  usermenuController;
    
    @FXML
    private ObservableList<String> regionData = FXCollections.observableArrayList();
    
    @FXML
    private ListView<String> listeregion;
    
    @FXML
    private ResultSet rsRegion;

    @FXML
    private void RegionAjouter(ActionEvent event) {
        getApp().gotoPage("administrateur/RegionAJMOSU");
    }

    @FXML
    private void RegionModifier(ActionEvent event) {
        getApp().gotoPage("administrateur/RegionAJMOSU");
    }

         @FXML
    private void getRegionDB() throws IOException {
        
        System.out.println(Auth.getUserInfo().toString());

        try (Connection cn = Auth.getConnection();
                Statement st = cn.createStatement()) {
            String sql = "SELECT * FROM Region";

            rsRegion=st.executeQuery(sql);
            while(rsRegion.next()){
                regionData.add(new String(rsRegion.getString("libelle")));
               
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
          
    }
    public ObservableList<String> getRegionData()  {
        
        try {
            this.getRegionDB();
        } catch (IOException ex) {
             ex.printStackTrace();
        }
         regionData.add(new String("4")) ;
         return regionData;
    }
    
    
    @Override
    public void setApp(Main m) {
        super.setApp(m);
        usermenuController.setApp(m);
        listeregion.setItems(getRegionData());
    }
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
