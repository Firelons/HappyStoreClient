/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub.commercial;

import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;

import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.StringConverter;
import regub.AbstractController;
import regub.Auth;
import regub.Config;
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
    private int nb_diff;
    
    private String [][] data = new String [100][3];
    //variable d'utilisation pour recupperer les données du cient. dans ce cas si juste le nom du client en question
    private final Client cli = new Client();
    
    private double nb_jours =0;
    private Map parameters;
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
    private Button browse;
    @FXML
    private Button save;
    @FXML
    private Button devis;
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
    private Label contratoperation;
    @FXML
    private UserBarController usermenuController;
    
    private int nombremagasin;

    @FXML
    private ToggleGroup etatContrat;
    
    DefaultTableModel tableModel;

    private HashMap<String, Integer> RegionData;
    private HashMap<String, Integer> RayonData;
    private ObservableList<String> RayonsSelect; //rayons selectionés
    private ObservableList<String> RegionsSelect;//regions selectionés

    @FXML
    private void Annuler(ActionEvent event) throws IOException {
        Video.setCurVideo(null);
        getApp().gotoPage("commercial/AccueilCommercial");
    }
    
    @FXML
    private void Consulter(ActionEvent event) throws IOException {
        try {
            this.calculer(event);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        
       
        JasperPrint jasperPrint = null;
        net.sf.jasperreports.engine.JasperReport x = null; 
        TableModelData();
        try {
            x = JasperCompileManager.compileReport("reports/devis.jrxml");
            //JasperCompileManager.compileReportToFile("reports/report1.jrxml");
            jasperPrint = JasperFillManager.fillReport(x, parameters,
                    new JRTableModelDataSource(tableModel));
            //JasperViewer jasperViewer = new JasperViewer(jasperPrint);
            //jasperViewer.setVisible(true);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }
    
         private void TableModelData () {
             
             Client courant = Client.getCurClient();

         parameters = new HashMap(); 
         parameters.put("Nom", courant.getSociete());
         parameters.put("Adresse", courant.getRue());
         parameters.put("Code", courant.getPostalCode());
         parameters.put("Ville", courant.getVille());
         parameters.put("Numero", courant.getTelephone());
         parameters.put("Mail", courant.getEmail());
         parameters.put("Titre", this.titre.getText());
         parameters.put("Duree", this.duree.getText());
         parameters.put("Debut", Video.getCurVideo().getDate_debut());
         parameters.put("Fin", Video.getCurVideo().getDate_fin());
         parameters.put("Frequence", this.frequence.getText());
         parameters.put("Tarif", this.tarif.getText());
         parameters.put("Regions", Integer.toString(this.nombresRegions));
         parameters.put("Rayons", Integer.toString(this.nombresRayons));
         parameters.put("Magasins", Integer.toString(this.nombremagasin));
         parameters.put("Prix_Unitaire", Double.toString(this.tar*this.dur));
         parameters.put("Nombre_Diff", Double.toString(this.nb_jours*this.freq*this.nombremagasin));
         parameters.put("Duree_Diff", Double.toString(this.nb_jours));
         parameters.put("montant", this.montant.getText());    
         
        
        String[] columnNames = {"Nom", "Adresse", "Code"};
        String[][] data = {{"test"},{"test"},{"test"},{"test"}
            //Pour le client
        };
        
        try {
            getEntreprise();
        } catch (SQLException ex) {
             ex.printStackTrace();
        }
        if (this.nombremagasin==0) parameters.put("Info", "Aucune diffusion ne pourra être faite car aucun magasin ne comporte le rayon demandé dans les régions souhaitées "); 
        else parameters.put("Info", "");
        tableModel = new DefaultTableModel(this.data,columnNames);
    
    }

    @FXML
    private void browse(ActionEvent event) throws IOException {
        final FileChooser dialog = new FileChooser();
        dialog.setInitialDirectory(new File(System.getProperty("user.home")));
        dialog.setTitle("Choisir un fichier mp4");
        dialog.getExtensionFilters().addAll(
                new ExtensionFilter("Video Files", "*.mp4"));
        fichier = dialog.showOpenDialog(getstage());
        if (fichier != null) {
            fich.setText(fichier.getAbsolutePath());
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
    private void Modif_Montant (ActionEvent event) throws IOException {
        montant.setText(String.valueOf(0));
    }
    
    
    private void GetMagasins(ActionEvent event) throws Exception {
        
    }
    @FXML
    private void calculer(ActionEvent event) throws Exception {
        double nombrejours = 0;
        try {
//            Date datdeb = sf.parse(datedebut.getValue().toString());
//            Date datfin = sf.parse(datefin.getValue().toString());
//            EcartDebutFin = (datfin.getTime() - datdeb.getTime()) / (1000 * 86400);
//            System.out.println(EcartDebutFin);
            EcartDebutFin = datedebut.getValue().until(datefin.getValue(), ChronoUnit.DAYS);
//            System.out.println(EcartDebutFin);
//            nombrejours = EcartDebutFin - (int) (EcartDebutFin / 7) + 1;
//            System.out.println(nombrejours);
            nombrejours = EcartDebutFin - datedebut.getValue().until(datefin.getValue(), ChronoUnit.WEEKS) + 1;
//            System.out.println(nombrejours);
        } catch (NullPointerException nfe) {
        }
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
        
        //Informations sur l'entreprise 
        
        
        
        //Requete de sélection
        // Récupération des magasins concernés
        this.nombremagasin=0;
        String liste_magasin [] = new String [0];
        String test ;
        int i=0;
         try (Connection cn = Auth.getConnection()) {    
            RayonsSelect = Rayons.getSelectionModel().getSelectedItems();
            this.RegionsSelect = this.Regions.getSelectionModel().getSelectedItems();
            for (String str : RayonsSelect) {
            for (String str1 : RegionsSelect ) {
                String sql ="SELECT DISTINCT  Region.libelle, Magasin.nom, TypeRayon.libelle FROM Region,Magasin,Rayons,TypeRayon "
                + " WHERE Region.idRegion=Magasin.idRegion"
                + " AND Rayons.idMagasin=Magasin.idMagasin "
                + " AND Rayons.idTypeRayon =TypeRayon.idTypeRayon"
                + " AND Rayons.idTypeRayon = ? "
                + " AND Region.idRegion= ? "
                + "ORDER BY Region.libelle";
         
                try (PreparedStatement st = cn.prepareStatement(sql)) {
                    
                    st.setInt(1, RayonData.get(str));
                    st.setInt(2, RegionData.get(str1));
                    ResultSet rs = st.executeQuery();
                    
                    //System.out.println(sql + str);
                    while (rs.next()) {
                       // System.out.print("Colonne 1 renvoyée ");
                        for (int j=0;j<3;j++)
                         data[i][j]=rs.getString(j+1);
                        this.nombremagasin++;
                        i++;
                       System.out.println(rs.getString(1)+rs.getString(2)+rs.getString(3));
                               System.out.println(data);
                    }
                    rs.close();
                    st.close();
                    for(int k=this.nombremagasin;k<100;k++)
                    for (int j=0;j<3;j++)
                         data[k][j]="";
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            }
         }
        
         
        this.nb_jours = nombrejours;
        System.out.println(this.nb_jours);
        DecimalFormat df = new DecimalFormat ( ) ;
        df.setMaximumFractionDigits ( 2 ) ; 
        
         montant.setText(String.valueOf(df.format(freq * dur * tar * this.nb_jours*this.nombremagasin)));
    }

    @Override
    public void setApp(Main main) {
        super.setApp(main);
        usermenuController.setApp(main);
    }

    public void initialize(URL location, ResourceBundle resources) {

        try {
            RayonData = getliste("TypeRayon");
            Rayons.setItems(FXCollections.observableArrayList(RayonData.keySet()));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Rayons.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        try {
            RegionData = getliste("Region");
            Regions.setItems(FXCollections.observableArrayList(RegionData.keySet()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Regions.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        StringConverter<LocalDate> conv = new StringConverter<LocalDate>() {

            @Override
            public String toString(LocalDate object) {
                if (object != null) {
                    return DateTimeFormatter.ISO_LOCAL_DATE.format(object);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null) {
                    return LocalDate.parse(string, DateTimeFormatter.ISO_DATE);
                } else {
                    return null;
                }
            }
        };

        EventHandler<KeyEvent> datelbd = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                DatePicker d = (DatePicker) (((TextField) event.getSource()).getParent());
                if (!d.isShowing()) {
                    if (event.getCode().compareTo(KeyCode.SPACE) == 0) {
                        event.consume();
                        d.show();
                    }
                }
            }
        };
        datereception.setConverter(conv);
        datereception.getEditor().setOnKeyReleased(datelbd);
        datereception.setValue(LocalDate.now());

        datedebut.setConverter(conv);
        datedebut.getEditor().setOnKeyReleased(datelbd);

        datefin.setConverter(conv);
        datefin.getEditor().setOnKeyReleased(datelbd);

        datevalidation.setConverter(conv);
        datevalidation.getEditor().setOnKeyReleased(datelbd);
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

        if (Video.getCurVideo() != null) {
            titre.setText(Video.getCurVideo().getTitre());
            frequence.setText("" + Video.getCurVideo().getfrequence());
            duree.setText("" + Video.getCurVideo().getDuree());
            datedebut.setValue(LocalDate.parse("" + Video.getCurVideo().getDate_debut()));
            datefin.setValue(LocalDate.parse("" + Video.getCurVideo().getDate_fin()));
            datereception.setValue(LocalDate.parse("" + Video.getCurVideo().getDate_reception()));
            datevalidation.setValue(LocalDate.parse("" + Video.getCurVideo().getDate_validation()));
            tarif.setText("" + Video.getCurVideo().getTarif());
            fich.setText("Video n°" + Video.getCurVideo().getidVideo());
            contratoperation.setText("Modifier contrat");
            switch (Video.getCurVideo().getStatut()) {
                case 1:
                    valide.setSelected(true);
                    break;
                case 2:
                    preparation.setSelected(true);
                    break;
                default:
                    annule.setSelected(true);
                    break;
            }
            duree.setDisable(true);
            tarif.setDisable(true);
            browse.setDisable(true);
            if (LocalDate.now().toString().compareTo(Video.getCurVideo().getDate_debut()) > 0) {
                datedebut.setDisable(true);
            }
            if (LocalDate.now().toString().compareTo(Video.getCurVideo().getDate_fin()) > 0 || Video.getCurVideo().getStatut() == 3) {
                save.setDisable(true);
            }

            for (Map.Entry<String, Integer> entry : Video.getCurVideo().getcurTypeRayon().entrySet()) {
                Rayons.getSelectionModel().selectIndices(Rayons.getItems().indexOf("" + entry.getKey()));
            }

            for (Map.Entry<String, Integer> entry : Video.getCurVideo().getcurRegions().entrySet()) {
                Regions.getSelectionModel().selectIndices(Regions.getItems().indexOf("" + entry.getKey()));
            }

        } else {
            datereception.setValue(LocalDate.now());
            datevalidation.setValue(LocalDate.now());
            contratoperation.setText("Ajouter contrat");
        }
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
            tar = Double.parseDouble(tarif.getText());

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
        ResultSet res = null;
        String sql;
        String sql_region = " WHERE EXISTS ( SELECT * FROM Magasin WHERE Magasin.idRegion = Region.idRegion ORDER by `libelle`) ";
        String sql_rayon = " WHERE EXISTS ( SELECT * FROM Rayons WHERE TypeRayon.idTypeRayon = Rayons.idTypeRayon ORDER by `libelle`) ";

        if (Table.equals("Region")) {
            sql = "SELECT * FROM " + Table + sql_region + " ORDER by libelle ";

        } else {
            sql = "SELECT * FROM " + Table + sql_rayon +" order by `libelle`";

        }

        System.out.println(sql);
        try (Connection cn = Auth.getConnection();
                Statement st = cn.createStatement()) {

            res = st.executeQuery(sql);
            while (res.next()) {
                resuMap.put(res.getString("libelle"), res.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (res != null) {
                try {
                    res.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return resuMap;
    }

    private void  getEntreprise() throws SQLException {
        System.out.println(Auth.getUserInfo().toString());
        int count =0;
        ResultSet res = null;
        String sql;
        
        sql = "SELECT nom, adresse, code, ville, telephone, mail FROM Entreprise ";
        System.out.println(sql);
        
        try (Connection cn = Auth.getConnection();
                Statement st = cn.createStatement()) {

            res = st.executeQuery(sql);
             
            
            while (res.next()) {
               if(count==0)
                parameters.put("Enom", res.getString(1));
            
                parameters.put("Eadresse", res.getString(2));
                parameters.put("Ecode", res.getString(3));
                parameters.put("Eville", res.getString(4));
                parameters.put("Etelephone", res.getString(5));
                parameters.put("Email", res.getString(6));
             
               count++;
                System.out.println(res.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (res != null) {
                try {
                    res.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }  
    }
    
    private void Save_Contrat() throws IOException, ParseException, SQLException {
        Alert a = new Alert(
                Alert.AlertType.INFORMATION,
                null,
                ButtonType.OK);
        if (valide.isSelected()) {
            statut = 1;
            datevalidation.setValue(LocalDate.parse("" + LocalDate.now()));
        } else if (preparation.isSelected()) {
            statut = 2;
        } else if (annule.isSelected()) {
            statut = 3;
            datefin.setValue(LocalDate.parse("" + LocalDate.now()));
        }
        Rayons.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener) (c) -> {
                    nombresRayons  = Rayons.getSelectionModel().getSelectedItems().size();
                });
        Regions.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener) (c) -> {
                    nombresRegions = Regions.getSelectionModel().getSelectedItems().size();
                });

        int videoID = 0;
        System.out.println(Auth.getUserInfo().toString());
        try (Connection cn = Auth.getConnection()) {

            boolean update = false;
            String sql1;

            if (Video.getCurVideo() != null) {
                sql1 = "UPDATE `Video` SET `titre`=?,`frequence`=?,`duree`=?,`dateDebut`=?,`dateFin`=?,`dateReception`=?,`dateValidation`=?,`tarif`=?,`statut`=?,`idCommercial`=?,`idClient`=? WHERE `idVideo`=?;";
                update = true;
            } else {
                sql1 = "INSERT INTO Video(titre,frequence,duree,dateDebut,dateFin,dateReception,dateValidation,tarif,statut,idCommercial,idClient)" + "VALUES (?,?,?,?,?,?,?,?,?,?,?);";
            }

            try (PreparedStatement st1 = cn.prepareStatement(sql1);) {
                st1.setString(1, titre.getText());
                st1.setInt(2, freq);
                st1.setInt(3, dur);
                st1.setDate(4, java.sql.Date.valueOf(datedebut.getValue()));
                st1.setDate(5, java.sql.Date.valueOf(datefin.getValue()));
                st1.setDate(6, java.sql.Date.valueOf(datereception.getValue()));
                st1.setDate(7, java.sql.Date.valueOf(datevalidation.getValue()));
                st1.setDouble(8, tar);
                st1.setInt(9, statut);
                st1.setInt(10, Integer.parseInt((String) Auth.getUserInfo().get("id")));
                st1.setInt(11, Client.getCurClient().getId());

                if (update) {
                    st1.setInt(12, Video.getCurVideo().getidVideo());
                    st1.execute();
                } else {
                    st1.execute();
                    try (ResultSet res = st1.getGeneratedKeys()) {
                        res.next();
                        videoID = res.getInt("idVideo");
                    }
                }
                System.out.println(sql1);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
            if (!update) {

                try {
                    System.out.println("Enregistrement du fichier Video");
                    if (fichier != null) {
                        String t = Config.getConfig().get("rep_video").replace("$(HOME)", System.getProperty("user.home"));
                        File target = new File(t + videoID + ".mp4");
                        target.mkdirs();
                        Files.copy(Paths.get(fichier.getPath()), Paths.get(target.getPath()), StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String sql = "DELETE FROM Video WHERE idVideo=?;";
                    try (PreparedStatement st = cn.prepareStatement(sql)) {
                        st.setInt(1, videoID);
                        st.executeQuery();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    Alert al = new Alert(Alert.AlertType.ERROR, "Erreur d'enregistrement, Réesayez!", ButtonType.OK);
                    al.showAndWait();
                    return;
                }
            }
            if (update) {
                System.out.println("Suppression des anciens rayons pour le contrat : " + titre.getText());
                String sql;
                sql = "DELETE FROM DiffusionsTypesRayons WHERE idVideo=?;";
                try (PreparedStatement st = cn.prepareStatement(sql)) {
                    st.setInt(1, Video.getCurVideo().getidVideo());
                    st.executeQuery();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                System.out.println("Suppression des ancienes regions pour le contrat : " + titre.getText());
                sql = "DELETE FROM DiffusionRegions WHERE idVideo=?;";
                try (PreparedStatement st = cn.prepareStatement(sql)) {
                    st.setInt(1, Video.getCurVideo().getidVideo());
                    st.executeQuery();
                    System.out.println(sql);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }

            RayonsSelect = Rayons.getSelectionModel().getSelectedItems();
            for (String str : RayonsSelect) {
                String sql = "INSERT INTO DiffusionsTypesRayons (idVideo,idTypeRayon) VALUES (?,?);";
                try (PreparedStatement st = cn.prepareStatement(sql)) {
                    if (update) {
                        st.setInt(1, Video.getCurVideo().getidVideo());
                    } else {
                        st.setInt(1, videoID);
                    }
                    st.setInt(2, RayonData.get(str));
                    st.executeQuery();
                    System.out.println(sql + str);

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            System.out.println("Enregistrement des regions pour le contrat : " + titre.getText());
            RegionsSelect = Regions.getSelectionModel().getSelectedItems();
            for (String str : RegionsSelect) {
                String sql = "INSERT INTO `DiffusionRegions` (`idRegion`,`idVideo`) VALUES (?,?);";
                try (PreparedStatement st = cn.prepareStatement(sql)) {
                    st.setInt(1, RegionData.get(str));
                    if (update) {
                        st.setInt(2, Video.getCurVideo().getidVideo());
                    } else {
                        st.setInt(2, videoID);
                    }
                    st.executeQuery();
                    System.out.println(sql + str);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            if (update) {
                a.setContentText("Modifications éffectuées.");
            } else {
                a.setContentText("Enregistrements éffectués.");
            }

            a.showAndWait();
        }
    }

}
