package checkdasgepaeck;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortException;

/**
 * Communicates with the Scemtec HF-RFID reader via serial port
 * getInventory handles all necessary steps of communication
 */
public class SerialCommunication {

    private static final byte STX = 0x2;
    private static final byte ETX = 0x3;
    private static final String GET_AMOUNT = "6C20S";
    private static final String GET_INVENTORY = "6C21";
    private static final SerialPort serialPort = new SerialPort("COM1");

    /**
     * open connection to the scemtec reader
     * @return true if connected successfull
     */
    public static boolean open() {
        try {
            if (serialPort.openPort()) {
                serialPort.setParams(115200, 8, 1, 0);
                serialPort.setFlowControlMode(serialPort.FLOWCONTROL_NONE);
                int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;
                serialPort.setEventsMask(mask);
            } else {
                System.out.println("Konnte keine Verbindung herstellen!");
                return false;
            }
        } catch (SerialPortException ex) {
            Logger.getLogger(SerialCommunication.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * close connection to the scemtec reader
     * @return true if closed successfull
     */
    public static boolean close() {
        try {
            if (serialPort.closePort()) {
            } else {
                System.out.println("Konnte die Verbindung nicht schließen!");
                return false;
            }
        } catch (SerialPortException ex) {
            Logger.getLogger(SerialCommunication.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * @return List of IDs of tags in scanning range
     */
    public static ArrayList<String> getInventory() {
        // get amount of tags in scanning range
        sendCommand(GET_AMOUNT.getBytes());
        // discard response
        receiveResponse();
        // get IDs of tags in scanning range
        sendCommand(GET_INVENTORY.getBytes());
        String response = receiveResponse();
        return processGetInventory(response);
    }


    private static ArrayList<String> processGetInventory(String response) {
        if (response.substring(0, 4).compareTo(GET_INVENTORY) != 0) {
            System.out.println("Wrong response. Expected " + GET_INVENTORY + ", got " + response.substring(0, 4));
            return new ArrayList();
        }
        //
        String ids = response.substring(8);
        int amount = Integer.parseInt(response.substring(4, 8), 16);
        ArrayList<String> inventory = new ArrayList();
        for (int i = 0; i < amount; i++) {
            inventory.add(ids.substring(i * 16, i * 16 + 16));
        }
        return inventory;
    }

    private static String receiveResponse() {
        int bufferSize = 1024;
        int i = 0;
        boolean stxRead = false;
        char buffer[] = new char[bufferSize];
        // read one byte at a time, until we get command end (ETX)
        while (i < bufferSize) {
            try {
                byte smallBuffer[] = serialPort.readBytes(1);
                // command start read
                if (smallBuffer[0] == STX) {
                    stxRead = true;
                // command end read
                } else if (smallBuffer[0] == ETX) {
                    // discard checksum
                    serialPort.readBytes(1);
                    break;
                } else if (stxRead) {
                    buffer[i] = (char) smallBuffer[0];
                    i++;
                }
            } catch (Exception ex) {
                Logger.getLogger(SerialCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // now the buffer contains everything between start and end
        return String.copyValueOf(buffer);
    }

    private static void sendCommand(byte[] command) {
        command = calcScemtecFullCmd(command);
        try {
            serialPort.writeBytes(command);
        } catch (SerialPortException ex) {
            Logger.getLogger(SerialCommunication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static byte calcScemtecCRC(byte[] bArr) {
        byte crc = 0x0; // initialize CRC
        for (int i = 0; i < bArr.length; i++) {
            crc ^= bArr[i]; // XOR
        }
        return crc;
    }

    private static byte[] calcScemtecFullCmd(byte[] cmd) {
        byte bArr[] = new byte[cmd.length + 2]; // STX, cmd, ETX
        bArr[0] = STX; // start with STX
        System.arraycopy(cmd, 0, bArr, 1, cmd.length); // fill after STX
        bArr[cmd.length + 1] = ETX; // end with ETX
        byte crc = calcScemtecCRC(bArr); // get CRC
        // new array with CRC
        byte bArr2[] = new byte[bArr.length + 1]; // STX, cmd, ETX, CRC
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length); // copy
        bArr2[bArr.length] = crc;
        return bArr2;
    }
}
