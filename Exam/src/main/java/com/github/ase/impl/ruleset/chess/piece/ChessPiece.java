package com.github.ase.impl.ruleset.chess.piece;

import com.github.ase.BoardModel;
import com.github.ase.impl.Color;
import com.github.ase.impl.ruleset.Piece;

import java.util.Collection;

public class ChessPiece implements Piece<ChessPiece> {
    private final ChessPieceType type;
    private final int row;
    private final int col;

    protected ChessPiece(ChessPieceType type, int row, int col) {
        this.type = type;
        this.row = row;
        this.col = col;
    }

    @Override
    public String getCode() {
        return switch (this.type.getColor()) {
            case BLACK -> "b";
            case WHITE -> "w";
        } + this.type.getTypeCode();
    }


    @Override
    public Color getColor() {
        return this.type.getColor();
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
    public Collection<Move<ChessPiece>> getValidMoves(Collection<ChessPiece> pieces) {
        return this.type.getPossibleReplacements(pieces, this.getRow(), this.getCol()).stream()
            .filter(replacement -> replacement.getRow() >= 0)
            .filter(replacement -> replacement.getCol() >= 0)
            .filter(replacement -> replacement.getRow() < BoardModel.SIZE)
            .filter(replacement -> replacement.getCol() < BoardModel.SIZE)
            .filter(replacement -> pieces.stream()
                .filter(piece -> piece.getColor() == this.getColor())
                .filter(piece -> piece.getRow() == replacement.getRow())
                .filter(piece -> piece.getCol() == replacement.getCol())
                .findFirst().isEmpty()
            )
            .map(replacement -> new Move<>(
                replacement,
                pieces.stream()
                    .filter(piece -> piece.getColor() != this.getColor())
                    .filter(piece -> piece.getRow() == replacement.getRow())
                    .filter(piece -> piece.getCol() == replacement.getCol())
                    .findFirst()
            ))
            .toList();
    }

}
