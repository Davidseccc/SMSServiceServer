package SMSServiceServer.SmsService.utils;

/**
 * Created by fim on 15.05.17.
 */
public class PhoneNumberValidator {

    public static String[] validateNumbers(String[] numbers){
        String[] validNumbers = new String[numbers.length];
        for (int i=0; i< numbers.length; i++) {
            if (validatePhoneNumber(numbers[i])) {
                validNumbers[i] = removeSpaces(numbers[i]);
            }
        }
        return validNumbers;
    }

    private static boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber.matches("\\d{12}")) {
            return true;
        }
        if (phoneNumber.matches("[+]\\d{12}")) {
            return true;
        }
        else if(phoneNumber.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{3}")) {
            return true;
        }
        else if(phoneNumber.matches("[+]\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{3}")) { //00
            return true;
        }
        else if(phoneNumber.matches("\\d{3}[-\\.\\s]\\d{9}")) {
            return true;
        }
        else if(phoneNumber.matches("[+]\\d{3}[-\\.\\s]\\d{9}")) {
            return true;
        }
        else {
            System.out.println(phoneNumber + " could not be validated.");
            return false;
        }
    }
    private static String removeSpaces(String number) {
        number = number.replaceAll("[\\-\\+\\.\\^:,]", "");
        number = number.replaceAll(" ", "");
        return number;
    }
}
