import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.event.ListDataListener;


public class UI{

    private JFrame frame;
    private JList itemList;
    private JButton scanButton;

    public UI(Observable... observables){
        frame = new JFrame();
        itemList = new JList();
        itemList.setModel(new ItemListModel(new HashMap<String, String>()));
        itemList.setCellRenderer();
        scanButton = new JButton();
        scanButton.setMinimumSize(new Dimension(240,240));
        scanButton.setText("Scan");
        frame.setMinimumSize(new Dimension(752, 480));
        BorderLayout layout = new BorderLayout();
        frame.getContentPane().setLayout(layout);
        frame.add(itemList, BorderLayout.CENTER);
        frame.add(scanButton, BorderLayout.NORTH);

        frame.pack();
        //frame.setExtendedState(frame.getExtendedState() | frame.MAXIMIZED_BOTH);
        frame.toFront();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Airport Scanner");
        frame.setVisible(true);
    }
}
