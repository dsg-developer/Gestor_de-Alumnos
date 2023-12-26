package models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import views.RegistroError;

/**
 * @author Work-Game
 */

public class ExportarDocumentos {
    private File ruta;
    private XSSFWorkbook libro;
    private XSSFSheet hoja;
    private String[] encabezado;
    private String[][] cuerpo;

    public static void main(String[] args) {
        new ExportarDocumentos().exportarExcel();
    }
    
    public ExportarDocumentos() {
        this.ruta = new File("prueba excel.xlsx");
        this.libro = new XSSFWorkbook();//se crea el libro con el que se va a trabajar
        this.hoja = libro.createSheet("prueba1");//se crea la hoja que se va a utilizar y se le asigna nombre
        this.encabezado = new String[]{"Numero","Nombre","Apellido","Nota"};
        this.cuerpo = new String[][]{
            {"1","Andewrson","Mazueta Cordeto","85"},
            {"2","miguel","Cordeto","85"},
            {"3","Pablo","Herrera","85"},
            {"4","natanael","Mejia","85"},
            {"5","Rocky","mercedes patio","85"},
            {"6","Enmanuel","Crodero Medina","85"},
            {"7","Santo","de la Rosa","85"},
            {"8","maicol","Rodriguez","85"},
            {"9","Pedro","Mazueta Cordeto","85"},
            {"10","Carlos","Rosario al Monte","85"},
            {"11","Frank","Guzman Medina","85"},
            {"12","Francisco","Peralta","85"},
            {"13","Roberto","Medina Rosario","85"},
            {"14","Andry","Rosario","85"},
            {"15","andiery","Polanco","85"},
            {"16","andiery","Mejia de la Rosa","85"},
            {"17","Adrian","Salcedo Acosta","85"},
            {"18","Marcia","de la Cruz","85"},
            {"19","Rosa","Ruiz","85"}
        };
    }
    
    public void exportarExcel(){
        for (int x = 0; x <= cuerpo.length; x++) {
            XSSFRow fila = hoja.createRow(x);//se crea una fila en el documento de excel
            for (int i = 0; i < encabezado.length; i++) {
                if (x == 0) {
                    XSSFCell celda = fila.createCell(i);//crear celda para el documento.
                    celda.setCellValue(encabezado[i]);//establecer el encabesado de la hoja
                } else {
                    XSSFCell celda = fila.createCell(i);//crear celdas para el cuerpo del documeto
                    celda.setCellValue(cuerpo[x-1][i]);//establecer fla
                }
            }
        }
        
        try (FileOutputStream salida = new FileOutputStream(ruta);) {
            if(ruta.exists()){
                ruta.delete();
            }
            libro.write(salida);
            salida.flush();
        } catch (FileNotFoundException ex) {
            new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, "Nuevo error "
                                                                               + "detectado en el hilo de "
                                                                                + "exportacion de alumnos\n");
                        new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, 
                                                                  RegistroError.getStackTrace(ex));
        } catch (IOException ex) {
            new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, "Nuevo error "
                                                                               + "detectado en el hilo de "
                                                                                + "exportacion de alumnos\n");
                        new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, 
                                                                  RegistroError.getStackTrace(ex));
        }
    }
    
}
