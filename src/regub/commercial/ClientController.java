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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import regub.AbstractController;

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
        String url = "jdbc:mysql://localhost/regub";
        String login="com";
        String passwd ="com";
        Connection cn=null;
        Statement st=null;
        int Num=4;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            cn = DriverManager.getConnection(url, login, passwd);
            st=cn.createStatement();
            
            String sql ="INSERT INTO client(idClient,societe,telephone,email,addr_ligne1,addr_ligne2,ville,code_postal)"
                    + "VALUES (5,'"+this.textSociete.getText() +"','"+this.textTelephone.getText() +"','"+this.textEmail.getText() +"','"+this.textRue.getText() +"','','"+this.textVille.getText() +"',"+this.textBP.getText() +")";
            st.executeUpdate(sql);
        } catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }finally{
            try{
                cn.close();
                st.close();
            }catch (SQLException e){
            e.printStackTrace();
        }
        }
        
       
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}
