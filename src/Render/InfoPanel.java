package Render;

import Application.Main;
import Maze.FreeTile;
import Maze.Item;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * @author Daniel Pullon
 * Panel that display information about the game such as time left or chips left
 */
public class InfoPanel extends JPanel {
    private JLabel level;
    private JLabel timeRemaining;
    private JLabel chipsRemaining;
    private JPanel inventoryPanel;
    private ReplayPanel replayPanel;

    private int timeLeft, chipsLeft;

    private MainFrame frame;
    private Border etched;

    private TilePanel[] invPanels;
    private Item[] inventory;

    /**
     * Info panel needs:
     * Level label + level number
     * Time + time remaining
     * Chips left
     * Inventory (an array with 8 pos) so it'll be 8 labels that can have
     * an image drawn over them
     * @param frame the frame this panel is a part of
     */
    public InfoPanel(MainFrame frame) {
        this.frame = frame;
        Main game = frame.getGame();
        inventory = game.getPlayer().getInventory();

        setLayout(new GridLayout(4,1));

        float fontSize = 20;
        level = new JLabel("Level: " + game.getLevelBoard().getTitle());
        level.setHorizontalAlignment(JLabel.CENTER);
        level.setFont(level.getFont().deriveFont(fontSize));
        timeRemaining = new JLabel("Time Remaining: " + game.getLevelBoard().getTimeLimit());
        timeRemaining.setFont(timeRemaining.getFont().deriveFont(fontSize));
        timeRemaining.setHorizontalAlignment(JLabel.CENTER);
        timeLeft = game.getLevelBoard().getTimeLimit();
        chipsRemaining = new JLabel("Chips Remaining: " + game.getLevelBoard().getTotalChips());
        chipsRemaining.setHorizontalAlignment(JLabel.CENTER);
        chipsRemaining.setFont(chipsRemaining.getFont().deriveFont(fontSize));
        chipsLeft = game.getLevelBoard().getTotalChips();


        inventoryPanel = new JPanel(new GridLayout(2, 4));
        invPanels = new TilePanel[inventory.length];

        replayPanel = new ReplayPanel(frame);

        etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);

        redraw();
    }

    /**
     * Reduces the timeLeft by one second
     */
    public void decrementTimeRemaining() {
        timeRemaining.setText("Time Remaining: " + --timeLeft);
    }

    /**
     * Reduces the chips left by one
     */
    public void decrementChipsRemaining() {
        chipsRemaining.setText("Chips Remaining: " + --chipsLeft);
    }

    /**
     * Redraws the inventory
     */
    public void redraw() {
        inventoryPanel.removeAll();

        for (int i = 0; i < inventory.length; i++) {
            int row = i / 4 >= 1 ? 1 : 0;
            int col = i % 4;
            invPanels[i] = new TilePanel(new FreeTile(row, col));
            invPanels[i].getTile().addItem(inventory[i]);
            inventoryPanel.add(invPanels[i]);
        }

        JPanel outerInvPanel = new JPanel(new FlowLayout());
        outerInvPanel.add(inventoryPanel);

        add(level);
        add(timeRemaining);
        add(chipsRemaining);
        add(outerInvPanel);

        for (TilePanel tp : invPanels) {
            tp.repaint();
        }

        revalidate();
        repaint();
    }

    /**
     * Adds a replay panel.
     * @param replayPanel the replay panel to add.
     */
    public void addReplayPanel(ReplayPanel replayPanel) {
        setLayout(new GridLayout(5,1));

        add(replayPanel);
    }
}
