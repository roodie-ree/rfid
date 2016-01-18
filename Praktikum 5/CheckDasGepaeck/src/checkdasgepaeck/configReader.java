package checkdasgepaeck;

import java.io.*;
import java.util.*;

public class configReader {

    private HashMap<String, String[]> tagIDs;

    public HashMap<String, String[]> idauslesen() {

        try {
            FileReader fr = new FileReader("tagid.txt");
            BufferedReader br = new BufferedReader(fr);
            tagIDs = new HashMap<>();
            String zeileauslesen;

            while ((zeileauslesen = br.readLine()) != null) {
                String[] taginfo = zeileauslesen.split(" ");
                tagIDs.put(taginfo[0], new String[]{taginfo[1], taginfo[2]});
            }

            br.close();
        } catch (IOException e) {
            System.out.println("Keine Konfigurationsdatei vorhanden.");
        }
        return tagIDs;
    }
}
