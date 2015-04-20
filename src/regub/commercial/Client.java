/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package regub.commercial;


import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Lons
 */
public class Client {

    private final StringProperty societe;
    private final StringProperty telephone;
    private final StringProperty email;
    private final StringProperty rue;
    private final StringProperty ville;
    private final StringProperty postalCode;


    /**
     * Default constructor.
     */
    public Client() {
        this(null, null, null, null, null, null);
    }

    /**
     * Constructor with some initial data.
     * 
     * @param societe
    
     */
    public Client(String societe, String telephone, String email, String rue,String ville,  String postalCode) {
        this.societe = new SimpleStringProperty(societe);
        this.rue = new SimpleStringProperty( rue);

        // Some initial dummy data, just for convenient testing.
        this.ville = new SimpleStringProperty(ville);
        this.postalCode = new SimpleStringProperty(postalCode);
        this.telephone = new SimpleStringProperty(telephone);
        this.email = new SimpleStringProperty(email);
    }

    public String getSociete() {
        return societe.get();
    }

    public void setSociete(String firstName) {
        this.societe.set(firstName);
    }

    public StringProperty societeProperty() {
        return societe;
    }

    public String getRue() {
        return rue.get();
    }

    public void setRue(String lastName) {
        this.rue.set(lastName);
    }

    public StringProperty rueProperty() {
        return rue;
    }

    public String getVille() {
        return ville.get();
    }

    public void setVille(String street) {
        this.ville.set(street);
    }

    public StringProperty villeProperty() {
        return ville;
    }

    public String getPostalCode() {
        return postalCode.get();
    }

    public void setPostalCode(String postalCode) {
        this.postalCode.set(postalCode);
    }

    public StringProperty postalCodeProperty() {
        return postalCode;
    }

    public String getTelephone() {
        return telephone.get();
    }

    public void setTelephone(String city) {
        this.telephone.set(city);
    }

    public StringProperty telephoneProperty() {
        return telephone;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String birthday) {
        this.email.set(birthday);
    }

    public StringProperty emailProperty() {
        return email;
    }
}