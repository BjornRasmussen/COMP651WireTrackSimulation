public class DPoint {
    private double _x;
    private double _y;

    public DPoint(double x, double y) {
        _x = x;
        _y = y;
    }

    public double getX() {
        return _x;
    }

    public double getY() {
        return _y;
    }

    public double dist(DPoint other) {
        return Math.sqrt((getX()-other.getX())*(getX()-other.getX()) + (getY()-other.getY())*(getY()-other.getY()));
    }

    public double ang(DPoint other) {
        double ang = Math.atan((other._y-_y)/(other._x-_x));
        if (other._x-_x<0) ang = ang+Math.PI;
        return ang;
    }

    public DPoint goInDirection(double ang, double dist) {
        double dx = dist*Math.cos(ang);
        double dy = dist*Math.sin(ang);
        return new DPoint(_x+dx, _y+dy);
    }
}
