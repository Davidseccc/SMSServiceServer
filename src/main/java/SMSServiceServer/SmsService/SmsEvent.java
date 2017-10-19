package SMSServiceServer.SmsService;

/**
 * Created by fim on 11.05.17.
 */
public class SmsEvent {

    public enum SmsEventType {
        LIST_SMS,
        OPEN_PORT,
        CLOSE_PORT,
        MESSAGE_ARRIVED,
        MESSAGE_SEND,
        CREDIT_LOW,
        NO_SIGNAL,
        SEND_FAILED
    }

    SmsEventType eventType;

    String eventMessage;
    Object eventData;

    public SmsEvent(SmsEventType eventType, String eventMessage, Object eventData) {
        this.eventType = eventType;
        this.eventMessage = eventMessage;
        this.eventData = eventData;
    }

    public SmsEventType getEventType() {
        return eventType;
    }

    public String getEventMessage() {
        return eventMessage;
    }

    public Object getEventData() {
        return eventData;
    }

    public void setEventMessage(String eventMessage) {
        this.eventMessage = eventMessage;
    }

    public void setEventData(Object eventData) {
        this.eventData = eventData;
    }

}
