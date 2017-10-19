package SMSServiceServer.SmsService.utils;

import SMSServiceServer.SmsService.model.PDU;

/**
 * Created by fim on 11.07.17.
 */
public class PDUDecoder {
    PDU pdu;

    public static String decode(PDU pdu){
         HEXSmsData.getPDUMetaInfo(pdu);
        return null;
    }

}
