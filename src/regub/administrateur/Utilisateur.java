/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub.administrateur;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author batchanou
 */
public class Utilisateur {

    private final IntegerProperty id_util;
    private final StringProperty nom;
    private final StringProperty prenom;
    private final StringProperty login;
    private final StringProperty type_compte;
    private static Utilisateur curUtil;

    /**
     * Default constructor.
     */
    public Utilisateur() {
        this(0, null, null, null, null);
    }

    /**
     * Constructor with some initial data.
     *
     * @param id
     * @param nom
     * @param prenom
     * @param login
     * @param typecmpt
     *
     */
    public Utilisateur(int id, String nom, String prenom, String login, String typecmpt) {
        this.id_util = new SimpleIntegerProperty(id);
        this.nom = new SimpleStringProperty(nom);
        this.type_compte = new SimpleStringProperty(typecmpt);
        this.prenom = new SimpleStringProperty(prenom);
        this.login = new SimpleStringProperty(login);

    }

    /**
     * @return the id_util
     */
    public IntegerProperty getId_util() {
        return id_util;
    }

    /**
     * @param id_util the id_util to set
     */
    public void setId_util(int id_util) {
        this.id_util.set(id_util);
    }

    /**
     * @return the nom
     */
    public StringProperty getNom() {
        return nom;
    }

    /**
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom.set(nom);
    }

    /**
     * @return the prenom
     */
    public StringProperty getPrenom() {
        return prenom;
    }

    /**
     * @param prenom the prenom to set
     */
    public void setPrenom(String prenom) {
        this.prenom.set(prenom);
    }

    /**
     * @return the login
     */
    public StringProperty getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login.set(login);
    }

    /**
     * @return the id_type_compte
     */
    public StringProperty gettype_compte() {
        return type_compte;
    }

    /**
     * @param type_compte the id_type_compte to set
     */
    public void settype_compte(String type_compte) {
        this.type_compte.set(type_compte);
    }

    public static void setCurUtil(Utilisateur util) {
        curUtil = util;
    }

    public static Utilisateur getCurUtil() {
        return curUtil;
    }
}
