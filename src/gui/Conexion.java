package gui;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;



/**
 *
 * @author Kevinssg12
 */
public class Conexion {
    
    Connection connect = null;
    
    public Connection conectar(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://127.0.0.1/gestion_inmuebles","root","");
            //JOptionPane.showMessageDialog(null, "Ok Conectado");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return connect;
    }
    
}
