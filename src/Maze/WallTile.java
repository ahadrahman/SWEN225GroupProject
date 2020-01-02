package Maze;

/**
 * @author Ahad Rahman
 * Represents a wall tile in the game. Players can never move onto/pass through wall tiles. Items can't be pushed onto wall
 * tiles either. Wall tiles are static
 */
public class WallTile extends Tile {
    /** Creates a wall tile
     * @param row The row (in regards to the board) of the wall tile
     * @param col The column (in regards to the board) of the wall tile
     * */
    public WallTile(int row, int col) {
        super(row, col);
    }

    //unique setter method to this class

    /**
     * Method that checks whether the area is a place the player can walk on
     * Player can't walk on walls
     * @return false
     */
    @Override
    public boolean isWalkable() {
        return false;
    }

    /**
     * Player should not be able to interact with a Free Tile
     */
    @Override
    public void interact() {
        //Leave this unimplemented
    }

}
