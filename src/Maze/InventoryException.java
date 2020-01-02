package Maze;

/**
 * @author Hugh Lockwood
 * Class to manage exceptions with the player's inventory
 */
public class InventoryException extends Exception {

	/**
	 * @param msg the reason for the exception
	 */
	public InventoryException(String msg) {
        super(msg);
    }
}
