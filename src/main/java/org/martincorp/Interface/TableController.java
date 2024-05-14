package org.martincorp.Interface;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.martincorp.Database.DBActions;
import org.martincorp.Model.Company;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TableController {
    //@FXML objects section:
    @FXML private BorderPane root;

    @FXML private Menu backMenu;
    @FXML private Label backLabel;
    @FXML private Menu tableMenu;
    @FXML private MenuItem reloadItem;
    @FXML private Menu companyMenu;
    @FXML private MenuItem addItem;
    @FXML private MenuItem editItem;
    @FXML private MenuItem deleteItem;
    @FXML private MenuItem optionItem;
    @FXML private MenuItem aboutItem;

    @FXML private MenuItem contextAdd;
    @FXML private MenuItem contextEdit;
    @FXML private MenuItem contextDelete;

    @FXML private TableView<Company> table;
    @FXML private TableColumn<Company, Integer> idCol;
    @FXML private TableColumn<Company, String> nameCol;
    @FXML private TableColumn<Company, String> passCol;
    @FXML private TableColumn<Company, String> dateCol;

    //Other variables section:
    private Stage window;
    private Scene scene;
    private DBActions db;

    private DeleteController dCont;
    private EditController eCont;

    //Equivalent to main method when the controller is started:
    @FXML
    public void initialize(){
        backMenu.setGraphic(backLabel);
        db = new DBActions();
        initListeners();

        loadTable();
    }

    //Listener initializer:
    public void initListeners(){
        //Back menu:
        backLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e){
                MenuController.launchMain(window);
            }
        });

        //Table menu:
        reloadItem.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                loadTable();
            }
        });

        //Corporation menu:
        addItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e){
                MenuController.launchCompAdd();
            }
        });
        editItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e){
                MenuController.launchCompEdit();
            }
        });
        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e){
                MenuController.launchCompDel();
            }
        });

        //Help menu:
        optionItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e){
                System.out.println("Wish I could've finished this one.");
            }
        });
        aboutItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e){
                MenuController.launchAbout();
            }
        });

        //Context menu:
        contextAdd.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                MenuController.launchCompAdd();
            }
        });
        contextEdit.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                ObservableList<TablePosition> selectionList = table.getSelectionModel().getSelectedCells();
            
                if(selectionList.size() > 0){
                    editComp();
                    TablePosition selection = selectionList.get(0);
                    int selectionRow = selection.getRow();
                    Company selected = table.getItems().get(selectionRow);

                    eCont.extSetup(selected.getID(), selected.getName(), selected.getPass());
                }
            }
        });
        contextDelete.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                ObservableList<TablePosition> selectionList = table.getSelectionModel().getSelectedCells();
            
                if(selectionList.size() > 0){
                    deleteComp();
                    TablePosition selection = selectionList.get(0);
                    int selectionRow = selection.getRow();
                    Company selected = table.getItems().get(selectionRow);

                    dCont.extSetup(selected.getID(), selected.getName(), selected.getPass());
                }
            }
        });
    }

    //Misc. methods:
    public void setStage(Stage w){
        this.window = w;
        scene = window.getScene();
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
            newWindow.initModality(Modality.APPLICATION_MODAL);
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

            dCont = loader.getController();
            dCont.setWindow(newWindow);

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

            eCont = loader.getController();
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

    public void loadTable(){
        List<Company> companies = new ArrayList<Company>();
        ResultSet comps = db.getCompRows();

        if(comps != null){
            try{
                while(comps.next()){
                    Company newComp = new Company(comps.getInt(1), comps.getString(2), new String(comps.getBytes(3), StandardCharsets.UTF_8), comps.getTimestamp(4).toString());
                    companies.add(newComp);
                }
            }
            catch(SQLException sqle){
                sqle.printStackTrace();
                //GUI.launchMessage();
            }

            ObservableList<Company> tableComp = FXCollections.observableArrayList(companies);
            table.setItems(tableComp);

            idCol.setCellValueFactory(new PropertyValueFactory<>(companies.get(0).IDProperty().getName()));
            nameCol.setCellValueFactory(new PropertyValueFactory<>(companies.get(0).nameProperty().getName()));
            passCol.setCellValueFactory(new PropertyValueFactory<>(companies.get(0).passProperty().getName()));
            dateCol.setCellValueFactory(new PropertyValueFactory<>(companies.get(0).dateProperty().getName()));
        }
        else{
            System.out.println("No company data could be retrieved.");
            GUI.launchMessage(2, "Error de base de datos", "No se ha podido cargar ningun empleado\ndesde la base de datos");
        }
    }
}
