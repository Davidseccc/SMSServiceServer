package SMSServiceServer.SmsService.iface;

import SMSServiceServer.SmsService.SmsEvent;

/**
 * Created by fim on 11.05.17.
 */

public interface SmsEventListener {
    void onSmsEvent(SmsEvent e);
    void onMessageReceived(SmsEvent e);
    void onMessageSent(SmsEvent e);
}
