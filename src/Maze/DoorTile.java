package Maze;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Ahad Rahman - Interaction
 * @author August Bolter - Initial structure
 * Represents a door in the game. Doors can be unlocked using a key with the same color as the door. By unlocking the door
 * the player can access other parts of the map. */

public class DoorTile extends Tile {
    private static final String DOOR = "Door";
    private String color; //The color of the door. The color can be blue, green, red or yellow
    private boolean isWalkable; //Whether the door is locked

    /** Creates a door tile with a pre-defined color.
     * @param row The row (in regards to the board) of the door
     * @param col The column (in regards to the board) of the door
     * @param color The pre-defined color of the board
     * */
    public DoorTile(int row, int col, String color) {
        super(row, col, color);
        this.color = color;
        this.isWalkable = false; //The door by default (i.e. when created) is locked.
    }


    //getter and setter methods
    /** Locks/unlocks the door.
     * @param isWalkable a boolean representing if the door is getting locked
     * */
    public void setWalkable(boolean isWalkable) {
        this.isWalkable = isWalkable;
    }

    /**
     * Gets the colour of the door
     * @return colour of the door
     */
    public String getColor(){
        return color;
    }


    /**
     * Returns whether a door is unlocked and therefore it can be walked through
     * @return a boolean representing if the door is unlocked and therefore can be walked through
     */
    @Override
    public boolean isWalkable() { return isWalkable;}

    /**
     * Method used to interact with the door: this is when the player moves onto a tile with the door on it
     * In order to interact with a door the player has to have collected the correct key
     */
    @Override
    public void interact() {
        setWalkable();

        //checks if player can enter the tile with the door on it: i.e. whether the door is unlocked
        if (isWalkable()) {
            main.getLevelBoard().replaceWithEmptyTile(this);

            //Remove key from players inventory
            for (Item i : main.getPlayer().getInventory()) {
                if (i instanceof Key) {
                    Key k = (Key) i;
                    //check to make sure the key's colour is the same colour as the door
                    if (k.getColor().equals(color)) {
                        main.getPlayer().removeItemFromInventory(k);
                        return;
                    }
                }
            }

        }
    }

    /**
     * Checks if the player has the correct key and if so unlock the door
     */
    public void setWalkable() {
        for (Item i : main.getPlayer().getInventory()) {
            if (i instanceof Key) {
                Key k = (Key) i;
                if (k.getColor().equals(color)) {
                    isWalkable = true;
                }
            }
        }
    }

    /**
     * Paints the item in the tile on top of each tile.
     */
    public Image getImage() {
        if (main != null) {
            BufferedImage img = main.tileImages.get(color+DOOR + ".png");
            if (img != null) {
                return img;
            }
        }

        String path = PATH+color+DOOR + ".png";

        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new Error(path+"\nThe image failed to load:" + e);
        }
    }

}
