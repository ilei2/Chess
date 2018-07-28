package Player;
import Game.*;
import Pieces.Piece;

public class Player {
    private String color;
    private int pieces = 18;
    private boolean turn = true;

    public Player(String color) {
        this.color = color;
        if (color.equals("BLACK")) {
            this.turn = false;
        }
    }

    public String getPlayerColor() {
        return this.color;
    }

    public void movePiece(Board board, Piece piece, int end_row, int end_col) {
        if (piece != null && piece.getPieceColor().equals(this.color)) {
            piece.movePiece(board, piece.getRow(), piece.getCol(), end_row, end_col);
        }
    }

    public boolean thisTurn() {
        return this.turn;
    }

    public void updateTurn() {
        this.turn = !this.turn;
    }

}
