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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import regub.AbstractController;
import regub.Auth;
import regub.Main;
import regub.util.UserBarController;

/**
 *
 * @author Mesmerus
 */
public class ClientController extends AbstractController {

    
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
    
    private Client person;

    @FXML
    private UserBarController usermenuController;

    @FXML
    private void Enregistrer(ActionEvent event) throws IOException {
        if (Verifier_Saisie()) {
            Save_Client();
            getApp().gotoPage("commercial/AccueilCommercial");
        }

    }
    public void setClient(Client person) {
        this.person = person;

        textSociete.setText(person.getSociete());
        textTelephone.setText(person.getTelephone());
        textEmail.setText(person.getEmail());
        textRue.setText(person.getRue());
        textVille.setText(person.getVille());
        textBP.setText(person.getPostalCode());
        
    }
    @FXML
    private void Annuler(ActionEvent event) throws IOException {
        getApp().gotoPage("commercial/AccueilCommercial");
    }

    private boolean Verifier_Saisie() throws IOException {
        String message_error = "";
        Boolean retour = true;

        try {
            int monentier = Integer.parseInt(textTelephone.getText());

        } catch (NumberFormatException nfe) {
            message_error = "Numéro de Téléphone Invalide";
            retour = false;

        }
        try {
            int monentier1 = Integer.parseInt(textBP.getText());

        } catch (NumberFormatException nfe) {
            message_error = "Boite Postale Invalide";
            retour = false;
        }
        boolean essai = Pattern.matches("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)+$", textEmail.getText());
        if (essai == false) {
            message_error = "Adresse Mail Invalide";
            retour = false;
        }
        if (textBP.getText().length() == 0) {
            message_error = "Entrez le Code Postal";
            retour = false;
        }
        if (textVille.getText().length() == 0) {
            message_error = "Entrez la Ville";
            retour = false;
        }
        if (textRue.getText().length() == 0) {
            message_error = "Entrez l'adresse";
            retour = false;
        }
        if (textEmail.getText().length() == 0) {
            message_error = "Entrez l'email";
            retour = false;
        }
        if (textTelephone.getText().length() == 0) {
            message_error = "Entrez le numéro de Téléphone";
            retour = false;
        }

        if (textSociete.getText().length() == 0) {
            message_error = "Entrez le nom de la societé";
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
    private void Save_Client() throws IOException {
        System.out.println(Auth.getUserInfo().toString());

        try (Connection cn = Auth.getConnection();
                Statement st = cn.createStatement()) {
            String sql = "INSERT INTO Client(societe,telephone,email,addr_ligne1,ville,code_postal)"
                    + "VALUES (?,?,?,?,?,?);";
            PreparedStatement st1 = cn.prepareStatement(sql);
            st1.setString(1, textSociete.getText());
            st1.setString(2, textTelephone.getText());
            st1.setString(3, textEmail.getText());
            st1.setString(4, textRue.getText());
            st1.setString(5, textVille.getText());
            st1.setString(6, textBP.getText());

            st1.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setApp(Main main) {
        super.setApp(main);
        usermenuController.setApp(main);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textSociete.setText(Client.getCurClient().getSociete());
        //fais toute tes ajouts ici et a la fin on refais un null pour remetre le client courant a null !
        Client.setCurClient(null);
    }
}
