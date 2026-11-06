package com.github.ase.impl.ruleset.chess;

import com.github.ase.BoardModel;
import com.github.ase.impl.Color;
import com.github.ase.impl.match.history.Turn;
import com.github.ase.impl.ruleset.Ruleset;
import com.github.ase.impl.ruleset.chess.piece.ChessPiece;
import com.github.ase.impl.ruleset.chess.piece.ChessPieceFactory;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ChessRuleset implements Ruleset<ChessPiece> {
    private final ChessPieceFactory pieceFactory;

    public ChessRuleset(ChessPieceFactory pieceFactory) {
        this.pieceFactory = pieceFactory;
    }

    @Override
    public Collection<ChessPiece> getInitialBoard() {
        return Stream.concat(
            this.generateSide(Color.BLACK),
            this.generateSide(Color.WHITE)
        ).toList();
    }

    private Stream<ChessPiece> generateSide(Color color) {
        UnaryOperator<Integer> conditionalFlip = value -> switch (color) {
            case BLACK -> value;
            case WHITE -> BoardModel.SIZE - 1 - value;
        };

        return Stream.of(
            IntStream.range(0, BoardModel.SIZE).mapToObj(col -> this.pieceFactory.createPawn(
                color,
                conditionalFlip.apply(1),
                conditionalFlip.apply(col)
            )),
            Stream.of(
                this.pieceFactory.createRook(color, conditionalFlip.apply(0), 0),
                this.pieceFactory.createRook(color, conditionalFlip.apply(0), BoardModel.SIZE - 1)
            ),
            Stream.of(
                this.pieceFactory.createBishop(color, conditionalFlip.apply(0), 1),
                this.pieceFactory.createBishop(color, conditionalFlip.apply(0), BoardModel.SIZE - 1 - 1)
            ),
            Stream.of(
                this.pieceFactory.createKnight(color, conditionalFlip.apply(0), 2),
                this.pieceFactory.createKnight(color, conditionalFlip.apply(0), BoardModel.SIZE - 1 - 2)
            ),
            Stream.of(
                this.pieceFactory.createQueen(color, conditionalFlip.apply(0), 3),
                this.pieceFactory.createKing(color, conditionalFlip.apply(0), BoardModel.SIZE - 1 - 3)
            )
        ).flatMap(Function.identity());
    }

    @Override
    public String getTurnLabel(Turn<ChessPiece> turn) {
        return "";
    }

    private String formatPosition(int row, int col) {
        return "%c%d".formatted((char) ((int) 'A' + col), BoardModel.SIZE - row);
    }

    @Override
    public String getStatus(Turn<ChessPiece> turn) {
        return "";
    }

    @Override
    public boolean gameEnded(Collection<ChessPiece> pieces) {
        return false;
    }
}
