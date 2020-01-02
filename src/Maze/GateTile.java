package Maze;

/**
 * @author Ahad Rahman - Interaction
 * @author August Bolter - Initial structure
 * Represents a gate tile in the game. A gate tile (exit lock) behaves like a wall (player can't pass through it or interact
 * with it) if the player doesn't have all the chips in the level. Once they have all the chips the player can pass through
 * it.
 */
public class GateTile extends Tile {
    private boolean isPassable; //Representing whether the player can pass through the gate

    /** Creates a gate tile.
     * @param row The row (in regards to the board) of the gate tile
     * @param col The column (in regards to the board) of the gate tile */
    public GateTile(int row, int col) {
        super(row, col);
        /* By default the gate can't be passed through since the gate tile is created when the level is loaded in and at this
        * point the player won't have all the chips in the level. */
        isPassable = false;
    }

    //start of setter and getter methods
    @Override
    public boolean isWalkable() {
        return isPassable;
    }

    @Override
    public void interact() {
        if (main.allChipsCollected()){
            setPassable(true);
        }

        if (isPassable){
            main.getLevelBoard().replaceWithEmptyTile(this);
        }

    }

    /** Sets the 'passability' of the gate (i.e. can a player pass through it or not).
     * @param passable The boolean representing the 'passability' of the gate.
     * */
    public void setPassable(boolean passable){
        isPassable = passable;
    }
}
