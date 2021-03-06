package Pieces;

import Game.*;

import java.util.ArrayList;
import java.util.List;

public class NightRider extends Piece {
    private Type type;
    private int[] moves = new int[2];

    /**
     * A Nightrider can travel 1 space one way and 3 spaces another way.
     * @param color determines the color of the piece.
     */
    public NightRider(String color) {
        moves[0] = 1;
        moves[1] = 3;
        type = Type.NIGHTRIDER;
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
        if (outOfBounds(end_row, end_col, board.getBoardSize(), board.getBoardSize())) {
            return false;
        }

        boolean validRow = validRowMove(start_row, end_row);
        boolean validCol = validColMove(start_col, end_col);
        boolean validMove = (Math.abs(start_row-end_row) != Math.abs(start_col-end_col));

        if (!validRow || !validCol || !validMove) {
            return false;
        }

        if (targetIsAlly(board, end_row, end_col)) {
            return false;
        }

        return true;
    }

    /**
     * validRowMove verifies that the piece is moving within its bounds horizontally
     * @param start_row the row the piece is in
     * @param end_row the target row the piece is moving to
     * @return true if valid, else otherwise
     */
    public boolean validRowMove(int start_row, int end_row) {
        if (Math.abs(end_row - start_row) != this.moves[1] && Math.abs(end_row - start_row) != this.moves[0]) {
            return false;
        }
        return true;
    }

    /**
     * validColMove verifies that the piece is moving within its bounds vertically
     * @param start_col the column the piece is in
     * @param end_col the target column the piece is moving to
     * @return true if valid, else otherwise
     */
    public boolean validColMove(int start_col, int end_col) {
        if (Math.abs(end_col - start_col) != this.moves[1] && Math.abs(end_col - start_col) != this.moves[0]) {
            return false;
        }
        return true;
    }

    /**
     * verifyPath checks if the path is a valid path for the piece to move.
     * For the nightrider, this will always return true since it can hop over pieces.
     * @param board the Game board
     * @param start_row the current row of the piece
     * @param start_col the current column of the piece
     * @param end_row the target row of the piece
     * @param end_col the target column of the piece
     * @return
     */
    public boolean verifyPath(Board board, int start_row, int start_col, int end_row, int end_col){
        return true;
    }


    /**
     * canMove checks to see if the nightRider can move at all
     * @param board the game board
     * @return true if the nightRider can move, false otherwise
     */
    public boolean canMove(Board board) {
        int row = this.getRow();
        int col = this.getCol();
        List<Pair> coord = new ArrayList<Pair>();
        coord.add(new Pair(row-1, col-3));
        coord.add(new Pair(row-1, col+3));
        coord.add(new Pair(row-3, col-1));
        coord.add(new Pair(row-3, col+1));
        coord.add(new Pair(row+1, col-3));
        coord.add(new Pair(row+1, col+3));
        coord.add(new Pair(row+3, col-1));
        coord.add(new Pair(row+3, col+1));

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
     * getType returns the type
     * @return the type of the piece
     */
    public Type getType() {
        return Type.NIGHTRIDER;
    }
}
