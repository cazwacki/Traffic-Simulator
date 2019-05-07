import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class RotateTest {

    public static void main(String[] args) {
        new RotateTest();
    }

    public RotateTest() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new TestPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public class TestPane extends JPanel {

        private Rectangle rect01;
        private Rectangle rect02;

        private int angle = 0;

        public TestPane() {

            rect01 = new Rectangle(0, 0, 100, 100);
            rect02 = new Rectangle(0, 0, 100, 100);

            Timer timer = new Timer(40, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    angle++;
                    repaint();
                }
            });
            timer.start();

        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(250, 250);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();

            int width = getWidth();
            int height = getHeight();

            AffineTransform at = new AffineTransform();

            int center = width / 2;

            int x = center + (center - rect01.width) / 2;
            int y = (height - rect01.height) / 2;
            at.translate(x, y);
            at.rotate(Math.toRadians(angle), rect01.width / 2, rect01.height / 2);
            GeneralPath path1 = new GeneralPath();
            path1.append(rect01.getPathIterator(at), true);
            g2d.fill(path1);

            g2d.setColor(Color.BLUE);
            g2d.draw(path1.getBounds());

            at = new AffineTransform();
            x = (center - rect02.width) / 2;
            y = (height - rect02.height) / 2;
            at.translate(x, y);
            at.rotate(Math.toRadians(-angle), rect02.width / 2, rect02.height / 2);
            GeneralPath path2 = new GeneralPath();
            path2.append(rect01.getPathIterator(at), true);
            g2d.fill(path2);

            g2d.setColor(Color.BLUE);
            g2d.draw(path2.getBounds());

            Area a1 = new Area(path1);
            Area a2 = new Area(path2);
            a2.intersect(a1);
            if (!a2.isEmpty()) {
                g2d.setColor(Color.RED);
                g2d.fill(a2);
            }

            g2d.dispose();
        }

    }

}