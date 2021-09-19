import java.util.List;

public class GliderPosition {
    private Track _track;
    private int _segmentIndex;
    private double _wayThrough;

    public GliderPosition(int segmentIndex, double wayThrough) {
        _segmentIndex = segmentIndex;
        _wayThrough = wayThrough;
    }

    public void setTrack(Track track) {
        _track = track;
    }

    public int getSegmentIndex() {
        return _segmentIndex;
    }

    public double getWayThrough() {
        return _wayThrough;
    }

    public DPoint getPosition() {
        List<DPoint> points = _track.getPoints();
        DPoint startPoint = points.get(_segmentIndex);
        DPoint endPoint = points.get((getSegmentIndex()+1)%points.size());
        return new DPoint(getWayThrough()*endPoint.getX() + (1-getWayThrough())*startPoint.getX(),
                getWayThrough()*endPoint.getY() + (1-getWayThrough())*startPoint.getY());
    }

    public void setPosition(int segmentIndex, double wayThrough) {
        _segmentIndex = segmentIndex;
        _wayThrough = wayThrough;
    }
}
