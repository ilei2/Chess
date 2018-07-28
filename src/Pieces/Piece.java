package Pieces;
import Game.*;

/**
 * Piece has color, a row, and column attributes.
 * Since each piece has special moves, some methods are abstracted.
 */
public abstract class Piece {
    protected String color;
    protected int row;
    protected int col;

    class Pair {
        int row;
        int col;

        Pair(int row, int col) {
            this.row = row;
            this.col = col;
        }
        public int getRow() {
            return this.row;
        }
        public int getCol() {
            return this.col;
        }
    }

    /**
     * getType returns the type of the piece
     * @return
     */
    public abstract Type getType();

    /**
     * outOfBounds checks to see if the desired move is a valid location on the board.
     * @param tile_row the target row that the piece is going to move to
     * @param tile_col the target column that the piece is going to move to
     * @param max_tile_row the maximum row on the board
     * @param max_tile_col the maximum column on the board
     * @return true if the target row/column is within bounds, false otherwise
     */
    public boolean outOfBounds(int tile_row, int tile_col, int max_tile_row, int max_tile_col) {
        return (tile_row >= max_tile_row || tile_col >= max_tile_col || tile_row < 0 || tile_col < 0);
    }

    /**
     * verifyPath checks to see if the piece can move to a particular destination
     * @param board the Game board
     * @param start_x The original row coordinate of the piece
     * @param start_y The original column coordinate of the piece
     * @param destination_x The target row coordinate of the piece
     * @param destination_y the target column coordinate of the piece
     * @return true if the path is valid, false otherwise
     */
    public abstract boolean verifyPath(Board board, int start_x, int start_y, int destination_x, int destination_y);

    /**
     * movePiece is the method that attempts to move a piece to the target destination
     * @param board the Game board
     * @param start_x The original row coordinate of the piece
     * @param start_y The original column coordinate of the piece
     * @param end_x The target row coordinate of the piece
     * @param end_y The target column coordinate of the piece
     */
    public abstract void movePiece(Board board, int start_x, int start_y, int end_x, int end_y);

    /**
     * getPieceColor returns the color of the piece
     * @return the color of the piece
     */
    public String getPieceColor() {
        return this.color;
    }

    /**
     * setPieceColor sets a piece to a certain color
     * @param pieceColor the desired color to set the piece to
     */
    public void setPieceColor(String pieceColor) {
        this.color = pieceColor;
    }

    /**
     * setLocation sets the piece to a particular row and column
     * @param row the desired row coordinate
     * @param col the desired column coordinate
     */
    public void setLocation(int row, int col) {
        this.row = row;
        this.col = col;
    }


    /*
     *   DELETE THIS FUNCTION LATER!!!!!!!!!!!
     */
    /**
     * verifyCapture checks if the target tile is a valid target, and that the tile contains
     * an enemy piece.
     * @param board the game board
     * @param start_row the current row of the piece trying to attack
     * @param start_col the current column of the piece trying to attack
     * @param end_row the target row of the enemy piece
     * @param end_col the target column of the enemy piece
     * @return true if the tile is valid and contains an enemy piece, false otherwise.
     */
    public boolean verifyCapture(Board board, int start_row, int start_col, int end_row, int end_col) {
        if (outOfBounds(end_row, end_col, board.getBoardSize(), board.getBoardSize())) {
            return false;
        }
        Piece attackingPiece = board.getTile(start_row, start_col).getPiece();
        Tile enemyTile = board.getTile(end_row, end_col);
        if (!enemyTile.getOccupation()) {
            return false;
        }

        Piece enemyPiece = enemyTile.getPiece();
        if (enemyPiece.getPieceColor().equals(attackingPiece.getPieceColor())) {
            return false;
        }
        return true;
    }

    /**
     * targetIsAlly checks if a tile is occupied by an ally piece
     * @param board the game board
     * @param end_row the row position of the target tile
     * @param end_col the column position of the target tile
     * @return true if ally piece occupies the tile, false otherwise
     */
    public boolean targetIsAlly(Board board, int end_row, int end_col) {
        if (outOfBounds(end_row, end_col, board.getBoardSize(), board.getBoardSize())) {
            return false;
        }

        Tile targetTile = board.getTile(end_row, end_col);
        if (targetTile.getOccupation()) {
            String color = targetTile.getPiece().getPieceColor();
            if (color == this.color) {
                return true;
            }
        }
        return false;
    }

    /**
     * canMove determines if a piece can move at all.
     * @param board the game board
     * @return true if a piece can move, false otherwise
     */
    public abstract boolean canMove(Board board);

    public abstract boolean isAValidMove(Board board, int start_row, int start_col, int end_row, int end_col);

    /**
     * getRow returns the row coordinate of the piece
     * @return the row coordinate of the piece
     */
    public int getRow() {
        return this.row;
    }

    /**
     * getCol returns the column coordinate of the piece
     * @return the column coordinate of the piece
     */
    public int getCol() {
        return this.col;
    }
}
