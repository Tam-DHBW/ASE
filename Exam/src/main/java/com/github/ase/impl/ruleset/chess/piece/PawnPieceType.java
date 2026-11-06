package com.github.ase.impl.ruleset.chess.piece;

import com.github.ase.BoardModel;
import com.github.ase.impl.Color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

class PawnPieceType extends ChessPieceType {
    protected PawnPieceType(Color color, ChessPieceFactory factory) {
        super(color, factory);
    }

    @Override
    protected String getTypeCode() {
        return "P";
    }

    @Override
    protected Collection<ChessPiece> getPossibleReplacements(Collection<ChessPiece> pieces, int row, int col) {
        int direction = switch (this.getColor()) {
            case BLACK -> 1;
            case WHITE -> -1;
        };
        Predicate<ChessPiece> alreadyOccupied = replacement -> pieces.stream().anyMatch(
            piece -> piece.getRow() == replacement.getRow() && piece.getCol() == replacement.getCol()
        );

        List<ChessPiece> regularReplacements = new ArrayList<>();
        regularReplacements.add(this.factory.createPawn(
            this.getColor(),
            row + direction,
            col
        ));

        boolean isFirstMove = switch (this.getColor()) {
            case BLACK -> 1;
            case WHITE -> BoardModel.SIZE - 1 - 1;
        } == row;
        if (isFirstMove) {
            regularReplacements.add(this.factory.createPawn(
                this.getColor(),
                row + direction * 2,
                col
            ));
        }

        Stream<ChessPiece> captureReplacements = Stream.of(
            this.factory.createPawn(this.getColor(), row + direction, col - 1),
            this.factory.createPawn(this.getColor(), row + direction, col + 1)
        );

        return Stream.concat(
            regularReplacements.stream().filter(replacement -> !alreadyOccupied.test(replacement)),
            captureReplacements.filter(alreadyOccupied)
        ).toList();
    }
}
