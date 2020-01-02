import Maze.GateTile;
import Maze.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for Gate
 * @author Justina
 */

class GateTileTest {

    /**
     * Tests that you can make a new Gate
     * Also tests whether you the gate can be walked through
     */
    @Test
    public void workingGateTest(){
        GateTile g = new GateTile(0, 0);
        Boolean b = g.isWalkable();
        assertEquals(false, b);
    }

    /**
     * Tests whether you can set the gate to be walked through
     */
    @Test
    public void canWalkThrough(){
        GateTile g = new GateTile(0, 0);
        g.setPassable(true);
        assertEquals(true, g.isWalkable());
    }
}