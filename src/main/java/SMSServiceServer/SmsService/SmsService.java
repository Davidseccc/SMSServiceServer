package SMSServiceServer.SmsService;

import SMSServiceServer.SmsService.iface.SmsEventListener;
import SMSServiceServer.SmsService.iface.SmsServiceInterface;
import SMSServiceServer.SmsService.model.PDU;
import SMSServiceServer.SmsService.utils.PhoneNumberValidator;
import SMSServiceServer.SmsService.utils.RXTX_Factory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fim on 11.05.17.
 */
public class SmsService implements SmsServiceInterface {
    private String devicePort;
    private String[] numbers;
    private String[] messages;
    private List<SmsEventListener> listeners = new ArrayList<SmsEventListener>();
    RXTX_Factory factory;
    JSerialComm jSerialComm;

    public SmsService(String devicePort) {
        this.devicePort = devicePort;
        setupSMS();
    }

    @Override
    public void setupSMS() {
        factory = new RXTX_Factory(devicePort);
    }

    @Override
    public void sendSMS(SmsEvent e) {
    }

    @Override
    public void propagateEvent(SmsEvent e) {    //module neco delej, tj CORE->MODULE
        //TODO implement propagation of event message
        if (e.getEventType() == SmsEvent.SmsEventType.OPEN_PORT) {
            factory.openPort();
        }
        if (e.getEventType() == SmsEvent.SmsEventType.CLOSE_PORT) {
            factory.closePort();
        }
        if (e.getEventType() == SmsEvent.SmsEventType.MESSAGE_SEND) {
            factory.sendSMS("number","SMS");
        }
        if (e.getEventType() == SmsEvent.SmsEventType.LIST_SMS) {
            List<PDU> list = factory.getSMS(4);
            for (PDU p:list) {
                System.out.println(p.toString());
            }
        }
    }

    private void notifyListeners(SmsEvent e) { //MODULE->CORE
        for (SmsEventListener l : listeners) {
            l.onSmsEvent(e);
        }
    }

    public void addEventListener(SmsEventListener toAdd) {
        listeners.add(toAdd);
    }


    public String getDevicePort() {
        return devicePort;
    }

    public void setDevicePort(String devicePort) {
        this.devicePort = devicePort;
    }
    public String[] getNumbers() { return numbers; }

    public void setNumbers(String[] numbers) {
        this.numbers = PhoneNumberValidator.validateNumbers(numbers);
    }

    public String[] getMessages() { return messages; }

    public void setMessages(String[] messages) { this.messages = messages; }

    public List<SmsEventListener> getListeners() { return listeners; }

    public void setListeners(List<SmsEventListener> listeners) { this.listeners = listeners; }

}
