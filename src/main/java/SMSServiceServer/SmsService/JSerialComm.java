package SMSServiceServer.SmsService;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

/**
 * Created by fim on 09.07.17.
 */
public class JSerialComm {

    private SerialPort serialPort;
    private StringBuilder message = new StringBuilder();

    public void run(){
        initialize();

    }

    public void write(String message) {
        try {
            message = message.concat("\r\n");
            serialPort.writeBytes(message.getBytes(), message.getBytes().length);
        } catch (Exception ex) {
            System.out.println("Error");
            ex.printStackTrace();
        }
    }
    public void close() {
        if (this.serialPort.closePort()) {
            System.out.println("serialPort{} closed" + serialPort.getDescriptivePortName());
        } else {
            System.out.println("Failed to close serialPort{}" + serialPort.getDescriptivePortName());
        }
    }

    private void initialize() {
        SerialPort[] serialPorts = SerialPort.getCommPorts();
        System.out.println("Number of serial port available:{}" + serialPorts.length);
        for (int portNo = 0; portNo < serialPorts.length; portNo++) {
            System.out.println("SerialPort[{}]:[{},{}]" + portNo + 1 + serialPorts[portNo].getSystemPortName() +
                    serialPorts[portNo].getDescriptivePortName());
        }

        // create an instance of the serial communications class
        serialPort = SerialPort.getCommPort("/dev/tty.HUAWEIMobile-Modem");

        serialPort.openPort();//Open port
        if (!serialPort.isOpen()) {
            System.out.println("Unable to open serial port:[{}]");
            return;
        }
        serialPort.setComPortParameters(
                9600,
                8,  // data bits
                SerialPort.ONE_STOP_BIT,
                SerialPort.NO_PARITY);

        serialPort.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);

        serialPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                    return;
                }
                try {
                    byte[] buffer = new byte[serialPort.bytesAvailable()];
                    serialPort.readBytes(buffer, buffer.length);
                    String toProcess = new String(buffer);
                    System.out.println("Received a message:[{}]" + toProcess);
                } catch (Exception rEx) {
                    message.setLength(0);
                }
            }
        });
        if(serialPort.openPort()){
            System.out.println("opened");
            write("AP");
        }
        else{
            System.out.println("closed");
        }

        // create and register the serial data listener
        System.out.println("Serial port initialized with the driver:{}, PortName:{}, BaudRate:{}");
    }
}
