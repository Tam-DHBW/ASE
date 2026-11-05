package com.github.ase.test.ruleset.checkers;

import com.github.ase.impl.ruleset.checkers.CheckersRuleset;
import com.github.ase.impl.ruleset.checkers.piece.CheckersPiece;
import com.github.ase.impl.ruleset.checkers.piece.StaticCheckersPieceFactory;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class CheckersRulesetTest {
    @Test
    public void testInitialBoard() {
        CheckersRuleset ruleset = new CheckersRuleset(StaticCheckersPieceFactory.getInstance());
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
}