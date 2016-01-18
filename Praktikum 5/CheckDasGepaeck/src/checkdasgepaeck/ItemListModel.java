package checkdasgepaeck;

import javax.swing.*;
import java.util.*;

/**
 * ListModel for the list of items currently in range
 * update must be called to receive current data
 * isElementGood provides information on the goodness of items
 */
public class ItemListModel extends AbstractListModel<String> {

    private HashMap<String, String[]> itemNames;
    private ArrayList<String> items;

    /**
     * Initialize model from configuration file
     */
    public ItemListModel() {
        itemNames =  ConfigReader.read();
        items = new ArrayList<>();
    }

    /**
     * Update tags in range
     */
    public void update() {
        items = SerialCommunication.getInventory();
        fireContentsChanged(this, 0, items.size() - 1);
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public String getElementAt(int index) {
        String[] item = itemNames.get(items.get(index));
        if (item != null) {
            return item[0];
        } else {
            return "Unbekanntes Produkt";
        }
    }

    /**
     * @param index of the element
     * @return true if element is good, else false
     */
    public boolean isElementGood(int index) {
        String[] item = itemNames.get(items.get(index));
        if (item != null) {
            return item[1].compareTo("good") == 0;
        } else {
            return false;
        }
    }
}
