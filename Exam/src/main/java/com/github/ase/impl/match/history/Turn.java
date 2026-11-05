package com.github.ase.impl.match.history;

import com.github.ase.impl.Color;
import com.github.ase.impl.ruleset.Piece;

import java.util.Collection;

public interface Turn<P extends Piece<P>> {
    Piece<P> getPiece();
    Piece.Move<P> getMove();
    Collection<P> getPiecesSnapshot();
    SelectTurnCommand<P> selectCommand();
}