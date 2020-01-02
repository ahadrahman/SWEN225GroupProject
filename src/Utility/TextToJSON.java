package Utility;


import Persistence.SaveJSON;

import java.io.*;

/**
 * Takes a text file representing a level of Chip's Challenge and converts it to a JSON file describing the level.
 * Level images are available from https://strategywiki.org/wiki/Chip%27s_Challenge/Levels_1-20#Level_1:_LESSON_1.
 * All levels are 32x32.
 */
class TextToJSON {
    private static final String TEXT_FILENAME = "Level-10.txt";

    /**
     * Converts the text file to a JSON file
     * @param args no arguments
     */
    public static void main(String[] args) {
        if (!TEXT_FILENAME.matches("Level-[0-9]+\\.txt"))
            throw new Error("Incorrect format for filename " + TEXT_FILENAME);

        writeJSON();
    }

    private static void writeJSON() {
        File jsonFile = new File("src/Utility/" + TEXT_FILENAME.substring(0, TEXT_FILENAME.length() - 4) + ".json");

        try {
            BufferedReader in = new BufferedReader(new FileReader("src/Utility/" + TEXT_FILENAME));
            PrintStream out = new PrintStream(jsonFile);
            StringBuilder builder = new StringBuilder();

            // Write the start of the JSON file
            builder.append("{\n\t\"title\": \"Lesson 1\",\n" +
                    "\t\"chips\": 11,\n" +
                    "\t\"timeLimit\" : 100,\n" +
                    "\t\"tiles\" : [\n");

            int row = 0;
            String line;

            while ((line = in.readLine()) != null)  {
                char[] rowTiles = line.toCharArray();

                if (rowTiles.length != 32)
                    throw new Error("Row " + row + " has " + rowTiles.length + " tiles when it should have exactly 32.");

                for (int i = 0; i < rowTiles.length; i++) {
                    String type = "", extra = null, item = null, itemExtra = null;

                    if (rowTiles[i] == '_') {
                        continue;
                    } else if (rowTiles[i] == 'x') {
                        type = "WallTile";
                    } else if (rowTiles[i] == 'r') {
                        type = "FreeTile";
                        item = "Key";
                        itemExtra = "red";
                    } else if (rowTiles[i] == 'g') {
                        type = "FreeTile";
                        item = "Key";
                        itemExtra = "green";
                    } else if (rowTiles[i] == 'b') {
                        type = "FreeTile";
                        item = "Key";
                        itemExtra = "blue";
                    } else if (rowTiles[i] == 'y') {
                        type = "FreeTile";
                        item = "Key";
                        itemExtra = "yellow";
                    } else if (rowTiles[i] == 'e') {
                        type = "DoorTile";
                        extra = "blue";
                    } else if (rowTiles[i] == 'w') {
                        type = "DoorTile";
                        extra = "yellow";
                    } else if (rowTiles[i] == 'n') {
                        type = "DoorTile";
                        extra = "green";
                    } else if (rowTiles[i] == 'd') {
                        type = "DoorTile";
                        extra = "red";
                    } else if (rowTiles[i] == 'o') {
                        type = "FreeTile";
                        item = "Chip";
                    } else if (rowTiles[i] == 'p') {
                        type = "FreeTile";
                        item = "Boots";
                    } else if (rowTiles[i] == 's') {
                        type = "TeleportTile";
                        extra = "16>27";
                    } else if (rowTiles[i] == 't') {
                        type = "TeleportTile";
                        extra = "14>27";
                    } else if (rowTiles[i] == 'z') {
                        type = "FireTile";
                    } else if (rowTiles[i] == 'E') {
                        type = "ExitTile";
                    } else if (rowTiles[i] == 'L') {
                        type = "GateTile";
                    } else if (rowTiles[i] == 'I') {
                        type = "InfoTile";
                        extra = "Hints: Collect Boots to walk on lava. Collect Boxing Gloves to move Venosaur. Watch out for Charizard and his Fireblasts, and avoid moving the moving Blastoise.";
                    } else if (rowTiles[i] == 'C') {
                        type = "FreeTile";
                        item = "Player";
                    } else if (rowTiles[i] == 'R') {
                        type = "FreeTile";
                        item = "RedEnemy";
                        itemExtra = "LEFT";
                    } else if (rowTiles[i] == 'G') {
                        type = "FreeTile";
                        item = "GreenEnemy";
                        itemExtra = "DOWN";
                    } else if (rowTiles[i] == 'B') {
                        type = "FreeTile";
                        item = "BlueEnemy";
                        itemExtra = "DOWN";
                    } else if (rowTiles[i] == '@') {
                        type = "FreeTile";
                        item = "Gloves";
                    } else {
                        System.out.println("Missing character " + rowTiles[i]);
                    }

                    // Print to JSON
                    builder.append(SaveJSON.tileAsJSON(type, extra, item + "|" + itemExtra, row, i));
                }

                row++;
            }

            if (row != 32) {
                jsonFile.delete();
                throw new Error("Wrong number of rows " + TEXT_FILENAME + ". There are " + row + ", there should be exactly 32.");
            }

            // Write the end of the JSON file
            builder.setLength(builder.length() - 2);
            builder.append("\n\t]\n}");
            out.print(builder.toString());

            in.close();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
