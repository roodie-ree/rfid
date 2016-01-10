package checkdasgepaeck;

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class UI {

    private JFrame frame;
    private JPanel northPanel, southPanel;
    private JLabel tagCounter;
    private JList tagList;
    private configReader tagReader;
    private ArrayList<String[]> tagIDs;

    public UI(Observable... observables) {
        tagReader = new configReader();
        tagIDs = tagReader.idauslesen();

        tagList = new JList(new Vector<CheckItem>() {
            {
                for (String[] item : tagIDs) {
                    add(new CheckItem(item[0], item[1]));
                    //System.out.println(item[0] + item[1]);
                }
            }
        });

        frame = new JFrame();

        northPanel = new JPanel();
        northPanel.setBackground(Color.green);
        northPanel.setPreferredSize(new Dimension(100, 100));

        southPanel = new JPanel();
        tagCounter = new JLabel("Anzahl Tags: " + tagIDs.size());
        southPanel.add(tagCounter);

        tagList.setCellRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof CheckItem) {
                    CheckItem nextCheckItem = (CheckItem) value;
                    setText(nextCheckItem.itemname);
                    if (nextCheckItem.condition.compareTo("good") == 0) {
                        setBackground(Color.GREEN);
                    } else if (nextCheckItem.condition.compareTo("bad") == 0) {
                        setBackground(Color.RED);
                        northPanel.setBackground(Color.RED);
                    }
                    if (isSelected) {
                        setBackground(getBackground().darker());
                    }
                } else {
                    setText("unbekannt");
                }
                return c;
            }

        });

        tagList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        tagList.setLayoutOrientation(JList.VERTICAL);
        tagList.setVisibleRowCount(-1);
        JScrollPane listScroller = new JScrollPane(tagList);
        listScroller.setPreferredSize(new Dimension(100, 100));

        frame.setMinimumSize(new Dimension(500, 300));
        BorderLayout layout = new BorderLayout();
        frame.getContentPane().setLayout(layout);
        frame.add(northPanel, BorderLayout.NORTH);
        frame.add(southPanel, BorderLayout.SOUTH);
        frame.add(listScroller, BorderLayout.CENTER);

        frame.pack();
        frame.toFront();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Check das Gepäck!");
        frame.setVisible(true);
    }

    class CheckItem {

        String itemname = "";
        String condition = "";

        public CheckItem(String itemname, String condition) {
            this.itemname = itemname;
            this.condition = condition;
        }
    }
}
