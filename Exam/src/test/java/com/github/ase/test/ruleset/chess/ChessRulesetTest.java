package com.github.ase.test.ruleset.chess;

import com.github.ase.impl.Color;
import com.github.ase.impl.match.history.Turn;
import com.github.ase.impl.ruleset.Piece;
import com.github.ase.impl.ruleset.chess.ChessRuleset;
import com.github.ase.impl.ruleset.chess.piece.ChessPiece;
import com.github.ase.impl.ruleset.chess.piece.ChessPieceFactory;
import com.github.ase.impl.ruleset.chess.piece.HashMapChessPieceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChessRulesetTest {
    private ChessPieceFactory pieceFactory;
    private ChessRuleset ruleset;

    @BeforeEach
    public void init() {
        this.pieceFactory = new HashMapChessPieceFactory();
        this.ruleset = new ChessRuleset(this.pieceFactory);
    }

    @Test
    public void testInitialBoard() {
        Collection<ChessPiece> initialBoard = ruleset.getInitialBoard();

        assertEquals(32, initialBoard.size());
        assertEquals(16, initialBoard.stream().filter(piece -> piece.getColor() == Color.BLACK).count());
        assertEquals(16, initialBoard.stream().filter(piece -> piece.getColor() == Color.WHITE).count());

        assertEquals("bR", getPieceAt(initialBoard, 0, 0).getCode());
        assertEquals("bR", getPieceAt(initialBoard, 0, 7).getCode());
        assertEquals("bB", getPieceAt(initialBoard, 0, 1).getCode());
        assertEquals("bB", getPieceAt(initialBoard, 0, 6).getCode());
        assertEquals("bN", getPieceAt(initialBoard, 0, 2).getCode());
        assertEquals("bN", getPieceAt(initialBoard, 0, 5).getCode());
        assertEquals("bQ", getPieceAt(initialBoard, 0, 3).getCode());
        assertEquals("bK", getPieceAt(initialBoard, 0, 4).getCode());
        for (int col = 0; col < 8; col++) {
            assertEquals("bP", getPieceAt(initialBoard, 1, col).getCode());
        }

        for (int col = 0; col < 8; col++) {
            assertEquals("wP", getPieceAt(initialBoard, 6, col).getCode());
        }
        assertEquals("wR", getPieceAt(initialBoard, 7, 0).getCode());
        assertEquals("wR", getPieceAt(initialBoard, 7, 7).getCode());
        assertEquals("wB", getPieceAt(initialBoard, 7, 1).getCode());
        assertEquals("wB", getPieceAt(initialBoard, 7, 6).getCode());
        assertEquals("wN", getPieceAt(initialBoard, 7, 2).getCode());
        assertEquals("wN", getPieceAt(initialBoard, 7, 5).getCode());
        assertEquals("wQ", getPieceAt(initialBoard, 7, 3).getCode());
        assertEquals("wK", getPieceAt(initialBoard, 7, 4).getCode());
    }

    private ChessPiece getPieceAt(Collection<ChessPiece> pieces, int row, int col) {
        return pieces.stream()
            .filter(piece -> piece.getRow() == row && piece.getCol() == col)
            .findFirst()
            .orElseThrow(() -> new AssertionError("no piece at " + row + "," + col));
    }

    @Test
    public void testRegularTurnLabel() {
        Turn<ChessPiece> turn = mock(Turn.class);
        when(turn.getPiece()).thenReturn(pieceFactory.createPawn(Color.WHITE, 6, 3));
        when(turn.getMove()).thenReturn(new Piece.Move<>(
            pieceFactory.createPawn(Color.WHITE, 5, 3),
            Optional.empty()
        ));

        assertEquals("wP D2 -> D3", ruleset.getTurnLabel(turn));
    }

    @Test
    public void testCaptureTurnLabel() {
        Turn<ChessPiece> turn = mock(Turn.class);
        when(turn.getPiece()).thenReturn(pieceFactory.createRook(Color.WHITE, 7, 0));
        when(turn.getMove()).thenReturn(new Piece.Move<>(
            pieceFactory.createRook(Color.WHITE, 7, 4),
            Optional.of(pieceFactory.createPawn(Color.BLACK, 7, 4))
        ));

        assertEquals("wR A1 X bP E1", ruleset.getTurnLabel(turn));
    }

    @Test
    public void testStatus() {
        Turn<ChessPiece> turn1 = mock(Turn.class);
        when(turn1.getPiecesSnapshot()).thenReturn(List.of(
            pieceFactory.createKing(Color.WHITE, 7, 4),
            pieceFactory.createKing(Color.BLACK, 0, 4)
        ));
        assertEquals("", ruleset.getStatus(turn1));

        Turn<ChessPiece> turn2 = mock(Turn.class);
        when(turn2.getPiecesSnapshot()).thenReturn(List.of(
            pieceFactory.createKing(Color.BLACK, 0, 4)
        ));
        assertEquals("Black has won this match of chess!", ruleset.getStatus(turn2));

        Turn<ChessPiece> turn3 = mock(Turn.class);
        when(turn3.getPiecesSnapshot()).thenReturn(List.of(
            pieceFactory.createKing(Color.WHITE, 7, 4)
        ));
        assertEquals("White has won this match of chess!", ruleset.getStatus(turn3));
    }

    @Test
    public void testGameEnded() {
        assertFalse(ruleset.gameEnded(List.of(
            pieceFactory.createKing(Color.WHITE, 7, 4),
            pieceFactory.createKing(Color.BLACK, 0, 4)
        )));

        assertTrue(ruleset.gameEnded(List.of(
            pieceFactory.createKing(Color.BLACK, 0, 4)
        )));

        assertTrue(ruleset.gameEnded(List.of(
            pieceFactory.createKing(Color.WHITE, 7, 4)
        )));
    }
}
