package checkdasgepaeck;

import java.io.*;
import java.util.*;

public class ConfigReader {

    /**
     * Reads configuration out of a text file
     * The expected format is:
     * tagID itemName goodOrBad
     * One item per line
     * @return Hash containing the tagID as key, name and goodness as value
     */
    public static HashMap<String, String[]> read() {
        HashMap<String, String[]> tagIDs = new HashMap<>();
        try {
            FileReader fr = new FileReader("tagid.txt");
            BufferedReader br = new BufferedReader(fr);
            String readLine;

            while ((readLine = br.readLine()) != null) {
                String[] taginfo = readLine.split(" ");
                tagIDs.put(taginfo[0], new String[]{taginfo[1], taginfo[2]});
            }

            br.close();
        } catch (IOException e) {
            System.out.println("Keine Konfigurationsdatei vorhanden.");
        }
        return tagIDs;
    }
}
