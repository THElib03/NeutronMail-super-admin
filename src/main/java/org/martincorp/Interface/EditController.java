package org.martincorp.Interface;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.martincorp.Codec.Encrypt;
import org.martincorp.Database.DBActions;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class EditController {
    //@FXML objects section:
    @FXML TextField nameField;
    @FXML TextField passField;
    @FXML TextField searchField;
    @FXML Button cancelBut;
    @FXML Button approveBut;
    @FXML Button searchBut;

    //Other variables section:
    private Stage stage;
    private DBActions db;
    private Encrypt enc;
    
    private int activeID;
    private String activeDB;
    private byte[] activePass;

    private String newDB;
    private String newPass;

    //Equivalent to main method when the controller is loaded:
    @FXML
    public void initialize(){
        db = new DBActions();
        enc = new Encrypt(2);
        initListeners();
    }

    //Misc. methods:
    public void initListeners(){
        nameField.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent e){
                if(e.getCode() == KeyCode.ENTER){
                    launchEdit();
                }
            }
        });
        passField.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent e){
                if(e.getCode() == KeyCode.ENTER){
                    launchEdit();
                }
            }
        });
        searchField.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent e){
                if(e.getCode() == KeyCode.ENTER){
                    searchComp();
                }
            }
        });

        cancelBut.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e){
                stage.close();
            }
        });
        approveBut.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e){
                launchEdit();
            }
        });
        searchBut.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e){
                searchComp();
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void searchComp(){
        try{
            int id = Integer.valueOf(searchField.getText());
            ResultSet res = db.findComp(id);
            if(res.next()){
                this.activeID = res.getInt(1);
                this.activeDB = res.getString(2);
                this.activePass = res.getBytes(3);

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
        this.activeID = id;
        this.activeDB = db;
        this.activePass = pass.getBytes();

        searchField.setText(String.valueOf(id));
        nameField.setText(db);
        passField.setText(pass.toString());
    }

    public void launchEdit(){
        if(activeDB != null && String.valueOf(activeID) != null){
            if(!nameField.getText().equals("") && !passField.getText().equals("")){
                newDB = nameField.getText();
                newPass = passField.getText();

                //Salted password generation:
                byte[] salt = enc.getSalt();
                byte[] hash = enc.saltedHash(newPass.toCharArray(), salt);
                byte[] key = new byte[salt.length + hash.length];
                System.arraycopy(salt, 0, key, 0, salt.length);
                System.arraycopy(hash, 0, key, salt.length, hash.length);


                boolean tableDone = db.editComp(activeID, newDB, key);
                boolean dbDone = db.editDB(activeDB, newDB);

                if(tableDone && dbDone){
                    GUI.launchMessage(3, "Éxito", "Se han editado los datos de la corporación indicada.");
                    stage.close();
                }
                else if(tableDone && !dbDone){
                    db.editComp(activeID, activeDB, activePass);
                }
            }
            else{
                GUI.launchMessage(5, "Advertencia", "Uno de los campos obligatorios está vacío.");
            }
        }
        else{
            GUI.launchMessage(5, "", "No se ha seleccionado ninguna compañia para eliminar.");
        }
    }
}
