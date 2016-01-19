package gui;

import com.tibco.tibrv.TibrvException;
import rv_wrappers.RVMessageInterfaceWraper;
import rv_wrappers.RvCommunication;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by JLyc.Development@gmail.com on 1/19/2016.
 */
public class MainFrame extends JFrame implements ActionListener {
    private static final String NEW_MSG = "\n******* New MSG ********\n";

    private static JTextField rvNetwork;
    private static JTextField rvDeamon;
    private static JTextField rvService;
    private static JTextField rvSubject;
    private static JTextArea rvMsg;
    private static JTextField isConnected;
    private static JCheckBox replyCheckBox = new JCheckBox("Auto reply with msg", false);
    private static JButton connect  = new JButton("Connect");
    private static JTextArea status = new JTextArea("Connect to rv deamon");
    private static JTextArea replyMsg = new JTextArea("<reply_msg>\n\n" +
            "</reply_msg>");
    DefaultCaret caret = (DefaultCaret) status.getCaret();

    private JButton sendMsg;
    private static JTextArea receivedMsg = new JTextArea(NEW_MSG);
    private static JButton listener = new JButton("Start Listener");

    public static JButton getListener() {
        return listener;
    }

    public static JButton getConnect(){
        return connect;
    }


    private JTextArea listenerText;

    public static JTextField getRvNetwork() {
        return rvNetwork;
    }

    public static JTextField getRvDeamon() {
        return rvDeamon;
    }

    public static JTextField getRvService() {
        return rvService;
    }

    public static JTextField getRvSubject() {
        return rvSubject;
    }

    public static JTextArea getRvMsg() {
        return rvMsg;
    }

    public static JTextArea getStatus() {
        return status;
    }

    public MainFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setName("RvTool");
        this.setMinimumSize(new Dimension(500, 400));
        this.getRootPane().setContentPane(mainPanel());


        this.setVisible(true);
    }

    private Container mainPanel() {

        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(11, 2));

        JLabel rvDeamonText = new JLabel("RvDeamon");
        rvDeamon = new JTextField("7500");
        JLabel rvNetworkText = new JLabel("RvNetwork");
        rvNetwork = new JTextField("");
        JLabel rvServiceText = new JLabel("RvService");
        rvService = new JTextField("tcp:7500");
        JLabel rvSubjectText = new JLabel("RvSubject");
        rvSubject = new JTextField("TMSK.CRM.MSG");
        JLabel rvMsgText = new JLabel("RvMsg");
        rvMsg = new JTextArea("<empty_msg>\n\n" +
                "</empty_msg>");

        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.getViewport().add(rvMsg);
        JScrollPane jScrollPaneStatus = new JScrollPane();
        jScrollPaneStatus.getViewport().add(status);
        DefaultCaret caret = (DefaultCaret) status.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        isConnected = new JTextField("Not Connected");
        isConnected.setEditable(false);
        connect.addActionListener(this);


//        status.setEditable(false);
        sendMsg = new JButton("Send");
        sendMsg.setEnabled(false);
        sendMsg.addActionListener(this);

        listenerText = new JTextArea("Not Running");
        listenerText.setEditable(false);
        listener.setEnabled(false);
        listener.addActionListener(this);
        JLabel received = new JLabel("Received msg");
        JScrollPane jScrollPanelReceived = new JScrollPane();
        jScrollPanelReceived.getViewport().add(receivedMsg);


        JScrollPane jScrollPanelReply = new JScrollPane();
        jScrollPanelReply.getViewport().add(replyMsg);

        mainPanel.add(rvDeamonText);
        mainPanel.add(rvDeamon);
        mainPanel.add(rvNetworkText);
        mainPanel.add(rvNetwork);
        mainPanel.add(rvServiceText);
        mainPanel.add(rvService);
        mainPanel.add(rvSubjectText);
        mainPanel.add(rvSubject);
        mainPanel.add(connect);
        mainPanel.add(isConnected);
        mainPanel.add(rvMsgText);
        mainPanel.add(jScrollPane);
        mainPanel.add(jScrollPaneStatus);
        mainPanel.add(sendMsg);
        mainPanel.add(listenerText);
        mainPanel.add(listener);
        mainPanel.add(received);
        mainPanel.add(jScrollPanelReceived);
        mainPanel.add(replyCheckBox);
        mainPanel.add(jScrollPanelReply);

        return mainPanel;
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("Connect")) {
            rvDeamon.setEditable(false);
            rvNetwork.setEditable(false);
            rvService.setEditable(false);
            rvSubject.setEditable(false);
            connect.setText("Disconnect");
            isConnected.setText("Connected");
            isConnected.setBackground(Color.GREEN);
            sendMsg.setEnabled(true);
            listener.setEnabled(true);
            RvCommunication.getInstance().init();
        }

        if (e.getActionCommand().equals("Disconnect")) {
            RvCommunication.getInstance().close();
            rvDeamon.setEditable(true);
            rvNetwork.setEditable(true);
            rvService.setEditable(true);
            rvSubject.setEditable(true);
            isConnected.setBackground(Color.RED);
            isConnected.setText("Not Connected");
            connect.setText("Connect");
            sendMsg.setEnabled(false);
            listener.setEnabled(false);
            status.setBackground(this.getBackground());
        }

        if (e.getActionCommand().equals("Send")) {
            RvCommunication rvCommunication = RvCommunication.getInstance();
            rvCommunication.sendMsg(rvMsg.getText());
        }


        if (e.getActionCommand().equals("Start Listener")) {
            new Thread(RvCommunication.getInstance()).start();
            listenerText.setText("Listening on: " + rvSubject.getText() + " subject");
            listener.setText("Stop Listener");
        }

        if (e.getActionCommand().equals("Stop Listener")) {
            RvCommunication.getInstance().stopListening();
            listenerText.setText("Not Running");
            listener.setText("Start Listener");
        }

    }

    public static void newMsg(RVMessageInterfaceWraper msg) {
        receivedMsg.append(NEW_MSG);
        receivedMsg.append(msg.getText());
        if (replyCheckBox.isSelected()) {
            try {
                RvCommunication.getInstance().sendReply(new RVMessageInterfaceWraper(replyMsg.getText()), msg);
            } catch (TibrvException e) {
                status.append(e.getMessage());
            }
        }
    }
}
