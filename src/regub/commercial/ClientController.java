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
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
	 private Label Message;
    @FXML
    private void Enregistrer(ActionEvent event) throws IOException {
        this.Verifier_Saisie();
        this.Save_Client();
           
    }
    @FXML
    private void Annuler(ActionEvent event) throws IOException {
        this.Verifier_Saisie();
        this.Save_Client();
        getApp().gotoPage("commercial/AccueilCommercial");        
    }
    @FXML
    private void Verifier_Saisie() throws IOException {
        String message_error="";
        Boolean retour=true;
        
    try {
        int monentier = Integer.parseInt(this.textTelephone.getText());
           
    } catch (NumberFormatException nfe) {
        message_error="Numéro de Téléphone Invalide";
        retour=false;
        // traitement à faire dans ce cas
    }
    try {
        int monentier1 = Integer.parseInt(this.textBP.getText());
        
    } catch (NumberFormatException nfe) {
        message_error="Boite Postale Invalide";
        retour=false;
        // traitement à faire dans ce cas
    }
    
    boolean essai=Pattern.matches("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)+$", this.textEmail.getText());
           if(essai==false){
               message_error="Adresse Mail Invalide";
              retour=false;
           } 
    if(this.textBP.getText().length()==0){
        message_error="Entrez le Code Postal"; 
        retour=false;
    }   
    if(this.textVille.getText().length()==0){
        message_error="Entrez la Ville";  
        retour=false;
    }
    if(this.textRue.getText().length()==0){
        message_error="Entrez l'adresse";   
        retour=false;
    }
     if(this.textEmail.getText().length()==0){
        message_error="Entrez l'email";  
        retour=false;
    }
     if(this.textTelephone.getText().length()==0){
        message_error="Entrez le numéro de Téléphone";   
        retour=false;
    }
    
     if(this.textSociete.getText().length()==0){
        message_error="Entrez le nom de la societé";   
        retour=false;  
    }
    
           if(retour){
               getApp().gotoPage("commercial/AccueilCommercial"); 
           }else{
               this.Message.setText(message_error);
               
           }
           
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
