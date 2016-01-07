/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkswing;

import java.io.*;
import java.util.*;

/**
 *
 * @author christian
 */
public class Checkswing {

    static private HashMap<String, String[]> tagIDs;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        System.out.println("Hallo du da.");
        idauslesen();
    }

    public static void idauslesen() throws IOException {

        FileReader fr = new FileReader("tagid.txt");
        BufferedReader br = new BufferedReader(fr);
        tagIDs = new HashMap<>();
        String zeileauslesen = "";

        while ((zeileauslesen = br.readLine()) != null) {
            String[] taginfo = zeileauslesen.split(" ");
            tagIDs.put(taginfo[0], new String[]{taginfo[1], taginfo[2]});
        }

        /*
        String tagid = "3dc34c000104e0";
        String value[] = tagIDs.get(tagid);
        System.out.println("TagID: " + tagid + " Gegenstand: " + value[0] + " Status: " + value[1]);
         */
        for (String tagidfor : tagIDs.keySet()) {
            String[] contents = tagIDs.get(tagidfor);

            System.out.println("TagID: " + tagidfor + ", Gegenstand: " + contents[0] + ", Status: " + contents[1]);
        }

        br.close();

        FileReader fr2 = new FileReader("antwortstxetx.txt");
        BufferedReader br2 = new BufferedReader(fr2);

        String antwort1 = "";
        String antwort2 = "";

        antwort1 = br2.readLine();
        antwort2 = br2.readLine();

        if (antwort1.substring(0, 4).compareTo("6C20") == 0) {
            int anzahltags = Integer.parseInt(antwort1.substring(6), 16);  
            System.out.println("Anzahl Tags: " + anzahltags);
        }

        if (antwort2.substring(0, 4).compareTo("6C21") == 0) {
            int anzahltags = Integer.parseInt(antwort2.substring(4, 8), 16);  
            System.out.println("Anzahl Tags: " + anzahltags);
            //System.out.println(antwort2.substring(4));
        }
        
        br2.close();
    }

}
