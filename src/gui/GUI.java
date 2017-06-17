package gui;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javafx.geometry.Point2D;
import resources.UE;
import util.FileExport;
import resources.Antenna;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GUI extends Application {
    final int sceneWidth = 800;
    final int sceneHeight = 700;
    int numberOfUEs = 0;
    int currentSchedulingAlgorithm = 1;

    final int ROUND_ROBIN = 1;
    final int CQI = 2;
    final int PROPORTIONAL_FAIR = 3;
    final int MIN_MAX = 4;
    HashMap <Integer, Color> colorCorrespondence = new HashMap <Integer, Color> ();

    Antenna antenna;
    Text numberOfUEsField;

    public static void main (String [] args){
        launch (args);
    }

    public ArrayList <Circle > drawUEs () {
        ArrayList <Circle> uesDrawn = new ArrayList <Circle> ();
        Random random = new Random();
        int idIndex = 0;
        for (int i=0; i<numberOfUEs; i++) {
            double randX = random.nextInt(sceneWidth+1);
            double randY = random.nextInt(sceneHeight+1);
            UE tmpUE = new UE(randX, randY, i);
            if (this.antenna.calculateDistance(tmpUE) <= this.antenna.coverage) {
                UE.UEs.add(new UE(randX, randY, idIndex));
                idIndex++;
            }
            Circle circle = new Circle();
            circle.setCenterX(randX);
            circle.setCenterY(randY);
            circle.setRadius(5);
            circle.setFill(Color.BLUE);
            uesDrawn.add(circle);
        }

        this.numberOfUEsField.setText(String.valueOf(UE.UEs.size()));

        System.out.println(UE.UEs.size());


        return uesDrawn;
    }

    public void drawAntenna(String path, Pane pane){

        Image antennaImage = new Image (path);
        ImageView iv = new ImageView();
        iv.setImage(antennaImage);
        iv.setFitWidth(70);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);
        iv.setX(400-70/2);
        iv.setY(350-92/2);

        Circle antennaRatio = new Circle();
        antennaRatio.setCenterX(this.antenna.position.getX());
        antennaRatio.setCenterY(this.antenna.position.getY());
        antennaRatio.setRadius(this.antenna.coverage);
        antennaRatio.setFill(Color.YELLOW);
        antennaRatio.setOpacity(0.3);
        pane.getChildren().addAll(iv, antennaRatio);

    }

    public BorderPane setSimulationInterface(Pane map) {

        BorderPane simulation = new BorderPane();
        simulation.setPadding(new Insets(10));

        Button simulationButton = new Button ("Simulate");
        Button drawButton = new Button ("Draw Map");
        simulation.setCenter(drawButton);
        simulation.setBottom(simulationButton);

        drawButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                map.getChildren().clear();
                colorCorrespondence.clear();
                map.getChildren().addAll(drawUEs());
                drawAntenna ("Pictures/antenna.png", map);

                //Assign a color to each UE
                ArrayList<Color> usedColors = new ArrayList<Color>();
                for (int i=0; i<UE.UEs.size(); i++) {
                    Color color = new Color(Math.random(), Math.random(), Math.random(), 1);
                    while (usedColors.contains(color)) {
                        color = new Color(Math.random(), Math.random(), Math.random(), 1);
                    }
                    usedColors.add(color);
                    colorCorrespondence.put(UE.UEs.get(i).id, color);
                }
            }
        });

        simulationButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                UE.showUEs();
                //ArrayList<ArrayList<Integer>> results = antenna.roundRobin(UE.UEs);
                //ArrayList<ArrayList<Integer>> results = antenna.pf(UE.UEs);
                ArrayList<ArrayList<Integer>> results = antenna.maxMin(UE.UEs);
                GridPane schedulingPane = drawScheduling(results);
                ScrollPane sp = new ScrollPane();
                sp.setContent(schedulingPane);
                Scene schedulingScene = new Scene(sp, 236, 600);
                Stage schedulingStage = new Stage();
                schedulingStage.setScene(schedulingScene);
                schedulingStage.show();
                antenna.showScheduling(results);
                FileExport.write(results);
                UE.UEs.clear();
            }
        });

        return simulation;
    }

    public GridPane drawScheduling (ArrayList<ArrayList<Integer>> schedulingList){
        GridPane scheduler = new GridPane();
        scheduler.setHgap(0);
        scheduler.setVgap(0);
        for (int i=0; i<schedulingList.size(); i++) {
            for (int j=0; j<schedulingList.get(i).size(); j++) {
                Text UEId = new Text(String.valueOf(schedulingList.get(i).get(j)));
                UEId.setFill(Color.BLACK);
                StackPane stack = new StackPane();
                Rectangle r = new Rectangle();
                //r.setX(50);
                //r.setY(50);
                r.setWidth(35);
                r.setHeight(35);
                r.setStroke(Color.BLACK);
                r.setFill(colorCorrespondence.get(schedulingList.get(i).get(j)));
                stack.getChildren().addAll(r, UEId);
                scheduler.add(stack, j, i);
            }
        }
        return scheduler;
    }

    public VBox setConfigurationsInterface (){
        VBox configurationVbox = new VBox();
        configurationVbox.setPadding(new Insets(10));
        configurationVbox.setSpacing(10);

        Text configurationText = new Text("Configurations");
        configurationText.setFill(Color.BLACK);
        configurationText.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
        configurationVbox.getChildren().add(configurationText);

        GridPane configurations = new GridPane();
        configurations.setHgap(15);
        configurations.setVgap(10);

        //creating the number of UEs
        Text numberOfUEText = new Text("Number of UEs");
        TextField numberOfUETF = new TextField();
        numberOfUETF.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("textfield changed from " + oldValue + " to " + newValue);
            numberOfUEs = Integer.parseInt(newValue);
        });

        //creating the text for entering the current UE numbers inside the coverage
        Text numberOfUEsText = new Text("Number of UEs in covered");
        this.numberOfUEsField = new Text();

        //Creating scheduling algorithm choicebox
        Text algorithmText = new Text("Choose Algorithm");
        ChoiceBox<String> algorithmCB = new ChoiceBox<String>(FXCollections.observableArrayList("Round Robin", "Best CQI", "Proportional Fair Scheduler", "Min Max"));
        algorithmCB.getSelectionModel().selectFirst();

        //adding the configuration elements
        int startRow = 0;
        configurations.add(numberOfUEText, 0, startRow + 0);
        configurations.add(numberOfUETF, 1, startRow + 0);

        configurations.add(algorithmText, 0, startRow + 1);
        configurations.add(algorithmCB, 1, startRow + 1);

        configurations.add(numberOfUEsText, 0, startRow + 2);
        configurations.add(this.numberOfUEsField, 1, startRow + 2);

        configurationVbox.getChildren().add(configurations);

        return configurationVbox;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.antenna = new Antenna(400, 350, 315);
        primaryStage.setTitle("LTE Scheduling");
        BorderPane root = new BorderPane ();

        Pane map = new Pane();
        map.setPrefSize(sceneWidth, sceneHeight);
        /*map.getChildren().addAll(drawUEs());
        drawAntenna ("antenna.png", map);*/

        VBox configurations = setConfigurationsInterface();
        BorderPane simulation = setSimulationInterface(map);

        root.setLeft(configurations);
        root.setRight(simulation);
        root.setCenter(map);


        Scene primaryScene = new Scene(root);
        //primaryScene.getStylesheets().add(GraphicalInterface.class.getResource("style.css").toExternalForm());
        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }


}
