package checkdasgepaeck;

import java.io.IOException;
import javax.swing.*;

public class CheckDasGepaeck {

    public static UI ui;

    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException |
                InstantiationException |
                IllegalAccessException |
                UnsupportedLookAndFeelException ex) {
        }
        SerialCommunication.open();
        ui = new UI();
    }

}
