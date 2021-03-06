import common.AdditionalParameters;
import common.ShapeType;

import java.awt.Insets;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.Stroke;
import java.awt.RenderingHints;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.Timer;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import javax.swing.JPanel;


public class TitlesPanel extends JPanel implements ActionListener
{
    private Graphics2D g2d;
    private Timer animation;
    private boolean is_done;
    private int start_angle;
    private ShapeType type;
    private AdditionalParameters parameters;

    //** We put an shape identifier to constructor*/
    public TitlesPanel(final ShapeType type, final AdditionalParameters parameters) {
        this.start_angle = 0;
        this.is_done = true;
        this.type = type;
        this.parameters= parameters;
        (this.animation = new Timer(50, this)).setInitialDelay(50);
        this.animation.start();
    }

    @Override
    public void actionPerformed(final ActionEvent arg0) {
        if (this.is_done) {
            this.repaint();
        }
    }
    
    /** this method drawing shapes on ui */
    private void doDrawing(final Graphics g) {
        this.is_done = false;
        (this.g2d = (Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        final Dimension size = this.getSize();
        final Insets insets = this.getInsets();
        final int w = size.width - insets.left - insets.right;
        final int h = size.height - insets.top - insets.bottom;
        final ShapeFactory shape = new ShapeFactory(type, parameters);
        this.g2d.setStroke(shape.stroke);
        this.g2d.setPaint(shape.paint);
        double angle = this.start_angle++;
        if (this.start_angle > 360) {
            this.start_angle = 0;
        }
        final double dr = 90.0 / (w / (shape.width * 1.5));
        for (int j = shape.height; j < h; j += (int)(shape.height * 1.5)) {
            for (int i = shape.width; i < w; i += (int)(shape.width * 1.5)) {
                angle = ((angle > 360.0) ? 0.0 : (angle + dr));
                final AffineTransform transform = new AffineTransform();
                transform.translate(i, j);
                transform.rotate(Math.toRadians(angle));
                this.g2d.draw(transform.createTransformedShape(shape.shape));
            }
        }
        this.is_done = true;
    }

    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        this.doDrawing(g);
    }
}