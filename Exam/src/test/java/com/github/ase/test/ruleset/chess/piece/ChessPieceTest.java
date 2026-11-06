package com.github.ase.test.ruleset.chess.piece;

import com.github.ase.impl.Color;
import com.github.ase.impl.ruleset.Piece.Move;
import com.github.ase.impl.ruleset.chess.piece.ChessPiece;
import com.github.ase.impl.ruleset.chess.piece.ChessPieceFactory;
import com.github.ase.impl.ruleset.chess.piece.HashMapChessPieceFactory;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ChessPieceTest {
    ChessPieceFactory factory = new HashMapChessPieceFactory();

    @Test
    public void testCodes() {
        assertEquals("bP", factory.createPawn(Color.BLACK, 0, 0).getCode());
        assertEquals("wP", factory.createPawn(Color.WHITE, 0, 0).getCode());
        assertEquals("bR", factory.createRook(Color.BLACK, 0, 0).getCode());
        assertEquals("wR", factory.createRook(Color.WHITE, 0, 0).getCode());
        assertEquals("bB", factory.createBishop(Color.BLACK, 0, 0).getCode());
        assertEquals("wB", factory.createBishop(Color.WHITE, 0, 0).getCode());
        assertEquals("bN", factory.createKnight(Color.BLACK, 0, 0).getCode());
        assertEquals("wN", factory.createKnight(Color.WHITE, 0, 0).getCode());
        assertEquals("bQ", factory.createQueen(Color.BLACK, 0, 0).getCode());
        assertEquals("wQ", factory.createQueen(Color.WHITE, 0, 0).getCode());
        assertEquals("bK", factory.createKing(Color.BLACK, 0, 0).getCode());
        assertEquals("wK", factory.createKing(Color.WHITE, 0, 0).getCode());
    }

    @Test
    public void testColor() {
        assertEquals(Color.BLACK, factory.createPawn(Color.BLACK, 3, 3).getColor());
        assertEquals(Color.WHITE, factory.createKing(Color.WHITE, 3, 3).getColor());
    }

    @Test
    public void testPositions() {
        ChessPiece pawn = factory.createPawn(Color.BLACK, 3, 4);
        assertEquals(3, pawn.getRow());
        assertEquals(4, pawn.getCol());
    }

    @Test
    public void testIsKing() {
        assertTrue(factory.createKing(Color.BLACK, 3, 3).isKing());
        assertFalse(factory.createPawn(Color.BLACK, 3, 3).isKing());
        assertFalse(factory.createQueen(Color.BLACK, 3, 3).isKing());
        assertFalse(factory.createKnight(Color.BLACK, 3, 3).isKing());
        assertFalse(factory.createRook(Color.BLACK, 3, 3).isKing());
        assertFalse(factory.createBishop(Color.BLACK, 3, 3).isKing());
    }

    @Test
    public void testPawnRegularMove() {
        ChessPiece pawn = factory.createPawn(Color.BLACK, 3, 3);

        Collection<Move<ChessPiece>> moves = pawn.getValidMoves(List.of(pawn));

        assertEquals(1, moves.size());
        Move<ChessPiece> move = moves.iterator().next();
        assertTrue(move.takes().isEmpty());
        assertEquals(4, move.replacement().getRow());
        assertEquals(3, move.replacement().getCol());
    }

    @Test
    public void testPawnFirstMoveTwoStep() {
        ChessPiece pawn = factory.createPawn(Color.WHITE, 6, 3);

        Collection<Move<ChessPiece>> moves = pawn.getValidMoves(List.of(pawn));

        assertEquals(2, moves.size());
        assertTrue(moves.stream().noneMatch(move -> move.takes().isPresent()));
        assertTrue(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 5 && move.replacement().getCol() == 3
        ));
        assertTrue(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 4 && move.replacement().getCol() == 3
        ));
    }

    @Test
    public void testPawnBlockedForward() {
        ChessPiece pawn = factory.createPawn(Color.BLACK, 3, 3);
        ChessPiece blocker = factory.createPawn(Color.WHITE, 4, 3);

        Collection<Move<ChessPiece>> moves = pawn.getValidMoves(List.of(pawn, blocker));

        assertEquals(0, moves.size());
    }

    @Test
    public void testPawnCapture() {
        ChessPiece pawn = factory.createPawn(Color.BLACK, 3, 3);
        ChessPiece blocker = factory.createPawn(Color.WHITE, 4, 3);
        ChessPiece enemy = factory.createPawn(Color.WHITE, 4, 2);

        Collection<Move<ChessPiece>> moves = pawn.getValidMoves(List.of(pawn, blocker, enemy));

        assertEquals(1, moves.size());
        Move<ChessPiece> move = moves.iterator().next();
        assertTrue(move.takes().isPresent());
        assertEquals(enemy, move.takes().get());
        assertEquals(4, move.replacement().getRow());
        assertEquals(2, move.replacement().getCol());
    }

    @Test
    public void testPawnOwnPieceBlocksDiagonal() {
        ChessPiece pawn = factory.createPawn(Color.BLACK, 3, 3);
        ChessPiece ally = factory.createPawn(Color.BLACK, 4, 2);
        ChessPiece blocker = factory.createPawn(Color.WHITE, 4, 3);

        Collection<Move<ChessPiece>> moves = pawn.getValidMoves(List.of(pawn, ally, blocker));

        assertEquals(0, moves.size());
    }

    @Test
    public void testKnightMovesFromCenter() {
        ChessPiece knight = factory.createKnight(Color.BLACK, 3, 3);

        Collection<Move<ChessPiece>> moves = knight.getValidMoves(List.of(knight));

        assertEquals(8, moves.size());
        assertTrue(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 5 && move.replacement().getCol() == 4
        ));
        assertTrue(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 1 && move.replacement().getCol() == 2
        ));
        assertFalse(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 4 && move.replacement().getCol() == 3
        ));
    }

    @Test
    public void testKnightMovesFromCorner() {
        ChessPiece knight = factory.createKnight(Color.BLACK, 0, 0);

        Collection<Move<ChessPiece>> moves = knight.getValidMoves(List.of(knight));

        assertEquals(2, moves.size());
        assertTrue(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 2 && move.replacement().getCol() == 1
        ));
        assertTrue(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 1 && move.replacement().getCol() == 2
        ));
    }

    @Test
    public void testKnightCapture() {
        ChessPiece knight = factory.createKnight(Color.BLACK, 3, 3);
        ChessPiece enemy = factory.createKnight(Color.WHITE, 5, 4);

        Collection<Move<ChessPiece>> moves = knight.getValidMoves(List.of(knight, enemy));

        assertEquals(8, moves.size());
        assertTrue(moves.stream().anyMatch(move ->
            move.takes().isPresent()
            && move.replacement().getRow() == 5
            && move.replacement().getCol() == 4
        ));
    }

    @Test
    public void testRookMovesFromCenter() {
        ChessPiece rook = factory.createRook(Color.BLACK, 3, 3);

        Collection<Move<ChessPiece>> moves = rook.getValidMoves(List.of(rook));

        assertEquals(14, moves.size());
        assertTrue(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 3 && move.replacement().getCol() == 4
        ));
        assertTrue(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 4 && move.replacement().getCol() == 3
        ));
        assertFalse(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 4 && move.replacement().getCol() == 4
        ));
    }

    @Test
    public void testRookBlockedByOwnPiece() {
        ChessPiece rook = factory.createRook(Color.BLACK, 3, 3);
        ChessPiece ally = factory.createRook(Color.BLACK, 3, 6);

        Collection<Move<ChessPiece>> moves = rook.getValidMoves(List.of(rook, ally));

        assertEquals(12, moves.size());
    }

    @Test
    public void testRookCaptureEnemy() {
        ChessPiece rook = factory.createRook(Color.BLACK, 3, 3);
        ChessPiece enemy = factory.createRook(Color.WHITE, 3, 6);

        Collection<Move<ChessPiece>> moves = rook.getValidMoves(List.of(rook, enemy));

        assertEquals(13, moves.size());
        assertTrue(moves.stream().anyMatch(move ->
            move.takes().isPresent()
            && move.replacement().getRow() == 3
            && move.replacement().getCol() == 6
        ));
    }

    @Test
    public void testBishopMovesFromCenter() {
        ChessPiece bishop = factory.createBishop(Color.BLACK, 3, 3);

        Collection<Move<ChessPiece>> moves = bishop.getValidMoves(List.of(bishop));

        assertEquals(13, moves.size());
        assertTrue(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 4 && move.replacement().getCol() == 4
        ));
        assertTrue(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 2 && move.replacement().getCol() == 2
        ));
        assertFalse(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 3 && move.replacement().getCol() == 4
        ));
    }

    @Test
    public void testBishopBlockedByOwnPiece() {
        ChessPiece bishop = factory.createBishop(Color.BLACK, 3, 3);
        ChessPiece ally = factory.createBishop(Color.BLACK, 4, 4);

        Collection<Move<ChessPiece>> moves = bishop.getValidMoves(List.of(bishop, ally));

        assertEquals(9, moves.size());
    }

    @Test
    public void testQueenMovesFromCenter() {
        ChessPiece queen = factory.createQueen(Color.BLACK, 3, 3);

        Collection<Move<ChessPiece>> moves = queen.getValidMoves(List.of(queen));

        assertEquals(27, moves.size());
        assertTrue(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 3 && move.replacement().getCol() == 4
        ));
        assertTrue(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 4 && move.replacement().getCol() == 4
        ));
        assertFalse(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 5 && move.replacement().getCol() == 4
        ));
    }

    @Test
    public void testQueenCaptureEnemy() {
        ChessPiece queen = factory.createQueen(Color.BLACK, 3, 3);
        ChessPiece enemy = factory.createRook(Color.WHITE, 3, 7);

        Collection<Move<ChessPiece>> moves = queen.getValidMoves(List.of(queen, enemy));

        assertEquals(27, moves.size());
        assertTrue(moves.stream().anyMatch(move ->
            move.takes().isPresent()
            && move.replacement().getRow() == 3
            && move.replacement().getCol() == 7
        ));
    }

    @Test
    public void testKingMovesFromCenter() {
        ChessPiece king = factory.createKing(Color.BLACK, 3, 3);

        Collection<Move<ChessPiece>> moves = king.getValidMoves(List.of(king));

        assertEquals(8, moves.size());
        assertTrue(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 4 && move.replacement().getCol() == 4
        ));
        assertTrue(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 2 && move.replacement().getCol() == 3
        ));
        assertFalse(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 5 && move.replacement().getCol() == 5
        ));
    }

    @Test
    public void testKingMovesFromCorner() {
        ChessPiece king = factory.createKing(Color.BLACK, 0, 0);

        Collection<Move<ChessPiece>> moves = king.getValidMoves(List.of(king));

        assertEquals(3, moves.size());
        assertTrue(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 1 && move.replacement().getCol() == 1
        ));
        assertTrue(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 0 && move.replacement().getCol() == 1
        ));
        assertTrue(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 1 && move.replacement().getCol() == 0
        ));
    }

    @Test
    public void testKingCapture() {
        ChessPiece king = factory.createKing(Color.BLACK, 3, 3);
        ChessPiece enemy = factory.createKing(Color.WHITE, 4, 4);

        Collection<Move<ChessPiece>> moves = king.getValidMoves(List.of(king, enemy));

        assertEquals(8, moves.size());
        assertTrue(moves.stream().anyMatch(move ->
            move.takes().isPresent()
            && move.replacement().getRow() == 4
            && move.replacement().getCol() == 4
        ));
    }

    @Test
    public void testOutOfBounds() {
        ChessPiece bishop = factory.createBishop(Color.BLACK, 0, 0);
        assertEquals(7, bishop.getValidMoves(List.of(bishop)).size());

        ChessPiece rook = factory.createRook(Color.BLACK, 0, 0);
        assertEquals(14, rook.getValidMoves(List.of(rook)).size());
    }
}
