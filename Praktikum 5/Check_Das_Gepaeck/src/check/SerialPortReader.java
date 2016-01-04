package check;

import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

class SerialPortReader implements SerialPortEventListener {

    public String letzteNachricht = "";			//So steht die letzte Empfangene Nachricht der Main-Klasse zur Verfügung
    public int ResponseLength=100;					//Wird vor dem Senden aus der Main-Klasse heraus gesetzt

    public void serialEvent(SerialPortEvent event) {	//Routine wird bei jedem Datenempfang ausgelöst

        System.out.println("Hallo");
        try {
            System.out.println("try");
            byte buffer[] = Check.serialPort.readBytes(ResponseLength);	//Schreibe empfangene Daten in einen Buffer
            String s = new String(buffer);								//Schriebe die Daten aus dem Buffer in einen String
            letzteNachricht = s;
        } catch (SerialPortException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (event.isCTS()) {//If CTS line has changed state
            if (event.getEventValue() == 1) {//If line is ON
                System.out.println("CTS - ON");
            } else {
                System.out.println("CTS - OFF");
            }
        }

        if (event.isDSR()) {///If DSR line has changed state
            if (event.getEventValue() == 1) {//If line is ON
                System.out.println("DSR - ON");
            } else {
                System.out.println("DSR - OFF");
            }
        }

    }
}
