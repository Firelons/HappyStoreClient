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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.StringProperty;
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
import javafx.scene.control.ToggleGroup;
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
    private int freq;
    private int dur;
    private double tar;
    private int nombresRayons;
    private int nombresRegions;
    private double EcartDebutFin;
    //variables de gestion de bases
    private final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    //variables pour l'eregistrement dans la base
    private int mont;
    private int dval;
    private int statut;
    //variable d'utilisation pour recupperer les données du cient. dans ce cas si juste le nom du client en question
    private final Client cli = new Client();

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
    private RadioButton annule;
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
    private UserBarController usermenuController;

    @FXML
    private ToggleGroup etatContrat;

    private HashMap<String, Integer> RegionData;
    private HashMap<String, Integer> RayonData;
    private ObservableList<String> RayonsSelect; //rayons selectionés
    private ObservableList<String> RegionsSelect;//regions selectionés

    @FXML
    private void Annuler(ActionEvent event) throws IOException {
        getApp().gotoPage("commercial/AccueilCommercial");
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
    private void Enregistrer(ActionEvent event) throws IOException, ParseException, SQLException {
        if (Verifier_Saisie()) {
            Save_Contrat();
            getApp().gotoPage("commercial/AccueilCommercial");
        }
    }

    @FXML
    private void calculer(ActionEvent event) throws Exception {
        double nombrejours = 0 ;
        try {
            Date datdeb = sf.parse(datedebut.getValue().toString());
            Date datfin = sf.parse(datefin.getValue().toString());
            EcartDebutFin = (datfin.getTime() - datdeb.getTime()) / (1000 * 86400);
            nombrejours = EcartDebutFin - (int) (EcartDebutFin / 7) + 1;
        } catch (NullPointerException nfe) {
        }
        //ecart en semaine auquel on retire les dimanches : nous donne le nombre de jours de diffusion
      
        try {
            tar = Double.parseDouble(tarif.getText());
        } catch (NumberFormatException nfe) {
        }

        try {
            freq = Integer.parseInt(frequence.getText());

        } catch (NumberFormatException nfe) {
        }

        try {
            dur = Integer.parseInt(duree.getText());
        } catch (NumberFormatException nfe) {
        }

        

        montant.setText(String.valueOf(freq * dur * tar * nombresRayons * nombresRegions * nombrejours));

    }

    @Override
    public void setApp(Main main) {
        super.setApp(main);
        usermenuController.setApp(main);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            RayonData = getliste("typerayon");
            Rayons.setItems(FXCollections.observableArrayList(RayonData.keySet()));
        } catch (IOException ex) {
            Logger.getLogger(ContratController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Rayons.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        try {
            RegionData = getliste("region");
            Regions.setItems(FXCollections.observableArrayList(RegionData.keySet()));
        } catch (IOException ex) {
            Logger.getLogger(ContratController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Regions.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        datereception.setValue(LocalDate.now());
        datevalidation.setValue(LocalDate.now());

        client.setText(Client.getCurClient().getSociete());
        Rayons.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener) (c) -> {
                    nombresRayons = Rayons.getSelectionModel().getSelectedItems().size();
                });
        Regions.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener) (c) -> {
                    nombresRegions = Regions.getSelectionModel().getSelectedItems().size();
                });
        tarif.textProperty().addListener((str) -> {
            try {
                tar = Double.parseDouble(((StringProperty) str).getValue());
                tarif.setStyle("");

            } catch (NumberFormatException ex) {
                tar = 0.0;
                tarif.setStyle("-fx-background-color: #FFB2B4;");
            }
        });
        duree.textProperty().addListener((str) -> {
            try {
                dur = Integer.parseInt(((StringProperty) str).getValue());
                duree.setStyle("");
            } catch (NumberFormatException nfe) {
                dur = 0;
                duree.setStyle("-fx-background-color: #FFB2B4;");
            }
        });
        frequence.textProperty().addListener((str) -> {
            try {
                freq = Integer.parseInt(((StringProperty) str).getValue());
                frequence.setStyle("");

            } catch (NumberFormatException nfe) {
                freq = 0;
                frequence.setStyle("-fx-background-color: #FFB2B4;");
            }
        });
    }

    private boolean Verifier_Saisie() throws IOException {
        String message_error = "";
        Boolean retour = true;

        if (Rayons.getSelectionModel().getSelectedItems().size() == 0) {
            message_error = "Entrez les rayons";
            retour = false;
        }
        if (Regions.getSelectionModel().getSelectedItems().size() == 0) {
            message_error = "Entrez les regions";
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
            dval = datevalidation.getValue().toString().length();
        } catch (NullPointerException nfe) {
            message_error = "Date de validation invalide";
            retour = false;
        }

        try {
            if (datereception.getValue().compareTo(datedebut.getValue()) > 0) {
                message_error = "La date de debut est anterieure a la date de reception";
                retour = false;
            }
        } catch (NullPointerException nfe) {
            message_error = "Date de reception ou validation invalide";
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
        try {
            if (datedebut.getValue().compareTo(datefin.getValue()) > 0) {
                message_error = "La date de fin est anterieure a la date de debut";
                retour = false;
            }
        } catch (NullPointerException nfe) {
            message_error = "Date debut ou date fin invalide";
            retour = false;
        }

        /*if (fich.getText().length() == 0) {
         message_error = "Entrez le fichier mp4";
         retour = false;
         }*/
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

        if (!retour) {
            Alert a = new Alert(Alert.AlertType.WARNING, message_error, ButtonType.OK);
            a.showAndWait();
            this.Message.setText(message_error);
        }
        return retour;

    }

    private HashMap<String, Integer> getliste(String Table) throws IOException {
        System.out.println(Auth.getUserInfo().toString());
        HashMap<String, Integer> resuMap = new HashMap<>();
        try (Connection cn = Auth.getConnection();
                Statement st = cn.createStatement()) {
            String sql = "SELECT * FROM " + Table + " ORDER BY libelle ASC";
            ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                resuMap.put(res.getString("libelle"), res.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resuMap;
    }

    private void Save_Contrat() throws IOException, ParseException, SQLException {
        if (valide.isSelected()) {
            statut = 1;
        } else if (preparation.isSelected()) {
            statut = 2;
        } else if (annule.isSelected()) {
            statut = 3;
        }
        int videoID;
        System.out.println(Auth.getUserInfo().toString());
        try (Connection cn = Auth.getConnection()) {
            String sql1 = "INSERT INTO video(titre,frequence,duree,dateDebut,dateFin,dateReception,dateValidation,tarif,statut,idCommercial,idClient)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?);";
            try (PreparedStatement st1 = cn.prepareStatement(sql1);) {
                st1.setString(1, titre.getText());
                st1.setInt(2, freq);
                st1.setInt(3, dur);
                st1.setDate(4, java.sql.Date.valueOf(datedebut.getValue().toString()));
                st1.setDate(5, java.sql.Date.valueOf(datefin.getValue().toString()));
                st1.setDate(6, java.sql.Date.valueOf(datereception.getValue().toString()));
                st1.setDate(7, java.sql.Date.valueOf(datevalidation.getValue().toString()));
                st1.setDouble(8, tar);
                st1.setInt(9, statut);
                st1.setString(10, (String) Auth.getUserInfo().get("id"));
                st1.setInt(11, Client.getCurClient().getId());
                st1.execute();
                ResultSet res = st1.getGeneratedKeys();
                res.next();
                videoID = res.getInt("idVideo");
                res.close();
                System.out.println(sql1);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }

            System.out.println("Enregistrement des rayons pour le contrat : " + titre.getText());
            RayonsSelect = Rayons.getSelectionModel().getSelectedItems();
            for (String str : RayonsSelect) {
                String sql = "INSERT INTO diffusionstypesrayons (idVideo,idRayon) VALUES (?,?);";
                try (PreparedStatement st = cn.prepareStatement(sql)) {
                    st.setInt(1, videoID);
                    st.setInt(2, RayonData.get(str));
                    st.executeQuery();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            System.out.println("Enregistrement des regions pour le contrat : " + titre.getText());
            RegionsSelect = Regions.getSelectionModel().getSelectedItems();
            for (String str : RegionsSelect) {
                String sql = "INSERT INTO `diffusionregions` (`idRegion`,`idVideo`) VALUES (?,?);";
                try (PreparedStatement st = cn.prepareStatement(sql)) {
                    st.setInt(2, RegionData.get(str));
                    st.setInt(1, videoID);
                    st.executeQuery();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

}
