import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GraphicsPanel extends JFrame {
    Point red;
    Point green;
    private Track _t;
    public GraphicsPanel(Track t) {
        _t = t;
        setTitle("Magnet Track");
        setSize(800, 800);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.WHITE);

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                _t.moveCar(0.5);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        new Thread(() -> {
            for (int i = 0; i < 10000 /*10000 = 5 min */; i++) {
                _t.moveCar(0.07);
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                repaint();
            }
        }).start();
    }

    @Override
    public void paint(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;

        if (red != null) {
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(25, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10));
            g.drawLine(red.x, red.y, red.x, red.y);
            g.drawLine(green.x, green.y, green.x, green.y);
        }

        g.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10));
        g.setColor(new Color(40, 170, 242));
        int[] xArray = new int[_t.getPoints().size()+1];
        int[] yArray = new int[_t.getPoints().size()+1];

        for (int i = 0; i < _t.getPoints().size(); i++) {
            Point A = convertCoords(_t.getPoints().get(i));
            xArray[i] = A.x;
            yArray[i] = A.y;
        }
        xArray[xArray.length-1] = xArray[0];
        yArray[yArray.length-1] = yArray[0];
        g.drawPolyline(xArray, yArray, xArray.length);
        g.setStroke(new BasicStroke(25, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10));
        g.setColor(Color.RED);
        DoublePoint magnet = _t.getMagnetPos();
        Point mp = convertCoords(magnet);
        g.drawLine(mp.x, mp.y, mp.x, mp.y);

        g.setStroke(new BasicStroke(23, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10));
        g.setColor(Color.GREEN);
        DoublePoint car = _t.getCarPos();
        Point cp = convertCoords(car);
        g.drawLine(cp.x, cp.y, cp.x, cp.y);

        red = mp;
        green = cp;
    }

    private static Point convertCoords(DoublePoint p) {
        // DUAL RENDERING CODE:
//        // is 5 in 1, 1 dir, at 4, -4
//        DoublePoint dp = new DoublePoint(4, 0);
//        double r = 3;
//        double dist = p.dist(dp);
//        double dir = dp.ang(p);
//        p = dp.goInDirection(dir, r*r/dist);
//        // END DUAL RENDERING CODE
        int x = (int) (p.getX()*50 + 400.5);
        int y = (int) (p.getY()*-50 + 400.5);
        return new Point(x, y);
    }
}