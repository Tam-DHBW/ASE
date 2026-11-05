package com.github.ase.impl.match.history;

import com.github.ase.impl.Color;
import com.github.ase.impl.ruleset.Piece;

import java.util.Collection;
import java.util.List;

public interface TurnHistoryManager<P extends Piece<P>> {
    void addTurn(Piece<P> piece, Piece.Move<P> move, Collection<P> piecesSnapshot);
    List<Turn<P>> getTurns();
}
