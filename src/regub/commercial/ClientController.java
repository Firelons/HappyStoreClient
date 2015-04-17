/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub.commercial;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import regub.AbstractController;
import regub.Auth;

/**
 *
 * @author Mesmerus
 */
public class ClientController extends AbstractController{

    
        @FXML
	 private TextField textSociete;
	 @FXML
	 private TextField textTelephone;
	 @FXML
	 private TextField textEmail;
         @FXML
	 private TextField textRue;
         @FXML
	 private TextField textVille;
         @FXML
	 private TextField textBP;
    @FXML
    private void Annuler(ActionEvent event) throws IOException {
        this.Save_Client();
        getApp().gotoPage("commercial/AccueilCommercial");        
    }
    @FXML
    private void Save_Client() throws IOException {
        System.out.println(Auth.getUserInfo().toString());
       
        try(Connection cn = Auth.getConnection();
            Statement st=cn.createStatement()){
            String sql ="INSERT INTO Client(societe,telephone,email,addr_ligne1,ville,code_postal)"
                    + "VALUES (?,?,?,?,?,?);";
            PreparedStatement st1=cn.prepareStatement(sql);
            st1.setString(1, this.textSociete.getText() );
            st1.setString(2, this.textTelephone.getText() );
            st1.setString(3, this.textEmail.getText() );
            st1.setString(4, this.textRue.getText() );
            st1.setString(5, this.textVille.getText());
            st1.setString(6, this.textBP.getText() );
            
            st1.execute();
            
        } catch (SQLException e){
            e.printStackTrace();
        }              
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
}
