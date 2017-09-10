package sample;

import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D.*;
import java.lang.Double;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hello World");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label numBalls = new Label("Number of balls");
        grid.add(numBalls, 0, 1);

        TextField numBallsField = new TextField();
        grid.add(numBallsField, 1, 1);

        Label pw = new Label("p value");
        grid.add(pw, 0, 2);

        TextField pwBox = new TextField();
        grid.add(pwBox, 1, 2);

        Label n = new Label("number of slots");
        grid.add(n, 0, 3);

        TextField numSlots = new TextField();
        grid.add(numSlots, 1, 3);

        Label timeLabel = new Label("Time to run (seconds)");
        grid.add(timeLabel, 0, 4);

        TextField timeField = new TextField();
        grid.add(timeField, 1, 4);

        Button btn = new Button("Start");


        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);


        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 6);
//        grid.setGridLinesVisible(true);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 7);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                NUM_LINES = Integer.parseInt(numSlots.getText());
                P = Double.parseDouble(pwBox.getText());
                NUM_BALLS = Integer.parseInt(numBallsField.getText());
                T = Double.parseDouble(timeField.getText());
                actiontarget.setFill(Color.FIREBRICK);
                actiontarget.setText("Sign in button pressed");
                run(primaryStage);
            }
        });

        Scene scene = new Scene(grid, 700, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void printOutput(ArrayList<Ball> balls) {
        for (Ball b : balls) {
            System.out.println(b.getIndex() + " ");
        }
    }

    public void run(Stage primaryStage) {
        Group circles = new Group();
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 400, 400);
        Circle circle = new Circle(15 * NUM_LINES, Color.web("white", 1));
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setStrokeWidth(4);
        circle.setStroke(Color.BLACK);
        circles.getChildren().add(circle);

        double radius = circle.getRadius();

        Circle innerCircle = new Circle(radius - 50, Color.web("white", 1));
        innerCircle.setStrokeType(StrokeType.INSIDE);
        innerCircle.setStrokeWidth(4);
        innerCircle.setStroke(Color.BLACK);
        circles.getChildren().add(innerCircle);

        Group lines = new Group();

        double angleInRadians = (2 * Math.PI) / ((double) NUM_LINES);

        double smallerRadius = innerCircle.getRadius();


        ArrayList<Point3D> Posns = new ArrayList<>();
        // Initialize the possible positions
        for (int i = 0; i < NUM_LINES; i++) {
            double x1 = Math.cos(angleInRadians * i) * smallerRadius;
            double y1 = Math.sin(angleInRadians * i) * smallerRadius;
            double x2 = Math.cos(angleInRadians * i) * radius;
            double y2 = Math.sin(angleInRadians * i) * radius;

            Line line = new Line(x1, y1, x2, y2);
            lines.getChildren().add(line);
            double half = angleInRadians / ((double) 2);
            x1 = Math.cos(angleInRadians * i + half) * smallerRadius;
            y1 = Math.sin(angleInRadians * i + half) * smallerRadius;
            x2 = Math.cos(angleInRadians * i + half) * radius;
            y2 = Math.sin(angleInRadians * i + half) * radius;

            Posns.add(new Point3D((x2 + x1) / ((double) 2), (y2 + y1) / ((double) 2), i));
        }

        ArrayList<Point3D> posnsCopy = new ArrayList<>(Posns);
        Collections.shuffle(posnsCopy);
        ArrayList<Ball> Balls = new ArrayList<>();
        marker = new boolean[NUM_LINES];
        int j;
        // Initialize the balls
        for (int i = 0; i < NUM_BALLS; i++) {
            Point3D p = posnsCopy.get(i);
            j = (int) p.getZ();
            marker[j] = true;
            Ball b = new Ball(p.getX(), p.getY(), j, P, NUM_LINES, marker, dt);
            Balls.add(b);
            circles.getChildren().add(b.getCircle());
        }

//        final Duration PROBE_FREQUENCY = Duration.seconds(.1);
        java.awt.event.ActionListener t = new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                Ball b0 = Balls.get(0);
                if (T <= 0) {
                    timer.stop();
                    printOutput(Balls);
                }
                for (Ball ball : Balls) {
                    int i = ball.computeNext();
                    Point3D p = Posns.get(i);
                    ball.updatePos(p.getX(), p.getY());

                }
                T -= (double) delay / 1000;
            }
        };


        timer = new Timer(delay, t);


//        timer = new AnimationTimer() {
//            @Override
//            public void handle(long l) {
//                for (Ball ball : Balls) {
//                    int i = ball.computeNext();
//                    Point2D.Double p = Posns.get(i);
//                    ball.updatePos(p.getX(), p.getY());
//                }
//            }
//
//        };


//
//        timeline = new Timeline();
//        KeyFrame keyFrame = new KeyFrame(
//                        PROBE_FREQUENCY,
//                        new EventHandler<ActionEvent>() {
//                            @Override public void handle(ActionEvent actionEvent) {
//                                for (Ball ball : Balls) {
//                                    int i = ball.computeNext();
//                                    Point2D.Double p = Posns.get(i);
//                                    ball.updatePos(p.getX(), p.getY());
//                                }
//                            }
//                        }
//                );
//        timeline.getKeyFrames().add(keyFrame);
//        timeline.setCycleCount(Timeline.INDEFINITE);


        root.getChildren().add(circles);
        root.getChildren().add(lines);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMaximized(true);
        timer.start();
//        timeline.play();
//        timer.start();
    }


    public static void main(String[] args) {
        launch(args);
    }


    private int NUM_LINES;
    private double P;
    private int NUM_BALLS = 3;
    private boolean[] marker;
    private double dt = 1;
    private Timer timer;
    private double T;
    private int delay = 100;
//    private Timeline timeline;
//    private AnimationTimer timer;

}
