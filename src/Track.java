import java.util.List;

public class Track {
    private List<DPoint> _points; // Assume last point connects to first.
    private GliderPosition _car;
    private GliderPosition _magnet;
    private final GliderPosition _carInit;
    private final GliderPosition _magnetInit;

    public Track(List<DPoint> points, GliderPosition car, GliderPosition magnet) {
        _points = points;
        _car = car;
        _car.setTrack(this);
        _carInit = new GliderPosition(_car.getSegmentIndex(), _car.getWayThrough());
        _carInit.setTrack(this);
        _magnet = magnet;
        _magnet.setTrack(this);
        _magnetInit = new GliderPosition(_magnet.getSegmentIndex(), _magnet.getWayThrough());
        _magnetInit.setTrack(this);
    }

    public List<DPoint> getPoints() {
        return _points;
    }

    public void moveCar(double dist /* can be negative */) {
        if (dist == 0) return;
        DPoint startCarPos = _car.getPosition();

        int indexZ = (_car.getSegmentIndex()-1+_points.size())%_points.size();
        int indexA = _car.getSegmentIndex();
        int indexB = (_car.getSegmentIndex()+1)%_points.size();
        int indexC = (_car.getSegmentIndex()+2)%_points.size();

        DPoint Z = _points.get(indexZ);
        DPoint A = _points.get(indexA);
        DPoint B = _points.get(indexB);
        DPoint C = _points.get(indexC);

        if (_car.getWayThrough() < 0.0001/A.dist(B) && dist < 0) {
            // Car is at start node and will be moving backwards to other line segment:
            _car.setPosition(indexZ, trim(1+(dist/Z.dist(A)), 0, 1));
        } else if (_car.getWayThrough() > 1-(0.0001/A.dist(B)) && dist > 0) {
            // Car is at end node and will be moving forwards to next line segment:
            _car.setPosition(indexB, trim(dist/B.dist(C), 0, 1));
        } else {
            // Car will stay on AB
            _car.setPosition(indexA, trim(_car.getWayThrough()+(dist/A.dist(B)), 0, 1));
        }

        updateMagnetPosition(0);

        double distTravelled = _car.getPosition().dist(startCarPos);
        if (dist < 0) distTravelled = -distTravelled;
        if (Math.abs(dist-distTravelled) > 0.0001) {
            moveCar(dist-distTravelled);
        }
    }

    private void updateMagnetPosition(int num) {
        if (num > 6) return; // REMOVE THIS LINE TO MAKE THE MAGNET INFINITELY FAST
        DPoint car = _car.getPosition();

        int indexZ = (_magnet.getSegmentIndex()+_points.size()-1)%_points.size();
        int indexA = _magnet.getSegmentIndex();
        int indexB = (_magnet.getSegmentIndex()+1)%_points.size();
        int indexC = (_magnet.getSegmentIndex()+2)%_points.size();

        DPoint Z = _points.get(indexZ);
        DPoint A = _points.get(indexA);
        DPoint B = _points.get(indexB);
        DPoint C = _points.get(indexC);

        double prevProjectedWayThrough = getProjectedWayThrough(Z, A, car);
        double projectedWayThrough = getProjectedWayThrough(A, B, car);
        double nextProjectedWayThrough = getProjectedWayThrough(B, C, car);

        if (_magnet.getWayThrough() < 0.0001/A.dist(B)) {
            // On first point, check prev and current:
            if (prevProjectedWayThrough >= 1 && projectedWayThrough <= 0) {
                return; // magnet is in optimal position already.
            } else if ((1-prevProjectedWayThrough)*(Z.dist(A)) > projectedWayThrough*(A.dist(B))) {
                // Prev is better angle to travel along:
                double spotTrimmed = trim(prevProjectedWayThrough, 0, 1);
                _magnet.setPosition(indexZ, spotTrimmed);
                if (spotTrimmed == prevProjectedWayThrough) {
                    return; // magnet is now at its final resting place.
                }
            } else {
                // Current is better angle to travel along:
                double spotTrimmed = trim(projectedWayThrough, 0, 1);
                _magnet.setPosition(indexA, spotTrimmed);
                if (spotTrimmed == projectedWayThrough) {
                    return; // magnet is now at its final resting place.
                }
            }
        } else if (_magnet.getWayThrough() > 1-(0.0001/A.dist(B))) {
            // On last point, check current and next:
            if (nextProjectedWayThrough <= 0 && projectedWayThrough >= 1) {
                return; // magnet is in optimal position already.
            } else if (nextProjectedWayThrough*B.dist(C) > A.dist(B)*(1-projectedWayThrough)) {
                // Next is better angle to travel along:
                double spotTrimmed = trim(nextProjectedWayThrough, 0, 1);
                _magnet.setPosition(indexB, spotTrimmed);
                if (spotTrimmed == nextProjectedWayThrough) {
                    return; // magnet is now at its final resting place.
                }
            } else {
                // Current is better angle to travel along:
                double spotTrimmed = trim(projectedWayThrough, 0, 1);
                _magnet.setPosition(indexA, spotTrimmed);
                if (spotTrimmed == projectedWayThrough) {
                    return; // magnet is now at its final resting place.
                }
            }
        } else {
            // Somewhere in between, check current:
            double spotTrimmed = trim(projectedWayThrough, 0, 1);
            _magnet.setPosition(indexA, spotTrimmed);
            if (spotTrimmed == projectedWayThrough) {
                return; // magnet is now at its final resting place.
            }
        }

        // Hasn't finished, keep moving magnet:
        updateMagnetPosition(num+1);
    }

    private double getProjectedWayThrough(DPoint A, DPoint B, DPoint C) {
        double dx = B.getX()-A.getX();
        double dy = B.getY()-A.getY();
        double dist = A.dist(B);
        return (C.getX()-A.getX())*dx/dist/dist + (C.getY()-A.getY())*dy/dist/dist;
    }

    private double trim(double value, double min, double max) {
        return Math.min(Math.max(min, value), max);
    }

    public DPoint getCarPos() {
        return _car.getPosition();
    }

    public DPoint getMagnetPos() {
        return _magnet.getPosition();
    }

    public void restart() {
        _car.setPosition(_carInit.getSegmentIndex(), _carInit.getWayThrough());
        _magnet.setPosition(_magnetInit.getSegmentIndex(), _magnetInit.getWayThrough());
    }
}

