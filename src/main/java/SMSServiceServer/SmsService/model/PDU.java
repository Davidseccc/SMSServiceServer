package SMSServiceServer.SmsService.model;

import SMSServiceServer.SmsService.utils.PDUDecoder;

/**
 * Created by fim on 13.07.17.
 */

public class PDU {
    String HEX;
    String smsc;
    String sender;
    String timeStamp;
    String TP_PID;
    String TP_DSC;
    String TP_DSC_DESC;
    String copression;
    String cls;
    String Alphabet;
    String userData;

    public PDU(String HEX) {
        this.HEX = HEX;
        PDUDecoder.decode(this);
    }

    public String getHEX() {
        return HEX;
    }

    public String getSmsc() {
        return smsc;
    }

    public void setSmsc(String smsc) {
        this.smsc = smsc;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTP_PID() {
        return TP_PID;
    }

    public void setTP_PID(String TP_PID) {
        this.TP_PID = TP_PID;
    }

    public String getTP_DSC() {
        return TP_DSC;
    }

    public void setTP_DSC(String TP_DSC) {
        this.TP_DSC = TP_DSC;
    }

    public String getCopression() {
        return copression;
    }

    public void setCopression(String copression) {
        this.copression = copression;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public String getAlphabet() {
        return Alphabet;
    }

    public void setAlphabet(String alphabet) {
        Alphabet = alphabet;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserData(String message) {
        this.userData = message;
    }

    public String getTP_DSC_DESC() {
        return TP_DSC_DESC;
    }

    public void setTP_DSC_DESC(String TP_DSC_DESC) {
        this.TP_DSC_DESC = TP_DSC_DESC;
    }

    @Override
    public String toString() {
        return "PDU{" +
                "HEX='" + HEX + '\'' +
                ", smsc='" + smsc + '\'' +
                ", sender='" + sender + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", TP_PID='" + TP_PID + '\'' +
                ", TP_DSC='" + TP_DSC + '\'' +
                ", TP_DSC_DESC='" + TP_DSC_DESC + '\'' +
                ", copression='" + copression + '\'' +
                ", cls='" + cls + '\'' +
                ", Alphabet='" + Alphabet + '\'' +
                ", userData='" + userData + '\'' +
                '}';
    }
}
