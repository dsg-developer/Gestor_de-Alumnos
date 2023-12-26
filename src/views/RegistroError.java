package views;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.*;
import javax.swing.DefaultListModel;

/**
 * @author DSTEAM
 */

public class RegistroError extends javax.swing.JDialog {

    
    public RegistroError(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        setSize(505, 338);
        this.setIconImage(getIconImage());
        setTitle("Reporte de errores");
        MONITO_ERRORES = Logger.getLogger("main");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        JLListaErrores = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        JLListaErrores.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(JLListaErrores);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 63, 448, 180));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Registro de errores");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-1, 6, 505, 39));

        jButton1.setText("Reportar");
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(394, 261, -1, -1));

        jButton2.setText("Limpiar Registro");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 261, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        vaciarError();
    }//GEN-LAST:event_jButton2ActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                RegistroError dialog = new RegistroError(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> JLListaErrores;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    private byte conteo = 0;
    private static Logger MONITO_ERRORES;
    
    public void crearLog(){
        final String caracter = System.getProperty("file.separator");
        final String path = System.getProperty("user.home")+caracter+"AppData"+caracter+"Local"+caracter+
                            "gestor_notas";
        try {
            File rota = new File(path);
            rota.mkdir();
            Handler ch = new ConsoleHandler();
            Handler fh = new FileHandler(path+caracter+"log.ds", true);
            
            SimpleFormatter sf = new SimpleFormatter();
            fh.setFormatter(sf);
            
            MONITO_ERRORES.addHandler(ch);
            MONITO_ERRORES.addHandler(fh);
            
            ch.setLevel(Level.ALL);
            fh.setLevel(Level.ALL);
        } catch (IOException | SecurityException ex) {
            Logger.getLogger(RegistroError.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Image getIconImage(){
        Image img = Toolkit.getDefaultToolkit().getImage(ClassLoader.
                                                         getSystemResource("views/imagenes/myLogo.png"));
        return img;
    }
    
    public void mostrarError() {
        final String caracter = System.getProperty("file.separator");
        final String path = System.getProperty("user.home")+caracter+"AppData"+caracter+"Local"+caracter+
                            "gestor_notas"+caracter+"log.ds";
        try (FileReader ruta = new FileReader(new File(path)); BufferedReader br = new BufferedReader(ruta);){
            String linea = null;
            DefaultListModel lm = new DefaultListModel();
            while ((linea=br.readLine()) != null) {                
                lm.addElement(linea);
            }
            JLListaErrores.setModel(lm);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RegistroError.class.getName()).log(Level.SEVERE, null, ex);
            registrarError(Level.SEVERE, "Nuevo error FileNotFoundException al mostrar los errores\n");
            registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        } catch (IOException ex) {
            Logger.getLogger(RegistroError.class.getName()).log(Level.SEVERE, null, ex);
            registrarError(Level.SEVERE, "Nuevo error IO al mostrar los errores\n");
            registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }
        
    public static void registrarError(Level nivel, String info){
        MONITO_ERRORES.log(nivel, info);
    }
    
    public static String getStackTrace(Exception ex){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }
    
    public void vaciarError() {
        final String caracter = System.getProperty("file.separator");
        final String path = System.getProperty("user.home")+caracter+"AppData"+caracter+"Local"+caracter+
                            "gestor_notas"+caracter+"log.ds";
        try (FileWriter ruta = new FileWriter(new File(path)); PrintWriter pw = new PrintWriter(ruta);){
            DefaultListModel lm = new DefaultListModel();
            pw.print("");
            mostrarError();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RegistroError.class.getName()).log(Level.SEVERE, null, ex);
            registrarError(Level.SEVERE, "Nuevo error FileNotFoundException al mostrar los errores\n");
            registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        } catch (IOException ex) {
            Logger.getLogger(RegistroError.class.getName()).log(Level.SEVERE, null, ex);
            registrarError(Level.SEVERE, "Nuevo error IO al mostrar los errores\n");
            registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }
}
