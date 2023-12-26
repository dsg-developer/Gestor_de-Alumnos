/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package views;

import main.Start;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import javax.swing.text.MaskFormatter;
import org.netbeans.lib.awtextra.AbsoluteLayout;

/**
 * @author Work
 */

public class AddOrSelect extends JDialog {
    public static Class[] tipos;
    public static boolean[] permitirEdicion;
    public static String[] columnas;
    public boolean validacion  = true;
    
    public AddOrSelect(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setSize(436,416);
        this.setTitle("Agregar o Seleccionar");
        this.setIconImage(getIconImage());
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        closeWindow();
        JLAviso.setVisible(false);
        Start.setConteo(Start.getConteo()+1.0f);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        base = new javax.swing.JPanel();
        JLTitulo = new javax.swing.JLabel();
        JLAviso = new javax.swing.JLabel();
        scrollTable = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        JLAgregar = new javax.swing.JLabel();
        JLCurso = new javax.swing.JLabel();
        JTFCurso = new javax.swing.JTextField();
        JTFYear = new javax.swing.JTextField();
        JLAsignatura = new javax.swing.JLabel();
        JTFAsignatura = new javax.swing.JTextField();
        JBAdd = new javax.swing.JButton();
        JLFondo = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        jLabel3.setText("jLabel3");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        base.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        JLTitulo.setFont(new java.awt.Font("Palatino Linotype", 0, 18)); // NOI18N
        JLTitulo.setForeground(new java.awt.Color(242, 242, 242));
        JLTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLTitulo.setText("Seleccione su curso, asignatura, y año");
        base.add(JLTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 424, 30));

        JLAviso.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        JLAviso.setForeground(new java.awt.Color(230, 240, 240));
        JLAviso.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLAviso.setText("No se encontraron datos registrados.");
        base.add(JLAviso, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 424, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTable1.getTableHeader().setReorderingAllowed(false);
        scrollTable.setViewportView(jTable1);

        base.add(scrollTable, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 360, 190));

        JLAgregar.setFont(new java.awt.Font("Palatino Linotype", 0, 18)); // NOI18N
        JLAgregar.setForeground(new java.awt.Color(226, 226, 226));
        JLAgregar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLAgregar.setText("Agregar....");
        base.add(JLAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 255, 424, 30));

        JLCurso.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        JLCurso.setForeground(new java.awt.Color(175, 175, 175));
        JLCurso.setText("Escribir curso...");
        JLCurso.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        JLCurso.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JLCursoMouseClicked(evt);
            }
        });
        base.add(JLCurso, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 298, 210, 21));

        JTFCurso.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        JTFCurso.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                JTFCursoKeyReleased(evt);
            }
        });
        base.add(JTFCurso, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 295, 220, -1));

        JTFYear.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        JTFYear.setText("2022-2023");
        JTFYear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTFYearKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                JTFYearKeyReleased(evt);
            }
        });
        base.add(JTFYear, new org.netbeans.lib.awtextra.AbsoluteConstraints(275, 295, 114, -1));

        JLAsignatura.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        JLAsignatura.setForeground(new java.awt.Color(175, 175, 175));
        JLAsignatura.setText("Escribir Asignatura...");
        JLAsignatura.setToolTipText("");
        JLAsignatura.setAutoscrolls(true);
        JLAsignatura.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        JLAsignatura.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JLAsignaturaMouseClicked(evt);
            }
        });
        base.add(JLAsignatura, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 335, 210, 21));

        JTFAsignatura.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        JTFAsignatura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                JTFAsignaturaKeyReleased(evt);
            }
        });
        base.add(JTFAsignatura, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 332, 220, -1));

        JBAdd.setText("Agregar");
        JBAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        base.add(JBAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(317, 332, -1, -1));

        JLFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/views/imagenes/fondoPrincipal.jpg"))); // NOI18N
        base.add(JLFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 424, 380));

        getContentPane().add(base, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JTFYearKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JTFYearKeyPressed
        if(JTFYear.getText().length() == 9) JTFYear.setText(JTFYear.getText().substring(0, 8));
    }//GEN-LAST:event_JTFYearKeyPressed

    private void JTFYearKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JTFYearKeyReleased
        int n = 0;
        try {
            if(JTFYear.getText().length() <= 4){
                n = Short.parseShort(JTFYear.getText());
                JTFYear.setForeground(Color.BLACK);
                if(JTFYear.getText().length() == 4 && evt.getKeyCode() != 8 && evt.getKeyCode() != 127)
                    JTFYear.setText(JTFYear.getText()+"-");
                validacion = false;
            }else if(JTFYear.getText().length() <= 9){
                n = Integer.parseInt(JTFYear.getText().replace("-", "0"));
                JTFYear.setForeground(Color.BLACK);
                validacion = true;
            }
        } catch (NumberFormatException ex) {
            validacion = false;
            if(JTFYear.getText().isEmpty())
                JTFYear.setForeground(Color.BLACK);
            else{
                JTFYear.setForeground(Color.red);
            }
        }
    }//GEN-LAST:event_JTFYearKeyReleased

    private void JTFCursoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JTFCursoKeyReleased
        if(JTFCurso.getText().isEmpty())    JLCurso.setVisible(true);
        else    JLCurso.setVisible(false);
    }//GEN-LAST:event_JTFCursoKeyReleased

    private void JTFAsignaturaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JTFAsignaturaKeyReleased
        if(JTFAsignatura.getText().isEmpty())    JLAsignatura.setVisible(true);
        else    JLAsignatura.setVisible(false);
    }//GEN-LAST:event_JTFAsignaturaKeyReleased

    private void JLCursoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JLCursoMouseClicked
        JTFCurso.grabFocus();
    }//GEN-LAST:event_JLCursoMouseClicked

    private void JLAsignaturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JLAsignaturaMouseClicked
        JTFAsignatura.grabFocus();
    }//GEN-LAST:event_JLAsignaturaMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton JBAdd;
    public javax.swing.JLabel JLAgregar;
    public javax.swing.JLabel JLAsignatura;
    public javax.swing.JLabel JLAviso;
    public javax.swing.JLabel JLCurso;
    private javax.swing.JLabel JLFondo;
    public javax.swing.JLabel JLTitulo;
    public javax.swing.JTextField JTFAsignatura;
    public javax.swing.JTextField JTFCurso;
    public javax.swing.JTextField JTFYear;
    private javax.swing.JPanel base;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    public javax.swing.JTable jTable1;
    public javax.swing.JScrollPane scrollTable;
    // End of variables declaration//GEN-END:variables

    private void cargarInicio() {
        columnas = new String[]{"Curso", "Asignatura", "Año"};
        tipos = new Class[]{String.class, String.class, String.class};
        permitirEdicion = new boolean[]{false,false,false};
    }

    private void closeWindow() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we){
                JTFAsignatura.setText("");
                JLAsignatura.setVisible(true);
                JTFCurso.setText("");
                JLCurso.setVisible(true);
                if(Start.identificador == 0)    System.exit(0);
            }
        });
    }
    
    public void enabledComponents(boolean enabled){
        JTFYear.setEditable(enabled);
        JTFAsignatura.setEditable(enabled);
        JLAsignatura.setEnabled(enabled);
    }
    
    public Image getIconImage(){
        Image img = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("views/imagenes/myLogo.png"));
        return img;
    }
    
    
    public boolean getValidacion(){return validacion;}

    public void initVariables(){        
        switch(Start.identificador){
            case 0: cargarInicio(); break;
        }
    }
    
    public void resetTextField(){
        JTFCurso.setText("");
        JLCurso.setVisible(true);
        JTFAsignatura.setText("");
        JLAsignatura.setVisible(true);
    }
}
