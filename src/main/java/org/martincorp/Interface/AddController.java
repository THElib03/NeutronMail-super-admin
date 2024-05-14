package org.martincorp.Interface;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import org.martincorp.Codec.Encrypt;
import org.martincorp.Database.DBActions;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AddController {
    //@FXML objects section:
    @FXML TextField nameField;
    @FXML TextField passField;
    @FXML Button cancelBut;
    @FXML Button approveBut;

    //Other variables section:
    private Stage stage;
    private DBActions db;
    private Encrypt enc;

    //Equivalent to main method when the controller is started:
    @FXML
    public void initialize(){
        db = new DBActions();
        enc = new Encrypt(2);
        initListeners();
    }

    //Object's listeners initializer:
    public void initListeners(){
        cancelBut.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e){
                stage.close();
            }
        });
        cancelBut.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e){
                if(e.getCode() == KeyCode.ENTER){
                    stage.close();
                }
            }
        });
        approveBut.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e){
                newComp();
            }
        });
        approveBut.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e){
                if(e.getCode() == KeyCode.ENTER){
                    newComp();
                }
            }
        });
        nameField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e){
                if(e.getCode() == KeyCode.ENTER){
                    newComp();
                }
            }
        });
        passField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e){
                if(e.getCode() == KeyCode.ENTER){
                    newComp();
                }
            }
        });
    }

    //Misc. methods:
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void newComp(){
        if(!nameField.getText().equals("") && !passField.getText().equals("")){
            String name = nameField.getText();
            String pass = passField.getText();

            //Salted password generation:
            byte[] salt = enc.getSalt();
            byte[] hash = enc.saltedHash(pass.toCharArray(), salt);
            byte[] key = new byte[salt.length + hash.length];
            System.arraycopy(salt, 0, key, 0, salt.length);
            System.arraycopy(hash, 0, key, salt.length, hash.length);

            boolean compAdded = db.addComp(name, key);
            boolean dbAdded = db.addDB(name, new String(key, StandardCharsets.UTF_8));
            
            if(compAdded && dbAdded){
                GUI.launchMessage(3, "Éxito", "Nueva corporación y base de datos " + name + " registrada.");
                this.stage.close();
            }
            else if(!compAdded || !dbAdded){
                db.deleteComp(db.getNextRow());
                db.deleteDB(name);
            }
        }
        else{
            GUI.launchMessage(5, "Advertencia", "Uno de los campos obligatorios está vacío.");
        }
    }
}