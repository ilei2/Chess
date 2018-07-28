package Pieces;

import Game.*;
import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece{
    private Type type;
    private int maxMoves = 8;

    public Bishop(String color) {
        type = Type.BISHOP;
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
        boolean diagonal = isDiagonalMovement(start_row, start_col, end_row, end_col);
        boolean validPath = verifyPath(board, start_row, start_col, end_row, end_col);

        if (!diagonal || !validPath) {
            return false;
        }
        if (targetIsAlly(board, end_row, end_col)){
            return false;
        }
        return true;
    }

    /**
     * checks to see if the bishop is moving in a diagonal path
     * @param start_row the row position of the bishop
     * @param start_col the column position of the bishop
     * @param end_row the target row position
     * @param end_col the target column position
     * @return true if valid diagonal path, false otherwise
     */
    public boolean isDiagonalMovement(int start_row, int start_col, int end_row, int end_col) {
        int verticalDiff = Math.abs(start_row-end_row);
        int horizontalDiff = Math.abs(start_col-end_col);
        return(verticalDiff == horizontalDiff && verticalDiff != 0);
    }

    /**
     * verifyPath checks if the path is a valid path for the piece to move
     * @param board the Game board
     * @param start_row the current row of the piece
     * @param start_col the current column of the piece
     * @param end_row the target row of the piece
     * @param end_col the target column of the piece
     * @return true if valid path, false otherwise
     */
    public boolean verifyPath(Board board, int start_row, int start_col, int end_row, int end_col){
        if (outOfBounds(end_row, end_col, board.getBoardSize(), board.getBoardSize())) {
            return false;
        }
        int shift = 1;

        int top = start_row < end_row ? start_row : end_row;
        int bottom = start_row < end_row ? end_row : start_row;

        int horizontalStart = 0;
        int horizontalEnd = 0;
        if (top == start_row) {
            horizontalStart = start_col;
            horizontalEnd = end_col;
        }
        else {
            horizontalStart = end_col;
            horizontalEnd = start_col;
        }

        if (horizontalEnd < horizontalStart){
            shift = -1;
        }

        int colIndex = horizontalStart;
        for (int i = top; i < bottom; i++) {
            if (i != top) {
                Tile tile = board.getTile(i,colIndex);
                if (tile.getOccupation()) {
                    return false;
                }
            }
            colIndex += shift;
            if (colIndex < 0 || colIndex >= board.getBoardSize()) {
                return false;
            }
        }
        return true;
    }

    /**
     * checks if the bishop can move at all
     * @param board the game board
     * @return true if the bishop can move, false otherwise
     */
    public boolean canMove(Board board) {
        int row = this.getRow();
        int col = this.getCol();
        List<Pair> coord = new ArrayList<Pair>();
        coord.add(new Pair(row-1, col-1));
        coord.add(new Pair(row-1, col+1));
        coord.add(new Pair(row+1, col-1));
        coord.add(new Pair(row+1, col+1));

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
     * returns the type of the piece
     * @return the type of the piece
     */
    public Type getType() {
        return Type.BISHOP;
    }
}
