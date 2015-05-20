package regub.administrateur;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import regub.AbstractController;
import regub.Auth;
import regub.Main;
//import static regub.administrateur.RegionAccueilController.select_region;
//import static regub.administrateur.RegionAccueilController.select_region_id;
import regub.util.UserBarController;

public class CompteUtilController extends AbstractController {

    //Proprietés pour l'utilisateur
    private final ObservableList<Utilisateur> UtilData = FXCollections.observableArrayList();
    // teste si il y a des utilisateurs ou pas
    private boolean si_util = true;
    @FXML
    private TableView<Utilisateur> UtilTable;
    @FXML
    private TableColumn<Utilisateur, String> nom;
    @FXML
    private TableColumn<Utilisateur, String> prenom;
    @FXML
    private TableColumn<Utilisateur, String> Typecmpt;
    @FXML
    private TableColumn<Utilisateur, String> login;

    //boutons de la page
    @FXML
    private Button AjouterUtil;
    @FXML
    private Button ModifierUtil;
    @FXML
    private Button SupprimerUtil;

    private ResultSet rsUtil;//Récupère la liste des clients dans la base de données

    private static int ID;
    @FXML
    private UserBarController usermenuController;

    @FXML
    private void CompteUtilAjouter(ActionEvent event) {
        Utilisateur.setCurUtil(null);
        getApp().gotoPage("administrateur/CompteAJMO");
    }

    @FXML
    private void CompteUtilModifier(ActionEvent event) {
        getApp().gotoPage("administrateur/CompteAJMO");
    }

    @FXML
    private void CompteUtilSupprimer(ActionEvent event) {

        ID = Utilisateur.getCurUtil().getId_util().get();

        String sql;

        sql = "DELETE FROM Compte WHERE idCompte=" + ID + " ";
        try (Connection cn = Auth.getConnection();
                PreparedStatement st1 = cn.prepareStatement(sql)) {

            st1.execute();

        } catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Vous ne pouvez pas supprimer cet utilisateur !! ", ButtonType.OK);
            a.showAndWait();
        }
        getApp().gotoPage("administrateur/CompteUtil");
    }

    @FXML
    private void getUtilDB() throws IOException {

        String sql = "SELECT idCompte ,nom ,prenom ,login ,libelle "
                + "FROM Compte inner join TypeCompte ON Compte.idTypeCompte=TypeCompte.idTypeCompte";
        try (
                Connection cn = Auth.getConnection();
                Statement st = cn.createStatement();
                ResultSet rsUtil = st.executeQuery(sql)) {

            while (rsUtil.next()) {
                UtilData.add(
                        new Utilisateur(
                                rsUtil.getInt("idCompte"), rsUtil.getString("nom"),
                                rsUtil.getString("prenom"), rsUtil.getString("login"),
                                rsUtil.getString("libelle")
                        )
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void gestionUtilButton() {
        UtilTable.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener) (c) -> {
                    UtilTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                    int nbSelections = UtilTable.getSelectionModel().getSelectedItems().size();
                    System.out.println(nbSelections);
                    if (nbSelections == 1) {

                        if (si_util) {
                            SupprimerUtil.setDisable(false);
                            ModifierUtil.setDisable(false);

                            /*  if (SupprimerUtil.is) {
                             Alert a = new Alert(Alert.AlertType.WARNING, "voulez-vous vraiment supprimer ces donnees", ButtonType.OK);
                             a.showAndWait();
                             MessageBox.show(null, // Fenêtre parente
                             "Voulez-vous vraiment supprimer cet utilisateur", // Message à afficher
                             "Attention", // Titre de la fenêtre
                             MessageBox.ICON_WARNING // Icône à afficher
                             );
                                
                             SupprimerUtil.requestFocus();
                             return;
                             }*/
                        } else {
                            SupprimerUtil.setDisable(true);
                            ModifierUtil.setDisable(true);
                        }

                        //AjouterUtil.setDisable(false);
                    } else if (nbSelections > 1) {  // selection multiple
                        ModifierUtil.setDisable(true);
                        SupprimerUtil.setDisable(false);

                        //AjouterUtil.setDisable(false);
                    } else if (nbSelections == 0) {
                        ModifierUtil.setDisable(true);
                        SupprimerUtil.setDisable(true);
                        //AjouterUtil.setDisable(false);

                    }

                });

    }

    @Override
    public void setApp(Main m) {
        super.setApp(m);
        usermenuController.setApp(m);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Utilisateur.setCurUtil(null);

        gestionUtilButton();

        AjouterUtil.setDisable(false);
        ModifierUtil.setDisable(true);
        SupprimerUtil.setDisable(true);

        try {
            getUtilDB();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        UtilTable.setItems(UtilData);
        nom.setCellValueFactory(cellData -> cellData.getValue().getNom());
        prenom.setCellValueFactory(cellData -> cellData.getValue().getPrenom());
        Typecmpt.setCellValueFactory(cellData -> cellData.getValue().gettype_compte());
        login.setCellValueFactory(cellData -> cellData.getValue().getLogin());
        UtilTable.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends Utilisateur> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    Utilisateur.setCurUtil(c.getAddedSubList().get(0));
                    ModifierUtil.setDisable(false);
                    SupprimerUtil.setDisable(false);
                }
            }
        });
    }

}
