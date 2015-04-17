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
        this.Save_Client("test");
        getApp().gotoPage("commercial/AccueilCommercial");
        
    }
    @FXML
    private void Save_Client(String Client) throws IOException {
        
        System.out.println(Auth.getUserInfo().toString());
        int Num=4;
        try(Connection cn = Auth.getConnection();
            Statement st=cn.createStatement()){
            String sql ="INSERT INTO Client(societe,telephone,email,addr_ligne1,addr_ligne2,ville,code_postal)"
                    + "VALUES ('"+this.textSociete.getText() +"','"+this.textTelephone.getText() +"','"+this.textEmail.getText() +"','"+this.textRue.getText() +"','','"+this.textVille.getText() +"',"+this.textBP.getText() +")";
            st.executeUpdate(sql);
        } catch (SQLException e){
            e.printStackTrace();
        }
        
       
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}
