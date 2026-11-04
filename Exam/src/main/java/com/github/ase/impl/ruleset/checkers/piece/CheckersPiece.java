package com.github.ase.impl.ruleset.checkers.piece;

import com.github.ase.BoardModel;
import com.github.ase.impl.Color;
import com.github.ase.impl.ruleset.Piece;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public class CheckersPiece implements Piece<CheckersPiece> {
    private final CheckersPieceType type;
    private final int row;
    private final int col;
    private final CheckersPieceFactory factory;

    CheckersPiece(CheckersPieceType type, int row, int col, CheckersPieceFactory factory) {
        this.type = type;
        this.row = row;
        this.col = col;
        this.factory = factory;
    }

    @Override
    public String getCode() {
        String colorCode = switch (this.type.color) {
            case BLACK -> "d";
            case WHITE -> "l";
        };
        String pieceCode = this.type.king ? "K" : "M";

        return colorCode + pieceCode;
    }

    @Override
    public Color getColor() {
        return this.type.color;
    }

    @Override
    public int getRow() {
        return this.row;
    }

    @Override
    public int getCol() {
        return this.col;
    }

    @Override
    public Collection<Move<CheckersPiece>> getValidMoves(Collection<CheckersPiece> pieces) {
        int[][] offsets = {
            {-1, -1},
            {-1, 1},
            {1, -1},
            {1, 1}
        };

        return Arrays.stream(offsets)
            .map(offset -> {
                int regularMoveRow = this.row + offset[0];
                int regularMoveCol = this.col + offset[1];

                Optional<CheckersPiece> takes = pieces.stream()
                    .filter(piece -> piece.type.color != this.type.color)
                    .filter(piece -> piece.row == regularMoveRow)
                    .filter(piece -> piece.col == regularMoveCol)
                    .findAny();

                int newRow = regularMoveRow + (takes.isPresent() ? offset[0] : 0);
                int newCol = regularMoveCol + (takes.isPresent() ? offset[1] : 0);

                boolean newKing = this.type.king || newRow == switch (this.type.color) {
                    case BLACK -> BoardModel.SIZE - 1;
                    case WHITE -> 0;
                };

                return new Move<>(
                    factory.createPiece(
                        this.type.color,
                        newKing,
                        newRow,
                        newCol
                    ),
                    takes
                );
            })
            .filter(move -> pieces.stream().noneMatch(piece ->
                piece.getRow() == move.replacement().getRow() && piece.getCol() == move.replacement().getCol()
            ))
            .filter(move ->
                this.type.king || 0 < (move.replacement().row - this.row) * switch (this.type.color) {
                    case BLACK -> 1;
                    case WHITE -> -1;
                }
            )
            .filter(move -> move.replacement().row >= 0)
            .filter(move -> move.replacement().col >= 0)
            .filter(move -> move.replacement().row < BoardModel.SIZE)
            .filter(move -> move.replacement().col < BoardModel.SIZE)
            .toList();
    }
}
