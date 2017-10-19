package SMSServiceServer.SmsService.utils;

import SMSServiceServer.SmsService.ComPortSendReceive;
import SMSServiceServer.SmsService.model.PDU;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by fim on 11.07.17.
 */
public class RXTX_Factory {

    private String port;
    ComPortSendReceive comPortSendReceive;
    List<PDU> AllSMS;
    long lastUpdate = 0;
    long updateTime = 1*60*1000; //every minute

    public RXTX_Factory(String port) {
        this.port = port;
        comPortSendReceive = ComPortSendReceive.getInstance();
        comPortSendReceive.setPort(port);
    }

    public boolean openPort(){
        return comPortSendReceive.open(port);
    }
    public boolean closePort(){
        return comPortSendReceive.close();
    }

    public List<PDU> getSMS(int index){
        List<PDU> list = new ArrayList<PDU>();
        String out = comPortSendReceive.writeAndRead("AT+CMGF=0");
        System.out.println(out);
        out = comPortSendReceive.writeAndRead("AT+CMGL="+ index);
        //System.out.println(out);
        if(validateResult(out)) {
            String[] messages = out.split("\n");
            if (messages.length > 2) {
                messages = Arrays.copyOfRange(messages, 1, messages.length - 2);
                int c = 0;
                for (int i = 1; i < messages.length; i += 2) {
                    PDU p = new PDU(messages[i].trim());
                    list.add(p);
                    c++;
                }
                return list;
            }
        }
        return null;
    }

    public List<PDU> getAllSMS(){
        long now = System.currentTimeMillis();
        if(lastUpdate + updateTime > now){   //Use cache instead...
            return AllSMS;
        }
        List<PDU> list = new ArrayList<PDU>();
        String out = comPortSendReceive.writeAndRead("AT+CMGF=0"); //Switch to BINARY mode
        System.out.println(out);
        out = comPortSendReceive.writeAndRead("AT+CMGL=4");  // AT+CMGL="ALL"
        if(validateResult(out)) {
            String[] messages = out.split("\n");
            if (messages.length > 2) {
                messages = Arrays.copyOfRange(messages, 1, messages.length - 2);
                int c = 0;
                for (int i = 1; i < messages.length; i += 2) {
                    PDU p = new PDU(messages[i].trim());
                    list.add(p);
                    c++;
                }
                AllSMS = list;                              //update cache;
                lastUpdate = System.currentTimeMillis();
                return list;
            }
        }
        return null;
    }

    public boolean sendSMS(String number, String message){
        String out = comPortSendReceive.writeAndRead("AT+CMGF=1");
        System.out.println(out);
        out = comPortSendReceive.writeAndRead("AT+CMGS="+"\""+ number +"\"");
        System.out.println(out);
        out = comPortSendReceive.writeAndRead(message);
        System.out.println(out);
        out = comPortSendReceive.writeAndRead("\u001A");
        System.out.println(out);
        return true;
    }

    public String getSignalStrength(){
        String out = comPortSendReceive.writeAndRead("AT+CSQ");
        //+CSQ: 24,99
        System.out.println(out);
        if(validateResult(out)){
            out = out.trim().substring(15,20);
            return out;
        }
        return "0,0";

    }

    private boolean validateResult(String out) {
        out = out.trim();
        if(out.substring(out.length()-2, out.length()).equals("OK")){
            return true;
        }
        else{
            System.out.println("Could not found \"OK\" in:" + out );
            return false;
        }
    }

}
