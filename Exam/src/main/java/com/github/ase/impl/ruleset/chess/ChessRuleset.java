package com.github.ase.impl.ruleset.chess;

import com.github.ase.impl.ruleset.Ruleset;

import java.util.Collection;
import java.util.List;

public class ChessRuleset implements Ruleset<ChessPiece> {
    @Override
    public Collection<ChessPiece> getInitialBoard() {
        return List.of();
    }
}
