package Application;

import Maze.*;
import Persistence.LoadJSON;
import Persistence.Record;
import Persistence.Replay;
import Persistence.SaveJSON;
import Render.MainFrame;
import Render.TitleFrame;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Ahad Rahman
 * @author August Bolter
 * @author Justinia Koh
 * @author Hugh Lockwood
 * @author Daniel Pullon
 * Main class responsible for running the game.
 */
public class Main{
    //start of fields
    private int timeRemaining; //Level timer
    private Timer gameloop; //Timer object to ensure the game updates at a constant rate, regardless of the computer the game is running on
    private boolean gameover = false; //boolean the checks if the player has lost 3 levels

    //for actual game play
    private List<Chip> allChips = new ArrayList<Chip>();
    private int chipsRemaining;

    private Random generator = new Random();
    private long seed;

    private int level = 1;

    //for making the board
    private Player player;
    private LevelBoard levelBoard;
    private MainFrame frame;
    private List<Enemy> enemies;

    private boolean waitForRestart = false;
    private boolean paused = false;
    private boolean recordMoves;
    private Record currentRecord;
    private Replay currentReplay;
    private boolean firstMove = true;
    private double frameRate;
    private boolean replayMode = false;
    private long startTime;
    private double replaySpeed;
    ArrayList<Long> executedTimes;
    private long totalStepTime;
    private long lastDiff;


    /**
     * Map from a String of class name to a BufferedImage containing the image associated to that class.
     * This map is used for Tile objects.
     */
    public final Map<String, BufferedImage> tileImages = new HashMap<>();
    /**
     * Map from a String of class name to a BufferedImage containing the image associated to that class.
     * This map is used for Item objects.
     */
    public final Map<String, BufferedImage> itemImages = new HashMap<>();

    /**
     * Constructor used to create levels for the testcases
     * @param tester the tester to use
     * @param level the level to load
     */
    public Main(String tester, int level){
        //setting up the correct level
        initialiseMaps();
        levelBoard = LoadJSON.loadLevelFromJSON(level, null);
        levelBoard.setMain(this);
        this.level = level;
        //setting up players
        player = levelBoard.getPlayer();
        player.setCurrentPos();

        //setting up enemies
        enemies = levelBoard.getEnemies();
        for (Enemy e : enemies){
            e.setCurrentPos();
        }
        levelBoard.setMain(this);

        chipsRemaining = levelBoard.getTotalChips();
        timeRemaining = levelBoard.getTimeLimit();      //level must be completed before this time limit runs out

        frame = new MainFrame(this, "Tester");
        timer();

        seed = System.currentTimeMillis();
        generator.setSeed(seed);
    }

//    private TitleFrame titleFrame;

    /**
     * Creates a new main and sets up a level
     * @param level the number of the level to setup
     */
    public Main(int level) {
        setup(level);
    }
    /**
     * Used to initialise the maps with the correct .png files
     */
    private void initialiseMaps() {
        try {
            // Initialise tileImages
            tileImages.put("FreeTile", ImageIO.read(new File("Resources/floor/FreeTile.png")));
            tileImages.put("ExitTile", ImageIO.read(new File("Resources/floor/ExitTile.png")));
            tileImages.put("WallTile", ImageIO.read(new File("Resources/floor/WallTile.png")));
            tileImages.put("GateTile", ImageIO.read(new File("Resources/floor/GateTile.png")));
            tileImages.put("InfoTile", ImageIO.read(new File("Resources/floor/InfoTile.png")));
            tileImages.put("blueDoor", ImageIO.read(new File("Resources/floor/blueDoor.png")));
            tileImages.put("greenDoor", ImageIO.read(new File("Resources/floor/greenDoor.png")));
            tileImages.put("redDoor", ImageIO.read(new File("Resources/floor/redDoor.png")));
            tileImages.put("yellowDoor", ImageIO.read(new File("Resources/floor/yellowDoor.png")));

            // Initialise itemImages
            itemImages.put("pokeball", ImageIO.read(new File("Resources/items/pokeball.png")));
            itemImages.put("blueKey", ImageIO.read(new File("Resources/items/blueKey.png")));
            itemImages.put("greenKey", ImageIO.read(new File("Resources/items/greenKey.png")));
            itemImages.put("redKey", ImageIO.read(new File("Resources/items/redKey.png")));
            itemImages.put("yellowKey", ImageIO.read(new File("Resources/items/yellowKey.png")));
            itemImages.put("downPlayer", ImageIO.read(new File("Resources/player/down.png")));
            itemImages.put("upPlayer", ImageIO.read(new File("Resources/player/up.png")));
            itemImages.put("leftPlayer", ImageIO.read(new File("Resources/player/left.png")));
            itemImages.put("rightPlayer", ImageIO.read(new File("Resources/player/right.png")));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that setups the actual playing board for a level
     * @param levelX the level to setup
     */
    public void setup(int levelX) {
        // Setting up the correct level
        initialiseMaps();
        levelBoard = LoadJSON.loadLevelFromJSON(levelX, null);
        levelBoard.setMain(this);

        //setting up players
        player = levelBoard.getPlayer();
        player.setCurrentPos();

        // Setting up enemies
        enemies = levelBoard.getEnemies();
        for (Enemy e : enemies){
            e.setCurrentPos();
        }
        levelBoard.setMain(this);

        chipsRemaining = levelBoard.getTotalChips();
        timeRemaining = levelBoard.getTimeLimit();      //level must be completed before this time limit runs out

        frame = new MainFrame(this);
        timer();

        seed = System.currentTimeMillis();
        generator.setSeed(seed);
    }


    /**
     * Method that is called when you want to move in a direction
     * @param direction the direction to move
     * @return true is move is valid else false
     */
    public boolean doMove(LevelBoard.Direction direction){
        //Fetching tiles you are on want to move onto
        Tile currentPos = player.getCurrentPos();
        Tile desiredTile = levelBoard.getTileAtPosition(currentPos, direction);
        Tile newTile = null;
        LevelBoard.Direction oldDirection = player.getDirection();
        if (desiredTile != null) {
            if (desiredTile.isWalkable()) {
                newTile = levelBoard.getTileAtPosition(currentPos, direction);
                player.move(newTile);
            }
            desiredTile.interact();
            player.setDirection(direction);

            //gets each item on the tile and interacts with them
            for (Iterator<Item> iterator = desiredTile.getItems().iterator(); iterator.hasNext();) {
                Item item = iterator.next();
                item.interact();
            }
            if (recordMoves && !replayMode) {
                String fileName = "src/Utility/record-" + currentRecord.getCount() + ".json";
                long time = System.nanoTime()-startTime;
                SaveJSON.saveMove(fileName, direction, time, firstMove);
                firstMove = false;
            }
            return true;
        }
        return false;
    }


    /**
     * Keeps track of the time left and is used to control enemies
     */
    public void timer(){
        frameRate = 6; //Frame rate is 6, i.e. the board updates 6 times per second
        replaySpeed = 1; //The replay speed is 1 (100%) when the replay is loaded
        boolean firstTime = true; //A boolean that is used to get critical info only the first time the play button is pressed
        boolean beenPausedPerm = false; //Determines if the replay has been paused at least once
        boolean beenPaused = false; //Determines if the replay is currently paused
        long lastTick = System.nanoTime();
        executedTimes = new ArrayList<Long>(); //A list of times (of moves) that have been already executed by the replay
        long startReplayTime = 0; //The start time that is determined when the replay is loaded
        long pauseTime = 0; //The start time of the replay pause
        long unpauseTime = 0; //The end time of the replay pause
        long totalPauseTime = 0;
        totalStepTime = 0; //The total step time (time skipped by pressing the next step button)
        int tick = 0; //A tick is 1/6th of a second
        lastDiff = 0; //The last diff (before the game is paused) that is calculated
        long diff = 0; //The time that has elapsed so far for the replay


        while (timeRemaining > 0) {
            if (replayMode & firstTime) { //If we have pressed the play button for the first time
                startReplayTime = System.nanoTime();
                firstTime = false;
            }
            if (!replayMode & !firstTime & !beenPaused) { //If we have pressed the stop button
                beenPausedPerm = true; //Then the replay has been paused at least once
                beenPaused = true;
                setLastDiff(diff);
                pauseTime = System.nanoTime();
            }

            long now = System.nanoTime();

            if (replayMode) { //If we are replaying a record, (play button has been pressed)
                /* In the first iteration get the time of when the unpause happened and calculate the totalPauseTime from this */
                if (diff >= currentRecord.getFinalTime()) {
                    frameRate = 0000000000000000.1;
                }
                if (beenPaused) {
                    unpauseTime = System.nanoTime();
                    totalPauseTime = totalPauseTime + (unpauseTime - pauseTime);
                }
                /* If the game has been paused once then calculate the diff is different then if the game hasn't been paused */
                if (beenPausedPerm) {
                    /* replaySpeed is used to calculate diff, the higher the replay speed, the quicker moves will execute  */
                    diff = (long) (((now - startReplayTime) - (totalPauseTime - totalStepTime))*replaySpeed);
                }
                else {
                    diff = (long) ((now - startReplayTime + totalStepTime)*replaySpeed);
                }
                beenPaused = false;
                /* Iterate through the player moves map */
                if (!firstMove) {
                    for (Map.Entry<Long, ArrayList<String>> entry : currentReplay.getTickToMovesMap().entrySet()) {
                        /*Check if the time the move happened is less than the diff (time elapsed), i.e. the move should be executed
                          and don't execute moves that have already been executed */
                        if (diff > entry.getKey() && !executedTimes.contains(entry.getKey())) {
                            for (String s : entry.getValue()) {
                                replayMove(s); //Execute the move
                            }
                            executedTimes.add(entry.getKey()); //And register that this move has been executed
                        }
                    }
                }
            }


            //Every 1/6 of a second
            if (!paused && now - lastTick > (1000000000/replaySpeed)/frameRate) {

                if (waitForRestart) {
                    reloadLevel();
                }

                lastTick = now;
                tick++;

                //Every second
                if (tick % frameRate == 0) {
                    //Update the InfoPanel information
                    frame.getInfoPanel().decrementTimeRemaining();

                    //Move each enemy
                    for (int i = 0; i < enemies.size(); i++) {
                        enemies.get(i).onTick();
                    }
                    //Update relevant variables
                    timeRemaining--;
                    levelBoard.updateFields();
                }
                if (!replayMode) {
                    frame.redraw();
                }
            }

        }

        frame.displayInfo("Out of time");
        reloadLevel();

    }

    /**
     * Reloads the current level.
     * Used when restarting or switching levels.
     */
    public void reloadLevel() {
        levelBoard = LoadJSON.loadLevelFromJSON(level, null);
        levelBoard.setMain(this);
        player = levelBoard.getPlayer();
        player.setCurrentPos();
        enemies = levelBoard.getEnemies();

        for (Enemy e : enemies){
            e.setCurrentPos();
        }

        frame.redraw();
        chipsRemaining = levelBoard.getTotalChips();
        timeRemaining = levelBoard.getTimeLimit();

        seed = System.currentTimeMillis();
        generator.setSeed(seed);
        waitForRestart = false;
    }

    /**
     * Method used to restart the level. Will occur when the user pressed the restart button
     * @param lvl the level number to restart. If not present the level in the level field is restarte
     */
    public void restartLevel(Optional<Integer> lvl) {
        int level;
        if (lvl.isPresent()) {
            level = lvl.get();
        } else {
            level = this.level;
        }

        this.level = level;

        waitForRestart = true;

    }

    /**
     * Moves to the next level when a level is completed
     */
    public void nextLevel() {
        if (level ==1) {
            level = 2;
            restartLevel(Optional.empty());
        } else {
            new TitleFrame(frame);
            restartLevel(Optional.of(2));
            frame.setVisible(false);
        }
    }

    //start of getter and setters
    private void replayMove(String dir) {
        /* Execute a move with a direction corresponding to the string passed in */
        if (dir.equals("LEFT")) {
            doMove(LevelBoard.Direction.LEFT);
        } else if (dir.equals("RIGHT")) {
            doMove(LevelBoard.Direction.RIGHT);
        } else if (dir.equals("UP")) {
            doMove(LevelBoard.Direction.UP);
        } else {
            doMove(LevelBoard.Direction.DOWN);
        }
        /* Update the board */
        frame.redraw();
    }

    /**
     * @return the current main frame
     */
    public MainFrame getFrame() {
        return frame;
    }

    /**
     * Getter method: fetches the current level board
     * @return level board we are on
     */
    public LevelBoard getLevelBoard() {
        return levelBoard;
    }

    /**
     * Getter method: fetches the remaining chips
     * @return total number of chips remaining
     */
    public int getChipsRemaining() {
        return chipsRemaining;
    }

    /**
     * Getter method: fetches the time left - level must be completed before time runs out
     * @return the time remaining
     */
    public int getTimeRemaining() {
        return timeRemaining;
    }

    /**
     * Getter method: returns whether there are more chips that need to be fetched to complete the method
     * @return true if there are chips remaining in level, else false
     */
    public boolean allChipsCollected(){
        return chipsRemaining == 0;
    }

    /**
     * Getter method: fetches a random integer
     * @return random integer generated
     */
    public int getRandomInt() {
        return generator.nextInt();
    }

    /**
     * Getter method: fetches the current player
     * @return player currently playing
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return the current level
     */
    public int getLevel() {
        return level;

    }

    /**
     * Sets the chips left
     * @param chipsLeft the number of chips left
     */
    public void setChipsRemaining(int chipsLeft) {
        chipsRemaining = chipsLeft;
    }

    /**
     * Sets the time left
     * @param timeLeft the time left in seconds
     */
    public void setTimeRemaining(int timeLeft) {
        timeRemaining = timeLeft;
    }

    /**
     * Sets the player
     * @param p the player
     */
    public void setPlayer(Player p) {
        player = p;
    }


    /**
     * Sets the framerate of the game
     * @param frameRate the framerate in frames per second
     */
    public void setFrameRate(double frameRate) {
        this.frameRate = frameRate;
    }

    /**
     * Pauses the game
     */
    public void setPaused() {
        this.paused = true;
    }

    /**
     * Resumes the game
     */
    public void resume() {
        this.paused = false;
    }

    /**
     * Setter method: changes by decrementing the total number of chips left
     */
    public void decrementChipsRemaining(){
        if (chipsRemaining > 0) {
            chipsRemaining--;
        }
    }


    /**
     * Adds an enemy
     * @param e the enemy to add
     */
    public void addEnemy(Enemy e) {
        enemies.add(e);
    }

    /**
     * Removes an enemy
     * @param e the enemy to remove
     */
    public void removeEnemy(Enemy e) {
        enemies.remove(e);
    }

    /**
     * Main method
     * @param args none
     */
    public static void main(String[] args) {
        new Main(1);
    }

    /**
     * @param b true if recording moves
     */
    public void recordMoves(boolean b) {
        recordMoves = b;
    }

    /**
     * Sets the record object.
     * @param record the object
     */
    public void setRecord(Record record) {
        currentRecord = record;
    }

    /**
     * Sets the replay object.
     * @param replay the object
     */
    public void setReplay(Replay replay) {
        currentReplay = replay;
    }

    /**
     * Sets the level board.
     * @param board the level board object
     */
    public void setLevelBoard(LevelBoard board) {
        levelBoard = board;
    }

    /**
     * Sets replay mode to true or false.
     * @param replayMode true if in replay mode
     */
    public void setReplayMode(boolean replayMode) {
        this.replayMode = replayMode;
    }

    /**
     * Sets the replay speed.
     * @param replaySpeed the replay speed.
     */
    public void setReplaySpeed(double replaySpeed) {
        this.replaySpeed = replaySpeed;
    }

    /**
     * Sets the step time.
     * @param stepTime the time for each step
     */
    public void addStepTime(long stepTime) {
        long amountToAdd = stepTime - lastDiff;
        totalStepTime = totalStepTime + amountToAdd;
    }

    /**
     * Sets the difference.
     * @param diff the difference
     */
    public void setLastDiff(long diff) {
        lastDiff = diff;
    }

    /**
     * Moves to the next step of the replay.
     */
    public void nextStep() {
        SortedSet<Long> sortedMoves = new TreeSet<Long>(currentReplay.getTickToMovesMap().keySet());
        for (long l : sortedMoves) {
            if (!executedTimes.contains(l)) {
                replayMove(currentReplay.getTickToMovesMap().get(l).get(0));
                executedTimes.add(l);
                addStepTime(l);
                break;
            }
        }
    }

    /**
     * Sets the start time in nanoseconds.
     * @param nanoTime the time in nanoseconds.
     */
    public void setStartTime(long nanoTime) {
        startTime = nanoTime;
    }

    /** Gets the last record made from the game
     * */
    public Record getCurrentRecord() {
        return currentRecord;
    }

    /** Gets the start time of the recording
     * @return the start time
     * */
    public long getStartTime() {
        return startTime;
    }

    /** Sets the boolean 'firstMove'
     * @param fMove whether the player has made a move when recording
     *  */
    public void setFirstMove(boolean fMove) {
        firstMove = fMove;
    }

    /** Gets the variable 'firstMove'
     * @return firstMove*/
    public boolean getFirstMove() {
        return firstMove;
    }
}
