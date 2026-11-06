package com.github.ase.impl.ruleset.chess.piece;

import com.github.ase.impl.Color;

import java.util.Collection;

abstract class ChessPieceType {
    private final Color color;
    protected final ChessPieceFactory factory;


    protected ChessPieceType(Color color, ChessPieceFactory factory) {
        this.color = color;
        this.factory = factory;
    }

    public Color getColor() {
        return color;
    }

    protected abstract String getTypeCode();
    protected abstract Collection<ChessPiece> getPossibleReplacements(
        Collection<ChessPiece> pieces,
        int row,
        int col
    );
}
