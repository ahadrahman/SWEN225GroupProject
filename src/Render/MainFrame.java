package Render;

import Application.Main;
import Maze.LevelBoard;
import Persistence.SaveJSON;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Daniel Pullon
 * @author August Bolter
 * The main frame that contains other panels.
 */
public class MainFrame extends JFrame implements KeyListener, WindowListener, ActionListener {
    private Main game;
    private JPanel outerpanel;
    private BoardPanel boardpanel;
    private InfoPanel infoPanel;
    private ReplayPanel replayPanel;

    private Set<Integer> pressedKeys;
    private boolean paused = false;

    private JMenuItem save, level1, level2, pause, resume, quit;

    /**
     * Creates a new main frame.
     * @param game the game to represent
     */
    public MainFrame(Main game){
        super("Chip's Challenge");

        new TitleFrame(this);

        setVisible(false);
        this.game = game;

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // Makes the program have exit dialogue instead
        addWindowListener(this);

        outerpanel = new JPanel();

        setContentPane(outerpanel);
        addBoardPanel();
        addInfoPanel();
        addReplayPanel();

        // Setup key listener
        addKeyListener(this);
        setFocusable(true);
        requestFocus();

        createMenuBar();

        outerpanel.setLayout(new GridBagLayout());
        outerpanel.setBackground(new Color(0, 100, 0));

        setPreferredSize(new Dimension(1000, 800));

        pack();
        setLocationRelativeTo(null);
        repaint();

        pressedKeys = new HashSet<>();
    }

    /**
     * Constructor for the test cases
     * @param game the main game
     */
    public MainFrame(Main game, String tester){
        super("Chip's Challenge");

        //TitleFrame title = new TitleFrame(this);

        setVisible(true);
        this.game = game;

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // Makes the program have exit dialogue instead
        addWindowListener(this);

        outerpanel = new JPanel();

        setContentPane(outerpanel);
        addBoardPanel();
        addInfoPanel();
        addReplayPanel();

        // Setup key listener
       addKeyListener(this);
        setFocusable(true);
        requestFocus();

        createMenuBar();

        outerpanel.setLayout(new GridBagLayout());
        outerpanel.setBorder(new GameBorder(Color.BLUE));

        pack();
        setLocationRelativeTo(null);
        repaint();
        setResizable(false);

        pressedKeys = new HashSet<>();
    }

    private void createMenuBar() {
        JMenuBar menu = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        save = new JMenuItem("Save");
        level1 = new JMenuItem("Level 1");
        level2 = new JMenuItem("Level 2");
        pause = new JMenuItem("Pause");
        resume = new JMenuItem("Resume");
        quit = new JMenuItem("Quit");

        save.addActionListener(this);
        level1.addActionListener(this);
        level2.addActionListener(this);
        pause.addActionListener(this);
        resume.addActionListener(this);
        quit.addActionListener(this);

        menu.add(gameMenu);

        gameMenu.add(save);
        gameMenu.add(level1);
        gameMenu.add(level2);
        gameMenu.add(pause);
        gameMenu.add(resume);
        gameMenu.add(quit);

        setJMenuBar(menu);
    }

    private void addBoardPanel() {
        boardpanel = new BoardPanel(game.getLevelBoard().getBoard(), game.getPlayer());
        outerpanel.add(boardpanel);
    }

    private void addInfoPanel() {
        infoPanel = new InfoPanel(this);
        outerpanel.add(infoPanel);
    }

    private void addReplayPanel() {
        replayPanel = new ReplayPanel(this);
        infoPanel.addReplayPanel(replayPanel);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        // Unimplemented
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        pressedKeys.add(keyCode);
        LevelBoard.Direction direction;

        switch (keyCode) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                direction = LevelBoard.Direction.UP;
                break;
            case KeyEvent.VK_S:
                if (pressedKeys.contains(KeyEvent.VK_CONTROL)) {
                    save();
                    return;
                }
            case KeyEvent.VK_DOWN:
                direction = LevelBoard.Direction.DOWN;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                direction = LevelBoard.Direction.LEFT;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                direction = LevelBoard.Direction.RIGHT;
                break;
            case KeyEvent.VK_X:
                // Opens the exit dialogue
                if (pressedKeys.contains(KeyEvent.VK_CONTROL)) {
                    quit();
                }
                return;
            case KeyEvent.VK_R:
                // Resumes a saved game
                if (pressedKeys.contains(KeyEvent.VK_CONTROL)) {
                    restart(-1);
                }
                return;
            case KeyEvent.VK_P:
                // Pauses the game
                if (pressedKeys.contains(KeyEvent.VK_CONTROL)) {
                    pause();
                }
                return;
            case KeyEvent.VK_1:
                // Starts level 1
                if (pressedKeys.contains(KeyEvent.VK_CONTROL)) {
                    restart(1);
                }
                return;
            case KeyEvent.VK_2:
                // Starts level 2
                if (pressedKeys.contains(KeyEvent.VK_CONTROL)) {
                    restart(2);
                }
                return;
            case KeyEvent.VK_SPACE:
                // Pauses the game
                pause();
                paused = true;
                displayInfo("Paused. Press esc to resume.");
                return;
            case KeyEvent.VK_ESCAPE:
                // Closes the pause dialog
                resume();
                paused = false;
                return;

            default:
                return;
        }

        if (!paused)
            game.doMove(direction);
    }

    /**
     * Redraws the main frame.
     */
    public void redraw() {
        outerpanel.remove(boardpanel);
        outerpanel.remove(infoPanel);
        boardpanel = new BoardPanel(game.getLevelBoard().getBoard(), game.getPlayer());
        infoPanel = new InfoPanel(this);


        outerpanel.add(boardpanel);
        outerpanel.add(infoPanel);
        infoPanel.addReplayPanel(replayPanel);

        revalidate();
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        pressedKeys.remove(keyEvent.getKeyCode());
    }

    /**
     * Creates an info popup box
     * @param info the information to display
     */
    public void displayInfo(String info) {
        JOptionPane.showMessageDialog(this, info);
    }

    /**
     * @return the board panel
     */
    public BoardPanel getBoardPanel() {
        return boardpanel;
    }

    /**
     * @return the info panel
     */
    public InfoPanel getInfoPanel() {
        return infoPanel;
    }

    /**
     * Saves the game.
     */
    public void save() {
        SaveJSON.saveGame(game.getLevelBoard(), "src/Utility/save.json", true);
    }

    /**
     * Quits the game.
     */
    public void quit() {
        int confirmed = JOptionPane.showConfirmDialog(null, "Exit Program?","EXIT",JOptionPane.YES_NO_OPTION);
        if(confirmed == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    /**
     * Resumes the game.
     */
    public void resume() {
        game.resume();
    }

    /**
     * Resume a given level
     * @param level the number of the level to restart
     */
    public void restart(int level) {
        game.restartLevel(Optional.of(level));
    }

    /**
     * Pauses the game.
     */
    public void pause() {
        game.setPaused();
    }

    /**
     * Sets the fra,e as visible so it is shown.
     */
    public void showFrame() {
        setVisible(true);
    }

    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {
        quit();
    }

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(quit)) {
            quit();
        } else if (e.getSource().equals(save)) {
            save();
        } else if (e.getSource().equals(level1)) {
            restart(1);
        } else if (e.getSource().equals(level2)) {
            restart(2);
        } else if (e.getSource().equals(pause)) {
            pause();
        } else if (e.getSource().equals(resume)) {
            resume();
        }
    }

    /**
     * Creates the interface for changing the speed of the replay.
     */
    public void createChangeSpeedWindow() {
        JDialog changeSpeedWindow = new JDialog();
        JPanel changeSpeedPanel = new JPanel();
        changeSpeedPanel.setLayout(new GridLayout(3, 1));
        JLabel info = new JLabel("Please select a replay speed");
        String[] speeds = {"0.25", "0.5", "1.0"};
        JComboBox<String> speedOptions = new JComboBox<String>(speeds);
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == submitButton) {
                    String selectedSpeedString = (String) speedOptions.getSelectedItem();
                    game.setReplaySpeed(Double.parseDouble(selectedSpeedString));
                    changeSpeedWindow.dispose();
                }
            }
        });
        changeSpeedPanel.add(info);
        changeSpeedPanel.add(speedOptions);
        changeSpeedPanel.add(submitButton);
        changeSpeedWindow.add(changeSpeedPanel);
        changeSpeedWindow.setVisible(true);
        changeSpeedWindow.pack();
    }

    /**
     * @return the game this frame is a part of
     */
    public Main getGame() {
        return game;
    }
}
