package SMSServiceServer.SmsService.iface;


import SMSServiceServer.SmsService.SmsEvent;

/**
 * Created by fim on 11.05.17.
 */
public interface SmsServiceInterface {
    void setupSMS();
    void sendSMS(SmsEvent e);
    void propagateEvent(SmsEvent e);


}
