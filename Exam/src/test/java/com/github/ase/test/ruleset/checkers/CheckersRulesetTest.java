package com.github.ase.test.ruleset.checkers;

import com.github.ase.impl.Color;
import com.github.ase.impl.match.history.Turn;
import com.github.ase.impl.ruleset.Piece;
import com.github.ase.impl.ruleset.checkers.CheckersRuleset;
import com.github.ase.impl.ruleset.checkers.piece.CheckersPiece;
import com.github.ase.impl.ruleset.checkers.piece.CheckersPieceFactory;
import com.github.ase.impl.ruleset.checkers.piece.StaticCheckersPieceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CheckersRulesetTest {
    private CheckersPieceFactory pieceFactory;
    private CheckersRuleset ruleset;

    @BeforeEach
    public void init() {
        this.pieceFactory = StaticCheckersPieceFactory.getInstance();
        this.ruleset = new CheckersRuleset(pieceFactory);
    }

    @Test
    public void testInitialBoard() {
        Collection<CheckersPiece> initialBoard = ruleset.getInitialBoard();

        assertEquals(24, initialBoard.size());

        for (CheckersPiece piece : initialBoard) {
            assertEquals(1, (piece.getRow() + piece.getCol()) % 2);
            switch (piece.getColor()) {
                case BLACK -> {
                    assertEquals("dM", piece.getCode());
                    assertTrue(piece.getRow() < 3);
                }
                case WHITE -> {
                    assertEquals("lM", piece.getCode());
                    assertTrue(piece.getRow() > 4);
                }
            }
        }
    }

    @Test
    public void testRegularTurnLabel() {
        Turn<CheckersPiece> turn = mock(Turn.class);
        when(turn.getPiece()).thenReturn(pieceFactory.createPiece(Color.WHITE, false, 1, 1));
        when(turn.getMove()).thenReturn(new Piece.Move<>(
            pieceFactory.createPiece(Color.WHITE, true, 0, 0),
            Optional.empty()
        ));

        assertEquals("lM B1 -> A0", ruleset.getTurnLabel(turn));
    }

    @Test
    public void testCaptureTurnLabel() {
        Turn<CheckersPiece> turn = mock(Turn.class);
        when(turn.getPiece()).thenReturn(pieceFactory.createPiece(Color.WHITE, false, 2, 2));
        when(turn.getMove()).thenReturn(new Piece.Move<>(
            pieceFactory.createPiece(Color.WHITE, true, 1, 1),
            Optional.of(pieceFactory.createPiece(Color.BLACK, false, 0, 0))
        ));

        assertEquals("lM C2 X dM A0", ruleset.getTurnLabel(turn));
    }

    @Test
    public void testStatus() {
        Turn<CheckersPiece> turn1 = mock(Turn.class);
        when(turn1.getPiecesSnapshot()).thenReturn(List.of(
            pieceFactory.createPiece(Color.WHITE, false, 1, 1),
            pieceFactory.createPiece(Color.BLACK, false, 2, 2)
        ));
        assertEquals("", ruleset.getStatus(turn1));

        Turn<CheckersPiece> turn2 = mock(Turn.class);
        when(turn2.getPiecesSnapshot()).thenReturn(List.of(
            pieceFactory.createPiece(Color.BLACK, false, 2, 2)
        ));
        assertEquals("Black has won!", ruleset.getStatus(turn2));
    }

    @Test
    public void testGameEnded() {
        assertFalse(ruleset.gameEnded(List.of(
            pieceFactory.createPiece(Color.WHITE, false, 1, 1),
            pieceFactory.createPiece(Color.BLACK, false, 2, 2)
        )));

        assertTrue(ruleset.gameEnded(List.of(
            pieceFactory.createPiece(Color.BLACK, false, 2, 2)
        )));
    }
}