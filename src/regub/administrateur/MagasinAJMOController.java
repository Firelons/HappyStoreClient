/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub.administrateur;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import regub.AbstractController;
import regub.Auth;
import regub.Main;
import regub.commercial.Client;
import regub.util.UserBarController;

/**
 * FXML Controller class
 *
 * @author BREGMESTRE
 */
public class MagasinAJMOController  extends AbstractController {

    @FXML
    private TextField textNom;
    @FXML
    private TextField textEmail;
    @FXML
    private TextField textRue;
    @FXML
    private TextField textCodePostal;
    @FXML
    private TextField textVille;
    @FXML
    private ListView listTypeRayon;
    @FXML
    private Label Message;
    @FXML
    private UserBarController usermenuController;
    
        private boolean Verifier_Saisie() throws IOException {

        String message_error = "";
        Boolean retour = true;

        if (textNom.getText().length() == 0) {
            message_error = "Entrez le nom du magasin";
            retour = false;
        }

        if (!retour) {
            Alert a = new Alert(Alert.AlertType.WARNING, message_error, ButtonType.OK);
            a.showAndWait();
            Message.setText(message_error);
        }
        
        if (textEmail.getText().length() == 0) {
            message_error = "Entrez l'email";
            retour = false;
        }

        if (!retour) {
            Alert a = new Alert(Alert.AlertType.WARNING, message_error, ButtonType.OK);
            a.showAndWait();
            Message.setText(message_error);
        }
        
        if (textRue.getText().length() == 0) {
            message_error = "Entrez la rue";
            retour = false;
        }

        if (!retour) {
            Alert a = new Alert(Alert.AlertType.WARNING, message_error, ButtonType.OK);
            a.showAndWait();
            Message.setText(message_error);
        }

        return retour;

    }

    @FXML
    private void Save_Magasin() throws IOException {

        System.out.println(Auth.getUserInfo().toString());
       
        String operation = "";
        String sql;
        boolean update = false;
        
            sql = "INSERT INTO `magasin`(`idMagasin`, `nom`, `addr_ligne1`, `addr_ligne2`, `code_postal`, `idRegion`, `ville`) VALUES (??,??,??,??,??,??,??);";
            
       

        try (Connection cn = Auth.getConnection();
                PreparedStatement st1 = cn.prepareStatement(sql)) {
            
            st1.setString(1, textNom.getText());
            st1.setString(2, textEmail.getText());
            st1.setString(3, textRue.getText());
            st1.setString(4, textCodePostal.getText());
            st1.setString(5, textVille.getText());
            
            if(update){
                 st1.setInt(7, Client.getCurClient().getId());
            }
            st1.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Valider_Region(ActionEvent event) throws IOException {
        if (Verifier_Saisie()) {
            Save_Region();
            getApp().gotoPage("administrateur/RegionAccueil");
        }

    }

    @Override
    public void setApp(Main m) {
        super.setApp(m);
        usermenuController.setApp(m);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
