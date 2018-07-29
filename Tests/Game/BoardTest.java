package Game;

import Pieces.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BoardTest {
    Board board = new Board();
    Piece blackKing = new King("BLACK");
    Piece whiteKing = new King("WHITE");

    public void initializeKings() {
        board.blackPieces.add(blackKing);
        board.whitePieces.add(whiteKing);
    }

    @Test
    public void testGetAttackingTilesBlackKing() throws Exception {
        Tile start = board.getTile(0,0);
        start.setPiece(blackKing);
        Piece whiteBishop = new Bishop("WHITE");
        board.whitePieces.add(whiteBishop);
        Tile tile = board.getTile(4,4);
        tile.setPiece(whiteBishop);
        List<Board.Pair> attackPieces = new ArrayList<Board.Pair>();
        attackPieces = board.getAttackingTiles("WHITE", attackPieces, board, blackKing.getRow(), blackKing.getCol());
        assertEquals(1, attackPieces.size());
    }

    @Test
    public void testGetAttackingTilesWhiteKing() throws Exception {
        Tile start = board.getTile(4,4);
        start.setPiece(whiteKing);
        Piece blackBishop = new Bishop("BLACK");
        board.blackPieces.add(blackBishop);
        Tile tile = board.getTile(6,2);
        tile.setPiece(blackBishop);
        List<Board.Pair> attackPieces = new ArrayList<Board.Pair>();
        attackPieces = board.getAttackingTiles("BLACK", attackPieces, board, whiteKing.getRow(), whiteKing.getCol());
        board.printLayout(board);
        assertEquals(1, attackPieces.size());
    }

    @Test
    public void testWhiteCheckMate() throws Exception {
        initializeKings();
        Tile tile = board.getTile(7,2);
        tile.setPiece(whiteKing);
        tile = board.getTile(7,0);
        Piece blackRook = new Rook("BLACK");
        tile.setPiece(blackRook);
        tile = board.getTile(6,0);
        Piece blackRook2 = new Rook("BLACK");
        tile.setPiece(blackRook2);
        board.blackPieces.add(blackRook);
        board.blackPieces.add(blackRook2);
        boolean checkMate = board.checkMate(board, "WHITE", "BLACK");
        assertEquals(true, checkMate);
    }

    @Test
    public void testBlackCheckMate() throws Exception {
        initializeKings();
        Tile tile = board.getTile(7,2);
        tile.setPiece(blackKing);
        tile = board.getTile(7,0);
        Piece whiteRook = new Rook("WHITE");
        tile.setPiece(whiteRook);
        tile = board.getTile(6,0);
        Piece whiteQueen = new Queen("WHITE");
        tile.setPiece(whiteQueen);
        board.whitePieces.add(whiteRook);
        board.whitePieces.add(whiteQueen);
        boolean checkMate = board.checkMate(board, "BLACK", "WHITE");
        assertEquals(true, checkMate);
    }

    @Test
    public void testBlackCheck() throws Exception {
        initializeKings();
        Tile tile = board.getTile(7,2);
        tile.setPiece(blackKing);
        tile = board.getTile(5,0);
        Piece whiteBishop = new Bishop("WHITE");
        tile.setPiece(whiteBishop);
        board.whitePieces.add(whiteBishop);
        boolean check = board.check(board, "BLACK", "WHITE");
        assertEquals(true, check);
    }

    @Test
    public void testWhiteCheck() throws Exception {
        initializeKings();
        Tile tile = board.getTile(5,5);
        tile.setPiece(whiteKing);
        tile = board.getTile(5,0);
        Piece blackRook = new Rook("BLACK");
        tile.setPiece(blackRook);
        board.blackPieces.add(blackRook);
        boolean check = board.check(board, "WHITE", "BLACK");
        assertEquals(true, check);
    }


}