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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import regub.AbstractController;
import regub.Auth;
import regub.Main;
import regub.commercial.Client;
import regub.util.UserBarController;

public class CompteUtilController extends AbstractController {
    
    
    //Proprietés pour l'utilisateur
    private final ObservableList<Utilisateur> UtilData = FXCollections.observableArrayList();
    @FXML
    private TableView<Utilisateur> UtilTable;
    @FXML
    private TableColumn<Utilisateur, String> nom;
    @FXML
    private TableColumn<Utilisateur, String> prenom;
    @FXML
    private TableColumn<Utilisateur, String> Typecmpt;
    @FXML
    private TableColumn<Utilisateur, String> login;
    
    
     //boutons de la page
    @FXML
    private Button AjouterUtil;
    @FXML
    private Button ModifierUtil;
    @FXML
    private Button SupprimerUtil;
    
    private ResultSet rsUtil;//Récupère la liste des clients dans la base de donées

    @FXML
    private UserBarController usermenuController;
    
    @FXML
    private void CompteUtilAjouter(ActionEvent event) {
        getApp().gotoPage("administrateur/CompteAJMO");
    }
    
     @FXML
    private void CompteUtilModifier(ActionEvent event) {
        
        Utilisateur.setCurUtil(UtilTable.getSelectionModel().getSelectedItem());
        
        getApp().gotoPage("administrateur/CompteAJMO");
    }
    
     @FXML
    private void CompteUtilSupprimer(ActionEvent event) {
        
       
        
        getApp().gotoPage("administrateur/CompteAJMO");
    }
    
    
     @FXML
     private void getUtilDB() throws IOException{
         
      try (Connection cn = Auth.getConnection();
                Statement st = cn.createStatement()) {
            String sql = "SELECT * FROM Compte inner join TypeCompte on Compte.idTypeCompte=TypeCompte.idTypeCompte";

            rsUtil = st.executeQuery(sql);
            System.out.println(rsUtil);
            while (rsUtil.next()) {
                UtilData.add(new Utilisateur(rsUtil.getInt("idCompte"), rsUtil.getString("nom"), rsUtil.getString("prenom"),
                rsUtil.getString("login"), rsUtil.getString("libelle") ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
     }
     public ObservableList<Utilisateur>getUtilData(){
          try {
            this.getUtilDB();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return UtilData;
        }
     
   

    @Override
    public void setApp(Main m) {
        super.setApp(m);
        usermenuController.setApp(m);
         UtilTable.setItems(getUtilData());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nom.setCellValueFactory(cellData -> cellData.getValue().getNom());
        prenom.setCellValueFactory(cellData -> cellData.getValue().getPrenom());
        Typecmpt.setCellValueFactory(cellData -> cellData.getValue().gettype_compte());
        login.setCellValueFactory(cellData -> cellData.getValue().getLogin());
     }
}
