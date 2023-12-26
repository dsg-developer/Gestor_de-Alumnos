package views;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author Work-Game
 */

public class Renderer_table extends DefaultTableCellRenderer{
    DefaultTableCellRenderer dcr = new DefaultTableCellRenderer();
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, 
                                                    int row, int column){
        //System.out.println(String.valueOf(value));
        //dcr.setHorizontalAlignment(SwingConstants.CENTER);
        if(column == 3){
            JCheckBox select = new JCheckBox();
            //setHorizontalAlignment(CENTER);
            //setVerticalAlignment(CENTER);
            if (row%2 == 0) {
                //table.getRow().setBackground(Color.red);
            }
            if(Byte.parseByte(String.valueOf(value)) == 1)
               select.setSelected(true);
            else    select.setSelected(false);
            return select;
        }
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
    
    
    
    
    
}
