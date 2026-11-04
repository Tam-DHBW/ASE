package com.github.ase.impl.ruleset.checkers.piece;

import com.github.ase.impl.Color;

class CheckersPieceType {
    final Color color;
    final boolean king;

    CheckersPieceType(Color color, boolean king) {
        this.color = color;
        this.king = king;
    }
}
