package Render;

import Maze.*;

import javax.swing.*;
import java.awt.*;

/**
 * @author Daniel Pullon
 * A panel representing an individual tile.
 */
public class TilePanel extends JPanel {
    private Tile tile;
    private JLabel image;

    /**
     * Creates a new tile panel.
     * @param tile The title of the panel.
     */
    public TilePanel(Tile tile) {
        setLayout(new GridLayout(1,1));
        this.tile = tile;

        if(tile.getImage() == null) throw new RuntimeException(tile.getClass().getName() + "Tile image was null");
        image = new JLabel(new ImageIcon(tile.getImage()));
        add(image);
        setVisible(true);
    }

    /**
     * @return the tile the panel is drawing
     */
    public Tile getTile() {
        return tile;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(tile.getImage(),0, 0, this);
        if (tile.hasItem()) {
            for (Item i : tile.getItems()) {
                if (i != null) {
                    g.drawImage(i.getImage(), 0, 0, this);
                }
            }
        }
    }
}
