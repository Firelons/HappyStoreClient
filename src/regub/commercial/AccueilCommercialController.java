/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub.commercial;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import regub.AbstractController;
import regub.Auth;
import regub.Main;
import regub.util.UserBarController;

/**
 *
 * @author Mesmerus
 */
public class AccueilCommercialController extends AbstractController {

    private int testNombrecontrat;

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

    private ResultSet rsClient;//Récupère la liste des clients dans la base de donées

    @FXML
    private UserBarController usermenuController;

    @FXML
    private void AjouterContrat(ActionEvent event) throws IOException {
        getApp().gotoPage("commercial/Contrat");
    }

    @FXML
    private void getClientDB() throws IOException {

        System.out.println(Auth.getUserInfo().toString());

        try (Connection cn = Auth.getConnection();
                Statement st = cn.createStatement()) {
            String sql = "SELECT * FROM Client";

            rsClient = st.executeQuery(sql);
            while (rsClient.next()) {
                clientData.add(new Client(rsClient.getInt("idClient"), rsClient.getString("societe"), rsClient.getString("telephone"), rsClient.getString("email"),
                        rsClient.getString("addr_ligne1"), rsClient.getString("ville"), rsClient.getString("code_postal")));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getVideoDB(Client client) throws IOException {
        //Vider la liste des video
        videoData.clear();

        System.out.println(Auth.getUserInfo().toString());
        ResultSet rsVideos;
        try (Connection cn = Auth.getConnection();
                Statement st = cn.createStatement()) {
            //Parametres à changer
            String sql = "SELECT * FROM video WHERE idClient=" + client.getId() + "";

            rsVideos = st.executeQuery(sql);
            while (rsVideos.next()) {
                videoData.add(new Video(rsVideos.getString("titre"), rsVideos.getInt("duree"), rsVideos.getDouble("tarif"),
                        rsVideos.getString("dateDebut"), rsVideos.getString("dateFin")));
                System.out.println(rsVideos.getString("titre") + rsVideos.getInt("duree") + rsVideos.getDouble("tarif")
                        + rsVideos.getString("dateDebut") + rsVideos.getString("dateFin"));
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
        //clientData.add(new Client("4","4","4","4","4","4")) ;
        return clientData;
    }

    public ObservableList<Video> getVideoData(Client client) {

        try {
            this.getVideoDB(client);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //clientData.add(new Client("4","4","4","4","4","4")) ;
        return videoData;
    }

    @FXML
    private void AjouterClient(ActionEvent event) throws IOException {
        getApp().gotoPage("commercial/Client");
    }

    @Override
    public void setApp(Main main) {
        super.setApp(main);
        usermenuController.setApp(main);

        clientTable.setItems(getClientData());

    }

    private void ClientButton() {

    }

    private void ContratButton() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        societe.setCellValueFactory(cellData -> cellData.getValue().societeProperty());
        rue.setCellValueFactory(cellData -> cellData.getValue().rueProperty());

        clientTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> videoTable.setItems(getVideoData(newValue)));

        titre.setCellValueFactory(cellData -> cellData.getValue().titreProperty());
        date_debut.setCellValueFactory(cellData -> cellData.getValue().date_debutProperty());
        date_fin.setCellValueFactory(cellData -> cellData.getValue().date_finProperty());
        duree.setCellValueFactory(cellData -> cellData.getValue().dureeProperty().asObject());
        tarif.setCellValueFactory(cellData -> cellData.getValue().tarifProperty().asObject());
        //initialisation des bouttons
        //modifications

        ModifierClient.setDisable(true);
        SupprimerClient.setDisable(true);
        AjouterContrat.setDisable(true);
        ModifierContrat.setDisable(true);
        SupprimerContrat.setDisable(true);
        
       
    }

}
