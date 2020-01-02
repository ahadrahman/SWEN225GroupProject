package Maze;


import Application.Main;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hugh Lockwood
 * @author Ahad Rahman -
 * @author August Bolter
 * @author Daniel Pullon
 * Class for the levelBoard
 */
public class LevelBoard {
    private static final int SIZE = 32;
    private final String title;

    /**
     * An enum for the direction actors are facing
     */
    public enum Direction {
		/*** left */ LEFT,
	    /*** right */ RIGHT,
	    /*** up */ UP,
	    /*** down */ DOWN
    }
    
    private int totalChips, timeLimit;

    private Main main;

    private Tile[][] board;

    /**
     * Creates a new level board object
     * @param title the level's title
     * @param totalChips the number of chips in the level
     * @param timeLimit the time limit of the level
     * @param board the array of objects in the level
     */
    public LevelBoard(String title, int totalChips, int timeLimit, Tile[][] board) {
        this.title = title;
        this.totalChips = totalChips;
        this.timeLimit = timeLimit;
        this.board = board;
    }

    /**
     * Gets the tile at a given direction from a given position.
     * @param currentPos the given position
     * @param direction the direction from the given position
     * @return the tile at the direction
     */
    public Tile getTileAtPosition(Tile currentPos, Direction direction) {
        switch (direction) {
            case LEFT:
                if (currentPos.getCol() > 0)
                    return board[currentPos.getRow()][currentPos.getCol()-1];
                else
                    return null;
            case RIGHT:
                if (currentPos.getCol() < board.length)
                    return board[currentPos.getRow()][currentPos.getCol()+1];
                else
                    return null;
            case UP:
                if (currentPos.getRow() > 0)
                    return board[currentPos.getRow()-1][currentPos.getCol()];
                else
                    return null;
            case DOWN:
                if (currentPos.getCol() < board[0].length)
                    return board[currentPos.getRow()+1][currentPos.getCol()];
                else
                    return null;
            default:
                return null;
        }
    }

    /**
     * Associates tiles and items to this main object
     */
    public void linkTilesAndItemsToMain() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j].setMain(main);
                for (Item item : board[i][j].getItems()){
                    item.setMain(main);
                }
            }
        }
    }


    /**
     * Replaces a tile with an empty free tile
     * Used when unlocking doors for example
     * @param tile the tile to replace
     */
    public void replaceWithEmptyTile(Tile tile){
        board[tile.getRow()][tile.getCol()] = new FreeTile(tile.getRow(), tile.getCol());
    }

    //getter and setter methods

    /**
     * @return the board
     */
    public Tile[][] getBoard() {
        return board;
    }

    /**
     * Method that gets the total number of chips
     * @return number of chips
     */
    public int getTotalChips() {
        return totalChips;
    }

    /**
     * Method that gets the time limit
     * @return the timeLimit
     */
    public int getTimeLimit() {
        return timeLimit;
    }

    /**
     * Update the chips and time left
     */
    public void updateFields() {
        this.totalChips = main.getChipsRemaining();
        this.timeLimit = main.getTimeRemaining();
    }

    /**
     * Method that gets the title
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param main the main game
     */
    public void setMain(Main main) {
        this.main = main;
        linkTilesAndItemsToMain();
    }

    /**
     * Returns the player currently on the board
     * @return the player on the level
     */
    public Player getPlayer() {
        for (int row = 0; row < board.length; row++)
            for (int col = 0; col < board[row].length; col++)
                if (board[row][col].hasItem())
                    for (Item item : board[row][col].getItems())
                        if (item instanceof Player)
                            return (Player) item;

        return null;
    }

    /**
     * Gets a list of all the enemies on the board
     * @return list of enemies on the board
     */
    public List<Enemy> getEnemies(){
        List<Enemy> enemies = new ArrayList<>();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col].hasItem()) {
                    for (Item item : board[row][col].getItems()) {
                        if (item instanceof Enemy){
                            enemies.add((Enemy)item);
                        }
                    }
                }
            }
        }
        return enemies;
    }

    /**
     * @param dir the direction
     * @return the inverse of that direction e.g. dir := up returns down
     */
    public static Direction directionInverse(Direction dir) {
        switch(dir) {
            case UP:
                return Direction.DOWN;
            case DOWN:
                return Direction.UP;
            case LEFT:
                return Direction.RIGHT;
            case RIGHT:
                return Direction.LEFT;
            default:
                return null; // unreachable
        }
    }

}
