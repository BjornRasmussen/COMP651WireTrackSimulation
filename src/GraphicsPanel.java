import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;

public class GraphicsPanel extends JPanel {
    Point red;
    Point green;
    private Track _t;
    public GraphicsPanel(Track t) {
        _t = t;
        JFrame frame = new JFrame();
        frame.setTitle("Magnet Track");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this, BorderLayout.CENTER);
        setSize(800, 800);
        frame.add(new JLabel("Arrow keys to move, Ctrl+Arrow keys to move fast, R to restart."), BorderLayout.BEFORE_FIRST_LINE);
        frame.setBackground(Color.WHITE);
        setBackground(Color.WHITE);
        frame.setVisible(true);
        final boolean[] dir = {false, false, false, false};
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_LEFT -> {
                        // Handle left:
                        dir[0] = true;
                        dir[2] = false;
                    }
                    case KeyEvent.VK_RIGHT -> {
                        // Handle right:
                        dir[1] = true;
                        dir[2] = true;
                    }
                    case KeyEvent.VK_CONTROL -> {
                        // Speed it up:
                        dir[3] = true;
                    }
                    case KeyEvent.VK_R -> {
                        _t.restart();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_LEFT -> {
                        // handle left
                        dir[0] = false;
                    }
                    case KeyEvent.VK_RIGHT -> {
                        // handle right
                        dir[1] = false;
                    }
                    case KeyEvent.VK_CONTROL -> {
                        // Slow it down:
                        dir[3] = false;
                    }
                }
            }
        });

        new Timer(17, e -> {
            double dist = dir[2] ? (dir[1] ? 0.05 : dir[0] ? -0.05 : 0.0) : (dir[0] ? -0.05 : dir[1] ? 0.05 : 0.0);
            dist *= dir[3] ? 3 : 1;
            _t.moveCar(dist);
            repaint();
        }).start();
    }

    @Override
    public void paintComponent(Graphics g1) {
        super.paintComponent(g1);
        Graphics2D g = (Graphics2D) g1;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        if (red != null) {
//            g.setColor(Color.WHITE);
//            g.setStroke(new BasicStroke(27, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10));
//            g.drawLine(red.x, red.y, red.x, red.y);
//            g.drawLine(green.x, green.y, green.x, green.y);
//        }

        g.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10));
        g.setColor(new Color(40, 170, 242));
        int[] xArray = new int[_t.getPoints().size()+1];
        int[] yArray = new int[_t.getPoints().size()+1];
        Line2D l = new Line2D.Double();
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
        DPoint magnet = _t.getMagnetPos();
        Point mp = convertCoords(magnet);
        g.drawLine(mp.x, mp.y, mp.x, mp.y);

        g.setStroke(new BasicStroke(23, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10));
        g.setColor(Color.GREEN);
        DPoint car = _t.getCarPos();
        Point cp = convertCoords(car);
        g.drawLine(cp.x, cp.y, cp.x, cp.y);

        red = mp;
        green = cp;
    }

    private static Point convertCoords(DPoint p) {
        // DUAL RENDERING CODE:
//        // is 5 in 1, 1 dir, at 4, -4
//        DoublePoint dp = new DoublePoint(4, 0);
//        double r = 3;
//        double dist = p.dist(dp);
//        double dir = dp.ang(p);
//        p = dp.goInDirection(dir, r*r/dist);
//        // END DUAL RENDERING CODE
//        int x = (int) ((3.4+p.getX())*2000 + 400.5);
//        int y = (int) ((1.25+p.getY())*-2000 + 400.5);

        int x = (int) (p.getX()*50 + 400.5);
        int y = (int) (p.getY()*-50 + 400.5);
        return new Point(x, y);
    }
}