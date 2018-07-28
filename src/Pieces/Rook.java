package Pieces;

import Game.Board;
import Game.Tile;
import Game.Type;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
    private Type type;
    private int maxMoves = 8;

    public Rook(String color) {
        type = Type.ROOK;
        this.color = color;
    }

    /**
     * Same description as the abstract class Piece.
     * @param board the Game board
     * @param start_row
     * @param start_col
     * @param end_row
     * @param end_col
     */
    public void movePiece(Board board, int start_row, int start_col, int end_row, int end_col) {
        Tile startTile = board.getTile(start_row, start_col);
        Piece movingPiece = startTile.getPiece();
        if (this.isAValidMove(board, start_row, start_col, end_row, end_col)) {
            Tile endTile = board.getTile(end_row, end_col);
            if (endTile.getOccupation()) {
                board.decreasePieceCount(endTile);
                endTile.removePiece();
            }
            endTile.setPiece(movingPiece);
            startTile.removePiece();
        }
    }

    /**
     * isAValidMove checks if the piece can move to the destined row and column coordinates
     * @param board the Game Board
     * @param start_row the row the piece was on
     * @param start_col the column the piece was on
     * @param end_row the target row the piece is moving to
     * @param end_col the target column the piece is moving to
     * @return true if the move is valid, false otherwise
     */
    public boolean isAValidMove(Board board, int start_row, int start_col, int end_row, int end_col) {
        boolean linear = isLinearMovement(start_row, start_col, end_row, end_col);
        boolean validPath = verifyPath(board, start_row, start_col, end_row, end_col);

        if (!linear) {
            return false;
        }

        if (targetIsAlly(board, end_row, end_col)){
            return false;
        }

        if (!validPath) {
            return false;
        }
        return true;
    }

    /**
     * checks if the move is a valid linear move
     * @param start_row the row position of the piece
     * @param start_col the column position of the piece
     * @param end_row the target row position
     * @param end_col the target column position
     * @return true if valid linear move, false otherwise
     */
    public boolean isLinearMovement(int start_row, int start_col, int end_row, int end_col) {
        boolean verticalPath = (Math.abs(start_row-end_row) != 0);
        boolean horizontalPath = (Math.abs(start_col-end_col) != 0);
        if ((verticalPath && horizontalPath) || (!verticalPath && !horizontalPath)) {
            return false;
        }
        return true;
    }

    /**
     * verifyPath checks if the piece can move to the target position
     * @param board the Game board
     * @param start_row the row position of the queen
     * @param start_col the column position of the queen
     * @param end_row the target row position
     * @param end_col the target column position
     * @return true if valid path, false otherwise
     */
    public boolean verifyPath(Board board, int start_row, int start_col, int end_row, int end_col){
        if (outOfBounds(end_row, end_col, board.getBoardSize(), board.getBoardSize())) {
            return false;
        }

        int left = start_col < end_col ? start_col : end_col;
        int right = start_col < end_col ? end_col : start_col;
        int up = start_row < end_row ? start_row : end_row;
        int down = start_row < end_row ? end_row : start_row;

        for(int i = up; i <= down; i++){
            for(int j = left; j <= right; j++){
                if ((i != up || j != left) && (i != down || j != right)) {
                    Tile tile = board.getTile(i, j);
                    if (tile.getOccupation()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * checks if the rook can move at all
     * @param board the game board
     * @return true if the rook can move, false otherwise
     */
    public boolean canMove(Board board) {
        int row = this.getRow();
        int col = this.getCol();
        List<Pair> coord = new ArrayList<Pair>();

        coord.add(new Pair(row-1, col));
        coord.add(new Pair(row,col-1));
        coord.add(new Pair(row, col+1));
        coord.add(new Pair(row+1, col));

        for(int i=0; i < coord.size(); i++) {
            Pair target = coord.get(i);
            if (!this.isAValidMove(board, row, col, target.getRow(), target.getCol())) {
                coord.remove(target);
                i--;
            }
        }

        if (coord.size() > 0) {
            return true;
        }

        return false;
    }

    /**
     * gets the type of the piece
     * @return the type of the piece
     */
    public Type getType() {
        return Type.ROOK;
    }
}
