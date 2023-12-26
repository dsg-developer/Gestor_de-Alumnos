/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import controllers.singletons.SingletonModels;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

/**
 * @author Work-Game
 */

public class CellEditor_table extends JCheckBox implements TableCellEditor{
     /** Lista de suscriptores */
    private byte value;
     private List<Object> suscriptores = new ArrayList<>();

    public CellEditor_table() {        
         // Nos apuntamos a cuando se seleccione algo, para avisar a la tabla
         // de que hemos cambiado el dato.
         this.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed (ActionEvent evento)
             {
                 editado(true);
             }
         });
         
         // Nos apuntamos a la pérdida de foco, que quiere decir que se ha
         // dejado de editar la celda, sin aceptar ninguna opción. Avisamos
         // a la tabla de la cancelación de la edición.
         this.addFocusListener(new FocusListener() {
             @Override
             public void focusGained (FocusEvent e) {;}
             @Override
             public void focusLost (FocusEvent e)
             {
                 editado (false);
             }
         });
        
    }
    
     @Override
    public Component getTableCellEditorComponent(JTable jtable, Object o, boolean bln, int row, int column) {
         if(Byte.parseByte(String.valueOf(o)) == 1) this.setSelected(true);
         else   setSelected(false);
       
        return this;
    }

    @Override
    public Object getCellEditorValue() {
        if(this.isSelected()) return 1;
        else return 0;
    }

    @Override
    public boolean isCellEditable(EventObject eo) {
        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject eo) {
        return true;
    }

    @Override
    public boolean stopCellEditing() {
        return true;
    }

    @Override
    public void cancelCellEditing() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addCellEditorListener(CellEditorListener l) {
       // Se añade el suscriptor a la lista.
        suscriptores.add (l);
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l) {
        // Se elimina el suscriptor.
        suscriptores.remove(l);
    }

    /**
      * Si cambiado es true, se avisa a los suscriptores de que se ha terminado
      * la edición. Si es false, se avisa de que se ha cancelado la edición.
      */
     protected void editado(boolean cambiado)     {
        ChangeEvent evento = new ChangeEvent (this);
        for (int i=0; i<suscriptores.size(); i++){
            CellEditorListener aux = (CellEditorListener)suscriptores.get(i);
            if (cambiado)
                aux.editingStopped(evento);
            else
                aux.editingCanceled(evento);
        }
    }
}
