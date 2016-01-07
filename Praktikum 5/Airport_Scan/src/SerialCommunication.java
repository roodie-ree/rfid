import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortException;

public class SerialCommunication {

    public static final byte STX = 0x2;
    public static final byte ETX = 0x3;
    public static final String GET_AMOUNT = "6C20S";
    public static final String GET_INVENTORY = "6C21";
    public static SerialPort serialPort;

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        serialPort = new SerialPort("COM1");
        try {
            if (serialPort.openPort()) {
                System.out.println("Offen");
                serialPort.setParams(115200, 8, 1, 0);
                serialPort.setFlowControlMode(serialPort.FLOWCONTROL_NONE);
                int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;
                serialPort.setEventsMask(mask);
            } else {
                System.out.println("Konnte keine Verbindung herstellen!");
            }
            sendCommand(GET_AMOUNT.getBytes());
            System.out.println(receiveResponse());
            sendCommand(GET_INVENTORY.getBytes());
            String response = receiveResponse();
            System.out.println(response);
            String[] inventory = processGetInventory(response);
            for (String item : inventory) {
                System.out.println(item);
            }
            if (serialPort.closePort()) {
                System.out.println("Geschlossen");
            } else {
                System.out.println("Konnte die Verbindung nicht schließen!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private static String[] processGetInventory(String response) {
        if (response.substring(0, 4).compareTo("6C21") != 0)
            return new String[0];

        String ids = response.substring(8);
        int amount = Integer.parseInt(response.substring(4, 8), 16);
        String[] inventory = new String[amount];
        for(int i = 0; i < amount; i++) {
            inventory[i] = ids.substring(i*16, i*16+16);
        }
        return inventory;
    }

    private static String receiveResponse() throws SerialPortException {
        int bufferSize = 1024;
        int i = 0;
        boolean stxRead = false;
        char buffer[] = new char[bufferSize];
        while (i < bufferSize) {
            try {
                byte smallBuffer[] = serialPort.readBytes(1);
                if (smallBuffer[0] == 0x2) {
                    stxRead = true;
                } else if (smallBuffer[0] == 0x3) {
                    serialPort.readBytes(1);
                    break;
                } else if (stxRead) {
                    buffer[i] = (char) smallBuffer[0];
                    i++;
                }
            } catch (Exception e) {
            }
        }
        return String.copyValueOf(buffer);
    }

    private static void sendCommand(byte[] command) {
        command = calcScemtecFullCmd(command);
        try {
            serialPort.writeBytes(command);
        } catch (SerialPortException ex) {
            Logger.getLogger(Check.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static byte calcScemtecCRC(byte[] bArr) {
        byte crc = 0x0; // initialize CRC
        for (int i = 0; i < bArr.length; i++) {
            crc ^= bArr[i]; // XOR
        }
        return crc;
    }

    public static byte[] calcScemtecFullCmd(byte[] cmd) {
        byte bArr[] = new byte[cmd.length + 2]; // STX, cmd, ETX
        bArr[0] = STX; // start with STX
        for (int i = 0; i < cmd.length; i++) {
            bArr[i + 1] = cmd[i]; // fill after STX
        }
        bArr[cmd.length + 1] = ETX; // end with ETX
        byte crc = calcScemtecCRC(bArr); // get CRC
        // new array with CRC
        byte bArr2[] = new byte[bArr.length + 1]; // STX, cmd, ETX, CRC
        for (int i = 0; i < bArr.length; i++) {
            bArr2[i] = bArr[i]; // copy
        }
        bArr2[bArr.length] = crc;
        return bArr2;
    }
}