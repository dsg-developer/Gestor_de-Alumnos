package views;

import controllers.CallBack;
import controllers.singletons.Singleton_Controllers;
import controllers.singletons.Singleton_Interfaces;
import java.util.Vector;
import javax.swing.JCheckBox;
import javax.swing.table.DefaultTableModel;
import main.Start;

/**
 * @author Work
 */

public class ModelsTable {
    private CallBack aviso;
    private Singleton_Controllers controlador;
        
    public ModelsTable() {Start.setConteo(Start.getConteo()+1.0f);}    
    
    public DefaultTableModel addOrSelect(){
        return (new DefaultTableModel(null, AddOrSelect.columnas){
            @Override
            public Class getColumnClass(int columnIndex){
                return AddOrSelect.tipos[columnIndex];
            }
            
            @Override
            public boolean isCellEditable(int row, int column){
                return AddOrSelect.permitirEdicion[column];
            }
        });
    }
    
    public DefaultTableModel alumnos(){
        return (new DefaultTableModel(null, new String[]{"Número", "Apellido", "Nombre", "Promedio", "Porcentaje de Asistencia"}){
            
            Class[] tipos = {Byte.class, String.class, String.class, byte.class, String.class};
            boolean[] permitirEdicion = {true, true, true, false, false};
            
            @Override
            public Class getColumnClass(int columnIndex){
                return tipos[columnIndex];
            }
            
            @Override
            public boolean isCellEditable(int row, int column){
                return permitirEdicion[column];
            }
                        
            @Override
            public void setValueAt(Object value, int row, int column){
                if(aviso != null) aviso.avisoListener(value);
                super.setValueAt(value, row, column);
            }
             
            @Override
            public void fireTableCellUpdated(int row, int column) {
                super.fireTableCellUpdated(row, column); //To change body of generated methods, choose Tools
                //| Templates.
            }
        });
    }
    
    public DefaultTableModel asignatura(){
        controlador = Singleton_Controllers.getInstance();
        return (new DefaultTableModel(null, new String[]{"Asignatura"}){
            Class[] tipos = {String.class};
            boolean[] permitirEdicion = {true};
            
            @Override
            public Class getColumnClass(int columnIndex){
                return tipos[columnIndex];
            }
            
            @Override
            public boolean isCellEditable(int row, int column){
                return permitirEdicion[column];
            }
        
            @Override
            public void fireTableCellUpdated(int row, int column) {
                controlador.add_select.editarAsignatura();
                super.fireTableCellUpdated(row, column); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }
    
    public DefaultTableModel asistencia(){
        return (new DefaultTableModel(null, new String[]{"Número", "Apellido", "Nombre", "Asistencia"}){
            
            Class[] tipos = {byte.class, String.class, String.class, Boolean.class};
            boolean[] permitirEdicion = {false, false, false, true};
            
            @Override
            public Class getColumnClass(int columnIndex){
                return tipos[columnIndex];
            }
            
            @Override
            public boolean isCellEditable(int row, int column){
                return permitirEdicion[column];
            }
            
            @Override
            public void setValueAt(Object value, int row, int column){
                if (aviso != null) {
                    aviso.avisoListener(value);}
                super.setValueAt(value, row, column);
            }
            
            @Override
            public void fireTableCellUpdated(int row, int column){
                controlador = Singleton_Controllers.getInstance();
                new Thread(controlador.principal.hiloUpdateAlumno).start();
                super.fireTableCellUpdated(row, column);
            }
            
        });
    }
    
    public DefaultTableModel calificacionIndicador(){
        return (new DefaultTableModel(null, new String[]{"Número", "Apellido", "Nombre", "Calificación"}){
            Class[] tipos = {byte.class, String.class, String.class, byte.class};
            boolean[] permitirEdicion = {false, false, false, false};
            
            @Override
            public Class getColumnClass(int columnIndex){
                return tipos[columnIndex];
            }
            
            @Override
            public boolean isCellEditable(int row, int column){
                return permitirEdicion[column];
            }
        });
    }
    
    public DefaultTableModel competencia(){
        return (new DefaultTableModel(null, new String[]{"Competencia"}){
            Class[] tipos = {String.class};
            boolean[] permitirEdicion = {false};
            
            @Override
            public Class getColumnClass(int columnIndex){
                return tipos[columnIndex];
            }
            
            @Override
            public boolean isCellEditable(int row, int column){
                return permitirEdicion[column];
            }
        });
    }
    
    public DefaultTableModel curso(){
        controlador = Singleton_Controllers.getInstance();
        return (new DefaultTableModel(null, new String[]{"Curso"}){
            Class[] tipos = {String.class};
            boolean[] permitirEdicion = {true};
            
            @Override
            public Class getColumnClass(int columnIndex){
                return tipos[columnIndex];
            }
            
            @Override
            public boolean isCellEditable(int row, int column){
                return permitirEdicion[column];
            }
        
            @Override
            public void fireTableCellUpdated(int row, int column) {
                controlador.add_select.editarCurso();
                super.fireTableCellUpdated(row, column); //To change body of generated methods, choose Tools | Templates.
            }
            
        });
    }
    
    public DefaultTableModel indicador(){
        return (new DefaultTableModel(null, new String[]{"Indicador"}){
            Class[] tipos = {String.class};
            boolean[] permitirEdicion = {false};
            
            @Override
            public Class getColumnClass(int columnIndex){
                return tipos[columnIndex];
            }
            
            @Override
            public boolean isCellEditable(int row, int column){
                return permitirEdicion[column];
            }
        });
    }
    
    public DefaultTableModel indicador_logro(byte edicion){
        if(edicion == 0){
            return (new DefaultTableModel(null, new String[]{"Unidad", "Competencia", "Indicador"}){

                Class[] tipos = {String.class, String.class, String.class};
                boolean[] permitirEdicion = {false, false, false};

                @Override
                public Class getColumnClass(int columnIndex){
                    return tipos[columnIndex];
                }

                @Override
                public boolean isCellEditable(int row, int column){
                    return permitirEdicion[column];
                }
            });
        }else{
                return (new DefaultTableModel(null, new String[]{"Unidad", "Competencia", "Tema", "periodo"}){

                Class[] tipos = {String.class, String.class, String.class, byte.class};
                boolean[] permitirEdicion = {false, false, false, true};

                @Override
                public Class getColumnClass(int columnIndex){
                    return tipos[columnIndex];
                }

                @Override
                public boolean isCellEditable(int row, int column){
                    return permitirEdicion[column];
                }

                @Override
                public void setValueAt(Object value, int row, int column){
                    if (aviso != null) {aviso.avisoListener(value);}
                }
            });
        }
    }
        
    public DefaultTableModel IndicadorRP(){
        controlador = Singleton_Controllers.getInstance();
        return (new DefaultTableModel(null, new String[]{"Indicador", "Tema"}){
            Class[] tipos = {String.class, String.class};
            boolean[] permitirEdicion = {false, false};
            
            @Override
            public Class getColumnClass(int columnIndex){
                return tipos[columnIndex];
            }
            
            @Override
            public boolean isCellEditable(int row, int column){
                return permitirEdicion[column];
            }
        
        });
    }
    
    public DefaultTableModel notas(){
        return (new DefaultTableModel(null, new String[]{"Número", "Apellido", "Nombre", "P1", "P2", "P3", "P4"}){
            Class[] tipos = {byte.class, String.class, String.class, byte.class, byte.class, byte.class, byte.class};
            boolean[] permitirEdicion = {false, false, false, false, false, false, false};
            
            @Override
            public Class getColumnClass(int columnIndex){
                return tipos[columnIndex];
            }
            
            @Override
            public boolean isCellEditable(int row, int column){
                return permitirEdicion[column];
            }
        });
    }
    
    public DefaultTableModel RegistroPersonal(){
        return (new DefaultTableModel(null, new String[]{"Número", "Apellido", "Nombre"}){
            @Override
            public Class getColumnClass(int columnIndex){
                return super.getColumnClass(columnIndex);
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                switch(column){
                    case 0: return false;
                    case 1: return false;
                    case 2: return false;
                    default:    return true;
                }
            }
            
            @Override
            public void setValueAt(Object value, int row, int column){
                if(aviso != null) aviso.avisoListener(value);
                super.setValueAt(value, row, column);
            }
        
            @Override
            public void fireTableCellUpdated(int row, int column){
                controlador = Singleton_Controllers.getInstance();
                new Thread(controlador.addRegistro_Personal.putNotaIndecadores).start();
                super.fireTableCellUpdated(row, column);
            }
            
        });
    }
    
    public DefaultTableModel temasRP(){
        return (new DefaultTableModel(null, new String[]{"Tema"}){
            Class[] tipos = {String.class};
            boolean[] permitirEdicion = {false};
            
            @Override
            public Class getColumnClass(int columnIndex){
                return tipos[columnIndex];
            }
            
            @Override
            public boolean isCellEditable(int row, int column){
                return permitirEdicion[column];
            }
        });
    }
    
    public DefaultTableModel unidades(){
        controlador = Singleton_Controllers.getInstance();
        return (new DefaultTableModel(null, new String[]{"Unidad"}){
            Class[] tipos = {String.class};
            boolean[] permitirEdicion = {true};
            
            
            @Override
            public Class getColumnClass(int columnIndex){
                return tipos[columnIndex];
            }
            
            @Override
            public boolean isCellEditable(int row, int column){
                return permitirEdicion[column];
            }
                
            @Override
            public void fireTableCellUpdated(int row, int column) {
                controlador.add_select.editarUnidad();
                super.fireTableCellUpdated(row, column); //To change body of generated methods, choose Tools | Templates.
            }
            
        });
    }
    
    public void setCallBackListener(CallBack aviso){
        this.aviso = aviso;
    }

    public void setAviso(CallBack aviso) {
        this.aviso = aviso;
    }
}
