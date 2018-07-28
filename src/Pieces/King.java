package Pieces;

import Game.Board;
import Game.Tile;
import Game.Type;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    private Type type;
    private int maxMoves = 1;

    public King(String color) {
        type = Type.KING;
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
     * verifyMove determines if the target position is a valid move
     * @param start_row the row position of the piece
     * @param start_col the column position of the piece
     * @param end_row the target row position
     * @param end_col the target column position
     * @return true if valid move, false otherwise
     */
    public boolean verifyMove(int start_row, int start_col, int end_row, int end_col) {
        int verticalDiff = Math.abs(start_row-end_row);
        int horizontalDiff = Math.abs(start_col-end_col);
        if (verticalDiff > maxMoves || horizontalDiff > maxMoves) {
            return false;
        }
        if (verticalDiff == 0 && horizontalDiff == 0) {
            return false;
        }
        return true;
    }

    /**
     * verifyPath checks for potential pieces blocking the movement of the piece.
     * The King piece is special since it only moves one space in any direction.
     * Therefore, no pieces are blocking any spaces between the king's position and the target position.
     * @param board the Game board
     * @param start_row the row position of the piece
     * @param start_col the column position of the piece
     * @param end_row the target row of the piece
     * @param end_col the target column of the piece
     * @return true
     */
    public boolean verifyPath(Board board, int start_row, int start_col, int end_row, int end_col) {
        return true;
    }

    /**
     * isAValidMove checks to see if the target row and column is a valid tile to move to
     * @param board the game board
     * @param start_row the row position of the piece
     * @param start_col the column position of the piece
     * @param end_row the target row position
     * @param end_col the target column position
     * @return true if valid move, false otherwise
     */
    public boolean isAValidMove(Board board, int start_row, int start_col, int end_row, int end_col) {
        if (outOfBounds(end_row, end_col, board.getBoardSize(), board.getBoardSize())) {
            return false;
        }

        if (!verifyMove(start_row, start_col, end_row, end_col)) {
            return false;
        }

        if (targetIsAlly(board, end_row, end_col)) {
            return false;
        }

        return true;
    }

    /**
     * canMove checks if the king can move at all.
     * @param board the game board
     * @return true if the king can move, false otherwise
     */
    public boolean canMove(Board board) {
        int row = this.getRow();
        int col = this.getCol();
        List<Pair> coord = new ArrayList<Pair>();
        coord.add(new Pair(row-1, col-1));
        coord.add(new Pair(row-1, col));
        coord.add(new Pair(row-1, col+1));
        coord.add(new Pair(row, col-1));
        coord.add(new Pair(row, col+1));
        coord.add(new Pair(row+1, col-1));
        coord.add(new Pair(row+1, col));
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
        return Type.KING;
    }


}
