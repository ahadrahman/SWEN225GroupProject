package Persistence;

import Application.Main;

/**
 * @author August Bolter
 * Class to record gameplay.
 */
public class Record {
    private boolean isRecording;
    private int count = -1;
    private Main game;
    private String fileName;
    private long finalTime;

    /**
     * Creates a new recording for a given game.
     * @param game the game to record.
     */
    public Record(Main game) {
        this.game = game;
        isRecording = false;
    }

    /** Returns the count (used for naming the record file)
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /** If the game is being recorded
     * @return true if recording, false otherwise
     */
    public boolean isRecording() {
        return isRecording;
    }

    /** Sets if the game is being recorded
     * @param recording sets recording to true or false
     */
    public void setRecording(boolean recording) {
        isRecording = recording;
    }

    /**
     * Method to actually record.
     */
    public void record() {
        count++;
        setRecording(true);
        fileName = "src/Utility/record-" + count + ".json";
        game.recordMoves(true);
        SaveJSON.saveGame(game.getLevelBoard(), fileName, false);
    }

    /** Sets the end time of the recording
     * @param time the end time */
    public void setFinalTime(long time) {
        finalTime = time;
    }

    /** Gets the end time of the recording
     * @return the end time */
    public long getFinalTime() {
        return finalTime;
    }

    /**
     * Method to stop the recording.
     */
    public void stopRecording() {
        setRecording(false);
        game.recordMoves(false);
        SaveJSON.endRecord(fileName, game.getFirstMove());
    }
}
