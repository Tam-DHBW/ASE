package com.github.ase.test.match.history;

import com.github.ase.impl.match.history.ArrayListTurnHistoryManagerFactory;
import com.github.ase.impl.match.history.Turn;
import com.github.ase.impl.match.history.TurnHistoryManager;
import com.github.ase.impl.match.history.TurnHistoryManagerFactory;
import com.github.ase.impl.ruleset.Piece;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ArrayListTurnHistoryManagerTest {
    private static final TurnHistoryManagerFactory<TestPiece> factory = new ArrayListTurnHistoryManagerFactory<>();

    @Mock private Piece<TestPiece> piece1;
    @Mock private Piece.Move<TestPiece> move1;
    @Mock private Collection<TestPiece> pieces1;
    @Mock private Piece<TestPiece> piece2;
    @Mock private Piece.Move<TestPiece> move2;
    @Mock private Collection<TestPiece> pieces2;
    @Mock private Piece<TestPiece> piece3;
    @Mock private Piece.Move<TestPiece> move3;
    @Mock private Collection<TestPiece> pieces3;
    @Mock private Piece<TestPiece> piece4;
    @Mock private Piece.Move<TestPiece> move4;
    @Mock private Collection<TestPiece> pieces4;

    @Test
    public void testHistoryManager() {
        assertNotEquals(move1, move2); // Sanity check mockito creates separate objects

        TurnHistoryManager<TestPiece> manager = factory.createTurnHistoryManager();

        manager.addTurn(piece1, move1, pieces1);
        manager.addTurn(piece2, move2, pieces2);
        manager.addTurn(piece3, move3, pieces3);
        assertEquals(3, manager.getTurns().size());

        Turn<TestPiece> selectedTurn = manager.getTurns().getFirst().selectCommand().selectTurn();
        assertEquals(piece1, selectedTurn.getPiece());
        assertEquals(move1, selectedTurn.getMove());
        assertEquals(pieces1, selectedTurn.getPiecesSnapshot());
        assertEquals(3, manager.getTurns().size());

        manager.addTurn(piece4, move4, pieces4);
        assertEquals(2, manager.getTurns().size());
        assertEquals(move1, manager.getTurns().getFirst().getMove());
        assertEquals(move4, manager.getTurns().getLast().getMove());
    }

    private interface TestPiece extends Piece<TestPiece> {}
}