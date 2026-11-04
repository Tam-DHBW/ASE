package com.github.ase.test;

import com.github.ase.BoardModel;
import com.github.ase.impl.MatchBoardModel;
import com.github.ase.impl.match.Match;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class MatchBoardModelTest {
    @Test
    public void testSetup() {
        BoardModel.Game firstGame = mock(BoardModel.Game.class);
        Match firstMatch = mock(Match.class);
        BoardModel.Game secondGame = mock(BoardModel.Game.class);
        Match secondMatch = mock(Match.class);
        BoardModel boardModel = new MatchBoardModel(game -> {
            if (game == firstGame) return firstMatch;
            if (game == secondGame) return secondMatch;
            throw new AssertionError("Unreachable");
        });

        verifyProxy(boardModel, firstGame, firstMatch, secondMatch);
        verifyProxy(boardModel, secondGame, secondMatch, firstMatch);
    }

    private void verifyProxy(BoardModel boardModel, BoardModel.Game game, Match correct, Match wrong) {
        boardModel.setup(game);

        boardModel.pieceAt(4, 6);
        verify(correct).pieceAt(4, 6);

        boardModel.isCellHighlighted(2, 3);
        verify(correct).isCellHighlighted(2, 3);

        boardModel.onCellClick(5, 5);
        verify(correct).onCellClick(5, 5);

        boardModel.moves();
        verify(correct).moves();

        boardModel.onSelectMove("test");
        verify(correct).onSelectMove("test");

        boardModel.currentPlayer();
        verify(correct).currentPlayer();

        boardModel.getStatus();
        verify(correct).getStatus();

        verifyNoMoreInteractions(wrong);
    }
}