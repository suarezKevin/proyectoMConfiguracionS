/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Login;

import gui.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Dalembert
 */
public class SQL {
    //metodo para incrementar en la BD

    public static int id_inrementable() {
        int id = 1;
        try {
            PreparedStatement ps = null;
            ResultSet rs = null;
            Conexion cn = new Conexion();
            Connection cnx = cn.conectar();
            ps = cnx.prepareStatement("select max(id_con) from contrato_pro_sec");
            rs = ps.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1) + 1;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        } 
        return id;
    }
    
    public static int id_incrementableAlquilar() {
        int id = 1;
        try {
            PreparedStatement ps = null;
            ResultSet rs = null;
            Conexion cn = new Conexion();
            Connection cnx = cn.conectar();
            ps = cnx.prepareStatement("select max(id_alq) from alquilar");
            rs = ps.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1) + 1;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        } 
        return id;
    }

}
