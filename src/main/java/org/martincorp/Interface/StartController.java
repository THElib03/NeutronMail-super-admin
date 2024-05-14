package org.martincorp.Interface;

import java.io.IOException;
import java.net.URL;

import org.martincorp.Database.Bridge;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class StartController {
    // @FXML objects section:
    private @FXML BorderPane scene;
    private @FXML ImageView logoView;

    // Other variables section:
    private static Stage window;
    private int time;

    // Equivalent to main method when the controller is started:
    @FXML
    public void initialize() {}

    //Misc. methods:
    public static void setStage(Stage w) {
        window = w;
    }

    public void setLogo(){
        URL logoURL = getClass().getResource("/Img/logo.png");
        Image logo = new Image(logoURL.toExternalForm(), window.getWidth(), window.getHeight(), true, true);
        logoView.setImage(logo);
    }

    public void launchMain(int time){
        try{               
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/main.fxml"));
            Parent root = loader.load();

            MainController mCont = loader.getController();
            Scene newScene = new Scene(root, window.getWidth(), window.getHeight());
            mCont.setStage(window);

            Thread.sleep(time);
            Platform.runLater( () -> window.setScene(newScene));
        }
        catch(InterruptedException ie){
            ie.printStackTrace();
            GUI.launchMessage(2, "Error del sistema", "Ha ocurrido un error con el manejo de procesos.\n\n" + ie.getMessage());
        }
        catch(IOException ioe){
            ioe.printStackTrace();
            GUI.launchMessage(2, "Error de interfaz", "No se ha modido cargar la ventana seleccionada del programa.\n\n" + ioe.getMessage());
        }
        catch(IllegalStateException ise){
            ise.printStackTrace();
            GUI.launchMessage(2, "Error de interfaz", "No se ha modido cargar la ventana seleccionada del programa.\n\n" + ise.getMessage());
        }
    }
}