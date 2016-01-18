package checkdasgepaeck;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class UI {

    private JFrame frame;
    private JPanel northPanel, southPanel;
    private JLabel tagCounter;
    private ItemListModel tagListModel;
    private JList tagList;
    private Timer timer;

    public UI() {
        tagListModel = new ItemListModel();
        tagList = new JList(tagListModel);
        tagList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (tagListModel.isElementGood(index)) {
                    setBackground(Color.GREEN);
                } else {
                    setBackground(Color.RED);
                }
                if (isSelected) {
                    setBackground(getBackground().darker());
                }
                return c;
            }
        }
        );
        timer = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                tagListModel.update();
                tagCounter.setText("Anzahl Tags: " + tagListModel.getSize());
                setPanelColor();
            }
        });
        timer.setRepeats(true);
        timer.start();
        frame = new JFrame();

        northPanel = new JPanel();

        northPanel.setBackground(Color.green);

        northPanel.setPreferredSize(
                new Dimension(100, 100));

        southPanel = new JPanel();

        tagCounter = new JLabel("Anzahl Tags: " + tagListModel.getSize());

        southPanel.add(tagCounter);

        tagList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        tagList.setLayoutOrientation(JList.VERTICAL);

        tagList.setVisibleRowCount(
                -1);
        JScrollPane listScroller = new JScrollPane(tagList);

        listScroller.setPreferredSize(
                new Dimension(100, 100));

        frame.setMinimumSize(
                new Dimension(500, 300));
        BorderLayout layout = new BorderLayout();

        frame.getContentPane()
                .setLayout(layout);
        frame.add(northPanel, BorderLayout.NORTH);

        frame.add(southPanel, BorderLayout.SOUTH);

        frame.add(listScroller, BorderLayout.CENTER);

        frame.pack();

        frame.toFront();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                SerialCommunication.close();
                timer.stop();
            }
        });

        frame.setTitle(
                "Check das Gepäck!");
        frame.setVisible(
                true);
    }

    private void setPanelColor() {
        Color allGood = Color.GREEN;
        for (int i = 0; i < tagListModel.getSize(); i++) {
            if (!tagListModel.isElementGood(i)) {
                allGood = Color.RED;
            }
        }
        northPanel.setBackground(allGood);
    }
}
