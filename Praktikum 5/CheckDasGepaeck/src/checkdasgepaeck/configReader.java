package checkdasgepaeck;

import java.io.*;
import java.util.*;

public class configReader {

    private HashMap<String, String[]> tagIDs;
    private String[] inventory;
    private SerialCommunication serialcom;

    public ArrayList<String[]> idauslesen() {

        ArrayList<String[]> itemList = new ArrayList<String[]>();

        try {
            FileReader fr = new FileReader("tagid.txt");
            BufferedReader br = new BufferedReader(fr);
            tagIDs = new HashMap<>();
            String zeileauslesen = "";

            while ((zeileauslesen = br.readLine()) != null) {
                String[] taginfo = zeileauslesen.split(" ");
                tagIDs.put(taginfo[0], new String[]{taginfo[1], taginfo[2]});
            }

            br.close();
        } catch (IOException e) {
            System.out.println("Keine Konfigurationsdatei vorhanden.");
        }

        //inventory = serialcom.gimmeid();
        //dieser Teil muss mit dem seriellen Zeug ersetzt werden
        ///*
        try {
            FileReader fr2 = new FileReader("antwortstxetx.txt");
            //FileReader fr2 = new FileReader("antwortstxetx2.txt");
            BufferedReader br2 = new BufferedReader(fr2);

            String antwort1 = "";
            String antwort2 = "";

            antwort1 = br2.readLine();
            antwort2 = br2.readLine();

            if (antwort1.substring(0, 4).compareTo("6C20") == 0) {
                int anzahltags = Integer.parseInt(antwort1.substring(6), 16);
            }

            if (antwort2.substring(0, 4).compareTo("6C21") == 0) {
                String ids = antwort2.substring(8);
                int anzahltags = Integer.parseInt(antwort2.substring(4, 8), 16);
                inventory = new String[anzahltags];
                for (int i = 0; i < anzahltags; i++) {
                    inventory[i] = ids.substring(i * 16, i * 16 + 16);
                    //System.out.println(inventory[i]);
                }
            }
            br2.close();
        } catch (IOException e) {
            System.out.println("Datei nicht vorhanden.");
        }
        //*/

        if (inventory.length > 0) {
            for (String tagidfor : tagIDs.keySet()) {
                for (String inventoryitem : inventory) {
                    if (inventoryitem.compareTo(tagidfor) == 0) {
                        String[] contents = tagIDs.get(tagidfor);
                        itemList.add(contents);
                        //System.out.println(contents[0] + contents[1]);
                    }
                }
            }
        }

        return itemList;
    }
}
