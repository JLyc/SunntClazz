package rv_wrappers;

import com.logica.eai.test.bw.BWConstants;
import com.logica.eai.test.bw.IntegrationRuntimeException;
import com.tibco.tibrv.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.*;
import java.io.UnsupportedEncodingException;


/**
 * Created by JLyc on 9. 4. 2015.
 */
public class RvCommunication implements TibrvMsgCallback, Runnable {
    private static final Log LOG = LogFactory.getLog(RvCommunication.class);

    // RV fields
    private TibrvRvdTransport rvdTransport = null;
    private TibrvQueue rvQueue = null;
    private TibrvListener tibrvListener = null;
    private static RvCommunication rvCommunication;

    // RV properties
    private String rvName = "default";
    private String rvDeamon = null;
    private String rvNetwork = null;
    private String rvService = null;
    private String rvQueueName = null;

    public RvCommunication(String rvName, String rvDeamon, String rvNetwork, String rvService, String rvQueueName) {
        this.rvName = rvName;
        this.rvDeamon = rvDeamon;
        this.rvNetwork = rvNetwork;
        this.rvService = rvService;
        this.rvQueueName = rvQueueName;
    }

    public boolean isListening() {
        return isListening;
    }

    private boolean isListening = true;

    public void open() {
        LOG.info("\nInitializing RV connection...");
        try {
            Tibrv.open(Tibrv.IMPL_NATIVE);
            rvdTransport = new TibrvRvdTransport(rvService,
                   rvNetwork, rvDeamon);
            if (rvdTransport == null) {
                LOG.info("RV transport is not initialized!");
            }
            rvQueue = new TibrvQueue();
            tibrvListener = new TibrvListener(rvQueue, this, rvdTransport, rvQueueName, null);
            LOG.info("\nRV Listening on subject: " + rvQueueName);
        } catch (TibrvException e) {
            LOG.info("\nRV failed to initialize: "+ e);
        }
    }

    public void close() {
        try {
            if(tibrvListener==null ||rvdTransport == null)
            {
                LOG.info("\nCould not close connection! No connection crated ");
                return;
            }
            tibrvListener.destroy();
            rvdTransport.destroy();
            Tibrv.close();
        } catch (TibrvException e) {
            LOG.info("\nCould not close connection! "+ e.getMessage());
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
                LOG.info("No Rvd Transport crated\nMsg not send");
                isListening=false;
                return;
            }
                rvdTransport.send(new RVMessageInterfaceWraper(xmlMsg));

        } catch (TibrvException e) {
            LOG.info("sendMsg error "+ e.getMessage());
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
                message.setSendSubject(rvQueueName);
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
                LOG.info("No Rvd Transport crated\nMsg not send");
                return;
            }
                rvdTransport.sendReply(responseMsg.getMessage(), sourceMsg.getMessage());
        } catch (TibrvException e) {
            LOG.info("Failed to send response" + responseMsg.getText()+ e.getMessage());
        }
    }

    public void run() {
        isListening=true;
        LOG.info("\nRV listener started");
        while(isListening){
            try {
                if(rvQueue==null){
                    LOG.info("\nRV listener stopped. Listening queue not created");
                    return;
                }
                rvQueue.dispatch();
//                            timed dispatch should make time to listen ?
//                            rvQueue.timedDispatch();
            } catch (TibrvException | InterruptedException e) {
                LOG.info("\nException at rv listening thread fail to dispatch "+e);
            }
        }
        LOG.info("\nRV listener stopped");
    }

    @Override
    public void onMsg(TibrvListener tibrvListener, TibrvMsg tibrvMsg) {
//        MainFrame.getRvMsg().setText(tibrvMsg.toString());
//        MainFrame.newMsg(new RVMessageInterfaceWraper(tibrvMsg));
    }

    public String getName() {
        return rvName;
    }
}
