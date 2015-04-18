/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * Classe de base de tous les controleurs
 *
 * @author antoine
 */
public abstract class AbstractController implements Initializable {

    private Main appMain;
    private Stage appStage;

    /**
     * Donne au controleur l'address de l'instance Main (appelée automatiquement
     * lors du gotoPage()), doit être surchargée en cas d'inclusion de
     * controleur et lancer sur chaque controleur inclus.
     *
     * @param main
     */
    public void setApp(Main main) {
        appMain = main;
    }

    /**
     * Permet l'accès à l'instance de Main (Application) affin par exemple de
     * changer de page
     *
     * @return l'objet Main
     */
    public Main getApp() {
        return appMain;
    }
    
    public void setStage(Stage stage) {
        appStage = stage;
    }
    public Stage getstage() {
        return appStage;
    }
    
}
