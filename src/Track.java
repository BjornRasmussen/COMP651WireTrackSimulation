import java.util.List;

public class Track {
    private List<DoublePoint> _points; // Assume last point connects to first.
    private GliderPosition _car;
    private GliderPosition _magnet;

    public Track(List<DoublePoint> points, GliderPosition car, GliderPosition magnet) {
        _points = points;
        _car = car;
        _car.setTrack(this);
        _magnet = magnet;
        _magnet.setTrack(this);
    }

    public List<DoublePoint> getPoints() {
        return _points;
    }

    public void moveCar(double dist /* can be negative */) {
        if (dist == 0) return;

        int indexZ = (_car.getSegmentIndex()-1+_points.size())%_points.size();
        int indexA = _car.getSegmentIndex();
        int indexB = (_car.getSegmentIndex()+1)%_points.size();
        int indexC = (_car.getSegmentIndex()+2)%_points.size();

        DoublePoint Z = _points.get(indexZ);
        DoublePoint A = _points.get(indexA);
        DoublePoint B = _points.get(indexB);
        DoublePoint C = _points.get(indexC);

        if (_car.getWayThrough() < 0.01/A.dist(B) && dist < 0) {
            // Car is at start node and will be moving backwards to other line segment:
            _car.updatePosition(indexZ, trim(1+(dist/Z.dist(A)), 0, 1));
        } else if (_car.getWayThrough() > (1-0.01/A.dist(B)) && dist > 0) {
            // Car is at end node and will be moving forwards to next line segment:
            _car.updatePosition(indexB, trim(dist/B.dist(C), 0, 1));
        } else {
            // Car will stay on AB
            _car.updatePosition(indexA, trim(_car.getWayThrough()+(dist/A.dist(B)), 0, 1));
        }

        updateMagnetPosition();
    }

    private void updateMagnetPosition() {
        DoublePoint car = _car.getPosition();
        DoublePoint magnet = _magnet.getPosition();
        Vector magnetToCar = new Vector(car.getX()-magnet.getX(), car.getY()-magnet.getY());
        int indexZ = (_magnet.getSegmentIndex()+_points.size()-1)%_points.size();
        int indexA = _magnet.getSegmentIndex();
        int indexB = (_magnet.getSegmentIndex()+1)%_points.size();
        int indexC = (_magnet.getSegmentIndex()+2)%_points.size();
        DoublePoint Z = _points.get(indexZ);
        DoublePoint A = _points.get(indexA);
        DoublePoint B = _points.get(indexB);
        DoublePoint C = _points.get(indexC);

        double prevProjectedWayThrough = getProjectedWayThrough(Z, A, car);
        double projectedWayThrough = getProjectedWayThrough(A, B, car);
        double nextProjectedWayThrough = getProjectedWayThrough(B, C, car);

        if (_magnet.getWayThrough() < 0.01) {
            // On first point, check prev and current:
            if (prevProjectedWayThrough >= 1 && projectedWayThrough <= 0) {
                return; // magnet is in optimal position already.
            } else if (1-prevProjectedWayThrough > projectedWayThrough) {
                // Prev is better angle to travel along:
                double spotTrimmed = trim(prevProjectedWayThrough, 0, 1);
                _magnet.updatePosition(indexZ, spotTrimmed);
                if (spotTrimmed == prevProjectedWayThrough) {
                    return; // magnet is now at its final resting place.
                }
            } else {
                // Current is better angle to travel along:
                double spotTrimmed = trim(projectedWayThrough, 0, 1);
                _magnet.updatePosition(indexA, spotTrimmed);
                if (spotTrimmed == projectedWayThrough) {
                    return; // magnet is now at its final resting place.
                }
            }
        } else if (_magnet.getWayThrough() > 0.99) {
            // On last point, check current and next:
            if (nextProjectedWayThrough <= 0 && projectedWayThrough >= 1) {
                return; // magnet is in optimal position already.
            } else if (nextProjectedWayThrough > 1-projectedWayThrough) {
                // Next is better angle to travel along:
                double spotTrimmed = trim(nextProjectedWayThrough, 0, 1);
                _magnet.updatePosition(indexB, spotTrimmed);
                if (spotTrimmed == nextProjectedWayThrough) {
                    return; // magnet is now at its final resting place.
                }
            } else {
                // Current is better angle to travel along:
                double spotTrimmed = trim(projectedWayThrough, 0, 1);
                _magnet.updatePosition(indexA, spotTrimmed);
                if (spotTrimmed == projectedWayThrough) {
                    return; // magnet is now at its final resting place.
                }
            }
        } else {
            // Somewhere in between, check current:
            double spotTrimmed = trim(projectedWayThrough, 0, 1);
            _magnet.updatePosition(indexA, spotTrimmed);
            if (spotTrimmed == projectedWayThrough) {
                return; // magnet is now at its final resting place.
            }
        }

        updateMagnetPosition();
    }

    private double getProjectedWayThrough(DoublePoint A, DoublePoint B, DoublePoint C) {
        double dx = B.getX()-A.getX();
        double dy = B.getY()-A.getY();
        double dist = A.dist(B);
        return (C.getX()-A.getX())*dx/dist/dist + (C.getY()-A.getY())*dy/dist/dist;
    }

    private double trim(double value, double min, double max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    public DoublePoint getCarPos() {
        return _car.getPosition();
    }

    public DoublePoint getMagnetPos() {
        return _magnet.getPosition();
    }
}

