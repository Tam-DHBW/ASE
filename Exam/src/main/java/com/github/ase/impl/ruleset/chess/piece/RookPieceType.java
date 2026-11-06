package com.github.ase.impl.ruleset.chess.piece;

import com.github.ase.impl.Color;

import java.util.Collection;
import java.util.stream.Stream;

class RookPieceType extends ChessPieceType {
    protected RookPieceType(Color color, ChessPieceFactory factory) {
        super(color, factory);
    }

    @Override
    protected String getTypeCode() {
        return "R";
    }

    @Override
    protected Collection<ChessPiece> getPossibleReplacements(Collection<ChessPiece> pieces, int row, int col) {
        return Stream.of(
                PieceUtils.directionalReplacements(pieces, i -> this.factory.createRook(this.getColor(), row - i, col)),
                PieceUtils.directionalReplacements(pieces, i -> this.factory.createRook(this.getColor(), row + i, col)),
                PieceUtils.directionalReplacements(pieces, i -> this.factory.createRook(this.getColor(), row, col - i)),
                PieceUtils.directionalReplacements(pieces, i -> this.factory.createRook(this.getColor(), row, col + i))
            )
            .flatMap(Collection::stream)
            .toList();
    }
}
