package com.github.ase.impl.ruleset.checkers;

import com.github.ase.BoardModel;
import com.github.ase.impl.Color;
import com.github.ase.impl.match.history.Turn;
import com.github.ase.impl.ruleset.Ruleset;
import com.github.ase.impl.ruleset.checkers.piece.CheckersPiece;
import com.github.ase.impl.ruleset.checkers.piece.CheckersPieceFactory;

import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CheckersRuleset implements Ruleset<CheckersPiece> {
    private final CheckersPieceFactory pieceFactory;

    public CheckersRuleset(CheckersPieceFactory pieceFactory) {
        this.pieceFactory = pieceFactory;
    }

    @Override
    public Collection<CheckersPiece> getInitialBoard() {
        return Stream.concat(
            IntStream.range(0, BoardModel.SIZE * 3)
                .mapToObj(i -> pieceFactory.createPiece(
                        Color.BLACK,
                        false,
                        i / BoardModel.SIZE,
                        i % BoardModel.SIZE
                    )
                ),
            IntStream.range(BoardModel.SIZE * (BoardModel.SIZE - 3), BoardModel.SIZE * BoardModel.SIZE)
                .mapToObj(i -> pieceFactory.createPiece(
                        Color.WHITE,
                        false,
                        i / BoardModel.SIZE,
                        i % BoardModel.SIZE
                    )
                )
        ).filter(piece -> (piece.getRow() + piece.getCol()) % 2 != 0).toList();
    }

    @Override
    public String getTurnLabel(Turn<CheckersPiece> turn) {
        return turn.getPiece().getCode();
    }
}
