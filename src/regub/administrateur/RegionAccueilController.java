package regub.administrateur;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
    private Button ModifierRegion;
    
    @FXML
    private Button SupprimerRegion;
    
    @FXML
    private Button AjouterRegion;
    @FXML
    private UserBarController  usermenuController;
    

    //private ObservableList<String> regionData = FXCollections.observableArrayList();
    
    @FXML
    private ListView<String> listeregion;
    
    private HashMap<String, Integer> regionData;
    
    @FXML
    private ResultSet rsRegion;
    
    public static int select_region_id;
    public static String select_region;
    @FXML
    private void RegionAjouter(ActionEvent event) {
        select_region_id=0;
        select_region=null;
        getApp().gotoPage("administrateur/RegionAJMOSU");
    }

    @FXML
    private void RegionModifier(ActionEvent event) {
        
        select_region_id=regionData.get(listeregion.getSelectionModel().getSelectedItem());
        select_region=listeregion.getSelectionModel().getSelectedItem();
        System.out.println(regionData.get(listeregion.getSelectionModel().getSelectedItem()));
        getApp().gotoPage("administrateur/RegionAJMOSU");
    }
    
    @FXML
    private void RegionSupprimer(ActionEvent event) {
        
        select_region_id=regionData.get(listeregion.getSelectionModel().getSelectedItem());
        select_region=listeregion.getSelectionModel().getSelectedItem();     
    
        String sql;
        
        sql = "DELETE FROM Region WHERE idRegion="+select_region_id+" ";
        try (Connection cn = Auth.getConnection();
                PreparedStatement st1 = cn.prepareStatement(sql)) {
            
            st1.execute();
           
            } catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Vous ne pouvez pas supprimer cette région !! ", ButtonType.OK);
            a.showAndWait();
            }
        getApp().gotoPage("administrateur/RegionAccueil");
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
     private void gestionRegionButton() {
         
         ModifierRegion.setDisable(true);
        SupprimerRegion.setDisable(true);
        
        listeregion.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener) (c) -> {
                    listeregion.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                    int nbSelection = listeregion.getSelectionModel().getSelectedItems().size();
                    System.out.println(nbSelection);
                    if (nbSelection == 1) {
                        ModifierRegion.setDisable(false);
                        SupprimerRegion.setDisable(false);
                        /*if(magasin_ou_pas()){
                            SupprimerRegion.setDisable(false);
                        }else{
                            SupprimerRegion.setDisable(true);
                        }*/
                        
                    } else if (nbSelection > 1) {
                        ModifierRegion.setDisable(true);
                        SupprimerRegion.setDisable(false);
                    } else if (nbSelection == 0) {
                        ModifierRegion.setDisable(true);
                        SupprimerRegion.setDisable(true);

                    }
                });
    }
    
    @Override
    public void setApp(Main m) {
        super.setApp(m);
        usermenuController.setApp(m);
        //listeregion.setItems(getRegionData());
    }
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        gestionRegionButton() ;
        getRegionData();
    }
}
