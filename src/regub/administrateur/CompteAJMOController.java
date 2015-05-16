/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub.administrateur;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import regub.AbstractController;
import regub.Auth;
import regub.Main;
import regub.util.UserBarController;

/**
 * FXML Controller class
 *
 * @author BREGMESTRE
 */
public class CompteAJMOController extends AbstractController {

    @FXML
    private UserBarController usermenuController;
    @FXML
    private Label lblTitre;
    @FXML
    private TextField tfLogin;
    @FXML
    private TextField tfPrenom;
    @FXML
    private TextField tfNom;
    @FXML
    private PasswordField pwfPassWord;
    @FXML
    private ComboBox<String> comb_compte;

    private boolean generateLogin = true;

    private boolean insertion = true;

    @Override
    public void setApp(Main m) {
        super.setApp(m);
        usermenuController.setApp(m);
    }

    @FXML
    public void handleValider(ActionEvent event) {
        boolean ok = false;
        String msg;
        if (!checkdata()) {
            return;
        }
        if (insertion) {
            if (insertUtil()) {
                ok = true;
                msg = "L'utilisateur à bien été ajouté";
            } else {
                msg = "Erreur durant l'insertion de l'utilisateur";
            }
        } else {
            if (updateUtil()) {
                msg = "L'utilisateur à bien été mis à jour";
                ok = true;
            } else {
                msg = "Erreur durant la mis à jour de l'utilisateur";
            }
        }
        Alert a = new Alert(Alert.AlertType.NONE);
        a.setContentText(msg);
        if (ok) {
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setHeaderText("Confirmation");
            Utilisateur.setCurUtil(null);
        } else {
            a.setAlertType(Alert.AlertType.ERROR);
        }
        a.showAndWait();
        if (ok) {
            getApp().gotoPage("administrateur/CompteUtil");
        }
    }

    @FXML
    public void handleAnnuler(ActionEvent event) {
        Utilisateur.setCurUtil(null);
        getApp().gotoPage("administrateur/CompteUtil");
    }

    boolean checkdata() {
        String msg;
        if (tfLogin.getText().length() == 0) {
            msg = "L'utilisateur doit avoir un login";
        } else if (tfNom.getText().length() == 0) {
            msg = "L'utilisateur doit avoir un nom";
        } else if (tfPrenom.getText().length() == 0) {
            msg = "L'utilisateur doit avoir un prénom";
        } else if (pwfPassWord.getText().length() == 0 && insertion) {
            msg = "L'utilisateur doit avoir un mot de passe non vide";
        } else if (comb_compte.getValue() == null) {
            msg = "L'utilisateur doit avoir un type de compte";
        } else {
            try {
                boolean newLogin = insertion
                        || Utilisateur.getCurUtil().getLogin().get()
                        .compareTo(tfLogin.getText()) != 0;
                if (newLogin && loginExist(tfLogin.getText())) {
                    msg = "Ce login est déjà utilisé";
                } else {
                    return true;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                msg = "Problème lors de la connexion à la base de donnée";
            }

        }
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(msg);
        a.showAndWait();
        return false;
    }

    private boolean loginExist(String login) throws SQLException {
        String sql = "SELECT COUNT(*) AS nbr FROM Compte WHERE( login = ?);";
        ResultSet res = null;
        try (Connection cn = Auth.getConnection();
                PreparedStatement st = cn.prepareStatement(sql);) {
            st.setString(1, login);
            st.executeQuery();
            res = st.getResultSet();
            res.first();
            return res.getInt("nbr") != 0;
        } catch (SQLException ex) {
            throw ex;
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

    private boolean insertUtil() {
        String sql = "INSERT INTO `Compte` (`nom`, `prenom`, `login`, `password`, `idTypeCompte`)"
                + " VALUES (?,?,?,?, (SELECT TypeCompte.idTypeCompte FROM TypeCompte WHERE(TypeCompte.libelle = ?)) );";
        try (Connection cn = Auth.getConnection();
                PreparedStatement st = cn.prepareStatement(sql);) {
            st.setString(1, tfNom.getText());
            st.setString(2, tfPrenom.getText());
            st.setString(3, tfLogin.getText());
            st.setString(4, pwfPassWord.getText());
            st.setString(5, comb_compte.getValue());
            st.executeQuery();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean updateUtil() {
        boolean setPassw = insertion || !pwfPassWord.getText().isEmpty();
        String sql = "UPDATE `Compte` SET `nom` = ? , `prenom` = ? , `login` = ?,"
                + (setPassw ? " `password` = ?," : "")
                + "`idTypeCompte` = (SELECT TypeCompte.idTypeCompte FROM TypeCompte WHERE(TypeCompte.libelle = ?))"
                + "WHERE idCompte = ?;";
        try (Connection cn = Auth.getConnection();
                PreparedStatement st = cn.prepareStatement(sql);) {
            int i = 1;
            st.setString(i++, tfNom.getText());
            st.setString(i++, tfPrenom.getText());
            st.setString(i++, tfLogin.getText());
            if (setPassw) {
                st.setString(i++, pwfPassWord.getText());
            }
            st.setString(i++, comb_compte.getValue());
            st.setInt(i++, Utilisateur.getCurUtil().getId_util().get());
            st.executeQuery();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (Utilisateur.getCurUtil() == null) {
            insertion = true;
            lblTitre.setText("Ajouter un Compte Utilisateur");
        } else {
            insertion = false;
            lblTitre.setText("Modifier un Compte Utilisateur");
            Utilisateur cur = Utilisateur.getCurUtil();
            tfLogin.setText(cur.getLogin().get());
            tfNom.setText(cur.getNom().get());
            tfPrenom.setText(cur.getPrenom().get());
            comb_compte.setValue(cur.gettype_compte().get());
        }
        if (insertion) {
            tfLogin.textProperty().addListener((str) -> {
                if (generateLogin) {
                    String pw = ((StringProperty) str).get();
                    pwfPassWord.setText(pw);
                }
            });
        }

        pwfPassWord.textProperty().addListener((str) -> {
            if (generateLogin && pwfPassWord.isFocused()) {
                generateLogin = false;
            }
        });
    }

}
