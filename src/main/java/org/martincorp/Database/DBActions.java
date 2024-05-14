package org.martincorp.Database;

import java.sql.*;
import org.martincorp.Interface.*;

public class DBActions {
    //Variables:
    private Bridge bridge;

      //Query variables:
    private final String ROWS;
    private final String ROWS_NUM;
    private final String ADD;
    private final String DELETE;
    private final String EDIT;

    private final String ADD_DB = "CREATE DATABASE ";
    private final String DELETE_DB = "DROP DATABASE IF EXISTS ";
    private final String USE = "USE ";

    private final String EMP_TABLE = "CREATE TABLE employee("
                                    + "emp_id INTEGER PRIMARY KEY AUTO_INCREMENT, "
                                    + "emp_fname VARCHAR(20) NOT NULL, "
                                    + "emp_lname VARCHAR(40) NOT NULL, "
                                    + "emp_sDate DATE NOT NULL DEFAULT '2003-10-16', "
                                    + "emp_eDate DATE DEFAULT '2024-02-24', "
                                    + "emp_alias VARCHAR(50) NOT NULL, "
                                    + "emp_email VARCHAR(40) NOT NULL"
                                    + ")";
    private final String CHAT_TABLE = "CREATE TABLE chat("
                                    + "chat_id INT PRIMARY KEY AUTO_INCREMENT, "
                                    + "chat_user1 INT, "
                                    + "chat_user2 INT, "
                                    + "CONSTRAINT firstEmp FOREIGN KEY (chat_user1) REFERENCES employee(emp_id) ON UPDATE CASCADE ON DELETE SET NULL, "
                                    + "CONSTRAINT secondEmp FOREIGN KEY (chat_user2) REFERENCES employee(emp_id) ON UPDATE CASCADE ON DELETE SET NULL "
                                    + ")";
    private final String GROUP_TABLE = "CREATE TABLE publicgroup("
                                    + "grp_id INT PRIMARY KEY AUTO_INCREMENT, "
                                    + "grp_name VARCHAR(60) DEFAULT 'Nuevo Grupo', "
                                    + "grp_pass BLOB NOT NULL, "
                                    + "grp_owner INT, "
                                    + "CONSTRAINT groupOwner FOREIGN KEY (grp_owner) REFERENCES employee(emp_id) ON UPDATE CASCADE ON DELETE SET NULL "
                                    + ")";
    private final String GROUP_USER_TABLE = "CREATE TABLE groupuser("
                                    + "gru_group INT NOT NULL, "
                                    + "gru_user INT NOT NULL, "
                                    + "CONSTRAINT groupFK FOREIGN KEY (gru_group) REFERENCES publicgroup(grp_id) ON UPDATE CASCADE ON DELETE CASCADE, "
                                    + "CONSTRAINT userFK FOREIGN KEY (gru_user) REFERENCES employee(emp_id) ON UPDATE CASCADE ON DELETE CASCADE"
                                    + ")";
    private final String CERT_TABLE = "CREATE TABLE certificate("
                                    + "cert_id INT PRIMARY KEY AUTO_INCREMENT,"
                                    + "cert_emp INTEGER, "
                                    + "cert_group INTEGER, "
                                    + "cert_public_key BLOB, "
                                    + "cert_private_key BLOB, "
                                    + "CONSTRAINT emp_cert_FK FOREIGN KEY (cert_emp) REFERENCES employee(emp_id) ON UPDATE CASCADE ON DELETE SET NULL, "
                                    + "CONSTRAINT group_cert_FK FOREIGN KEY (cert_group) REFERENCES publicgroup(grp_id) ON UPDATE CASCADE ON DELETE SET NULL"
                                    + ")";
    private final String MESS_TABLE = "CREATE TABLE message("
                                    + "mes_id INT PRIMARY KEY AUTO_INCREMENT, "
                                    + "mes_chat INT, "
                                    + "mes_group INT, "
                                    + "mes_sender INT NOT NULL, "
                                    + "mes_receiver INT, "
                                    + "mes_message TEXT, "
                                    + "mes_filename VARCHAR(270), "
                                    + "mes_file LONGBLOB,"
                                    + "mes_hash BOOLEAN NOT NULL DEFAULT 0, "
                                    + "mes_sendTime TIMESTAMP NOT NULL DEFAULT now(),"
                                    + "mes_status BOOLEAN NOT NULL DEFAULT 0, "
                                    + "CONSTRAINT chatId FOREIGN KEY (mes_chat) REFERENCES chat(chat_id) ON UPDATE CASCADE ON DELETE SET NULL, "
                                    + "CONSTRAINT groupId FOREIGN KEY (mes_group) REFERENCES publicgroup(grp_id) ON UPDATE CASCADE ON DELETE SET NULL, "
                                    + "CONSTRAINT senderId FOREIGN KEY (mes_sender) REFERENCES employee(emp_id) ON UPDATE CASCADE ON DELETE CASCADE,"
                                    + "CONSTRAINT receiverId FOREIGN KEY (mes_receiver) REFERENCES employee(emp_id) ON UPDATE CASCADE ON DELETE SET NULL"
                                    + ")";
    private final String ACTV_TABLE = "CREATE TABLE `active`("
                                    + "act_emp INTEGER, "
                                    + "act_logged BOOLEAN DEFAULT 0, "
                                    + "CONSTRAINT emp_FK FOREIGN KEY (act_emp) REFERENCES employee(emp_id) ON UPDATE CASCADE ON DELETE CASCADE"
                                    + ")";
    //TODO: rewrite message to include a proper message text, filename shouldn't be the message AND the windows limit is at 255 characters for the WHOLE path (fucking retrocompatibility).
    private final String SAMPLE_EMP = "INSERT INTO employee VALUES(1, '', '', '2000-01-01', '2000-01-01', '', '')";
    private final String SAMPLE_CHAT = "INSERT INTO chat VALUES(1, 1, 1)";
    private final String SAMPLE_ACT = "INSERT INTO `active` VALUES(1, 0)";

    private final String ADD_ADMIN = "CREATE USER ?@'%' IDENTIFIED BY ?";
    private final String EDIT_ADMIN = "RENAME USER ?@'%' TO ?@'%'";
    private final String DELETE_ADMIN = "DROP USER IF EXISTS ?";

    private final String TABLES = "SHOW TABLES FROM ";
    private final String FLUSH = "FLUSH PRIVILEGES";

    //Builder:
    public DBActions(){
        bridge = new Bridge();

        if(!bridge.connected){
            GUI.launchMessage(2, "Error de la base de datos", "La base de datos no se encuentra conectada.");
        }

        ROWS = "SELECT * FROM " + bridge.getDB() + ".company;";
        ROWS_NUM = "SELECT MAX(com_id) FROM " + bridge.getDB() + ".company";
        ADD = "INSERT INTO " + bridge.getDB() + ".company VALUES(?, ?, ?, ?)";
        DELETE = "DELETE FROM " + bridge.getDB() + ".company WHERE com_id=?";
        EDIT = "UPDATE " + bridge.getDB() + ".company SET com_name=?, com_pass=? WHERE com_id=?";
    }

    //Methods:
      //Various getters of general DB info:
    public boolean test(){
        boolean connected = bridge.connected;
        return connected;
    }

    public ResultSet getCompRows(){
        ResultSet res = null;

        if(bridge.querier != null){
            try{
                res = bridge.querier.executeQuery(ROWS);

                return res;
            }
            catch(SQLException sqle){
                sqle.printStackTrace();
                GUI.launchMessage(2, "Error de base de datos", "Ha ocurrido un error al intentar cargar datos.");
                bridge.checkConnection();
            }
        }
        else{
            GUI.launchMessage(2, "Error de base de datos", "No se han podido cargar los datos dado que la base de datos se encuentra desconectada u otro error.");
        }

        return res;
    }

    public int getNextRow(){
        int next = 0;

        if(bridge.querier != null){
            try{
                ResultSet res = bridge.querier.executeQuery(ROWS_NUM);
                res.next();
                next = res.getInt(1);
            }
            catch(SQLException sqle){
                sqle.printStackTrace();
                GUI.launchMessage(2, "Error de base de datos", "Ha ocurrido un error al intentar cargar datos.");
                bridge.checkConnection();
            }
        }
        else{
            GUI.launchMessage(2, "Error de base de datos", "No se han podido cargar los datos dado que la base de datos se encuentra desconectada u otro error.");
        }

        return next + 1;
    }

    public ResultSet findComp(int id){
        ResultSet res = null;

        if(bridge.querier != null){
            try{
                res = bridge.querier.executeQuery("SELECT * FROM " + bridge.getDB() + ".company WHERE com_id LIKE " + id);
            }
            catch(SQLException sqle){
                sqle.printStackTrace();
                GUI.launchMessage(2, "Error de base de datos", "Ha ocurrido un error al intentar cargar los datos de las compañías.");
                bridge.checkConnection();
            }
        }
        else{
            GUI.launchMessage(2, "Error de base de datos", "No se han podido cargar los datos dado que la base de datos se encuentra desconectada u otro error.");
        }

        return res; 
    }

    public ResultSet getTables(String db){
        ResultSet res = null;

        if(bridge.querier != null){
            try{
                res = bridge.querier.executeQuery(TABLES + db);
            }
            catch(SQLException sqle){
                sqle.printStackTrace();
                GUI.launchMessage(2, "Error de base de datos", "Ha ocurrido un error al intentar cargar datos.");
                bridge.checkConnection();
            }
        }
        else{
            GUI.launchMessage(2, "Error de base de datos", "No se han podido cargar los datos dado que la base de datos se encuentra desconectada u otro error.");
        }

        return res;
    }

      //Methods that affect DB content:
    public boolean addComp(String name, byte[] pass){
        PreparedStatement insertSta;
        int exec = 0;
        boolean done = false;
        Timestamp cDate = new Timestamp(System.currentTimeMillis());

        if(bridge.querier != null){
            if(!name.equals("") && pass.length != 0){
                try{
                    insertSta = bridge.conn.prepareStatement(ADD);
                    insertSta.setInt(1, getNextRow());
                    insertSta.setString(2, name);
                    insertSta.setBytes(3, pass);
                    insertSta.setTimestamp(4, cDate);

                    exec = insertSta.executeUpdate();
                }
                catch(SQLException sqle){
                    sqle.printStackTrace();
                    GUI.launchMessage(2, "Error de base de datos", "Ha ocurrido un error durante la introducción de nuevos datos.");
                    bridge.checkConnection();
                }

                if(exec == 1){
                    System.out.println("New corporation registered.");
                    done = true;
                }
                else if(exec < 1){
                    System.out.println("No lines affected, no corporation registered.");
                    done = false;
                    GUI.launchMessage(2, "Error de base de datos", "No se ha registrado ninguna corporación en la base de datos.");
                }
                else if(exec > 1){
                    System.out.println("Too many lines affected.");
                    done = true;
                    GUI.launchMessage(3, "Éxito", "Se han registrado múltiples corporaciones (Error indefinido).");
                }
            }
            else{
                System.out.println("One or some of the fields are empty");
                done = false;
                GUI.launchMessage(5, "Datos incorrectos", "Uno de los campos obligatorios está vacío, por favor inténtelo de nuevo.");
            }
        }
        else{
            done = false;
            GUI.launchMessage(2, "Error de base de datos", "No se han podido cargar los datos dado que la base de datos se encuentra desconectada u otro error.");
        }

        return done;
    }

    public boolean deleteComp(int id){
        boolean done = false;
        PreparedStatement deleteSta;
        int exec = 0;

        if(bridge.querier != null){
            if(id >= 1){
                try{
                    deleteSta = bridge.conn.prepareStatement(DELETE);
                    deleteSta.setInt(1, id);
                    exec = deleteSta.executeUpdate();
                }
                catch(SQLException sqle){
                    sqle.printStackTrace();
                    GUI.launchMessage(2, "Error de base de datos", "");
                    bridge.checkConnection();
                }

                if(exec == 1){
                    System.out.println("Selected corporation deleted.");
                    done = true;
                }
                else if(exec < 1){
                    System.out.println("No lines affected, no corporation deleted.");
                    done = false;
                    GUI.launchMessage(2, "Error de base de datos", "No se ha borrado ninguna corporación de la base de datos.");
                }
                else if(exec > 1){
                    System.out.println("Too many lines affected.");
                    done = true;
                    GUI.launchMessage(3, "Éxito", "Se han borrado múltiples corporaciones (Error indefinido no esperado ).");
                }
            }
            else{
                done = false;
                GUI.launchMessage(2, "Error interno", "La compañía indicada a borrar no es válida.");
            }
        }
        else{
            done = false;
            GUI.launchMessage(2, "Error de base de datos", "No se ha podido borrar la compañia indicada dado que la base de datos se encuentra desconectada u otro error ha ocurrido.");
        }

        return done;
    }

    public boolean editComp(int id, String newName, byte[] newPass){
        boolean done = false;
        PreparedStatement editSta;
        int exec = 0;

        if(bridge.querier != null){
            if(id >= 1){
                if(!newName.equals("") && newPass.length != 0){
                    try{
                        editSta = bridge.conn.prepareStatement(EDIT);
                        editSta.setString(1, newName);
                        editSta.setBytes(2, newPass);
                        editSta.setInt(3, id);

                        exec = editSta.executeUpdate();
                    }
                    catch(SQLException sqle){
                        sqle.printStackTrace();
                        GUI.launchMessage(2, "Error de base de datos", "");
                        bridge.checkConnection();
                    }

                    if(exec == 1){
                        System.out.println("Selected corporation edited.");
                        done = true;
                    }
                    else if(exec < 1){
                        System.out.println("No lines affected, no corporation deleted.");
                        done = false;
                        GUI.launchMessage(2, "Error de base de datos", "No se ha editado ninguna corporación de la base de datos.");
                    }
                    else if(exec > 1){
                        System.out.println("Too many lines affected.");
                        done = true;
                        GUI.launchMessage(3, "Éxito", "Se han editado múltiples corporaciones (Error indefinido no esperado ).");
                    }
                }
            }
            else{
                done = false;
                GUI.launchMessage(2, "Error interno", "La compañía indicada a editar no es válida.");
            }
        }
        else{
            done = false;
            GUI.launchMessage(2, "Error de base de datos", "No se han podido editar la compañia indicada dado que la base de datos se encuentra desconectada u otro error ha ocurrido.");
        }

        return done;
    }

    public boolean addDB(String name, String pass) {
        //TODO: i don't know if i can add from this app an employee for admin, i may need to copy the number generator and all that code.
        boolean done = false;
        PreparedStatement newAdmin;
        PreparedStatement grants;

        if(bridge.querier != null){
            try{
                bridge.querier.execute(ADD_DB + name);
                bridge.querier.execute(USE + name);

                //DONE: check if this order is correct & works, then keep testing.
                bridge.querier.execute(EMP_TABLE);
                bridge.querier.execute(CHAT_TABLE);
                bridge.querier.execute(GROUP_TABLE);
                bridge.querier.execute(GROUP_USER_TABLE);
                bridge.querier.execute(CERT_TABLE);
                bridge.querier.execute(MESS_TABLE);
                bridge.querier.execute(ACTV_TABLE);

                bridge.querier.execute(SAMPLE_EMP);
                bridge.querier.execute(SAMPLE_CHAT);
                bridge.querier.execute(SAMPLE_ACT);

                newAdmin = bridge.conn.prepareStatement(ADD_ADMIN);
                newAdmin.setString(1, name);
                newAdmin.setString(2, pass);
                System.out.println();
                newAdmin.execute();
                    
                grants = bridge.conn.prepareStatement("GRANT CREATE, SELECT, INSERT, UPDATE, DELETE ON " + name + ".* TO ?@'%' WITH GRANT OPTION");
                grants.setString(1, name);
                grants.execute();

                grants = bridge.conn.prepareStatement("GRANT INSERT ON companies.`user` TO ?@'%'");
                grants.setString(1, name);
                grants.execute();

                bridge.querier.execute("GRANT SELECT, UPDATE ON " + name + ".`active` TO 'empl_pass'@'%';");

                bridge.querier.execute("GRANT SELECT ON " + name + ".employee TO 'messager'@'%';");
                bridge.querier.execute("GRANT SELECT, UPDATE ON " + name + ".certificate TO 'messager'@'%'");
                bridge.querier.execute("GRANT SELECT, INSERT ON " + name + ".message TO 'messager'@'%'");
                bridge.querier.execute("GRANT SELECT, INSERT ON " + name + ".chat TO 'messager'@'%'");
                bridge.querier.execute("GRANT SELECT ON " + name + ".publicgroup TO 'messager'@'%'");
                bridge.querier.execute("GRANT SELECT ON " + name + ".groupuser TO 'messager'@'%'");

                bridge.querier.execute("GRANT INSERT, UPDATE, DELETE ON " + name + ".publicgroup TO 'grouper'@'%'");
                bridge.querier.execute("GRANT INSERT, DELETE ON " + name + ".groupuser TO 'grouper'@'%'");

                bridge.querier.execute(FLUSH);

                done = true;
                System.out.println("Selected corporation db added.");
            }
            catch(SQLException sqle){
                sqle.printStackTrace();
                GUI.launchMessage(2, "Error de base de datos", "Ha ocurrido un error durante la creación de la base de datos, es posible que ya exista una corporación de mismo nombre."); 
                bridge.checkConnection();
                done = false;
            }
        }
        else{
            done = false;
            GUI.launchMessage(2, "Error de base de datos", "No se ha podido crear la base de datos dado que el sistema se encuentra desconectado u otro error ha ocurrido.");
        }

        return done;
    }

    public boolean deleteDB(String db){
        PreparedStatement deleteUser;
        boolean done = false;
        
        if(!db.equals("")){
            try{
                deleteUser = bridge.conn.prepareStatement(DELETE_ADMIN);
                deleteUser.setString(1, db);
                bridge.querier.execute(FLUSH);

                bridge.querier.execute(DELETE_DB + db);
                deleteUser.execute();
                
                System.out.println("Selected DB deleted.");
                done = true;
            }
            catch(SQLException sqle){
                sqle.printStackTrace();
                GUI.launchMessage(2, "Error de base de datos", "Ha ocurrido un error durante el borrado de la base de datos.");
                bridge.checkConnection();
            }
        }
        else{
            done = false;
            GUI.launchMessage(2, "Error interno", "La base de datos a eliminar indicada no és válida.");
        }

        return done;
    }

    public boolean editDB(String oldDB, String newDB){
        PreparedStatement editUser;
        boolean done = false;

        if(bridge.querier != null){
            try{
                bridge.querier.execute(ADD_DB + newDB);
                
                ResultSet tables = getTables(oldDB);
                while(tables.next()){
                    String table = tables.getString(1);
                    Statement editer = bridge.conn.createStatement();
                    editer.executeUpdate("RENAME TABLE " + oldDB + "." + table + " TO " + newDB + "." + table);
                }

                editUser = bridge.conn.prepareStatement(EDIT_ADMIN);
                editUser.setString(1, oldDB);
                editUser.setString(2, newDB);
                editUser.execute();

                deleteDB(oldDB);

                tables.close();
                System.out.println("Selected DB edited.");
                done = true;
            }
            catch(SQLException sqle){
                done = false;
                sqle.printStackTrace();
                GUI.launchMessage(2, "Error de base de datos", "Un error ha ocurrido al editar la base de datos de la corporación.\nEs posible que ya exista una corporación con el mismo nombre.");
                bridge.checkConnection();
            }
        }
        else{
            done = false;
            GUI.launchMessage(2, "Error de base de datos", "No se han podido editar la base de datos dado que el sistema se encuentra desconectado u otro error ha ocurrido.");
        }

        return done;
    }
}