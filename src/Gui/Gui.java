package Gui;

import Game.Board;
import Game.Tile;
import Pieces.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Gui {

    private final JFrame gameFrame;
    private BoardPanel boardPanel;

    public TextPanel textPanel;

    private static Board chessBoard = new Board();

    private Tile sourceTile;
    private Piece movedPiece;

    private Piece capturedPiece = null;
    private int prevRow = -1;
    private int prevCol = -1;
    private int prevTargetRow = -1;
    private int prevTargetCol = -1;

    private boolean endFlag;
    private int whiteWins = 0;
    private int blackWins = 0;

    String playerOne;
    String playerTwo;

    private Color light = Color.decode("#FFFACD");
    private Color dark = Color.decode("#b77e31");
    private final static int num_tiles = chessBoard.getBoardSize()*chessBoard.getBoardSize();
    private final static int board_length = chessBoard.getBoardSize();
    private final static String iconPath = "src/Icons/";

    private final static Dimension OUTER_FRAME_DIM = new Dimension(600, 600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
    private final static Dimension TEXT_PANEL_DIMENSION = new Dimension(400, 50);

    /**
     * Gui will initially ask for players' names before initializing the game board.
     */
    public Gui() {
        this.gameFrame = new JFrame("Chess");
        playerOne = JOptionPane.showInputDialog(gameFrame,"Enter Player One Name: ", null);
        playerTwo = JOptionPane.showInputDialog(gameFrame,"Enter Player Two Name: ", null);
        playerOne = playerOne.toUpperCase();
        playerTwo = playerTwo.toUpperCase();

        /**
         * Set up outer frame layout
         */
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);

        /**
         * Set up board layout
         */
        this.boardPanel = new BoardPanel();

        /**
         * Set up frame layout (?)
         */
        this.gameFrame.setLayout(new BorderLayout());
        this.gameFrame.setSize(OUTER_FRAME_DIM);
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);

        /**
         * Set up text panels
         */
        textPanel = new TextPanel();
        textPanel.setWhiteTurn();
        this.gameFrame.add(textPanel, BorderLayout.SOUTH);

        startNewGame();
    }

    /**
     * starts a default new game
     */
    public void startNewGame() {
        // reset game to start again
        System.out.println("Restart Regular Game");
        this.endFlag = false;

        // set up board and pieces
        this.chessBoard = new Board();
        this.chessBoard.setChessPieces();
        this.gameFrame.setVisible(true);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                boardPanel.drawBoard(chessBoard);
                textPanel.drawTextPanel();
            }
        });
    }

    /**
     * initializes a new chess game with custom pieces included
     */
    public void startSpecialGame() {
        System.out.println("Restart Special Game");
        // start new game
        this.endFlag = false;

        // set up board and pieces
        this.chessBoard = new Board();
        this.chessBoard.setSpecialPieces();
        this.boardPanel = new BoardPanel();
        this.gameFrame.setVisible(true);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                boardPanel.drawBoard(chessBoard);
                textPanel.drawTextPanel();
            }
        });
    }

    /**
     * shows wins for each player
     */
    public void showScore() {
        this.textPanel.showScore();
        this.gameFrame.add(this.textPanel, BorderLayout.SOUTH);
        this.gameFrame.setVisible(true);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                boardPanel.drawBoard(chessBoard);
                textPanel.drawTextPanel();
            }
        });
    }

    /**
     * Player that does not forfeit 'wins' the game.
     */
    public void forfeitGame() {
        endFlag = true;
        if (chessBoard.getCurrentPlayer().getPlayerColor().equals("WHITE")) {
            blackWins++;
            this.textPanel.setForfeitWhite();
        }
        else {
            whiteWins++;
            this.textPanel.setForfeitBlack();
        }
        this.gameFrame.add(this.textPanel, BorderLayout.SOUTH);
        this.gameFrame.setVisible(true);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                boardPanel.drawBoard(chessBoard);
                textPanel.drawTextPanel();
            }
        });
    }

    /**
     * Players are allowed to undo their most recent move.
     * Once their opponent moves, they can no longer undo.
     */
    public void undoMove() {
        if (prevRow != -1) {
            Piece p = chessBoard.getPiece(prevTargetRow, prevTargetCol);
            chessBoard.getTile(prevRow, prevCol).setPiece(p);
            chessBoard.getTile(prevTargetRow, prevTargetCol).removePiece();
            if (capturedPiece != null) {
                chessBoard.getTile(prevTargetRow, prevTargetCol).setPiece(capturedPiece);
            }
            chessBoard.updateCurrentPlayer();
            setTextStatus();
            prevRow = -1;
            prevCol = -1;
            prevTargetRow = -1;
            prevTargetCol = -1;
            capturedPiece = null;
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                boardPanel.drawBoard(chessBoard);
                textPanel.drawTextPanel();
            }
        });
    }

    /**
     * table menu bar for the gui
     * @return the File and Options tabs
     */
    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createOptionsMenu());
        return tableMenuBar;
    }

    /**
     * Undo and Forfeit are underneath the Options tab
     * @return the Undo and Forfeit functionalities
     */
    private JMenu createOptionsMenu() {
        final JMenu optionsMenu = new JMenu("Options");
        final JMenuItem undoMenuItem = new JMenuItem("Undo");
        undoMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undoMove();
            }
        });
        final JMenuItem forfeitMenuItem = new JMenuItem("Forfeit");
        forfeitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                forfeitGame();
            }
        });

        optionsMenu.add(undoMenuItem);
        optionsMenu.add(forfeitMenuItem);
        return optionsMenu;
    }

    /**
     * Exit, New Game, New Special (Game), and Score are inside the File tab
     * @return the items under the File tab
     */
    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");

        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        final JMenuItem newGameMenuItem = new JMenuItem("New Game");
        newGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewGame();
            }
        });

        final JMenuItem newSpecialMenuItem = new JMenuItem("New Special");
        newSpecialMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSpecialGame();
            }
        });

        final JMenuItem showScoreMenuItem = new JMenuItem("Score");
        showScoreMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showScore();
            }
        });

        fileMenu.add(newGameMenuItem);
        fileMenu.add(newSpecialMenuItem);
        fileMenu.add(showScoreMenuItem);
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }

    /**
     * TextPanel is used to display current status of a game.
     */
    private class TextPanel extends JPanel {
        private JLabel text;

        public void drawTextPanel() {
            removeAll();
            add(this.text);
            validate();
            repaint();
        }

        TextPanel() {
            super(new GridLayout(1,2));
            text = new JLabel();
            add(text);
            text.setHorizontalAlignment(SwingConstants.CENTER);
            setPreferredSize(TEXT_PANEL_DIMENSION);
            validate();
        }

        /**
         * Player One's turn
         */
        public void setWhiteTurn() { this.text.setText(playerOne + "'S TURN"); }
        /**
         * Player Two's turn
         */
        public void setBlackTurn() {
            this.text.setText(playerTwo + "'S TURN");
        }
        /**
         * Player One's turn because of an invalid move
         */
        public void setWhiteTurnInvalidMove() {
            this.text.setText("INVALID MOVE. " + playerOne + "'S TURN");
        }
        /**
         * Player Two's turn because of an invalid move
         */
        public void setBlackTurnInvalidMove() {
            this.text.setText("INVALID MOVE. " + playerTwo + "'S TURN");
        }

        /**
         * Player One is in check
         */
        public void setWhiteCheck() {
            this.text.setText("CHECK ON " + playerOne + ". " + playerOne + "'S TURN");
        }

        /**
         * Player Two is in check
         */
        public void setBlackCheck() {
            this.text.setText("CHECK ON " + playerTwo + ". " + playerTwo + "'S TURN");
        }

        /**
         * Player One is in checkmate and loses the game
         */
        public void setWhiteCheckMate() {
            this.text.setText("CHECKMATE. " + playerTwo + " WINS");
        }
        /**
         * Player Two is in checkmate and loses the game
         */
        public void setBlackCheckMate() {
            this.text.setText("CHECKMATE. " + playerOne + " WINS");
        }

        /**
         * Players are at a stalemate
         */
        public void setStaleMate() {
            this.text.setText("STALEMATE.");
        }

        /**
         * Display the score between the two players
         */
        public void showScore() {
            this.text.setText(playerOne + " WINS: " + whiteWins + ", " + playerTwo + " WINS: " + blackWins);
        }

        /**
         * Player Two forfeits, and Player One wins
         */
        public void setForfeitBlack() {
            this.text.setText(playerTwo + " FORFEITS. " + playerOne + " WINS.");
        }

        /**
         * Player One forfeits, and Player Two wins
         */
        public void setForfeitWhite() {
            this.text.setText(playerOne + " FORFEITS. " + playerTwo + " WINS.");
        }
    }

    /**
     * BoardPanel consists of tiles, where each tile can contain a chess piece
     */
    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;

        BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();

            for (int i = 0; i < num_tiles; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        /**
         * renders the board and fills in the tiles with pieces
         * @param board the board to fill in with tiles
         */
        public void drawBoard(final Board board) {
            System.out.println("DrawBoard");
            removeAll();
            for(final TilePanel tilePanel: boardTiles) {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            repaint();
            revalidate();
        }
    }

    /**
     * Checks and updates status of the game
     */
    public void setTextStatus() {
        if (chessBoard.checkMate(chessBoard, "WHITE", "BLACK")) {
            textPanel.setWhiteCheckMate();
            endFlag = true;
            this.blackWins++;
        }
        else if (chessBoard.checkMate(chessBoard, "BLACK", "WHITE")) {
            textPanel.setWhiteCheckMate();
            endFlag = true;
            this.blackWins++;
        }
//        if (chessBoard.whiteCheckMate(chessBoard)) {
//            textPanel.setWhiteCheckMate();
//            endFlag = true;
//            this.blackWins++;
//        }
//        else if (chessBoard.blackCheckMate(chessBoard)) {
//            textPanel.setBlackCheckMate();
//            endFlag = true;
//            this.whiteWins++;
//        }
        else if (chessBoard.blackStaleMate(chessBoard) || chessBoard.whiteStaleMate(chessBoard)) {
            textPanel.setStaleMate();
            endFlag = true;
        }
        else if (chessBoard.check(chessBoard, "BLACK", "WHITE")) {
            textPanel.setBlackCheck();
        }
        else if (chessBoard.check(chessBoard, "WHITE", "BLACK")) {
            textPanel.setWhiteCheck();
        }
        else if (chessBoard.getCurrentPlayer().getPlayerColor().equals("BLACK")) {
            textPanel.setBlackTurn();
        }
        else {
            textPanel.setWhiteTurn();
        }
    }

    /**
     * TilePanel is one grid in the board.
     * They have a tile color and a tile ID
     * There is a mouse listener that will determine a player's move
     */
    private class TilePanel extends JPanel {
        private final int tileId;

        TilePanel(final BoardPanel boardPanel, final int tileId) {
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessBoard);

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (!endFlag) {

                        if (isRightMouseButton(e)) {
                            sourceTile = null;
                            movedPiece = null;
                        } else if (isLeftMouseButton(e)) {
                            if (sourceTile == null) {
                                int row = tileId / board_length;
                                int col = tileId % board_length;
                                sourceTile = chessBoard.getTile(row, col);
                                movedPiece = sourceTile.getPiece();
                                prevRow = row;
                                prevCol = col;
                                if (movedPiece == null) {
                                    sourceTile = null;
                                }
                            } else {
                                int row = tileId / board_length;
                                int col = tileId % board_length;
                                prevTargetRow = row;
                                prevTargetCol = col;
                                capturedPiece = chessBoard.getPiece(row,col);
                                if (movedPiece.getRow() != row || movedPiece.getCol() != col) {
                                    chessBoard.getCurrentPlayer().movePiece(chessBoard, movedPiece, row, col);
                                    if (movedPiece.getRow() == row && movedPiece.getCol() == col) {
                                        chessBoard.updateCurrentPlayer();
                                        setTextStatus();
                                    } else {
                                        if (chessBoard.getCurrentPlayer().getPlayerColor().equals("BLACK")) {
                                            textPanel.setBlackTurnInvalidMove();
                                        } else {
                                            textPanel.setWhiteTurnInvalidMove();
                                        }
                                    }
                                } else {
                                    if (chessBoard.getCurrentPlayer().getPlayerColor().equals("BLACK")) {
                                        textPanel.setBlackTurnInvalidMove();
                                    } else {
                                        textPanel.setWhiteTurnInvalidMove();
                                    }
                                }
                                sourceTile = null;
                                movedPiece = null;
                            }
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    boardPanel.drawBoard(chessBoard);
                                }
                            });
                        }
                    }
                }
                @Override
                public void mousePressed(final MouseEvent e) {
                }
                @Override
                public void mouseReleased(final MouseEvent e) {
                }
                @Override
                public void mouseEntered(final MouseEvent e) {
                }
                @Override
                public void mouseExited(final MouseEvent e) {
                }
            });

            validate();
        }

        /**
         * assigns the tile color and tile piece if needed
         * @param board
         */
        public void drawTile(final Board board) {
            assignTileColor();
            assignTilePieceIcon(board);
            validate();
            repaint();
        }

        /**
         * assigns a tile with an chess piece icon
         * @param board
         */
        private void assignTilePieceIcon(final Board board) {
            this.removeAll();
            int row = tileId / board_length;
            int col = tileId % board_length;
            if (board.getTile(row, col).getOccupation()) {
                try {
                    String type = board.getTile(row, col).getPiece().getType().toString();
                    String color = board.getTile(row, col).getPiece().getPieceColor();
                    final BufferedImage image = ImageIO.read(new File(iconPath + color + type + ".png"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * assigns the Tile color
         */
        private void assignTileColor() {
            setBackground(light);
            int row = (tileId / board_length);
            int col = (tileId % board_length);
            if ((row % 2 == 0 && col % 2 == 0) || (row % 2 == 1 && col % 2 == 1)) {
                setBackground(light);
            } else {
                setBackground(dark);
            }
        }
    }
}