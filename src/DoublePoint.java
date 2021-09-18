import java.awt.*;

public class DoublePoint {
    private double _x;
    private double _y;

    public DoublePoint(double x, double y) {
        _x = x;
        _y = y;
    }

    public double getX() {
        return _x;
    }

    public double getY() {
        return _y;
    }

    public double dist(DoublePoint other) {
        return Math.sqrt((getX()-other.getX())*(getX()-other.getX()) + (getY()-other.getY())*(getY()-other.getY()));
    }

    public double ang(DoublePoint other) {
        double ang = Math.atan((other._y-_y)/(other._x-_x));
        if (other._x-_x<0) ang = ang+Math.PI;
        return ang;
    }

    public DoublePoint goInDirection(double ang, double dist) {
        double dx = dist*Math.cos(ang);
        double dy = dist*Math.sin(ang);
        return new DoublePoint(_x+dx, _y+dy);
    }
}
