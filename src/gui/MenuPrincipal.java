/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.Conexion;
import Login.Login;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Kevinssg12
 */
public class MenuPrincipal extends javax.swing.JFrame {

    String cedulaU;
    String cargoU;
    String nombreTabla;

    /**
     * Creates new form GestionPropietario
     */
    public MenuPrincipal(String cedulaU, String cargo) {
        initComponents();
        this.cedulaU = cedulaU;
        this.cargoU = cargo;
        identificarCargo();
        deshabilitarComponentes();
    }

    public void interfazInquilino() {
        jBtnGdepartamento.setVisible(false);
        jBtnGlocal.setVisible(false);
        jBtnGedificio.setVisible(false);
        GestionInquilino gi = new GestionInquilino(this.cedulaU);
        jDsktpVentana.add(gi);
        gi.setVisible(true);
    }

    

    public void dimensionGpropietario(GestionPropietario gp) {
        int ancho = jDsktpVentana.getWidth();
        int alto = jDsktpVentana.getHeight();
        gp.setSize(ancho, alto);
    }

    public void identificarCargo() {
        try {
            Conexion cnx = new Conexion();
            Connection cnn = cnx.conectar();
            String sql = "";
            sql = "select cargo_usu from usuarios where ced_usu_per=" + this.cedulaU;
            Statement stt = cnn.createStatement();
            ResultSet rs = stt.executeQuery(sql);
            while (rs.next()) {
                if (rs.getString("cargo_usu").equals("Propietario")) {
                    this.cargoU = rs.getString("cargo_usu");
                } else if (rs.getString("cargo_usu").equals("Inquilino")) {
                    this.cargoU = rs.getString("cargo_usu");
                    interfazInquilino();
                } else {
                    this.cargoU = rs.getString("cargo_usu");
                    
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void deshabilitarComponentes() {
        jTxtCedula.setVisible(false);
        jTxtNombre.setVisible(false);
        jTxtApellido.setVisible(false);
        jTxtDireccion.setVisible(false);
        jTxtTelefono.setVisible(false);
        jLbCedula.setVisible(false);
        jLbNombre.setVisible(false);
        jLbApellido.setVisible(false);
        jLbDireccion.setVisible(false);
        jLbTelefono.setVisible(false);
        jBtnCancelarMod.setVisible(false);
        jBtnGuardarMod.setVisible(false);
        jSeparaModificar.setVisible(false);
        jLbModificarTitulo.setVisible(false);
    }

    public void habilitarComponentes() {
        jTxtCedula.setVisible(true);
        jTxtNombre.setVisible(true);
        jTxtApellido.setVisible(true);
        jTxtDireccion.setVisible(true);
        jTxtTelefono.setVisible(true);
        jLbCedula.setVisible(true);
        jLbNombre.setVisible(true);
        jLbApellido.setVisible(true);
        jLbDireccion.setVisible(true);
        jLbTelefono.setVisible(true);
        jBtnCancelarMod.setVisible(true);
        jBtnGuardarMod.setVisible(true);
        jSeparaModificar.setVisible(true);
        jLbModificarTitulo.setVisible(true);
    }

    public void llenarTxtDatosPersonales() {
        switch (this.cargoU) {
            case "Propietario":
                buscarPropietario();
                break;
            case "Inquilino":
                buscarInquilino();
                break;
            case "Secretaria":
                buscarSecretaria();
                break;
            default:
        }
    }

    public void buscarPropietario() {
        try {
            Conexion cc = new Conexion();
            Connection cn = cc.conectar();
            String sql = "";
            sql = "select * from propietarios where ced_pro='" + this.cedulaU + "'";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                jTxtCedula.setText(rs.getString("ced_pro"));
                jTxtNombre.setText(rs.getString("nom_pro"));
                jTxtApellido.setText(rs.getString("ape_pro"));
                jTxtDireccion.setText(rs.getString("dir_pro"));
                jTxtTelefono.setText(rs.getString("tel_pro"));
            }
            jTxtCedula.setEditable(false);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void buscarInquilino() {
        try {
            Conexion cc = new Conexion();
            Connection cn = cc.conectar();
            String sql = "";
            sql = "select * from inquilinos where ced_inq='" + this.cedulaU + "'";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                jTxtCedula.setText(rs.getString("ced_inq"));
                jTxtNombre.setText(rs.getString("nom_inq"));
                jTxtApellido.setText(rs.getString("ape_inq"));
                jTxtDireccion.setText(rs.getString("dir_inq"));
                jTxtTelefono.setText(rs.getString("tel_inq"));
            }
            jTxtCedula.setEditable(false);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void buscarSecretaria() {
        try {
            Conexion cc = new Conexion();
            Connection cn = cc.conectar();
            String sql = "";
            sql = "select * from secretarias where cedula_sec='" + this.cedulaU + "'";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                jTxtCedula.setText(rs.getString("cedula_sec"));
                jTxtNombre.setText(rs.getString("nombre_sec"));
                jTxtApellido.setText(rs.getString("apellido_sec"));
                jTxtDireccion.setText(rs.getString("direccion_sec"));
                jTxtTelefono.setText(rs.getString("telefono_sec"));
            }
            jTxtCedula.setEditable(false);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void editarDatosPersonales() {
        habilitarComponentes();
        llenarTxtDatosPersonales();
    }

    public void actualizarDatosPersonales() {
        switch (this.cargoU) {
            case "Propietario":
                actualizarDatosPersonalesPro();
                break;
            case "Inquilino":
                actualizarDatosPersonalesInq();
                break;
            case "Secretaria":
                actualizarDatosPersonalesSec();
                break;
            default:
        }
    }

    public void actualizarDatosPersonalesPro() {
        if (jTxtNombre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el Nombre");
            jTxtNombre.requestFocus();
        } else if (jTxtApellido.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el Apellido");
            jTxtApellido.requestFocus();
        } else if (jTxtDireccion.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el Direccion");
            jTxtDireccion.requestFocus();
        } else if (jTxtTelefono.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el Telefono");
            jTxtTelefono.requestFocus();
        } else {
            try {
                Conexion cc = new Conexion();
                Connection cn = cc.conectar();
                String sql = "";
                sql = "update propietarios set nom_pro='" + jTxtNombre.getText() + "'where ced_pro='" + jTxtCedula.getText() + "'";
                PreparedStatement psd = cn.prepareStatement(sql);
                psd.executeUpdate();
                sql = "update propietarios set ape_pro='" + jTxtApellido.getText() + "'where ced_pro='" + jTxtCedula.getText() + "'";
                psd = cn.prepareStatement(sql);
                psd.executeUpdate();
                sql = "update propietarios set dir_pro='" + jTxtDireccion.getText() + "'where ced_pro='" + jTxtCedula.getText() + "'";
                psd = cn.prepareStatement(sql);
                psd.executeUpdate();
                sql = "update propietarios set tel_pro='" + jTxtTelefono.getText() + "'where ced_pro='" + jTxtCedula.getText() + "'";
                psd = cn.prepareStatement(sql);
                psd.executeUpdate();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    public void actualizarDatosPersonalesInq() {
        if (jTxtNombre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el Nombre");
            jTxtNombre.requestFocus();
        } else if (jTxtApellido.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el Apellido");
            jTxtApellido.requestFocus();
        } else if (jTxtDireccion.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el Direccion");
            jTxtDireccion.requestFocus();
        } else if (jTxtTelefono.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el Telefono");
            jTxtTelefono.requestFocus();
        } else {
            try {
                Conexion cc = new Conexion();
                Connection cn = cc.conectar();
                String sql = "";
                sql = "update inquilinos set nom_inq='" + jTxtNombre.getText() + "'where ced_inq='" + jTxtCedula.getText() + "'";
                PreparedStatement psd = cn.prepareStatement(sql);
                psd.executeUpdate();
                sql = "update inquilinos set ape_inq='" + jTxtApellido.getText() + "'where ced_inq='" + jTxtCedula.getText() + "'";
                psd = cn.prepareStatement(sql);
                psd.executeUpdate();
                sql = "update inquilinos set dir_inq='" + jTxtDireccion.getText() + "'where ced_inq='" + jTxtCedula.getText() + "'";
                psd = cn.prepareStatement(sql);
                psd.executeUpdate();
                sql = "update inquilinos set tel_inq='" + jTxtTelefono.getText() + "'where ced_inq='" + jTxtCedula.getText() + "'";
                psd = cn.prepareStatement(sql);
                psd.executeUpdate();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    public void actualizarDatosPersonalesSec() {
        if (jTxtNombre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el Nombre");
            jTxtNombre.requestFocus();
        } else if (jTxtApellido.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el Apellido");
            jTxtApellido.requestFocus();
        } else if (jTxtDireccion.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el Direccion");
            jTxtDireccion.requestFocus();
        } else if (jTxtTelefono.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el Telefono");
            jTxtTelefono.requestFocus();
        } else {
            try {
                Conexion cc = new Conexion();
                Connection cn = cc.conectar();
                String sql = "";
                sql = "update secretarias set nombre_sec='" + jTxtNombre.getText() + "'where cedula_sec='" + jTxtCedula.getText() + "'";
                PreparedStatement psd = cn.prepareStatement(sql);
                psd.executeUpdate();
                sql = "update secretarias set apellido_sec='" + jTxtApellido.getText() + "'where cedula_sec='" + jTxtCedula.getText() + "'";
                psd = cn.prepareStatement(sql);
                psd.executeUpdate();
                sql = "update secretarias set direccion_sec='" + jTxtDireccion.getText() + "'where cedula_sec='" + jTxtCedula.getText() + "'";
                psd = cn.prepareStatement(sql);
                psd.executeUpdate();
                sql = "update secretarias set telefono_sec='" + jTxtTelefono.getText() + "'where cedula_sec='" + jTxtCedula.getText() + "'";
                psd = cn.prepareStatement(sql);
                psd.executeUpdate();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    public void darseBaja() {
        try {
            int res = JOptionPane.showConfirmDialog(null, "¿Esta seguro de realizar esta accion?", "Seleccione una opcion para continuar", JOptionPane.YES_NO_OPTION);
            if (res == 0) {
                Conexion cc = new Conexion();
                Connection cn = cc.conectar();
                String sql = "";
                sql = "update usuarios set estado_usu='NO' where ced_usu_per='" + this.cedulaU + "'";
                PreparedStatement psd = cn.prepareStatement(sql);
                psd.executeUpdate();
                JOptionPane.showMessageDialog(null, "Usted sera dirigido a la ventana de Login, y no podra acceder a su cuenta !");
                this.dispose();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
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
        jBtnGdepartamento = new javax.swing.JButton();
        jBtnGlocal = new javax.swing.JButton();
        jBtnGedificio = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jDsktpVentana = new javax.swing.JDesktopPane();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jBtnCerrarSesion = new javax.swing.JButton();
        jBtnEditar = new javax.swing.JButton();
        jBtnDarseBaja = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jTxtCedula = new javax.swing.JTextField();
        jTxtNombre = new javax.swing.JTextField();
        jTxtApellido = new javax.swing.JTextField();
        jTxtDireccion = new javax.swing.JTextField();
        jTxtTelefono = new javax.swing.JTextField();
        jLbCedula = new javax.swing.JLabel();
        jLbNombre = new javax.swing.JLabel();
        jLbApellido = new javax.swing.JLabel();
        jLbDireccion = new javax.swing.JLabel();
        jLbTelefono = new javax.swing.JLabel();
        jBtnCancelarMod = new javax.swing.JButton();
        jBtnGuardarMod = new javax.swing.JButton();
        jLbModificarTitulo = new javax.swing.JLabel();
        jSeparaModificar = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jBtnGdepartamento.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jBtnGdepartamento.setText("Gestion Departamento");
        jBtnGdepartamento.setFocusable(false);
        jBtnGdepartamento.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnGdepartamento.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnGdepartamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnGdepartamentoActionPerformed(evt);
            }
        });

        jBtnGlocal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jBtnGlocal.setText("Gestion Local");
        jBtnGlocal.setFocusable(false);
        jBtnGlocal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnGlocal.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnGlocal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnGlocalActionPerformed(evt);
            }
        });

        jBtnGedificio.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jBtnGedificio.setText("Gestion Edificio");
        jBtnGedificio.setFocusable(false);
        jBtnGedificio.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnGedificio.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnGedificio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnGedificioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jBtnGedificio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBtnGlocal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBtnGdepartamento)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jBtnGlocal)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jBtnGedificio)
                        .addComponent(jBtnGdepartamento, javax.swing.GroupLayout.Alignment.TRAILING)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/principal.jpeg"))); // NOI18N

        jDsktpVentana.setLayer(jLabel3, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDsktpVentanaLayout = new javax.swing.GroupLayout(jDsktpVentana);
        jDsktpVentana.setLayout(jDsktpVentanaLayout);
        jDsktpVentanaLayout.setHorizontalGroup(
            jDsktpVentanaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDsktpVentanaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
                .addContainerGap())
        );
        jDsktpVentanaLayout.setVerticalGroup(
            jDsktpVentanaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDsktpVentanaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jDsktpVentana)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jDsktpVentana)
                .addContainerGap())
        );

        jBtnCerrarSesion.setText("Cerrar Sesion");
        jBtnCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCerrarSesionActionPerformed(evt);
            }
        });

        jBtnEditar.setText("Editar Perfil");
        jBtnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnEditarActionPerformed(evt);
            }
        });

        jBtnDarseBaja.setText("Darse de Baja");
        jBtnDarseBaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnDarseBajaActionPerformed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Datos Personales");

        jLbCedula.setText("Cedula:");

        jLbNombre.setText("Nombre:");

        jLbApellido.setText("Apellido:");

        jLbDireccion.setText("Direccion:");

        jLbTelefono.setText("Telefono:");

        jBtnCancelarMod.setText("Cancelar");
        jBtnCancelarMod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCancelarModActionPerformed(evt);
            }
        });

        jBtnGuardarMod.setText("Guardar");
        jBtnGuardarMod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnGuardarModActionPerformed(evt);
            }
        });

        jLbModificarTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLbModificarTitulo.setText("Modificar Datos Personales");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLbDireccion)
                            .addComponent(jLbCedula)
                            .addComponent(jLbNombre)
                            .addComponent(jLbApellido)
                            .addComponent(jLbTelefono))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTxtDireccion, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTxtApellido, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTxtNombre, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTxtCedula)
                            .addComponent(jTxtTelefono)))
                    .addComponent(jSeparaModificar)
                    .addComponent(jLbModificarTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jBtnCancelarMod, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jBtnGuardarMod, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE))
                    .addComponent(jBtnEditar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBtnDarseBaja, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBtnCerrarSesion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jBtnCerrarSesion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 120, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBtnEditar)
                .addGap(18, 18, 18)
                .addComponent(jBtnDarseBaja)
                .addGap(21, 21, 21)
                .addComponent(jLbModificarTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparaModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLbCedula)
                    .addComponent(jTxtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLbNombre)
                    .addComponent(jTxtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLbApellido)
                    .addComponent(jTxtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLbDireccion)
                    .addComponent(jTxtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLbTelefono)
                    .addComponent(jTxtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnCancelarMod)
                    .addComponent(jBtnGuardarMod))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jBtnGedificioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGedificioActionPerformed
        GestionPropietario ge = new GestionPropietario(this.cedulaU, "EDIFICIO");
        dimensionGpropietario(ge);
        jDsktpVentana.add(ge);
        ge.setVisible(true);

    }//GEN-LAST:event_jBtnGedificioActionPerformed

    private void jBtnGlocalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGlocalActionPerformed
        GestionPropietario ge = new GestionPropietario(this.cedulaU, "LOCAL");
        dimensionGpropietario(ge);
        jDsktpVentana.add(ge);
        ge.setVisible(true);


    }//GEN-LAST:event_jBtnGlocalActionPerformed

    private void jBtnGdepartamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGdepartamentoActionPerformed
        GestionPropietario ge = new GestionPropietario(this.cedulaU, "DEPARTAMENTO");
        dimensionGpropietario(ge);
        jDsktpVentana.add(ge);
        ge.setVisible(true);

    }//GEN-LAST:event_jBtnGdepartamentoActionPerformed

    private void jBtnCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCerrarSesionActionPerformed
        this.dispose();
        Login logeo = new Login();
        logeo.setVisible(true);
    }//GEN-LAST:event_jBtnCerrarSesionActionPerformed

    private void jBtnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnEditarActionPerformed
        editarDatosPersonales();
    }//GEN-LAST:event_jBtnEditarActionPerformed

    private void jBtnCancelarModActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelarModActionPerformed
        deshabilitarComponentes();
    }//GEN-LAST:event_jBtnCancelarModActionPerformed

    private void jBtnGuardarModActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGuardarModActionPerformed
        actualizarDatosPersonales();
        deshabilitarComponentes();
        JOptionPane.showMessageDialog(null, "Datos Actualizados");
    }//GEN-LAST:event_jBtnGuardarModActionPerformed

    private void jBtnDarseBajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnDarseBajaActionPerformed
        darseBaja();
    }//GEN-LAST:event_jBtnDarseBajaActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnCancelarMod;
    private javax.swing.JButton jBtnCerrarSesion;
    private javax.swing.JButton jBtnDarseBaja;
    private javax.swing.JButton jBtnEditar;
    private javax.swing.JButton jBtnGdepartamento;
    private javax.swing.JButton jBtnGedificio;
    private javax.swing.JButton jBtnGlocal;
    private javax.swing.JButton jBtnGuardarMod;
    private javax.swing.JDesktopPane jDsktpVentana;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLbApellido;
    private javax.swing.JLabel jLbCedula;
    private javax.swing.JLabel jLbDireccion;
    private javax.swing.JLabel jLbModificarTitulo;
    private javax.swing.JLabel jLbNombre;
    private javax.swing.JLabel jLbTelefono;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparaModificar;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTxtApellido;
    private javax.swing.JTextField jTxtCedula;
    private javax.swing.JTextField jTxtDireccion;
    private javax.swing.JTextField jTxtNombre;
    private javax.swing.JTextField jTxtTelefono;
    // End of variables declaration//GEN-END:variables
}
