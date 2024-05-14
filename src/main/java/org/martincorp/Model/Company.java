package org.martincorp.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class Company {
    //Variables:
    private IntegerProperty ID;
    private StringProperty name, pass, date;

    //Builder:
    public Company(int i, String n, String p, String d){
        setID(i);
        setName(n);
        setPass(p);
        setDate(d);
    }

    //Methods:
    public String toString(){
        String string = "Número de empresa: " + getID()
                      + "\nNombre de la empresa: " + getName()
                      + "\nContraseña decodificada: " + getPass()
                      + "\nFecha de suscripción: " + getDate();

        return string;
    }

    public void setID(int i){
        IDProperty().set(i);
    }

    public Integer getID(){
        return IDProperty().getValue();
    }

    public IntegerProperty IDProperty(){
        if(ID == null){
            ID = new SimpleIntegerProperty(this, "ID");
        }

        return ID;
    }

    public void setName(String n){
        nameProperty().set(n);
    }

    public String getName(){
        return nameProperty().getValue();
    }

    public StringProperty nameProperty(){
        if(name == null){
            name = new SimpleStringProperty(this, "name");
        }

        return name;
    }

    public void setPass(String n){
        passProperty().set(n);
    }

    public String getPass(){
        return passProperty().getValue();
    }

    public StringProperty passProperty(){
        if(pass == null){
            pass = new SimpleStringProperty(this, "pass");
        }

        return pass;
    }

    public void setDate(String n){
        dateProperty().set(n);
    }

    public String getDate(){
        return dateProperty().getValue();
    }

    public StringProperty dateProperty(){
        if(date == null){
            date = new SimpleStringProperty(this, "date");
        }

        return date;
    }
}