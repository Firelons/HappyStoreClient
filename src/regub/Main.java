/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package regub;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Classe représentant l'application
 * 
 * @author antoine
 */
public class Main extends Application {

    private final double MINIMUM_WINDOW_WIDTH = 640;
    private final double MINIMUM_WINDOW_HEIGHT = 480;

    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        try {
            stage = primaryStage;
            stage.setTitle("Regub");
            stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
            stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
            stage.setScene(new Scene(new Pane()));
            stage.centerOnScreen();
            primaryStage.show();
    //////////   PAGE D'ACCUEIL   ////////////
            gotoPage("login");
    //////////////////////////////////////////
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
    /**
     * charge le fxml et change de page
     * 
     * @param pageName nom du fxml, sans l'extension
     */
    public void gotoPage(String pageName) {
        FXMLLoader loader = new FXMLLoader();
        Pane page;
        try (InputStream in = Main.class.getResourceAsStream(pageName + ".fxml")) {
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            loader.setLocation(Main.class.getResource(pageName + ".fxml"));
            page = loader.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        AbstractController ac = (AbstractController) loader.getController();
        ac.setApp(this);
        Scene scene = new Scene(page,
            stage.isShowing() ? stage.getScene().getWidth() : MINIMUM_WINDOW_WIDTH,
            stage.isShowing() ? stage.getScene().getHeight() : MINIMUM_WINDOW_HEIGHT);
        stage.setScene(scene);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
