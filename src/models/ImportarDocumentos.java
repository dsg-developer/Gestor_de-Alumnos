package models;

import controllers.singletons.SingletonDtos;
import controllers.singletons.SingletonModels;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import main.Start;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import views.Carga;
import views.RegistroError;

/**
 *
 * @author Work-Game
 */
public class ImportarDocumentos {
    private Cell c;
    private Row r;
    private final SingletonDtos dtos;
    private SingletonModels modelos;
    private Thread ejecutar;

    public ImportarDocumentos() {
        dtos = SingletonDtos.getInstance();
    }
    
    public void importarExcel(File ruta){
        try (FileInputStream entrada = new FileInputStream(ruta);) {
            XSSFWorkbook libro = new XSSFWorkbook(entrada);//obteniendo el excel para leer
            XSSFSheet hoja = libro.getSheetAt(0);//obtener la hoja que se va a leer en el documento
            Iterator<Row> fila = hoja.rowIterator();//Obtener todas las filas del documento
            
            thread(fila);
            Start.setConteo(0.0f);
            Start.maxConteo = hoja.getLastRowNum();
            Start.setCargando(true);
            new Carga(ejecutar).setVisible(true);
            
        } catch (FileNotFoundException ex) {
            new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, "Nuevo error "
                                                                 + "detectado: No se encontro el archivo "
                                                                + "para importar los alumnos\n");
                        new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, 
                                                                  RegistroError.getStackTrace(ex));
        } catch (IOException ex) {
            new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, "Nuevo error "
                                                                  + "detectado de IOExceptio en importación "
                                                                     + "de alumnos\n");
                        new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, 
                                                                  RegistroError.getStackTrace(ex));
        }
    }
    
    private synchronized void thread(Iterator<Row> fila){
        ejecutar = new Thread(()->{
            List<String[]> celdaNum = new ArrayList<>();
            int v = 0;
            while (fila.hasNext()){ 
                r = fila.next();
                v++;
                //if(v > 0) Start.setCargando(false);
                //se obtienen las celdas por filas
                Iterator<Cell> celdas = r.cellIterator();
                
                //se corre cada celda
                while (celdas.hasNext()) {                    
                    c = celdas.next();
                    switch(c.getStringCellValue()){
                        case "Numero": celdaNum.add(new String[]{String.valueOf(c.getColumnIndex()),
                                        "numero"});break;
                        case "Número": celdaNum.add(new String[]{String.valueOf(c.getColumnIndex()),
                                        "numero"});break;
                        case "numero": celdaNum.add(new String[]{String.valueOf(c.getColumnIndex()),
                                        "numero"});break;
                        case "número": celdaNum.add(new String[]{String.valueOf(c.getColumnIndex()),
                                        "numero"});break;
                        
                        case "Nombre": celdaNum.add(new String[]{String.valueOf(c.getColumnIndex()),
                                        "nombre"});break;
                        case "nombre": celdaNum.add(new String[]{String.valueOf(c.getColumnIndex()),
                                        "nombre"});break;
                        
                        case "Apellido": celdaNum.add(new String[]{String.valueOf(c.getColumnIndex()),
                                        "apellido"});break;
                        case "apelido": celdaNum.add(new String[]{String.valueOf(c.getColumnIndex()),
                                        "apellido"});break;
                    }
                    celdaNum.forEach(d ->{
                        if(Integer.parseInt(d[0]) == c.getColumnIndex() && r.getRowNum() != 0){
                            switch(d[1]){
                                case "numero":  dtos.alumno.setNumero(Byte.parseByte(
                                                                        c.getStringCellValue().
                                                                        replace(",", "")));
                                                break;
                                                
                                case "nombre":  dtos.alumno.setNombre(c.getStringCellValue()
                                                                       .replace(",",""));
                                                break;
                                
                                case "apellido":    dtos.alumno.setApellido(c.getStringCellValue()
                                                                           .replace(",",""));
                                                    break;
                            }
                        }
                    });
                }
                //System.err.println(Start.isCargando());
                if(dtos.alumno.getNombre() != null){
                    try {
                        modelos = SingletonModels.getInstance();
                        modelos.alumno.search(dtos.alumno);
                        dtos.fillAlumnoCAY(dtos.alumno, dtos.curso, dtos.asignatura, dtos.yearEscolar);
                        modelos.alumno_CAY.search(dtos.alumno_CAY);
                        Start.setConteo(Start.getConteo()+1.0f);
                    } catch (DaoException ex) {
                        new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, "Nuevo error "
                                                                               + "detectado en el hilo de "
                                                                                + "importacion de alumnos\n");
                        new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, 
                                                                  RegistroError.getStackTrace(ex));
                    }
                }
            }
            Start.setCargando(false);
            
            JOptionPane.showMessageDialog(null, "El Proceso de importacion ha finalizado con exito.", 
                                                "Informacion", JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
