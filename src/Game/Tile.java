package Game;

import Pieces.Piece;

/**
 * Tile has the row and column coordinate that indicates its position on the board.
 * It has a boolean tile_Is_Occupied that indicates whether or not the tile is occupied.
 */
public class Tile {

    private int tile_row;
    private int tile_col;
    private boolean tile_Is_Occupied;
    private Piece piece;
    private String color;

    public Tile(int x, int y) {
        this.tile_row = x;
        this.tile_col = y;
        this.tile_Is_Occupied = false;
        this.piece = null;
    }

    /**
     * setTileColor sets the tile to a certain color
     * @param tileColor the color to set the tile to
     */
    public void setTileColor(String tileColor) {
        this.color = tileColor;
    }

    /**
     * getTileColor returns the color of the tile
     * @return the tile's color
     */
    public String getTileColor() {
        return this.color;
    }

    /**
     * setPiece sets a piece on the tile
     * @param piece the piece to set on the tile
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
        this.setOccupation();
        piece.setLocation(tile_row, tile_col);
    }

    /**
     * removePiece removes the piece from the tile
     */
    public void removePiece() {
        this.piece = null;
        this.freeOccupation();
    }

    /**
     * getPiece returns the piece on the tile
     * @return the piece, or null if there is no piece
     */
    public Piece getPiece() {
        return this.piece;
    }

    /**
     * getRow gets the row of the tile
     * @return the row of the tile
     */
    public int getRow() {
        return this.tile_row;
    }

    /**
     * getColumn gets the row of the tile
     * @return the column of the tile
     */
    public int getColumn(){
        return this.tile_col;
    }

    /**
     * getOccupation returns whether or not the tile is occupied
     * @return true if the tile is occupied, false if the tile is available
     */
    public boolean getOccupation() {
        return this.tile_Is_Occupied;
    }

    /**
     * setOccupation sets the tile to be occupied
     */
    private void setOccupation() {
        this.tile_Is_Occupied = true;
    }

    /**
     * freeOccupation sets the tile to be available
     */
    public void freeOccupation() {
        this.tile_Is_Occupied = false;
    }

}
