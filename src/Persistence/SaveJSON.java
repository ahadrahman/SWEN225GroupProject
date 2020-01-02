package Persistence;

import Maze.Item;
import Maze.LevelBoard;
import Maze.Player;
import Maze.Tile;

import java.io.*;

/**
 * @author Hugh Lockwood - wrote the class initially
 * @author August Bolter - Added code for the replay system
 * Class responsible for saving and loading the game
 */
public class SaveJSON {

    /**
     * Saves the current game state to a .JSON file
     * @param levelBoard the level board to save
     * @param fileName the name of the file ot save
     * @param endFile if the end of the file should be written
     */
    public static void saveGame(LevelBoard levelBoard, String fileName, boolean endFile) {
        Tile[][] levelArray = levelBoard.getBoard();
        File jsonFile;
        jsonFile = new File(fileName);

        try {
            PrintStream out = new PrintStream(jsonFile);
            StringBuilder builder = new StringBuilder();

            // Write the start of the JSON file
            builder.append("{\n\t\"title\": \"Lesson 1\",\n" +
                    "\t\"chips\": " + levelBoard.getTotalChips() + ",\n" +
                    "\t\"timeLimit\" : " + levelBoard.getTimeLimit() + ",\n" +
                    "\t\"tiles\" : [\n");

            for (int row = 0; row < levelArray.length; row++) {
                for (int col = 0; col < levelArray[row].length; col++) {
                    // Get tile descriptions
                    Tile tile = levelArray[row][col];
                    Class<?> clazz = tile.getClass();
                    String[] classComposition = clazz.getName().split("\\.");
                    String type = (classComposition.length == 2) ? classComposition[1] : classComposition[0];


                    String extra = tile.getExtra();

                    // Get item description if the tile has one
                    Item itemObj;
                    String item, itemExtra;
                    String itemString;
                    if (tile.getItems().isEmpty()) {
                        itemObj = null;
                        itemString = null;
                    } else {
                        itemString = "";

                        for (Item itemO : tile.getItems()) {
                            if (itemO == null) continue;
                            itemObj = itemO;

                            String[] itemComposition = itemObj.getClass().getName().split("\\.");
                            item = (itemComposition.length == 2) ? itemComposition[1] : itemComposition[0];
                            itemExtra = (itemObj.getExtra() == null) ? "_" : itemObj.getExtra();
                            if (itemObj instanceof Player) {
                                itemExtra = "";
                                Player player = (Player) itemObj;
                                for (int i = 0; i < player.getInventory().length; i++) {
                                    if (player.getInventory()[i] == null) {
                                        itemExtra += "_>";
                                    } else {
                                        String[] itemExtraComp = player.getInventory()[i].getClass().getName().split("\\.");
                                        String itemComp = itemExtraComp.length==2 ? itemExtraComp[1] : itemExtraComp[0];
                                        itemExtra += itemComp + "/" + ((player.getInventory()[i].getExtra() == null) ? "_" : player.getInventory()[i].getExtra()) + ">";
                                    }
                                }
                                itemExtra = itemExtra.substring(0, itemExtra.length() - 1);
                            }
                            itemString += (item + "|" + itemExtra + ",");
                        }
                        itemString = itemString.substring(0, itemString.length() - 1);
                    }

                    // Print to JSON
                    builder.append(tileAsJSON(type, extra, itemString, row, col));
                }
            }


            // Write the end of the JSON file
            builder.setLength(builder.length() - 2);
            if (endFile) {
                builder.append("\n\t]\n}");
            }
            else {
                builder.append("\n\t]");
            }
            out.print(builder.toString());

            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param type the class
     * @param extra extra descriptor
     * @param itemString item class and descriptor
     * @param row the row
     * @param col the col
     * @return A tile's information as a JSON object string
     */
    public static String tileAsJSON(String type, String extra, String itemString, int row, int col) {
        StringBuilder builder = new StringBuilder();

        builder.append("\t\t{\n\t\t\t\"type\" : \""+ type +"\",\n" +
                "\t\t\t\"row\" : " + row + ",\n" +
                "\t\t\t\"col\" : " + col);

        if (extra != null) {
            builder.append(",\n\t\t\t\"extra\" : \"" + extra + "\"");
        }

        if (itemString != null && !itemString.equals("null|null")) {
            builder.append(",\n\t\t\t\"item\" : {\n\t\t\t\t\"descriptor\" : \"" + itemString + "\"");

            builder.append("\n\t\t\t}");
        }

        builder.append("\n\t\t},\n");

        return builder.toString();
    }

    /**
     * Saves a move the player makes.
     * Used when recording.
     * @param fileName the name of the file
     * @param direction the direction the player moved
     * @param time the time the move was made
     * @param firstMove true if this is the first recorded move
     */
    public static void saveMove(String fileName, LevelBoard.Direction direction, long time, boolean firstMove) {
        try {
            StringBuilder move = new StringBuilder();
            FileWriter fileWriter = new FileWriter(fileName, true);
            BufferedWriter out = new BufferedWriter(fileWriter);
            if (firstMove) {
                move.append(",\n\t\"moves\" : [\n");
            }
            String type = "playerMove";
            String directionText = "";
            if (LevelBoard.Direction.DOWN == direction) {
                directionText = "DOWN";
            }
            else if (LevelBoard.Direction.UP == direction) {
                directionText = "UP";
            }
            else if (LevelBoard.Direction.RIGHT == direction) {
                directionText = "RIGHT";
            }
            else {
                directionText = "LEFT";
            }
            if (!firstMove) {
                move.append(",\n");
            }
            move.append("\t\t{\n\t\t\t\"type\" : \""+ type +"\",\n" +
                    "\t\t\t\"direction\" : \"" + directionText + "\",\n" +
                    "\t\t\t\"time\" : " + time);

            move.append("\n\t\t}");
            out.append(move.toString());
            out.close();
            fileWriter.close();
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to write the end of the file at the end of the recording.
     * @param fileName the name of the file
     * @param firstMove true if this is the first move
     */
    public static void endRecord(String fileName, boolean firstMove) {
        try {
            StringBuilder move = new StringBuilder();
            FileWriter fileWriter = new FileWriter(fileName, true);
            BufferedWriter out = new BufferedWriter(fileWriter);
            if (firstMove) {
                move.append("\n}");
            }
            else {
                move.append("\n\t]\n}");
            }
            out.append(move.toString());
            out.close();
            fileWriter.close();
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
