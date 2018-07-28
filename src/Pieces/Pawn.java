package Pieces;
import Game.*;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece{
    private Type type;
    private int move_vertical;
    private int move_vertical_attack;
    private int move_capture;
    private boolean firstMove;

    /**
     * The pawn has three variables that determine how much it can move in certain directions.
     * On the first move, a pawn can move a maximum of two spaces, which is defined by move_vertical.
     * The boolean firstMove will then update, and from that point on, the pawn moves at most 1 space.
     * @param color determines the pawn's color
     */
    public Pawn(String color) {
        this.move_vertical = 1;
        this.move_vertical_attack = 1;
        this.move_capture = 1;
        this.firstMove = true;
        type = Type.PAWN;
        this.color = color;
    }

    /**
     * updateVerticalMove decreases the Pawn's vertical movement from 2 to 1
     */
    public void updateVerticalMove() {
        if (this.firstMove) {
            this.move_vertical = 1;
            this.firstMove = false;
        }
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
        Tile currentTile = board.getTile(start_row, start_col);
        Piece movingPiece = currentTile.getPiece();
        if (this.isAValidMove(board, start_row, start_col, end_row, end_col)) {
            Tile endTile = board.getTile(end_row, end_col);
            if (endTile.getOccupation()) {
                board.decreasePieceCount(endTile);
                endTile.removePiece();
            }
            endTile.setPiece(movingPiece);
            currentTile.removePiece();
            updateVerticalMove();
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
        boolean canCapture = this.verifyCapture(board, start_row, start_col, end_row, end_col);
        boolean validRow = validRowMove(start_row, end_row);
        boolean validRowAttack = validRowAttack(start_row, end_row);
        boolean validCol = validColMove(start_col, end_col);
        boolean validColCapture = validColCaptureMove(start_col, end_col);
        boolean clearPath = verifyPath(board, start_row, start_col, end_row, end_col);

        if (targetIsAlly(board, end_row, end_col)) {
            return  false;
        }
        if (!canCapture) {
            if (!validRow || !validCol || !clearPath) {
                return false;
            }
        }
        else if (!validRowAttack || !validColCapture) {
            return false;
        }
        return true;
    }

    /**
     * validRowMove verifies that the piece is moving within its bounds horizontally
     * @param start_row the row the piece is in
     * @param end_row the target row the piece is moving to
     * @return
     */
    public boolean validRowMove(int start_row, int end_row) {
        if (this.getPieceColor() == "BLACK") {
            if (start_row == 1) {
                if (end_row < start_row || Math.abs(end_row - start_row) > 2) {
                    return false;
                }
                return true;
            }
            if (end_row < start_row || Math.abs(end_row - start_row) > this.move_vertical) {
                return false;
            }
        }
        if (this.getPieceColor() == "WHITE") {
            if (start_row == 6) {
                if (end_row > start_row || Math.abs(end_row - start_row) > 2) {
                    return false;
                }
                return true;
            }
            if (end_row > start_row || Math.abs(end_row - start_row) > this.move_vertical) {
                return false;
            }
        }
        return true;
    }

    public boolean validRowAttack(int start_row, int end_row) {
        if (this.getPieceColor() == "BLACK") {
            if (end_row < start_row || Math.abs(end_row - start_row) != this.move_vertical_attack) {
                return false;
            }
        }
        if (this.getPieceColor() == "WHITE") {
            if (end_row > start_row || Math.abs(end_row - start_row) != this.move_vertical_attack) {
                return false;
            }
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
        if (start_col != end_col){
            return false;
        }
        return true;
    }

    /**
     * validColCaptureMove checks if the Pawn can capture another piece within its bounds
     * @param start_col the column the piece is in
     * @param end_col the target column the piece is moving to
     * @return true if valid, else otherwise
     */
    public boolean validColCaptureMove(int start_col, int end_col) {
        if (Math.abs(end_col - start_col) != this.move_capture) {
            return false;
        }
        return true;
    }

    /**
     * verifyPath checks if the path is a valid path for the piece to move
     * @param board the Game board
     * @param start_row the current row of the piece
     * @param start_col the current column of the piece
     * @param end_row the target row of the piece
     * @param end_col the target column of the piece
     * @return true if path is valid, false otherwise
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
                if (i != start_row){
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
     * checks if the pawn can move at all
     * @param board the game board
     * @return true if the pawn can move, false otherwise
     */
    public boolean canMove(Board board) {
        int row = this.getRow();
        int col = this.getCol();
        List<Pair> coord = new ArrayList<Pair>();
        coord.add(new Pair(row-1, col-1));
        coord.add(new Pair(row-1, col));
        coord.add(new Pair(row-1, col+1));
        coord.add(new Pair(row+1, col+1));
        coord.add(new Pair(row+1, col));
        coord.add(new Pair(row+1, col-1));

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
        return Type.PAWN;
    }

}
