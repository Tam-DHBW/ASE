package com.github.ase.impl.ruleset.chess.piece;

import com.github.ase.impl.Color;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

class BishopPieceType extends ChessPieceType {
    protected BishopPieceType(Color color, ChessPieceFactory factory) {
        super(color, factory);
    }

    @Override
    protected String getTypeCode() {
        return "B";
    }

    @Override
    protected Collection<ChessPiece> getPossibleReplacements(Collection<ChessPiece> pieces, int row, int col) {
        return Stream.of(
                PieceUtils.directionalReplacements(pieces, i -> this.factory.createBishop(this.getColor(), row - i, col - i)),
                PieceUtils.directionalReplacements(pieces, i -> this.factory.createBishop(this.getColor(), row + i, col + i)),
                PieceUtils.directionalReplacements(pieces, i -> this.factory.createBishop(this.getColor(), row + i, col - i)),
                PieceUtils.directionalReplacements(pieces, i -> this.factory.createBishop(this.getColor(), row - i, col + i))
            )
            .flatMap(Collection::stream)
            .toList();
    }
}
