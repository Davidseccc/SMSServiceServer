package SMSServiceServer.SmsService.iface;

import SMSServiceServer.SmsService.SmsEvent;

/**
 * Created by fim on 17.07.17.
 */
public interface ApiEventListener {
    void onAPIEvent(SmsEvent e);
    void onAPIReceived(SmsEvent e);
    void onAPISent(SmsEvent e);


}
