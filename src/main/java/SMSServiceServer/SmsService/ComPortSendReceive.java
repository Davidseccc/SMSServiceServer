package SMSServiceServer.SmsService;

import jssc.*;

import java.io.IOException;

/**
 * @author secda1
 */
public class ComPortSendReceive {

    private static ComPortSendReceive instance = null;
    private SerialPort serialPort;
    String port;


    public static ComPortSendReceive getInstance() {
        if(instance == null) {
            instance = new ComPortSendReceive();
        }
        return instance;
    }

    public boolean open(String port) {
        SerialPortList spl = new SerialPortList();
        String[] portNames = spl.getPortNames();

        if (portNames.length == 0) {
            System.out.println("There are no serial-ports");
            System.out.println("Press Enter to exit...");
            try {
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        serialPort = new SerialPort(port);
        try {
            // opening port
            serialPort.openPort();

            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

            int mask = SerialPort.MASK_RXCHAR;
                //Set the prepared mask
            serialPort.setEventsMask(mask);
                //Add an interface through which we will receive information about events
            serialPort.addEventListener(new SerialPortReader());
            System.out.println("Connected to " + port);

            return true;
        } catch (SerialPortException ex) {
            System.out.println("Could not connect to port" + port + ": " + ex);
            return false;
        }
    }

    public boolean close(){
        try {
            System.out.println("Closing port...");
            serialPort.closePort();
            return true;
        } catch (SerialPortException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void write(String command) {
        try {
            if (!serialPort.isOpened()) {
                System.out.println("Port is not opened... Reopening.");
                open(getPort());
            }
            //serialPort.writeBytes(command.getBytes());//Write data to port
            serialPort.writeString(command.concat("\r\n"));
            System.out.println("String " + command + " has been written.");

            //serialPort.closePort();//Close serial port
        } catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }

    public String read() {
        if (!serialPort.isOpened()) {
            System.out.println("Port is not opened... Reopening.");
            open(getPort());
        }
        try {
            //serialPort.openPort();//Open serial port
            //serialPort.setParams(9600, 8, 1, 0);//Set params.
            byte[] buffer = serialPort.readBytes();
            System.out.println("Readed " + buffer.length + " bytes: \n" + new String(buffer));
            //serialPort.closePort();//Close serial port
            return new String(buffer);
        } catch (SerialPortException ex) {
            System.out.println(ex);
            return null;
        }
    }

    public String writeAndRead(String command) {
        String out = null;
        try {
        write(command);
        Thread.sleep(1000);
        out = read();
        }
        catch (InterruptedException ex) {
            System.out.println(ex);
        }
        return out;
    }

    public String[] scanPorts() {
        SerialPortList spl = new SerialPortList();
        String[] portNames = spl.getPortNames();

        System.out.println("Available com-ports:");
        for (int i = 0; i < portNames.length; i++) {
            System.out.println(portNames[i]);
        }
        return portNames;
    }

    // receiving response from port
    private class SerialPortReader implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event) {
            //Object type SerialPortEvent carries information about which event occurred and a value.
            //For example, if the data came a method event.getEventValue() returns us the number of bytes in the input buffer.
           /* if (event.isRXCHAR()) {
                if (event.getEventValue() > 0) {
                    try {
                        byte buffer[]  = serialPort.readBytes();
                        String message = new String(buffer);
                        System.out.println(message);
                        String[] messages = message.split("\n");
                        int i=0;
                        for (String s: messages) {
                            System.out.println("["+i+"]" +s);
                            i++;
                        }
                        message = messages[2].trim();
                        System.out.println("Message:" + message);
                        message = SMSDecoder.fromHextoString(message);
                        System.out.println("Decoded:" + message);

                    } catch (SerialPortException exc) {
                        System.out.println(exc);
                    }
                }
            }
//If the CTS line status has changed, then the method event.getEventValue() returns 1 if the line is ON and 0 if it is OFF.
            else if (event.isCTS()) {
                if (event.getEventValue() == 1) {
                    System.out.println("CTS - ON");
                } else {
                    System.out.println("CTS - OFF");
                }
            } else if (event.isDSR()) {
                if (event.getEventValue() == 1) {
                    System.out.println("DSR - ON");
                } else {
                    System.out.println("DSR - OFF");
                }
            }*/
        }
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
