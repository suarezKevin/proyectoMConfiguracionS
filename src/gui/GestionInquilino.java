/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.Conexion;
import Login.SQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Kevinssg12
 */
public class GestionInquilino extends javax.swing.JInternalFrame {

    DefaultTableModel alquilados;
    DefaultTableModel modelo;
    String cedulaUsuario;
    String tipo;

    /**
     * Creates new form GestionInquilino
     */
    public GestionInquilino(String cedula) {
        initComponents();
        ((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        this.cedulaUsuario = cedula;
        String[] opciones = {"Selecciones uno...", "TODOS", "EDIFICIO", "LOCAL", "DEPARTAMENTO"};
        DefaultComboBoxModel mod = new DefaultComboBoxModel(opciones);
        jCmbxTipoInmueble.setModel(mod);
        //cargarTabla();
        agregarTablaAquilados();
    }

    private GestionInquilino() {

    }

    public void inicializarDatos() {
        String[] titulos = {"Codigo", "Nombre", "Direccion", "Costo"};
        jTblInmueblesDisponibles.setModel(new DefaultTableModel(null, titulos) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        //cargarTablaEdificio();
    }

    public void cargarTabla() {
        Conexion cc = new Conexion();
        Connection cn = cc.conectar();
        tipo = jCmbxTipoInmueble.getItemAt(jCmbxTipoInmueble.getSelectedIndex());
        if (tipo.equals("TODOS")) {
            try {
                String[] titulos = {"ID", "NOMBRE", "DIRECCION", "COSTO"};
                modelo = new DefaultTableModel(null, titulos);
                String[] registros = new String[4];
                String sql = "";
                sql = "SELECT id_inm,nom_inm,dir_inm,cos_inm FROM inmuebles WHERE estado_inm='DISPONIBLE'";
                Statement psd = cn.createStatement();
                ResultSet rs = psd.executeQuery(sql);
                while (rs.next()) {
                    registros[0] = rs.getString("id_inm");
                    registros[1] = rs.getString("nom_inm");
                    registros[2] = rs.getString("dir_inm");
                    registros[3] = String.valueOf(rs.getDouble("cos_inm"));
                    modelo.addRow(registros);
                    jTblInmueblesDisponibles.setModel(modelo);
                }
            } catch (SQLException ex) {
                Logger.getLogger(GestionInquilino.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            cargarTablaPorTipo();
        }
    }

    public void cargarTablaPorTipo() {
        try {
            Conexion cc = new Conexion();
            Connection cn = cc.conectar();
            String[] titulos = {"ID", "NOMBRE", "DIRECCION", "COSTO"};
            modelo = new DefaultTableModel(null, titulos);
            String[] registros = new String[4];
            String sql = "";
            sql = "SELECT id_inm,nom_inm,dir_inm,cos_inm FROM inmuebles WHERE tipo_inm='" + tipo + "' AND estado_inm='DISPONIBLE'";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                registros[0] = rs.getString("id_inm");
                registros[1] = rs.getString("nom_inm");
                registros[2] = rs.getString("dir_inm");
                registros[3] = String.valueOf(rs.getDouble("cos_inm"));
                modelo.addRow(registros);
            }
            jTblInmueblesDisponibles.setModel(modelo);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public String fechaActual() {
        Date fecha = new Date();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/YYYY");
        return formato.format(fecha);
    }

    public void alquilarInmueble() {
        Conexion cc = new Conexion();
        Connection cn = cc.conectar();
        String sql = "";
        if (jTblInmueblesDisponibles.getSelectedRow() != -1) {
            try {
                int res = JOptionPane.showConfirmDialog(null, "¿Esta seguro de realizar esta accion?", "Seleccione una opcion para continuar", JOptionPane.YES_NO_OPTION);
                if (res == 0) {
                    int fila = jTblInmueblesDisponibles.getSelectedRow();
                    String id_inmueble = jTblInmueblesDisponibles.getValueAt(fila, 0).toString();
                    String nom_inm = jTblInmueblesDisponibles.getValueAt(fila, 1).toString();
                    String dir_inm = jTblInmueblesDisponibles.getValueAt(fila, 2).toString();
                    String cos_inmueble = jTblInmueblesDisponibles.getValueAt(fila, 3).toString();
                    sql = "update inmuebles set estado_inm='ALQUILADO' where id_inm='" + id_inmueble + "'";
                    PreparedStatement psd = cn.prepareStatement(sql);
                    psd.executeUpdate();
                    String sqlAl = "";
                    sqlAl = "insert into alquilar(id_alq,fec_ini_alq,ced_inquilino,id_inm_alq,cos_inm_alq,fec_fin_alq,estado_alq)values(?,?,?,?,?,?,?)";
                    PreparedStatement pst = cn.prepareStatement(sqlAl);
                    pst.setInt(1, SQL.id_incrementableAlquilar());
                    pst.setString(2, fechaActual());
                    pst.setString(3, this.cedulaUsuario);
                    pst.setString(4, id_inmueble);
                    pst.setString(5, cos_inmueble);
                    pst.setString(6, "NULL");
                    pst.setString(7, "YES");
                    int n = pst.executeUpdate();
                    if (n > 0) {
                        cargarTablaPorTipo();
                        JOptionPane.showMessageDialog(null, "Inmueble Alquilado con exito ! Felicidades");
                        agregarTablaAquilados();
                    }
                } else {
                    //inicializarDatos();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento de la tabla");
        }
    }

    public void agregarTablaAquilados() {
        try {
            String[] titulos = {"ID", "NOMBRE", "DIRECCION", "COSTO", "FECHA/ALQUILER"};
            String[] nuevoInmueble = new String[5];
            alquilados = new DefaultTableModel(null, titulos);
            Conexion cc = new Conexion();
            Connection cn = cc.conectar();
            String sql = "";
            sql = "select i.id_inm,i.nom_inm,i.dir_inm,i.cos_inm, a.fec_ini_alq from inmuebles i, alquilar a, inquilinos inq where i.id_inm=a.id_inm_alq and a.ced_inquilino='" + this.cedulaUsuario + "' and a.estado_alq='YES' GROUP BY a.id_inm_alq";
            Statement stt = cn.createStatement();
            ResultSet rs = stt.executeQuery(sql);
            while (rs.next()) {
                nuevoInmueble[0] = rs.getString("i.id_inm");
                nuevoInmueble[1] = rs.getString("i.nom_inm");
                nuevoInmueble[2] = rs.getString("i.dir_inm");
                nuevoInmueble[3] = rs.getString("i.cos_inm");
                nuevoInmueble[4] = rs.getString("a.fec_ini_alq");
                alquilados.addRow(nuevoInmueble);
            }
            jTblInmueblesAlquilados.setModel(alquilados);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    public void desalquilarInmueble(){
        Conexion cc = new Conexion();
        Connection cn = cc.conectar();
        String sql = "";
        if (jTblInmueblesAlquilados.getSelectedRow() != -1) {
            try {
                int res = JOptionPane.showConfirmDialog(null, "¿Esta seguro de realizar esta accion?", "Seleccione una opcion para continuar", JOptionPane.YES_NO_OPTION);
                if (res == 0) {
                    int fila = jTblInmueblesAlquilados.getSelectedRow();
                    String id_inmueble = jTblInmueblesAlquilados.getValueAt(fila, 0).toString();
                    sql = "update inmuebles set estado_inm='DISPONIBLE' where id_inm='" + id_inmueble + "'";
                    PreparedStatement psd = cn.prepareStatement(sql);
                    psd.executeUpdate();
                    String sqlDesalq = "";
                    sqlDesalq = "update alquilar set fec_fin_alq='" + fechaActual() + "' where id_inm_alq='" + id_inmueble + "'";
                    PreparedStatement pst = cn.prepareStatement(sqlDesalq);
                    pst.executeUpdate();
                    sqlDesalq = "update alquilar set estado_alq='NO' where id_inm_alq='" + id_inmueble + "'";
                    pst = cn.prepareStatement(sqlDesalq);
                    pst.executeUpdate();
                    int n = pst.executeUpdate();
                    if (n > 0) {
                        cargarTablaPorTipo();
                        JOptionPane.showMessageDialog(null, "Inmueble Desalquilado con exito !");
                        agregarTablaAquilados();
                    }
                } else {
                    //inicializarDatos();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento de la tabla");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jBtnDesalquilar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTblInmueblesAlquilados = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTblInmueblesDisponibles = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jCmbxTipoInmueble = new javax.swing.JComboBox<>();
        jBtnAlquilar = new javax.swing.JButton();

        setBackground(new java.awt.Color(19, 56, 190));
        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jBtnDesalquilar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jBtnDesalquilar.setForeground(new java.awt.Color(19, 56, 190));
        jBtnDesalquilar.setText("Desalquilar");
        jBtnDesalquilar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnDesalquilarActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(19, 56, 190));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Inmuebles Alquilados");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 321, Short.MAX_VALUE)
                        .addComponent(jBtnDesalquilar))
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(13, 13, 13)
                .addComponent(jBtnDesalquilar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setPreferredSize(new java.awt.Dimension(472, 143));

        jTblInmueblesAlquilados.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTblInmueblesAlquilados.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jTblInmueblesAlquilados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTblInmueblesAlquilados);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(8, 8, 8))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jTblInmueblesDisponibles.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTblInmueblesDisponibles.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jTblInmueblesDisponibles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTblInmueblesDisponibles);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(8, 8, 8))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(19, 56, 190));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Inmuebles Disponibles");

        jCmbxTipoInmueble.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jCmbxTipoInmueble.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jCmbxTipoInmueble.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCmbxTipoInmuebleActionPerformed(evt);
            }
        });

        jBtnAlquilar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jBtnAlquilar.setForeground(new java.awt.Color(19, 56, 190));
        jBtnAlquilar.setText("Alquilar");
        jBtnAlquilar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnAlquilarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jCmbxTipoInmueble, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(176, 176, 176)
                        .addComponent(jBtnAlquilar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCmbxTipoInmueble, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnAlquilar))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBtnAlquilarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnAlquilarActionPerformed
        alquilarInmueble();
    }//GEN-LAST:event_jBtnAlquilarActionPerformed

    private void jCmbxTipoInmuebleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCmbxTipoInmuebleActionPerformed
        cargarTabla();
    }//GEN-LAST:event_jCmbxTipoInmuebleActionPerformed

    private void jBtnDesalquilarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnDesalquilarActionPerformed
        desalquilarInmueble();
    }//GEN-LAST:event_jBtnDesalquilarActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnAlquilar;
    private javax.swing.JButton jBtnDesalquilar;
    private javax.swing.JComboBox<String> jCmbxTipoInmueble;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTblInmueblesAlquilados;
    private javax.swing.JTable jTblInmueblesDisponibles;
    // End of variables declaration//GEN-END:variables
}
