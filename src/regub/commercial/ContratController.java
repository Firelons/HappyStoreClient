/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub.commercial;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import regub.AbstractController;
import regub.Auth;
import regub.Main;
import regub.util.UserBarController;

/**
 * FXML Controller class
 *
 * @author Mesmerus
 */
public class ContratController extends AbstractController {

    private File fichier;
    private double freq;
    private double dur;
    private double tar;
    private int nombresRayons;
    private int nombresRegions;

    @FXML
    private Label client;
    @FXML
    private TextField titre;
    @FXML
    private TextField frequence;
    @FXML
    private TextField duree;
    @FXML
    private TextField fich;
    @FXML
    private DatePicker datedebut;
    @FXML
    private DatePicker datefin;
    @FXML
    private DatePicker datereception;
    @FXML
    private RadioButton valide;
    @FXML
    private RadioButton preparation;
    @FXML
    private RadioButton Annule;
    @FXML
    private DatePicker datevalidation;
    @FXML
    private TextField tarif;
    @FXML
    private TextField montant;
    @FXML
    private ListView Rayons;
    @FXML
    private ListView Regions;
    @FXML
    private Label Message;
    @FXML
    private ResultSet rsClient;//Récupère la liste des Regions ou des  dans la base de donées
    @FXML
    private UserBarController usermenuController;
    private ObservableList<String> RegionData = FXCollections.observableArrayList();
    private ObservableList<String> RayonData = FXCollections.observableArrayList();
    @FXML
    private void Annuler(ActionEvent event) throws IOException {
        //  getApp().gotoPage("commercial/AccueilCommercial");
        ObservableList<String> data = Rayons.getSelectionModel().getSelectedItems();

        for (Iterator iterator = data.iterator(); iterator.hasNext();) {
            System.out.println(iterator.next());
        }

        if (Rayons.getSelectionModel().getSelectedItems().size() == 0) {
            String message_error = "Entrez les rayons";
            Alert a = new Alert(Alert.AlertType.WARNING, message_error, ButtonType.OK);
            a.showAndWait();
            boolean retour = false;
        }
        if (Regions.getSelectionModel().getSelectedItems().size() == 0) {
            String message_error = "Entrez les regions";
            Alert a = new Alert(Alert.AlertType.WARNING, message_error, ButtonType.OK);
            a.showAndWait();
            boolean retour = false;
        }
    }

    @FXML
    private void browse(ActionEvent event) {

        final FileChooser dialog = new FileChooser();
        dialog.setTitle("Choisir un fichier mp4");
        dialog.showOpenDialog(getstage());
        if (fichier != null) {
            // Effectuer la sauvegarde. 

        }
    }

    @FXML
    private void Enregistrer(ActionEvent event) throws IOException {
        Verifier_Saisie();
        Save_Client();
    }

    @FXML
    private void calculer(ActionEvent event) throws Exception {
        String message_error = "";
        Boolean retour = true;
        try {
            tar = Double.parseDouble(tarif.getText());

        } catch (NumberFormatException nfe) {
        }
        try {
            freq = Double.parseDouble(frequence.getText());

        } catch (NumberFormatException nfe) {
        }
        try {
            dur = Double.parseDouble(duree.getText());

        } catch (NumberFormatException nfe) {
        }
        Rayons.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener) (c) -> {

                    nombresRayons = Rayons.getSelectionModel().getSelectedItems().size();

                });
        Regions.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener) (c) -> {

                    nombresRegions = Regions.getSelectionModel().getSelectedItems().size();

                });
        montant.setText(String.valueOf(freq * dur * tar * nombresRayons * nombresRegions));

    }

    @Override
    public void setApp(Main main) {
        super.setApp(main);
        usermenuController.setApp(main);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

       
        try {
            getliste("typerayon");
        } catch (IOException ex) {
            Logger.getLogger(ContratController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Rayons.setItems(RayonData);
        Rayons.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        
        
        try {
            getliste("region");
        } catch (IOException ex) {
            Logger.getLogger(ContratController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Regions.setItems(RegionData);
        Regions.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    private void Verifier_Saisie() throws IOException {
        String message_error = "";
        Boolean retour = true;

        try {
            int mont = Integer.parseInt(tarif.getText());

        } catch (NumberFormatException nfe) {
            message_error = "Montant Invalide";
            retour = false;
        }
        if (montant.getText().length() == 0) {
            message_error = "Montant non calculé";
            retour = false;
        }

        try {
            tar = Integer.parseInt(tarif.getText());

        } catch (NumberFormatException nfe) {
            message_error = "tarif Invalide";
            retour = false;
        }
        if (tarif.getText().length() == 0) {
            message_error = "Entrez le tarif";
            retour = false;
        }

        try {
            int dval = datevalidation.getValue().toString().length();
        } catch (NullPointerException nfe) {
            message_error = "Date de validation invalide";
            retour = false;
        }

        if (!valide.isSelected() && !preparation.isSelected() && !Annule.isSelected()) {
            message_error = "Choisissez un etat";
            retour = false;
        }

        try {
            int drecep = datereception.getValue().toString().length();
        } catch (NullPointerException nfe) {
            message_error = "Date de reception invalide";
            retour = false;
        }

        try {
            int dfin = datefin.getValue().toString().length();
        } catch (NullPointerException nfe) {
            message_error = "Date fin invalide";
            retour = false;
        }
        try {
            int ddebut = datedebut.getValue().toString().length();
        } catch (NullPointerException nfe) {
            message_error = "Date debut invalide";
            retour = false;
        }

        if (fich.getText().length() == 0) {
            message_error = "Entrez le fichier mp4";
            retour = false;
        }
        try {
            dur = Integer.parseInt(duree.getText());

        } catch (NumberFormatException nfe) {
            message_error = "Duree Invalide";
            retour = false;
        }
        if (duree.getText().length() == 0) {
            message_error = "Entrez la duree";
            retour = false;
        }
        try {
            freq = Integer.parseInt(frequence.getText());

        } catch (NumberFormatException nfe) {
            message_error = "frequence Invalide";
            retour = false;
        }
        if (frequence.getText().length() == 0) {
            message_error = "Entrez la frequence";
            retour = false;
        }

        if (titre.getText().length() == 0) {
            message_error = "Entrez le titre";
            retour = false;
        }

        if (retour) {
            getApp().gotoPage("commercial/AccueilCommercial");
        } else {
            Alert a = new Alert(Alert.AlertType.WARNING, message_error, ButtonType.OK);
            a.showAndWait();
        }

    }

    private void getliste(String Table) throws IOException {
        System.out.println(Auth.getUserInfo().toString());

        try (Connection cn = Auth.getConnection();
                Statement st = cn.createStatement()) {
            String sql = "SELECT * FROM " + Table;

            rsClient = st.executeQuery(sql);
            while ( rsClient.next()) {
                  if(Table.compareTo("typerayon")==0) RayonData.add(rsClient.getString("libelle"));
                  if(Table.compareTo("region")==0) RegionData.add(rsClient.getString("libelle"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void Save_Client() throws IOException {

    }

}
