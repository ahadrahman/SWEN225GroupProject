package Maze;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Ahad Rahman - Interaction
 * @author August Bolter - Initial structure
 * Represents a treasure (chip) in the game. This item can be picked up by the player. Once they have all the chips in the
 * level they can pass through the GateTile and finish the level by reaching the ExitTile.
 */
public class Chip extends Item {
    private static final String POKEBALL = "pokeball";

    /** Creates a chip
     * @param row The row (in regards to the board) of the chip
     * @param col The column (in regards to the board) of the chip
     * */
    public Chip(int row, int col) {
        super(row, col);
        this.setPriority(2);
    }

    /**
     * Method used to interact with the chip: this is when the player moves onto a tile with the chip on it
     * Two things will happen when player interacts with chip:
     * 1) chip will be added to total number of chips
     * 2) chip will disappear from the board and total number of chips on the board will decrease
     */
    @Override
    public void interact() {
        main.decrementChipsRemaining();

        //remove chip from tile
        getTile().removeItem(this);

        // Decrement chips left
        main.getFrame().getInfoPanel().decrementChipsRemaining();
        main.getLevelBoard().updateFields();
    }

    /**
     * Gets the image of the chip
     * @return image of the chip
     */
    public Image getImage() {
        if (main != null) {
            BufferedImage img = main.itemImages.get("pokeball");
            if (img != null) {
                return img;
            }
        }

        String path = PATH + POKEBALL + ".png";

        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new Error(path+"\nThe image failed to load:" + e);
        }
    }

}
