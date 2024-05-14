package org.martincorp.Interface;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.martincorp.Database.DBActions;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class MainController {
    //@FXML objects section:
    @FXML private Menu backMenu;
    @FXML private Menu tableMenu;
    @FXML private MenuItem reloadItem;
    @FXML private Menu companyMenu;
    @FXML private MenuItem addItem;
    @FXML private MenuItem editItem;
    @FXML private MenuItem deleteItem;
    @FXML private MenuItem optionItem;
    @FXML private MenuItem aboutItem;

    @FXML private Label backLabel;
    @FXML private Label tableLabel;

    @FXML private Label connText;
    @FXML private Circle connCircle;
    @FXML private Label compNumText;


    //Other variables section:
    private Stage window;
    private DBActions db;

    //Equivalent to main method when the controller is started:
    @FXML public void initialize(){
        initListeners();
        db = new DBActions();
        setup();

        backMenu.setGraphic(backLabel);
        backMenu.setDisable(true);
        reloadItem.setDisable(true);
    }

    //Misc. methods:
    public void setStage(Stage w){
        this.window = w;
    }

    //Listener initializer:
    public void initListeners(){
        //Table menu:
        tableLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e){
                MenuController.launchCompTable(window);
            }
        });

        //Corporation menu:
        addItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e){
                MenuController.launchCompAdd();
            }
        });
        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e){
                MenuController.launchCompDel();
            }
        });
        editItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e){
                MenuController.launchCompEdit();
            }
        });

        //Help menu:
        optionItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e){
                MenuController.launchSettings();
            }
        });
        aboutItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e){
                MenuController.launchAbout();
            }
        });

    }

    public void setup(){
        compNumText.setText(String.valueOf(db.getNextRow() - 1));
        
        if(db.test()){
            connText.setText("Online");
            Color green = Color.rgb(0, 255, 0);
            Platform.runLater( () -> connCircle.setFill(green));
        }
        else{
            Color red = Color.rgb(255, 0, 0);
            Platform.runLater( () -> connCircle.setFill(red));
        }
    }

    public void showTable(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/table.fxml"));
            Parent root = loader.load();

            TableController tCont = loader.getController();
            tCont.setStage(window);
            Scene newScene = new Scene(root);

            Platform.runLater( () -> this.window.setScene(newScene));
        }
        catch(IOException ioe){
            ioe.printStackTrace();
            GUI.launchMessage(2, "Error de interfaz", "No se ha podido cargar la ventana seleccionada del programa.");
        }
    }

    public void showAbout(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/about.fxml"));
            Parent root = loader.load();

            Stage newWindow = new Stage();
            Scene aboutScene = new Scene(root);

            AboutController aCont = loader.getController();
            aCont.setStage(newWindow);

            URL iconURL = getClass().getResource("/Img/icon.png");
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

    public void addComp(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/add.fxml"));
            Parent root = loader.load();

            Stage newWindow = new Stage();
            Scene newScene = new Scene(root);

            AddController aCont = loader.getController();
            aCont.setStage(newWindow);

            URL iconURL = getClass().getResource("/Img/icon.png");
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

    public void deleteComp(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/delete.fxml"));
            Parent root = loader.load();

            Stage newWindow = new Stage();
            Scene newScene = new Scene(root);

            DeleteController rCont = loader.getController();
            rCont.setWindow(newWindow);

            URL iconURL = getClass().getResource("/Img/icon.png");
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

    public void editComp(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/edit.fxml"));
            Parent root = loader.load();

            Stage newWindow = new Stage();
            Scene newScene = new Scene(root);

            EditController eCont = loader.getController();
            eCont.setStage(newWindow);

            URL iconURL = getClass().getResource("/Img/icon.png");
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
}