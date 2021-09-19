import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<DPoint> points = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            double angleFromTop = i*Math.PI/5;
//            double distsmall = 5;
//            double distbetween = 6.9;
//            double distbig = 7;
//            double ang2 = 0.17;
//            double ang3 = 0.4;
//            double ang4 = 0.42;
//            points.add(new DoublePoint(distsmall*Math.sin(angleFromTop), distsmall*Math.cos(angleFromTop)));
//            points.add(new DoublePoint(distbetween*Math.sin(angleFromTop+ang2), distbetween*Math.cos(angleFromTop+ang2)));
//            points.add(new DoublePoint(distbig*Math.sin(angleFromTop+ang3), distbig*Math.cos(angleFromTop+ang3)));
//            points.add(new DoublePoint(distsmall*Math.sin(angleFromTop+ang4), distsmall*Math.cos(angleFromTop+ang4)));
//        }
        for (int i = 0; i < 1000; i++) {
            double ang = i*Math.PI/500;
            double dist = 5+1.5*Math.cos(ang*15);
            points.add(new DPoint(dist*Math.sin(ang), dist*Math.cos(ang)));
        }
        GliderPosition magnet = new GliderPosition(0, 0.5);
        GliderPosition car = new GliderPosition(59, 0.5);

        Track t = new Track(points, car, magnet);
        new GraphicsPanel(t);
    }
}
