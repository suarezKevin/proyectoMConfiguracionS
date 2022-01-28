package gui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Dalembert
 */

public class GestionPropietario extends javax.swing.JInternalFrame {

    DefaultTableModel modelo;
    Inmueble inmueble;
    String cedulaUsuario;
    String tipoInmueble;
    
    /**
     * Creates new form InterfazPropietario
     */
    //metodo
    public GestionPropietario(String cedulaU, String tipoG) {
        initComponents();
        ((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        this.inmueble = new Inmueble();
        this.cedulaUsuario = cedulaU;
        this.tipoInmueble = tipoG;
        camposDeshabilitados();
        jBtnGuardar.setEnabled(false);
        jBtnActualizar.setEnabled(false);
        inicializarDatos();
        jLblGestionELD.setText("GESTION DE " + this.tipoInmueble);
    }
    //metodo de iniciar
    public void inicializarDatos() {
        String[] titulos = {"Codigo", "Nombre", "Direccion", "Costo"};
        jTblEdificios.setModel(new DefaultTableModel(null, titulos) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        cargarTablaEdificio();
    }

    public void modificarDatos() {
        if (jTblEdificios.getSelectedRow() != -1) {
            int fila = jTblEdificios.getSelectedRow();
            jTxtId.setText(jTblEdificios.getValueAt(fila, 0).toString().trim());
            jTxtNombre.setText(jTblEdificios.getValueAt(fila, 1).toString().trim());
            jTxtDireccion.setText(jTblEdificios.getValueAt(fila, 2).toString().trim());
            jTxtCosto.setText(jTblEdificios.getValueAt(fila, 3).toString().trim());
            camposHabilitados();
            jTxtId.setEditable(false);
            jBtnActualizar.setEnabled(true);
            jBtnNuevo.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento de la tabla");
        }
    }

    public void camposDeshabilitados() {
        jTxtId.setEnabled(false);
        jTxtNombre.setEnabled(false);
        jTxtDireccion.setEnabled(false);
        jTxtCosto.setEnabled(false);
    }

    public void camposHabilitados() {
        jTxtId.setEnabled(true);
        jTxtNombre.setEnabled(true);
        jTxtDireccion.setEnabled(true);
        jTxtCosto.setEnabled(true);
    }

    public void guardarBaseDatos() {
        try {
            if (jTxtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Campos en blanco");
            } else if (jTxtNombre.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Campos en blanco");
            } else if (jTxtDireccion.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Campos en blanco");
            } else if (jTxtCosto.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Campos en blanco");
            } else {
                this.inmueble.setId(jTxtId.getText());
                this.inmueble.setNombre(jTxtNombre.getText());
                this.inmueble.setDireccion(jTxtDireccion.getText());
                this.inmueble.setCosto(Double.valueOf(jTxtCosto.getText()));
                Conexion cnx = new Conexion();
                Connection cn = cnx.conectar();
                String sql = "";
                sql = "insert into inmuebles (id_inm, nom_inm, dir_inm, cos_inm, estado_inm, tipo_inm, ced_pro_per ) values(?,?,?,?,?,?,?)";
                PreparedStatement psd = cn.prepareStatement(sql);
                psd.setString(1, this.inmueble.getId());
                psd.setString(2, this.inmueble.getNombre());
                psd.setString(3, this.inmueble.getDireccion());
                psd.setString(4, this.inmueble.getCosto().toString());
                psd.setString(5, "DISPONIBLE");
                psd.setString(6, this.tipoInmueble);
                psd.setString(7, this.cedulaUsuario);
                int n = psd.executeUpdate();
                if (n > 0) {
                    JOptionPane.showMessageDialog(null, "Se guardo con exito");
                    inicializarDatos();
                    limpiarTextos();
                    camposDeshabilitados();
                    jBtnGuardar.setEnabled(false);
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void limpiarTextos() {
        jTxtId.setText("");
        jTxtNombre.setText("");
        jTxtDireccion.setText("");
        jTxtCosto.setText("");
    }

    public void modificarPorBusqueda() {
        try {
            boolean bandera = false;
            if (!jTxtCodigo.getText().isEmpty()) {
                Conexion cc = new Conexion();
                Connection cn = cc.conectar();
                String sql = "";
                sql = "select id_inm from inmuebles where id_inm='" + jTxtCodigo.getText() + "' and tipo_inm='" + this.tipoInmueble + "'";
                Statement stt = cn.createStatement();
                ResultSet rst = stt.executeQuery(sql);
                while (rst.next()) {
                    if (jTxtCodigo.getText().equals(rst.getString("id_inm"))) {
                        JOptionPane.showMessageDialog(null, "Elemento encontrado");
                        bandera = true;
                    }
                }
                if (bandera) {
                    String sql2 = "";
                    System.out.println(jTxtCodigo.getText());
                    sql2 = "select id_inm, nom_inm, dir_inm, cos_inm from inmuebles where id_inm='" + jTxtCodigo.getText() + "' and tipo_inm='" + this.tipoInmueble + "'";
                    Statement psd = cn.createStatement();
                    ResultSet rs = psd.executeQuery(sql2);
                    while (rs.next()) {
                        camposHabilitados();
                        jTxtId.setText(rs.getString("id_inm"));
                        jTxtNombre.setText(rs.getString("nom_inm"));
                        jTxtDireccion.setText(rs.getString("dir_inm"));
                        jTxtCosto.setText(rs.getString("cos_inm"));
                        jTxtCodigo.setText("");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontro el elemento");
                }

            } else {
                JOptionPane.showMessageDialog(null, "No hay valor por buscar");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void cargarTablaEdificio() {
        try {
            modelo = new DefaultTableModel();
            modelo = (DefaultTableModel) jTblEdificios.getModel();
            String[] registros = new String[4];
            Conexion cc = new Conexion();
            Connection cn = cc.conectar();
            String sql = "";
            sql = "select id_inm, nom_inm, dir_inm, cos_inm from inmuebles where tipo_inm='" + this.tipoInmueble + "' and (estado_inm='DISPONIBLE' or estado_inm='ALQUILADO') and ced_pro_per=" + this.cedulaUsuario;
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                registros[0] = rs.getString("id_inm");
                registros[1] = rs.getString("nom_inm");
                registros[2] = rs.getString("dir_inm");
                registros[3] = rs.getString("cos_inm");
                modelo.addRow(registros);
            }
            jTblEdificios.setModel(modelo);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void actualizarDatos() {
        if (jTxtNombre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el Nombre");
            jTxtNombre.requestFocus();
        } else if (jTxtDireccion.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar la Direccion");
            jTxtDireccion.requestFocus();
        } else if (jTxtCosto.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el Costo");
            jTxtCosto.requestFocus();
        } else {
            try {
                Conexion cc = new Conexion();
                Connection cn = cc.conectar();
                String sql = "";
                sql = "update inmuebles set nom_inm='" + jTxtNombre.getText() + "'where id_inm='" + jTxtId.getText() + "'";
                PreparedStatement psd = cn.prepareStatement(sql);
                psd.executeUpdate();
                sql = "update inmuebles set dir_inm='" + jTxtDireccion.getText() + "'where id_inm='" + jTxtId.getText() + "'";
                psd = cn.prepareStatement(sql);
                psd.executeUpdate();
                sql = "update inmuebles set cos_inm='" + jTxtCosto.getText() + "'where id_inm='" + jTxtId.getText() + "'";
                psd = cn.prepareStatement(sql);
                psd.executeUpdate();
                limpiarTextos();
                camposDeshabilitados();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    public void darBaja() {
        Conexion cc = new Conexion();
        Connection cn = cc.conectar();
        String sql = "";
        if (jTblEdificios.getSelectedRow() != -1) {
            try {
                int res = JOptionPane.showConfirmDialog(null, "¿Esta seguro de realizar esta accion?", "Seleccione una opcion para continuar", JOptionPane.YES_NO_OPTION);
                if (res == 0) {
                    int fila = jTblEdificios.getSelectedRow();
                    String id_inmueble = jTblEdificios.getValueAt(fila, 0).toString();
                    sql = "update inmuebles set estado_inm='NO' where id_inm='" + id_inmueble + "'";
                    PreparedStatement psd = cn.prepareStatement(sql);
                    psd.executeUpdate();
                    inicializarDatos();
                } else {
                    inicializarDatos();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }

        } else if (!jTxtId.getText().isEmpty()) {
            try {
                int res = JOptionPane.showConfirmDialog(null, "¿Esta seguro de realizar esta accion?", "Seleccione una opcion para continuar", JOptionPane.YES_NO_OPTION);
                if (res == 0) {
                    sql = "update inmuebles set estado_inm='NO' where id_inm='" + jTxtId.getText() + "'";
                    PreparedStatement psd = cn.prepareStatement(sql);
                    psd.executeUpdate();
                    inicializarDatos();
                    limpiarTextos();
                } else {
                    inicializarDatos();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GestionPropietario.class.getName()).log(Level.SEVERE, null, ex);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTxtId = new javax.swing.JTextField();
        jTxtNombre = new javax.swing.JTextField();
        jTxtDireccion = new javax.swing.JTextField();
        jTxtCosto = new javax.swing.JTextField();
        jBtnGuardar = new javax.swing.JButton();
        jBtnNuevo = new javax.swing.JButton();
        jLblGestionELD = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTblEdificios = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jTxtCodigo = new javax.swing.JTextField();
        jBtnBuscar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jBtnDarBaja = new javax.swing.JButton();
        jBtnModificar = new javax.swing.JButton();
        jBtnCerrar = new javax.swing.JButton();
        jBtnActualizar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jBtnCancelar = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setBackground(new java.awt.Color(19, 56, 190));
        setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel2.setText("ID:");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel3.setText("Nombre:");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel4.setText("Direccion:");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setText("Costo:");

        jTxtId.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N

        jTxtNombre.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N

        jTxtDireccion.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N

        jTxtCosto.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N

        jBtnGuardar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jBtnGuardar.setForeground(new java.awt.Color(19, 56, 190));
        jBtnGuardar.setText("Guardar");
        jBtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnGuardarActionPerformed(evt);
            }
        });

        jBtnNuevo.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jBtnNuevo.setForeground(new java.awt.Color(19, 56, 190));
        jBtnNuevo.setText("Nuevo");
        jBtnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnNuevoActionPerformed(evt);
            }
        });

        jLblGestionELD.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLblGestionELD.setForeground(new java.awt.Color(19, 56, 190));
        jLblGestionELD.setText("Gestion Inmuebles");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(jBtnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jBtnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLblGestionELD, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(12, 12, 12)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTxtId, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                                    .addComponent(jTxtNombre)
                                    .addComponent(jTxtDireccion)
                                    .addComponent(jTxtCosto))))))
                .addGap(0, 23, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLblGestionELD)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTxtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jTxtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTxtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTxtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnNuevo)
                    .addComponent(jBtnGuardar))
                .addGap(23, 23, 23))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jTblEdificios.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jTblEdificios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTblEdificios);

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setText("Id a buscar:");

        jTxtCodigo.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N

        jBtnBuscar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jBtnBuscar.setForeground(new java.awt.Color(19, 56, 190));
        jBtnBuscar.setText("Buscar");
        jBtnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(5, 5, 5))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(jLabel7)
                .addGap(8, 8, 8)
                .addComponent(jTxtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jBtnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnBuscar)
                    .addComponent(jTxtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jBtnDarBaja.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jBtnDarBaja.setForeground(new java.awt.Color(19, 56, 190));
        jBtnDarBaja.setText("Dar de Baja");
        jBtnDarBaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnDarBajaActionPerformed(evt);
            }
        });

        jBtnModificar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jBtnModificar.setForeground(new java.awt.Color(19, 56, 190));
        jBtnModificar.setText("Modificar");
        jBtnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnModificarActionPerformed(evt);
            }
        });

        jBtnCerrar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jBtnCerrar.setForeground(new java.awt.Color(19, 56, 190));
        jBtnCerrar.setText("Cerrar");
        jBtnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCerrarActionPerformed(evt);
            }
        });

        jBtnActualizar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jBtnActualizar.setForeground(new java.awt.Color(19, 56, 190));
        jBtnActualizar.setText("Actualizar");
        jBtnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnActualizarActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Editar");

        jBtnCancelar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jBtnCancelar.setForeground(new java.awt.Color(19, 56, 190));
        jBtnCancelar.setText("Cancelar");
        jBtnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBtnDarBaja, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                    .addComponent(jBtnModificar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBtnCerrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBtnActualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addComponent(jBtnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jBtnCerrar)
                .addGap(27, 27, 27)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBtnDarBaja)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jBtnModificar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jBtnActualizar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jBtnCancelar)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBtnDarBajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnDarBajaActionPerformed
        jTblEdificios.setEnabled(true);
        darBaja();
    }//GEN-LAST:event_jBtnDarBajaActionPerformed

    private void jBtnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnModificarActionPerformed
        jTblEdificios.setEnabled(true);
        modificarDatos();

        jBtnGuardar.setEnabled(false);

    }//GEN-LAST:event_jBtnModificarActionPerformed

    private void jBtnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCerrarActionPerformed
        this.dispose();
    }//GEN-LAST:event_jBtnCerrarActionPerformed

    private void jBtnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnActualizarActionPerformed
        actualizarDatos();
        jBtnActualizar.setEnabled(false);
        jBtnNuevo.setEnabled(true);
        inicializarDatos();
        jBtnModificar.setEnabled(true);
    }//GEN-LAST:event_jBtnActualizarActionPerformed

    private void jBtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevoActionPerformed
        jTxtId.setEditable(true);
        camposHabilitados();
        limpiarTextos();
        jBtnGuardar.setEnabled(true);
        jBtnActualizar.setEnabled(false);
        jBtnModificar.setEnabled(false);
    }//GEN-LAST:event_jBtnNuevoActionPerformed

    private void jBtnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGuardarActionPerformed
        guardarBaseDatos();
        jBtnModificar.setEnabled(true);
    }//GEN-LAST:event_jBtnGuardarActionPerformed

    private void jBtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelarActionPerformed
        camposDeshabilitados();
        limpiarTextos();
        jBtnActualizar.setEnabled(false);
        jBtnGuardar.setEnabled(false);
        jBtnNuevo.setEnabled(true);
        jBtnModificar.setEnabled(true);
    }//GEN-LAST:event_jBtnCancelarActionPerformed

    private void jBtnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnBuscarActionPerformed
        modificarPorBusqueda();
        jBtnModificar.setEnabled(false);
        jBtnActualizar.setEnabled(true);
        jBtnGuardar.setEnabled(false);
    }//GEN-LAST:event_jBtnBuscarActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnActualizar;
    private javax.swing.JButton jBtnBuscar;
    private javax.swing.JButton jBtnCancelar;
    private javax.swing.JButton jBtnCerrar;
    private javax.swing.JButton jBtnDarBaja;
    private javax.swing.JButton jBtnGuardar;
    private javax.swing.JButton jBtnModificar;
    private javax.swing.JButton jBtnNuevo;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLblGestionELD;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTblEdificios;
    private javax.swing.JTextField jTxtCodigo;
    private javax.swing.JTextField jTxtCosto;
    private javax.swing.JTextField jTxtDireccion;
    private javax.swing.JTextField jTxtId;
    private javax.swing.JTextField jTxtNombre;
    // End of variables declaration//GEN-END:variables
}
