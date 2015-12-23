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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import java.util.Date;
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
    
    private String [][] data = new String [100][4];
    
    private Map parameters;
    DefaultTableModel tableModel;

    private int nombremagasin;
    private int nombreregion;
    private int nombrerayons;
    private int nombreposte;
    private int nombrejours;
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
        calcul_duree();
        
        try {
            /*
            try {
            this.calculer(event);
            } catch (Exception ex) {
            ex.printStackTrace();
            }
            */
            getMagasin();
            getRayon();
            getRegion();
            getDiffusion();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
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
         System.out.println(Client.getCurClient());
         
             Client courant = clientTable.getSelectionModel().getSelectedItem();
             Video vid_courant = videoTable.getSelectionModel().getSelectedItem();
        int freq =0;
        if(this.nombrejours!=0)
        freq =this.nombremagasin/this.nombrejours;
       //System.out.println(courant.getSociete());
         parameters = new HashMap(); 
         parameters.put("Nom", courant.getSociete());
         parameters.put("Adresse", courant.getRue());
         parameters.put("Code", courant.getPostalCode());
         parameters.put("Ville", courant.getVille());
         parameters.put("Numero", courant.getTelephone());
         parameters.put("Mail", courant.getEmail());
         parameters.put("Titre", vid_courant.getTitre());
         parameters.put("Duree",Integer.toString(vid_courant.getDuree()));
         parameters.put("Debut", ConversionDate(vid_courant.getDate_debut()));
         parameters.put("Fin",   ConversionDate(vid_courant.getDate_fin()));
         parameters.put("Frequence",Double.toString(freq));
         parameters.put("Tarif", Double.toString(vid_courant.getTarif()));
         parameters.put("Regions", Integer.toString(this.nombreregion));
         parameters.put("Rayons", Integer.toString(this.nombrerayons));
         parameters.put("Magasins", Integer.toString(this.nombreposte));
         parameters.put("Prix_Unitaire",Double.toString(vid_courant.getDuree()*vid_courant.getTarif()));
         parameters.put("Nombre_Diff",Integer.toString(this.nombremagasin));
         parameters.put("Duree_Diff",Integer.toString(this.nombrejours) );
         parameters.put("montant", Double.toString(vid_courant.getDuree()*vid_courant.getTarif()*this.nombremagasin));    
         
        
        String[] columnNames = {"Région", "Rayons", "Magasin","Date"};
        String[][] data = {{"test"},{"test"},{"test"},{"test"}
            //Pour le client
        };
        
        try {
            getEntreprise();
        } catch (SQLException ex) {
             ex.printStackTrace();
        }
       // if (this.nombremagasin==0) 
            parameters.put("Info", "Aucune diffusion ne pourra être faite car aucun magasin ne comporte le rayon demandé dans les régions souhaitées "); 
       // else parameters.put("Info", "");
        tableModel = new DefaultTableModel(this.data,columnNames);
    
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
                parameters.put("Etelephone",res.getString(5));
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
         
         private String ConversionDate(String Date){
             String  Date_Convertie="";
             Date_Convertie=Date_Convertie+Date.charAt(8);
             Date_Convertie=Date_Convertie+Date.charAt(9);
             Date_Convertie=Date_Convertie+Date.charAt(4);
             Date_Convertie=Date_Convertie+Date.charAt(5);
             Date_Convertie=Date_Convertie+Date.charAt(6);
             Date_Convertie=Date_Convertie+Date.charAt(4);
             Date_Convertie=Date_Convertie+Date.charAt(0);
             Date_Convertie=Date_Convertie+Date.charAt(1);
             Date_Convertie=Date_Convertie+Date.charAt(2);
             Date_Convertie=Date_Convertie+Date.charAt(3);
            return Date_Convertie;
         }
         
         private void  getDiffusion() throws SQLException {
             Video courant = videoTable.getSelectionModel().getSelectedItem();
        System.out.println(Auth.getUserInfo().toString());
        int count =0;
        ResultSet rs = null; 
        this.nombremagasin=0;
         int i=0;
        String sql ="SELECT Region.libelle, Magasin.nom,  TypeRayon.libelle, dateDiffusion  FROM Diffusions, Magasin, TypeRayon, Region"
                + " WHERE Region.idRegion = Magasin.idRegion "
                + " AND Diffusions.idMagasin  = Magasin.idMagasin "
                + " AND Diffusions.idTypeRayon = TypeRayon.idTypeRayon"
                + " AND Diffusions.idVideo = ? ";
        
        
        System.out.println(sql);
        
        try (Connection cn = Auth.getConnection();
               PreparedStatement st = cn.prepareStatement(sql)) {
            System.out.println(courant.getidVideo());
            st.setInt(1, courant.getidVideo());
            
            rs = st.executeQuery();
             System.out.println(courant.getidVideo());
            
              //System.out.println(sql + str);
                    while (rs.next()) {
                        System.out.println(courant.getidVideo());
                       // System.out.print("Colonne 1 renvoyée ");
                        for (int j=0;j<4;j++)
                         data[i][j]=rs.getString(j+1);
                        this.nombremagasin++;
                        i++;
                       System.out.println(rs.getString(1)+rs.getString(2)+rs.getString(3)+rs.getString(4));
                               //System.out.println(data);
                    }
                    rs.close();
                    st.close();
                    for(int k=this.nombremagasin;k<100;k++)
                    for (int j=0;j<4;j++)
                         data[k][j]="";
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
       
    }
         private void calcul_duree(){
        Video courant = videoTable.getSelectionModel().getSelectedItem();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        long diff;
        int diffInDays;
	String dateInDebut = courant.getDate_debut();
        String dateInFin = courant.getDate_fin();
	try {

		Date date_deb = formatter.parse(dateInDebut);
                Date date_fin = formatter.parse(dateInFin);
                diff=date_fin.getTime()-date_deb.getTime();
		diffInDays = (int) ((diff) / (1000 * 60 * 60 * 24));
                
                System.out.println(date_deb);
		System.out.println(formatter.format(date_deb));
                System.out.println(date_fin);
		System.out.println(formatter.format(date_fin));
                
                this.nombrejours=diffInDays+1;
                

	} catch (ParseException e) {
		e.printStackTrace();
	}
         }
         
         private void  getRegion() throws SQLException {
             Video courant = videoTable.getSelectionModel().getSelectedItem();
        System.out.println(Auth.getUserInfo().toString());
        int count =0;
        ResultSet rs = null; 
        this.nombreregion=0;
         int i=0;
        String sql ="SELECT DISTINCT Region.idRegion  FROM Diffusions, Magasin, TypeRayon, Region"
                + " WHERE Region.idRegion = Magasin.idRegion "
                + " AND Diffusions.idMagasin  = Magasin.idMagasin "
                + " AND Diffusions.idTypeRayon = TypeRayon.idTypeRayon"
                + " AND Diffusions.idVideo = ? ";
        System.out.println(sql);
        
        try (Connection cn = Auth.getConnection();
               PreparedStatement st = cn.prepareStatement(sql)) {
            System.out.println(courant.getidVideo());
            st.setInt(1, courant.getidVideo());
            
            rs = st.executeQuery();
             System.out.println(courant.getidVideo());
            
              //System.out.println(sql + str);
                    while (rs.next()) {
                        System.out.println(courant.getidVideo());
                       // System.out.print("Colonne 1 renvoyée ");
                        
                        this.nombreregion++;
                        i++;
                      // System.out.println(rs.getString(1)+rs.getString(2)+rs.getString(3)+rs.getString(4));
                               //System.out.println(data);
                    }
                    rs.close();
                    st.close();
                    
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
       
    }
         private void  getRayon() throws SQLException {
             Video courant = videoTable.getSelectionModel().getSelectedItem();
        System.out.println(Auth.getUserInfo().toString());
        int count =0;
        ResultSet rs = null; 
        this.nombrerayons=0;
         int i=0;
        String sql ="SELECT DISTINCT Diffusions.idTypeRayon  FROM Diffusions "
                + "WHERE Diffusions.idVideo = ? ";
        System.out.println(sql);
        
        try (Connection cn = Auth.getConnection();
               PreparedStatement st = cn.prepareStatement(sql)) {
            System.out.println(courant.getidVideo());
            st.setInt(1, courant.getidVideo());
            
            rs = st.executeQuery();
             System.out.println(courant.getidVideo());
            
              //System.out.println(sql + str);
                    while (rs.next()) {
                        System.out.println(courant.getidVideo());
                       // System.out.print("Colonne 1 renvoyée ");
                        
                        this.nombrerayons++;
                        i++;
                      // System.out.println(rs.getString(1)+rs.getString(2)+rs.getString(3)+rs.getString(4));
                               //System.out.println(data);
                    }
                    rs.close();
                    st.close();
                    
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
        
        private void  getMagasin() throws SQLException {
             Video courant = videoTable.getSelectionModel().getSelectedItem();
        System.out.println(Auth.getUserInfo().toString());
        int count =0;
        ResultSet rs = null; 
        this.nombreposte=0;
         int i=0;
        String sql ="SELECT DISTINCT Region.libelle, Magasin.nom,  TypeRayon.libelle  FROM Diffusions, Magasin, TypeRayon, Region"
                + " WHERE Region.idRegion = Magasin.idRegion "
                + " AND Diffusions.idMagasin  = Magasin.idMagasin "
                + " AND Diffusions.idTypeRayon = TypeRayon.idTypeRayon"
                + " AND Diffusions.idVideo = ? ";
        System.out.println(sql);
        
        try (Connection cn = Auth.getConnection();
               PreparedStatement st = cn.prepareStatement(sql)) {
            System.out.println(courant.getidVideo());
            st.setInt(1, courant.getidVideo());
            
            rs = st.executeQuery();
             System.out.println(courant.getidVideo());
            
              //System.out.println(sql + str);
                    while (rs.next()) {
                        System.out.println(courant.getidVideo());
                       // System.out.print("Colonne 1 renvoyée ");
                        
                        this.nombreposte++;
                        i++;
                      // System.out.println(rs.getString(1)+rs.getString(2)+rs.getString(3)+rs.getString(4));
                               //System.out.println(data);
                    }
                    rs.close();
                    st.close();
                    
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
