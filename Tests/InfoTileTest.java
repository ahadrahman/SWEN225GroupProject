import Maze.InfoTile;
import Maze.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for infotile
 * @author Justina
 */

class InfoTileTest {

    @Test
    public void getInfoTest(){
        InfoTile i = new InfoTile(0, 0, "This is an info tile");
        String actual = i.getInfo();
        String expected = "This is an info tile";
        assertEquals(expected, actual);
    }

    @Test
    public void isWalkableTest(){
        InfoTile i = new InfoTile(0, 0, "This is an info tile");
        boolean b = i.isWalkable();
        assertEquals(true, b);
    }

}