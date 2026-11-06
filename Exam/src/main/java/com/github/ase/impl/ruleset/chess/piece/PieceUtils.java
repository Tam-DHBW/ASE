package com.github.ase.impl.ruleset.chess.piece;

import com.github.ase.BoardModel;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

final class PieceUtils {
    private PieceUtils() {}

    public static Collection<ChessPiece> directionalReplacements(
        Collection<ChessPiece> pieces,
        Function<Integer, ChessPiece> offsetFactory
    ) {
        List<ChessPiece> allReplacements = IntStream.range(1, BoardModel.SIZE)
            .mapToObj(offsetFactory::apply)
            .toList();

        Optional<ChessPiece> collision = allReplacements.stream()
            .filter(replacement -> pieces.stream().anyMatch(piece ->
                piece.getRow() == replacement.getRow() && piece.getCol() == replacement.getCol()
            ))
            .findFirst();

        return allReplacements.subList(
            0,
            collision
                .map(piece -> allReplacements.indexOf(piece) + 1)
                .orElse(allReplacements.size())
        );
    }
}
