package com.github.ase.impl.ruleset.checkers.piece;

import com.github.ase.impl.Color;

public interface CheckersPieceFactory {
    CheckersPiece createPiece(
        Color color,
        boolean king,
        int row,
        int col
    );
}
