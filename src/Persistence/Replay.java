package Persistence;

import Application.Main;
import Maze.LevelBoard;
import Maze.Player;
import Render.ReplayPanel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author August Bolter
 * Class to handle replaying recordings.
 */
public class Replay {
    private ReplayPanel replayPanel; //A panel which holds all the replay buttons
    private Main game;
    private HashMap<Long, ArrayList<String>> tickToMovesMap; //A map linking the nanosecond a move occurs to the move itself

    /**
     * Replays a given record
     * @param game the record to replay
     * @param panel the panel to use
     */
    public Replay(Main game, ReplayPanel panel) {
        this.game = game;
        replayPanel = panel;
    }

    /**
     * Method to actually replay the game
     */
    public void replay() {
        game.setFrameRate(0000000000000000.1); //Stopping the game
        game.setReplay(this);

        File selectedReplay = replayPanel.openFileChooser(); //Opens a file chooser, so the user can pick the replay file
        LevelBoard replayBoard = LoadJSON.loadLevelFromJSON(game.getLevel(), selectedReplay); //Loads the initial board state

        /* Replacing the current game info with the replay file info */
        game.setLevelBoard(replayBoard);
        game.getLevelBoard().setMain(game);

        game.setChipsRemaining(replayBoard.getTotalChips());
        game.setTimeRemaining(replayBoard.getTimeLimit());

        Player p = replayBoard.getPlayer();
        p.setCurrentPos();
        game.setPlayer(p);

        /* The player can't move the character while in replay mode */
        game.getFrame().removeKeyListener(game.getFrame());

        replayPanel.changeButtons();
        replayPanel.addReplayButtons();
        if (!game.getFirstMove()) {
            tickToMovesMap = LoadJSON.loadMoves(selectedReplay);
        }
        game.getFrame().redraw();

    }

    /** Gets the moves map
     * @return the map of ticks to moves
     */
    public HashMap<Long, ArrayList<String>> getTickToMovesMap() {
        return tickToMovesMap;
    }
}
