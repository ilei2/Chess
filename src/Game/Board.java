package Game;

import Pieces.*;
import Player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import Gui.*;


/**
 * Board consists of a tile board. When there is a new game,
 * pieces can be set on the board.
 */

public class Board {

    private static final int blackPawnIndex = 1;
    private static final int whitePawnIndex = 6;
    private static final int leftRook = 0;
    private static final int rightRook = 7;
    private static final int leftKnight = 1;
    private static final int rightKnight = 6;
    private static final int leftBishop = 2;
    private static final int rightBishop = 5;
    private static final int blackQueen = 3;
    private static final int blackKing = 4;
    private static final int whiteQueen = 3;
    private static final int whiteKing = 4;
    private static final int whiteLeaper = 0;
    private static final int whiteRider = 7;
    private static final int blackLeaper = 7;
    private static final int blackRider = 0;
    private static final int SIZE = 8;
    public static final String black = "BLACK";
    public static final String white = "WHITE";

    public int blackCount = 18;
    public int whiteCount = 18;

    protected List<Piece> blackPieces = new ArrayList<Piece>();
    protected List<Piece> whitePieces = new ArrayList<Piece>();

    Player blackPlayer = new Player("BLACK");
    Player whitePlayer = new Player("WHITE");

    public Tile[][] board = new Tile[SIZE][SIZE];

    /**
     *
     * @return the player who is making a move
     */
    public Player getCurrentPlayer() {
        if (blackPlayer.thisTurn()) {
            return blackPlayer;
        }
        return whitePlayer;
    }

    /**
     * updates the current player's status
     */
    public void updateCurrentPlayer() {
        blackPlayer.updateTurn();
        whitePlayer.updateTurn();
    }

    /**
     * decreases the piece count of a player
     * @param tile the tile that contains a piece that is to be removed
     */
    public void decreasePieceCount(Tile tile) {
        Piece piece = tile.getPiece();
        String color = tile.getPiece().getPieceColor().toString();
//        int row = piece.getRow();
//        int col = piece.getCol();
        if (color == black) {
            blackCount--;
            blackPieces.remove(piece);
        }
        else {
            whiteCount--;
            whitePieces.remove(piece);
        }
    }

    /**
     * printPiece(Tile tile) takes in a tile and gets the piece on the tile.
     * Based on its color and type, the method returns the label of the piece.
     * First letter "B" for Black or "W" for white. Second letter signifies piece Type.
     * @param tile a tile that is occupied and has a piece.
     * @return a string label of the piece
     */
    public static String printPiece(Tile tile) {
        String pieceLabel = "";
        Piece piece = tile.getPiece();
        if (piece.getPieceColor().equals(black)) {
            pieceLabel = pieceLabel + "B";
        }
        else {
            pieceLabel = pieceLabel + "W";
        }

        if (piece.getType() == Type.LEAPER) {
            pieceLabel = pieceLabel + "L";
        }
        if (piece.getType() == Type.NIGHTRIDER) {
            pieceLabel = pieceLabel + "X";
        }
        if (piece.getType() == Type.PAWN) {
            pieceLabel = pieceLabel + "P";
        }
        if (piece.getType() == Type.KNIGHT) {
            pieceLabel = pieceLabel + "N";
        }
        if (piece.getType() == Type.ROOK) {
            pieceLabel = pieceLabel + "R";
        }
        if (piece.getType() == Type.BISHOP) {
            pieceLabel = pieceLabel + "B";
        }
        if (piece.getType() == Type.QUEEN) {
            pieceLabel = pieceLabel + "Q";
        }
        if (piece.getType() == Type.KING) {
            pieceLabel = pieceLabel + "K";
        }
        return pieceLabel;
    }

    /**
     * getBoardSize() returns the size of the game board.
     * @return the size
     */
    public int getBoardSize() {
        return SIZE;
    }

    /**
     * adds black pieces to the board
     * @param i the index of the board
     */
    public void addBlackPieces(int i) {
        blackPieces.add(board[i][leftRook].getPiece());
        blackPieces.add(board[i][rightRook].getPiece());
        blackPieces.add(board[i][leftKnight].getPiece());
        blackPieces.add(board[i][rightKnight].getPiece());
        blackPieces.add(board[i][leftBishop].getPiece());
        blackPieces.add(board[i][rightBishop].getPiece());
        blackPieces.add(board[i][blackQueen].getPiece());
        blackPieces.add(board[i][blackKing].getPiece());
    }

    /**
     * adds white pieces to the board
     * @param i the index of the board
     */
    public void addWhitePieces(int i) {
        whitePieces.add(board[i][leftRook].getPiece());
        whitePieces.add(board[i][rightRook].getPiece());
        whitePieces.add(board[i][leftKnight].getPiece());
        whitePieces.add(board[i][rightKnight].getPiece());
        whitePieces.add(board[i][leftBishop].getPiece());
        whitePieces.add(board[i][rightBishop].getPiece());
        whitePieces.add(board[i][whiteQueen].getPiece());
        whitePieces.add(board[i][whiteKing].getPiece());
    }

    /**
     * this method is only used if the user chooses the Special Game option
     */
    public void removePawns() {
        for(int i=0; i < blackPieces.size(); i++) {
            Piece p = blackPieces.get(i);
            if (p.getRow() == blackPawnIndex && p.getCol() == 0) {
                blackPieces.remove(p);
                i--;
            }
            if (p.getRow() == blackPawnIndex && p.getCol() == 7) {
                blackPieces.remove(p);
                i--;
            }
        }
        for(int i=0; i < whitePieces.size(); i++) {
            Piece p = whitePieces.get(i);
            if (p.getRow() == whitePawnIndex && p.getCol() == 0) {
                whitePieces.remove(p);
                i--;
            }
            if (p.getRow() == whitePawnIndex && p.getCol() == 7) {
                whitePieces.remove(p);
                i--;
            }
        }
    }

    /**
     * special pieces are added to the board if the user chooses the Special Game option
     */
    public void addSpecialPieces() {
        removePawns();
        board[blackPawnIndex][0].removePiece();
        board[blackPawnIndex][0].setPiece(new Leaper(black));
        board[blackPawnIndex][7].removePiece();
        board[blackPawnIndex][7].setPiece(new NightRider(black));
        board[whitePawnIndex][0].removePiece();
        board[whitePawnIndex][0].setPiece(new NightRider(white));
        board[whitePawnIndex][7].removePiece();
        board[whitePawnIndex][7].setPiece(new Leaper(white));
        blackPieces.add(board[blackPawnIndex][0].getPiece());
        blackPieces.add(board[blackPawnIndex][7].getPiece());
        whitePieces.add(board[whitePawnIndex][0].getPiece());
        whitePieces.add(board[whitePawnIndex][7].getPiece());
    }

    /**
     * setPieces places the pieces on the game board
     * @param index the index that determines the row of the placement
     * @param color determines the color of the piece
     */
    public void setPieces(int index, String color) {
        for(int i = 0; i < SIZE; i++) {
            if (i == index) {
                board[i][leftRook].setPiece(new Rook(color));
                board[i][rightRook].setPiece(new Rook(color));
                board[i][leftKnight].setPiece(new Knight(color));
                board[i][rightKnight].setPiece(new Knight(color));
                board[i][leftBishop].setPiece(new Bishop(color));
                board[i][rightBishop].setPiece(new Bishop(color));
                if (color.equals(black)) {
                    board[i][blackQueen].setPiece(new Queen(color));
                    board[i][blackKing].setPiece(new King(color));
                }
                else {
                    board[i][whiteQueen].setPiece(new Queen(color));
                    board[i][whiteKing].setPiece(new King(color));
                }
            }
            if (color.equals(black) && i == index) {
                addBlackPieces(i);
            }
            if (color.equals(white) && i == index) {
                addWhitePieces(i);
            }
        }
    }

    /**
     * getPiece gets the piece from the specified row and column.
     * @param row the row of the desired piece
     * @param col the column of the desired piece
     * @return the piece or mull if the tile is empty
     */
    public Piece getPiece(int row, int col) {
        Tile tile = board[row][col];
        return tile.getPiece();
    }

    /**
     * setPawns initializes the pawns on the board.
     * @param black the string that determines color of chess piece
     * @param white the string that determines color of chess piece
     */
    public void setPawns(String black, String white) {
        for(int i=0; i < SIZE; i++) {
            board[blackPawnIndex][i].setPiece(new Pawn(black));
            board[whitePawnIndex][i].setPiece(new Pawn(white));
            blackPieces.add(board[blackPawnIndex][i].getPiece());
            whitePieces.add(board[whitePawnIndex][i].getPiece());
        }
    }

    /**
     * setChessPieces sets up all chess pieces on the game board.
     */
    public void setChessPieces() {
        setPieces(0, black);
        setPieces(SIZE-1,white);
        setPawns(black, white);
    }

    public void setSpecialPieces() {
        setChessPieces();
        addSpecialPieces();
    }

    /**
     * Constructor Board is initialized with new tiles
     */
    public Board() {
        for(int x = 0; x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {
                board[x][y] = new Tile(x,y);
                if (x%2 == 0 && y%2 == 0) {
                    board[x][y].setTileColor(white);
                }
            }
        }
    }

    /**
     * getTile returns the tile from the desired row and column
     * @param row the desired row
     * @param col the desired column
     * @return
     */
    public Tile getTile(int row, int col) {
        return board[row][col];
    }

    /**
     * printLayout is the temporary format to show players what the chess board looks like.
     * "+" indicates that there is no piece on that tile
     * @param board a board of tiles
     */
    public static void printLayout(Board board) {
        for(int i = 0; i < board.getBoardSize(); i++) {
            for(int j = 0; j < board.getBoardSize(); j++) {
                Tile tile = board.getTile(i,j);
                if (tile.getOccupation()) {
                    System.out.print(printPiece(tile) + " ");
                }
                else {
                    System.out.print(" + ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }


    /**
     * gets the white king for reference
     * @return the white king
     */
    public Piece getWhiteKing() {
        for (Piece p : whitePieces) {
            if (p.getType() == Type.KING) {
                return p;
            }
        }
        return null;
    }

    /**
     * gets the black king for reference
     * @return the black king
     */
    public Piece getBlackKing() {
        for (Piece p : blackPieces) {
            if (p.getType() == Type.KING) {
                return p;
            }
        }
        return null;
    }

    /**
     * the Pair class keeps track of coordinates (row and column)
     */
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
     * updateKingTiles keeps a list of all the possible tiles the king can move to
     * @param kingTiles A list that keeps track of possible tiles the king can move to
     * @param king the king piece
     * @param board the game board
     * @param row the king's row position
     * @param col the king's col position
     * @return the list of all coordinates of potential king moves
     */
    public List<Pair> updateKingTiles(List<Pair> kingTiles, Piece king, Board board, int row, int col) {
        if (validKingTile(king, board, row-1, col-1)) {
            kingTiles.add(new Pair(row-1, col-1));
        }
        if (validKingTile(king, board, row-1, col)) {
            kingTiles.add(new Pair(row-1, col));
        }
        if (validKingTile(king, board, row-1, col+1)) {
            kingTiles.add(new Pair(row-1, col+1));
        }
        if (validKingTile(king, board, row, col-1)) {
            kingTiles.add(new Pair(row, col-1));
        }
        if (validKingTile(king, board, row, col+1)) {
            kingTiles.add(new Pair(row, col+1));
        }
        if (validKingTile(king, board, row+1, col-1)) {
            kingTiles.add(new Pair(row+1, col-1));
        }
        if (validKingTile(king, board, row+1, col)) {
            kingTiles.add(new Pair(row+1, col));
        }
        if (validKingTile(king, board, row+1, col+1)) {
            kingTiles.add(new Pair(row+1, col+1));
        }
        return kingTiles;
    }

    /**
     * getAttackingTiles keeps a list of all the pieces that can check a king
     * @param opponent chess pieces that can check a king
     * @param attackingTiles a list of coordinates of the attacking pieces
     * @param board the game board
     * @param row the row position of the black king
     * @param col the column position of the black king
     * @return a list of all attacking pieces
     */
    public List<Pair> getAttackingTiles (String opponent, List<Pair> attackingTiles, Board board, int row, int col) {
        List<Piece> pieces;
        if (opponent == "WHITE") {
            pieces = whitePieces;
        }
        else {
            pieces = blackPieces;
        }
        for (Piece p : pieces) {
            if (p.isAValidMove(board, p.getRow(), p.getCol(), row, col)) {
                attackingTiles.add(new Pair(p.getRow(), p.getCol()));
            }
        }
        return attackingTiles;
    }

    /**
     * Updates an arrayList containing coordinates of valid tile moves
     * @param tileArray
     * @param tilesToRemove
     * @return
     */
    public List<Pair> updateTileMoves(List<Pair> tileArray, List<Integer> tilesToRemove) {
        for (int i=0; i < tilesToRemove.size(); i++) {
            tileArray.remove(tileArray.get(i));
        }
        return tileArray;
    }


    /**
     * validates if there is a checkmate
     * @param board the chess board
     * @param user the player that could lose the game
     * @param opponent the player that could win the game
     * @return true or false
     */
    public boolean checkMate(Board board, String user, String opponent) {
        if (!check(board, user, opponent)) {
            return false;
        }

        Piece king;
        List<Piece> opponentPieces;
        List<Piece> userPieces;

        if (user == "WHITE") {
            king = getWhiteKing();
            userPieces = whitePieces;
            opponentPieces = blackPieces;
        }
        else {
            king = getBlackKing();
            userPieces = blackPieces;
            opponentPieces = whitePieces;
        }
        int row = king.getRow();
        int col = king.getCol();
        board.getTile(row, col).removePiece();

        List<Pair> kingTiles = new ArrayList<Pair>();
        List<Pair> blockTiles = new ArrayList<Pair>();
        List<Pair> attackingTiles = new ArrayList<Pair>();
        List<Integer> tilesToRemove = new ArrayList<>();

        kingTiles = updateKingTiles(kingTiles, king, board, row, col);
        attackingTiles = getAttackingTiles(opponent, attackingTiles, board, row, col);

        for(int i=0; i < opponentPieces.size(); i++) {
            Piece p = opponentPieces.get(i);
            for(int j=0; j < kingTiles.size(); j++) {
                Pair coord = kingTiles.get(j);
                if (p.isAValidMove(board, p.getRow(), p.getCol(), coord.getRow(), coord.getCol())) {
                    tilesToRemove.add(j);
                    blockTiles.add(coord);
                }
            }
        }

        kingTiles = updateTileMoves(kingTiles, tilesToRemove);
        tilesToRemove.clear();

        for(int i=0; i < userPieces.size(); i++) {
            Piece p = userPieces.get(i);
            if (p.getType() != Type.KING) {
                for (int j = 0; j < blockTiles.size(); j++) {
                    Pair coord = blockTiles.get(j);
                    if (p.isAValidMove(board, p.getRow(), p.getCol(), coord.getRow(), coord.getCol())) {
                        kingTiles.add(coord);
                        tilesToRemove.add(j);
                    }
                }
                blockTiles = updateTileMoves(blockTiles, tilesToRemove);
                tilesToRemove.clear();
            }
            for(int k=0; k < attackingTiles.size(); k++) {
                Pair coord = attackingTiles.get(k);
                if (p.isAValidMove(board, p.getRow(), p.getCol(), coord.getRow(), coord.getCol())) {
                    tilesToRemove.add(k);
                }
            }
            attackingTiles = updateTileMoves(attackingTiles, tilesToRemove);
            tilesToRemove.clear();
        }

        board.getTile(row, col).setPiece(king);

        if (kingTiles.size() == 0 && attackingTiles.size() > 0) {
            return true;
        }

        return false;
    }

    /**
     * validKingTile checks if the king can move to a certain position
     * @param p the chess piece
     * @param board the game board
     * @param row the row position of the king
     * @param col the column position of the king
     * @return true if valid tile, false otherwise
     */
    public boolean validKingTile(Piece p, Board board, int row, int col) {
        if (p.outOfBounds(row, col, board.getBoardSize(), board.getBoardSize())) {
            return false;
        }
        if (board.getTile(row, col).getOccupation()) {
            return false;
        }
        return true;
    }

    /**
     * validates whether a chess piece can capture the king
     * @param board
     * @param opponent
     * @param row
     * @param col
     * @param attack
     * @return true or false
     */
    public List<Boolean> validateAttacks(Board board, String opponent, int row, int col, List<Boolean> attack) {
        List<Piece> opponentPieces;
        if (opponent == "WHITE") {
            opponentPieces = whitePieces;
        }
        else {
            opponentPieces = blackPieces;
        }
        for(int i=0; i < opponentPieces.size(); i++) {
            Piece p = opponentPieces.get(i);
            if (p.isAValidMove(board, p.getRow(), p.getCol(), row, col)) {
                attack.add(true);
            }
            else {
                attack.add(false);
            }
        }
        return attack;
    }

    /**
     * Check returns if a king is in check
     * @param board
     * @param user
     * @param opponent
     * @return
     */
    public boolean check(Board board, String user, String opponent) {
        Piece king;
        if (user == "BLACK") {
            king = getBlackKing();
        }
        else if (user == "WHITE"){
            king = getWhiteKing();
        }
        else {
            System.out.println("Please check Player types.");
            return false;
        }
        int row = king.getRow();
        int col = king.getCol();
        List<Boolean> attack = new ArrayList<>();
        attack = validateAttacks(board, opponent, row, col, attack);
        return (attack.contains(true));
    }


    /**
     * staleMate determines if the white player is at a stalemate.
     * That is, the white player is not in check but has no legal moves.
     * @param board the game board
     * @return true if player cannot move, false otherwise.
     */
    public boolean whiteStaleMate(Board board) {
        if (check(board, "WHITE", "BLACK")) {
            return false;
        }

        Piece whiteKing = getWhiteKing();
        int row = whiteKing.getRow();
        int col = whiteKing.getCol();
        board.getTile(row, col).removePiece();

        List<Pair> kingTiles = new ArrayList<Pair>();
        kingTiles = updateKingTiles(kingTiles, whiteKing, board, row, col);

        List<Pair> blockTiles = new ArrayList<Pair>();
        List<Pair> attackingTiles = new ArrayList<Pair>();
//        attackingTiles = getAttackingTilesWhiteKing(attackingTiles, board, row, col);
        attackingTiles = getAttackingTiles("BLACK", attackingTiles, board, row, col);

        for(int i=0; i < blackPieces.size(); i++) {
            Piece p = blackPieces.get(i);
            for(int j=0; j < kingTiles.size(); j++) {
                Pair coord = kingTiles.get(j);
                if (p.isAValidMove(board, p.getRow(), p.getCol(), coord.getRow(), coord.getCol())) {
                    kingTiles.remove(coord);
                    blockTiles.add(coord);
                    j--;
                }
            }
        }

        for(int i=0; i < whitePieces.size(); i++) {
            Piece p = whitePieces.get(i);
            if (p.getType() != Type.KING) {
                for (int j = 0; j < blockTiles.size(); j++) {
                    Pair coord = blockTiles.get(j);
                    if (p.isAValidMove(board, p.getRow(), p.getCol(), coord.getRow(), coord.getCol())) {
                        blockTiles.remove(coord);
                        kingTiles.add(coord);
                        j--;
                    }
                }
            }
            for(int k=0; k < attackingTiles.size(); k++) {
                Pair coord = attackingTiles.get(k);
                if (p.isAValidMove(board, p.getRow(), p.getCol(), coord.getRow(), coord.getCol())) {
                    attackingTiles.remove(coord);
                    k--;
                }
            }
        }

        board.getTile(row, col).setPiece(whiteKing);

        if (kingTiles.size() == 0){
            for(int i=0; i< whitePieces.size(); i++) {
                Piece p = whitePieces.get(i);
                if (p.getType() != Type.KING && p.canMove(board)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * staleMate determines if the black player is at a stalemate.
     * That is, the black player is not in check but has no legal moves.
     * @param board the game board
     * @return true if player cannot move, false otherwise.
     */
    public boolean blackStaleMate(Board board) {
        if (check(board, "BLACK", "WHITE")) {
            return false;
        }

        Piece blackKing = getBlackKing();
        int row = blackKing.getRow();
        int col = blackKing.getCol();
        board.getTile(row, col).removePiece();

        List<Pair> kingTiles = new ArrayList<Pair>();
        kingTiles = updateKingTiles(kingTiles, blackKing, board, row, col);

        List<Pair> attackingTiles = new ArrayList<Pair>();
        List<Pair> blockTiles = new ArrayList<Pair>();
//        attackingTiles = getAttackingTilesBlackKing(attackingTiles, board, row, col);
        attackingTiles = getAttackingTiles("BLACK", attackingTiles, board, row, col);

        for(int i=0; i < whitePieces.size(); i++) {
            Piece p = whitePieces.get(i);
            for(int j=0; j < kingTiles.size(); j++) {
                Pair coord = kingTiles.get(j);
                if (p.verifyPath(board, p.getRow(), p.getCol(), coord.getRow(), coord.getCol())) {
                    kingTiles.remove(coord);
                    blockTiles.add(coord);
                    j--;
                }
            }
        }

        for(int i=0; i < blackPieces.size(); i++) {
            Piece p = blackPieces.get(i);
            if (p.getType() != Type.KING) {
                for (int j = 0; j < blockTiles.size(); j++) {
                    Pair coord = blockTiles.get(j);
                    if (p.verifyPath(board, p.getRow(), p.getCol(), coord.getRow(), coord.getCol())) {
                        blockTiles.remove(coord);
                        kingTiles.add(coord);
                        j--;
                    }
                }
            }
            for(int k=0; k < attackingTiles.size(); k++) {
                Pair coord = attackingTiles.get(k);
                if (p.verifyPath(board, p.getRow(), p.getCol(), coord.getRow(), coord.getCol())) {
                    attackingTiles.remove(coord);
                    k--;
                }
            }
        }

        board.getTile(row,col).setPiece(blackKing);
        if (kingTiles.size() == 0) {
            for(int i=0; i<blackPieces.size(); i++) {
                Piece p = blackPieces.get(i);
                if (p.getType() != Type.KING && p.canMove(board)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    public static void main (String args[]) {
        Gui gui = new Gui();
    }

}
