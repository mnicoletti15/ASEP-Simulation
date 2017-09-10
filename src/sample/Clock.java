package sample;

public class Clock {

    public Clock(double stepSize) {
        t = 0.0;
        dt = stepSize;
        MULTIPLIER = 1;
    }

    public double tick() {
        double step = dt * MULTIPLIER;
        t += step;
        return step;
    }

    public double getTime() {
        return t;
    }

    public void reset() {
        t = 0;
    }

    private double t;
    private final int MULTIPLIER;
    public double dt;
}
