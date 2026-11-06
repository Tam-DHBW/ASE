package com.github.ase.impl.ruleset.chess.piece;

import com.github.ase.impl.Color;

import java.util.Collection;
import java.util.stream.IntStream;

class KingPieceType extends ChessPieceType {
    protected KingPieceType(Color color, ChessPieceFactory factory) {
        super(color, factory);
    }

    @Override
    protected String getTypeCode() {
        return "K";
    }

    @Override
    protected Collection<ChessPiece> getPossibleReplacements(Collection<ChessPiece> pieces, int row, int col) {
        return IntStream.rangeClosed(-1, 1).boxed().flatMap(rowOffset ->
            IntStream.rangeClosed(-1, 1).mapToObj(colOffset ->
                this.factory.createKing(this.getColor(), row + rowOffset, col + colOffset)
            )
        ).toList();
    }
}
