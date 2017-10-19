package SMSServiceServer.SmsService.utils;

import SMSServiceServer.SmsService.model.PDU;

import javax.xml.bind.DatatypeConverter;

/**
 * Created by fim on 11.07.17.
 */
public class HEXSmsData {

    public static PDU getPDUMetaInfo(PDU pdu)
    {
        String PDUString = pdu.getHEX();
        String out = "";
        int start = 0;

        int SMSC_lengthInfo = HexToNum(PDUString.substring(0,2));
        String SMSC_info = PDUString.substring(2,2+(SMSC_lengthInfo*2));
        String SMSC_TypeOfAddress = SMSC_info.substring(0,2);
        String SMSC_Number = SMSC_info.substring(2,(SMSC_lengthInfo*2)-2);

        if (SMSC_lengthInfo != 0)
        {
            SMSC_Number = semiOctetToString(SMSC_Number);

            // if the length is odd remove the trailing  F
            if((SMSC_Number.substring(SMSC_Number.length()-1,SMSC_Number.length()).equals('F')) || (SMSC_Number.substring(SMSC_Number.length()-1,SMSC_Number.length()).equals('f')))
            {
                SMSC_Number = SMSC_Number.substring(0,SMSC_Number.length()-1);
            }
            if (SMSC_TypeOfAddress.equals("91"))
            {
                SMSC_Number = "+" + SMSC_Number;
            }
        }

        int start_SMSDeleivery = (SMSC_lengthInfo*2)+2;

        start = start_SMSDeleivery;
        String firstOctet_SMSDeliver = PDUString.substring(start,start + 2);
        start = start + 2;

        if ((HexToNum(firstOctet_SMSDeliver) & 0x03) == 1) // Transmit Message
        {
            int MessageReference = HexToNum(PDUString.substring(start,start+2));
            start = start + 2;

            // length in decimals
            int sender_addressLength = HexToNum(PDUString.substring(start,start+2));
            if(sender_addressLength%2 != 0)
            {
                sender_addressLength +=1;
            }
            start = start + 2;

            String sender_typeOfAddress = PDUString.substring(start,start+2);
            start = start + 2;

            String sender_number = semiOctetToString(PDUString.substring(start,start+sender_addressLength));

            if((sender_number.substring(sender_number.length()-1,sender_number.length()).equals('F')) || (sender_number.substring(sender_number.length()-1,sender_number.length()).equals('f')))
            {
                sender_number =	sender_number.substring(0,sender_number.length()-1);
            }
            if (sender_typeOfAddress.equals("91"))
            {
                sender_number = "+" + sender_number;
            }
            start +=sender_addressLength;

            String tp_PID = PDUString.substring(start,start+2);
            start +=2;

            String tp_DCS = PDUString.substring(start,start+2);
            String tp_DCS_desc = tpDCSMeaning(tp_DCS);
            start +=2;

            int ValidityPeriod = HexToNum(PDUString.substring(start,start+2));
            start +=2;

// Commonish...
            int messageLength = HexToNum(PDUString.substring(start,start+2));

            start += 2;

            int bitSize = DCS_Bits(tp_DCS);
            String userData = "Undefined format";
            if (bitSize==7)
            {
                userData = getUserMessage(PDUString.substring(start,PDUString.length()),messageLength);
            }
            else if (bitSize==8)
            {
                userData = getUserMessage8(PDUString.substring(start,PDUString.length()),messageLength);
            }
            else if (bitSize==16)
            {
                userData = getUserMessage16(PDUString.substring(start,start + PDUString.length()),messageLength);
            }

            userData = userData.substring(0,messageLength);
            if (bitSize==16)
            {
                messageLength/=2;
            }

            out =  "SMSC#"+SMSC_Number+"\nSender:"+sender_number+"\nTP_PID:"+tp_PID+"\nTP_DCS:"+tp_DCS+"\nTP_DCS-popis:"+tp_DCS_desc+"\n"+userData+"\nLength:"+messageLength;
            pdu.setSmsc(SMSC_Number);
            pdu.setSender(sender_number);
            pdu.setTP_PID(tp_PID);
            pdu.setTP_DSC(tp_DCS);
            pdu.setTP_DSC_DESC(tp_DCS_desc);
            pdu.setUserData(userData);
        }
        else // Receive Message
            if ((HexToNum(firstOctet_SMSDeliver) & 0x03) == 0) // Receive Message
            {
                // length in decimals
                int sender_addressLength = HexToNum(PDUString.substring(start,start+ 2));
                if(sender_addressLength%2 != 0)
                {
                    sender_addressLength +=1;
                }
                start = start + 2;

                String sender_typeOfAddress = PDUString.substring(start,start + 2);
                start = start + 2;

                String sender_number = semiOctetToString(PDUString.substring(start,start+sender_addressLength));

                if((sender_number.substring(sender_number.length()-1,sender_number.length()).equals("F")) || (sender_number.substring(sender_number.length()-1,sender_number.length()).equals("f") ))
                {
                    sender_number =	sender_number.substring(0,sender_number.length()-1);
                }
                if (sender_typeOfAddress.equals("91"))
                {
                    sender_number = "+" + sender_number;
                }
                start +=sender_addressLength;

                String tp_PID = PDUString.substring(start,start + 2);
                start +=2;

                String tp_DCS = PDUString.substring(start,start + 2);
                String tp_DCS_desc = tpDCSMeaning(tp_DCS);
                start +=2;

                String timeStamp = semiOctetToString(PDUString.substring(start,start + 14));

                // get date
                String year = timeStamp.substring(0,2);
                String month = timeStamp.substring(2,4);
                String day = timeStamp.substring(4,6);
                String hours = timeStamp.substring(6,8);
                String minutes = timeStamp.substring(8,10);
                String seconds = timeStamp.substring(10,12);

                timeStamp = day + "/" + month + "/" + year + " " + hours + ":" + minutes + ":" + seconds;
                start +=14;

// Commonish...
                int messageLength = HexToNum(PDUString.substring(start,start + 2));
                start += 2;

                int bitSize = DCS_Bits(tp_DCS);
                String userData = "Undefined format";
                if (bitSize==7)
                {
                    userData = getUserMessage(PDUString.substring(start,PDUString.length()),messageLength);
                }
                else if (bitSize==8)
                {
                    userData = getUserMessage8(PDUString.substring(start,PDUString.length()),messageLength);
                }
                else if (bitSize==16)
                {
                    messageLength = messageLength/2;
                    userData = getUserMessage16(PDUString.substring(start,PDUString.length()),messageLength);
                }

                userData = userData.substring(0,messageLength);

                if (bitSize==16)
                {
                    messageLength/=2;
                }

                out =  "SMSC#"+SMSC_Number+"\nSender:"+sender_number+"\nTimeStamp:"+timeStamp+"\nTP_PID:"+tp_PID+"\nTP_DCS:"+tp_DCS+"\nTP_DCS_desc:"+tp_DCS_desc+"\n"+userData+"\nLength:"+messageLength;
                pdu.setSmsc(SMSC_Number);
                pdu.setSender(sender_number);
                pdu.setTimeStamp(timeStamp);
                pdu.setTP_PID(tp_PID);
                pdu.setTP_DSC(tp_DCS);
                pdu.setTP_DSC_DESC(tp_DCS_desc);
                pdu.setUserData(userData);
            }
            else
            {
                 return pdu;

            }
        //System.out.println(out);
        return pdu;
    }


    private static String getUserMessage16(String substring, int messageLength) {
        substring = SMSDecoder.hextoString16(substring);
        return substring;
    }

    private static String getUserMessage8(String substring, int messageLength) {
        substring = SMSDecoder.hextoString8(substring);
        return substring;
    }

    private static String getUserMessage(String substring,int truelength) {
            substring = message7(HEXSmsData.hexStringToByteArray(substring));
            return substring;
    }

    // function to convert semioctets to a string
    private static String semiOctetToString(String inp) //sp
    {
        String out = "";
        for(int i=0;i<inp.length();i=i+2)
        {
            String temp = inp.substring(i,i+2);
            out = out + temp.charAt(1) + temp.charAt(0);
        }
        return out;
    }

    private static String tpDCSMeaning(String tp_DCS)
    {
        String tp_DCS_desc=tp_DCS;
        int pomDCS = HexToNum(tp_DCS);
        switch(pomDCS & 192)
        {
            case 0: if((pomDCS & 3) > 0)
            {
                tp_DCS_desc="Compressed Text";
            }
            else
            {
                tp_DCS_desc="Uncompressed Text";
            }
                if((pomDCS & 16)>0)
                {
                    tp_DCS_desc+="No class";
                }
                else
                {
                    tp_DCS_desc+="class:";

                    switch(pomDCS & 3)
                    {
                        case 0:
                            tp_DCS_desc+="0";
                            break;
                        case 1:
                            tp_DCS_desc+="1";
                            break;
                        case 2:
                            tp_DCS_desc+="2";
                            break;
                        case 3:
                            tp_DCS_desc+="3";
                            break;
                    }
                }
                tp_DCS_desc+="Alphabet:";
                switch(pomDCS & 12)
                {
                    case 0:
                        tp_DCS_desc+="Default";
                        break;
                    case 4:
                        tp_DCS_desc+="8bit";
                        break;
                    case 8:
                        tp_DCS_desc+="UCS2(16)bit";
                        break;
                    case 12:
                        tp_DCS_desc+="Reserved";
                        break;
                }
                break;
            case 64:
            case 128:
                tp_DCS_desc ="Reserved coding group";
                break;
            case 192:
                switch(pomDCS & 0x30)
                {
                    case 0:
                        tp_DCS_desc ="Message waiting group";
                        tp_DCS_desc+="Discard";
                        break;
                    case 0x10:
                        tp_DCS_desc ="Message waiting group";
                        tp_DCS_desc+="Store Message. Default Alphabet";
                        break;
                    case 0x20:
                        tp_DCS_desc ="Message waiting group";
                        tp_DCS_desc+="Store Message. UCS2 Alphabet";
                        break;
                    case 0x30:
                        tp_DCS_desc ="Data coding message class";
                        if ((pomDCS & 0x4)>0)
                        {
                            tp_DCS_desc+="Default Alphabet";
                        }
                        else
                        {
                            tp_DCS_desc+="8 bit Alphabet";
                        }
                        break;
                }
                break;

        }

        //alert(tp_DCS.valueOf());

        return(tp_DCS_desc);
    }

    private static byte DCS_Bits(String tp_DCS)
    {
        byte AlphabetSize=7; // Set Default
//alert(tp_DCS);
        byte pomDCS = (byte) HexToNum(tp_DCS);
//alert(pomDCS);
        switch(pomDCS & 192)
        {
            case 0: if((pomDCS & 32)>0)
            {
                // tp_DCS_desc="Compressed Text\n";
            }
            else
            {
                // tp_DCS_desc="Uncompressed Text\n";
            }
                switch(pomDCS & 12)
                {
                    case 4:
                        AlphabetSize=8;
                        break;
                    case 8:
                        AlphabetSize=16;
                        break;
                }
                break;
            case 192:
                switch(pomDCS & 0x30)
                {
                    case 0x20:
                        AlphabetSize=16;
                        break;
                    case 0x30:
                        if ((pomDCS & 0x4)>0)
                        {
                            //;
                        }
                        else
                        {
                            AlphabetSize=8;
                        }
                        break;
                }
                break;

        }

        return(AlphabetSize);
    }


    private static int HexToNum(String hex){
        return Integer.parseInt(hex, 16);
    }

    private static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }

    public static short[] hexStringToByteArray(String s) {
        int len = s.length();
        short[] data = new short[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (short) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String message7(short[] data ){
        StringBuilder s = new StringBuilder();
        short p1,p2,c = 0;
        int i = 0;
        for (int j=0;j< data.length; j++){
            i = j%7;

            p1 = (short) (((((0xFF >> (i+1)) & data[j]) << i))& 0xFF);
            p2 = 0;
            if((i>0) ) {

                p2 = (short) ((data[j - 1] >> (8 - i)) & 0xFF);

            }

            c =  (short)((p1 + p2)& 0xFF);

            //System.out.println("char:" + c);
            s.append(Alphabet.get(c));

            if (i == 6 & j>0){
                c = (short) ((data[j] >> (1)) & 0xFF);

                //System.out.println("char:" + c);
                s.append(Alphabet.get(c));
            }
        }
        return s.toString();
    }
}
