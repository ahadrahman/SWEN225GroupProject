package Maze;

import Application.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.Objects;

/**
 * @author Ahad Rahman - Interaction
 * @author Daniel Pullon - Get image
 * @author Hugh Lockwood - Make adjustments to get image
 * @author August Bolter - Initial structure
 * Abstract class for Item - this will be implemented by all inventory items (keys, chips). Items are on top of tiles.
 */
public abstract class Item implements Comparable<Item> {
    private int row;
    private int col;
    private String extra;
    protected Main main;
    private boolean inInventory;

    private int priority;
    /**
     * The path the the images folder
     */
    public static final String PATH = "Resources/items/";

    /** Creates an item.
     * @param row The row (in regards to the board) of the item
     * @param col The column (in regards to the board) of the item */
    public Item(int row, int col) {
        this.row = row;
        this.col = col;
        inInventory = false;
    }

    /**
     * Creates an item with an extra
     * @param row the row
     * @param col the col
     * @param extra the extra descriptor
     */
    public Item(int row, int col, String extra) {
        this.row = row;
        this.col = col;
        this.extra = extra;
    }

    //Start of getter and setter methods

    /**
     * Changes the current location of the item
     * @param newRow the row
     * @param newCol the col
     */
    public void changeLocation(int newRow, int newCol){
        this.row = newRow;
        this.col = newCol;
    }

    /**
     * Gets the column
     * @return the col in reference to the level board
     */
    public int getCol() {
        return col;
    }

    /**
     * Gets the row
     * @return the row in reference to the level board
     */
    public int getRow(){
        return row;
    }

    /**
     * @return the extra descriptor
     */
    public String getExtra() { return extra; }

    /**
     * Sets the item's row number
     * @param rowNum the row
     */
    public void setRow(int rowNum){ this.row = rowNum;}

    /**
     * Sets the item's col number
     * @param colNum the col
     */
    public void setCol(int colNum){this.col = colNum;}

    /**
     * Get's the tile associated with the item
     * @return The associated tile
     * */
    public Tile getTile() {
        if (inInventory) {
            return null;
        }
        Tile[][] tiles = main.getLevelBoard().getBoard();
        return tiles[row][col];
    }

    /**
     * Set's the item to be out of/into the players inventory.
     * @param inInventory true if in inventory, false otherwise
     * */
    public void setInInventory(boolean inInventory) {
        this.inInventory = inInventory;
    }

    /**
     * Method to interact with the item
     */
    public abstract void interact();

    /**
     * Check's if an object equals an Item
     * @return Represents if the two objects are equal
     * */
    @Override
    public boolean equals(Object o) {
    	if (o == null) return false;
        if (this == o) return true;
        if (o.getClass() != this.getClass()) return false;
        Item i = (Item) o;
        if (i.getRow() != this.getRow() || i.getCol() != this.getCol()) return false;
        return true;
    }

    /**
     * Paints the item in the tile on top of each tile.
     * @return the image assocatied to the item
     */
    public Image getImage() {
        String itemName = getClass().getName().substring(5);

        if (main != null) {
            BufferedImage img = main.tileImages.get(itemName);
            if (img != null) {
                return img;
            }
        }

        String path = PATH;

        try {
            return ImageIO.read(new File(path+itemName+".png"));
        } catch (IOException e) {
            // If the image is not part of the default resources look in the level plugin specific resources
            try {
                int level;
                if (main == null) {
                    level = 2;
                }
                else {
                    level = (main.getLevel() < 0) ? 2 : main.getLevel();
                }
                return ImageIO.read(new File("src/Utility/Level-" + level + "/Resources/" + getClass().getName() + ".png"));
            } catch (IOException ex) {
                throw new Error("src/Utility/Level-" + main.getLevel() + "/Resources/" + getClass().getName() + ".png" + e);
            }
        }
    }

    /**
     * @return returns the item's priority for drawing
     */
    public int getPriority() {
        return priority;
    }

    /**
     * @param priority sets the item's priority for drawing
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * @param main sets the main class
     */
    public void setMain(Main main) {
        this.main = main;
    }

    @Override
    public String toString() {
        return this.getClass().toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public int compareTo(Item other) {
        return other.getPriority() - this.getPriority();
    }

}
