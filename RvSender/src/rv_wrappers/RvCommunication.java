package rv_wrappers;

import com.logica.eai.test.bw.BWConstants;
import com.logica.eai.test.bw.IntegrationRuntimeException;
import com.tibco.tibrv.*;
import gui.MainFrame;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.*;
import java.io.UnsupportedEncodingException;


/**
 * Created by JLyc on 9. 4. 2015.
 */
public class RvCommunication implements TibrvMsgCallback, Runnable {
    private static final Log LOG = LogFactory.getLog(RvCommunication.class);

    private static RvCommunication rvCommunication;

    public static RvCommunication getInstance() {
        if (rvCommunication == null) rvCommunication = new RvCommunication();
        return rvCommunication;
    }

    // RV fields
    private TibrvRvdTransport rvdTransport = null;
    private TibrvQueue rvQueue = null;
    private TibrvListener tibrvListener = null;

    public boolean isListening() {
        return isListening;
    }

    private boolean isListening = true;

    public void init() {
        MainFrame.getStatus().append("\nInitializing RV connection...");
        try {
            Tibrv.open(Tibrv.IMPL_NATIVE);
            String service = null;
            String network = null;
            String deamon = null;
            if(MainFrame.getRvService().getText().length()>0){
                service = MainFrame.getRvService().getText();
            }
            if(MainFrame.getRvNetwork().getText().length()>0){
                network = MainFrame.getRvNetwork().getText();
            }
            if(MainFrame.getRvService().getText().length()>0){
                deamon = MainFrame.getRvDeamon().getText();
            }
            System.out.println("rvdTransport");
            rvdTransport = new TibrvRvdTransport(service,
                   network, deamon);
            System.out.println("rvdTransport");

            if (rvdTransport == null) {
                MainFrame.getStatus().append("RV transport is not initialized!");
            }
            rvQueue = new TibrvQueue();
            System.out.println(MainFrame.getRvSubject().getText());
            tibrvListener = new TibrvListener(rvQueue, this, rvdTransport, MainFrame.getRvSubject().getText(),null);
            MainFrame.getStatus().append("\nRV Listening on subject: " + MainFrame.getRvSubject().getText());
        } catch (TibrvException e) {
            MainFrame.getStatus().append("\nRV failed to initialize: "+ e);
            MainFrame.getConnect().doClick();
        }
    }

    public void close() {
        try {
            if(tibrvListener==null ||rvdTransport == null)
            {
                MainFrame.getStatus().append("\nCould not close connection! No connection crated ");
                return;
            }
            tibrvListener.destroy();
            rvdTransport.destroy();
            Tibrv.close();
        } catch (TibrvException e) {
            MainFrame.getStatus().append("\nCould not close connection! "+ e.getMessage());
        }finally {
            stopListening();
        }
    }

    public void stopListening() {
        isListening=false;
    }

    public void sendMsg(String xmlMsg){
        try {
            if(rvdTransport==null) {
                MainFrame.getStatus().append("No Rvd Transport crated\nMsg not send");
                isListening=false;
                return;
            }
                rvdTransport.send(new RVMessageInterfaceWraper(xmlMsg));

        } catch (TibrvException e) {
            MainFrame.getStatus().append("sendMsg error "+ e.getMessage());
        }
    }

    public void sendMsgNew(String xmlMsg){
            TibrvMsg message = new TibrvMsg();
            try
            {
                message.add(
                        BWConstants.RV_CONTENT_TYPE,
                        new TibrvXml(
                                xmlMsg.trim().getBytes("UTF-8")
                        )
                );
                message.setSendSubject(MainFrame.getRvSubject().getText());
                rvdTransport.send(message);

            }
            catch (UnsupportedEncodingException ueExc)
            {
                throw new IntegrationRuntimeException(ueExc);
            } catch (TibrvException e) {
                e.printStackTrace();
            }
    }


    public void sendReply(RVMessageInterfaceWraper responseMsg, RVMessageInterfaceWraper sourceMsg) {
        try {
            if(rvdTransport==null) {
                MainFrame.getStatus().append("No Rvd Transport crated\nMsg not send");
                MainFrame.getStatus().setBackground(Color.RED);
                return;
            }
                rvdTransport.sendReply(responseMsg.getMessage(), sourceMsg.getMessage());
        } catch (TibrvException e) {
            MainFrame.getStatus().append("Failed to send response" + responseMsg.getText()+ e.getMessage());
        }
    }

    public void run() {
        isListening=true;
        MainFrame.getStatus().append("\nRV listener started");
        while(isListening){
            try {
                if(rvQueue==null){
                    MainFrame.getStatus().append("\nRV listener stopped. Listening queue not created");
                    MainFrame.getListener().doClick();
                    return;
                }
                rvQueue.dispatch();
//                            timed dispatch should make time to listen ?
//                            rvQueue.timedDispatch();
            } catch (TibrvException | InterruptedException e) {
                MainFrame.getStatus().append("\nException at rv listening thread fail to dispatch "+e);
            }
        }
        MainFrame.getStatus().append("\nRV listener stopped");
    }

    @Override
    public void onMsg(TibrvListener tibrvListener, TibrvMsg tibrvMsg) {
        MainFrame.getRvMsg().setText(tibrvMsg.toString());
        MainFrame.newMsg(new RVMessageInterfaceWraper(tibrvMsg));
    }
}
