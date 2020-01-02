import Maze.DoorTile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the DoorTile
 * @author Justina
 */
class DoorTileTest {

    /**
     * Tests whether the setWalkable method works
     */
    @Test
    void setWalkable() {
        DoorTile d = new DoorTile(0, 0, "Blue");
        d.setWalkable(true);
        boolean actual = d.isWalkable();
        assertEquals(true, actual);
    }

    /**
     * Checks whether the right colour is fetched
     */
    @Test
    void getColor() {
        DoorTile d = new DoorTile(0, 0, "Blue");
        String actual = d.getColor();
        assertEquals("Blue", actual);
    }
}