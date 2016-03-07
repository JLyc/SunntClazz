package gui;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by JLyc on 3/6/2016.
 */
public class MainFX extends Application {


    private TextField rvDeamon;
    private TextField rvNetwork;
    private TextField rvService;

    private Accordion accordion = new Accordion();


    private void init(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setScene(new Scene(root));

        final GridPane gridPane = new GridPane();

        gridPane.setHgap(10);
        gridPane.setVgap(10);
        deamonProperties(gridPane);
        deamonInstances(gridPane);

        Button testConnection = new Button("Test Connection");
        GridPane.setConstraints(testConnection, 2, 4);
        testConnection.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (TitledPane titledPane : accordion.getPanes()) {
                    if(titledPane.getText().equals("Deamon: 0")){
//                        accordion.getPanes().remove(titledPane);
                        titledPane.setVisible(false);
                    }

                }
            }
        });

        Button addDeamon = new Button("Add Deamon");
        addDeamon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                int index = accordion.getPanes().size();
                GridPane layut = new GridPane();
                RvDeamon titledPane = new RvDeamon("Deamon: " + index, layut, rvDeamon.getText(),rvNetwork.getText(), rvService.getText());
                titledPane.addToAccordion(accordion);
            }
        });
        GridPane.setConstraints(addDeamon, 1, 4);

        Label msgLabel = new Label("Msg");
        GridPane.setConstraints(msgLabel, 1,5);
        TextField msgText = new TextField();
        msgText.setMinSize(100,200);
        GridPane.setConstraints(msgText, 1,6);

        gridPane.getChildren().addAll(testConnection, addDeamon, msgLabel, msgText);

        root.getChildren().add(gridPane);
    }

    private void deamonInstances(GridPane gridPane) {
        GridPane.setConstraints(accordion, 3, 1);
        GridPane.setRowSpan(accordion, 10);
        GridPane.setColumnSpan(accordion, 4);
        gridPane.getChildren().add(accordion);
    }

    private void deamonProperties(GridPane gridPane) {
        Label RvDeamonLabel = new Label("RvDeamon");
        GridPane.setConstraints(RvDeamonLabel, 1, 1);
        Label RvNetworkLabel = new Label("RvNetwork");
        GridPane.setConstraints(RvNetworkLabel, 1, 2);
        Label RvServiceLabel = new Label("RvService");
        GridPane.setConstraints(RvServiceLabel, 1, 3);

        rvDeamon = new TextField("RvDeamon");
        GridPane.setConstraints(rvDeamon, 2, 1);
        rvNetwork = new TextField("RvNetwork");
        GridPane.setConstraints(rvNetwork, 2, 2);
        rvService = new TextField("RvService");
        GridPane.setConstraints(rvService, 2, 3);


        gridPane.getChildren().addAll(
                RvDeamonLabel,
                rvDeamon,
                RvNetworkLabel,
                rvNetwork,
                RvServiceLabel,
                rvService
        );
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.setMinWidth(450);
        primaryStage.setMinHeight(200);
        primaryStage.show();
        primaryStage.setTitle("RandevouzClien");
    }

    public static void launchFX() {
        launch();
    }
}
