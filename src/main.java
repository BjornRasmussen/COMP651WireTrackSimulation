import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args) {
        List<DoublePoint> points = new ArrayList<>();
        for (int i = 0; i < 19; i++) {
            double angleFromTop = i*Math.PI/10;
            double distsmall = 6;
            double distbetween = 6.9;
            double distbig = 7;
            double ang2 = 0.1;
            double ang3 = 0.2;
            double ang4 = 0.21;
            points.add(new DoublePoint(distsmall*Math.sin(angleFromTop), distsmall*Math.cos(angleFromTop)));
            points.add(new DoublePoint(distbetween*Math.sin(angleFromTop+ang2), distbetween*Math.cos(angleFromTop+ang2)));
            points.add(new DoublePoint(distbig*Math.sin(angleFromTop+ang3), distbig*Math.cos(angleFromTop+ang3)));
            points.add(new DoublePoint(distsmall*Math.sin(angleFromTop+ang4), distsmall*Math.cos(angleFromTop+ang4)));
        }
        GliderPosition magnet = new GliderPosition(0, 0.5);
        GliderPosition car = new GliderPosition(4, 0.5);

        Track t = new Track(points, car, magnet);
        new GraphicsPanel(t);
    }
}
