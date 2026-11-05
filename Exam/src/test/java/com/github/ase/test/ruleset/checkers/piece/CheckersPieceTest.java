package com.github.ase.test.ruleset.checkers.piece;

import com.github.ase.BoardModel;
import com.github.ase.impl.Color;
import com.github.ase.impl.ruleset.Piece.Move;
import com.github.ase.impl.ruleset.checkers.piece.CheckersPiece;
import com.github.ase.impl.ruleset.checkers.piece.CheckersPieceFactory;
import com.github.ase.impl.ruleset.checkers.piece.StaticCheckersPieceFactory;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CheckersPieceTest {
    CheckersPieceFactory factory = new StaticCheckersPieceFactory();

    @Test
    public void testCode() {
        assertEquals("dM", factory.createPiece(Color.BLACK, false, 0, 0).getCode());
        assertEquals("dK", factory.createPiece(Color.BLACK, true, 0, 0).getCode());
        assertEquals("lM", factory.createPiece(Color.WHITE, false, 0, 0).getCode());
        assertEquals("lK", factory.createPiece(Color.WHITE, true, 0, 0).getCode());
    }

    @Test
    public void testColor() {
        assertEquals(Color.BLACK, factory.createPiece(Color.BLACK, false, 0, 0).getColor());
        assertEquals(Color.BLACK, factory.createPiece(Color.BLACK, true, 0, 0).getColor());
        assertEquals(Color.WHITE, factory.createPiece(Color.WHITE, false, 0, 0).getColor());
        assertEquals(Color.WHITE, factory.createPiece(Color.WHITE, true, 0, 0).getColor());
    }

    @Test
    public void testPositions() {
        CheckersPiece black = factory.createPiece(Color.BLACK, false, 3, 4);
        assertEquals(3, black.getRow());
        assertEquals(4, black.getCol());

        CheckersPiece white = factory.createPiece(Color.WHITE, true, 1, 7);
        assertEquals(1, white.getRow());
        assertEquals(7, white.getCol());
    }

    @Test
    public void testValidRegularMoves() {
        CheckersPiece piece = factory.createPiece(Color.BLACK, false, 3, 3);

        Collection<Move<CheckersPiece>> moves = piece.getValidMoves(List.of());

        assertEquals(2, moves.size());
        assertTrue(moves.stream().noneMatch(move -> move.takes().isPresent()));
        assertTrue(moves.stream().allMatch(move -> move.replacement().getRow() > piece.getRow()));
        assertTrue(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 4 && move.replacement().getCol() == 2)
        );
        assertTrue(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 4 && move.replacement().getCol() == 4)
        );
    }

    @Test
    public void testValidKingMoves() {
        CheckersPiece king = factory.createPiece(Color.BLACK, true, 3, 3);

        Collection<Move<CheckersPiece>> moves = king.getValidMoves(List.of());

        assertEquals(4, moves.size());
        assertTrue(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 2 && move.replacement().getCol() == 2)
        );
        assertTrue(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 2 && move.replacement().getCol() == 4)
        );
        assertTrue(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 4 && move.replacement().getCol() == 2)
        );
        assertTrue(moves.stream().anyMatch(move ->
            move.replacement().getRow() == 4 && move.replacement().getCol() == 4)
        );
    }

    @Test
    public void testRegularCaptures() {
        CheckersPiece piece = factory.createPiece(Color.BLACK, false, 3, 0);
        CheckersPiece enemy = factory.createPiece(Color.WHITE, false, 4, 1);
        Collection<CheckersPiece> pieces = List.of(piece, enemy);

        Collection<Move<CheckersPiece>> moves = piece.getValidMoves(pieces);

        assertEquals(1, moves.size());
        Move<CheckersPiece> move = moves.iterator().next();
        assertTrue(move.takes().isPresent());
        assertEquals(5, move.replacement().getRow());
        assertEquals(2, move.replacement().getCol());
        assertEquals(4, move.takes().get().getRow());
        assertEquals(1, move.takes().get().getCol());
    }

    @Test
    public void testKingCaptures() {
        CheckersPiece king = factory.createPiece(Color.BLACK, true, 3, 3);
        CheckersPiece enemyForward = factory.createPiece(Color.WHITE, false, 4, 4);
        CheckersPiece enemyBackward = factory.createPiece(Color.WHITE, false, 2, 2);
        Collection<CheckersPiece> pieces = List.of(king, enemyForward, enemyBackward);

        Collection<Move<CheckersPiece>> moves = king.getValidMoves(pieces);

        Collection<Move<CheckersPiece>> captures = moves.stream()
            .filter(m -> m.takes().isPresent())
            .toList();

        assertEquals(2, captures.size());
        assertTrue(captures.stream().anyMatch(move ->
            move.replacement().getRow() == 5 && move.replacement().getCol() == 5)
        );
        assertTrue(captures.stream().anyMatch(move ->
            move.replacement().getRow() == 1 && move.replacement().getCol() == 1)
        );
    }

    @Test
    public void testRegularMoveBlockedByOther() {
        CheckersPiece piece = factory.createPiece(Color.BLACK, false, 3, 3);
        CheckersPiece blocker1 = factory.createPiece(Color.BLACK, false, 4, 2);
        CheckersPiece blocker2 = factory.createPiece(Color.BLACK, false, 4, 4);
        Collection<CheckersPiece> pieces = List.of(piece, blocker1, blocker2);

        Collection<Move<CheckersPiece>> moves = piece.getValidMoves(pieces);

        assertEquals(0, moves.size());
    }

    @Test
    public void testCaptureBlockedByOther() {
        CheckersPiece piece = factory.createPiece(Color.BLACK, false, 0, 0);
        CheckersPiece enemy1 = factory.createPiece(Color.WHITE, false, 1, 1);
        CheckersPiece enemy2 = factory.createPiece(Color.WHITE, false, 2, 2);
        Collection<CheckersPiece> pieces = List.of(piece, enemy1, enemy2);

        Collection<Move<CheckersPiece>> moves = piece.getValidMoves(pieces);

        assertEquals(0, moves.size());
    }

    @Test
    public void testMakeKing() {
        CheckersPiece piece = factory.createPiece(Color.WHITE, false, 1, 1);

        Collection<Move<CheckersPiece>> moves = piece.getValidMoves(List.of());

        assertEquals(2, moves.size());
        for (Move<CheckersPiece> move : moves) {
            assertEquals("lK", move.replacement().getCode());
        }
    }

    @Test
    public void testPreventOOB() {
        CheckersPiece top = factory.createPiece(Color.WHITE, false, 0, 2);
        CheckersPiece bottom = factory.createPiece(Color.BLACK, false, BoardModel.SIZE, 2);
        CheckersPiece left = factory.createPiece(Color.BLACK, false, 2, 0);
        CheckersPiece right = factory.createPiece(Color.BLACK, false, 2, BoardModel.SIZE);

        assertEquals(0, top.getValidMoves(List.of()).size());
        assertEquals(0, bottom.getValidMoves(List.of()).size());
        assertEquals(1, left.getValidMoves(List.of()).size());
        assertEquals(1, right.getValidMoves(List.of()).size());
    }
}
