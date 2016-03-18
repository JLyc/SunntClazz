package gui;

import com.sun.xml.internal.txw2.TXW;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import rv_wrappers.RvCommunication;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JLyc on 3/6/2016.
 */
public class MainFX extends Application {


    private TextField rvDeamonName;
    private TextField rvDeamon;
    private TextField rvNetwork;
    private TextField rvService;
    private TextField rvSubject;

    private Accordion accordion = new Accordion();

    private static TextArea msgTextSend;

    public static TextArea getMsgTextSend() {
        return msgTextSend;
    }

    public static void log(String log) {
        logfield.appendText("\n" + log);
    }

    private static TextArea logfield;


    private void init(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setScene(new Scene(root));

        final GridPane gridPane = new GridPane();

        gridPane.setHgap(10);
        gridPane.setVgap(10);
        deamonProperties(gridPane);
        deamonInstances(gridPane);
        messagePanel(gridPane);

        root.getChildren().add(gridPane);
    }

    private static final ObservableList send = FXCollections.observableArrayList("current");
    private static Map<String, String> sendSaved = new HashMap<>();
    private static final ObservableList received = FXCollections.observableArrayList("current");
    private static Map<String, String> receivedSaved = new HashMap<>();

    public static ObservableList getSend() {
        return send;
    }

    public static Map<String, String> getSendSaved() {
        return sendSaved;
    }

    private void messagePanel(GridPane gridPane) {
        final TabPane tabPane = new TabPane();
        msgTextSend = new TextArea();
        msgTextSend.setPromptText("\t Enter your randevousz msg here");
        msgTextSend.setPrefSize(244, 180);
        msgTextSend.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        messageEditor(msgTextSend);
                    }
                }
            }
        });

        final TextArea msgTextReceived = new TextArea();
        msgTextReceived.setPromptText("\t Enter your randevousz msg here");
        msgTextReceived.setPrefSize(244, 180);
        msgTextReceived.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        messageEditor(msgTextReceived);
                    }
                }
            }
        });
        logfield = new TextArea("Beginning of log:");
        logfield.setPrefSize(244, 180);

        final ComboBox<String> savedMsgs = new ComboBox<>();
        savedMsgs.setPromptText("Enter name or chose msg...");
        savedMsgs.setEditable(true);
        savedMsgs.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                if (tabPane.getTabs().get(0).isSelected()) {
                    msgTextSend.setText(sendSaved.get(t1));
                } else {
                    msgTextReceived.setText(receivedSaved.get(t1));
                }
            }
        });

        GridPane.setConstraints(savedMsgs, 2, 8);
        final Tab msgSendTab = new Tab("Sent Msg", msgTextSend);
        msgSendTab.setClosable(false);
        msgSendTab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (((Tab) event.getSource()).isSelected()) {
                    savedMsgs.setItems(send);
                }
            }
        });
        Tab msgReceivedTab = new Tab("Received Msg", msgTextReceived);
        msgReceivedTab.setClosable(false);
        msgReceivedTab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (((Tab) event.getSource()).isSelected()) {
                    savedMsgs.setItems(received);
                }
            }
        });
        Tab logTab = new Tab("Loging", logfield);
        logTab.setClosable(false);
        tabPane.getTabs().addAll(msgSendTab, msgReceivedTab, logTab);
        GridPane.setConstraints(tabPane, 1, 7, 2, 1);

        Button saveMsg = new Button("Save msg");
        saveMsg.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tabPane.getTabs().get(0).isSelected()) {
                    String _name = savedMsgs.getEditor().getText();
                    send.add(_name);
                    sendSaved.put(_name, msgTextSend.getText());
                } else {
                    String _name = savedMsgs.getEditor().getText();
                    received.add(_name);
                    sendSaved.put(_name, msgTextReceived.getText());
                }
            }
        });
        GridPane.setConstraints(saveMsg, 1, 8);
        gridPane.getChildren().addAll(tabPane, saveMsg, savedMsgs);
    }

    private void deamonInstances(GridPane gridPane) {
        Button testConnection = new Button("Test Connection");
        GridPane.setConstraints(testConnection, 2, 6);
        testConnection.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                RvCommunication rvCommunicationDeamon = new RvCommunication(rvDeamonName.getText(),rvDeamon.getText(),rvNetwork.getText(),rvService.getText(),rvSubject.getText());
                rvCommunicationDeamon.open();
                rvCommunicationDeamon.close();
            }
        });

        Button addDeamon = new Button("Add Deamon");
        GridPane.setConstraints(addDeamon, 1, 6);
        addDeamon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                RvCommunication rvCommunicationDeamon = new RvCommunication(rvDeamonName.getText(),rvDeamon.getText(),rvNetwork.getText(),rvService.getText(),rvSubject.getText());
                rvCommunicationDeamon.open();
                GridPane layut = new GridPane();
                RvDeamon titledPane = new RvDeamon(rvCommunicationDeamon , layut, rvDeamon.getText(), rvNetwork.getText(), rvService.getText());
                titledPane.addToAccordion(accordion);
            }
        });
        GridPane.setConstraints(accordion, 3, 1,4, 10);
        gridPane.getChildren().addAll(accordion, addDeamon, testConnection);
    }

    private void deamonProperties(GridPane gridPane) {
        Label rvDeamonNameLabel = new Label("RvName");
        GridPane.setConstraints(rvDeamonNameLabel, 1, 1);
        Label rvDeamonLabel = new Label("RvDeamon");
        GridPane.setConstraints(rvDeamonLabel, 1, 2);
        Label rvNetworkLabel = new Label("RvNetwork");
        GridPane.setConstraints(rvNetworkLabel, 1, 3);
        Label rvServiceLabel = new Label("RvService");
        GridPane.setConstraints(rvServiceLabel, 1, 4);
        Label rvSubjectLabel = new Label("RvSubject");
        GridPane.setConstraints(rvSubjectLabel, 1, 5);


        rvDeamonName = new TextField("artemis");
        GridPane.setConstraints(rvDeamonName, 2, 1);
        rvDeamon = new TextField("tcp:7541");
        GridPane.setConstraints(rvDeamon, 2, 2);
        rvNetwork = new TextField("");
        GridPane.setConstraints(rvNetwork, 2, 3);
        rvService = new TextField("7541");
        GridPane.setConstraints(rvService, 2, 4);
        rvSubject = new TextField("RvSubject");
        GridPane.setConstraints(rvSubject, 2, 5);

        gridPane.getChildren().addAll(
                rvDeamonNameLabel,
                rvDeamonName,
                rvSubjectLabel,
                rvDeamon,
                rvNetworkLabel,
                rvNetwork,
                rvServiceLabel,
                rvService,
                rvSubject,
                rvDeamonLabel
        );
    }

    public static void messageEditor(TextArea msgReply) {
        JTextArea helpArea = new JTextArea(msgReply.getText());
        JScrollPane scrollPane = new JScrollPane(helpArea);
        helpArea.setLineWrap(true);
        helpArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize(new Dimension(500, 500));
        System.out.println("loading");
        JOptionPane.showMessageDialog(null, scrollPane, "RV message",
                JOptionPane.PLAIN_MESSAGE);
        msgReply.setText(helpArea.getText());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(270);
        primaryStage.show();
        primaryStage.setTitle("RandevouzClient");
    }

    public static void launchFX() {
        launch();
    }
}
