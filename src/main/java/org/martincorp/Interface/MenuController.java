package org.martincorp.Interface;

import java.io.IOException;
import java.net.URL;

import org.martincorp.Database.DBActions;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MenuController {
    //Variables:
    static DBActions db = new DBActions();

    //Methods:
    public static void launchStart(Stage window){
        try {
            FXMLLoader loader = new FXMLLoader(GUI.class.getResource("/Fxml/start.fxml"));
            Parent root = loader.load();

            StartController sCont = loader.getController();
            sCont.setStage(window);
            Scene startScene = new Scene(root);
            window.setScene(startScene);
            window.show();

            sCont.launchMain(750);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            GUI.launchMessage(2, "Error de interfaz", "No se ha modido cargar la siguiente ventana del programa.");
        }
    }

    public static void launchMain(Stage window){
        try{
            FXMLLoader loader = new FXMLLoader(GUI.class.getResource("/Fxml/main.fxml"));
            Parent root = loader.load();

            MainController mCont = loader.getController();
            Scene newScene = new Scene(root);
            mCont.setStage(window);

            Platform.runLater( () -> window.setScene(newScene));
        }
        catch(IOException ioe){
            ioe.printStackTrace();
            GUI.launchMessage(2, "Error de interfaz", "No se ha modido cargar la ventana seleccionada del programa.\n\n" + ioe.getMessage());
        }
    }

    public static void launchCompTable(Stage window){
        try{
            FXMLLoader loader = new FXMLLoader(GUI.class.getResource("/Fxml/table.fxml"));
            Parent root = loader.load();

            TableController tCont = loader.getController();
            tCont.setStage (window);
            Scene newScene = new Scene(root);
            Platform.runLater( () -> window.setScene(newScene));
        }
        catch(IOException ioe){
            ioe.printStackTrace();
            GUI.launchMessage(2, "Error de interfaz", "No se ha modido cargar la ventana seleccionada del programa.\n\n" + ioe.getMessage());
        }
    }

    public static void launchCompAdd(){
        try{
            FXMLLoader loader = new FXMLLoader(GUI.class.getResource("/Fxml/add.fxml"));
            Parent root = loader.load();

            Stage newWindow = new Stage();
            Scene newScene = new Scene(root);

            AddController aCont = loader.getController();
            aCont.setStage(newWindow);

            URL iconURL = GUI.class.getResource("/Img/icon.png");
            newWindow.getIcons().add(new Image(iconURL.toExternalForm()));
            newWindow.setTitle("Añadir nueva empresa");
            newWindow.setWidth(640);
            newWindow.setHeight(480);
            newWindow.initModality(Modality.WINDOW_MODAL);
            newWindow.setScene(newScene);
            newWindow.show();
        }
        catch(IOException ioe){
            ioe.printStackTrace();
            GUI.launchMessage(2, "Error de interfaz", "No se ha modido cargar la ventana seleccionada del programa.");
        }
    }

    public static void launchCompEdit(){
        try{
            FXMLLoader loader = new FXMLLoader(GUI.class.getResource("/Fxml/edit.fxml"));
            Parent root = loader.load();

            Stage newWindow = new Stage();
            Scene newScene = new Scene(root);

            EditController eCont = loader.getController();
            eCont.setStage(newWindow);

            URL iconURL = GUI.class.getResource("/Img/icon.png");
            newWindow.getIcons().add(new Image(iconURL.toExternalForm()));
            newWindow.setTitle("Eliminar empresa");
            newWindow.setWidth(640);
            newWindow.setHeight(480);
            newWindow.initModality(Modality.WINDOW_MODAL);
            newWindow.setScene(newScene);
            newWindow.show();
        }
        catch(IOException ioe){
            ioe.printStackTrace();
            GUI.launchMessage(2, "Error de interfaz", "No se ha modido cargar la ventana seleccionada del programa.");
        }
    }

    public static void launchCompDel(){
        try{
            FXMLLoader loader = new FXMLLoader(GUI.class.getResource("/Fxml/delete.fxml"));
            Parent root = loader.load();

            Stage newWindow = new Stage();
            Scene newScene = new Scene(root);

            DeleteController rCont = loader.getController();
            rCont.setWindow(newWindow);

            URL iconURL = GUI.class.getResource("/Img/icon.png");
            newWindow.getIcons().add(new Image(iconURL.toExternalForm()));
            newWindow.setTitle("Editar empresa existente");
            newWindow.setWidth(640);
            newWindow.setHeight(480);
            newWindow.initModality(Modality.WINDOW_MODAL);
            newWindow.setScene(newScene);
            newWindow.show();
        }
        catch(IOException ioe){
            ioe.printStackTrace();
            GUI.launchMessage(2, "Error de interfaz", "No se ha modido cargar la ventana seleccionada del programa.");
        }
    }

    public static void launchSettings(){
        try{
            FXMLLoader loader = new FXMLLoader(GUI.class.getResource("/Fxml/settings.fxml"));
            Parent root = loader.load();

            Stage newWindow = new Stage();
            Scene aboutScene = new Scene(root);

            AboutController aCont = loader.getController();
            aCont.setStage(newWindow);

            URL iconURL = GUI.class.getResource("/Img/icon.png");
            newWindow.getIcons().add(new Image(iconURL.toExternalForm()));
            newWindow.setTitle("Ajustes");
            newWindow.setWidth(640);
            newWindow.setHeight(480);
            newWindow.initModality(Modality.WINDOW_MODAL);
            newWindow.setScene(aboutScene);
            newWindow.showAndWait();
        }
        catch(IOException ioe){
            ioe.printStackTrace();
            GUI.launchMessage(2, "Error de interfaz", "No se ha modido cargar la ventana seleccionada del programa.\n\n" + ioe.getMessage());
        }
    }

    public static void launchAbout(){
        try{
            FXMLLoader loader = new FXMLLoader(GUI.class.getResource("/Fxml/about.fxml"));
            Parent root = loader.load();

            Stage newWindow = new Stage();
            Scene aboutScene = new Scene(root);

            AboutController aCont = loader.getController();
            aCont.setStage(newWindow);

            URL iconURL = GUI.class.getResource("/Img/icon.png");
            newWindow.getIcons().add(new Image(iconURL.toExternalForm()));
            newWindow.setTitle("Sobre el cliente de NeutronMail para Administradores");
            newWindow.setWidth(640);
            newWindow.setHeight(480);
            newWindow.initModality(Modality.WINDOW_MODAL);
            newWindow.setScene(aboutScene);
            newWindow.showAndWait();
        }
        catch(IOException ioe){
            ioe.printStackTrace();
            GUI.launchMessage(2, "Error de interfaz", "No se ha modido cargar la ventana seleccionada del programa.\n\n" + ioe.getMessage());
        }
    }

    //DONE: add the rest of launchers.
}
