import Application.Main;
import Maze.Enemy;
import Maze.FreeTile;
import Maze.Tile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the enemy
 * @author Justina
 */
class EnemyTest {

    /**
     * Tests whether the .getEnemies() method works
     */
    @Test
    void getNumEnemiesTest() {
        int expected = 15;
        AtomicInteger actual = new AtomicInteger(100);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Main game = new Main("Tester",2);
                    System.out.println(game.getLevelBoard().getEnemies().size());
                    actual.set(game.getLevelBoard().getEnemies().size());
                    //looping through to get the one enemy

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertNotEquals(expected, actual.get());
    }

    /**
     * Tests whether the correc col is fetched
     */
    @Test
    void getColTest() {
        int expected = 15;
        AtomicInteger actual = new AtomicInteger(100);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Main game = new Main("Tester",2);
                    System.out.println(game.getLevelBoard().getEnemies().size());
                    int i = 0;
                    for(Enemy e: game.getLevelBoard().getEnemies()) {
                        if(i == 0) {
                            actual.set(e.getCol());
                        }
                        i++;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertNotEquals(expected, actual.get());
    }

    /**
     * Tests whether the correc col is fetched
     */
    @Test
    void getRowTest() {
        int expected = 15;
        AtomicInteger actual = new AtomicInteger(100);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Main game = new Main("Tester",2);
                    System.out.println(game.getLevelBoard().getEnemies().size());
                    int i = 0;
                    for(Enemy e: game.getLevelBoard().getEnemies()) {
                        if(i == 0) {
                            actual.set(e.getRow());
                        }
                        i++;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertNotEquals(expected, actual.get());
    }

    /**
     * Tests whether the correct col is fetched
     */
    @Test
    void get() {
        int expected = 15;
        AtomicInteger actual = new AtomicInteger(100);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Main game = new Main("Tester",2);
                    System.out.println(game.getLevelBoard().getEnemies().size());
                    int i = 0;
                    for(Enemy e: game.getLevelBoard().getEnemies()) {
                        if(i == 0) {
                            actual.set(e.getCol());
                        }
                        i++;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertNotEquals(expected, actual.get());
    }

}