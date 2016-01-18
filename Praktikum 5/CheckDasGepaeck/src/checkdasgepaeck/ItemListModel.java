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

    public ItemListModel() {
        itemNames = new configReader().read();
        items = new ArrayList<>();
    }

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

    public boolean isElementGood(int index) {
        String[] item = itemNames.get(items.get(index));
        if (item != null) {
            return item[1].compareTo("good") == 0;
        } else {
            return false;
        }
    }
}
