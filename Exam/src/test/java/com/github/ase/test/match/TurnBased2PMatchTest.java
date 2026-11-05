package com.github.ase.test.match;

import com.github.ase.impl.Color;
import com.github.ase.impl.match.TurnBased2PMatch;
import com.github.ase.impl.match.history.ArrayListTurnHistoryManagerFactory;
import com.github.ase.impl.match.history.Turn;
import com.github.ase.impl.ruleset.Piece;
import com.github.ase.impl.ruleset.Ruleset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TurnBased2PMatchTest {
    private TurnBased2PMatch<TestPiece> match;

    /**
     * 0 1 2 3 .
     * 0   b b
     * 1
     * 2   w w
     * 3
     * .
     * Vertical moves only. Black moves +1, White moves -1.
     * Captures destination.
     */
    @BeforeEach
    public void init() {
        match = new TurnBased2PMatch<>(
            new TestRuleset(),
            new ArrayListTurnHistoryManagerFactory<TestPiece>().createTurnHistoryManager()
        );
    }

    @Test
    public void testPieceAt() {
        assertEquals("b", match.pieceAt(0, 1));
        assertEquals("b", match.pieceAt(0, 2));
        assertEquals("w", match.pieceAt(2, 1));
        assertEquals("w", match.pieceAt(2, 2));
        assertNull(match.pieceAt(4, 4));
    }

    @Test
    public void testInitialPlayer() {
        // Sanity check so we dont assume wrong initial player in tests
        assertEquals("White", match.currentPlayer());
    }


    @Test
    public void testProhibitMoveOpponent() {
        match.onCellClick(0, 1);
        assertFalse(match.isCellHighlighted(1, 1));
    }

    @Test
    public void testHighlightPossibleMove() {
        match.onCellClick(2, 1);
        assertTrue(match.isCellHighlighted(1, 1));
        assertFalse(match.isCellHighlighted(2, 1));
        assertFalse(match.isCellHighlighted(0, 1));
        assertFalse(match.isCellHighlighted(1, 2));
    }

    @Test
    public void testMakeMove() {
        assertNull(match.pieceAt(1, 1));
        match.onCellClick(2, 1);
        match.onCellClick(1, 1);
        assertEquals("w", match.pieceAt(1, 1));
        assertEquals("Black", match.currentPlayer());
    }

    @Test
    public void testAbortMove() {
        match.onCellClick(2, 1);
        match.onCellClick(6, 6);
        assertEquals("w", match.pieceAt(2, 1));
        assertEquals("White", match.currentPlayer());

        match.onCellClick(2, 1);
        match.onCellClick(2, 2);
        assertEquals("w", match.pieceAt(2, 1));
        assertEquals("White", match.currentPlayer());
    }

    @Test
    public void testCapture() {
        match.onCellClick(2, 1);
        match.onCellClick(1, 1);
        assertEquals("Black", match.currentPlayer());
        assertEquals("w", match.pieceAt(1, 1));

        match.onCellClick(0, 1);
        match.onCellClick(1, 1);
        assertEquals("White", match.currentPlayer());
        assertEquals("b", match.pieceAt(1, 1));
        assertNull(match.pieceAt(0, 1));
        assertNull(match.pieceAt(2, 1));
    }

    @Test
    public void testMoveList() {
        match.onCellClick(2, 1);
        match.onCellClick(1, 1);

        match.onCellClick(0, 1);
        match.onCellClick(1, 1);

        match.onCellClick(2, 2);
        match.onCellClick(1, 2);

        List<String> moves = match.moves();
        assertEquals("#1 test label w", moves.get(0));
        assertEquals("#2 test label b", moves.get(1));
        assertEquals("#3 test label w", moves.get(2));
        assertEquals(3, moves.size());

        match.onSelectMove("#1 test label w");

        moves = match.moves();
        assertEquals("#1 test label w", moves.get(0));
        assertEquals("#2 test label b", moves.get(1));
        assertEquals("#3 test label w", moves.get(2));
        assertEquals(3, moves.size());
        assertEquals("Black", match.currentPlayer());

        match.onCellClick(0, 2);
        match.onCellClick(1, 2);

        moves = match.moves();
        assertEquals("#1 test label w", moves.get(0));
        assertEquals("#2 test label b", moves.get(1));
        assertEquals(2, moves.size());
    }

    /**
     * Small concrete implementation is easier than mocking move logic
     */
    private static class TestRuleset implements Ruleset<TestPiece> {

        @Override
        public Collection<TestPiece> getInitialBoard() {
            TestPiece pieceB1 = new TestPiece(Color.BLACK, 0, 1);
            TestPiece pieceB2 = new TestPiece(Color.BLACK, 0, 2);
            TestPiece pieceW1 = new TestPiece(Color.WHITE, 2, 1);
            TestPiece pieceW2 = new TestPiece(Color.WHITE, 2, 2);
            return List.of(
                pieceB1,
                pieceB2,
                pieceW1,
                pieceW2
            );
        }

        @Override
        public String getTurnLabel(Turn<TestPiece> turn) {
            return "test label " + turn.getPiece().getCode();
        }
    }

    private record TestPiece(Color color, int row, int col) implements Piece<TestPiece> {
        @Override
        public String getCode() {
            return switch (this.color()) {
                case BLACK -> "b";
                case WHITE -> "w";
            };
        }

        @Override
        public Color getColor() {
            return this.color();
        }

        @Override
        public int getRow() {
            return this.row();
        }

        @Override
        public int getCol() {
            return this.col();
        }

        @Override
        public Collection<Move<TestPiece>> getValidMoves(Collection<TestPiece> pieces) {
            int moveRow = this.row() + (switch (this.color()) {
                case BLACK -> 1;
                case WHITE -> -1;
            });

            return List.of(
                new Move<>(
                    new TestPiece(
                        this.color(),
                        moveRow,
                        this.col()
                    ),
                    pieces.stream()
                        .filter(piece -> piece.getRow() == moveRow && piece.getCol() == this.col())
                        .findAny()
                )
            );
        }
    }
}
