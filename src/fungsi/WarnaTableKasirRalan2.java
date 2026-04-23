/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fungsi;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Owner
 */
public class WarnaTableKasirRalan2 extends DefaultTableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (row % 2 == 1){
            component.setBackground(new Color(255,244,244));
            component.setForeground(new Color(50,50,50));
        }else{
            component.setBackground(new Color(255,255,255));
            component.setForeground(new Color(50,50,50));
        } 
        if(table.getValueAt(row,10).toString().equals("Selesai Dikaji")){
            component.setBackground(new Color(65,152,115));
            component.setForeground(new Color(255,255,255));
        }else if(table.getValueAt(row,10).toString().equals("Prioritas Datang")){
            component.setBackground(new Color(229,208,255));
            component.setForeground(new Color(45,41,51));               
        }else if(table.getValueAt(row,10).toString().equals("Prioritas Dikaji")){
            component.setBackground(new Color(152,111,204));
            component.setForeground(new Color(255,255,255));
        }else if(table.getValueAt(row,10).toString().equals("Cek Lab")){
            component.setBackground(new Color(232,112,42));
            component.setForeground(new Color(255,255,255));               
        }else if(table.getValueAt(row,10).toString().equals("Datang")){
            component.setBackground(new Color(255,129,160));
            component.setForeground(new Color(51,22,29));
        }else if(table.getValueAt(row,10).toString().equals("Sudah")){
            component.setBackground(new Color(41,112,204));
            component.setForeground(new Color(255,255,255));
        }else if(table.getValueAt(row,10).toString().equals("Batal")){
            component.setBackground(new Color(191,0,0));
            component.setForeground(new Color(255,255,255));
        }else if(table.getValueAt(row,10).toString().equals("Dirujuk")||table.getValueAt(row,10).toString().equals("Meninggal")||table.getValueAt(row,10).toString().equals("Pulang Paksa")){
            component.setBackground(new Color(152,152,156));
            component.setForeground(new Color(245,245,255));
        }else if(table.getValueAt(row,10).toString().equals("Dirawat")){
            component.setBackground(new Color(119,221,119));
            component.setForeground(new Color(245,255,245));
        }
        if(table.getValueAt(row,15).toString().equals("Sudah Bayar")){
            component.setBackground(new Color(50,50,50));
            component.setForeground(new Color(255,255,255));
        }
        return component;
    }

}
