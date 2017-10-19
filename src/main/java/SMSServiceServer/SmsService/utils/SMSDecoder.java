package SMSServiceServer.SmsService.utils;

/**
 *
 * This class is used to cenvert incoming SMS from
 * HEX format into a human readable string with UTF-8 chars.
 *
 * Created by fim on 11.05.17.
 */
public class SMSDecoder {

    private String input;
    private String output;

    public SMSDecoder(String input) {
        this.input = input;
    }

    public static String hextoString16(String hex) {
        while (hex.length()%4 != 0){
            hex = hex.substring(1);
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        for( int i=0; i<hex.length()-1; i+=4 ){
            String output = hex.substring(i, (i + 4));
            int decimal = Integer.parseInt(output, 16);
            sb.append((char)decimal);
            temp.append(decimal);
        }
        return sb.toString();
    }

    public static String hextoString8(String hex){

        String ascii="";
        String str;

        // Convert hex string to "even" length
        int rmd,length;
        length=hex.length();
        rmd =length % 2;
        if(rmd==1)
            hex = "0"+hex;

        // split into two characters
        for( int i=0; i<hex.length()-1; i+=2 ){

            //split the hex into pairs
            String pair = hex.substring(i, (i + 2));
            //convert hex to decimal
            int dec = Integer.parseInt(pair, 16);
            str=CheckCode(dec);
            ascii=ascii+str;
        }
        return ascii;
    }

    public static String CheckCode(int dec){
        String str;

        //convert the decimal to character
        str = Character.toString((char) dec);

        if(dec<32 || dec>126 && dec<161)
            str="n/a";
        return str;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
