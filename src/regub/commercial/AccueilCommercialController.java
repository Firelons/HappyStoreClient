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
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;

import regub.AbstractController;
import regub.Auth;
import regub.Main;
import regub.util.UserBarController;

/**
 *
 * @author Lons & Mesmerus
 */
public class AccueilCommercialController extends AbstractController {

    private int testNombrecontrat;
    //Renvoie si le client a des vidéos ou pas
    private boolean video_ou_non = false;
    //Proprietés pour le client
    private final ObservableList<Client> clientData = FXCollections.observableArrayList();
    @FXML
    private TableView<Client> clientTable;
    @FXML
    private TableColumn<Client, String> societe;
    @FXML
    private TableColumn<Client, String> rue;

    //Proprietés pour les vidéos
    private final ObservableList<Video> videoData = FXCollections.observableArrayList();
    @FXML
    private TableView<Video> videoTable;
    @FXML
    private TableColumn<Video, String> titre;
    @FXML
    private TableColumn<Video, String> date_debut;
    @FXML
    private TableColumn<Video, String> date_fin;
    @FXML
    private TableColumn<Video, Double> tarif;
    @FXML
    private TableColumn<Video, Integer> duree;
    @FXML
    private TableColumn<Video, String> statut;

    //boutons de la page
    @FXML
    private Button ModifierClient;
    @FXML
    private Button SupprimerClient;
    @FXML
    private Button AjouterContrat;
    @FXML
    private Button ModifierContrat;
    @FXML
    private Button SupprimerContrat;
    @FXML
    private Button Facture;
    
    private Map parameters;
    DefaultTableModel tableModel;

    @FXML
    private UserBarController usermenuController;

    @FXML
    private void AjouterContrat(ActionEvent event) throws IOException {
        Client.setCurClient(clientTable.getSelectionModel().getSelectedItem());
        getApp().gotoPage("commercial/Contrat");
    }

    @FXML
    private void ModifierContrat(ActionEvent event) throws IOException {

        Video.setCurVideo(videoTable.getSelectionModel().getSelectedItem());
        Client.setCurClient(clientTable.getSelectionModel().getSelectedItem());
        getApp().gotoPage("commercial/Contrat");
    }

    @FXML
    private void AnnulerContrat(ActionEvent event) throws IOException {

        LocalDate datdeb = LocalDate.parse(videoTable.getSelectionModel().getSelectedItem().getDate_debut());
        LocalDate datfin = LocalDate.parse(videoTable.getSelectionModel().getSelectedItem().getDate_fin());
//       datfin = sf.parse(datefin.getValue().toString());
        Alert a = new Alert(
                Alert.AlertType.INFORMATION,
                null,
                ButtonType.OK);
        if (datfin.isAfter(LocalDate.now()) && videoTable.getSelectionModel().getSelectedItem().getStatut() != 3) {
            String sql = "UPDATE Video SET dateFin=?,statut=? "
                    + " WHERE idVideo=?;";
            try (Connection cn = Auth.getConnection();
                    PreparedStatement st1 = cn.prepareStatement(sql)) {
                st1.setString(1, "" + LocalDate.now());
                st1.setInt(2, 3);
                st1.setInt(3, videoTable.getSelectionModel().getSelectedItem().getidVideo());
                st1.execute();
                a.setContentText("Le contrat a été annulé.");
                SupprimerContrat.setDisable(true);
                a.showAndWait();
                videoTable.getSelectionModel().getSelectedItem().setStatut(3);
                videoTable.getSelectionModel().getSelectedItem().setDate_fin("" + LocalDate.now());
            } catch (SQLException e) {
                e.printStackTrace();
                a.setContentText("Erreur d'annulation.");
                a.showAndWait();
            }
        }

    }

    private void getClientDB() throws IOException {

        //System.out.println(Auth.getUserInfo().toString());
        String sql = "SELECT * FROM Client ORDER BY societe ASC ";
        try (Connection cn = Auth.getConnection();
                Statement st = cn.createStatement();
                ResultSet rsClient = st.executeQuery(sql);) {
            while (rsClient.next()) {
                clientData.add(new Client(
                        rsClient.getInt("idClient"),
                        rsClient.getString("societe"),
                        rsClient.getString("telephone"),
                        rsClient.getString("email"),
                        rsClient.getString("addr_ligne1"),
                        rsClient.getString("ville"),
                        rsClient.getString("code_postal")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getVideoDB(Client client) throws IOException {
        //Vider la liste des video
        videoData.clear();
        String sql = "SELECT * FROM Video WHERE idClient = ?"
                + " ORDER BY titre ASC";
        try (Connection cn = Auth.getConnection();
                PreparedStatement st = cn.prepareStatement(sql)) {
            //Parametres à changer
            st.setInt(1, client.getId());
            try (ResultSet rsVideos = st.executeQuery()) {
                video_ou_non = rsVideos.getFetchSize() > 0;
                while (rsVideos.next()) {
                    videoData.add(new Video(
                            rsVideos.getInt("idVideo"),
                            rsVideos.getString("titre"),
                            rsVideos.getInt("frequence"),
                            rsVideos.getInt("duree"),
                            rsVideos.getString("dateDebut"),
                            rsVideos.getString("dateFin"),
                            rsVideos.getString("dateReception"),
                            rsVideos.getString("dateValidation"),
                            rsVideos.getDouble("tarif"),
                            rsVideos.getInt("statut")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Client> getClientData() {

        try {
            this.getClientDB();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return clientData;
    }

    public ObservableList<Video> getVideoData(Client client) {

        try {
            this.getVideoDB(client);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return videoData;
    }

    @FXML
    private void AjouterClient(ActionEvent event) throws IOException {
        getApp().gotoPage("commercial/Client");
    }

    @FXML
    private void SupprimerClient(ActionEvent event) {

        String sql;

        sql = "DELETE FROM Client WHERE idClient=" + clientTable.getSelectionModel().getSelectedItem().getId() + " ";
        System.out.println(clientTable.getSelectionModel().getSelectedItem().getId());
        try (Connection cn = Auth.getConnection();
                PreparedStatement st1 = cn.prepareStatement(sql)) {
            st1.execute();
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Suppression éffectuée", ButtonType.OK);
            a.showAndWait();
        } catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Vous ne pouvez pas supprimer ce client !! ", ButtonType.OK);
            a.showAndWait();
        }
        getApp().gotoPage("commercial/AccueilCommercial");
    }

    @Override
    public void setApp(Main main) {
        super.setApp(main);
        usermenuController.setApp(main);

        clientTable.setItems(getClientData());

    }

    private void gestionClientButton() {
        clientTable.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener) (c) -> {
                    clientTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                    int nbSelections = clientTable.getSelectionModel().getSelectedItems().size();
                    if (nbSelections == 1) {
                        ModifierClient.setDisable(false);
                        SupprimerClient.setDisable(false);
                        AjouterContrat.setDisable(false);
                    } else if (nbSelections > 1) {
                        ModifierClient.setDisable(true);
                        SupprimerClient.setDisable(false);
                        AjouterContrat.setDisable(true);
                    } else if (nbSelections == 0) {
                        ModifierClient.setDisable(true);
                        SupprimerClient.setDisable(true);

                    }
                });
    }

    private void gestioncontratButton() {
        videoTable.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener) (c) -> {
                    videoTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                    int nbSelection = videoTable.getSelectionModel().getSelectedItems().size();
                    if (nbSelection == 1) {
                        LocalDate datdeb = LocalDate.parse(videoTable.getSelectionModel().getSelectedItem().getDate_debut());
                        LocalDate datfin = LocalDate.parse(videoTable.getSelectionModel().getSelectedItem().getDate_fin());
                        if (datfin.isBefore(LocalDate.now()) || videoTable.getSelectionModel().getSelectedItem().getStatut() == 3) {
                            SupprimerContrat.setDisable(true);
                        } else {
                            SupprimerContrat.setDisable(false);
                        }
                        ModifierContrat.setDisable(false);
                    } else if (nbSelection > 1) {
                        ModifierContrat.setDisable(true);
                        SupprimerContrat.setDisable(false);
                    } else if (nbSelection == 0) {
                        ModifierContrat.setDisable(true);
                        SupprimerContrat.setDisable(true);

                    }
                });
    }

    @FXML
    private void ModifierClient(ActionEvent event) throws IOException {
        Client.setCurClient(clientTable.getSelectionModel().getSelectedItem());
        getApp().gotoPage("commercial/Client");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        societe.setCellValueFactory(cellData -> cellData.getValue().societeProperty());
        rue.setCellValueFactory(cellData -> cellData.getValue().rueProperty());

        clientTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> videoTable.setItems(getVideoData(newValue)));

        this.gestionClientButton();
        this.gestioncontratButton();

        titre.setCellValueFactory(cellData -> cellData.getValue().titreProperty());
        date_debut.setCellValueFactory(cellData -> cellData.getValue().date_debutProperty());
        date_fin.setCellValueFactory(cellData -> cellData.getValue().date_finProperty());
        duree.setCellValueFactory(cellData -> cellData.getValue().dureeProperty().asObject());
        tarif.setCellValueFactory(cellData -> cellData.getValue().tarifProperty().asObject());
        statut.setCellValueFactory((cellData) ->{
            switch(cellData.getValue().getStatut()){
                case 1:
                    return new SimpleStringProperty("validé");
                case 2:
                    return new SimpleStringProperty("en preparation");
                default:
                    return new SimpleStringProperty("annulé");
            }
           
        });
        //initialisation des bouttons
        //modifications

        Client.setCurClient(null);
        Video.setCurVideo(null);
        ModifierClient.setDisable(true);
        SupprimerClient.setDisable(true);
        AjouterContrat.setDisable(true);
        ModifierContrat.setDisable(true);
        SupprimerContrat.setDisable(true);

    }
    @FXML
    private void AfficherFacture(ActionEvent event) throws IOException {

        
       
        JasperPrint jasperPrint = null;
        net.sf.jasperreports.engine.JasperReport x = null; 
        TableModelData();
        try {
            x = JasperCompileManager.compileReport("reports/facture.jrxml");
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
        Client.setCurClient(clientTable.getSelectionModel().getSelectedItem());
        
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
        /* parameters.put("Frequence", this.frequence.getText());
         parameters.put("Tarif", this.tarif.getText());
         parameters.put("Regions", Integer.toString(this.nombresRegions));
         parameters.put("Rayons", Integer.toString(this.nombresRayons));
         parameters.put("Magasins", Integer.toString(this.nombremagasin));
         parameters.put("Prix_Unitaire", Double.toString(this.tar*this.dur));
         parameters.put("Nombre_Diff", Double.toString(this.nb_jours*this.freq*this.nombremagasin));
         parameters.put("Duree_Diff", Double.toString(this.nb_jours));
         parameters.put("montant", this.montant.getText());    
         */
        
        String[] columnNames = {"Nom", "Adresse", "Code"};
        String[][] data = {{"test"},{"test"},{"test"},{"test"}
            //Pour le client
        };
        /*
        try {
            getEntreprise();
        } catch (SQLException ex) {
             ex.printStackTrace();
        }
        if (this.nombremagasin==0) parameters.put("Info", "Aucune diffusion ne pourra être faite car aucun magasin ne comporte le rayon demandé dans les régions souhaitées "); 
        else parameters.put("Info", "");
        tableModel = new DefaultTableModel(this.data,columnNames);
    */
    }

}
