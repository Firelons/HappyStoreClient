package regub.administrateur;

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

public class CompteUtilController extends AbstractController {

    //Proprietés pour l'utilisateur
    private final ObservableList<Utilisateur> UtilData = FXCollections.observableArrayList();
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

    @FXML
    private UserBarController usermenuController;

    @FXML
    private void CompteUtilAjouter(ActionEvent event) {
        getApp().gotoPage("administrateur/CompteAJMO");
    }

    @FXML
    private void CompteUtilModifier(ActionEvent event) {
        getApp().gotoPage("administrateur/CompteAJMO");
    }

    @FXML
    private void getUtilDB() throws IOException {

        String sql = "SELECT idCompte ,nom ,prenom ,login ,libelle "
                + "FROM Compte inner join TypeCompte ON Compte.idTypeCompte=TypeCompte.idTypeCompte";
        try (
                Connection cn = Auth.getConnection();
                Statement st = cn.createStatement();
                ResultSet res = st.executeQuery(sql)) {

            while (res.next()) {
                UtilData.add(
                        new Utilisateur(
                                res.getInt("idCompte"), res.getString("nom"),
                                res.getString("prenom"), res.getString("login"),
                                res.getString("libelle")
                        )
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setApp(Main m) {
        super.setApp(m);
        usermenuController.setApp(m);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Utilisateur.setCurUtil(null);
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
