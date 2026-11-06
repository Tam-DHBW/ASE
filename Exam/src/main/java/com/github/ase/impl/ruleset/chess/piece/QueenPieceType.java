package com.github.ase.impl.ruleset.chess.piece;

import com.github.ase.impl.Color;

import java.util.Collection;
import java.util.stream.Stream;

class QueenPieceType extends ChessPieceType {
    protected QueenPieceType(Color color, ChessPieceFactory factory) {
        super(color, factory);
    }

    @Override
    protected String getTypeCode() {
        return "Q";
    }

    @Override
    protected Collection<ChessPiece> getPossibleReplacements(Collection<ChessPiece> pieces, int row, int col) {
        return Stream.of(
                PieceUtils.directionalReplacements(pieces, i -> this.factory.createQueen(this.getColor(), row - i, col)),
                PieceUtils.directionalReplacements(pieces, i -> this.factory.createQueen(this.getColor(), row + i, col)),
                PieceUtils.directionalReplacements(pieces, i -> this.factory.createQueen(this.getColor(), row, col - i)),
                PieceUtils.directionalReplacements(pieces, i -> this.factory.createQueen(this.getColor(), row, col + i)),
                PieceUtils.directionalReplacements(pieces, i -> this.factory.createQueen(this.getColor(), row - i, col - i)),
                PieceUtils.directionalReplacements(pieces, i -> this.factory.createQueen(this.getColor(), row + i, col + i)),
                PieceUtils.directionalReplacements(pieces, i -> this.factory.createQueen(this.getColor(), row + i, col - i)),
                PieceUtils.directionalReplacements(pieces, i -> this.factory.createQueen(this.getColor(), row - i, col + i))
            )
            .flatMap(Collection::stream)
            .toList();
    }
}
