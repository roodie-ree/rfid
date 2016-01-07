import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by david on 07/01/2016.
 */
public class ItemListModel implements ListModel {

    private HashMap<String, String[]> itemNames;
    private ArrayList<String> items;

    public ItemListModel(HashMap<String, String[]> idToName) {
        itemNames = idToName;
        items = new ArrayList<String>();
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public Object getElementAt(int index) {
        return itemNames.get(items.get(index))[0];
    }

    public boolean isElementGood(int index) {
        return itemNames.get(items.get(index))[1].compareTo("good") == 0;
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }
}
