package com.github.ase.impl.ruleset.chess.piece;

import com.github.ase.impl.Color;

import java.util.Collection;
import java.util.List;

class KnightPieceType extends ChessPieceType {
    protected KnightPieceType(Color color, ChessPieceFactory factory) {
        super(color, factory);
    }

    @Override
    protected String getTypeCode() {
        return "N";
    }

    @Override
    protected Collection<ChessPiece> getPossibleReplacements(Collection<ChessPiece> pieces, int row, int col) {
        return List.of(
            this.factory.createKnight(this.getColor(), row + 2, col + 1),
            this.factory.createKnight(this.getColor(), row + 2, col - 1),
            this.factory.createKnight(this.getColor(), row - 2, col + 1),
            this.factory.createKnight(this.getColor(), row - 2, col - 1),
            this.factory.createKnight(this.getColor(), row + 1, col + 2),
            this.factory.createKnight(this.getColor(), row + 1, col - 2),
            this.factory.createKnight(this.getColor(), row - 1, col + 2),
            this.factory.createKnight(this.getColor(), row - 1, col - 2)
        );
    }
}
