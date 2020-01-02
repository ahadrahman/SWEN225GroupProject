package Maze;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Ahad Rahman - Wrote the initial class
 * @author Hugh Lockwood - Converted to abstract class and wrote onTick
 * Represents an enemy or other actor.
 * Items that do an action on each tick should extend this class.
 */
public abstract class Enemy extends Item {
    protected Tile currentPos;
    protected Maze.LevelBoard.Direction direction;  //direction enemy is travelling in
    private int row, col;       //location of the enemy

    /**
     * Constructor for an enemy
     * @param row The row (in regards to the board) of the chip
     * @param col The column (in regards to the board) of the chip
     * @param direction enemy is travelling in
     */
    public Enemy(int row, int col, String direction){
        super(row, col, direction);
        this.row = row;
        this.col = col;
        this.direction = LevelBoard.Direction.DOWN;
        this.setPriority(5);
    }


    @Override
    public void interact() {}

    /**
     * Describes what the enemy should do each tick.
     * This method will be called each tick in Application.
     */
    public abstract void onTick();

    /**
     * Makes the enemy move from one tile to another tile
     * @param toMoveTo - tile it is moving to
     */
    public void doMove(Tile toMoveTo){
        currentPos.removeItem(this);
        toMoveTo.addItem(this);
        setCurrentPos(toMoveTo);
    }

    @Override
    public Image getImage() {
        String itemName = getClass().getName().substring(5);
        String dir = direction.toString().toUpperCase();
        dir = dir.charAt(0) + dir.substring(1).toLowerCase();

        if (main != null) {
            BufferedImage img = main.itemImages.get(itemName+dir);
            if (img != null) {
                return img;
            }
        }

        String path = "Resources/enemy/"+itemName+dir+".png";

        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            // If the image is not part of the default resources look in the level plugin specific resources
            try {
                int level = (main.getLevel() < 0) ? 2 : main.getLevel();
                return ImageIO.read(new File("src/Utility/Level-" + level + "/Resources/" + getClass().getName() + dir + ".png"));
            } catch (IOException ex) {
                throw new Error(PATH + getClass().getName() + dir + ".png \nThe image failed to load: " + e);
            }
        }
    }


    //start of getters and setters

    /**
     * Getter method: fetches the current position of the enemy
     * @return currentPos - the tile the enemy is currently on
     */
    public Tile getCurrentPos() {
        return currentPos;
    }

    /**
     * Getter method: fetches the direction of the enemy
     * @return direction - direction enemy is going to move in
     */
    public LevelBoard.Direction getDirection() {
        return direction;
    }

    /**
     * Getter method: fetches the row of the enemy
     * @return row - row enemy is currently on
     */
    @Override
    public int getRow() {
        return row;
    }

    /**
     * Getter method: fetches the col of the enemy
     * @return col - col enemy is currently on
     */
    @Override
    public int getCol() {
        return col;
    }

    /**
     * Setter method: sets the row of the enemy.
     * In one sense: updates the current position to the position the enemy has moved onto.
     * @param currentPos the tile to set the current position to
     */
    public void setCurrentPos(Tile currentPos) {
        this.currentPos = currentPos;
    }


    /**
     * Setter method called at the start of the game.
     * Sets the current tile field to the tile the Enemy is currently on.
     * Needed as the Tile may not have been created when this item is initialised.
     */
    public void setCurrentPos() {
        this.currentPos = main.getLevelBoard().getBoard()[row][col];
    }

    @Override
    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public void setCol(int col) {
        this.col = col;
    }
}
