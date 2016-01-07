import javax.swing.*;
import java.awt.*;

/**
 * Created by david on 07/01/2016.
 */
public class GoodBadListCellRenderer extends DefaultListCellRenderer{
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component cell =  super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (true) {
            cell.setBackground(Color.green);
        } else {
            cell.setBackground(Color.red);
        }
    }
}
