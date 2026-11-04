package com.github.ase.impl.ruleset.checkers.piece;

import com.github.ase.impl.Color;

public class StaticCheckersPieceFactory implements CheckersPieceFactory {
    private static final CheckersPieceType darkManType = new CheckersPieceType(Color.BLACK, false);
    private static final CheckersPieceType darkKingType = new CheckersPieceType(Color.BLACK, true);
    private static final CheckersPieceType lightManType = new CheckersPieceType(Color.WHITE, false);
    private static final CheckersPieceType lightKingType = new CheckersPieceType(Color.WHITE, true);

    public CheckersPiece createPiece(
        Color color,
        boolean king,
        int row,
        int col
    ) {
        CheckersPieceType type = switch (color) {
            case BLACK -> king ? darkKingType : darkManType;
            case WHITE -> king ? lightKingType : lightManType;
        };

        return new CheckersPiece(type, row, col, this);
    }
}
