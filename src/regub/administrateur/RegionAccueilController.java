package regub.administrateur;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import regub.AbstractController;
import regub.Auth;
import regub.Main;
import regub.commercial.ContratController;
import regub.util.UserBarController;

public class RegionAccueilController extends AbstractController {

    @FXML
    private TextField NomRegion;

    @FXML
    private UserBarController  usermenuController;
    
    
    //private ObservableList<String> regionData = FXCollections.observableArrayList();
    
    @FXML
    private ListView<String> listeregion;
    
    private HashMap<String, Integer> regionData;
    
    @FXML
    private ResultSet rsRegion;

    @FXML
    private void RegionAjouter(ActionEvent event) {
        getApp().gotoPage("administrateur/RegionAJMOSU");
    }

    @FXML
    private void RegionModifier(ActionEvent event) {
        int select_region;
        select_region=regionData.get(listeregion.getSelectionModel().getSelectedItem());
        System.out.println(regionData.get(listeregion.getSelectionModel().getSelectedItem()));
        
        getApp().gotoPage("administrateur/RegionAJMOSU");
    }

         
   /* private void getRegionDB() throws IOException {
        
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
          
    } */
       @FXML  
    public void getRegionData()  {
        
        try {
            regionData = getliste("Region");
            listeregion.setItems(FXCollections.observableArrayList(regionData.keySet()));
        } catch (IOException ex) {
            Logger.getLogger(ContratController.class.getName()).log(Level.SEVERE, null, ex);
        }
        listeregion.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }
    private HashMap<String, Integer> getliste(String Table) throws IOException {
        System.out.println(Auth.getUserInfo().toString());
        HashMap<String, Integer> resuMap = new HashMap<>();
        ResultSet res = null;
        try (Connection cn = Auth.getConnection();
                Statement st = cn.createStatement()) {
            String sql = "SELECT * FROM " + Table + " ORDER BY libelle ASC";
            res = st.executeQuery(sql);
            while (res.next()) {
                resuMap.put(res.getString("libelle"), res.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (res != null) {
                try {
                    res.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return resuMap;
    }
    
    @Override
    public void setApp(Main m) {
        super.setApp(m);
        usermenuController.setApp(m);
        //listeregion.setItems(getRegionData());
    }
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getRegionData();
    }
}
