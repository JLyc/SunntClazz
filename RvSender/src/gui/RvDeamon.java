package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * Created by JLyc on 3/6/2016.
 */
public class RvDeamon extends TitledPane {
    String name;

    String rvDeamon = null;
    String rvNetwork = null;
    String rvService = null;

    GridPane gridPane = new GridPane();
    Accordion accordion;

    public RvDeamon(String s, GridPane layut, String rvDeamon, String rvNetwork, String rvService) {
        super(s, layut);

        name=s;
        gridPane = layut;

        if (rvDeamon != null)
            this.rvDeamon = rvDeamon;
        if (rvNetwork != null)
            this.rvNetwork = rvNetwork;
        if (rvService != null)
            this.rvService = rvService;

        init();
    }

    private void init() {
        Label deamon = new Label("Deamon:\t" + rvDeamon);
        GridPane.setConstraints(deamon, 1, 1);
        GridPane.setColumnSpan(deamon, 2);

        Label network = new Label("Network:\t" + rvNetwork);
        GridPane.setConstraints(network, 1, 2);
        GridPane.setColumnSpan(network, 2);

        Label service = new Label("Service:\t" + rvService);
        GridPane.setConstraints(service, 1, 3);
        GridPane.setColumnSpan(service, 2);

        final ToggleButton startStop = new ToggleButton("Start");
        ToggleGroup tg = new ToggleGroup();
        startStop.setToggleGroup(tg);
        tg.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle toggle, Toggle new_toggle) {
                if (new_toggle == null)
                    startStop.setText("Start");
                else {
                    startStop.setText("Stop");
                }
            }
        });
        GridPane.setConstraints(startStop, 1, 5);

        Button sendMsg = new Button("Send msg");
        GridPane.setConstraints(sendMsg, 2, 5);
        ComboBox<String> msgComboBox = new ComboBox<String>();
        GridPane.setConstraints(msgComboBox, 1, 6);


        Button delete = new Button("X");
        GridPane.setConstraints(delete, 3, 1);
        delete.setStyle("-fx-base: red;");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (TitledPane titledPane : accordion.getPanes()) {
                    if(titledPane.getText().equals(name)){
                        accordion.getPanes().remove(titledPane);
                        titledPane.setVisible(false);
                    }
                }
            }
        });

        gridPane.getChildren().addAll(deamon, network, service, startStop, sendMsg, delete);
    }

    public void addToAccordion(Accordion accordion) {
        this.accordion=accordion;
        accordion.getPanes().add(this);
    }
}
