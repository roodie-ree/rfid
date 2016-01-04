package check;

import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortException;

public class Check {
    public static final byte STX = 0x2;
    public static final byte ETX = 0x3;
    public static SerialPort serialPort;
    public static SerialPortReader spreader;

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
                spreader = new SerialPortReader();
                serialPort.addEventListener(spreader);
            } else {
                System.out.println("Konnte keine Verbindung herstellen!");
            }
            byte[] command = new byte[] {0x2, 0x36, 0x43, 0x32, 0x30, 0x73, 0x03, 0x05};
            for (int i = 0; i < 10; i++) {
                sendCommand(command);
                Thread.sleep(1000);
            }
            if (serialPort.closePort()) {
                System.out.println("Geschlossen");
            } else {
                System.out.println("Konnte die Verbindung nicht schließen!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        //spreader.letzteNachricht = "";
        //sendCommand("aa");

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
