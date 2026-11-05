package com.github.ase.impl.ruleset.checkers;

import com.github.ase.BoardModel;
import com.github.ase.impl.Color;
import com.github.ase.impl.match.history.Turn;
import com.github.ase.impl.ruleset.Ruleset;
import com.github.ase.impl.ruleset.checkers.piece.CheckersPiece;
import com.github.ase.impl.ruleset.checkers.piece.CheckersPieceFactory;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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
        return turn.getMove().takes().map(takes -> "%s %s X %s %s".formatted(
            turn.getPiece().getCode(),
            formatPosition(turn.getPiece().getRow(), turn.getPiece().getCol()),
            takes.getCode(),
            formatPosition(takes.getRow(), takes.getCol())
        )).orElseGet(() -> "%s %s -> %s".formatted(
            turn.getPiece().getCode(),
            formatPosition(turn.getPiece().getRow(), turn.getPiece().getCol()),
            formatPosition(turn.getMove().replacement().getRow(), turn.getMove().replacement().getCol())
        ));
    }

    @Override
    public String getStatus(Turn<CheckersPiece> turn) {
        return getWinner(turn.getPiecesSnapshot()).map(winner ->
            "%s has won!".formatted(winner.getNameTitleCase())
        ).orElse("");
    }

    private String formatPosition(int row, int col) {
        return "%c%d".formatted((char) ((int) 'A' + col), BoardModel.SIZE - row);
    }

    @Override
    public boolean gameEnded(Collection<CheckersPiece> pieces) {
        return getWinner(pieces).isPresent();
    }

    private Optional<Color> getWinner(Collection<CheckersPiece> pieces) {
        Set<Color> remainingColors = pieces.stream().map(CheckersPiece::getColor).collect(Collectors.toSet());
        if (remainingColors.size() == 1) return Optional.of(remainingColors.iterator().next());
        else return Optional.empty();
    }
}
