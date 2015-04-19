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
    private final IntegerProperty telephone;
    private final StringProperty email;
    private final StringProperty rue;
    private final StringProperty ville;
    private final IntegerProperty postalCode;


    /**
     * Default constructor.
     */
    public Client() {
        this(null, 0, null, null, null, 0);
    }

    /**
     * Constructor with some initial data.
     * 
     * @param societe
    
     */
    public Client(String societe, int telephone, String email, String rue,String ville, int postalCode) {
        this.societe = new SimpleStringProperty(societe);
        this.rue = new SimpleStringProperty( rue);

        // Some initial dummy data, just for convenient testing.
        this.ville = new SimpleStringProperty(ville);
        this.postalCode = new SimpleIntegerProperty(postalCode);
        this.telephone = new SimpleIntegerProperty(telephone);
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

    public int getPostalCode() {
        return postalCode.get();
    }

    public void setPostalCode(int postalCode) {
        this.postalCode.set(postalCode);
    }

    public IntegerProperty postalCodeProperty() {
        return postalCode;
    }

    public Integer getTelephone() {
        return telephone.get();
    }

    public void setTelephone(int city) {
        this.telephone.set(city);
    }

    public IntegerProperty cityProperty() {
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