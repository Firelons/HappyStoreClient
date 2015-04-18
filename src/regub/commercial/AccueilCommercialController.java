/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub.commercial;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import regub.AbstractController;
import regub.Auth;
import regub.Main;
import regub.util.UserBarController;

/**
 *
 * @author Mesmerus
 */
public class AccueilCommercialController extends AbstractController {
     
    @FXML
    private ResultSet rsClient;//Récupère la liste des clients dans la base de donées
    
    @FXML
    private UserBarController usermenuController;

    @FXML
    private void AjouterContrat(ActionEvent event) throws IOException {
        getApp().gotoPage("commercial/Contrat");
    }
    
    @FXML
    private void getClientDB() throws IOException {
        System.out.println(Auth.getUserInfo().toString());

        try (Connection cn = Auth.getConnection();
                Statement st = cn.createStatement()) {
            String sql = "SELECT * FROM Client";

            rsClient=st.executeQuery(sql);
            while(rsClient.next()){
                System.out.println(rsClient.getString("societe"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void AjouterClient(ActionEvent event) throws IOException {
       
        getApp().gotoPage("commercial/Client");
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
