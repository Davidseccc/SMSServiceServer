package SMSServiceServer.SmsService;

import SMSServiceServer.SmsService.iface.SmsEventListener;
import SMSServiceServer.SmsService.utils.MessageStrings;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.net.URL;

/**
 * Created by Honza on 12. 4. 2017.
 */
public class Core {

    XMLConfiguration xml;
    XMLConfiguration localeXml;
    SmsService smsService;
    ClassLoader classloader;

    /**
     * Creates new Core instance. Constructor makes all surrounding modules instances and prepares all routes
     */
    public Core() {
        try {
            classloader = Thread.currentThread().getContextClassLoader();
            URL cfg = classloader.getResource("config.xml");
            Configurations config = new Configurations();
            xml = config.xml(cfg);
            System.out.println("Config ready");
            createInstances();
            createListeners();

        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prepares all necessary instances for modules
     */
    private void createInstances() {

        System.out.println("Creating instances");
        String port = xml.getString("modem.comport");
        String[] numbers = xml.getStringArray("nummers.number");
        SmsService smsService = new SmsService(port);
        smsService.setNumbers(numbers);
        smsService.setDevicePort(port);
        String stringLocale = xml.getString("language.localeFile");

        if (classloader != null) {
            classloader = Thread.currentThread().getContextClassLoader();
            URL messageStrings = classloader.getResource(stringLocale);
            Configurations config = new Configurations();
            try {
                localeXml = config.xml(messageStrings);
            } catch (ConfigurationException e) {
                e.printStackTrace();
            }

            MessageStrings.strings.put("MESSAGE_SEND", localeXml.getString("sms-message.received"));
            MessageStrings.strings.put("MESSAGE_RECEIVED", localeXml.getString("sms-message.send"));

            SmsService event = new SmsService(port);
            //SmsEvent e = new SmsEvent(SmsEvent.SmsEventType.OPEN_PORT,"", "");
            //event.propagateEvent(e);

            //event.propagateEvent(e);
            //e = new SmsEvent(SmsEvent.SmsEventType.CLOSE_PORT,"", "");
            //event.propagateEvent(e);

            String message7 =   "0791246080006518040ED0C6E0B0287C3E9700007170708030800024D037DD2ED7BFEDE1711AB47E934170F91B640C8FCBE2F77BAD03D972B31B0E06";
            String message8 =   "07911356131313F311000A9260214365870004AA1F41686F6A2C20746F746F206A6520746573746F76616369207A70726176612E";
            String message16  =   "0791246080006518040C91246070229307000871500181640080480050006F006B00750073006E00E10020007A0070007200E100760061002000730020010D00650073006B006F00750020006400690061006B0072006900740069006B006F0075002E";
            //System.out.println(HEXSmsData.getPDUMetaInfo(message7));
            //System.out.println(HEXSmsData.getPDUMetaInfo(message8));
            //System.out.println(HEXSmsData.message7(HEXSmsData.hexStringToByteArray("D037DD2ED7BFEDE1711AB47E934170F91B640C8FCBE2F77BAD03D972B31B0E06")));
        }
    }

    /**
     * Prepares all listeners for instantiated modules
     */
    private void createListeners() {
        if (smsService != null) {
            smsService.addEventListener(new SmsEventListener() {
                @Override
                public void onSmsEvent(SmsEvent e) {
                    if (e.getEventType() == SmsEvent.SmsEventType.MESSAGE_ARRIVED) {
                        //todo: DO something.
                    }
                    if (e.getEventType() == SmsEvent.SmsEventType.CREDIT_LOW) {
                        //todo: DO something else.
                    }
                    if (e.getEventType() == SmsEvent.SmsEventType.NO_SIGNAL) {
                        //todo: DO something else.
                    }
                    if (e.getEventType() == SmsEvent.SmsEventType.SEND_FAILED) {
                        //todo: DO something else.
                    }
                }

                @Override
                public void onMessageReceived(SmsEvent e) {

                }

                @Override
                public void onMessageSent(SmsEvent e) {

                }
            });

        }


    }

}
