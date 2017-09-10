package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Matthew.Nicoletti@ibm.com on 6/27/17.
 */
public class Ball {
    public Ball(double xPos, double yPos, int i, double probClockwise, int numSpots, boolean[] posns, double dt) {
        marker = posns;
        NUM_SPOTS = numSpots;
        p = probClockwise;
        index = i;
        x = xPos;
        y = yPos;
        makeCircle();
        clock = new Clock(dt);
        random = new Random();
    }

    private void makeCircle() {
        circle = new Circle(15, Color.web("black", 1));
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setStrokeWidth(4);
        circle.setStroke(Color.BLACK);
        circle.setCenterX(x);
        circle.setCenterY(y);
    }

    public Circle getCircle() {
        return circle;
    }

    public int getIndex() {
        return index;
    }

    public Clock getClock() {
        return clock;
    }

    public int computeNext() {
        double prob = p * clock.dt;
        if (random.nextDouble() < clock.tick() * p) {
            clock.reset();
            if (random.nextDouble() < p) {
                int i = mod(index - 1, NUM_SPOTS);
                if (!marker[i]) {
                    marker[index] = false;
                    marker[i] = true;
                    index = i;
                }
                return index;
            } else {
                int i = mod(index + 1, NUM_SPOTS);
                if (!marker[i]) {
                    marker[index] = false;
                    marker[i] = true;
                    index = i;
                }
                return index;
            }
        } else {
            return index;
        }
    }

    private int mod(int a, int b) {
        int result = a % b;
        if (result < 0) {
            result += b;
        }
        return result;
    }

    public void updatePos(double xPos, double yPos) {
        x = xPos;
        y = yPos;
        circle.setCenterX(x);
        circle.setCenterY(y);
    }


    private double x;
    private double y;
    private Circle circle;
    private Clock clock;
    private Random random;
    private int index;
    private double p;
    private final int NUM_SPOTS;
    private boolean[] marker;
}
