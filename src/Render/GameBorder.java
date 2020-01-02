package Render;

import javax.swing.border.AbstractBorder;
import java.awt.*;

/**
 * @author Daniel Pullon
 * The border for the panel window
 */
public class GameBorder extends AbstractBorder {
    private Color wallColor = Color.gray;

    private int thickness = 10;

    /**
     * @param thickness the thickness of the border
     */
    public GameBorder(int thickness) {
        this.thickness = thickness;
    }

    /**
     * @param wall the color of the wall
     */
    public GameBorder(Color wall) {
        this.wallColor = wall;
    }

    /**
     * @param thickness the border thickness
     * @param wall the border colour
     */
    public GameBorder(int thickness, Color wall) {
        this.thickness = thickness;
        this.wallColor = wall;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        g.setColor(getWallColor());

//        g.drawLine(x, y, w - 1, h - 1);

        //  Paint a tall wall around the component
        for (int i = 0; i < thickness; i++) {
            g.drawRoundRect(x + i, y + i, w - i - 1, h - i - 1, thickness - i,
                    thickness);
            g.drawRoundRect(x + i, y + i, w - i - 1, h - i - 1, thickness,
                    thickness - i);
            g.drawRoundRect(x + i, y, w - i - 1, h - 1, thickness - i,
                    thickness);
            g.drawRoundRect(x, y + i, w - 1, h - i - 1, thickness, thickness
                    - i);
        }
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(thickness, thickness, thickness, thickness);
    }

    public Insets getBorderInsets(Component c, Insets i) {
        i.left = i.right = i.bottom = i.top = thickness;
        return i;
    }

    public boolean isBorderOpaque() {
        return true;
    }

    /**
     * @return the thickness
     */
    public int getThickness() {
        return thickness;
    }

    /**
     * @return the colour
     */
    public Color getWallColor() {
        return wallColor;
    }
}
