package Maze;

import Application.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author August Bolter
 * @author Hugh Lockwood
 * @author Ahad Rahman
 * An abstract class representing a tile in the game. This abstract class is extended by concrete classes like DoorTile,
 * ExitTile etc. The board is made up of tiles (with items on some of the tiles).
 */
public abstract class Tile {
    /**
     *  The path to Tile images
     */
    public static final String PATH = "Resources/floor/";
    private int row;
    private int col;
    private String extra;
    private List<Item> items;
    protected Main main;

    /** Creates a tile.
     * @param row The row (in regards to the board) of the tile
     * @param col The column (in regards to the board) of the tile */
    public Tile(int row, int col){
        this.row = row;
        this.col = col;
        this.items = new ArrayList<>();
    }

    /**
     * Creates a new tile
     * @param row the row in reference to the level board
     * @param col the col in reference to the level board
     * @param extra any extra descriptions
     */
    public Tile(int row, int col, String extra) {
        this.row = row;
        this.col = col;
        this.extra = extra;
        this.items = new ArrayList<>();
    }

    /** Gets the row of the tile
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /** Gets the column of the tile
     * @return the col
     */
    public int getCol() {
        return col;
    }

    /**
     * @return the extra field, null if the tile doesn't have one
     */
    public String getExtra() { return extra; }

    /**
     * @return the list of items on this tile
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Adds a new item to this tile.
     * @param item the item to add
     */
    public void addItem(Item item) {
        items.add(item);
        Collections.sort(items);
    }

    /**
     * Adds all items in a list to the tiles list of items
     * @param items the list of items to add
     */
    public void addAllItems(List<Item> items) {
        this.items.addAll(items);
        Collections.sort(this.items);
    }

    /**
     * Removes a new item from this tile.
     * @param item the item to removes
     */
    public void removeItem(Item item) {
        items.removeIf(i -> i.equals(item));
    }

    /**
     * Paints the item in the tile on top of each tile.
     * @return the image associated to the tile
     */
    public Image getImage() {
        String tileName = getClass().getName().substring(5);

        if (main != null) {
            BufferedImage img = main.tileImages.get(tileName);
            if (img != null) {
                return img;
            }
        }

        try {
            return ImageIO.read(new File(PATH+tileName+".png"));
        } catch (IOException e) {
            int level = (main.getLevel() < 0) ? 2 : main.getLevel();
            // If the image is not part of the default resources look in the level plugin specific resources
            try {
                return ImageIO.read(new File("src/Utility/Level-" + level + "/Resources/" + getClass().getName() + ".png"));
            } catch (IOException ex) {
               throw new Error(PATH + getClass().getName() + "\nThe image failed to load:" + e);
            }
        }
    }

    /**
     * @return true if the tile has the player on it, false otherwise
     */
    public boolean hasPlayer(){
        for (Item i : items){
            if (i instanceof Player){
                return true;
            }
        }
        return false;
    }

    /** Checks if an Object is the same as (equals) this tile.
     * @param o The object that the tile is being compared to
     * @return Whether the object is the same as the tile
     * */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tile)) return false;
        Tile tile = (Tile) o;
        /* If the tile is in the same location as 'this' (this tile) then they are the same tile. */
        return getRow() == tile.getRow() &&
                getCol() == tile.getCol();
    }

    /** An abstract method which determines if a tile is walkable.
     * @return Whether the tile is walkable */
    public abstract boolean isWalkable();

    /**
     * Method to interact with the tile.
     */
    public abstract void interact();

    /**
     * @return true if the tile has at least one item
     */
    public boolean hasItem() {
        return !items.isEmpty();
    }

    /**
     * Sets the main class
     * @param main the class to set
     */
    public void setMain(Main main) {
        this.main = main;
    }
}
