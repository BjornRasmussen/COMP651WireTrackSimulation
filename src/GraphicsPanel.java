import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GraphicsPanel extends JFrame {
    private Track _t;
    public GraphicsPanel(Track t) {
        _t = t;
        setTitle("Magnet Track");
        setSize(800, 800);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

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
                _t.moveCar(0.1);
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
        super.paintComponents(g1);
        Graphics2D g = (Graphics2D) g1;
        g.setStroke(new BasicStroke(4));
        g.setColor(new Color(13, 130, 202));
        for (int i = 0; i < _t.getPoints().size(); i++) {
            Point A = convertCoords(_t.getPoints().get(i));
            Point B = convertCoords(_t.getPoints().get((i+1)%_t.getPoints().size()));
            g.drawLine(A.x, A.y, B.x, B.y);
        }
        g.setStroke(new BasicStroke(15, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10));
        g.setColor(Color.RED);
        DoublePoint magnet = _t.getMagnetPos();
        Point mp = convertCoords(magnet);
        g.drawLine(mp.x, mp.y, mp.x, mp.y);

        g.setStroke(new BasicStroke(13, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10));
        g.setColor(Color.GREEN);
        DoublePoint car = _t.getCarPos();
        Point cp = convertCoords(car);
        g.drawLine(cp.x, cp.y, cp.x, cp.y);
    }

    private static Point convertCoords(DoublePoint p) {
        // DUAL RENDERING CODE:
        // is 5 in 1, 1 dir, at 4, -4
        DoublePoint dp = new DoublePoint(4, 0);
        double r = 3;
        double dist = p.dist(dp);
        double dir = dp.ang(p);
        p = dp.goInDirection(dir, r*r/dist);
        // END DUAL RENDERING CODE
        int x = (int) (p.getX()*50 + 400.5);
        int y = (int) (p.getY()*-50 + 400.5);
        return new Point(x, y);
    }
}