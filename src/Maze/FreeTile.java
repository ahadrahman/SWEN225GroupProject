package Maze;

/**
 * @author Ahad Rahman - Interaction
 * @author August Bolter - Initial structure
 * Represents a free space (free tile) in the game. All free tiles can be moved onto by the player. Objects can also be pushed
 * onto/off free tiles. They may hold items as well.
 */
public class FreeTile extends Tile {
    /** Creates a free tile
     * @param row The row (in regards to the board) of the free tile
     * @param col The column (in regards to the board) of the free tile
     * */
    public FreeTile(int row, int col) {
        super(row, col);
    }

    /** Method that checks whether the area is a place the player can walk on
     * Player can always walk on freetiles
     * @return true
     */
    @Override
    public boolean isWalkable() {
        return true;
    }

    /**
     * Player should not be able to interact with a Free Tile
     */
    @Override
    public void interact() {
        //Leave this unimplemented
    }
}
