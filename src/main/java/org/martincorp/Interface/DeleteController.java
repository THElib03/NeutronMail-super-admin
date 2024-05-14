package org.martincorp.Interface;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.martincorp.Database.DBActions;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
 
public class DeleteController {
    //@FXML objects section:
    @FXML TextField nameField;
    @FXML TextField passField;
    @FXML TextField searchField;
    @FXML Button searchBut;
    @FXML Button cancelBut;
    @FXML Button approveBut;

    //Other variables section:
    private Stage window;
    private DBActions db;
    private int activeId;
    private String activeDb;

    //Equivalent to main method when the controller is loaded:
    @FXML
    public void initialize(){
        db = new DBActions();
        initListeners();

        nameField.setDisable(true);
        passField.setDisable(true);
    }

    //Misc. methods:
    public void initListeners(){
        searchField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e){
                if(e.getCode() == KeyCode.ENTER){
                    compSearch();
                }
            }
        });
        searchBut.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e){
                compSearch();
            }
        });
        searchBut.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e){
                if(e.getCode() == KeyCode.ENTER){
                    compSearch();
                }
            }
        });
        
        approveBut.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e){
                compDelete();
            }
        });
        approveBut.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e){
                if(e.getCode() == KeyCode.ENTER){
                    compDelete();
                }
            }
        });

        cancelBut.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e){
                window.close();
            }
        });
        cancelBut.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e){
                window.close();
            }
        });
    }

    public void setWindow(Stage stage) {
        this.window = stage;
    }

    public void compDelete(){
        if(String.valueOf(activeId) != null && activeDb != null){
            boolean tableDone = db.deleteComp(activeId);
            boolean dbDone = db.deleteDB(activeDb);

            if(tableDone && dbDone){
                GUI.launchMessage(3, "Éxito", "La compañia indicada ha sido eliminada.");
                window.close();
            }
            else if(tableDone && !dbDone){
                boolean deleteDone = db.deleteComp(activeId);

                if(deleteDone){
                    
                }
            }
        }
        else{
            GUI.launchMessage(5, "", "No se ha seleccionado ninguna compañia para eliminar.");
        }
    }

    public void compSearch(){
        try{
            int id = Integer.valueOf(searchField.getText());
            ResultSet res = db.findComp(id);
            
            if(res.next()){
                this.activeId = res.getInt(1);
                this.activeDb = res.getString(2);
                nameField.setText(res.getString(2));
                passField.setText(res.getString(3));
            }
        }
        catch(NumberFormatException nfe){
            nfe.printStackTrace();
            GUI.launchMessage(5, "Advertencia", "Solo se permiten valores numéricos para buscar compañías.");
        }
        catch(SQLException sqle){
            sqle.printStackTrace();
            GUI.launchMessage(2, "Error de base de datos", "Ha ocurrido un error durante la búsqueda.");
        }
    }

    public void extSetup(int id, String db, String pass){
        this.activeId = id;
        this.activeDb = db;

        if(String.valueOf(activeId) == null || activeDb == null){
            System.out.println(activeDb + ", " + activeId);
        }

        searchField.setText(String.valueOf(id));
        nameField.setText(db);
        passField.setText(pass);
    }
}
