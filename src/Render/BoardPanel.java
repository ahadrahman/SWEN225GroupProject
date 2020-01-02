package Render;

import Maze.Player;
import Maze.Tile;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * @author Daniel Pullon
 * The panel that draws the game
 */
public class BoardPanel extends JPanel {
    /*** The board. */
    private Tile[][] board;
    /*** The board labels. */
    public final TilePanel[][] boardLabels;
    /*** Display size. */
    public static final int DISPLAY_SIZE = 9;
    /*** The player. */
    private Player player;
    /*** The max coordinate i.e. width/height. */
    public final static int MAX = 32;
    /*** The min coordinate. */
    public final static int MIN = 0;


    /**
     * Creates a new board panel
     * @param board the board to draw
     * @param player the player
     */
    public BoardPanel(Tile[][] board, Player player) {
        this.board = board;
        this.player = player;
        boardLabels = new TilePanel[board.length][board[0].length];

        setLayout(new GridLayout(DISPLAY_SIZE, DISPLAY_SIZE));
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                boardLabels[row][col] = new TilePanel(board[row][col]); // Makes the label, gives it the image for the tile
            }

        }
        createBorder();
        redraw();

    }

    /**
     * Creates a new border.
     */
    public void createBorder() {
        Border blackline = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        setBorder(blackline);
    }

    /**
     * Goes through each tile in the array and gets the correct image for that tile.
     */
    public void redraw() {
        int playerRow = player.getCurrentPos().getRow();
        int playerCol = player.getCurrentPos().getCol();

        int minRow = Math.max(playerRow - (DISPLAY_SIZE - 1) / 2,  MIN);
        int minCol = Math.max(playerCol - (DISPLAY_SIZE - 1) / 2, MIN);

        int maxRow = Math.min(minRow + DISPLAY_SIZE, MAX);
        if (maxRow == MAX)
            minRow = MAX - DISPLAY_SIZE;
        int maxCol = Math.min(minCol + DISPLAY_SIZE, MAX);
        if (maxCol == MAX)
            minCol = MAX - DISPLAY_SIZE;
        for (int row = minRow; row < maxRow; row++) {
            for (int col = minCol; col < maxCol; col++) {
                boardLabels[row][col].repaint();
                add(boardLabels[row][col]);
            }
        }
        revalidate();
        repaint();
    }

    /**
     * Sets the player.
     * @param p the player
     */
    public void setPlayer(Player p) {
        player = p;
    }

    /**
     * Sets the board.
     * @param board the board
     */
    public void setBoard(Tile[][] board) {
        this.board = board;
    }
}
