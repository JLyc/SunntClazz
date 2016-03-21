package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JLyc on 3/19/2016.
 */
public class MessagePanel {
    private static final int TEXT_AREA_PREF_W = 244;
    private static final int TEXT_AREA_PREF_H = 180;

    private int panelOffset;


    private static MessagePanel messagePanel;
    private ComboBox<String> savedMsgs = new ComboBox<>();


    public static MessagePanel getMessagePanel() {
        if (messagePanel == null) {
            messagePanel = new MessagePanel();
        }
        return messagePanel;
    }

    private TextArea sendTxt;
    private TextArea receivedTxt;
    private static TextArea logTxt = new TextArea("### Beginning of log ###");

    private final ObservableList send = FXCollections.observableArrayList("current");
    private Map<String, String> sendSaved = new HashMap<>();
    private final ObservableList received = FXCollections.observableArrayList("current");
    private Map<String, String> receivedSaved = new HashMap<>();

    public void init(GridPane gridPane, int panelOffset) {
        this.panelOffset = new Integer(panelOffset);


        final TabPane tabPane = new TabPane();
        constructTxtAreas();
        constructTabPane(tabPane);

        savedMsgs = new ComboBox<>();
        savedMsgs.setPromptText("Enter name or chose msg...");
        savedMsgs.setEditable(true);
        savedMsgs.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                if (tabPane.getTabs().get(0).isSelected()) {
                    sendTxt.setText(sendSaved.get(t1));
                } else {
                    receivedTxt.setText(receivedSaved.get(t1));
                }
            }
        });

        GridPane.setConstraints(savedMsgs, 2, panelOffset+2);

        Button saveMsg = new Button("Save msg");
        saveMsg.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tabPane.getTabs().get(0).isSelected()) {
                    String _name = savedMsgs.getEditor().getText();
                    send.add(_name);
                    sendSaved.put(_name, sendTxt.getText());
                } else {
                    String _name = savedMsgs.getEditor().getText();
                    received.add(_name);
                    sendSaved.put(_name, receivedTxt.getText());
                }
            }
        });
        GridPane.setConstraints(saveMsg, 1, panelOffset+2);

        gridPane.getChildren().addAll(tabPane, saveMsg, savedMsgs);
    }

    private TabPane constructTabPane(TabPane tabPane) {
        final Tab msgSendTab = new Tab("Sent Msg", sendTxt);
        msgSendTab.setClosable(false);
        msgSendTab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (((Tab) event.getSource()).isSelected()) {
                    savedMsgs.setItems(send);
                }
            }
        });

        Tab msgReceivedTab = new Tab("Received Msg", receivedTxt);
        msgReceivedTab.setClosable(false);
        msgReceivedTab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (((Tab) event.getSource()).isSelected()) {
                    savedMsgs.setItems(received);
                }
            }
        });

        Tab logTab = new Tab("Loging", logTxt);
        logTab.setClosable(false);
        tabPane.getTabs().addAll(msgSendTab, msgReceivedTab, logTab);

        GridPane.setConstraints(tabPane, 1, panelOffset+1, 2, 1);
        return tabPane;
    }

    private void constructTxtAreas() {
        sendTxt = new TextArea();
        sendTxt.setPromptText("Enter your randevousz msg here");
        sendTxt.setPrefSize(TEXT_AREA_PREF_W, TEXT_AREA_PREF_H);
        sendTxt.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
                    MainFX.messageEditor(sendTxt);
                }
            }
        });

        receivedTxt = new TextArea();
        receivedTxt.setPromptText("Enter your randevousz msg here");
        receivedTxt.setPrefSize(TEXT_AREA_PREF_W, TEXT_AREA_PREF_H);
        receivedTxt.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
                    MainFX.messageEditor(receivedTxt);
                }
            }
        });

        logTxt.setPrefSize(TEXT_AREA_PREF_W, TEXT_AREA_PREF_H);
        logTxt.setWrapText(true);
        logTxt.setStyle("-fx-control-inner-background: black; -fx-control-inner-foreground: white;");
        logTxt.textProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue,
                                Object newValue) {
                logTxt.setScrollTop(Double.MAX_VALUE);
            }
        });
    }

    public ObservableList getSend() {
        return send;
    }

    public ObservableList getReceived() {
        return received;
    }

    public TextArea getSendTxt() {
        return sendTxt;
    }

    public void setSendTxt(String msgTxt) {
        sendTxt.setText(msgTxt);
    }

    public TextArea getReceivedTxt() {
        return receivedTxt;
    }

    public void setReceiveddTxt(String msgTxt) {
        receivedTxt.setText(msgTxt);
    }

    private static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    public static void logTxt(String msg) {

        logTxt.appendText("\n"+dateFormat.format(new Date())+" "+msg);
    }

    public String getSavedTxt(String msgKey){
        return sendSaved.get(msgKey);
    }
}
