package com.github.ase.impl.ruleset;

import com.github.ase.BoardModel;
import com.github.ase.impl.match.history.Turn;

public class FormatUtils {
    public static String formatTurnLabel(Turn<?> turn) {
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
    private static String formatPosition(int row, int col) {
        return "%c%d".formatted((char) ((int) 'A' + col), BoardModel.SIZE - row);
    }
}
