package SMSServiceServer.REST;

import SMSServiceServer.SmsService.model.PDU;
import SMSServiceServer.SmsService.utils.PDUtoJSON;
import SMSServiceServer.SmsService.utils.RXTX_Factory;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/SMS")
public class SMSServlet {
    RXTX_Factory factory = new RXTX_Factory("/dev/ttyUSB0");;



    @GET
    @Path("get/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getSMS(@PathParam("id")int id) {
        factory.openPort();
        List<PDU> pdu =  factory.getSMS(id);
        factory.closePort();

        if(pdu != null){
            String out = PDUtoJSON.pduListToJson(pdu);
            return Response.ok().header("Content-Type", "application/json;charset=UTF-8").entity(out).build();
        }
        return Response.status(200).entity("No messages").build();
    }

    @GET
    @Path("getAll")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getAllSMS() {
        factory.openPort();
        List<PDU> pdu = factory.getAllSMS();
        factory.closePort();

        if(pdu != null){
            System.out.print("PDU: size()"+ pdu.size());
            String out = PDUtoJSON.pduListToJson(pdu);
            return Response.ok().header("Content-Type", "application/json;charset=UTF-8").entity(out).build();
        }
        return Response.ok().entity("No messages").build();
    }

    @GET
    @Path("signal")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getSignalStrength() {
        factory.openPort();
        String out = factory.getSignalStrength();
        factory.closePort();
        String strength = strengthToString(Integer.parseInt(out.split(",")[0]));
        JSONObject obj = new JSONObject();
        obj.put("CSQ", out);
        obj.put("strenght", strength);

        //obj.put("", );
        //SmsEvent e = new SmsEvent(SmsEvent.SmsEventType.MESSAGE_SEND,"SEND_SMS", "MESSAGE");
        return Response.status(201).entity(obj.toJSONString()).build();
    }

    @POST
    @Path("send")
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
    public Response send(@FormParam("number") String  number, @FormParam("message") String  message) {
        String result;
        factory.openPort();
        if(factory.sendSMS(number, message)){
            result = "SMS Sucesfully send to "+ number;
            factory.closePort();
            result = JSONObject.toString("message",result);
            return Response.status(201).entity(result).build();
        }
        else{
            result = "SMS SEND FAILED";
            factory.closePort();
            result = JSONObject.toString("message", result);
            return Response.status(400).entity(result).build();
        }
    }

    public static String strengthToString(int strenght){
        String out = "";
                if(strenght <= 9){
                    out="Marginal";
                }
                if(strenght > 9 && strenght <= 14 ){
                out="OK";
                }
                if(strenght > 14 && strenght <= 19 ){
                out="Good";
                }
            if(strenght > 19 ){
                out="Excellent";
            }
            return out;
    }

}