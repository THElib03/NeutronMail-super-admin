package org.martincorp.Interface;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import org.martincorp.Database.Bridge;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.scene.image.*;

public class GUI extends Application {
    // Variables:
    public static Stage win;
    private Scene startScene;
    private static Bridge bridge;
    private StartController sCont;

    public static void main(String[] args) {
        launch(args);
    }

    // Launch method:
    @Override
    public void start(Stage window) {
        //I can't do these in main() 'cause they use launchMessage() and that one needs a JavaFX thread:
        /* SettingsParser settingsLoad = new SettingsParser("settings.set");*/
        startDb();

        //Window launch:
        URL iconURL = getClass().getResource("/Img/icon.png");
        window.getIcons().add(new Image(iconURL.toExternalForm()));// "main/java/org/martincorp/Img/icon.png", window.getWidth(), window.getHeight(), true, true));
        window.setTitle("Centralita de NeutronMail");

        //Window size:
        window.setWidth(1366);
        window.setHeight(768);

        MenuController.launchStart(window);
    }

    //Methods:
    /*
     * Displays a range of alert popup windows.
     * Requires the type of alert to be shown, this can be:
     * 1 - CONFIRMATION alert, will display a message asking for a user's choice
     * 2 - ERROR alert, will display a message suggestion that something has gone wrong.
     * 3 - INFORMATION alert, will display a message giving some extra information.
     * 4 - NONE alert, will display a message without any default properties. -It's use is not recommended, it works fine but it cannot be closed by the user.
     * 5 - WARNING alert, will display a message about a possible consecuence of a certain action.
     *
     * When user input is needed in an alert, the next structure is recommended or is the simplest:
        Optional<ButtonType> save = LoginUI.launchMessage(5, "Advertencia", "Se han realizado cambios sin guardar\n¿Desea guardar los cambios?'");
        if(save.isPresent() && save.get() == ButtonType.OK){
            parser.saveProfile();
        }
     * The actions/input of an alert can be saved into a Optional<ButtonType> object and then be used for further actions.
     */
    public static Optional<ButtonType> launchMessage(int type, String mainMess, String contextMess) {
        switch (type) {
            case 1:
                Alert conAlert = new Alert(AlertType.CONFIRMATION, mainMess, null);
                conAlert.setHeaderText(mainMess);
                conAlert.setContentText(contextMess);

                return conAlert.showAndWait();
            case 2:
                Alert errAlert = new Alert(AlertType.ERROR, mainMess, null);
                errAlert.setHeaderText(mainMess);
                errAlert.setContentText(contextMess);

                return errAlert.showAndWait();
            case 3:
                Alert infAlert = new Alert(AlertType.INFORMATION, mainMess, null);
                infAlert.setHeaderText(mainMess);
                infAlert.setContentText(contextMess);

                return infAlert.showAndWait();
            case 4:
                Alert nonAlert = new Alert(AlertType.NONE, mainMess, null);
                nonAlert.setHeaderText(mainMess);
                nonAlert.setContentText(contextMess);

                return nonAlert.showAndWait();
            case 5:
                Alert warAlert = new Alert(AlertType.WARNING, mainMess, null);
                warAlert.setHeaderText(mainMess);
                warAlert.setContentText(contextMess);

                return warAlert.showAndWait();
            default:
                return launchMessage(2, "Error de alerta", "Se ha lanzado una alerta no válida");
        }
    }

    // 
    public static void restartSettings(String p){
        Optional<ButtonType> res = GUI.launchMessage(1, "No hay configuración presente", "¿Desea crear un nuevo archivo de\nconfiguración con valores por defecto?");
        
        if(res.isPresent() && res.get() == ButtonType.OK){
            // SettingsParser newSettings = new SettingsParser("settings.set");
        }
    }

    public static void startDb(){
        Thread t = new Thread( () -> {
            try{
                bridge = new Bridge("companies");
                bridge.startConnection();
            }
            catch(NullPointerException npe){
                npe.printStackTrace();
                retryDatabase(npe.getMessage());
            }
        });
        t.run();
    }


    public static void retryDatabase(String message){
        Optional<ButtonType> save = launchMessage(1, "Error Interno", "Ha ocurrido un error al conectar con la base de\ndatos o esta se encuentra desconectada:\n" + message + "\n\n¿Desea reintentar la conexión?");
        if(save.isPresent() && save.get() == ButtonType.OK){
            startDb();
        }
    }
}
