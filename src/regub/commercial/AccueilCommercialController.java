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
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
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
        Client.setCurClient(clientTable.getSelectionModel().getSelectedItem());
        getApp().gotoPage("commercial/Contrat");
    }

    @FXML
    private void ModifierContrat(ActionEvent event) throws IOException {

        Video.setCurVideo(videoTable.getSelectionModel().getSelectedItem());
        Client.setCurClient(clientTable.getSelectionModel().getSelectedItem());
        getApp().gotoPage("commercial/Contrat");
    }

    private void getClientDB() throws IOException {

        System.out.println(Auth.getUserInfo().toString());

        try (Connection cn = Auth.getConnection();
                Statement st = cn.createStatement()) {
            String sql = "SELECT * FROM Client" + " ORDER BY societe ASC ";

            rsClient = st.executeQuery(sql);
            System.out.println(rsClient);
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
        String sql = "SELECT * FROM Video WHERE idClient = ?" + " ORDER BY titre ASC";
        try (Connection cn = Auth.getConnection();
                PreparedStatement st = cn.prepareStatement(sql)) {
            //Parametres à changer
            st.setInt(1, client.getId());

            rsVideos = st.executeQuery();
            System.out.println(rsVideos);
            if (rsVideos != null) {
                video_ou_non = true;
            }
            while (rsVideos.next()) {
                videoData.add(new Video(rsVideos.getInt("idVideo"),rsVideos.getString("titre"), rsVideos.getInt("frequence"), rsVideos.getInt("duree"), rsVideos.getString("dateDebut"), rsVideos.getString("dateFin"), rsVideos.getString("dateReception"), rsVideos.getString("dateValidation"), rsVideos.getDouble("tarif"),rsVideos.getInt("statut")));
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
                    System.out.println(nbSelections);
                    if (nbSelections == 1) {
                        ModifierClient.setDisable(false);
                        if (video_ou_non) {
                            SupprimerClient.setDisable(false);
                        } else {
                            SupprimerClient.setDisable(true);
                        }

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
                    System.out.println(nbSelection);
                    if (nbSelection == 1) {
                        ModifierContrat.setDisable(false);
                        SupprimerContrat.setDisable(false);
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
    private void ModifierClient() {
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

}
