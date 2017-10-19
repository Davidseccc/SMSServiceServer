package SMSServiceServer.SmsService.iface;

/**
 * Created by fim on 17.07.17.
 */
public interface ApiServiceInterface {
    void getSMS(int type);
    void getAllSMS();
    void send();
}
